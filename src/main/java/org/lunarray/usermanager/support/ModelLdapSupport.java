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
package org.lunarray.usermanager.support;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.naming.InvalidNameException;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.BasicAttribute;
import javax.naming.directory.BasicAttributes;
import javax.naming.ldap.LdapName;
import javax.naming.ldap.Rdn;

import org.apache.commons.lang.Validate;
import org.lunarray.model.descriptor.accessor.exceptions.ValueAccessException;
import org.lunarray.model.descriptor.converter.ConverterTool;
import org.lunarray.model.descriptor.converter.exceptions.ConverterException;
import org.lunarray.model.descriptor.creational.CreationException;
import org.lunarray.model.descriptor.model.Model;
import org.lunarray.model.descriptor.model.entity.EntityDescriptor;
import org.lunarray.model.descriptor.model.entity.KeyedEntityDescriptor;
import org.lunarray.model.descriptor.model.member.Cardinality;
import org.lunarray.model.descriptor.model.property.CollectionPropertyDescriptor;
import org.lunarray.model.descriptor.model.property.PropertyDescriptor;
import org.lunarray.model.descriptor.model.relation.RelationDescriptor;
import org.lunarray.usermanager.support.exceptions.ModelSupportException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Model LDAP support.
 * 
 * Maps to and from LDAP attributes based on a given Model and mappings.
 * 
 * @author Pal Hargitai (pal@lunarray.org)
 */
public final class ModelLdapSupport {

	/** The logger. */
	private static final Logger LOGGER = LoggerFactory.getLogger(ModelLdapSupport.class);
	/** The model. */
	private Model<Object> model;
	/** The Entity name to object classes mapping. */
	private Map<String, String[]> objectClasses;
	/** The Entity.Property name to attribute name mapping. */
	private Map<String, String[]> propertyAttributeMapping;
	/** The Entity to sub tree mapping. */
	private Map<String, List<Rdn>> subTreeMapping;

	/**
	 * Default constructor.
	 */
	public ModelLdapSupport() {
		// Default constructor.
	}

	/**
	 * Gets the subtree for a given type.
	 * 
	 * @param type
	 *            The type to get the subtree for. Entity must be described and
	 *            mapped.
	 * @return The subtree of the given type.
	 */
	public String getSubTree(final Class<?> type) {
		final EntityDescriptor<?> descriptor = this.model.getEntity(type);
		Validate.notNull(descriptor, "Entity must be described.");
		Validate.isTrue(this.subTreeMapping.containsKey(descriptor.getName()), "Entity must be mapped.");
		return new LdapName(this.subTreeMapping.get(descriptor.getName())).toString();
	}

	/**
	 * Map attributes to an entity.
	 * 
	 * @param type
	 *            The result entity. May not be null. Type must be mapped.
	 * @param attributes
	 *            The attributes used to map to an entity. May not be null.
	 * @return A new instance of the entity.
	 * @throws ModelSupportException
	 *             Thrown if the mapping could not be done.
	 * @param <E>
	 *            The entity type.
	 */
	public <E> E map(final Class<E> type, final Attributes attributes) throws ModelSupportException {
		Validate.notNull(type, "Type may not be null.");
		Validate.notNull(attributes, "Attributes may not be null.");
		final EntityDescriptor<E> descriptor = this.model.getEntity(type);
		Validate.notNull(descriptor, "Entity must be described.");
		E instance = null;
		try {
			instance = descriptor.createEntity();
		} catch (final CreationException e) {
			throw new ModelSupportException("Could not create entity.", e);
		}
		for (final PropertyDescriptor<?, E> property : descriptor.getProperties()) {
			final String key = this.resolveKey(descriptor, property);
			if (this.propertyAttributeMapping.containsKey(key)) {
				final String mapping = this.propertyAttributeMapping.get(key)[0];
				this.processProperty(instance, property, attributes.get(mapping));
			}
		}
		ModelLdapSupport.LOGGER.debug("Mapping attributes {} to {}", attributes, instance);
		return instance;
	}

