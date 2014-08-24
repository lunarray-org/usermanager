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
package org.lunarray.usermanager.support.spring;

import org.lunarray.usermanager.support.ConfigurationValue;
import org.springframework.beans.factory.FactoryBean;

/**
 * 
 * @author Pal Hargitai (pal@lunarray.org)
 */
public final class ConfigurationValueFactoryBean
		implements FactoryBean<String> {

	/** The delegating configuration value. */
	private transient ConfigurationValue configurationValue;

	/**
	 * Gets the value for the configurationValue field.
	 * 
	 * @return The value for the configurationValue field.
	 */
	public ConfigurationValue getConfigurationValue() {
		return this.configurationValue;
	}

	/** {@inheritDoc} */
	@Override
	public String getObject() {
		return this.configurationValue.get();
	}

	/** {@inheritDoc} */
	@Override
	public Class<?> getObjectType() {
		return String.class;
	}

	/** {@inheritDoc} */
	@Override
	public boolean isSingleton() {
		return false;
	}

	/**
	 * Sets a new value for the configurationValue field.
	 * 
	 * @param configurationValue
	 *            The new value for the configurationValue field.
	 */
	public void setConfigurationValue(final ConfigurationValue configurationValue) {
		this.configurationValue = configurationValue;
	}
}
