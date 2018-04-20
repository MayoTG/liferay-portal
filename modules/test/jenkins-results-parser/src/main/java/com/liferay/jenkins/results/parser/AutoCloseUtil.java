/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */

package com.liferay.jenkins.results.parser;

/**
 * @author Peter Yoo
 */
public class AutoCloseUtil {

	public boolean autoCloseOnCriticalBatchFailures(Build topLevelBuild) throws Exception {
		String autoCloseCommentAvailable = project.getProperty("auto.close.comment.available");

		if (autoCloseCommentAvailable.equals("true")) {
			return false;
		}

		String githubReceiverUsername = project.getProperty("env.GITHUB_RECEIVER_USERNAME");
		String githubSenderUsername = project.getProperty("env.GITHUB_SENDER_USERNAME");

		if ((githubReceiverUsername == null) || (githubSenderUsername == null) || githubReceiverUsername.equals(githubSenderUsername)) {
			return false;
		}

		List rules = getAutoCloseRules();

		for (AutoCloseRule rule : rules) {
			List downstreamBuilds = topLevelBuild.getDownstreamBuilds(null);

			List failedDownstreamBuilds = rule.evaluate(downstreamBuilds);

			if (failedDownstreamBuilds.isEmpty()) {
				continue;
			}

			String repository = project.getProperty("repository");

			Map attributes = new HashMap();

			attributes.put("pull.request.number", project.getProperty("env.GITHUB_PULL_REQUEST_NUMBER"));
			attributes.put("repository", repository);
			attributes.put("username", project.getProperty("env.GITHUB_RECEIVER_USERNAME"));

			runTask("close-github-pull-request", attributes);

			StringBuilder sb = new StringBuilder();

			sb.append("<h1>The pull request tester is still running.</h1><p>Please wait until you get the <i><b>final report</b></i> before running 'ci:retest'.</p><p>See this link to check on the status of your test:</p>");

			sb.append("<ul><li><a href=\"");
			sb.append(project.getProperty("env.BUILD_URL"));
			sb.append("\">");
			sb.append(topLevelBuild.getJobName());
			sb.append("</a></li></ul><p>@");
			sb.append(project.getProperty("github.sender.username"));
			sb.append("</p><hr />");

			sb.append("<h1>However, the pull request was closed.</h1><p>The pull request was closed because the following critical batches had failed:</p><ul>");

			String failureBuildURL = "";

			for (Build failedDownstreamBuild : failedDownstreamBuilds) {
				failureBuildURL = failedDownstreamBuild.getBuildURL();

				sb.append("<li><a href=\"");
				sb.append(failureBuildURL);
				sb.append("\">");
				sb.append(getBatchName(failedDownstreamBuild));
				sb.append("</a></li>");
			}

			sb.append("</ul><p>For information as to why we automatically close out certain pull requests see this <a href=\"https://in.liferay.com/web/global.engineering/wiki/-/wiki/Quality+Assurance+Main/Test+Batch+Automatic+Close+List\">article</a>.</p>");
			sb.append("<p auto-close=\"false\"><strong><em>*This pull will no longer automatically close if this comment is available. If you believe this is a mistake please re-open this pull by entering the following command as a comment.</em></strong></p><pre>ci&#58;reopen</pre><hr /><h3>Critical Failure Details:</h3>");

			JenkinsResultsParserUtil.setBuildProperties(project.getProperties());

			for (Build failedDownstreamBuild : failedDownstreamBuilds) {
				try {
					sb.append(Dom4JUtil.format(failedDownstreamBuild.getGitHubMessageElement(), false));
				}
				catch (Exception e) {
					e.printStackTrace();

					throw e;
				}
			}

			attributes.put("comment.body", sb.toString());

			runTask("post-github-comment", attributes);

			return true;
		}

		return false;
	}