	/**
	 * Map an instance to attributes
	 * 
	 * @param instance
	 *            The instance to map. May not be null.
	 * @return The mapped attributes.
	 * @throws ModelSupportException
	 *             Thrown if the mapping could not be done.
	 * @param <E>
	 *            The entity type.
	 */
	public <E> Attributes map(final E instance) throws ModelSupportException {
		Validate.notNull(instance, "Instance may not be null.");
		final Attributes attributes = new BasicAttributes();
		@SuppressWarnings("unchecked")
		final Class<E> type = (Class<E>) instance.getClass();
		final EntityDescriptor<E> descriptor = this.model.getEntity(type);
		Validate.notNull(descriptor, "Entity must be described.");
		for (final PropertyDescriptor<?, E> property : descriptor.getProperties()) {
			final String key = this.resolveKey(descriptor, property);
			if (this.propertyAttributeMapping.containsKey(key)) {
				final String[] mappings = this.propertyAttributeMapping.get(key);
				this.processProperty(instance, property, mappings, attributes);
			}
		}
		ModelLdapSupport.LOGGER.debug("Mapping entity {} to {}", instance, attributes);
		return attributes;
	}

	/**
	 * Map a type to an object class.
	 * 
	 * @param type
	 *            The type to map. May not be null.
	 * @return The object class mapping for this type.
	 */
	public Attribute mapObjectType(final Class<?> type) {
		Validate.notNull(type, "Type may not be null.");
		final BasicAttribute attr = new BasicAttribute("objectClass");
		final EntityDescriptor<?> descriptor = this.model.getEntity(type);
		Validate.notNull(descriptor, "Entity must be described.");
		Validate.isTrue(this.objectClasses.containsKey(descriptor.getName()), "Entity must be mapped.");
		final String[] classes = this.objectClasses.get(descriptor.getName());
		for (final String oc : classes) {
			attr.add(oc);
		}
		ModelLdapSupport.LOGGER.debug("Mapping type {} to {}", type, attr);
		return attr;
	}

	/**
	 * Map attributes to an entity.
	 * 
	 * @param type
	 *            The result entity. May not be null. Type must be mapped.
	 * @param attributes
	 *            The attributes used to map to an entity. May not be null.
	 * @param including
	 *            Indicates the given properties are to be included, otherwise
	 *            excluded.
	 * @param properties
	 *            The properties to in- or exclude.
	 * @return A new instance of the entity.
	 * @throws ModelSupportException
	 *             Thrown if the mapping could not be done.
	 * @param <E>
	 *            The entity type.
	 */
	public <E> E mapWith(final Class<E> type, final Attributes attributes, final boolean including, final Set<String> properties)
			throws ModelSupportException {
		Validate.notNull(type, "Type may not be null.");
		Validate.notNull(attributes, "Attributes may not be null.");
		final EntityDescriptor<E> descriptor = this.model.getEntity(type);
		Validate.notNull(descriptor, "Entity must be described.");
		E instance = null;
		try {
			ModelLdapSupport.LOGGER.debug("Creating entity of type {}", type);
			instance = descriptor.createEntity();
		} catch (final CreationException e) {
			throw new ModelSupportException("Could not create entity.", e);
		}
		for (final PropertyDescriptor<?, E> property : descriptor.getProperties()) {
			ModelLdapSupport.LOGGER.debug("Processing property {} for entity {} to entity", property, instance);
			if (properties.contains(property.getName()) == including) {
				final String key = this.resolveKey(descriptor, property);
				if (this.propertyAttributeMapping.containsKey(key)) {
					final String mapping = this.propertyAttributeMapping.get(key)[0];
					this.processProperty(instance, property, attributes.get(mapping));
				}
			}
		}
		return instance;
	}

