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
package org.lunarray.usermanager.presentation.page.user;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

import org.lunarray.model.descriptor.model.Model;
import org.lunarray.model.descriptor.model.entity.EntityDescriptor;
import org.lunarray.usermanager.presentation.domain.PresentationUser;
import org.lunarray.usermanager.presentation.session.UsersSession;
import org.lunarray.usermanager.service.exceptions.ServiceException;
import org.lunarray.usermanager.support.jsf.MessageUtils;

/**
 * The users overview page.
 * 
 * @author Pal Hargitai (pal@lunarray.org)
 */
public final class UsersPage
		implements Serializable {

	/** Serial id. */
	private static final long serialVersionUID = -880713138029214651L;
	/** The model. */
	private Model<Object> presentationModel;
	/** The users. */
	private List<PresentationUser> users;
	/** The user session. */
	private UsersSession usersSession;

	/**
	 * Gets all users.
	 * 
	 * @return The users.
	 */
	public List<PresentationUser> getUsers() {
		if (this.users == null) {
			try {
				this.users = this.usersSession.getUsers();
			} catch (final ServiceException e) {
				this.users = Collections.emptyList();
				MessageUtils.addExceptionMessage(e);
			}
		}
		return this.users;
	}

	/**
	 * Gets the entity descriptor.
	 * 
	 * @return The entity descriptor.
	 */
	public EntityDescriptor<PresentationUser> getUserType() {
		return this.presentationModel.getEntity(PresentationUser.class);
	}

	/**
	 * Sets a new value for the presentationModel field.
	 * 
	 * @param presentationModel
	 *            The new value for the presentationModel field.
	 */
	public void setPresentationModel(final Model<Object> presentationModel) {
		this.presentationModel = presentationModel;
	}

	/**
	 * Sets a new value for the usersSession field.
	 * 
	 * @param usersSession
	 *            The new value for the usersSession field.
	 */
	public void setUsersSession(final UsersSession usersSession) {
		this.usersSession = usersSession;
	}
}
