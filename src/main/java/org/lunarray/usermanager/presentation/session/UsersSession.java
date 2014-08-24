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
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.lunarray.usermanager.domain.Role;
import org.lunarray.usermanager.domain.User;
import org.lunarray.usermanager.presentation.domain.PresentationRole;
import org.lunarray.usermanager.presentation.domain.PresentationUser;
import org.lunarray.usermanager.presentation.domain.adapters.PresentationRoleAdapter;
import org.lunarray.usermanager.presentation.domain.adapters.PresentationUserAdapter;
import org.lunarray.usermanager.service.UserService;
import org.lunarray.usermanager.service.exceptions.ServiceException;

/**
 * The session bean for the users part of the application.
 * 
 * @author Pal Hargitai (pal@lunarray.org)
 */
public final class UsersSession
		implements Serializable {
	/** Serial id. */
	private static final long serialVersionUID = 3185784857276666282L;
	/** The role adapter. */
	private transient PresentationRoleAdapter presentationRoleAdapter;
	/** The user adapter. */
	private transient PresentationUserAdapter presentationUserAdapter;
	/** The user service. */
	private transient UserService userService;

	/**
	 * Default constructor.
	 */
	public UsersSession() {
		// Default constructor.
	}

	/**
	 * Creates a user.
	 * 
	 * @param user
	 *            The user.
	 * @throws ServiceException
	 *             Thrown if the operation could not be completed.
	 */
	public void createUser(final PresentationUser user) throws ServiceException {
		this.userService.createUser(this.presentationUserAdapter.toUser(user));
	}

	/**
	 * Deletes a user.
	 * 
	 * @param identifier
	 *            The user identifier.
	 * @throws ServiceException
	 *             Thrown if the operation could not be completed.
	 */
	public void deleteUser(final String identifier) throws ServiceException {
		this.userService.deleteUser(identifier);
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
		return this.presentationRoleAdapter.toPresentationRole(this.userService.getRole(identifier));
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
		final List<Role> roles = this.userService.getRoles();
		for (final Role role : roles) {
			result.put(role.getIdentifier(), this.presentationRoleAdapter.toPresentationRole(role));
		}
		return new ArrayList<PresentationRole>(result.values());
	}

	/**
	 * Gets a users roles.
	 * 
	 * @param userIdentifier
	 *            The user identifier.
	 * @return The roles.
	 * @throws ServiceException
	 *             Thrown if the operation could not be completed.
	 */
	public List<PresentationRole> getRoles(final String userIdentifier) throws ServiceException {
		final Map<String, PresentationRole> result = new TreeMap<String, PresentationRole>();
		final List<String> roles = this.userService.getUserRoles(userIdentifier);
		for (final String roleString : roles) {
			final PresentationRole role = this.getRole(roleString);
			result.put(role.getIdentifier(), role);
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
		return this.presentationUserAdapter.toPresentationUser(this.userService.getUser(identifier));
	}

	/**
	 * Gets all users.
	 * 
	 * @return The users.
	 * @throws ServiceException
	 *             Thrown if the operation could not be completed.
	 */
	public List<PresentationUser> getUsers() throws ServiceException {
		final Map<String, PresentationUser> result = new TreeMap<String, PresentationUser>();
		final List<User> serviceResults = this.userService.getUsers();
		for (final User user : serviceResults) {
			result.put(user.getIdentifier(), this.presentationUserAdapter.toPresentationUser(user));
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
	 * Sets the roles for a user.
	 * 
	 * @param userIdentifier
	 *            The user identifier.
	 * @param roles
	 *            The roles.
	 * @throws ServiceException
	 *             Thrown if the operation could not be completed.
	 */
	public void setRoles(final String userIdentifier, final List<PresentationRole> roles) throws ServiceException {
		final List<String> result = new ArrayList<String>(roles.size());
		for (final PresentationRole role : roles) {
			result.add(role.getIdentifier());
		}
		this.userService.setUserRoles(userIdentifier, result);
	}

	/**
	 * Sets a new value for the userService field.
	 * 
	 * @param userService
	 *            The new value for the userService field.
	 */
	public void setUserService(final UserService userService) {
		this.userService = userService;
	}

	/**
	 * Updates a users password.
	 * 
	 * @param identifier
	 *            The user identifier.
	 * @param password
	 *            The new password.
	 * @throws ServiceException
	 *             Thrown if the operation could not be completed.
	 */
	public void updatePassword(final String identifier, final String password) throws ServiceException {
		this.userService.updatePassword(identifier, password);
	}

	/**
	 * Updates a user.
	 * 
	 * @param user
	 *            The user.
	 * @throws ServiceException
	 *             Thrown if the operation could not be completed.
	 */
	public void updateUser(final PresentationUser user) throws ServiceException {
		this.userService.updateUser(this.presentationUserAdapter.toUser(user));
	}
}
