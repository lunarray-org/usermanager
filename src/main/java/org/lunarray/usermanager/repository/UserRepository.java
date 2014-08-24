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

import org.lunarray.usermanager.domain.User;
import org.lunarray.usermanager.repository.exceptions.BaseRepositoryException;

/**
 * Defines a repository for {@link User}s.
 * 
 * @author Pal Hargitai (pal@lunarray.org)
 */
public interface UserRepository {

	/**
	 * Tests if the user exists.
	 * 
	 * @param identifier
	 *            The user identifier.
	 * @return True if the user exists, otherwise false.
	 * @throws BaseRepositoryException
	 *             Thrown if the operation could not be completed.
	 */
	boolean containsUser(String identifier) throws BaseRepositoryException;

	/**
	 * Creates a user.
	 * 
	 * @param user
	 *            The user to create.
	 * @throws BaseRepositoryException
	 *             Thrown if the operation could not be completed.
	 */
	void createUser(User user) throws BaseRepositoryException;

	/**
	 * Deletes a user.
	 * 
	 * @param identifier
	 *            The user.
	 * @throws BaseRepositoryException
	 *             Thrown if the operation could not be completed.
	 */
	void deleteUser(String identifier) throws BaseRepositoryException;

	/**
	 * Gets a user.
	 * 
	 * @param identifier
	 *            The user identifier.
	 * @return The user.
	 * @throws BaseRepositoryException
	 *             Thrown if the operation could not be completed.
	 */
	User getUser(String identifier) throws BaseRepositoryException;

	/**
	 * Gets all user identifiers.
	 * 
	 * @return All users identifiers.
	 * @throws BaseRepositoryException
	 *             Thrown if the operation could not be completed.
	 */
	List<String> getUserIdentifiers() throws BaseRepositoryException;

	/**
	 * Retrieves all users.
	 * 
	 * @return The users.
	 * @throws BaseRepositoryException
	 *             Thrown if the operation could not be completed.
	 */
	List<User> getUsers() throws BaseRepositoryException;

	/**
	 * Gets a user without testing security. Should only be used in integrating
	 * services!
	 * 
	 * @param identifier
	 *            The user identifier.
	 * @return The user.
	 * @throws BaseRepositoryException
	 *             Thrown if the operation could not be completed.
	 */
	User getUserUnsecured(String identifier) throws BaseRepositoryException;

	/**
	 * Updates a user.
	 * 
	 * @param user
	 *            The user to update.
	 * @throws BaseRepositoryException
	 *             Thrown if the operation could not be completed.
	 */
	void updateUser(User user) throws BaseRepositoryException;

	/**
	 * Update the password for a user.
	 * 
	 * @param identifier
	 *            The user identifier.
	 * @param passwordHash
	 *            The password hash as bytes.
	 * @param algorithm
	 *            The algorithm used to hash the password.
	 * @throws BaseRepositoryException
	 *             Thrown if the operation could not be completed.
	 */
	void updateUserPassword(String identifier, byte[] passwordHash, String algorithm) throws BaseRepositoryException;
}
