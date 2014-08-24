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

import org.lunarray.common.check.CheckUtil;
import org.lunarray.model.descriptor.model.Model;
import org.lunarray.model.descriptor.model.entity.EntityDescriptor;
import org.lunarray.usermanager.presentation.domain.PresentationUser;
import org.lunarray.usermanager.presentation.parameter.UserParameter;
import org.lunarray.usermanager.presentation.session.UsersSession;
import org.lunarray.usermanager.service.exceptions.ServiceException;
import org.lunarray.usermanager.support.jsf.MessageUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The user deletion page.
 * 
 * @author Pal Hargitai (pal@lunarray.org)
 */
public final class DeleteUserPage
		implements Serializable {

	/** The logger. */
	private static final Logger LOGGER = LoggerFactory.getLogger(DeleteUserPage.class);
	/** Serial id. */
	private static final long serialVersionUID = 6267649516309215142L;
	/** The model. */
	private transient Model<Object> presentationModel;
	/** The selected user. */
	private PresentationUser selectedUser;
	/** The user parameter. */
	private transient UserParameter userParameter;
	/** The user session. */
	private transient UsersSession usersSession;

	/**
	 * Deletes a user.
	 * 
	 * @return The navigation rule.
	 */
	public String delete() {
		String result = null;
		try {
			this.usersSession.deleteUser(this.userParameter.getUserId());
			MessageUtils.addWarnMessage("Deleted user", String.format("Deleted user '%s'.", this.selectedUser.getDisplayName()));
			result = "pretty:users";
		} catch (final ServiceException e) {
			MessageUtils.addExceptionMessage(e);
		}
		return result;
	}

	/**
	 * Gets the value for the selectedUser field.
	 * 
	 * @return The value for the selectedUser field.
	 */
	public PresentationUser getSelectedUser() {
		if (CheckUtil.isNull(this.selectedUser) && !CheckUtil.isNull(this.userParameter)
				&& !CheckUtil.isNull(this.userParameter.getUserId())) {
			try {
				this.selectedUser = this.usersSession.getUser(this.userParameter.getUserId());
			} catch (final ServiceException e) {
				MessageUtils.addExceptionMessage(e);
				DeleteUserPage.LOGGER.warn("Could not get user.", e);
			}
		}
		if (!CheckUtil.isNull(this.selectedUser) && !CheckUtil.isNull(this.userParameter)
				&& !CheckUtil.isNull(this.userParameter.getUserId())) {
			if (!this.userParameter.getUserId().equals(this.selectedUser.getIdentifier())) {
				try {
					this.selectedUser = this.usersSession.getUser(this.userParameter.getUserId());
				} catch (final ServiceException e) {
					MessageUtils.addExceptionMessage(e);
					DeleteUserPage.LOGGER.warn("Could not get user.", e);
				}
			}
		} else {
			this.selectedUser = new PresentationUser();
		}
		return this.selectedUser;
	}

	/**
	 * The selected user.
	 * 
	 * @return The selected user.
	 */
	public List<PresentationUser> getUsers() {
		return Collections.singletonList(this.getSelectedUser());
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
