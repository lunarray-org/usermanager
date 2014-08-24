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
 * A service for the roles subsection of the web application.
 * 
 * @author Pal Hargitai (pal@lunarray.org)
 */
public interface RoleService {

	/**
	 * Create a role.
	 * 
	 * @param role
	 *            The role to create.
	 * @throws ServiceException
	 *             Thrown if the operation could not be completed.
	 */
	void createRole(Role role) throws ServiceException;

	/**
	 * Delete a role.
	 * 
	 * @param identifier
	 *            The role identifier.
	 * @throws ServiceException
	 *             Thrown if the operation could not be completed.
	 */
	void deleteRole(String identifier) throws ServiceException;

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
	 * Get all roles.
	 * 
	 * @return All roles.
	 * @throws ServiceException
	 *             Thrown if the operation could not be completed.
	 */
	List<Role> getRoles() throws ServiceException;

	/**
	 * Gets all users with a role.
	 * 
	 * @param roleIdentifier
	 *            The role identfier.
	 * @return The user identfiers.
	 * @throws ServiceException
	 *             Thrown if the operation could not be completed.
	 */
	List<String> getRoleUsers(String roleIdentifier) throws ServiceException;

	/**
	 * Gets a user.
	 * 
	 * @param identifier
	 *            The user identfier.
	 * @return The user.
	 * @throws ServiceException
	 *             Thrown if the operation could not be completed.
	 */
	User getUser(String identifier) throws ServiceException;

	/**
	 * Gets all user identifiers.
	 * 
	 * @return All user identifiers.
	 * @throws ServiceException
	 *             Thrown if the operation could not be completed.
	 */
	List<String> getUserIdentifiers() throws ServiceException;

	/**
	 * Gets all users.
	 * 
	 * @return The users.
	 * @throws ServiceException
	 *             Thrown if the operation could not be completed.
	 */
	List<User> getUsers() throws ServiceException;

	/**
	 * Updates the users of a role.
	 * 
	 * @param roleIdentifier
	 *            The role identifier.
	 * @param userIdentifiers
	 *            The user identifiers.
	 * @throws ServiceException
	 *             Thrown if the operation could not be completed.
	 */
	void setRoleUsers(String roleIdentifier, List<String> userIdentifiers) throws ServiceException;

	/**
	 * Updates a role, does not update the users.
	 * 
	 * @param role
	 *            The role to update.
	 * @throws ServiceException
	 *             Thrown if the operation could not be completed.
	 */
	void updateRole(Role role) throws ServiceException;
}
