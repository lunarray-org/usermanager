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
import org.lunarray.usermanager.presentation.domain.UpdatePassword;
import org.lunarray.usermanager.presentation.parameter.UserParameter;
import org.lunarray.usermanager.presentation.session.UsersSession;
import org.lunarray.usermanager.service.exceptions.ServiceException;
import org.lunarray.usermanager.support.jsf.MessageUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The password update page.
 * 
 * @author Pal Hargitai (pal@lunarray.org)
 */
public final class PasswordUserPage
		implements Serializable {

	/** The logger. */
	private static final Logger LOGGER = LoggerFactory.getLogger(PasswordUserPage.class);
	/** Serial id. */
	private static final long serialVersionUID = 1053972742143441291L;
	/** The model. */
	private transient Model<Object> presentationModel;
	/** The password. */
	private UpdatePassword updatePassword;
	/** The parameter. */
	private transient UserParameter userParameter;
	/** The session. */
	private transient UsersSession usersSession;

	/**
	 * Default constructor.
	 */
	public PasswordUserPage() {
		this.updatePassword = new UpdatePassword();
	}

	/**
	 * Gets the entity descriptor.
	 * 
	 * @return The entity descriptor.
	 */
	public EntityDescriptor<UpdatePassword> getPasswordType() {
		return this.presentationModel.getEntity(UpdatePassword.class);
	}

	/**
	 * Gets the value for the updatePassword field.
	 * 
	 * @return The value for the updatePassword field.
	 */
	public UpdatePassword getUpdatePassword() {
		return this.updatePassword;
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
	 * Sets a new value for the updatePassword field.
	 * 
	 * @param updatePassword
	 *            The new value for the updatePassword field.
	 */
	public void setUpdatePassword(final UpdatePassword updatePassword) {
		this.updatePassword = updatePassword;
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

	/**
	 * Update the user password.
	 * 
	 * @return The navigation rule.
	 */
	public String update() {
		String result = null;
		try {
			this.usersSession.updatePassword(this.userParameter.getUserId(), this.updatePassword.getPassword());
			MessageUtils.addInfoMessage("Updated user password", String.format("Updated user '%s' password.",
					this.usersSession.getUser(this.userParameter.getUserId()).getDisplayName()));
			result = "pretty:users";
		} catch (final ServiceException e) {
			MessageUtils.addExceptionMessage(e);
			PasswordUserPage.LOGGER.warn("Could not update user.", e);
		}
		return result;
	}
}