	/**
	 * Map an instance to attributes
	 * 
	 * @param instance
	 *            The instance to map. May not be null.
	 * @param including
	 *            Indicates the given properties are to be included, otherwise
	 *            excluded.
	 * @param properties
	 *            The properties to in- or exclude.
	 * @return The mapped attributes.
	 * @throws ModelSupportException
	 *             Thrown if the mapping could not be done.
	 * @param <E>
	 *            The entity type.
	 */
	public <E> Attributes mapWith(final E instance, final boolean including, final Set<String> properties) throws ModelSupportException {
		Validate.notNull(instance, "Instance may not be null.");
		final Attributes attributes = new BasicAttributes();
		@SuppressWarnings("unchecked")
		final Class<E> type = (Class<E>) instance.getClass();
		final EntityDescriptor<E> descriptor = this.model.getEntity(type);
		Validate.notNull(descriptor, "Entity must be described.");
		for (final PropertyDescriptor<?, E> property : descriptor.getProperties()) {
			ModelLdapSupport.LOGGER.debug("Processing property {} for entity {} to attributes", property, instance);
			if (properties.contains(property.getName()) == including) {
				final String key = this.resolveKey(descriptor, property);
				if (this.propertyAttributeMapping.containsKey(key)) {
					final String[] mappings = this.propertyAttributeMapping.get(key);
					this.processProperty(instance, property, mappings, attributes);
				}
			}
		}
		return attributes;
	}

	/**
	 * Process a property to an instance.
	 * 
	 * @param instance
	 *            The instance to map to. May not be null.
	 * @param descriptor
	 *            The property descriptor. May not be null.
	 * @param attribute
	 *            The attribute to map. May not be null.
	 * @throws ModelSupportException
	 *             Thrown if the operation could not be completed.
	 * @param <P>
	 *            The property type.
	 * @param <E>
	 *            The entity type.
	 * @param <C>
	 *            The collection type.
	 */
	public <C, P extends Collection<C>, E> void processCollectionProperty(final E instance,
			final CollectionPropertyDescriptor<C, P, E> descriptor, final Attribute attribute) throws ModelSupportException {
		Validate.notNull(instance, "Instance may not be null.");
		Validate.notNull(descriptor, "Property descriptor may not be null.");
		Validate.notNull(attribute, "Attribute may not be null.");
		final ConverterTool tool = this.model.getExtension(ConverterTool.class);
		try {
			final NamingEnumeration<?> values = attribute.getAll();
			while (values.hasMore()) {
				final Object attributeValue = values.next();
				@SuppressWarnings("unchecked")
				final Class<Object> attributeValueType = (Class<Object>) attributeValue.getClass();
				String attributeStringValue;
				try {
					attributeStringValue = tool.convertToString(attributeValueType, attributeValue);
					if (descriptor.isRelation()) {
						final RelationDescriptor relationDescriptor = descriptor.adapt(RelationDescriptor.class);
						attributeStringValue = this.toShortName(attributeStringValue,
								this.model.getEntity(relationDescriptor.getRelatedName()).getEntityType());
					}
					final C value = tool.convertToInstance(descriptor.getCollectionType(), attributeStringValue);
					ModelLdapSupport.LOGGER.debug("Processed property {} from value {} to {}", descriptor, attributeStringValue, value);
					descriptor.addValue(instance, value);
				} catch (final ConverterException e) {
					throw new ModelSupportException("Could not convert value.", e);
				} catch (final ValueAccessException e) {
					throw new ModelSupportException("Could not access value.", e);
				}
			}
		} catch (final NamingException e) {
			throw new ModelSupportException("Could not access value.", e);
		}
	}

