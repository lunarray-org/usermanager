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

import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.NamingException;
import javax.naming.ldap.InitialLdapContext;
import javax.naming.ldap.LdapContext;

import org.apache.shiro.realm.ldap.JndiLdapContextFactory;
import org.apache.shiro.realm.ldap.LdapContextFactory;
import org.lunarray.usermanager.support.ConfigurationValue;

/**
 * An LDAP context based on a dynamic query.
 * 
 * @author Pal Hargitai (pal@lunarray.org)
 */
public final class DynamicLdapContextFactory
		extends JndiLdapContextFactory
		implements LdapContextFactory {

	/** The dynamic LDAP url. */
	private ConfigurationValue dynamicUrl;

	/**
	 * Default constructor.
	 */
	public DynamicLdapContextFactory() {
		// Default constructor.
	}

	/**
	 * Gets the value for the dynamicUrl field.
	 * 
	 * @return The value for the dynamicUrl field.
	 */
	public ConfigurationValue getDynamicUrl() {
		return this.dynamicUrl;
	}

	/**
	 * Sets a new value for the dynamicUrl field.
	 * 
	 * @param dynamicUrl
	 *            The new value for the dynamicUrl field.
	 */
	public void setDynamicUrl(final ConfigurationValue dynamicUrl) {
		this.dynamicUrl = dynamicUrl;
	}

	/** {@inheritDoc} */
	@SuppressWarnings("unchecked")
	@Override
	protected LdapContext createLdapContext(@SuppressWarnings("rawtypes") final Hashtable env) throws NamingException {
		env.put(Context.PROVIDER_URL, this.dynamicUrl.get(env.get(Context.PROVIDER_URL).toString()));
		return new InitialLdapContext(env, null);
	}
}
