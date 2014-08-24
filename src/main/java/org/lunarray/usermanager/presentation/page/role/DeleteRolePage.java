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
import org.lunarray.usermanager.presentation.parameter.RoleParameter;
import org.lunarray.usermanager.presentation.session.RolesSession;
import org.lunarray.usermanager.service.exceptions.ServiceException;
import org.lunarray.usermanager.support.jsf.MessageUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The delete role page.
 * 
 * @author Pal Hargitai (pal@lunarray.org)
 */
public final class DeleteRolePage
		implements Serializable {

	/** The logger. */
	private static final Logger LOGGER = LoggerFactory.getLogger(DeleteRolePage.class);
	/** Serial id. */
	private static final long serialVersionUID = 6267649516309215142L;
	/** The model. */
	private transient Model<Object> presentationModel;
	/** The role parameter. */
	private transient RoleParameter roleParameter;
	/** The roles session. */
	private transient RolesSession rolesSession;
	/** The selected role. */
	private PresentationRole selectedRole;

	/**
	 * Default constructor.
	 */
	public DeleteRolePage() {
		// Default constructor.
	}

	/**
	 * Deletes the role.
	 * 
	 * @return The navigation rule.
	 */
	public String delete() {
		String result = null;
		try {
			this.rolesSession.deleteRole(this.roleParameter.getRoleId());
			MessageUtils.addWarnMessage("Deleted role", String.format("Deleted role '%s'.", this.selectedRole.getDisplayName()));
			result = "pretty:roles";
		} catch (final ServiceException e) {
			DeleteRolePage.LOGGER.warn("Could not delete role.", e);
			MessageUtils.addExceptionMessage(e);
		}
		return result;
	}

	/**
	 * Gets the selected role.
	 * 
	 * @return The selected role.
	 */
	public List<PresentationRole> getRoles() {
		return Collections.singletonList(this.getSelectedRole());
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
				DeleteRolePage.LOGGER.warn("Could not get role.", e);
			}
		}
		if (!CheckUtil.isNull(this.selectedRole) && !CheckUtil.isNull(this.roleParameter)
				&& !CheckUtil.isNull(this.roleParameter.getRoleId())) {
			if (!this.roleParameter.getRoleId().equals(this.selectedRole.getIdentifier())) {
				try {
					this.selectedRole = this.rolesSession.getRole(this.roleParameter.getRoleId());
				} catch (final ServiceException e) {
					MessageUtils.addExceptionMessage(e);
					DeleteRolePage.LOGGER.warn("Could not get role.", e);
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
}