	/**
	 * Process property mapping.
	 * 
	 * @param instance
	 *            The entity instance. May not be null.
	 * @param descriptor
	 *            The property descriptor. May not be null.
	 * @param mappings
	 *            The attributes mapping. May not be null.
	 * @param attributes
	 *            The attributes to process to. May not be null.
	 * @throws ModelSupportException
	 *             Thrown if the operation could not be completed.
	 * @param <P>
	 *            The property type.
	 * @param <E>
	 *            The entity type.
	 * @param <C>
	 *            The collection type.
	 */
	public <C, P extends Collection<C>, E> void processCollectionProperty(final E instance,
			final CollectionPropertyDescriptor<C, P, E> descriptor, final String[] mappings, final Attributes attributes)
			throws ModelSupportException {
		Validate.notNull(instance, "Instance may not be null.");
		Validate.notNull(descriptor, "Property descriptor may not be null.");
		Validate.notNull(mappings, "Mapping may not be null.");
		Validate.notNull(attributes, "Attributes may not be null.");
		final ConverterTool tool = this.model.getExtension(ConverterTool.class);
		try {
			for (final String mapping : mappings) {
				final BasicAttribute attribute = new BasicAttribute(mapping);
				for (final C cValue : descriptor.getValue(instance)) {
					String attributeStringValue;
					try {
						attributeStringValue = tool.convertToString(descriptor.getCollectionType(), cValue);
						if (descriptor.isRelation()) {
							final RelationDescriptor relationDescriptor = descriptor.adapt(RelationDescriptor.class);
							attributeStringValue = this.toQualifiedName(attributeStringValue,
									this.model.getEntity(relationDescriptor.getRelatedName()).getEntityType());
						}
						ModelLdapSupport.LOGGER
								.debug("Processed property {} from value {} to {}", descriptor, cValue, attributeStringValue);
						attribute.add(attributeStringValue);
					} catch (final ConverterException e) {
						throw new ModelSupportException("Could not convert value.", e);
					}
				}
				attributes.put(attribute);
			}
		} catch (final ValueAccessException e) {
			throw new ModelSupportException("Could not access value.", e);
		}
	}

	/**
	 * Process a property to an instance.
	 * 
	 * @param instance
	 *            The instance to map to. May not be null.
	 * @param descriptor
	 *            The property descriptor. May not be null.
	 * @param attribute
	 *            The attribute to map. May not be null.
	 * @throws ModelSupportException
	 *             Thrown if the operation could not be completed.
	 * @param <P>
	 *            The property type.
	 * @param <E>
	 *            The entity type.
	 */
	public <P, E> void processProperty(final E instance, final PropertyDescriptor<P, E> descriptor, final Attribute attribute)
			throws ModelSupportException {
		Validate.notNull(instance, "Instance may not be null.");
		Validate.notNull(descriptor, "Property descriptor may not be null.");
		if (attribute != null) {
			final ConverterTool tool = this.model.getExtension(ConverterTool.class);
			if (descriptor.getCardinality() == Cardinality.MULTIPLE) {
				@SuppressWarnings("unchecked")
				final CollectionPropertyDescriptor<Object, Collection<Object>, E> collectionDescriptor = descriptor
						.adapt(CollectionPropertyDescriptor.class);
				this.processCollectionProperty(instance, collectionDescriptor, attribute);
			} else {
				Object attributeValue = null;
				try {
					attributeValue = attribute.get();
				} catch (final NamingException e) {
					throw new ModelSupportException("Could not access value.", e);
				}
				@SuppressWarnings("unchecked")
				final Class<Object> attributeValueType = (Class<Object>) attributeValue.getClass();
				String attributeStringValue;
				try {
					attributeStringValue = tool.convertToString(attributeValueType, attributeValue);
					if (descriptor.isRelation()) {
						final RelationDescriptor relationDescriptor = descriptor.adapt(RelationDescriptor.class);
						attributeStringValue = this.toShortName(attributeStringValue,
								this.model.getEntity(relationDescriptor.getRelatedName()).getEntityType());
					}
					final P value = tool.convertToInstance(descriptor.getPropertyType(), attributeStringValue);
					descriptor.setValue(instance, value);
					ModelLdapSupport.LOGGER.debug("Processed property {} from value {} to {}", descriptor, attributeStringValue, value);
				} catch (final ConverterException e) {
					throw new ModelSupportException("Could not convert value.", e);
				} catch (final ValueAccessException e) {
					throw new ModelSupportException("Could not access value.", e);
				}
			}
		}
	}

