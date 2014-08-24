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

import org.lunarray.model.descriptor.model.Model;
import org.lunarray.model.descriptor.model.entity.EntityDescriptor;
import org.lunarray.usermanager.presentation.domain.PresentationUser;
import org.lunarray.usermanager.presentation.domain.qualifiers.Create;
import org.lunarray.usermanager.presentation.parameter.UserParameter;
import org.lunarray.usermanager.presentation.session.UsersSession;
import org.lunarray.usermanager.service.exceptions.ServiceException;
import org.lunarray.usermanager.support.jsf.MessageUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The user creation page.
 * 
 * @author Pal Hargitai (pal@lunarray.org)
 */
public final class CreateUserPage
		implements Serializable {

	/** The logger. */
	private static final Logger LOGGER = LoggerFactory.getLogger(CreateUserPage.class);
	/** Serial id. */
	private static final long serialVersionUID = 1062340121374910207L;
	/** The model. */
	private transient Model<Object> presentationModel;
	/** The selected user. */
	private PresentationUser selectedUser;
	/** The user parameter. */
	private transient UserParameter userParameter;
	/** The user session. */
	private transient UsersSession usersSession;

	/**
	 * Default constructor.
	 */
	public CreateUserPage() {
		this.selectedUser = new PresentationUser();
	}

	/**
	 * Creates a user.
	 * 
	 * @return The navigation rule.
	 */
	public String create() {
		String result = null;
		try {
			this.usersSession.createUser(this.selectedUser);
			this.userParameter.setUserId(this.selectedUser.getIdentifier());
			result = "pretty:updateUser";
			MessageUtils.addInfoMessage("Created role", String.format("Created user '%s'.", this.selectedUser.getDisplayName()));
		} catch (final ServiceException e) {
			MessageUtils.addExceptionMessage(e);
			CreateUserPage.LOGGER.warn("Could not create user.", e);
		}
		return result;
	}

	/**
	 * The creational view qualifier.
	 * 
	 * @return The qualifier.
	 */
	public Class<?> getQualifier() {
		return Create.class;
	}

	/**
	 * Gets the value for the selectedUser field.
	 * 
	 * @return The value for the selectedUser field.
	 */
	public PresentationUser getSelectedUser() {
		return this.selectedUser;
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
	 * Sets a new value for the selectedUser field.
	 * 
	 * @param selectedUser
	 *            The new value for the selectedUser field.
	 */
	public void setSelectedUser(final PresentationUser selectedUser) {
		this.selectedUser = selectedUser;
	}

	/**
	 * Sets a new value for the userParameter field.
	 * 
	 * @param userParameter
	 *            The new value for the userParameter field.
	 */
	public void setUserParameter(final UserParameter userParameter) {
		this.userParameter = userParameter;
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
