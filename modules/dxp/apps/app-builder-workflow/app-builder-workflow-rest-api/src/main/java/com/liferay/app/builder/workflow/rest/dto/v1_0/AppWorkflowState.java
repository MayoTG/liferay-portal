/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * The contents of this file are subject to the terms of the Liferay Enterprise
 * Subscription License ("License"). You may not use this file except in
 * compliance with the License. You can obtain a copy of the License by
 * contacting Liferay, Inc. See the License for the specific language governing
 * permissions and limitations under the License, including but not limited to
 * distribution rights of the Software.
 *
 *
 *
 */

package com.liferay.app.builder.workflow.rest.dto.v1_0;

import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import com.liferay.petra.function.UnsafeSupplier;
import com.liferay.petra.string.StringBundler;
import com.liferay.portal.vulcan.graphql.annotation.GraphQLField;
import com.liferay.portal.vulcan.graphql.annotation.GraphQLName;
import com.liferay.portal.vulcan.util.ObjectMapperUtil;

import io.swagger.v3.oas.annotations.media.Schema;

import java.io.Serializable;

import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import javax.annotation.Generated;

import javax.validation.Valid;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author Rafael Praxedes
 * @generated
 */
@Generated("")
@GraphQLName("AppWorkflowState")
@JsonFilter("Liferay.Vulcan")
@XmlRootElement(name = "AppWorkflowState")
public class AppWorkflowState implements Serializable {

	public static AppWorkflowState toDTO(String json) {
		return ObjectMapperUtil.readValue(AppWorkflowState.class, json);
	}

	@Schema
	@Valid
	public AppWorkflowTransition[] getAppWorkflowTransitions() {
		return appWorkflowTransitions;
	}

	public void setAppWorkflowTransitions(
		AppWorkflowTransition[] appWorkflowTransitions) {

		this.appWorkflowTransitions = appWorkflowTransitions;
	}

	@JsonIgnore
	public void setAppWorkflowTransitions(
		UnsafeSupplier<AppWorkflowTransition[], Exception>
			appWorkflowTransitionsUnsafeSupplier) {

		try {
			appWorkflowTransitions = appWorkflowTransitionsUnsafeSupplier.get();
		}
		catch (RuntimeException re) {
			throw re;
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@GraphQLField
	@JsonProperty(access = JsonProperty.Access.READ_WRITE)
	protected AppWorkflowTransition[] appWorkflowTransitions;

	@Schema
	public Boolean getInitial() {
		return initial;
	}

	public void setInitial(Boolean initial) {
		this.initial = initial;
	}

	@JsonIgnore
	public void setInitial(
		UnsafeSupplier<Boolean, Exception> initialUnsafeSupplier) {

		try {
			initial = initialUnsafeSupplier.get();
		}
		catch (RuntimeException re) {
			throw re;
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@GraphQLField
	@JsonProperty(access = JsonProperty.Access.READ_WRITE)
	protected Boolean initial;

	@Schema
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@JsonIgnore
	public void setName(UnsafeSupplier<String, Exception> nameUnsafeSupplier) {
		try {
			name = nameUnsafeSupplier.get();
		}
		catch (RuntimeException re) {
			throw re;
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@GraphQLField
	@JsonProperty(access = JsonProperty.Access.READ_WRITE)
	protected String name;

	@Override
	public boolean equals(Object object) {
		if (this == object) {
			return true;
		}

		if (!(object instanceof AppWorkflowState)) {
			return false;
		}

		AppWorkflowState appWorkflowState = (AppWorkflowState)object;

		return Objects.equals(toString(), appWorkflowState.toString());
	}

	@Override
	public int hashCode() {
		String string = toString();

		return string.hashCode();
	}

	public String toString() {
		StringBundler sb = new StringBundler();

		sb.append("{");

		if (appWorkflowTransitions != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"appWorkflowTransitions\": ");

			sb.append("[");

			for (int i = 0; i < appWorkflowTransitions.length; i++) {
				sb.append(String.valueOf(appWorkflowTransitions[i]));

				if ((i + 1) < appWorkflowTransitions.length) {
					sb.append(", ");
				}
			}

			sb.append("]");
		}

		if (initial != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"initial\": ");

			sb.append(initial);
		}

		if (name != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"name\": ");

			sb.append("\"");

			sb.append(_escape(name));

			sb.append("\"");
		}

		sb.append("}");

		return sb.toString();
	}

	@Schema(
		accessMode = Schema.AccessMode.READ_ONLY,
		defaultValue = "com.liferay.app.builder.workflow.rest.dto.v1_0.AppWorkflowState",
		name = "x-class-name"
	)
	public String xClassName;

	private static String _escape(Object object) {
		String string = String.valueOf(object);

		return string.replaceAll("\"", "\\\\\"");
	}

	private static boolean _isArray(Object value) {
		if (value == null) {
			return false;
		}

		Class<?> clazz = value.getClass();

		return clazz.isArray();
	}

	private static String _toJSON(Map<String, ?> map) {
		StringBuilder sb = new StringBuilder("{");

		@SuppressWarnings("unchecked")
		Set set = map.entrySet();

		@SuppressWarnings("unchecked")
		Iterator<Map.Entry<String, ?>> iterator = set.iterator();

		while (iterator.hasNext()) {
			Map.Entry<String, ?> entry = iterator.next();

			sb.append("\"");
			sb.append(entry.getKey());
			sb.append("\": ");

			Object value = entry.getValue();

			if (_isArray(value)) {
				sb.append("[");

				Object[] valueArray = (Object[])value;

				for (int i = 0; i < valueArray.length; i++) {
					if (valueArray[i] instanceof String) {
						sb.append("\"");
						sb.append(valueArray[i]);
						sb.append("\"");
					}
					else {
						sb.append(valueArray[i]);
					}

					if ((i + 1) < valueArray.length) {
						sb.append(", ");
					}
				}

				sb.append("]");
			}
			else if (value instanceof Map) {
				sb.append(_toJSON((Map<String, ?>)value));
			}
			else if (value instanceof String) {
				sb.append("\"");
				sb.append(value);
				sb.append("\"");
			}
			else {
				sb.append(value);
			}

			if (iterator.hasNext()) {
				sb.append(", ");
			}
		}

		sb.append("}");

		return sb.toString();
	}

}