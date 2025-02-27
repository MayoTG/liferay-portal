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

package com.liferay.powwow.service.base;

import com.liferay.portal.kernel.bean.BeanReference;
import com.liferay.portal.kernel.dao.db.DB;
import com.liferay.portal.kernel.dao.db.DBManagerUtil;
import com.liferay.portal.kernel.dao.jdbc.SqlUpdate;
import com.liferay.portal.kernel.dao.jdbc.SqlUpdateFactoryUtil;
import com.liferay.portal.kernel.dao.orm.ActionableDynamicQuery;
import com.liferay.portal.kernel.dao.orm.DefaultActionableDynamicQuery;
import com.liferay.portal.kernel.dao.orm.DynamicQuery;
import com.liferay.portal.kernel.dao.orm.DynamicQueryFactoryUtil;
import com.liferay.portal.kernel.dao.orm.IndexableActionableDynamicQuery;
import com.liferay.portal.kernel.dao.orm.Projection;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.model.PersistedModel;
import com.liferay.portal.kernel.module.framework.service.IdentifiableOSGiService;
import com.liferay.portal.kernel.search.Indexable;
import com.liferay.portal.kernel.search.IndexableType;
import com.liferay.portal.kernel.service.BaseLocalServiceImpl;
import com.liferay.portal.kernel.service.PersistedModelLocalServiceRegistryUtil;
import com.liferay.portal.kernel.service.persistence.BasePersistence;
import com.liferay.portal.kernel.service.persistence.ClassNamePersistence;
import com.liferay.portal.kernel.service.persistence.UserPersistence;
import com.liferay.portal.kernel.transaction.Transactional;
import com.liferay.portal.kernel.util.OrderByComparator;
import com.liferay.portal.kernel.util.PortalUtil;
import com.liferay.powwow.model.PowwowMeeting;
import com.liferay.powwow.service.PowwowMeetingLocalService;
import com.liferay.powwow.service.PowwowMeetingLocalServiceUtil;
import com.liferay.powwow.service.persistence.PowwowMeetingFinder;
import com.liferay.powwow.service.persistence.PowwowMeetingPersistence;
import com.liferay.powwow.service.persistence.PowwowParticipantPersistence;
import com.liferay.powwow.service.persistence.PowwowServerPersistence;

import java.io.Serializable;

import java.lang.reflect.Field;

import java.util.List;

import javax.sql.DataSource;

/**
 * Provides the base implementation for the powwow meeting local service.
 *
 * <p>
 * This implementation exists only as a container for the default service methods generated by ServiceBuilder. All custom service methods should be put in {@link com.liferay.powwow.service.impl.PowwowMeetingLocalServiceImpl}.
 * </p>
 *
 * @author Shinn Lok
 * @see com.liferay.powwow.service.impl.PowwowMeetingLocalServiceImpl
 * @generated
 */
