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
package org.lunarray.usermanager.presentation.parameter;

import java.io.Serializable;

/**
 * A parameter for the user section of the application.
 * 
 * @author Pal Hargitai (pal@lunarray.org)
 */
public final class UserParameter
		implements Serializable {

	/** Serial id. */
	private static final long serialVersionUID = 7442472636577265010L;
	/** The user identifier. */
	private String userId;

	/**
	 * Default constructor.
	 */
	public UserParameter() {
		// Default constructor.
	}

	/**
	 * Gets the value for the userId field.
	 * 
	 * @return The value for the userId field.
	 */
	public String getUserId() {
		return this.userId;
	}

	/**
	 * Sets a new value for the userId field.
	 * 
	 * @param userId
	 *            The new value for the userId field.
	 */
	public void setUserId(final String userId) {
		this.userId = userId;
	}
}
