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
import org.lunarray.usermanager.presentation.domain.PresentationRole;
import org.lunarray.usermanager.presentation.domain.PresentationUser;
import org.lunarray.usermanager.presentation.parameter.UserParameter;
import org.lunarray.usermanager.presentation.session.UsersSession;
import org.lunarray.usermanager.service.exceptions.ServiceException;
import org.lunarray.usermanager.support.jsf.MessageUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The user roles page.
 * 
 * @author Pal Hargitai (pal@lunarray.org)
 */
public final class RolesUserPage
		implements Serializable {

	/** The logger. */
	private static final Logger LOGGER = LoggerFactory.getLogger(RolesUserPage.class);
	/** Serial id. */
	private static final long serialVersionUID = 1062340121374910207L;
	/** The model. */
	private transient Model<Object> presentationModel;
	/** The user roles. */
	private List<PresentationRole> roles;
	/** The currently selected user. */
	private PresentationUser selectedUser;
	/** The user parameter. */
	private transient UserParameter userParameter;
	/** The user roles. */
	private List<PresentationRole> userRoles;
	/** The user session. */
	private transient UsersSession usersSession;

	/**
	 * Default constructor.
	 */
	public RolesUserPage() {
		this.selectedUser = new PresentationUser();
	}

	/**
	 * Gets the value for the presentationModel field.
	 * 
	 * @return The value for the presentationModel field.
	 */
	public Model<Object> getPresentationModel() {
		return this.presentationModel;
	}

	/**
	 * Gets the value for the roles field.
	 * 
	 * @return The value for the roles field.
	 */
	public List<PresentationRole> getRoles() {
		if (this.roles == null) {
			try {
				this.roles = this.usersSession.getRoles();
			} catch (final ServiceException e) {
				this.roles = Collections.emptyList();
				MessageUtils.addExceptionMessage(e);
				RolesUserPage.LOGGER.warn("Could not get roles.", e);
			}
		}
		return this.roles;
	}

	/**
	 * Gets the entity descriptor.
	 * 
	 * @return The entity descriptor.
	 */
	public EntityDescriptor<PresentationRole> getRoleType() {
		return this.presentationModel.getEntity(PresentationRole.class);
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
	 * Gets the value for the userParameter field.
	 * 
	 * @return The value for the userParameter field.
	 */
	public UserParameter getUserParameter() {
		return this.userParameter;
	}

	/**
	 * Gets the value for the userRoles field.
	 * 
	 * @return The value for the userRoles field.
	 */
	public List<PresentationRole> getUserRoles() {
		if (this.userRoles == null) {
			try {
				this.userRoles = this.usersSession.getRoles(this.userParameter.getUserId());
			} catch (final ServiceException e) {
				this.userRoles = Collections.emptyList();
				MessageUtils.addExceptionMessage(e);
				RolesUserPage.LOGGER.warn("Could not get roles.", e);
			}
		}
		return this.userRoles;
	}

	/**
	 * Gets the value for the usersSession field.
	 * 
	 * @return The value for the usersSession field.
	 */
	public UsersSession getUsersSession() {
		return this.usersSession;
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
	 * Sets a new value for the roles field.
	 * 
	 * @param roles
	 *            The new value for the roles field.
	 */
	public void setRoles(final List<PresentationRole> roles) {
		this.roles = roles;
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
	 * Sets a new value for the userRoles field.
	 * 
	 * @param userRoles
	 *            The new value for the userRoles field.
	 */
	public void setUserRoles(final List<PresentationRole> userRoles) {
		this.userRoles = userRoles;
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
	 * Update the roles for a user.
	 * 
	 * @return The navigation rule.
	 */
	public String update() {
		String result = null;
		try {
			this.usersSession.setRoles(this.userParameter.getUserId(), this.userRoles);
			MessageUtils.addInfoMessage("Updated users roles",
					String.format("Updated user '%s' roles.", this.usersSession.getUser(this.userParameter.getUserId()).getDisplayName()));
			result = "pretty:users";
		} catch (final ServiceException e) {
			MessageUtils.addExceptionMessage(e);
			RolesUserPage.LOGGER.warn("Could not set roles.", e);
		}
		return result;
	}
}
