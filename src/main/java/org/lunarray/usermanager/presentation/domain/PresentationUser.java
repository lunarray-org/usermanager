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
 * Defines a user.
 * 
 * @author Pal Hargitai (pal@lunarray.org)
 */
@EntityPresentationHint(resourceBundle = "org.lunarray.usermanager.presentation.domain.Labels")
public final class PresentationUser
		implements Serializable {

	/** Name minimal length. */
	private static final int NAME_MIN_LENGTH = 2;
	/** Serial id. */
	private static final long serialVersionUID = 5932564282977694747L;
	/** The display name. */
	@PresentationHint(order = 10)
	@Size(min = PresentationUser.NAME_MIN_LENGTH)
	private String displayName;
	/** The users first name. */
	@PresentationHint(order = 12)
	@Size(min = PresentationUser.NAME_MIN_LENGTH)
	private String firstName;
	/** The user identifier. */
	@Key
	@PresentationHint(immutable = BooleanInherit.TRUE, order = 0)
	@QualifierPresentationHint(hint = @PresentationHint(immutable = BooleanInherit.FALSE, order = 0), name = Create.class)
	@Size(min = PresentationUser.NAME_MIN_LENGTH)
	private String identifier;
	/** The users last name. */
	@PresentationHint(order = 14)
	@Size(min = PresentationUser.NAME_MIN_LENGTH)
	private String lastName;
	/** The users email address. */
	@PresentationHint(order = 18)
	@Size(min = PresentationUser.NAME_MIN_LENGTH)
	private String mail;

	/**
	 * Default constructor.
	 */
	public PresentationUser() {
		// Default constructor.
	}

	/**
	 * Creates a builder.
	 * 
	 * @return The builder.
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
	 * Gets the value for the firstName field.
	 * 
	 * @return The value for the firstName field.
	 */
	public String getFirstName() {
		return this.firstName;
	}

	/**
	 * Gets the value for the identifier field.
	 * 
	 * @return The value for the identifier field.
	 */
	public String getIdentifier() {
		return this.identifier;
	}

	/**
	 * Gets the value for the lastName field.
	 * 
	 * @return The value for the lastName field.
	 */
	public String getLastName() {
		return this.lastName;
	}

	/**
	 * Gets the value for the mail field.
	 * 
	 * @return The value for the mail field.
	 */
	public String getMail() {
		return this.mail;
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
	 * Sets a new value for the firstName field.
	 * 
	 * @param firstName
	 *            The new value for the firstName field.
	 */
	public void setFirstName(final String firstName) {
		this.firstName = firstName;
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

	/**
	 * Sets a new value for the lastName field.
	 * 
	 * @param lastName
	 *            The new value for the lastName field.
	 */
	public void setLastName(final String lastName) {
		this.lastName = lastName;
	}

	/**
	 * Sets a new value for the mail field.
	 * 
	 * @param mail
	 *            The new value for the mail field.
	 */
	public void setMail(final String mail) {
		this.mail = mail;
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
		/** The users first name. */
		private transient String firstNameBuilder;
		/** The user identifier. */
		private transient String identifierBuilder;
		/** The users last name. */
		private transient String lastNameBuilder;
		/** The users email address. */
		private transient String mailBuilder;

		/**
		 * Default constructor.
		 */
		protected Builder() {
			// Default constructor.
		}

		/**
		 * Build the entity.
		 * 
		 * @return The entity.
		 */
		public PresentationUser build() {
			final PresentationUser user = new PresentationUser();
			user.setDisplayName(this.displayNameBuilder);
			user.setFirstName(this.firstNameBuilder);
			user.setIdentifier(this.identifierBuilder);
			user.setLastName(this.lastNameBuilder);
			user.setMail(this.mailBuilder);
			return user;
		}

		/**
		 * Sets a new value for the displayName field.
		 * 
		 * @param displayName
		 *            The new value for the displayName field.
		 * @return The builder.
		 */
		public Builder displayName(final String displayName) {
			this.displayNameBuilder = displayName;
			return this;
		}

		/**
		 * Sets a new value for the firstName field.
		 * 
		 * @param firstName
		 *            The new value for the firstName field.
		 * @return The builder.
		 */
		public Builder firstName(final String firstName) {
			this.firstNameBuilder = firstName;
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

		/**
		 * Sets a new value for the lastName field.
		 * 
		 * @param lastName
		 *            The new value for the lastName field.
		 * @return The builder.
		 */
		public Builder lastName(final String lastName) {
			this.lastNameBuilder = lastName;
			return this;
		}

		/**
		 * Sets a new value for the mail field.
		 * 
		 * @param mail
		 *            The new value for the mail field.
		 * @return The builder.
		 */
		public Builder mail(final String mail) {
			this.mailBuilder = mail;
			return this;
		}
	}
}
