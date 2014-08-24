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

import org.lunarray.model.descriptor.model.Model;
import org.lunarray.model.descriptor.model.entity.EntityDescriptor;
import org.lunarray.usermanager.presentation.domain.PresentationRole;
import org.lunarray.usermanager.presentation.domain.qualifiers.Create;
import org.lunarray.usermanager.presentation.parameter.RoleParameter;
import org.lunarray.usermanager.presentation.session.RolesSession;
import org.lunarray.usermanager.service.exceptions.ServiceException;
import org.lunarray.usermanager.support.jsf.MessageUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Page for creating roles.
 * 
 * @author Pal Hargitai (pal@lunarray.org)
 */
public final class CreateRolePage
		implements Serializable {

	/** The logger. */
	private static final Logger LOGGER = LoggerFactory.getLogger(CreateRolePage.class);
	/** Serial id. */
	private static final long serialVersionUID = 1062340121374910207L;
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
	public CreateRolePage() {
		this.selectedRole = new PresentationRole();
	}

	/**
	 * Creates the role.
	 * 
	 * @return The navigation rule.
	 */
	public String create() {
		String result = null;
		try {
			this.rolesSession.createRole(this.selectedRole);
			this.roleParameter.setRoleId(this.selectedRole.getIdentifier());
			MessageUtils.addInfoMessage("Created role", String.format("Created role '%s'.", this.selectedRole.getDisplayName()));
			result = "pretty:updateRole";
		} catch (final ServiceException e) {
			CreateRolePage.LOGGER.warn("Could not create role.", e);
			MessageUtils.addExceptionMessage(e);
		}
		return result;
	}

	/**
	 * The view qualifier.
	 * 
	 * @return The qualifier.
	 */
	public Class<?> getQualifier() {
		return Create.class;
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
}
