/* 
 * User manager.
 * Copyright (C) 2013 Pal Hargitai (pal@lunarray.org)
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 * 
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.lunarray.usermanager.support.shiro;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.apache.shiro.authz.Permission;
import org.apache.shiro.authz.permission.PermissionResolver;
import org.apache.shiro.authz.permission.RolePermissionResolver;
import org.lunarray.common.check.CheckUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;

/**
 * Property file based role permission resolver.
 * 
 * @author Pal Hargitai (pal@lunarray.org)
 */
public final class PropertyRolePermissionResolver
		implements RolePermissionResolver {

	/** The logger. */
	private static final Logger LOGGER = LoggerFactory.getLogger(PropertyRolePermissionResolver.class);
	/** The mapping. */
	private transient Map<String, List<String>> mapping;
	/** The permission resolver. */
	private transient PermissionResolver permissionResolver;
	/** The role permission mapping. */
	private transient Resource rolePermission;

	/**
	 * Default constructor.
	 */
	public PropertyRolePermissionResolver() {
		// Default constructor.
	}

	/**
	 * Initializes the resolver.
	 */
	public void init() {
		this.mapping = new HashMap<String, List<String>>();
		final Properties properties = new Properties();
		InputStream input = null;
		try {
			input = this.rolePermission.getInputStream();
			PropertyRolePermissionResolver.LOGGER.debug("Reading: {}", this.rolePermission);
			properties.load(input);
		} catch (final IOException e) {
			throw new IllegalArgumentException("Could not initialize.", e);
		} finally {
			if (!CheckUtil.isNull(input)) {
				try {
					input.close();
				} catch (final IOException e) {
					PropertyRolePermissionResolver.LOGGER.warn("Could not close stream.");
				}
			}
		}
		for (final String key : properties.stringPropertyNames()) {
			final List<String> list = this.createList();
			this.mapping.put(key.trim(), list);
			final String valuesString = properties.getProperty(key);
			final String[] values = valuesString.split(",");
			PropertyRolePermissionResolver.LOGGER.debug("Read key: {}", key);
			for (final String value : values) {
				list.add(value);
				PropertyRolePermissionResolver.LOGGER.debug("Read value for key {}: {}", key, value);
			}
		}
	}

	/** {@inheritDoc} */
	@Override
	public Collection<Permission> resolvePermissionsInRole(final String roleString) {
		final Set<Permission> result = new LinkedHashSet<Permission>();
		if (this.mapping.containsKey(roleString)) {
			for (final String permission : this.mapping.get(roleString)) {
				result.add(this.permissionResolver.resolvePermission(permission));
			}
		}
		PropertyRolePermissionResolver.LOGGER.debug("Read permissions for role {}: {}", roleString, result);
		return result;
	}

	/**
	 * Sets a new value for the permissionResolver field.
	 * 
	 * @param permissionResolver
	 *            The new value for the permissionResolver field.
	 */
	public void setPermissionResolver(final PermissionResolver permissionResolver) {
		this.permissionResolver = permissionResolver;
	}

	/**
	 * Sets a new value for the rolePermission field.
	 * 
	 * @param rolePermission
	 *            The new value for the rolePermission field.
	 */
	public void setRolePermission(final Resource rolePermission) {
		this.rolePermission = rolePermission;
	}

	/**
	 * Creates a list.
	 * 
	 * @return The list.
	 */
	private LinkedList<String> createList() {
		return new LinkedList<String>();
	}
}
