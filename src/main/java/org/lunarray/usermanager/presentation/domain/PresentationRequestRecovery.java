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
import java.util.Date;

import org.lunarray.common.check.CheckUtil;
import org.lunarray.model.descriptor.model.annotations.Key;
import org.lunarray.model.descriptor.presentation.annotations.EntityPresentationHint;
import org.lunarray.model.descriptor.presentation.annotations.PresentationHint;
import org.lunarray.model.descriptor.presentation.annotations.QualifierPresentationHint;
import org.lunarray.model.descriptor.util.BooleanInherit;
import org.lunarray.usermanager.presentation.domain.qualifiers.Create;

/**
 * The presentation entity.
 * 
 * @author Pal Hargitai (pal@lunarray.org)
 */
@EntityPresentationHint(resourceBundle = "org.lunarray.usermanager.presentation.domain.Labels")
public final class PresentationRequestRecovery
		implements Serializable {

	/** Serial id. */
	private static final long serialVersionUID = -1301949424574065847L;
	/** The request identifier. */
	@Key
	@QualifierPresentationHint(name = Create.class, hint = @PresentationHint(visible = BooleanInherit.FALSE))
	@PresentationHint(order = 10)
	private String identifier;
	/** The request date. */
	@QualifierPresentationHint(name = Create.class, hint = @PresentationHint(visible = BooleanInherit.FALSE))
	@PresentationHint(order = 30)
	private Date requestDate;
	/** The user name. */
	@PresentationHint(order = 20)
	private String username;

	/**
	 * Default constructor.
	 */
	public PresentationRequestRecovery() {
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

	/**
	 * Gets the value for the identifier field.
	 * 
	 * @return The value for the identifier field.
	 */
	public String getIdentifier() {
		return this.identifier;
	}

	/**
	 * Gets the value for the requestDate field.
	 * 
	 * @return The value for the requestDate field.
	 */
	public Date getRequestDate() {
		final Date result = null;
		if (!CheckUtil.isNull(this.requestDate)) {
			this.requestDate = new Date(this.requestDate.getTime());
		}
		return result;
	}

	/**
	 * Gets the value for the username field.
	 * 
	 * @return The value for the username field.
	 */
	public String getUsername() {
		return this.username;
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
	 * Sets a new value for the requestDate field.
	 * 
	 * @param requestDate
	 *            The new value for the requestDate field.
	 */
	public void setRequestDate(final Date requestDate) {
		if (CheckUtil.isNull(requestDate)) {
			this.requestDate = null;
		} else {
			this.requestDate = new Date(requestDate.getTime());
		}
	}

	/**
	 * Sets a new value for the username field.
	 * 
	 * @param username
	 *            The new value for the username field.
	 */
	public void setUsername(final String username) {
		this.username = username;
	}

	/**
	 * The builder.
	 * 
	 * @author Pal Hargitai (pal@lunarray.org)
	 */
	public static final class Builder {
		/** The request identifier. */
		private String identifierBuilder;
		/** The request date. */
		private Date requestDateBuilder;
		/** The user name. */
		private String usernameBuilder;

		/**
		 * Default constructor.
		 */
		protected Builder() {
			// Default constructor.
		}

		/**
		 * Build the entity.
		 * 
		 * @return The built entity.
		 */
		public PresentationRequestRecovery build() {
			final PresentationRequestRecovery result = new PresentationRequestRecovery();
			result.setIdentifier(this.identifierBuilder);
			result.setRequestDate(this.requestDateBuilder);
			result.setUsername(this.usernameBuilder);
			return result;
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
		 * Sets a new value for the requestDate field.
		 * 
		 * @param requestDate
		 *            The new value for the requestDate field.
		 * @return The builder.
		 */
		public Builder requestDate(final Date requestDate) {
			this.requestDateBuilder = requestDate;
			return this;
		}

		/**
		 * Sets a new value for the username field.
		 * 
		 * @param username
		 *            The new value for the username field.
		 * @return The builder.
		 */
		public Builder username(final String username) {
			this.usernameBuilder = username;
			return this;
		}
	}
}
