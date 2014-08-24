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
package org.lunarray.usermanager.presentation.domain.adapters;

import java.io.Serializable;

import org.lunarray.usermanager.domain.Role;
import org.lunarray.usermanager.presentation.domain.PresentationRole;

/**
 * An adapter between a {@link Role} and the {@link PresentationRole}.
 * 
 * @author Pal Hargitai (pal@lunarray.org)
 */
public final class PresentationRoleAdapter
		implements Serializable {

	/** Serial id. */
	private static final long serialVersionUID = -1370527496214428125L;

	/**
	 * Default constructor.
	 */
	public PresentationRoleAdapter() {
		// Default constructor.
	}

	/**
	 * Convert to a {@link PresentationRole}.
	 * 
	 * @param value
	 *            The value to convert.
	 * @return The converted role.
	 */
	public PresentationRole toPresentationRole(final Role value) {
		final PresentationRole.Builder builder = PresentationRole.createBuilder();
		builder.identifier(value.getIdentifier()).displayName(value.getDisplayName());
		return builder.build();
	}

	/**
	 * Convert to a {@link Role}.
	 * 
	 * @param value
	 *            The value to convert.
	 * @return The converted role.
	 */
	public Role toRole(final PresentationRole value) {
		final Role.Builder builder = Role.createBuilder();
		builder.identifier(value.getIdentifier()).displayName(value.getDisplayName());
		return builder.build();
	}
}
