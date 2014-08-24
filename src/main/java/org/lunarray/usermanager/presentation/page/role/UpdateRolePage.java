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

import org.lunarray.common.check.CheckUtil;
import org.lunarray.model.descriptor.model.Model;
import org.lunarray.model.descriptor.model.entity.EntityDescriptor;
import org.lunarray.usermanager.presentation.domain.PresentationRole;
import org.lunarray.usermanager.presentation.parameter.RoleParameter;
import org.lunarray.usermanager.presentation.session.RolesSession;
import org.lunarray.usermanager.service.exceptions.ServiceException;
import org.lunarray.usermanager.support.jsf.MessageUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The update role page.
 * 
 * @author Pal Hargitai (pal@lunarray.org)
 */
public final class UpdateRolePage
		implements Serializable {

	/** The logger. */
	private static final Logger LOGGER = LoggerFactory.getLogger(UpdateRolePage.class);
	/** Serial id. */
	private static final long serialVersionUID = 1053972742143441291L;
	/** The presentation model. */
	private transient Model<Object> presentationModel;
	/** The role parameter. */
	private transient RoleParameter roleParameter;
	/** The roles session. */
	private transient RolesSession rolesSession;
	/** The currently selected role. */
	private PresentationRole selectedRole;

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
	 * Gets the entity descriptor.
	 * 
	 * @return The entity descriptor.
	 */
	public EntityDescriptor<PresentationRole> getRoleType() {
		return this.presentationModel.getEntity(PresentationRole.class);
	}

	/**
	 * Gets the value for the selectedRole field.
	 * 
	 * @return The value for the selectedRole field.
	 */
	public PresentationRole getSelectedRole() {
		if (CheckUtil.isNull(this.selectedRole) && !CheckUtil.isNull(this.roleParameter)
				&& !CheckUtil.isNull(this.roleParameter.getRoleId())) {
			try {
				this.selectedRole = this.rolesSession.getRole(this.roleParameter.getRoleId());
			} catch (final ServiceException e) {
				MessageUtils.addExceptionMessage(e);
				UpdateRolePage.LOGGER.warn("Could not get role.", e);
			}
		}
		if (!CheckUtil.isNull(this.selectedRole) && !CheckUtil.isNull(this.roleParameter)
				&& !CheckUtil.isNull(this.roleParameter.getRoleId())) {
			if (!this.roleParameter.getRoleId().equals(this.selectedRole.getIdentifier())) {
				try {
					this.selectedRole = this.rolesSession.getRole(this.roleParameter.getRoleId());
				} catch (final ServiceException e) {
					MessageUtils.addExceptionMessage(e);
					UpdateRolePage.LOGGER.warn("Could not get role.", e);
				}
			}
		} else {
			this.selectedRole = new PresentationRole();
		}
		return this.selectedRole;
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
	 * Sets a new value for the selectedRole field.
	 * 
	 * @param selectedRole
	 *            The new value for the selectedRole field.
	 */
	public void setSelectedRole(final PresentationRole selectedRole) {
		this.selectedRole = selectedRole;
	}

	/**
	 * Updates a role.
	 * 
	 * @return The navigation role.
	 */
	public String update() {
		String result = null;
		try {
			if (CheckUtil.isNull(this.selectedRole.getIdentifier())) {
				this.selectedRole.setIdentifier(this.roleParameter.getRoleId());
			}
			this.rolesSession.updateRole(this.selectedRole);
			MessageUtils.addInfoMessage("Updated role", String.format("Updated role '%s'.", this.selectedRole.getDisplayName()));
			result = "pretty:roles";
		} catch (final ServiceException e) {
			MessageUtils.addExceptionMessage(e);
			UpdateRolePage.LOGGER.warn("Could not update role.", e);
		}
		return result;
	}
}
