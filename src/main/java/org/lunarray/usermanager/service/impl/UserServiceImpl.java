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
package org.lunarray.usermanager.service.impl;

import java.util.List;

import org.apache.shiro.crypto.hash.Sha1Hash;
import org.lunarray.usermanager.domain.Role;
import org.lunarray.usermanager.domain.User;
import org.lunarray.usermanager.repository.RoleRepository;
import org.lunarray.usermanager.repository.UserRepository;
import org.lunarray.usermanager.repository.exceptions.BaseRepositoryException;
import org.lunarray.usermanager.repository.exceptions.EntityAlreadyExistsException;
import org.lunarray.usermanager.repository.exceptions.EntityNotFoundException;
import org.lunarray.usermanager.service.UserService;
import org.lunarray.usermanager.service.exceptions.ServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Implements the {@link UserService}.
 * 
 * @author Pal Hargitai (pal@lunarray.org)
 */
public class UserServiceImpl
		implements UserService {

	/** The logger. */
	private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);
	/** The role repository. */
	private transient RoleRepository roleRepository;
	/** The user repository. */
	private transient UserRepository userRepository;

	/** {@inheritDoc} */
	@Override
	public void createUser(final User user) throws ServiceException {
		UserServiceImpl.LOGGER.debug("Creating user: {}", user);
		try {
			this.userRepository.createUser(user);
			UserServiceImpl.LOGGER.debug("Created user: {}", user);
		} catch (final EntityAlreadyExistsException e) {
			UserServiceImpl.LOGGER.warn("Could not create user, user already exists.", e);
			throw new ServiceException("User already exists.", e);
		} catch (final BaseRepositoryException e) {
			UserServiceImpl.LOGGER.warn("Could not create user.", e);
			throw new ServiceException("Could not perform.", e);
		}
	}

	/** {@inheritDoc} */
	@Override
	public void deleteUser(final String identifier) throws ServiceException {
		UserServiceImpl.LOGGER.debug("Deleting user: {}", identifier);
		try {
			this.userRepository.deleteUser(identifier);
			UserServiceImpl.LOGGER.debug("Deleted user: {}", identifier);
		} catch (final EntityNotFoundException e) {
			UserServiceImpl.LOGGER.warn("Could not delete user, it wasn't found.", e);
			throw new ServiceException("User not found.", e);
		} catch (final BaseRepositoryException e) {
			UserServiceImpl.LOGGER.warn("Could not delete user.", e);
			throw new ServiceException("Could not perform.", e);
		}
	}

	/** {@inheritDoc} */
	@Override
	public Role getRole(final String identifier) throws ServiceException {
		UserServiceImpl.LOGGER.debug("Getting role: {}", identifier);
		Role result = null;
		try {
			result = this.roleRepository.getRole(identifier);
		} catch (final EntityNotFoundException e) {
			UserServiceImpl.LOGGER.warn("Could not get role, it doesn't exist.", e);
			throw new ServiceException("Role not found.", e);
		} catch (final BaseRepositoryException e) {
			UserServiceImpl.LOGGER.warn("Could not get role.", e);
			throw new ServiceException("Could not perform.", e);
		}
		UserServiceImpl.LOGGER.debug("Got role {}: {}", identifier, result);
		return result;
	}

	/** {@inheritDoc} */
	@Override
	public List<String> getRoleIdentifiers() throws ServiceException {
		List<String> result;
		UserServiceImpl.LOGGER.debug("Getting role identifiers.");
		try {
			result = this.roleRepository.getRoleIdentifiers();
		} catch (final BaseRepositoryException e) {
			UserServiceImpl.LOGGER.warn("Could not get role identifiers.", e);
			throw new ServiceException("Could not perform.", e);
		}
		UserServiceImpl.LOGGER.debug("Got role identifiers: {}", result);
		return result;
	}

	/** {@inheritDoc} */
	@Override
	public List<Role> getRoles() throws ServiceException {
		List<Role> result;
		UserServiceImpl.LOGGER.debug("Getting roles.");
		try {
			result = this.roleRepository.getRoles();
		} catch (final BaseRepositoryException e) {
			UserServiceImpl.LOGGER.warn("Could not get roles.", e);
			throw new ServiceException("Could not perform.", e);
		}
		UserServiceImpl.LOGGER.debug("Got roles: {}", result);
		return result;
	}

	/** {@inheritDoc} */
	@Override
	public User getUser(final String identifier) throws ServiceException {
		UserServiceImpl.LOGGER.debug("Getting user: {}", identifier);
		User result = null;
		try {
			result = this.userRepository.getUser(identifier);
		} catch (final EntityNotFoundException e) {
			UserServiceImpl.LOGGER.warn("Could not get user, it wasn't found.", e);
			throw new ServiceException("User not found.", e);
		} catch (final BaseRepositoryException e) {
			UserServiceImpl.LOGGER.warn("Could not get user.", e);
			throw new ServiceException("Could not perform.", e);
		}
		UserServiceImpl.LOGGER.debug("Got user {}: {}", identifier, result);
		return result;
	}

	/** {@inheritDoc} */
	@Override
	public List<String> getUserRoles(final String userIdentifier) throws ServiceException {
		List<String> result;
		UserServiceImpl.LOGGER.debug("Getting roles for user: {}", userIdentifier);
		try {
			result = this.roleRepository.getRolesForUser(userIdentifier);
		} catch (final EntityNotFoundException e) {
			UserServiceImpl.LOGGER.warn("Could not get roles for user, user wasn't found.", e);
			throw new ServiceException("User not found.", e);
		} catch (final BaseRepositoryException e) {
			UserServiceImpl.LOGGER.warn("Could not get roles for user.", e);
			throw new ServiceException("Could not perform.", e);
		}
		UserServiceImpl.LOGGER.debug("Got roles for user {}: {}", userIdentifier, result);
		return result;
	}

	/** {@inheritDoc} */
	@Override
	public List<User> getUsers() throws ServiceException {
		List<User> result;
		UserServiceImpl.LOGGER.debug("Getting users.");
		try {
			result = this.userRepository.getUsers();
		} catch (final BaseRepositoryException e) {
			UserServiceImpl.LOGGER.warn("Could not get users.", e);
			throw new ServiceException("Could not perform.", e);
		}
		UserServiceImpl.LOGGER.debug("Got users: {}", result);
		return result;
	}

	/**
	 * Sets a new value for the roleRepository field.
	 * 
	 * @param roleRepository
	 *            The new value for the roleRepository field.
	 */
	public void setRoleRepository(final RoleRepository roleRepository) {
		this.roleRepository = roleRepository;
	}

	/**
	 * Sets a new value for the userRepository field.
	 * 
	 * @param userRepository
	 *            The new value for the userRepository field.
	 */
	public void setUserRepository(final UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	/** {@inheritDoc} */
	@Override
	public void setUserRoles(final String userIdentifier, final List<String> roleIdentifiers) throws ServiceException {
		UserServiceImpl.LOGGER.debug("Setting roles for user {}: {}", userIdentifier, roleIdentifiers);
		try {
			this.roleRepository.setRolesForUser(userIdentifier, roleIdentifiers);
		} catch (final EntityNotFoundException e) {
			UserServiceImpl.LOGGER.warn("Could not set user roles, user or role not found.", e);
			throw new ServiceException("User or role not found.", e);
		} catch (final BaseRepositoryException e) {
			UserServiceImpl.LOGGER.warn("Could not set user roles.", e);
			throw new ServiceException("Could not perform.", e);
		}
		UserServiceImpl.LOGGER.debug("Set roles for user {}: {}", userIdentifier, roleIdentifiers);
	}

	/** {@inheritDoc} */
	@Override
	public void updatePassword(final String identifier, final String password) throws ServiceException {
		final byte[] bytes = new Sha1Hash(password).getBytes();
		UserServiceImpl.LOGGER.debug("Updating password for user: {}", identifier);
		try {
			this.userRepository.updateUserPassword(identifier, bytes, "SHA");
		} catch (final EntityNotFoundException e) {
			UserServiceImpl.LOGGER.warn("Could not update password for user, user not found.", e);
			throw new ServiceException("User not found.", e);
		} catch (final BaseRepositoryException e) {
			UserServiceImpl.LOGGER.warn("Could not update password for user.", e);
			throw new ServiceException("Could not perform.", e);
		}
		UserServiceImpl.LOGGER.debug("Updated password for user: {}", identifier);
	}

	/** {@inheritDoc} */
	@Override
	public void updateUser(final User user) throws ServiceException {
		UserServiceImpl.LOGGER.debug("Updating user: {}", user);
		try {
			this.userRepository.updateUser(user);
		} catch (final EntityNotFoundException e) {
			UserServiceImpl.LOGGER.debug("Could not updated user, user wasn't found.", e);
			throw new ServiceException("User not found.", e);
		} catch (final BaseRepositoryException e) {
			UserServiceImpl.LOGGER.debug("Could not updated user.", e);
			throw new ServiceException("Could not perform.", e);
		}
		UserServiceImpl.LOGGER.debug("Updated user: {}", user);
	}
}
