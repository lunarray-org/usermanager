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
package org.lunarray.usermanager.presentation.page.role;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

import org.lunarray.common.check.CheckUtil;
import org.lunarray.model.descriptor.model.Model;
import org.lunarray.model.descriptor.model.entity.EntityDescriptor;
import org.lunarray.usermanager.presentation.domain.PresentationRole;
import org.lunarray.usermanager.presentation.domain.PresentationUser;
import org.lunarray.usermanager.presentation.parameter.RoleParameter;
import org.lunarray.usermanager.presentation.session.RolesSession;
import org.lunarray.usermanager.service.exceptions.ServiceException;
import org.lunarray.usermanager.support.jsf.MessageUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The page to edit the users in a role.
 * 
 * @author Pal Hargitai (pal@lunarray.org)
 */
public final class UsersRolePage
		implements Serializable {

	/** The logger. */
	private static final Logger LOGGER = LoggerFactory.getLogger(UsersRolePage.class);
	/** Serial id. */
	private static final long serialVersionUID = 1062340121374910207L;
	/** The model. */
	private transient Model<Object> presentationModel;
	/** The parameter. */
	private transient RoleParameter roleParameter;
	/** The session bean. */
	private transient RolesSession rolesSession;
	/** Role users. */
	private transient List<PresentationUser> roleUsers;
	/** The selected role. */
	private PresentationRole selectedRole;
	/** The users list. */
	private transient List<PresentationUser> users;

	/**
	 * Default constructor.
	 */
	public UsersRolePage() {
		this.selectedRole = new PresentationRole();
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
	 * Gets the value for the roleParameter field.
	 * 
	 * @return The value for the roleParameter field.
	 */
	public RoleParameter getRoleParameter() {
		return this.roleParameter;
	}

	/**
	 * Gets the value for the rolesSession field.
	 * 
	 * @return The value for the rolesSession field.
	 */
	public RolesSession getRolesSession() {
		return this.rolesSession;
	}

	/**
	 * Gets the value for the roleUsers field.
	 * 
	 * @return The value for the roleUsers field.
	 */
	public List<PresentationUser> getRoleUsers() {
		if (CheckUtil.isNull(this.roleUsers)) {
			try {
				this.roleUsers = this.rolesSession.getUsers(this.roleParameter.getRoleId());
			} catch (final ServiceException e) {
				this.roleUsers = Collections.emptyList();
				MessageUtils.addExceptionMessage(e);
				UsersRolePage.LOGGER.warn("Could not get users.", e);
			}
		}
		return this.roleUsers;
	}

	/**
	 * Gets the value for the selectedRole field.
	 * 
	 * @return The value for the selectedRole field.
	 */
	public PresentationRole getSelectedRole() {
		return this.selectedRole;
	}

	/**
	 * Gets the value for the users field.
	 * 
	 * @return The value for the users field.
	 */
	public List<PresentationUser> getUsers() {
		if (CheckUtil.isNull(this.users)) {
			try {
				this.users = this.rolesSession.getUsers();
			} catch (final ServiceException e) {
				this.users = Collections.emptyList();
				MessageUtils.addExceptionMessage(e);
				UsersRolePage.LOGGER.warn("Could not get users.", e);
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
	 * Sets a new value for the roleParameter field.
	 * 
	 * @param roleParameter
	 *            The new value for the roleParameter field.
	 */
	public void setRoleParameter(final RoleParameter roleParameter) {
		this.roleParameter = roleParameter;
	}

	/**
	 * Sets a new value for the rolesSession field.
	 * 
	 * @param rolesSession
	 *            The new value for the rolesSession field.
	 */
	public void setRolesSession(final RolesSession rolesSession) {
		this.rolesSession = rolesSession;
	}

	/**
	 * Sets a new value for the roleUsers field.
	 * 
	 * @param roleUsers
	 *            The new value for the roleUsers field.
	 */
	public void setRoleUsers(final List<PresentationUser> roleUsers) {
		this.roleUsers = roleUsers;
	}

	/**
	 * Sets a new value for the selectedRole field.
	 * 
	 * @param selectedRole
	 *            The new value for the selectedRole field.
	 */
	public void setSelectedRole(final PresentationRole selectedRole) {
		this.selectedRole = selectedRole;
	}

	/**
	 * Sets a new value for the users field.
	 * 
	 * @param users
	 *            The new value for the users field.
	 */
	public void setUsers(final List<PresentationUser> users) {
		this.users = users;
	}

	/**
	 * Updates the role.
	 * 
	 * @return The navigation rule.
	 */
	public String update() {
		String result = null;
		try {
			this.rolesSession.setUsers(this.roleParameter.getRoleId(), this.roleUsers);
			MessageUtils.addInfoMessage("Updated roles users",
					String.format("Updated roles '%s' users.", this.selectedRole.getDisplayName()));
			result = "pretty:roles";
		} catch (final ServiceException e) {
			MessageUtils.addExceptionMessage(e);
			UsersRolePage.LOGGER.warn("Could not set users.", e);
		}
		return result;
	}
}