	/**
	 * Process property mapping.
	 * 
	 * @param instance
	 *            The entity instance. May not be null.
	 * @param descriptor
	 *            The property descriptor. May not be null.
	 * @param mappings
	 *            The attributes mapping. May not be null.
	 * @param attributes
	 *            The attributes to process to. May not be null.
	 * @throws ModelSupportException
	 *             Thrown if the operation could not be completed.
	 * @param <P>
	 *            The property type.
	 * @param <E>
	 *            The entity type.
	 */
	public <P, E> void processProperty(final E instance, final PropertyDescriptor<P, E> descriptor, final String[] mappings,
			final Attributes attributes) throws ModelSupportException {
		Validate.notNull(instance, "Instance may not be null.");
		Validate.notNull(descriptor, "Property descriptor may not be null.");
		Validate.notNull(mappings, "Mapping may not be null.");
		Validate.notNull(attributes, "Attributes may not be null.");
		final ConverterTool tool = this.model.getExtension(ConverterTool.class);
		if (descriptor.getCardinality() == Cardinality.MULTIPLE) {
			@SuppressWarnings("unchecked")
			final CollectionPropertyDescriptor<Object, Collection<Object>, E> collectionDescriptor = descriptor
					.adapt(CollectionPropertyDescriptor.class);
			this.processCollectionProperty(instance, collectionDescriptor, mappings, attributes);
		} else {
			P value = null;
			try {
				value = descriptor.getValue(instance);
			} catch (final ValueAccessException e) {
				throw new ModelSupportException("Could not access value.", e);
			}
			try {
				String attributeValue = tool.convertToString(descriptor.getPropertyType(), value);
				if (descriptor.isRelation()) {
					final RelationDescriptor relationDescriptor = descriptor.adapt(RelationDescriptor.class);
					attributeValue = this.toQualifiedName(attributeValue, this.model.getEntity(relationDescriptor.getRelatedName())
							.getEntityType());
				}
				for (final String mapping : mappings) {
					final BasicAttribute attr = new BasicAttribute(mapping);
					attr.add(attributeValue);
					attributes.put(attr);
					ModelLdapSupport.LOGGER.debug("Processed mapping {} for property {} with from value {} to {}", mapping, descriptor,
							value, attributeValue);
				}
			} catch (final ConverterException e) {
				throw new ModelSupportException("Could not convert value.", e);
			}
		}
	}

	/**
	 * Resolve the attribute name for a property.
	 * 
	 * @param type
	 *            The type. May not be null.
	 * @param propertyName
	 *            The property name. May not be null.
	 * @return The mapped attribute.
	 */
	public String resolveAttribute(final Class<?> type, final String propertyName) {
		Validate.notNull(type, "Type may not be null.");
		Validate.notNull(propertyName, "Property name may not be null.");
		final EntityDescriptor<?> descriptor = this.model.getEntity(type);
		Validate.notNull(descriptor, "Entity must be described.");
		final String key = new StringBuilder(descriptor.getName()).append('.').append(propertyName).toString();
		Validate.isTrue(this.propertyAttributeMapping.containsKey(key), "Property must be mapped.");
		final String result = this.propertyAttributeMapping.get(key)[0];
		ModelLdapSupport.LOGGER.debug("Resolved attribute {} for entity {} and property {}", result, type, propertyName);
		return result;
	}

	/**
	 * Resolve the attribute mapping key for a property of an entity.
	 * 
	 * @param entity
	 *            The entity. May not be null.
	 * @param property
	 *            The property. May not be null.
	 * @return The key.
	 */
	public String resolveKey(final EntityDescriptor<?> entity, final PropertyDescriptor<?, ?> property) {
		Validate.notNull(entity, "Entity descriptor may not be null.");
		Validate.notNull(property, "Property descriptor may not be null.");
		final String result = new StringBuilder().append(entity.getName()).append('.').append(property.getName()).toString();
		ModelLdapSupport.LOGGER.debug("Resolved key {} for entity {} and property {}", result, entity, property);
		return result;
	}

