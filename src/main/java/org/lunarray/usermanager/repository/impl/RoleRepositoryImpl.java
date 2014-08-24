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
package org.lunarray.usermanager.repository.impl;

import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.naming.NameAlreadyBoundException;
import javax.naming.NameClassPair;
import javax.naming.NameNotFoundException;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attributes;
import javax.naming.directory.BasicAttribute;
import javax.naming.directory.BasicAttributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.SearchResult;
import javax.naming.ldap.LdapContext;

import org.apache.commons.lang.Validate;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.realm.ldap.LdapContextFactory;
import org.lunarray.common.check.CheckUtil;
import org.lunarray.usermanager.domain.Role;
import org.lunarray.usermanager.domain.User;
import org.lunarray.usermanager.repository.RoleRepository;
import org.lunarray.usermanager.repository.exceptions.BaseRepositoryException;
import org.lunarray.usermanager.repository.exceptions.EntityAlreadyExistsException;
import org.lunarray.usermanager.repository.exceptions.EntityInvalidException;
import org.lunarray.usermanager.repository.exceptions.EntityNotFoundException;
import org.lunarray.usermanager.support.ModelLdapSupport;
import org.lunarray.usermanager.support.exceptions.ModelSupportException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Implements the {@link RoleRepository} using LDAP.
 * 
 * @author Pal Hargitai (pal@lunarray.org)
 */
