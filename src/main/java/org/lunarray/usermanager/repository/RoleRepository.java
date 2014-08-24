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
package org.lunarray.usermanager.repository;

import java.util.List;

import org.lunarray.usermanager.domain.Role;
import org.lunarray.usermanager.repository.exceptions.BaseRepositoryException;

/**
 * A repository for {@link Role}s.
 * 
 * @author Pal Hargitai (pal@lunarray.org)
 */
public interface RoleRepository {

	/**
	 * Creates a role.
	 * 
	 * @param role
	 *            The role to create.
	 * @throws BaseRepositoryException
	 *             Thrown if the operation could not be completed.
	 */
	void createRole(Role role) throws BaseRepositoryException;

	/**
	 * Deletes a role.
	 * 
	 * @param identifier
	 *            The role identifier.
	 * @throws BaseRepositoryException
	 *             Thrown if the operation could not be completed.
	 */
	void deleteRole(String identifier) throws BaseRepositoryException;

	/**
	 * Gets a role with the given identifier.
	 * 
	 * @param identifier
	 *            The role identifier.
	 * @return The role.
	 * @throws BaseRepositoryException
	 *             Thrown if the operation could not be completed.
	 */
	Role getRole(String identifier) throws BaseRepositoryException;

	/**
	 * Gets all role identifiers.
	 * 
	 * @return The role identifiers.
	 * @throws BaseRepositoryException
	 *             Thrown if the operation could not be completed.
	 */
	List<String> getRoleIdentifiers() throws BaseRepositoryException;

	/**
	 * Gets all role identifiers.
	 * 
	 * @return The role identifiers.
	 * @throws BaseRepositoryException
	 *             Thrown if the operation could not be completed.
	 */
	List<Role> getRoles() throws BaseRepositoryException;

	/**
	 * Gets the roles for a user.
	 * 
	 * @param userIdentifier
	 *            The user identifier.
	 * @return The role identifiers of a user.
	 * @throws BaseRepositoryException
	 *             Thrown if the operation could not be completed.
	 */
	List<String> getRolesForUser(String userIdentifier) throws BaseRepositoryException;

	/**
	 * Sets the roles for a given user.
	 * 
	 * @param userIdentifier
	 *            The user identifier.
	 * @param roles
	 *            The role identifiers.
	 * @throws BaseRepositoryException
	 *             Thrown if the operation could not be completed.
	 */
	void setRolesForUser(String userIdentifier, List<String> roles) throws BaseRepositoryException;

	/**
	 * Updates a role.
	 * 
	 * @param role
	 *            The role to update.
	 * @throws BaseRepositoryException
	 *             Thrown if the operation could not be completed.
	 */
	void updateRole(Role role) throws BaseRepositoryException;

	/**
	 * Updates a role, excluding users.
	 * 
	 * @param role
	 *            The role to update.
	 * @throws BaseRepositoryException
	 *             Thrown if the operation could not be completed.
	 */
	void updateRoleNoUsers(Role role) throws BaseRepositoryException;

	/**
	 * Updates a role, excluding users.
	 * 
	 * @param role
	 *            The role to update.
	 * @throws BaseRepositoryException
	 *             Thrown if the operation could not be completed.
	 */
	void updateRoleUsers(Role role) throws BaseRepositoryException;
}
