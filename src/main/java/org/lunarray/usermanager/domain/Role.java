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
package org.lunarray.usermanager.domain;

import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.lunarray.model.descriptor.model.annotations.Key;
import org.lunarray.model.descriptor.model.annotations.Reference;

/**
 * Defines a role with users.
 * 
 * @author Pal Hargitai (pal@lunarray.org)
 */
public final class Role {

	/** The display name. */
	private String displayName;
	/** The role identifier. */
	@Key
	private String identifier;
	/** The users of the role. */
	@Reference(User.class)
	private List<String> users;

	/**
	 * Default constructor.
	 */
	public Role() {
		this.users = new LinkedList<String>();
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

	/**
	 * Gets the value for the users field.
	 * 
	 * @return The value for the users field.
	 */
	public List<String> getUsers() {
		return this.users;
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

	/**
	 * Sets a new value for the users field.
	 * 
	 * @param users
	 *            The new value for the users field.
	 */
	public void setUsers(final List<String> users) {
		this.users = users;
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
		/** The users of the role. */
		private transient List<String> usersBuilder;

		/**
		 * Default constructor.
		 */
		protected Builder() {
			this.usersBuilder = new LinkedList<String>();
		}

		/**
		 * Build the role.
		 * 
		 * @return The role.
		 */
		public Role build() {
			final Role result = new Role();
			result.setIdentifier(this.identifierBuilder);
			result.setUsers(this.usersBuilder);
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

		/**
		 * Sets a new value for the users field.
		 * 
		 * @param users
		 *            The new value for the users field.
		 * @return The builder.
		 */
		public Builder users(final List<String> users) {
			this.usersBuilder = users;
			return this;
		}
	}
}