	public boolean autoCloseOnCriticalTestFailures(Build topLevelBuild) throws Exception {
		String autoCloseCommentAvailable = project.getProperty("auto.close.comment.available");

		if (autoCloseCommentAvailable.equals("true") || !isAutoCloseOnCriticalTestFailuresActive()) {
			return false;
		}

		String githubReceiverUsername = project.getProperty("env.GITHUB_RECEIVER_USERNAME");
		String githubSenderUsername = project.getProperty("env.GITHUB_SENDER_USERNAME");

		if ((githubReceiverUsername == null) || (githubSenderUsername == null) || githubReceiverUsername.equals(githubSenderUsername)) {
			return false;
		}

		String failedBuildURL = "";
		Build failedDownstreamBuild = null;
		List jenkinsJobFailureURLs = new ArrayList();

		List downstreamBuilds = topLevelBuild.getDownstreamBuilds(null);

		for (Build downstreamBuild : downstreamBuilds) {
			String batchName = getBatchName(downstreamBuild);

			if (batchName == null) {
				continue;
			}

			if (!batchName.contains("integration") && !batchName.contains("unit")) {
				continue;
			}

			String status = downstreamBuild.getStatus();

			if (!status.equals("completed")) {
				continue;
			}

			String result = downstreamBuild.getResult();

			if ((result == null) || !result.equals("UNSTABLE")) {
				continue;
			}

			String buildURL = JenkinsResultsParserUtil.getLocalURL(downstreamBuild.getBuildURL());

			String subrepositoryPackageNames = project.getProperty("subrepository.package.names");

			if (subrepositoryPackageNames != null) {
				for (String subrepositoryPackageName : subrepositoryPackageNames.split(",")) {
					if (!jenkinsJobFailureURLs.isEmpty()) {
						break;
					}

					List testResults = new ArrayList();

					testResults.addAll(downstreamBuild.getTestResults("FAILED"));
					testResults.addAll(downstreamBuild.getTestResults("REGRESSION"));

					for (TestResult testResult : testResults) {
						if (UpstreamFailureUtil.isTestFailingInUpstreamJob(testResult)) {
							continue;
						}

						String packageName = testResult.getPackageName();

						if (subrepositoryPackageName.equals(packageName)) {
							failedDownstreamBuild = downstreamBuild;

							StringBuilder sb = new StringBuilder();

							sb.append("<a href=\"");
							sb.append(testResult.getTestReportURL());
							sb.append("\">");
							sb.append(testResult.getClassName());
							sb.append("</a>");

							jenkinsJobFailureURLs.add(sb.toString());
						}
					}
				}
			}
		}

		if (!jenkinsJobFailureURLs.isEmpty()) {
			Map attributes = new HashMap();

			attributes.put("pull.request.number", project.getProperty("env.GITHUB_PULL_REQUEST_NUMBER"));
			attributes.put("repository", project.getProperty("repository"));
			attributes.put("username", project.getProperty("env.GITHUB_RECEIVER_USERNAME"));

			runTask("close-github-pull-request", attributes);

			StringBuilder sb = new StringBuilder();

			sb.append("<h1>The pull request tester is still running.</h1><p>Please wait until you get the <i><b>final report</b></i> before running 'ci:retest'.</p><p>See this link to check on the status of your test:</p>");

			sb.append("<ul><li><a href=\"");
			sb.append(project.getProperty("env.BUILD_URL"));
			sb.append("\">");
			sb.append(topLevelBuild.getJobName());
			sb.append("</a></li></ul>@");
			sb.append(project.getProperty("github.sender.username"));
			sb.append("</p><hr />");

			sb.append("<h1>However, the pull request was closed.</h1><p>The pull request was closed due to the following integration/unit test failures:</p><ul>");

			for (String jenkinsJobFailureURL : jenkinsJobFailureURLs) {
				sb.append("<li>");
				sb.append(jenkinsJobFailureURL);
				sb.append("</li>");
			};

			sb.append("</ul><p>These test failures are a part of a 'module group'/'subrepository' that was changed in this pull request.</p>");
			sb.append("<p auto-close=\"false\"><strong><em>*This pull will no longer automatically close if this comment is available. If you believe this is a mistake please re-open this pull by entering the following command as a comment.</em></strong></p><pre>ci&#58;reopen</pre><hr /><h3>Critical Failure Details:</h3>");

			JenkinsResultsParserUtil.setBuildProperties(project.getProperties());

			try {
				sb.append(Dom4JUtil.format(failedDownstreamBuild.getGitHubMessageElement(), false));
			}
			catch (Exception e) {
				e.printStackTrace();

				throw e;
			}

			attributes.put("comment.body", sb.toString());

			runTask("post-github-comment", attributes);

			return true;
		}

		return false;
	}

	public List getAutoCloseRules() throws Exception {
		List list = new ArrayList();

		String propertyNameTemplate = "test.batch.names.auto.close[" + project.getProperty("repository") +"?]";

		String repositoryBranchAutoClosePropertyName = propertyNameTemplate.replace("?", "-" + project.getProperty("branch.name"));

		String testBatchNamesAutoClose = project.getProperty(repositoryBranchAutoClosePropertyName);

		if (testBatchNamesAutoClose == null) {
			String repositoryAutoClosePropertyName = propertyNameTemplate.replace("?", "");

			testBatchNamesAutoClose = project.getProperty(repositoryAutoClosePropertyName);
		}

		if (testBatchNamesAutoClose != null) {
			String[] autoCloseRuleDataArray = StringUtils.split(testBatchNamesAutoClose, ",");

			for (String autoCloseRuleData : autoCloseRuleDataArray) {
				list.add(new AutoCloseRule(autoCloseRuleData));
			}
		}

		return list;
	}
}