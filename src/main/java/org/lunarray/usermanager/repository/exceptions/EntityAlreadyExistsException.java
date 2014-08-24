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
package org.lunarray.usermanager.repository.exceptions;

/**
 * Indicates an entity already exists.
 * 
 * @author Pal Hargitai (pal@lunarray.org)
 */
public class EntityAlreadyExistsException
		extends BaseRepositoryException {

	/** Serial id. */
	private static final long serialVersionUID = 4243341318417347046L;

	/**
	 * Default constructor.
	 * 
	 * @param message
	 *            The message.
	 */
	public EntityAlreadyExistsException(final String message) {
		super(message);
	}

	/**
	 * Default constructor.
	 * 
	 * @param message
	 *            The message.
	 * @param cause
	 *            The original cause.
	 */
	public EntityAlreadyExistsException(final String message, final Exception cause) {
		super(message, cause);
	}
}
