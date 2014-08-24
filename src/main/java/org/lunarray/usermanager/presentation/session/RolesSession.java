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
package org.lunarray.usermanager.presentation.session;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.shiro.SecurityUtils;
import org.lunarray.usermanager.domain.Role;
import org.lunarray.usermanager.domain.User;
import org.lunarray.usermanager.presentation.domain.PresentationRole;
import org.lunarray.usermanager.presentation.domain.PresentationUser;
import org.lunarray.usermanager.presentation.domain.adapters.PresentationRoleAdapter;
import org.lunarray.usermanager.presentation.domain.adapters.PresentationUserAdapter;
import org.lunarray.usermanager.service.RoleService;
import org.lunarray.usermanager.service.exceptions.ServiceException;

/**
 * The session bean for the roles part of the application.
 * 
 * @author Pal Hargitai (pal@lunarray.org)
 */
public final class RolesSession
		implements Serializable {
	/** Serial id. */
	private static final long serialVersionUID = -5097098800166997546L;
	/** The role adapter. */
	private transient PresentationRoleAdapter presentationRoleAdapter;
	/** The user adapter. */
	private transient PresentationUserAdapter presentationUserAdapter;
	/** The role service. */
	private transient RoleService roleService;

	/**
	 * Default constructor.
	 */
	public RolesSession() {
		// Default constructor.
	}

	/**
	 * Creates a role.
	 * 
	 * @param role
	 *            The role.
	 * @throws ServiceException
	 *             Thrown if the operation could not be completed.
	 */
	public void createRole(final PresentationRole role) throws ServiceException {
		final Role rrole = this.presentationRoleAdapter.toRole(role);
		rrole.setUsers(Collections.singletonList(SecurityUtils.getSubject().getPrincipal().toString()));
		this.roleService.createRole(rrole);
	}

	/**
	 * Deletes a role.
	 * 
	 * @param identifier
	 *            The role.
	 * @throws ServiceException
	 *             Thrown if the operation could not be completed.
	 */
	public void deleteRole(final String identifier) throws ServiceException {
		this.roleService.deleteRole(identifier);
	}

	/**
	 * Gets a role.
	 * 
	 * @param identifier
	 *            The role identifier.
	 * @return The role.
	 * @throws ServiceException
	 *             Thrown if the operation could not be completed.
	 */
	public PresentationRole getRole(final String identifier) throws ServiceException {
		return this.presentationRoleAdapter.toPresentationRole(this.roleService.getRole(identifier));
	}

	/**
	 * Gets all roles.
	 * 
	 * @return All roles.
	 * @throws ServiceException
	 *             Thrown if the operation could not be completed.
	 */
	public List<PresentationRole> getRoles() throws ServiceException {
		final Map<String, PresentationRole> result = new TreeMap<String, PresentationRole>();
		final List<Role> roles = this.roleService.getRoles();
		for (final Role role : roles) {
			result.put(role.getIdentifier(), this.presentationRoleAdapter.toPresentationRole(role));
		}
		return new ArrayList<PresentationRole>(result.values());
	}

	/**
	 * Gets a user.
	 * 
	 * @param identifier
	 *            The user identifier.
	 * @return The user.
	 * @throws ServiceException
	 *             Thrown if the operation could not be completed.
	 */
	public PresentationUser getUser(final String identifier) throws ServiceException {
		return this.presentationUserAdapter.toPresentationUser(this.roleService.getUser(identifier));
	}

	/**
	 * Gets the users.
	 * 
	 * @return The users.
	 * @throws ServiceException
	 *             Thrown if the operation could not be completed.
	 */
	public List<PresentationUser> getUsers() throws ServiceException {
		final Map<String, PresentationUser> result = new TreeMap<String, PresentationUser>();
		final List<User> serviceResults = this.roleService.getUsers();
		for (final User user : serviceResults) {
			result.put(user.getIdentifier(), this.presentationUserAdapter.toPresentationUser(user));
		}
		return new ArrayList<PresentationUser>(result.values());
	}

	/**
	 * Gets the users in a role.
	 * 
	 * @param roleIdentifier
	 *            The role.
	 * @return The users.
	 * @throws ServiceException
	 *             Thrown if the operation could not be completed.
	 */
	public List<PresentationUser> getUsers(final String roleIdentifier) throws ServiceException {
		final Map<String, PresentationUser> result = new TreeMap<String, PresentationUser>();
		final List<String> serviceResults = this.roleService.getRoleUsers(roleIdentifier);
		for (final String userString : serviceResults) {
			final PresentationUser user = this.getUser(userString);
			result.put(user.getIdentifier(), user);
		}
		return new ArrayList<PresentationUser>(result.values());
	}

	/**
	 * Sets a new value for the presentationRoleAdapter field.
	 * 
	 * @param presentationRoleAdapter
	 *            The new value for the presentationRoleAdapter field.
	 */
	public void setPresentationRoleAdapter(final PresentationRoleAdapter presentationRoleAdapter) {
		this.presentationRoleAdapter = presentationRoleAdapter;
	}

	/**
	 * Sets a new value for the presentationUserAdapter field.
	 * 
	 * @param presentationUserAdapter
	 *            The new value for the presentationUserAdapter field.
	 */
	public void setPresentationUserAdapter(final PresentationUserAdapter presentationUserAdapter) {
		this.presentationUserAdapter = presentationUserAdapter;
	}

	/**
	 * Sets a new value for the roleService field.
	 * 
	 * @param roleService
	 *            The new value for the roleService field.
	 */
	public void setRoleService(final RoleService roleService) {
		this.roleService = roleService;
	}

	/**
	 * Sets the users in o role.
	 * 
	 * @param roleIdentifier
	 *            The role identifier.
	 * @param users
	 *            The users for the role.
	 * @throws ServiceException
	 *             Thrown if the operation could not be completed.
	 */
	public void setUsers(final String roleIdentifier, final List<PresentationUser> users) throws ServiceException {
		final List<String> userIdentifiers = new ArrayList<String>(users.size());
		for (final PresentationUser user : users) {
			userIdentifiers.add(user.getIdentifier());
		}
		this.roleService.setRoleUsers(roleIdentifier, userIdentifiers);
	}

	/**
	 * Updates a role.
	 * 
	 * @param role
	 *            The role to update.
	 * @throws ServiceException
	 *             Thrown if the operation could not be completed.
	 */
	public void updateRole(final PresentationRole role) throws ServiceException {
		this.roleService.updateRole(this.presentationRoleAdapter.toRole(role));
	}
}
