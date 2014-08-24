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

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

import org.lunarray.common.check.CheckUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.core.io.Resource;
import org.springframework.util.StreamUtils;

/**
 * Factory that converts a {@link Resource} to a {@link String}.
 * 
 * @author Pal Hargitai (pal@lunarray.org)
 */
public final class ResourceStringFactory
		implements FactoryBean<String> {

	/** The logger. */
	private static final Logger LOGGER = LoggerFactory.getLogger(ResourceStringFactory.class);
	/** The resource. */
	private transient Resource resource;
	/** The resource result. */
	private transient String value;

	/**
	 * Default constructor.
	 */
	public ResourceStringFactory() {
		// Default constructor.
	}

	/** {@inheritDoc} */
	@Override
	public String getObject() {
		return this.value;
	}

	/** {@inheritDoc} */
	@Override
	public Class<?> getObjectType() {
		return String.class;
	}

	/**
	 * Does the conversion.
	 */
	public void init() {
		InputStream input = null;
		try {
			ResourceStringFactory.LOGGER.debug("Reading resource: {}", this.resource);
			input = this.resource.getInputStream();
			this.value = StreamUtils.copyToString(input, Charset.defaultCharset());
			ResourceStringFactory.LOGGER.debug("Read resource: {} to value: {}", this.resource, this.value);
		} catch (final IOException e) {
			throw new IllegalArgumentException("Could not read resource.", e);
		} finally {
			if (!CheckUtil.isNull(input)) {
				try {
					input.close();
				} catch (final IOException e) {
					ResourceStringFactory.LOGGER.warn("Could not close stream.", e);
				}
			}
		}
	}

	/** {@inheritDoc} */
	@Override
	public boolean isSingleton() {
		return true;
	}

	/**
	 * Sets a new value for the resource field.
	 * 
	 * @param resource
	 *            The new value for the resource field.
	 */
	public void setResource(final Resource resource) {
		this.resource = resource;
	}
}
