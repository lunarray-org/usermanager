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

import org.lunarray.model.descriptor.builder.annotation.simple.SimpleBuilder;
import org.lunarray.model.descriptor.model.Model;
import org.lunarray.model.descriptor.registry.Registry;
import org.lunarray.model.descriptor.resource.Resource;
import org.lunarray.model.descriptor.resource.ResourceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.FactoryBean;

/**
 * Builds a simple model.
 * 
 * @author Pal Hargitai (pal@lunarray.org)
 */
public final class SimpleModelFactory
		implements FactoryBean<Model<Object>> {

	/** The logger. */
	private static final Logger LOGGER = LoggerFactory.getLogger(SimpleModelFactory.class);
	/** A registry. */
	private transient Registry<String> registry;
	/** A resource. */
	private transient Resource<Class<? extends Object>> resource;

	/**
	 * Default constructor.
	 */
	public SimpleModelFactory() {
		// Default constructor.
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @throws ResourceException
	 */
	@Override
	public Model<Object> getObject() throws ResourceException {
		final Model<Object> result = SimpleBuilder.createBuilder().extensions(this.registry).resources(this.resource).build();
		SimpleModelFactory.LOGGER.debug("Created model: {}", result);
		return result;
	}

	/** {@inheritDoc} */
	@Override
	public Class<?> getObjectType() {
		return Model.class;
	}

	/** {@inheritDoc} */
	@Override
	public boolean isSingleton() {
		return false;
	}

	/**
	 * Sets a new value for the registry field.
	 * 
	 * @param registry
	 *            The new value for the registry field.
	 */
	public void setRegistry(final Registry<String> registry) {
		this.registry = registry;
	}

	/**
	 * Sets a new value for the resource field.
	 * 
	 * @param resource
	 *            The new value for the resource field.
	 */
	public void setResource(final Resource<Class<? extends Object>> resource) {
		this.resource = resource;
	}
}
