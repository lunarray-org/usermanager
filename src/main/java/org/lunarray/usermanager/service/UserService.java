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
package org.lunarray.usermanager.service;

import java.util.List;

import org.lunarray.usermanager.domain.Role;
import org.lunarray.usermanager.domain.User;
import org.lunarray.usermanager.service.exceptions.ServiceException;

/**
 * A service for the user part of the application.
 * 
 * @author Pal Hargitai (pal@lunarray.org)
 */
public interface UserService {

	/**
	 * Creates a user.
	 * 
	 * @param user
	 *            The new user.
	 * @throws ServiceException
	 *             Thrown if the operation could not be completed.
	 */
	void createUser(User user) throws ServiceException;

	/**
	 * Deletes a user.
	 * 
	 * @param identifier
	 *            The user identifier.
	 * @throws ServiceException
	 *             Thrown if the operation could not be completed.
	 */
	void deleteUser(String identifier) throws ServiceException;

	/**
	 * Gets a role.
	 * 
	 * @param identifier
	 *            The role identifier.
	 * @return The role with the given identifier.
	 * @throws ServiceException
	 *             Thrown if the operation could not be completed.
	 */
	Role getRole(String identifier) throws ServiceException;

	/**
	 * Gets all role identifiers.
	 * 
	 * @return All role identifiers.
	 * @throws ServiceException
	 *             Thrown if the operation could not be completed.
	 */
	List<String> getRoleIdentifiers() throws ServiceException;

	/**
	 * Gets all roles.
	 * 
	 * @return All roles.
	 * @throws ServiceException
	 *             Thrown if the operation could not be completed.
	 */
	List<Role> getRoles() throws ServiceException;

	/**
	 * Get a user.
	 * 
	 * @param identifier
	 *            The user identifier.
	 * @return The user.
	 * @throws ServiceException
	 *             Thrown if the operation could not be completed.
	 */
	User getUser(String identifier) throws ServiceException;

	/**
	 * Gets a users roles.
	 * 
	 * @param userIdentifier
	 *            The user identifier.
	 * @return The role identifiers.
	 * @throws ServiceException
	 *             Thrown if the operation could not be completed.
	 */
	List<String> getUserRoles(String userIdentifier) throws ServiceException;

	/**
	 * Gets all users.
	 * 
	 * @return The users.
	 * @throws ServiceException
	 *             Thrown if the operation could not be completed.
	 */
	List<User> getUsers() throws ServiceException;

	/**
	 * Sets the roles for a user.
	 * 
	 * @param userIdentifier
	 *            The user identifier.
	 * @param roleIdentifiers
	 *            The roles.
	 * @throws ServiceException
	 *             Thrown if the operation could not be completed.
	 */
	void setUserRoles(String userIdentifier, List<String> roleIdentifiers) throws ServiceException;

	/**
	 * Updates a users password.
	 * 
	 * @param identifier
	 *            The user identifier.
	 * @param password
	 *            The password.
	 * @throws ServiceException
	 *             Thrown if the operation could not be completed.
	 */
	void updatePassword(String identifier, String password) throws ServiceException;

	/**
	 * Updates a user.
	 * 
	 * @param user
	 *            The user to update.
	 * @throws ServiceException
	 *             Thrown if the operation could not be completed.
	 */
	void updateUser(User user) throws ServiceException;
}
