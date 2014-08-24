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

import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.Size;

import org.apache.commons.lang.StringUtils;
import org.lunarray.model.descriptor.model.annotations.Alias;
import org.lunarray.model.descriptor.presentation.RenderType;
import org.lunarray.model.descriptor.presentation.annotations.EntityPresentationHint;
import org.lunarray.model.descriptor.presentation.annotations.PresentationHint;
import org.lunarray.model.descriptor.util.BooleanInherit;

/**
 * The update password presentation value.
 * 
 * @author Pal Hargitai (pal@lunarray.org)
 */
@EntityPresentationHint(resourceBundle = "org.lunarray.usermanager.presentation.domain.Labels")
public final class UpdatePassword
		implements Serializable {

	/** Minimal password length. */
	private static final int PASSWORD_MIN_LENGTH = 8;
	/** The serial id. */
	private static final long serialVersionUID = -6619903542989434488L;
	/** The password. */
	@PresentationHint(order = 10, render = RenderType.SECRET)
	@Size(min = UpdatePassword.PASSWORD_MIN_LENGTH)
	private String password;
	/** The password again. */
	@PresentationHint(order = 20, render = RenderType.SECRET)
	@Size(min = UpdatePassword.PASSWORD_MIN_LENGTH)
	private String passwordAgain;
	/** A virtual property that indicates the passwords are equal. */
	@Alias("password")
	@PresentationHint(visible = BooleanInherit.FALSE)
	private transient boolean passwordEqual;

	/**
	 * Default constructor.
	 */
	public UpdatePassword() {
		// Default constructor.
	}

	/**
	 * Gets the value for the password field.
	 * 
	 * @return The value for the password field.
	 */
	public String getPassword() {
		return this.password;
	}

	/**
	 * Gets the value for the passwordAgain field.
	 * 
	 * @return The value for the passwordAgain field.
	 */
	public String getPasswordAgain() {
		return this.passwordAgain;
	}

	/**
	 * Tests if the password is equal.
	 * 
	 * @return True if and only if the password is equal.
	 */
	@PresentationHint(visible = BooleanInherit.FALSE)
	@AssertTrue(message = "{password.equals.message}")
	public boolean isPasswordEqual() {
		return StringUtils.equals(this.password, this.passwordAgain);
	}

	/**
	 * Sets a new value for the password field.
	 * 
	 * @param password
	 *            The new value for the password field.
	 */
	public void setPassword(final String password) {
		this.password = password;
	}

	/**
	 * Sets a new value for the passwordAgain field.
	 * 
	 * @param passwordAgain
	 *            The new value for the passwordAgain field.
	 */
	public void setPasswordAgain(final String passwordAgain) {
		this.passwordAgain = passwordAgain;
	}

	/**
	 * Sets a new value for the passwordEqual field.
	 * 
	 * @param passwordEqual
	 *            The new value for the passwordEqual field.
	 */
	public void setPasswordEqual(final boolean passwordEqual) {
		this.passwordEqual = passwordEqual;
	}
}
