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

package com.liferay.commerce.bom.model;

import com.liferay.portal.kernel.annotation.ImplementationClassName;
import com.liferay.portal.kernel.model.PersistedModel;
import com.liferay.portal.kernel.util.Accessor;

import org.osgi.annotation.versioning.ProviderType;

/**
 * The extended model interface for the CommerceBOMDefinition service. Represents a row in the &quot;CommerceBOMDefinition&quot; database table, with each column mapped to a property of this class.
 *
 * @author Luca Pellizzon
 * @see CommerceBOMDefinitionModel
 * @generated
 */
@ImplementationClassName(
	"com.liferay.commerce.bom.model.impl.CommerceBOMDefinitionImpl"
)
@ProviderType
public interface CommerceBOMDefinition
	extends CommerceBOMDefinitionModel, PersistedModel {

	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify this interface directly. Add methods to <code>com.liferay.commerce.bom.model.impl.CommerceBOMDefinitionImpl</code> and rerun ServiceBuilder to automatically copy the method declarations to this interface.
	 */
	public static final Accessor<CommerceBOMDefinition, Long>
		COMMERCE_BOM_DEFINITION_ID_ACCESSOR =
			new Accessor<CommerceBOMDefinition, Long>() {

				@Override
				public Long get(CommerceBOMDefinition commerceBOMDefinition) {
					return commerceBOMDefinition.getCommerceBOMDefinitionId();
				}

				@Override
				public Class<Long> getAttributeClass() {
					return Long.class;
				}

				@Override
				public Class<CommerceBOMDefinition> getTypeClass() {
					return CommerceBOMDefinition.class;
				}

			};

	public CommerceBOMFolder fetchCommerceBOMFolder();

	public com.liferay.commerce.product.model.CPAttachmentFileEntry
		fetchCPAttachmentFileEntry();

}