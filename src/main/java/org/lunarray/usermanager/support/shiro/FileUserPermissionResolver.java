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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.apache.shiro.authz.Permission;
import org.apache.shiro.authz.permission.PermissionResolver;
import org.lunarray.common.check.CheckUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;

/**
 * File based user permission resolver.
 * 
 * @author Pal Hargitai (pal@lunarray.org)
 */
public final class FileUserPermissionResolver
		implements UserPermissionResolver {

	/** Comment indicator. */
	private static final char COMMENT = '#';
	/** The logger. */
	private static final Logger LOGGER = LoggerFactory.getLogger(FileUserPermissionResolver.class);
	/** The permission resolver. */
	private transient PermissionResolver permissionResolver;
	/** The mapping. */
	private transient List<String> permissions;
	/** The user permission mapping. */
	private transient Resource userPermission;

	/**
	 * Default constructor.
	 */
	public FileUserPermissionResolver() {
		// Default constructor.
	}

	/**
	 * Gets the value for the permissionResolver field.
	 * 
	 * @return The value for the permissionResolver field.
	 */
	public PermissionResolver getPermissionResolver() {
		return this.permissionResolver;
	}

	/**
	 * Gets the value for the permissions field.
	 * 
	 * @return The value for the permissions field.
	 */
	public List<String> getPermissions() {
		return this.permissions;
	}

	/**
	 * Gets the value for the userPermission field.
	 * 
	 * @return The value for the userPermission field.
	 */
	public Resource getUserPermission() {
		return this.userPermission;
	}

	/**
	 * Initializes the resolver.
	 */
	public void init() {
		this.permissions = new LinkedList<String>();
		BufferedReader reader = null;
		try {
			FileUserPermissionResolver.LOGGER.debug("Reading permissions from: {}", this.userPermission);
			reader = new BufferedReader(new InputStreamReader(this.userPermission.getInputStream()));
			String line = reader.readLine();
			while (line != null) {
				if (line.charAt(0) != FileUserPermissionResolver.COMMENT) {
					FileUserPermissionResolver.LOGGER.debug("Read line: {}", line);
					this.permissions.add(line);
				}
				line = reader.readLine();
			}
		} catch (final IOException e) {
			throw new IllegalArgumentException("Could not initialize.", e);
		} finally {
			if (CheckUtil.isNull(reader)) {
				try {
					reader.close();
				} catch (final IOException e) {
					FileUserPermissionResolver.LOGGER.warn("Could not close stream.", e);
				}
			}
		}
	}

	/** {@inheritDoc} */
	@Override
	public Collection<Permission> resolvePermissions(final Object principal, final Collection<String> roles) {
		final List<Permission> result = new LinkedList<Permission>();
		for (final String permission : this.permissions) {
			// Process user.
			String uPermission = permission;
			if (principal instanceof String) {
				uPermission = uPermission.replace("${user}", (String) principal);
			}
			// Process user.
			if (uPermission.contains("${roles}")) {
				for (final String role : roles) {
					result.add(this.permissionResolver.resolvePermission(uPermission.replace("${roles}", role)));
				}
			} else {
				result.add(this.permissionResolver.resolvePermission(uPermission));
			}
		}
		FileUserPermissionResolver.LOGGER.debug("Found permissions for principal {}: {}", principal, result);
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
	 * Sets a new value for the permissions field.
	 * 
	 * @param permissions
	 *            The new value for the permissions field.
	 */
	public void setPermissions(final List<String> permissions) {
		this.permissions = permissions;
	}

	/**
	 * Sets a new value for the userPermission field.
	 * 
	 * @param userPermission
	 *            The new value for the userPermission field.
	 */
	public void setUserPermission(final Resource userPermission) {
		this.userPermission = userPermission;
	}
}
