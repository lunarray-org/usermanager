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
import org.lunarray.usermanager.presentation.session.RolesSession;
import org.lunarray.usermanager.service.exceptions.ServiceException;
import org.lunarray.usermanager.support.jsf.MessageUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The roles overview page.
 * 
 * @author Pal Hargitai (pal@lunarray.org)
 */
public final class RolesPage
		implements Serializable {

	/** The logger. */
	private static final Logger LOGGER = LoggerFactory.getLogger(RolesPage.class);
	/** Serial id. */
	private static final long serialVersionUID = -880713138029214651L;
	/** The model. */
	private transient Model<Object> presentationModel;
	/** The roles. */
	private List<PresentationRole> roles;
	/** The roles session. */
	private transient RolesSession rolesSession;

	/**
	 * Default constructor.
	 */
	public RolesPage() {
		// Default constructor.
	}

	/**
	 * Gets the list of roles.
	 * 
	 * @return The roles.
	 */
	public List<PresentationRole> getRoles() {
		if (CheckUtil.isNull(this.roles)) {
			try {
				this.roles = this.rolesSession.getRoles();
			} catch (final ServiceException e) {
				this.roles = Collections.emptyList();
				RolesPage.LOGGER.warn("Could not get roles.", e);
				MessageUtils.addExceptionMessage(e);
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
	 * Sets a new value for the presentationModel field.
	 * 
	 * @param presentationModel
	 *            The new value for the presentationModel field.
	 */
	public void setPresentationModel(final Model<Object> presentationModel) {
		this.presentationModel = presentationModel;
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