	/**
	 * Sets a new value for the model field.
	 * 
	 * @param model
	 *            The new value for the model field.
	 */
	public void setModel(final Model<Object> model) {
		Validate.notNull(model.getExtension(ConverterTool.class), "Model must contain a converter tool.");
		this.model = model;
	}

	/**
	 * Sets a new value for the objectClasses field.
	 * 
	 * @param objectClasses
	 *            The new value for the objectClasses field.
	 */
	public void setObjectClasses(final Map<String, String> objectClasses) {
		this.objectClasses = new HashMap<String, String[]>();
		for (final Map.Entry<String, String> value : objectClasses.entrySet()) {
			final String[] entryValue = value.getValue().split(",");
			Validate.isTrue(entryValue.length > 0, "Must map to at least 1 object class.");
			ModelLdapSupport.LOGGER.debug("Added object class mapping {} -> {}", value.getKey(), Arrays.toString(entryValue));
			this.objectClasses.put(value.getKey(), entryValue);
		}
	}

	/**
	 * Sets a new value for the propertyAttributeMapping field.
	 * 
	 * @param propertyAttributeMapping
	 *            The new value for the propertyAttributeMapping field.
	 */
	public void setPropertyAttributeMapping(final Map<String, String> propertyAttributeMapping) {
		this.propertyAttributeMapping = new HashMap<String, String[]>();
		for (final Map.Entry<String, String> value : propertyAttributeMapping.entrySet()) {
			final String[] entryValue = value.getValue().split(",");
			ModelLdapSupport.LOGGER.debug("Added property attribute mapping {} -> {}", value.getKey(), Arrays.toString(entryValue));
			Validate.isTrue(entryValue.length > 0, "Must map to at least 1 attribute.");
			this.propertyAttributeMapping.put(value.getKey(), entryValue);
		}
	}

	/**
	 * Sets a new value for the subTreeMapping field.
	 * 
	 * @param subTreeMapping
	 *            The new value for the subTreeMapping field.
	 */
	public void setSubTreeMapping(final Map<String, String> subTreeMapping) {
		this.subTreeMapping = new HashMap<String, List<Rdn>>();
		for (final Map.Entry<String, String> entry : subTreeMapping.entrySet()) {
			try {
				ModelLdapSupport.LOGGER.debug("Added sub tree mapping {} -> {}", entry.getKey(), entry.getValue());
				this.subTreeMapping.put(entry.getKey(), new LdapName(entry.getValue()).getRdns());
			} catch (final InvalidNameException e) {
				throw new IllegalArgumentException(e);
			}
		}
	}

	/**
	 * Extracts the qualified name from an entity.
	 * 
	 * @param entity
	 *            The entity. May not be null.
	 * @return The qualified name.
	 * @throws ModelSupportException
	 *             Thrown if the mapping could not be done.
	 * @param <E>
	 *            The entity type.
	 */
	public <E> String toQualifiedName(final E entity) throws ModelSupportException {
		Validate.notNull(entity, "Instance may not be null.");
		String result = null;
		@SuppressWarnings("unchecked")
		final EntityDescriptor<E> descriptor = this.model.getEntity((Class<E>) entity.getClass());
		if ((descriptor != null) && descriptor.adaptable(KeyedEntityDescriptor.class)) {
			@SuppressWarnings("unchecked")
			final KeyedEntityDescriptor<E, String> keyed = descriptor.adapt(KeyedEntityDescriptor.class);
			final PropertyDescriptor<String, E> keyProperty = keyed.getKeyProperty();
			if (String.class.equals(keyProperty.getPropertyType())) {
				final String propertyKey = this.resolveKey(descriptor, keyProperty);
				final List<Rdn> rnds = new LinkedList<Rdn>(this.subTreeMapping.get(descriptor.getName()));
				if (this.propertyAttributeMapping.containsKey(propertyKey)) {
					final String mapping = this.propertyAttributeMapping.get(propertyKey)[0];
					try {
						final String value = keyProperty.getValue(entity);
						rnds.add(new Rdn(mapping, value));
					} catch (final InvalidNameException e) {
						throw new ModelSupportException("Name is invalid.", e);
					} catch (final ValueAccessException e) {
						throw new ModelSupportException("Could not access value.", e);
					}
					result = new LdapName(rnds).toString();
				}
			}
		}
		ModelLdapSupport.LOGGER.debug("Resolved qualified name {} for {}", result, entity);
		return result;
	}

