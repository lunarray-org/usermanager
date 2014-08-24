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

import java.nio.charset.Charset;
import java.util.LinkedList;
import java.util.List;

import javax.naming.NameAlreadyBoundException;
import javax.naming.NameClassPair;
import javax.naming.NameNotFoundException;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attributes;
import javax.naming.directory.BasicAttribute;
import javax.naming.directory.BasicAttributes;
import javax.naming.directory.DirContext;
import javax.naming.ldap.LdapContext;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.Validate;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.realm.ldap.LdapContextFactory;
import org.lunarray.common.check.CheckUtil;
import org.lunarray.usermanager.domain.User;
import org.lunarray.usermanager.repository.UserRepository;
import org.lunarray.usermanager.repository.exceptions.BaseRepositoryException;
import org.lunarray.usermanager.repository.exceptions.EntityAlreadyExistsException;
import org.lunarray.usermanager.repository.exceptions.EntityInvalidException;
import org.lunarray.usermanager.repository.exceptions.EntityNotFoundException;
import org.lunarray.usermanager.support.ModelLdapSupport;
import org.lunarray.usermanager.support.exceptions.ModelSupportException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Implements the {@link UserRepository} using LDAP.
 * 
 * @author Pal Hargitai (pal@lunarray.org)
 */
public final class UserRepositoryImpl
		implements UserRepository {

	/** Validation message. */
	private static final String IDENTIFIER_NULL = "Identifier may not be null.";
	/** The logger. */
	private static final Logger LOGGER = LoggerFactory.getLogger(UserRepositoryImpl.class);
	/** Validation message. */
	private static final String USER_NULL = "User may not be null.";
	/** The context factory. */
	private LdapContextFactory factory;
	/** The model support. */
	private ModelLdapSupport modelLdapSupport;

	/**
	 * Default constructor.
	 */
	public UserRepositoryImpl() {
		// Default constructor.
	}

	/** {@inheritDoc} */
	@Override
	public boolean containsUser(final String identifier) throws BaseRepositoryException {
		UserRepositoryImpl.LOGGER.debug("Testing contains user: {}", identifier);
		Validate.notNull(identifier, UserRepositoryImpl.IDENTIFIER_NULL);
		boolean result = false;
		LdapContext ctx = null;
		try {
			ctx = this.factory.getSystemLdapContext();
			final String name = this.modelLdapSupport.toQualifiedName(identifier, User.class);
			ctx.getAttributes(name);
			result = true;
		} catch (final NameNotFoundException e) {
			result = false;
		} catch (final NamingException e) {
			throw new BaseRepositoryException("Could not process.", e);
		} catch (final ModelSupportException e) {
			throw new EntityInvalidException("Could not map entity.", e);
		} finally {
			if (!CheckUtil.isNull(ctx)) {
				try {
					ctx.close();
				} catch (final NamingException e) {
					UserRepositoryImpl.LOGGER.warn("Could not close context.", e);
				}
			}
		}
		UserRepositoryImpl.LOGGER.debug("Tested contains user {}: {}", identifier, result);
		return result;
	}

	/** {@inheritDoc} */
	@Override
	public void createUser(final User user) throws BaseRepositoryException {
		UserRepositoryImpl.LOGGER.debug("Creating user: {}", user);
		Validate.notNull(user, UserRepositoryImpl.USER_NULL);
		SecurityUtils.getSubject().checkPermission(String.format("user:%s:write", user.getIdentifier()));
		LdapContext ctx = null;
		try {
			ctx = this.factory.getSystemLdapContext();
			final String name = this.modelLdapSupport.toQualifiedName(user);
			final Attributes attributes = this.modelLdapSupport.map(user);
			attributes.put(this.modelLdapSupport.mapObjectType(User.class));
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
					UserRepositoryImpl.LOGGER.warn("Could not close context.", e);
				}
			}
		}
		UserRepositoryImpl.LOGGER.debug("Created user: {}", user);
	}

	/** {@inheritDoc} */
	@Override
	public void deleteUser(final String identifier) throws BaseRepositoryException {
		Validate.notNull(identifier, UserRepositoryImpl.IDENTIFIER_NULL);
		SecurityUtils.getSubject().checkPermission(String.format("user:%s:write", identifier));
		LdapContext ctx = null;
		try {
			ctx = this.factory.getSystemLdapContext();
			final String name = this.modelLdapSupport.toQualifiedName(identifier, User.class);
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
					UserRepositoryImpl.LOGGER.warn("Could not close context.", e);
				}
			}
		}
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
	public User getUser(final String identifier) throws BaseRepositoryException {
		Validate.notNull(identifier, UserRepositoryImpl.IDENTIFIER_NULL);
		UserRepositoryImpl.LOGGER.debug("Getting user: {}", identifier);
		SecurityUtils.getSubject().checkPermission(String.format("user:%s:read", identifier));
		User result = null;
		LdapContext ctx = null;
		try {
			ctx = this.factory.getSystemLdapContext();
			final String name = this.modelLdapSupport.toQualifiedName(identifier, User.class);
			result = this.modelLdapSupport.map(User.class, ctx.getAttributes(name));
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
					UserRepositoryImpl.LOGGER.warn("Could not close context.", e);
				}
			}
		}
		UserRepositoryImpl.LOGGER.debug("Got user {}: {}", identifier, result);
		return result;
	}

	/** {@inheritDoc} */
	@Override
	public List<String> getUserIdentifiers() throws BaseRepositoryException {
		final List<String> result = new LinkedList<String>();
		UserRepositoryImpl.LOGGER.debug("Getting all user identifiers.");
		LdapContext ctx = null;
		try {
			ctx = this.factory.getSystemLdapContext();
			final NamingEnumeration<NameClassPair> results = ctx.list(this.modelLdapSupport.getSubTree(User.class));
			while (results.hasMore()) {
				final String shortName = this.modelLdapSupport.toShortName(results.next().getName(), User.class);
				if (SecurityUtils.getSubject().isPermitted(String.format("user:%s:read", shortName))) {
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
					UserRepositoryImpl.LOGGER.warn("Could not close context.", e);
				}
			}
		}
		UserRepositoryImpl.LOGGER.debug("Get all user identifiers: {}", result);
		return result;
	}

	/** {@inheritDoc} */
	@Override
	public List<User> getUsers() throws BaseRepositoryException {
		final List<User> result = new LinkedList<User>();
		UserRepositoryImpl.LOGGER.debug("Getting all users.");
		LdapContext ctx = null;
		try {
			ctx = this.factory.getSystemLdapContext();
			final NamingEnumeration<NameClassPair> results = ctx.list(this.modelLdapSupport.getSubTree(User.class));
			while (results.hasMore()) {
				final String name = results.next().getNameInNamespace();
				final String shortName = this.modelLdapSupport.toShortName(name, User.class);
				if (SecurityUtils.getSubject().isPermitted(String.format("user:%s:read", shortName))) {
					result.add(this.modelLdapSupport.map(User.class, ctx.getAttributes(name)));
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
					// Silently fail.
				}
			}
		}
		UserRepositoryImpl.LOGGER.debug("Got all users: {}", result);
		return result;
	}

	/** {@inheritDoc} */
	@Override
	public User getUserUnsecured(final String identifier) throws BaseRepositoryException {
		Validate.notNull(identifier, UserRepositoryImpl.IDENTIFIER_NULL);
		UserRepositoryImpl.LOGGER.info("Getting user (unsecured!): {}", identifier);
		User result = null;
		LdapContext ctx = null;
		try {
			ctx = this.factory.getSystemLdapContext();
			final String name = this.modelLdapSupport.toQualifiedName(identifier, User.class);
			result = this.modelLdapSupport.map(User.class, ctx.getAttributes(name));
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
					UserRepositoryImpl.LOGGER.warn("Could not close context.", e);
				}
			}
		}
		UserRepositoryImpl.LOGGER.info("Got user (unsecured!) {}: {}", identifier, result);
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
	public void updateUser(final User user) throws BaseRepositoryException {
		Validate.notNull(user, UserRepositoryImpl.USER_NULL);
		UserRepositoryImpl.LOGGER.debug("Updating user: {}", user);
		SecurityUtils.getSubject().checkPermission(String.format("user:%s:write", user.getIdentifier()));
		LdapContext ctx = null;
		try {
			ctx = this.factory.getSystemLdapContext();
			final String name = this.modelLdapSupport.toQualifiedName(user);
			final Attributes attributes = this.modelLdapSupport.map(user);
			attributes.put(this.modelLdapSupport.mapObjectType(User.class));
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
					UserRepositoryImpl.LOGGER.warn("Could not close context.", e);
				}
			}
		}
		UserRepositoryImpl.LOGGER.debug("Updated user: {}", user);
	}

	/** {@inheritDoc} */
	@Override
	public void updateUserPassword(final String identifier, final byte[] passwordHash, final String algorithm)
			throws BaseRepositoryException {
		Validate.notNull(identifier, UserRepositoryImpl.IDENTIFIER_NULL);
		Validate.notNull(passwordHash, "Hash may not be null.");
		Validate.notNull(algorithm, "Algorithm may not be null.");
		UserRepositoryImpl.LOGGER.debug("Updating password for user: {}", identifier);
		SecurityUtils.getSubject().checkPermission(String.format("password:%s:modify", identifier));
		LdapContext ctx = null;
		try {
			ctx = this.factory.getSystemLdapContext();
			final String name = this.modelLdapSupport.toQualifiedName(identifier, User.class);
			final Attributes attributes = new BasicAttributes();
			final StringBuilder builder = new StringBuilder().append('{').append(algorithm).append('}');
			builder.append(new String(Base64.encodeBase64(passwordHash), Charset.forName("ASCII")));
			attributes.put(new BasicAttribute(this.modelLdapSupport.resolveAttribute(User.class, "password"), builder.toString()));
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
					UserRepositoryImpl.LOGGER.warn("Could not close context.", e);
				}
			}
		}
		UserRepositoryImpl.LOGGER.debug("Updated password for user: {}", identifier);
	}
}
