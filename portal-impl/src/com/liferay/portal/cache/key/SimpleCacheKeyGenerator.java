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

package com.liferay.portal.cache.key;

import com.liferay.petra.string.StringBundler;
import com.liferay.portal.kernel.cache.key.CacheKeyGenerator;

/**
 * @author Shuyang Zhou
 */
public class SimpleCacheKeyGenerator extends BaseCacheKeyGenerator {

	@Override
	public CacheKeyGenerator clone() {
		return new SimpleCacheKeyGenerator();
	}

	/**
	 * @deprecated As of Athanasius (7.3.x), replaced by {@link
	 *             #getCacheKey(StringBundler)}
	 */
	@Deprecated
	@Override
	public String getCacheKey(com.liferay.portal.kernel.util.StringBundler sb) {
		return sb.toString();
	}

	@Override
	public String getCacheKey(String key) {
		return key;
	}

	@Override
	public String getCacheKey(String[] keys) {
		StringBundler sb = new StringBundler(keys);

		return sb.toString();
	}

	@Override
	public String getCacheKey(StringBundler sb) {
		return sb.toString();
	}

}