public final class RoleRepositoryImpl
		implements RoleRepository {

	/** Validation message. */
	private static final String IDENTIFIER_NULL = "Identifier may not be null.";
	/** The logger. */
	private static final Logger LOGGER = LoggerFactory.getLogger(RoleRepositoryImpl.class);
	/** Validation message. */
	private static final String ROLE_NULL = "Role may not be null.";
	/** Validation message. */
	private static final String USER_IDENTIFIER_NULL = "User identifier may not be null.";
	/** The context factory. */
	private LdapContextFactory factory;
	/** The model support. */
	private ModelLdapSupport modelLdapSupport;

	/** {@inheritDoc} */
	@Override
	public void createRole(final Role role) throws BaseRepositoryException {
		RoleRepositoryImpl.LOGGER.debug("Creating role: {}", role);
		Validate.notNull(role, RoleRepositoryImpl.ROLE_NULL);
		SecurityUtils.getSubject().checkPermission(String.format("role:%s:write", role.getIdentifier()));
		LdapContext ctx = null;
		try {
			ctx = this.factory.getSystemLdapContext();
			final String name = this.modelLdapSupport.toQualifiedName(role);
			final Attributes attributes = this.modelLdapSupport.map(role);
			attributes.put(this.modelLdapSupport.mapObjectType(Role.class));
			ctx.bind(name, null, attributes);
		} catch (final NameAlreadyBoundException e) {
			throw new EntityAlreadyExistsException("Entity already exists.", e);
		} catch (final NamingException e) {
			throw new BaseRepositoryException("Could not process.", e);
		} catch (final ModelSupportException e) {
			throw new EntityInvalidException("Could not map entity.", e);
		} finally {
			if (!CheckUtil.isNull(ctx)) {
				try {
					ctx.close();
				} catch (final NamingException e) {
					RoleRepositoryImpl.LOGGER.warn("Could not close context.", e);
				}
			}
		}
		RoleRepositoryImpl.LOGGER.debug("Created role: {}", role);
	}

	/** {@inheritDoc} */
	@Override
	public void deleteRole(final String identifier) throws BaseRepositoryException {
		Validate.notNull(identifier, RoleRepositoryImpl.IDENTIFIER_NULL);
		RoleRepositoryImpl.LOGGER.debug("Deleting role: {}", identifier);
		SecurityUtils.getSubject().checkPermission(String.format("role:%s:write", identifier));
		LdapContext ctx = null;
		try {
			ctx = this.factory.getSystemLdapContext();
			final String name = this.modelLdapSupport.toQualifiedName(identifier, Role.class);
			ctx.unbind(name);
		} catch (final NameNotFoundException e) {
			throw new EntityNotFoundException("Entity does not exist.", e);
		} catch (final NamingException e) {
			throw new BaseRepositoryException("Could not process.", e);
		} catch (final ModelSupportException e) {
			throw new EntityInvalidException("Could not map entity.", e);
		} finally {
			if (!CheckUtil.isNull(ctx)) {
				try {
					ctx.close();
				} catch (final NamingException e) {
					RoleRepositoryImpl.LOGGER.warn("Could not close context.", e);
				}
			}
		}
		RoleRepositoryImpl.LOGGER.debug("Deleted role: {}", identifier);
	}

	/**
	 * Gets the value for the factory field.
	 * 
	 * @return The value for the factory field.
	 */
	public LdapContextFactory getFactory() {
		return this.factory;
	}

	/**
	 * Gets the value for the modelLdapSupport field.
	 * 
	 * @return The value for the modelLdapSupport field.
	 */
	public ModelLdapSupport getModelLdapSupport() {
		return this.modelLdapSupport;
	}

	/** {@inheritDoc} */
	@Override
	public Role getRole(final String identifier) throws BaseRepositoryException {
		Validate.notNull(identifier, RoleRepositoryImpl.IDENTIFIER_NULL);
		RoleRepositoryImpl.LOGGER.debug("Getting role: {}", identifier);
		SecurityUtils.getSubject().checkPermission(String.format("role:%s:read", identifier));
		Role result = null;
		LdapContext ctx = null;
		try {
			ctx = this.factory.getSystemLdapContext();
			final String name = this.modelLdapSupport.toQualifiedName(identifier, Role.class);
			result = this.modelLdapSupport.map(Role.class, ctx.getAttributes(name));
			final Iterator<String> usersIt = result.getUsers().iterator();
			while (usersIt.hasNext()) {
				if (!SecurityUtils.getSubject().isPermitted(String.format("user:%s:read", usersIt.next()))) {
					usersIt.remove();
				}
			}
		} catch (final NameNotFoundException e) {
			throw new EntityNotFoundException("Entity does not exist.", e);
		} catch (final NamingException e) {
			throw new BaseRepositoryException("Could not process.", e);
		} catch (final ModelSupportException e) {
			throw new EntityInvalidException("Could not map entity.", e);
		} finally {
			if (!CheckUtil.isNull(ctx)) {
				try {
					ctx.close();
				} catch (final NamingException e) {
					RoleRepositoryImpl.LOGGER.warn("Could not close context.", e);
				}
			}
		}
		RoleRepositoryImpl.LOGGER.debug("Got role {}: {}", identifier, result);
		return result;
	}

	/** {@inheritDoc} */
	@Override
	public List<String> getRoleIdentifiers() throws BaseRepositoryException {
		final List<String> result = new LinkedList<String>();
		RoleRepositoryImpl.LOGGER.debug("Get all role identifiers.");
		LdapContext ctx = null;
		try {
			ctx = this.factory.getSystemLdapContext();
			final NamingEnumeration<NameClassPair> results = ctx.list(this.modelLdapSupport.getSubTree(Role.class));
			while (results.hasMore()) {
				final String shortName = this.modelLdapSupport.toShortName(results.next().getName(), Role.class);
				if (SecurityUtils.getSubject().isPermitted(String.format("role:%s:read", shortName))) {
					result.add(shortName);
				}
			}
		} catch (final NamingException e) {
			throw new BaseRepositoryException("Could not process.", e);
		} catch (final ModelSupportException e) {
			throw new EntityInvalidException("Could not map entity.", e);
		} finally {
			if (!CheckUtil.isNull(ctx)) {
				try {
					ctx.close();
				} catch (final NamingException e) {
					RoleRepositoryImpl.LOGGER.warn("Could not close context.", e);
				}
			}
		}
		RoleRepositoryImpl.LOGGER.debug("Got all role identifiers: {}", result);
		return result;
	}

	/** {@inheritDoc} */
	@Override
	public List<Role> getRoles() throws BaseRepositoryException {
		final List<Role> result = new LinkedList<Role>();
		RoleRepositoryImpl.LOGGER.debug("Got all roles.");
		LdapContext ctx = null;
		try {
			ctx = this.factory.getSystemLdapContext();
			final NamingEnumeration<NameClassPair> results = ctx.list(this.modelLdapSupport.getSubTree(Role.class));
			while (results.hasMore()) {
				final String name = results.next().getNameInNamespace();
				final String shortName = this.modelLdapSupport.toShortName(name, Role.class);
				if (SecurityUtils.getSubject().isPermitted(String.format("role:%s:read", shortName))) {
					result.add(this.modelLdapSupport.map(Role.class, ctx.getAttributes(name)));
				}
			}
		} catch (final NamingException e) {
			throw new BaseRepositoryException("Could not process.", e);
		} catch (final ModelSupportException e) {
			throw new EntityInvalidException("Could not map entity.", e);
		} finally {
			if (!CheckUtil.isNull(ctx)) {
				try {
					ctx.close();
				} catch (final NamingException e) {
					RoleRepositoryImpl.LOGGER.warn("Could not close context.", e);
				}
			}
		}
		RoleRepositoryImpl.LOGGER.debug("Got all roles: {}", result);
		return result;
	}

	/** {@inheritDoc} */
	@Override
	public List<String> getRolesForUser(final String userIdentifier) throws BaseRepositoryException {
		Validate.notNull(userIdentifier, RoleRepositoryImpl.USER_IDENTIFIER_NULL);
		RoleRepositoryImpl.LOGGER.debug("Getting all roles for user: {}", userIdentifier);
		SecurityUtils.getSubject().checkPermission(String.format("user:%s:read", userIdentifier));
		final List<String> result = new LinkedList<String>();
		LdapContext ctx = null;
		try {
			ctx = this.factory.getSystemLdapContext();
			final BasicAttributes matchingAttributes = new BasicAttributes();
			final String qualifierName = this.modelLdapSupport.toQualifiedName(userIdentifier, User.class);
			final String attributeName = this.modelLdapSupport.resolveAttribute(Role.class, "users");
			matchingAttributes.put(new BasicAttribute(attributeName, qualifierName));
			final NamingEnumeration<SearchResult> searchResults = ctx.search(this.modelLdapSupport.getSubTree(Role.class),
					matchingAttributes);
			while (searchResults.hasMore()) {
				final String shortName = this.modelLdapSupport.toShortName(searchResults.next().getName(), Role.class);
				if (SecurityUtils.getSubject().isPermitted(String.format("role:%s:read", shortName))) {
					result.add(shortName);
				}
			}
		} catch (final NameNotFoundException e) {
			throw new EntityNotFoundException("Entity does not exist.", e);
		} catch (final NamingException e) {
			throw new BaseRepositoryException("Could not process.", e);
		} catch (final ModelSupportException e) {
			throw new EntityInvalidException("Could not map entity.", e);
		} finally {
			if (!CheckUtil.isNull(ctx)) {
				try {
					ctx.close();
				} catch (final NamingException e) {
					RoleRepositoryImpl.LOGGER.warn("Could not close context.", e);
				}
			}
		}
		RoleRepositoryImpl.LOGGER.debug("Got all roles for user {}: {}", userIdentifier, result);
		return result;
	}

	/**
	 * Sets a new value for the factory field.
	 * 
	 * @param factory
	 *            The new value for the factory field.
	 */
	public void setFactory(final LdapContextFactory factory) {
		this.factory = factory;
	}

	/**
	 * Sets a new value for the modelLdapSupport field.
	 * 
	 * @param modelLdapSupport
	 *            The new value for the modelLdapSupport field.
	 */
	public void setModelLdapSupport(final ModelLdapSupport modelLdapSupport) {
		this.modelLdapSupport = modelLdapSupport;
	}

	/** {@inheritDoc} */
	@Override
	public void setRolesForUser(final String userIdentifier, final List<String> roles) throws BaseRepositoryException {
		Validate.notNull(userIdentifier, RoleRepositoryImpl.USER_IDENTIFIER_NULL);
		Validate.notNull(roles, "Roles may not be null.");
		RoleRepositoryImpl.LOGGER.debug("Setting roles for user {}: {}", userIdentifier, roles);
		SecurityUtils.getSubject().checkPermission(String.format("user:%s:write", userIdentifier));
		final Set<String> currentRoles = new HashSet<String>(this.getRolesForUser(userIdentifier));
		final Set<String> futureRoles = new HashSet<String>(roles);
		futureRoles.removeAll(currentRoles);
		currentRoles.removeAll(roles);
		for (final String futureRole : futureRoles) {
			SecurityUtils.getSubject().checkPermission(String.format("role:%s:write", futureRole));
		}
		for (final String currentRole : currentRoles) {
			SecurityUtils.getSubject().checkPermission(String.format("role:%s:write", currentRole));
		}
		LdapContext ctx = null;
		try {
			ctx = this.factory.getSystemLdapContext();
			final String userName = this.modelLdapSupport.toQualifiedName(userIdentifier, User.class);
			final String attributeName = this.modelLdapSupport.resolveAttribute(Role.class, "users");
			for (final String futureRole : futureRoles) {
				RoleRepositoryImpl.LOGGER.debug("Added role {} to user {}", futureRole, userIdentifier);
				final String roleName = this.modelLdapSupport.toQualifiedName(futureRole, Role.class);
				ctx.modifyAttributes(roleName, DirContext.ADD_ATTRIBUTE, this.createAttribute(userName, attributeName));
			}
			for (final String currentRole : currentRoles) {
				RoleRepositoryImpl.LOGGER.debug("Removed role {} to user {}", currentRole, userIdentifier);
				final String roleName = this.modelLdapSupport.toQualifiedName(currentRole, Role.class);
				ctx.modifyAttributes(roleName, DirContext.REMOVE_ATTRIBUTE, this.createAttribute(userName, attributeName));
			}
		} catch (final NameNotFoundException e) {
			throw new EntityNotFoundException("Entity does not exist.", e);
		} catch (final NamingException e) {
			throw new BaseRepositoryException("Could not process.", e);
		} catch (final ModelSupportException e) {
			throw new EntityInvalidException("Could not map entity.", e);
		} finally {
			if (!CheckUtil.isNull(ctx)) {
				try {
					ctx.close();
				} catch (final NamingException e) {
					RoleRepositoryImpl.LOGGER.warn("Could not close context.", e);
				}
			}
		}
		RoleRepositoryImpl.LOGGER.debug("Set roles for user {}: {}", userIdentifier, roles);
	}

	/** {@inheritDoc} */
	@Override
	public void updateRole(final Role role) throws BaseRepositoryException {
		Validate.notNull(role, RoleRepositoryImpl.ROLE_NULL);
		RoleRepositoryImpl.LOGGER.debug("Updating role: {}", role);
		SecurityUtils.getSubject().checkPermission(String.format("role:%s:write", role.getIdentifier()));
		LdapContext ctx = null;
		try {
			ctx = this.factory.getSystemLdapContext();
			final String name = this.modelLdapSupport.toQualifiedName(role);
			final Attributes attributes = this.modelLdapSupport.map(role);
			attributes.put(this.modelLdapSupport.mapObjectType(Role.class));
			ctx.modifyAttributes(name, DirContext.REPLACE_ATTRIBUTE, attributes);
		} catch (final NameNotFoundException e) {
			throw new EntityNotFoundException("Entity does not exist.", e);
		} catch (final NamingException e) {
			throw new BaseRepositoryException("Could not process.", e);
		} catch (final ModelSupportException e) {
			throw new EntityInvalidException("Could not map entity.", e);
		} finally {
			if (!CheckUtil.isNull(ctx)) {
				try {
					ctx.close();
				} catch (final NamingException e) {
					RoleRepositoryImpl.LOGGER.warn("Could not close context.", e);
				}
			}
		}
		RoleRepositoryImpl.LOGGER.debug("Updated role: {}", role);
	}

	/** {@inheritDoc} */
	@Override
	public void updateRoleNoUsers(final Role role) throws BaseRepositoryException {
		Validate.notNull(role, RoleRepositoryImpl.ROLE_NULL);
		RoleRepositoryImpl.LOGGER.debug("Updating role (without users): {}", role);
		SecurityUtils.getSubject().checkPermission(String.format("role:%s:write", role.getIdentifier()));
		LdapContext ctx = null;
		try {
			ctx = this.factory.getSystemLdapContext();
			final String name = this.modelLdapSupport.toQualifiedName(role);
			final Attributes attributes = this.modelLdapSupport.mapWith(role, false, Collections.singleton("users"));
			attributes.put(this.modelLdapSupport.mapObjectType(Role.class));
			ctx.modifyAttributes(name, DirContext.REPLACE_ATTRIBUTE, attributes);
		} catch (final NameNotFoundException e) {
			throw new EntityNotFoundException("Entity does not exist.", e);
		} catch (final NamingException e) {
			throw new BaseRepositoryException("Could not process.", e);
		} catch (final ModelSupportException e) {
			throw new EntityInvalidException("Could not map entity.", e);
		} finally {
			if (!CheckUtil.isNull(ctx)) {
				try {
					ctx.close();
				} catch (final NamingException e) {
					RoleRepositoryImpl.LOGGER.warn("Could not close context.", e);
				}
			}
		}
		RoleRepositoryImpl.LOGGER.debug("Updated role (without users): {}", role);
	}

	/** {@inheritDoc} */
	@Override
	public void updateRoleUsers(final Role role) throws BaseRepositoryException {
		Validate.notNull(role, RoleRepositoryImpl.ROLE_NULL);
		RoleRepositoryImpl.LOGGER.debug("Updating role (just users): {}", role);
		SecurityUtils.getSubject().checkPermission(String.format("role:%s:write", role.getIdentifier()));
		LdapContext ctx = null;
		try {
			ctx = this.factory.getSystemLdapContext();
			final String name = this.modelLdapSupport.toQualifiedName(role);
			final Attributes attributes = this.modelLdapSupport.mapWith(role, true, Collections.singleton("users"));
			attributes.put(this.modelLdapSupport.mapObjectType(Role.class));
			ctx.modifyAttributes(name, DirContext.REPLACE_ATTRIBUTE, attributes);
		} catch (final NameNotFoundException e) {
			throw new EntityNotFoundException("Entity does not exist.", e);
		} catch (final NamingException e) {
			throw new BaseRepositoryException("Could not process.", e);
		} catch (final ModelSupportException e) {
			throw new EntityInvalidException("Could not map entity.", e);
		} finally {
			if (!CheckUtil.isNull(ctx)) {
				try {
					ctx.close();
				} catch (final NamingException e) {
					RoleRepositoryImpl.LOGGER.warn("Could not close context.", e);
				}
			}
		}
		RoleRepositoryImpl.LOGGER.debug("Updated role (just users): {}", role);
	}

	/**
	 * Creates a basic attribute.
	 * 
	 * @param userName
	 *            The user name.
	 * @param attributeName
	 *            The user name attribute.
	 * @return The attribute.
	 */
	private BasicAttributes createAttribute(final String userName, final String attributeName) {
		return new BasicAttributes(attributeName, userName);
	}
}
