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

import org.lunarray.usermanager.domain.Role;
import org.lunarray.usermanager.domain.User;
import org.lunarray.usermanager.repository.RoleRepository;
import org.lunarray.usermanager.repository.UserRepository;
import org.lunarray.usermanager.repository.exceptions.BaseRepositoryException;
import org.lunarray.usermanager.repository.exceptions.EntityAlreadyExistsException;
import org.lunarray.usermanager.repository.exceptions.EntityNotFoundException;
import org.lunarray.usermanager.service.RoleService;
import org.lunarray.usermanager.service.exceptions.ServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Implements the {@link RoleService}.
 * 
 * @author Pal Hargitai (pal@lunarray.org)
 */
public final class RoleServiceImpl
		implements RoleService {

	/** The logger. */
	private static final Logger LOGGER = LoggerFactory.getLogger(RoleServiceImpl.class);
	/** The role repository. */
	private transient RoleRepository roleRepository;
	/** The user repository. */
	private transient UserRepository userRepository;

	/** {@inheritDoc} */
	@Override
	public void createRole(final Role role) throws ServiceException {
		RoleServiceImpl.LOGGER.debug("Creating role: {}", role);
		try {
			this.roleRepository.createRole(role);
			RoleServiceImpl.LOGGER.debug("Created role: {}", role);
		} catch (final EntityAlreadyExistsException e) {
			RoleServiceImpl.LOGGER.warn("Could not create role, it already exists.", e);
			throw new ServiceException("Role already exists.", e);
		} catch (final BaseRepositoryException e) {
			RoleServiceImpl.LOGGER.warn("Could not create role.", e);
			throw new ServiceException("Could not perform.", e);
		}
	}

	/** {@inheritDoc} */
	@Override
	public void deleteRole(final String identifier) throws ServiceException {
		RoleServiceImpl.LOGGER.debug("Deleting role: {}", identifier);
		try {
			this.roleRepository.deleteRole(identifier);
			RoleServiceImpl.LOGGER.debug("Deleted role: {}", identifier);
		} catch (final EntityNotFoundException e) {
			RoleServiceImpl.LOGGER.warn("Could not delete role, it doesn't exist.", e);
			throw new ServiceException("Deleting role not found.", e);
		} catch (final BaseRepositoryException e) {
			RoleServiceImpl.LOGGER.warn("Could not delete role.", e);
			throw new ServiceException("Could not perform.", e);
		}
	}

	/** {@inheritDoc} */
	@Override
	public Role getRole(final String identifier) throws ServiceException {
		Role result = null;
		RoleServiceImpl.LOGGER.debug("Getting role: {}", identifier);
		try {
			result = this.roleRepository.getRole(identifier);
		} catch (final EntityNotFoundException e) {
			RoleServiceImpl.LOGGER.warn("Could not get role, it doesn't exist.", e);
			throw new ServiceException("Role not found.", e);
		} catch (final BaseRepositoryException e) {
			RoleServiceImpl.LOGGER.warn("Could not get role.", e);
			throw new ServiceException("Could not perform.", e);
		}
		RoleServiceImpl.LOGGER.debug("Got role: {}", result);
		return result;
	}

	/**
	 * Gets the value for the roleRepository field.
	 * 
	 * @return The value for the roleRepository field.
	 */
	public RoleRepository getRoleRepository() {
		return this.roleRepository;
	}

	/** {@inheritDoc} */
	@Override
	public List<Role> getRoles() throws ServiceException {
		List<Role> result;
		RoleServiceImpl.LOGGER.debug("Getting all roles.");
		try {
			result = this.roleRepository.getRoles();
		} catch (final BaseRepositoryException e) {
			RoleServiceImpl.LOGGER.warn("Could not get roles.", e);
			throw new ServiceException("Could not perform.", e);
		}
		RoleServiceImpl.LOGGER.debug("Got all roles: {}", result);
		return result;
	}

	/** {@inheritDoc} */
	@Override
	public List<String> getRoleUsers(final String roleIdentifier) throws ServiceException {
		List<String> result;
		RoleServiceImpl.LOGGER.debug("Getting all user identifiers for role: {}", roleIdentifier);
		try {
			result = this.roleRepository.getRole(roleIdentifier).getUsers();
		} catch (final EntityNotFoundException e) {
			RoleServiceImpl.LOGGER.warn("Could not get users, role doesn't exist.", e);
			throw new ServiceException("Role for get users not found.", e);
		} catch (final BaseRepositoryException e) {
			RoleServiceImpl.LOGGER.warn("Could not get users for role.", e);
			throw new ServiceException("Could not perform.", e);
		}
		RoleServiceImpl.LOGGER.debug("Got user identifiers for role {}: {}", roleIdentifier, result);
		return result;
	}

	/** {@inheritDoc} */
	@Override
	public User getUser(final String identifier) throws ServiceException {
		RoleServiceImpl.LOGGER.debug("Getting user: {}", identifier);
		User result = null;
		try {
			result = this.userRepository.getUser(identifier);
		} catch (final EntityNotFoundException e) {
			RoleServiceImpl.LOGGER.warn("Could not get user, it doesn't exist.", e);
			throw new ServiceException("User not found.", e);
		} catch (final BaseRepositoryException e) {
			RoleServiceImpl.LOGGER.warn("Could not get user.", e);
			throw new ServiceException("Could not perform.", e);
		}
		RoleServiceImpl.LOGGER.debug("Got user {}: {}", identifier, result);
		return result;
	}

	/** {@inheritDoc} */
	@Override
	public List<String> getUserIdentifiers() throws ServiceException {
		List<String> result;
		RoleServiceImpl.LOGGER.debug("Getting user identifiers.");
		try {
			result = this.userRepository.getUserIdentifiers();
		} catch (final BaseRepositoryException e) {
			RoleServiceImpl.LOGGER.warn("Could not get user identifiers.", e);
			throw new ServiceException("Could not perform.", e);
		}
		RoleServiceImpl.LOGGER.debug("Got user identifiers: {}", result);
		return result;
	}

	/**
	 * Gets the value for the userRepository field.
	 * 
	 * @return The value for the userRepository field.
	 */
	public UserRepository getUserRepository() {
		return this.userRepository;
	}

	/** {@inheritDoc} */
	@Override
	public List<User> getUsers() throws ServiceException {
		List<User> result;
		RoleServiceImpl.LOGGER.debug("Getting users.");
		try {
			result = this.userRepository.getUsers();
		} catch (final BaseRepositoryException e) {
			RoleServiceImpl.LOGGER.warn("Could not get users.", e);
			throw new ServiceException("Could not perform.", e);
		}
		RoleServiceImpl.LOGGER.debug("Got users: {}", result);
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

	/** {@inheritDoc} */
	@Override
	public void setRoleUsers(final String roleIdentifier, final List<String> userIdentifiers) throws ServiceException {
		final Role role = Role.createBuilder().identifier(roleIdentifier).users(userIdentifiers).build();
		RoleServiceImpl.LOGGER.debug("Updating role users: {}", role);
		try {
			this.roleRepository.updateRoleUsers(role);
			RoleServiceImpl.LOGGER.debug("Updated role users: {}", role);
		} catch (final EntityNotFoundException e) {
			RoleServiceImpl.LOGGER.warn("Could not update role users, it doesn't exist.", e);
			throw new ServiceException("Role not found.", e);
		} catch (final BaseRepositoryException e) {
			RoleServiceImpl.LOGGER.warn("Could not update role users.", e);
			throw new ServiceException("Could not perform.", e);
		}
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
	public void updateRole(final Role role) throws ServiceException {
		RoleServiceImpl.LOGGER.debug("Updating role: {}", role);
		try {
			this.roleRepository.updateRoleNoUsers(role);
			RoleServiceImpl.LOGGER.debug("Updated role: {}", role);
		} catch (final EntityNotFoundException e) {
			RoleServiceImpl.LOGGER.warn("Could not update role, it doesn't exist.", e);
			throw new ServiceException("Role not found.", e);
		} catch (final BaseRepositoryException e) {
			RoleServiceImpl.LOGGER.warn("Could not update role.", e);
			throw new ServiceException("Could not perform.", e);
		}
	}
}
