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
package org.lunarray.usermanager.support.shiro;

import java.util.LinkedList;
import java.util.List;

import javax.naming.InvalidNameException;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.NamingSecurityException;
import javax.naming.NoPermissionException;
import javax.naming.directory.Attributes;
import javax.naming.directory.BasicAttributes;
import javax.naming.directory.SearchResult;
import javax.naming.ldap.LdapContext;
import javax.naming.ldap.LdapName;
import javax.naming.ldap.Rdn;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.DisabledAccountException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.realm.ldap.LdapContextFactory;
import org.apache.shiro.realm.ldap.LdapUtils;
import org.apache.shiro.subject.PrincipalCollection;
import org.lunarray.common.check.CheckUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Shiro LDAP Realm.
 * 
 * @author Pal Hargitai (pal@lunarray.org)
 */
public final class LdapRealm
		extends AuthorizingRealm {

	/** The logger. */
	private static final Logger LOGGER = LoggerFactory.getLogger(LdapRealm.class);
	/** Ldap context factory. */
	private transient LdapContextFactory ldapContextFactory;
	/** Role id attribute. */
	private transient String roleAttribute;
	/** Role find criteria. */
	private transient String roleCriteria;
	/** Role subtree. */
	private transient List<Rdn> roleSubTree;
	/** System password for role lookups. */
	private transient Object systemPassword;
	/** System user for role lookups. */
	private transient Object systemUser;
	/** User id attribute. */
	private transient String userAttribute;
	/** A user permission resolver. */
	private transient UserPermissionResolver userPermissionResolver;
	/** User subtree. */
	private transient List<Rdn> userSubTree;

	/**
	 * Default constructor.
	 */
	public LdapRealm() {
		// Default constructor.
	}

	/**
	 * Sets a new value for the ldapContextFactory field.
	 * 
	 * @param ldapContextFactory
	 *            The new value for the ldapContextFactory field.
	 */
	public void setLdapContextFactory(final LdapContextFactory ldapContextFactory) {
		this.ldapContextFactory = ldapContextFactory;
	}

	/**
	 * Sets a new value for the roleAttribute field.
	 * 
	 * @param roleAttribute
	 *            The new value for the roleAttribute field.
	 */
	public void setRoleAttribute(final String roleAttribute) {
		this.roleAttribute = roleAttribute;
	}

	/**
	 * Sets a new value for the roleCriteria field.
	 * 
	 * @param roleCriteria
	 *            The new value for the roleCriteria field.
	 */
	public void setRoleCriteria(final String roleCriteria) {
		this.roleCriteria = roleCriteria;
	}

	/**
	 * Sets a new value for the roleSubTree field.
	 * 
	 * @param roleSubTree
	 *            The new value for the roleSubTree field.
	 */
	public void setRoleSubTree(final String roleSubTree) {
		try {
			this.roleSubTree = new LdapName(roleSubTree).getRdns();
		} catch (final InvalidNameException e) {
			throw new IllegalArgumentException("Invalid name.", e);
		}
	}

	/**
	 * Sets a new value for the systemPassword field.
	 * 
	 * @param systemPassword
	 *            The new value for the systemPassword field.
	 */
	public void setSystemPassword(final Object systemPassword) {
		this.systemPassword = systemPassword;
	}

	/**
	 * Sets a new value for the systemUser field.
	 * 
	 * @param systemUser
	 *            The new value for the systemUser field.
	 */
	public void setSystemUser(final Object systemUser) {
		this.systemUser = systemUser;
	}

	/**
	 * Sets a new value for the userAttribute field.
	 * 
	 * @param userAttribute
	 *            The new value for the userAttribute field.
	 */
	public void setUserAttribute(final String userAttribute) {
		this.userAttribute = userAttribute;
	}

	/**
	 * Sets a new value for the userPermissionResolver field.
	 * 
	 * @param userPermissionResolver
	 *            The new value for the userPermissionResolver field.
	 */
	public void setUserPermissionResolver(final UserPermissionResolver userPermissionResolver) {
		this.userPermissionResolver = userPermissionResolver;
	}

	/**
	 * Sets a new value for the userSubTree field.
	 * 
	 * @param userSubTree
	 *            The new value for the userSubTree field.
	 */
	public void setUserSubTree(final String userSubTree) {
		try {
			this.userSubTree = new LdapName(userSubTree).getRdns();
		} catch (final InvalidNameException e) {
			throw new IllegalArgumentException("Invalid name.", e);
		}
	}

	/** {@inheritDoc} */
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(final AuthenticationToken token) {
		final Object credentials = token.getCredentials();
		LdapRealm.LOGGER.debug("Authenticating token: {}", token);
		LdapContext ctx = null;
		SimpleAuthenticationInfo info = null;
		try {
			Object principal = token.getPrincipal();
			if (principal instanceof String) {
				final String sPrincipal = (String) principal;
				principal = this.toLdapName(sPrincipal);
			}
			LdapRealm.LOGGER.debug("Authenticating principal: {}", principal);
			ctx = this.ldapContextFactory.getLdapContext(principal, credentials);
			info = new SimpleAuthenticationInfo(token.getPrincipal(), token.getCredentials(), this.getName());
		} catch (final NoPermissionException e) {
			LdapRealm.LOGGER.warn("No permissions.", e);
			throw new DisabledAccountException(e);
		} catch (final javax.naming.AuthenticationException e) {
			LdapRealm.LOGGER.warn("Naming exception.", e);
			throw new IncorrectCredentialsException(e);
		} catch (final NamingSecurityException e) {
			LdapRealm.LOGGER.warn("Could not log in.", e);
			throw new AuthenticationException(e);
		} catch (final InvalidNameException e) {
			LdapRealm.LOGGER.warn("Could not log in.", e);
			throw new AuthenticationException(e);
		} catch (final NamingException e) {
			LdapRealm.LOGGER.warn("Could not log in.", e);
			throw new AuthenticationException(e);
		} finally {
			LdapUtils.closeContext(ctx);
		}
		LdapRealm.LOGGER.debug("Resolved authentication info for token {}: {}", token, info);
		return info;
	}

	/** {@inheritDoc} */
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(final PrincipalCollection principals) {
		Object principal = principals.getPrimaryPrincipal();
		LdapRealm.LOGGER.debug("Getting authorization info for principals: {}", principals);
		try {
			if (principal instanceof String) {
				principal = this.toLdapName((String) principal);
			}
			final LdapContext ctx = this.ldapContextFactory.getLdapContext(this.systemUser, this.systemPassword);
			final Attributes attributes = new BasicAttributes(this.roleCriteria, principal);
			final NamingEnumeration<SearchResult> results = ctx.search(new LdapName(this.roleSubTree), attributes);
			final SimpleAuthorizationInfo auth = new SimpleAuthorizationInfo();
			while (results.hasMore()) {
				final SearchResult result = results.next();
				final String role = result.getAttributes().get(this.roleAttribute).get().toString();
				auth.addRole(role);
				LdapRealm.LOGGER.debug("Found role {} for principal {}", role, principal);
				if (!CheckUtil.isNull(this.getRolePermissionResolver())) {
					auth.addObjectPermissions(this.getRolePermissionResolver().resolvePermissionsInRole(role));
				}
			}
			if (!CheckUtil.isNull(this.userPermissionResolver)) {
				auth.addObjectPermissions(this.userPermissionResolver.resolvePermissions(principals.getPrimaryPrincipal(), auth.getRoles()));
			}
			LdapRealm.LOGGER.debug("Resolved authorization info for principal {}: {}", principal, auth);
			return auth;
		} catch (final NamingException e) {
			throw new IllegalArgumentException("Could not search.", e);
		}
	}

	/**
	 * Convert a principal to an ldap name.
	 * 
	 * @param principal
	 *            The principal.
	 * @return The ldap name.
	 * @throws NamingException
	 *             Thrown if the name was invalid.
	 */
	private String toLdapName(final String principal) throws NamingException {
		final List<Rdn> rdn = new LinkedList<Rdn>(this.userSubTree);
		rdn.add(new Rdn(this.userAttribute, principal));
		return new LdapName(rdn).toString();
	}
}