public abstract class PowwowMeetingLocalServiceBaseImpl
	extends BaseLocalServiceImpl
	implements IdentifiableOSGiService, PowwowMeetingLocalService {

	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify or reference this class directly. Use <code>PowwowMeetingLocalService</code> via injection or a <code>org.osgi.util.tracker.ServiceTracker</code> or use <code>PowwowMeetingLocalServiceUtil</code>.
	 */

	/**
	 * Adds the powwow meeting to the database. Also notifies the appropriate model listeners.
	 *
	 * <p>
	 * <strong>Important:</strong> Inspect PowwowMeetingLocalServiceImpl for overloaded versions of the method. If provided, use these entry points to the API, as the implementation logic may require the additional parameters defined there.
	 * </p>
	 *
	 * @param powwowMeeting the powwow meeting
	 * @return the powwow meeting that was added
	 */
	@Indexable(type = IndexableType.REINDEX)
	@Override
	public PowwowMeeting addPowwowMeeting(PowwowMeeting powwowMeeting) {
		powwowMeeting.setNew(true);

		return powwowMeetingPersistence.update(powwowMeeting);
	}

	/**
	 * Creates a new powwow meeting with the primary key. Does not add the powwow meeting to the database.
	 *
	 * @param powwowMeetingId the primary key for the new powwow meeting
	 * @return the new powwow meeting
	 */
	@Override
	@Transactional(enabled = false)
	public PowwowMeeting createPowwowMeeting(long powwowMeetingId) {
		return powwowMeetingPersistence.create(powwowMeetingId);
	}

	/**
	 * Deletes the powwow meeting with the primary key from the database. Also notifies the appropriate model listeners.
	 *
	 * <p>
	 * <strong>Important:</strong> Inspect PowwowMeetingLocalServiceImpl for overloaded versions of the method. If provided, use these entry points to the API, as the implementation logic may require the additional parameters defined there.
	 * </p>
	 *
	 * @param powwowMeetingId the primary key of the powwow meeting
	 * @return the powwow meeting that was removed
	 * @throws PortalException if a powwow meeting with the primary key could not be found
	 */
	@Indexable(type = IndexableType.DELETE)
	@Override
	public PowwowMeeting deletePowwowMeeting(long powwowMeetingId)
		throws PortalException {

		return powwowMeetingPersistence.remove(powwowMeetingId);
	}

	/**
	 * Deletes the powwow meeting from the database. Also notifies the appropriate model listeners.
	 *
	 * <p>
	 * <strong>Important:</strong> Inspect PowwowMeetingLocalServiceImpl for overloaded versions of the method. If provided, use these entry points to the API, as the implementation logic may require the additional parameters defined there.
	 * </p>
	 *
	 * @param powwowMeeting the powwow meeting
	 * @return the powwow meeting that was removed
	 * @throws PortalException
	 */
	@Indexable(type = IndexableType.DELETE)
	@Override
	public PowwowMeeting deletePowwowMeeting(PowwowMeeting powwowMeeting)
		throws PortalException {

		return powwowMeetingPersistence.remove(powwowMeeting);
	}

	@Override
	public DynamicQuery dynamicQuery() {
		Class<?> clazz = getClass();

		return DynamicQueryFactoryUtil.forClass(
			PowwowMeeting.class, clazz.getClassLoader());
	}

	/**
	 * Performs a dynamic query on the database and returns the matching rows.
	 *
	 * @param dynamicQuery the dynamic query
	 * @return the matching rows
	 */
	@Override
	public <T> List<T> dynamicQuery(DynamicQuery dynamicQuery) {
		return powwowMeetingPersistence.findWithDynamicQuery(dynamicQuery);
	}

	/**
	 * Performs a dynamic query on the database and returns a range of the matching rows.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>com.liferay.powwow.model.impl.PowwowMeetingModelImpl</code>.
	 * </p>
	 *
	 * @param dynamicQuery the dynamic query
	 * @param start the lower bound of the range of model instances
	 * @param end the upper bound of the range of model instances (not inclusive)
	 * @return the range of matching rows
	 */
	@Override
	public <T> List<T> dynamicQuery(
		DynamicQuery dynamicQuery, int start, int end) {

		return powwowMeetingPersistence.findWithDynamicQuery(
			dynamicQuery, start, end);
	}

	/**
	 * Performs a dynamic query on the database and returns an ordered range of the matching rows.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>com.liferay.powwow.model.impl.PowwowMeetingModelImpl</code>.
	 * </p>
	 *
	 * @param dynamicQuery the dynamic query
	 * @param start the lower bound of the range of model instances
	 * @param end the upper bound of the range of model instances (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of matching rows
	 */
	@Override
	public <T> List<T> dynamicQuery(
		DynamicQuery dynamicQuery, int start, int end,
		OrderByComparator<T> orderByComparator) {

		return powwowMeetingPersistence.findWithDynamicQuery(
			dynamicQuery, start, end, orderByComparator);
	}

	/**
	 * Returns the number of rows matching the dynamic query.
	 *
	 * @param dynamicQuery the dynamic query
	 * @return the number of rows matching the dynamic query
	 */
	@Override
	public long dynamicQueryCount(DynamicQuery dynamicQuery) {
		return powwowMeetingPersistence.countWithDynamicQuery(dynamicQuery);
	}

	/**
	 * Returns the number of rows matching the dynamic query.
	 *
	 * @param dynamicQuery the dynamic query
	 * @param projection the projection to apply to the query
	 * @return the number of rows matching the dynamic query
	 */
	@Override
	public long dynamicQueryCount(
		DynamicQuery dynamicQuery, Projection projection) {

		return powwowMeetingPersistence.countWithDynamicQuery(
			dynamicQuery, projection);
	}

	@Override
	public PowwowMeeting fetchPowwowMeeting(long powwowMeetingId) {
		return powwowMeetingPersistence.fetchByPrimaryKey(powwowMeetingId);
	}

	/**
	 * Returns the powwow meeting with the primary key.
	 *
	 * @param powwowMeetingId the primary key of the powwow meeting
	 * @return the powwow meeting
	 * @throws PortalException if a powwow meeting with the primary key could not be found
	 */
	@Override
	public PowwowMeeting getPowwowMeeting(long powwowMeetingId)
		throws PortalException {

		return powwowMeetingPersistence.findByPrimaryKey(powwowMeetingId);
	}

	@Override
	public ActionableDynamicQuery getActionableDynamicQuery() {
		ActionableDynamicQuery actionableDynamicQuery =
			new DefaultActionableDynamicQuery();

		actionableDynamicQuery.setBaseLocalService(powwowMeetingLocalService);
		actionableDynamicQuery.setClassLoader(getClassLoader());
		actionableDynamicQuery.setModelClass(PowwowMeeting.class);

		actionableDynamicQuery.setPrimaryKeyPropertyName("powwowMeetingId");

		return actionableDynamicQuery;
	}

	@Override
	public IndexableActionableDynamicQuery
		getIndexableActionableDynamicQuery() {

		IndexableActionableDynamicQuery indexableActionableDynamicQuery =
			new IndexableActionableDynamicQuery();

		indexableActionableDynamicQuery.setBaseLocalService(
			powwowMeetingLocalService);
		indexableActionableDynamicQuery.setClassLoader(getClassLoader());
		indexableActionableDynamicQuery.setModelClass(PowwowMeeting.class);

		indexableActionableDynamicQuery.setPrimaryKeyPropertyName(
			"powwowMeetingId");

		return indexableActionableDynamicQuery;
	}

	protected void initActionableDynamicQuery(
		ActionableDynamicQuery actionableDynamicQuery) {

		actionableDynamicQuery.setBaseLocalService(powwowMeetingLocalService);
		actionableDynamicQuery.setClassLoader(getClassLoader());
		actionableDynamicQuery.setModelClass(PowwowMeeting.class);

		actionableDynamicQuery.setPrimaryKeyPropertyName("powwowMeetingId");
	}

	/**
	 * @throws PortalException
	 */
	public PersistedModel createPersistedModel(Serializable primaryKeyObj)
		throws PortalException {

		return powwowMeetingPersistence.create(
			((Long)primaryKeyObj).longValue());
	}

	/**
	 * @throws PortalException
	 */
	@Override
	public PersistedModel deletePersistedModel(PersistedModel persistedModel)
		throws PortalException {

		return powwowMeetingLocalService.deletePowwowMeeting(
			(PowwowMeeting)persistedModel);
	}

	public BasePersistence<PowwowMeeting> getBasePersistence() {
		return powwowMeetingPersistence;
	}

	/**
	 * @throws PortalException
	 */
	@Override
	public PersistedModel getPersistedModel(Serializable primaryKeyObj)
		throws PortalException {

		return powwowMeetingPersistence.findByPrimaryKey(primaryKeyObj);
	}

	/**
	 * Returns a range of all the powwow meetings.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>com.liferay.powwow.model.impl.PowwowMeetingModelImpl</code>.
	 * </p>
	 *
	 * @param start the lower bound of the range of powwow meetings
	 * @param end the upper bound of the range of powwow meetings (not inclusive)
	 * @return the range of powwow meetings
	 */
	@Override
	public List<PowwowMeeting> getPowwowMeetings(int start, int end) {
		return powwowMeetingPersistence.findAll(start, end);
	}

	/**
	 * Returns the number of powwow meetings.
	 *
	 * @return the number of powwow meetings
	 */
	@Override
	public int getPowwowMeetingsCount() {
		return powwowMeetingPersistence.countAll();
	}

	/**
	 * Updates the powwow meeting in the database or adds it if it does not yet exist. Also notifies the appropriate model listeners.
	 *
	 * <p>
	 * <strong>Important:</strong> Inspect PowwowMeetingLocalServiceImpl for overloaded versions of the method. If provided, use these entry points to the API, as the implementation logic may require the additional parameters defined there.
	 * </p>
	 *
	 * @param powwowMeeting the powwow meeting
	 * @return the powwow meeting that was updated
	 */
	@Indexable(type = IndexableType.REINDEX)
	@Override
	public PowwowMeeting updatePowwowMeeting(PowwowMeeting powwowMeeting) {
		return powwowMeetingPersistence.update(powwowMeeting);
	}

	/**
	 * Returns the powwow meeting local service.
	 *
	 * @return the powwow meeting local service
	 */
	public PowwowMeetingLocalService getPowwowMeetingLocalService() {
		return powwowMeetingLocalService;
	}

	/**
	 * Sets the powwow meeting local service.
	 *
	 * @param powwowMeetingLocalService the powwow meeting local service
	 */
	public void setPowwowMeetingLocalService(
		PowwowMeetingLocalService powwowMeetingLocalService) {

		this.powwowMeetingLocalService = powwowMeetingLocalService;
	}

	/**
	 * Returns the powwow meeting persistence.
	 *
	 * @return the powwow meeting persistence
	 */
	public PowwowMeetingPersistence getPowwowMeetingPersistence() {
		return powwowMeetingPersistence;
	}

	/**
	 * Sets the powwow meeting persistence.
	 *
	 * @param powwowMeetingPersistence the powwow meeting persistence
	 */
	public void setPowwowMeetingPersistence(
		PowwowMeetingPersistence powwowMeetingPersistence) {

		this.powwowMeetingPersistence = powwowMeetingPersistence;
	}

	/**
	 * Returns the powwow meeting finder.
	 *
	 * @return the powwow meeting finder
	 */
	public PowwowMeetingFinder getPowwowMeetingFinder() {
		return powwowMeetingFinder;
	}

	/**
	 * Sets the powwow meeting finder.
	 *
	 * @param powwowMeetingFinder the powwow meeting finder
	 */
	public void setPowwowMeetingFinder(
		PowwowMeetingFinder powwowMeetingFinder) {

		this.powwowMeetingFinder = powwowMeetingFinder;
	}

	/**
	 * Returns the powwow participant local service.
	 *
	 * @return the powwow participant local service
	 */
	public com.liferay.powwow.service.PowwowParticipantLocalService
		getPowwowParticipantLocalService() {

		return powwowParticipantLocalService;
	}

	/**
	 * Sets the powwow participant local service.
	 *
	 * @param powwowParticipantLocalService the powwow participant local service
	 */
	public void setPowwowParticipantLocalService(
		com.liferay.powwow.service.PowwowParticipantLocalService
			powwowParticipantLocalService) {

		this.powwowParticipantLocalService = powwowParticipantLocalService;
	}

	/**
	 * Returns the powwow participant persistence.
	 *
	 * @return the powwow participant persistence
	 */
	public PowwowParticipantPersistence getPowwowParticipantPersistence() {
		return powwowParticipantPersistence;
	}

	/**
	 * Sets the powwow participant persistence.
	 *
	 * @param powwowParticipantPersistence the powwow participant persistence
	 */
	public void setPowwowParticipantPersistence(
		PowwowParticipantPersistence powwowParticipantPersistence) {

		this.powwowParticipantPersistence = powwowParticipantPersistence;
	}

	/**
	 * Returns the powwow server local service.
	 *
	 * @return the powwow server local service
	 */
	public com.liferay.powwow.service.PowwowServerLocalService
		getPowwowServerLocalService() {

		return powwowServerLocalService;
	}

	/**
	 * Sets the powwow server local service.
	 *
	 * @param powwowServerLocalService the powwow server local service
	 */
	public void setPowwowServerLocalService(
		com.liferay.powwow.service.PowwowServerLocalService
			powwowServerLocalService) {

		this.powwowServerLocalService = powwowServerLocalService;
	}

	/**
	 * Returns the powwow server persistence.
	 *
	 * @return the powwow server persistence
	 */
	public PowwowServerPersistence getPowwowServerPersistence() {
		return powwowServerPersistence;
	}

	/**
	 * Sets the powwow server persistence.
	 *
	 * @param powwowServerPersistence the powwow server persistence
	 */
	public void setPowwowServerPersistence(
		PowwowServerPersistence powwowServerPersistence) {

		this.powwowServerPersistence = powwowServerPersistence;
	}

	/**
	 * Returns the counter local service.
	 *
	 * @return the counter local service
	 */
	public com.liferay.counter.kernel.service.CounterLocalService
		getCounterLocalService() {

		return counterLocalService;
	}

	/**
	 * Sets the counter local service.
	 *
	 * @param counterLocalService the counter local service
	 */
	public void setCounterLocalService(
		com.liferay.counter.kernel.service.CounterLocalService
			counterLocalService) {

		this.counterLocalService = counterLocalService;
	}

	/**
	 * Returns the class name local service.
	 *
	 * @return the class name local service
	 */
	public com.liferay.portal.kernel.service.ClassNameLocalService
		getClassNameLocalService() {

		return classNameLocalService;
	}

	/**
	 * Sets the class name local service.
	 *
	 * @param classNameLocalService the class name local service
	 */
	public void setClassNameLocalService(
		com.liferay.portal.kernel.service.ClassNameLocalService
			classNameLocalService) {

		this.classNameLocalService = classNameLocalService;
	}

	/**
	 * Returns the class name persistence.
	 *
	 * @return the class name persistence
	 */
	public ClassNamePersistence getClassNamePersistence() {
		return classNamePersistence;
	}

	/**
	 * Sets the class name persistence.
	 *
	 * @param classNamePersistence the class name persistence
	 */
	public void setClassNamePersistence(
		ClassNamePersistence classNamePersistence) {

		this.classNamePersistence = classNamePersistence;
	}

	/**
	 * Returns the resource local service.
	 *
	 * @return the resource local service
	 */
	public com.liferay.portal.kernel.service.ResourceLocalService
		getResourceLocalService() {

		return resourceLocalService;
	}

	/**
	 * Sets the resource local service.
	 *
	 * @param resourceLocalService the resource local service
	 */
	public void setResourceLocalService(
		com.liferay.portal.kernel.service.ResourceLocalService
			resourceLocalService) {

		this.resourceLocalService = resourceLocalService;
	}

	/**
	 * Returns the user local service.
	 *
	 * @return the user local service
	 */
	public com.liferay.portal.kernel.service.UserLocalService
		getUserLocalService() {

		return userLocalService;
	}

	/**
	 * Sets the user local service.
	 *
	 * @param userLocalService the user local service
	 */
	public void setUserLocalService(
		com.liferay.portal.kernel.service.UserLocalService userLocalService) {

		this.userLocalService = userLocalService;
	}

	/**
	 * Returns the user persistence.
	 *
	 * @return the user persistence
	 */
	public UserPersistence getUserPersistence() {
		return userPersistence;
	}

	/**
	 * Sets the user persistence.
	 *
	 * @param userPersistence the user persistence
	 */
	public void setUserPersistence(UserPersistence userPersistence) {
		this.userPersistence = userPersistence;
	}

	public void afterPropertiesSet() {
		PersistedModelLocalServiceRegistryUtil.register(
			"com.liferay.powwow.model.PowwowMeeting",
			powwowMeetingLocalService);

		_setLocalServiceUtilService(powwowMeetingLocalService);
	}

	public void destroy() {
		PersistedModelLocalServiceRegistryUtil.unregister(
			"com.liferay.powwow.model.PowwowMeeting");

		_setLocalServiceUtilService(null);
	}

	/**
	 * Returns the OSGi service identifier.
	 *
	 * @return the OSGi service identifier
	 */
	@Override
	public String getOSGiServiceIdentifier() {
		return PowwowMeetingLocalService.class.getName();
	}

	protected Class<?> getModelClass() {
		return PowwowMeeting.class;
	}

	protected String getModelClassName() {
		return PowwowMeeting.class.getName();
	}

	/**
	 * Performs a SQL query.
	 *
	 * @param sql the sql query
	 */
	protected void runSQL(String sql) {
		try {
			DataSource dataSource = powwowMeetingPersistence.getDataSource();

			DB db = DBManagerUtil.getDB();

			sql = db.buildSQL(sql);
			sql = PortalUtil.transformSQL(sql);

			SqlUpdate sqlUpdate = SqlUpdateFactoryUtil.getSqlUpdate(
				dataSource, sql);

			sqlUpdate.update();
		}
		catch (Exception exception) {
			throw new SystemException(exception);
		}
	}

	private void _setLocalServiceUtilService(
		PowwowMeetingLocalService powwowMeetingLocalService) {

		try {
			Field field = PowwowMeetingLocalServiceUtil.class.getDeclaredField(
				"_service");

			field.setAccessible(true);

			field.set(null, powwowMeetingLocalService);
		}
		catch (ReflectiveOperationException reflectiveOperationException) {
			throw new RuntimeException(reflectiveOperationException);
		}
	}

	@BeanReference(type = PowwowMeetingLocalService.class)
	protected PowwowMeetingLocalService powwowMeetingLocalService;

	@BeanReference(type = PowwowMeetingPersistence.class)
	protected PowwowMeetingPersistence powwowMeetingPersistence;

	@BeanReference(type = PowwowMeetingFinder.class)
	protected PowwowMeetingFinder powwowMeetingFinder;

	@BeanReference(
		type = com.liferay.powwow.service.PowwowParticipantLocalService.class
	)
	protected com.liferay.powwow.service.PowwowParticipantLocalService
		powwowParticipantLocalService;

	@BeanReference(type = PowwowParticipantPersistence.class)
	protected PowwowParticipantPersistence powwowParticipantPersistence;

	@BeanReference(
		type = com.liferay.powwow.service.PowwowServerLocalService.class
	)
	protected com.liferay.powwow.service.PowwowServerLocalService
		powwowServerLocalService;

	@BeanReference(type = PowwowServerPersistence.class)
	protected PowwowServerPersistence powwowServerPersistence;

	@BeanReference(
		type = com.liferay.counter.kernel.service.CounterLocalService.class
	)
	protected com.liferay.counter.kernel.service.CounterLocalService
		counterLocalService;

	@BeanReference(
		type = com.liferay.portal.kernel.service.ClassNameLocalService.class
	)
	protected com.liferay.portal.kernel.service.ClassNameLocalService
		classNameLocalService;

	@BeanReference(type = ClassNamePersistence.class)
	protected ClassNamePersistence classNamePersistence;

	@BeanReference(
		type = com.liferay.portal.kernel.service.ResourceLocalService.class
	)
	protected com.liferay.portal.kernel.service.ResourceLocalService
		resourceLocalService;

	@BeanReference(
		type = com.liferay.portal.kernel.service.UserLocalService.class
	)
	protected com.liferay.portal.kernel.service.UserLocalService
		userLocalService;

	@BeanReference(type = UserPersistence.class)
	protected UserPersistence userPersistence;

}