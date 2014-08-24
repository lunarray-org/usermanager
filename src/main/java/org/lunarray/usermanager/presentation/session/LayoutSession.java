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
package org.lunarray.usermanager.presentation.session;

import java.io.Serializable;

import org.lunarray.usermanager.presentation.domain.PresentationUser;
import org.lunarray.usermanager.presentation.domain.adapters.PresentationUserAdapter;
import org.lunarray.usermanager.service.UserService;
import org.lunarray.usermanager.service.exceptions.ServiceException;

/**
 * The session bean for the users part of the application.
 * 
 * @author Pal Hargitai (pal@lunarray.org)
 */
public final class LayoutSession
		implements Serializable {
	/** Serial id. */
	private static final long serialVersionUID = 3185784857276666282L;
	/** The user adapter. */
	private transient PresentationUserAdapter presentationUserAdapter;
	/** The user service. */
	private transient UserService userService;

	/**
	 * Default constructor.
	 */
	public LayoutSession() {
		// Default constructor.
	}

	/**
	 * Gets a user.
	 * 
	 * @param identifier
	 *            The user identifier.
	 * @return The user.
	 * @throws ServiceException
	 *             Thrown if the operation could not be completed.
	 */
	public PresentationUser getUser(final String identifier) throws ServiceException {
		return this.presentationUserAdapter.toPresentationUser(this.userService.getUser(identifier));
	}

	/**
	 * Sets a new value for the presentationUserAdapter field.
	 * 
	 * @param presentationUserAdapter
	 *            The new value for the presentationUserAdapter field.
	 */
	public void setPresentationUserAdapter(final PresentationUserAdapter presentationUserAdapter) {
		this.presentationUserAdapter = presentationUserAdapter;
	}

	/**
	 * Sets a new value for the userService field.
	 * 
	 * @param userService
	 *            The new value for the userService field.
	 */
	public void setUserService(final UserService userService) {
		this.userService = userService;
	}
}
