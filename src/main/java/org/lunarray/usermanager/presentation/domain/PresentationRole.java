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
package org.lunarray.usermanager.presentation.domain;

import java.io.Serializable;

import javax.validation.constraints.Size;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.lunarray.model.descriptor.model.annotations.Key;
import org.lunarray.model.descriptor.presentation.annotations.EntityPresentationHint;
import org.lunarray.model.descriptor.presentation.annotations.PresentationHint;
import org.lunarray.model.descriptor.presentation.annotations.QualifierPresentationHint;
import org.lunarray.model.descriptor.util.BooleanInherit;
import org.lunarray.usermanager.presentation.domain.qualifiers.Create;

/**
 * Defines a role with users.
 * 
 * @author Pal Hargitai (pal@lunarray.org)
 */
@EntityPresentationHint(resourceBundle = "org.lunarray.usermanager.presentation.domain.Labels")
public final class PresentationRole
		implements Serializable {

	/** Minimal name legth. */
	private static final int NAME_MIN_LENGTH = 2;
	/** The serial id. */
	private static final long serialVersionUID = 2589485246182810561L;
	/** The display name. */
	@PresentationHint(order = 20)
	@Size(min = PresentationRole.NAME_MIN_LENGTH)
	private String displayName;
	/** The role identifier. */
	@Key
	@PresentationHint(immutable = BooleanInherit.TRUE, order = 0)
	@QualifierPresentationHint(hint = @PresentationHint(immutable = BooleanInherit.FALSE, order = 0), name = Create.class)
	@Size(min = PresentationRole.NAME_MIN_LENGTH)
	private String identifier;

	/**
	 * Default constructor.
	 */
	public PresentationRole() {
		// Default constructor.
	}

	/**
	 * Create a builder.
	 * 
	 * @return A builder.
	 */
	public static Builder createBuilder() {
		return new Builder();
	}

	/** {@inheritDoc} */
	@Override
	public boolean equals(final Object obj) {
		return EqualsBuilder.reflectionEquals(this, obj);
	}

	/**
	 * Gets the value for the displayName field.
	 * 
	 * @return The value for the displayName field.
	 */
	public String getDisplayName() {
		return this.displayName;
	}

	/**
	 * Gets the value for the identifier field.
	 * 
	 * @return The value for the identifier field.
	 */
	public String getIdentifier() {
		return this.identifier;
	}

	/** {@inheritDoc} */
	@Override
	public int hashCode() {
		return HashCodeBuilder.reflectionHashCode(this);
	}

	/**
	 * Sets a new value for the displayName field.
	 * 
	 * @param displayName
	 *            The new value for the displayName field.
	 */
	public void setDisplayName(final String displayName) {
		this.displayName = displayName;
	}

	/**
	 * Sets a new value for the identifier field.
	 * 
	 * @param identifier
	 *            The new value for the identifier field.
	 */
	public void setIdentifier(final String identifier) {
		this.identifier = identifier;
	}

	/** {@inheritDoc} */
	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this);
	}

	/**
	 * A builder.
	 * 
	 * @author Pal Hargitai (pal@lunarray.org)
	 */
	public static final class Builder {

		/** The display name. */
		private transient String displayNameBuilder;
		/** The role identifier. */
		private transient String identifierBuilder;

		/**
		 * Default constructor.
		 */
		protected Builder() {
			// Default constructor.
		}

		/**
		 * Build the role.
		 * 
		 * @return The role.
		 */
		public PresentationRole build() {
			final PresentationRole result = new PresentationRole();
			result.setIdentifier(this.identifierBuilder);
			result.setDisplayName(this.displayNameBuilder);
			return result;
		}

		/**
		 * Sets a new value for the displayNameBuilder field.
		 * 
		 * @param displayNameBuilder
		 *            The new value for the displayNameBuilder field.
		 * @return The builder.
		 */
		public Builder displayName(final String displayNameBuilder) {
			this.displayNameBuilder = displayNameBuilder;
			return this;
		}

		/**
		 * Sets a new value for the identifier field.
		 * 
		 * @param identifier
		 *            The new value for the identifier field.
		 * @return The builder.
		 */
		public Builder identifier(final String identifier) {
			this.identifierBuilder = identifier;
			return this;
		}
	}
}
