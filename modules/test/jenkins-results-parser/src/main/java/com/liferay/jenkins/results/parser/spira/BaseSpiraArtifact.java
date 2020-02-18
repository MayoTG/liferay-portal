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

package com.liferay.jenkins.results.parser.spira;

import com.liferay.jenkins.results.parser.JenkinsResultsParserUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.json.JSONObject;

/**
 * @author Michael Hashimoto
 */
public abstract class BaseSpiraArtifact implements SpiraArtifact {

	@Override
	public String getName() {
		return jsonObject.getString("Name");
	}

	@Override
	public SpiraProject getSpiraProject() {
		if (this instanceof SpiraProject) {
			return (SpiraProject)this;
		}

		return SpiraProject.getSpiraProjectByID(jsonObject.getInt("ProjectId"));
	}

	@Override
	public String toString() {
		return jsonObject.toString();
	}

	protected static void addPreviousSearchParameters(
		Class<? extends BaseSpiraArtifact> baseSpiraArtifactClass,
		SearchParameter[] searchParameters) {

		List<SearchParameter[]> previousSearchParameters =
			previousSearchParametersMap.get(baseSpiraArtifactClass);

		if (previousSearchParameters == null) {
			previousSearchParameters = Collections.synchronizedList(
				new ArrayList<>());

			previousSearchParametersMap.put(
				baseSpiraArtifactClass, previousSearchParameters);
		}

		previousSearchParameters.add(searchParameters);
	}

	protected static boolean isPreviousSearch(
		Class<? extends BaseSpiraArtifact> baseSpiraArtifactClass,
		SearchParameter... searchParameters) {

		for (SearchParameter[] previousSearchParameters :
				previousSearchParametersMap.get(baseSpiraArtifactClass)) {

			if (previousSearchParameters.length != searchParameters.length) {
				continue;
			}

			List<SearchParameter> previousSearchParametersList = Arrays.asList(
				previousSearchParameters);

			for (SearchParameter searchParameter : searchParameters) {
				if (!previousSearchParametersList.contains(searchParameter)) {
					break;
				}
			}

			return true;
		}

		return false;
	}

	protected static String toDateString(Calendar calendar) {
		return JenkinsResultsParserUtil.combine(
			"/Date(", String.valueOf(calendar.getTimeInMillis()), ")/");
	}

	protected BaseSpiraArtifact(JSONObject jsonObject) {
		this.jsonObject = jsonObject;
	}

	protected boolean matches(SearchParameter... searchParameters) {
		for (SearchParameter searchParameter : searchParameters) {
			if (!searchParameter.matches(jsonObject)) {
				return false;
			}
		}

		return true;
	}

	protected static final Map
		<Class<? extends BaseSpiraArtifact>, List<SearchParameter[]>>
			previousSearchParametersMap = Collections.synchronizedMap(
				new HashMap<>());

	protected final JSONObject jsonObject;

	protected static class SearchParameter {

		public SearchParameter(String name, Object value) {
			_name = name;
			_value = value;
		}

		@Override
		public boolean equals(Object object) {
			if (object instanceof SearchParameter) {
				SearchParameter otherSearchParameter = (SearchParameter)object;

				if (_name.equals(otherSearchParameter.getName()) &&
					_value.equals(otherSearchParameter.getValue())) {

					return true;
				}

				return false;
			}

			return super.equals(object);
		}

		public String getName() {
			return _name;
		}

		public Object getValue() {
			return _value;
		}

		public boolean matches(JSONObject jsonObject) {
			if (!Objects.equals(getValue(), jsonObject.get(getName()))) {
				return false;
			}

			return true;
		}

		public JSONObject toFilterJSONObject() {
			JSONObject filterJSONObject = new JSONObject();

			filterJSONObject.put("PropertyName", _name);

			if (_value instanceof Integer) {
				Integer intValue = (Integer)_value;

				filterJSONObject.put("IntValue", intValue);
			}
			else if (_value instanceof String) {
				String stringValue = (String)_value;

				stringValue = stringValue.replaceAll("\\[", "[[]");

				filterJSONObject.put("StringValue", stringValue);
			}
			else {
				throw new RuntimeException("Invalid value type");
			}

			return filterJSONObject;
		}

		private final String _name;
		private final Object _value;

	}

}