	/**
	 * Convert a short name to a qualified name.
	 * 
	 * @param value
	 *            The short name.
	 * @param type
	 *            The type. May not be null.
	 * @return The qualified name.
	 * @throws ModelSupportException
	 *             Thrown if the mapping could not be done.
	 */
	public String toQualifiedName(final String value, final Class<?> type) throws ModelSupportException {
		Validate.notNull(type, "Type may not be null.");
		String result = value;
		final EntityDescriptor<?> descriptor = this.model.getEntity(type);
		if ((descriptor != null) && descriptor.adaptable(KeyedEntityDescriptor.class)) {
			@SuppressWarnings("unchecked")
			final KeyedEntityDescriptor<?, String> keyed = descriptor.adapt(KeyedEntityDescriptor.class);
			final PropertyDescriptor<String, ?> keyProperty = keyed.getKeyProperty();
			if (String.class.equals(keyProperty.getPropertyType())) {
				final String propertyKey = this.resolveKey(descriptor, keyProperty);
				final List<Rdn> rnds = new LinkedList<Rdn>(this.subTreeMapping.get(descriptor.getName()));
				if (this.propertyAttributeMapping.containsKey(propertyKey)) {
					final String mapping = this.propertyAttributeMapping.get(propertyKey)[0];
					try {
						rnds.add(new Rdn(mapping, value));
					} catch (final InvalidNameException e) {
						throw new ModelSupportException("Name is invalid.", e);
					}
					result = new LdapName(rnds).toString();
				}
			}
		}
		ModelLdapSupport.LOGGER.debug("Transformed short name {} to {} for type {}", value, result, type);
		return result;
	}

	/**
	 * Convert a qualified name for a type to a short name, i.e. just the
	 * identifier.
	 * 
	 * @param qualifiedName
	 *            The qualified name.
	 * @param type
	 *            The type. May not be null.
	 * @return The short name.
	 * @throws ModelSupportException
	 *             Thrown if the mapping could not be done.
	 */
	public String toShortName(final String qualifiedName, final Class<?> type) throws ModelSupportException {
		Validate.notNull(type, "Type may not be null.");
		String result = qualifiedName;
		final EntityDescriptor<?> descriptor = this.model.getEntity(type);
		if ((descriptor != null) && descriptor.adaptable(KeyedEntityDescriptor.class)) {
			@SuppressWarnings("unchecked")
			final KeyedEntityDescriptor<?, String> keyed = descriptor.adapt(KeyedEntityDescriptor.class);
			final PropertyDescriptor<String, ?> keyProperty = keyed.getKeyProperty();
			if (String.class.equals(keyProperty.getPropertyType())) {
				final String propertyKey = this.resolveKey(descriptor, keyProperty);
				try {
					final LdapName name = new LdapName(qualifiedName);
					final Iterator<Rdn> rdnIt = new LinkedList<Rdn>(name.getRdns()).descendingIterator();
					if (this.propertyAttributeMapping.containsKey(propertyKey)) {
						final String[] mapping = this.propertyAttributeMapping.get(propertyKey);
						final Set<String> mappingSet = new HashSet<String>();
						for (final String m : mapping) {
							mappingSet.add(m);
						}
						while (rdnIt.hasNext()) {
							final Rdn next = rdnIt.next();
							if (mappingSet.contains(next.getType())) {
								result = next.getValue().toString();
								break;
							}
						}
					}
				} catch (final InvalidNameException e) {
					throw new ModelSupportException("Name is invalid.", e);
				}
			}
		}
		ModelLdapSupport.LOGGER.debug("Transformed qualified name {} to {} for type {}", qualifiedName, result, type);
		return result;
	}
}
