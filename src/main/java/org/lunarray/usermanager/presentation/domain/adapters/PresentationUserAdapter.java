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

import org.lunarray.usermanager.domain.User;
import org.lunarray.usermanager.presentation.domain.PresentationUser;

/**
 * An adapter between a {@link User} and the {@link PresentationUser}.
 * 
 * @author Pal Hargitai (pal@lunarray.org)
 */
public final class PresentationUserAdapter
		implements Serializable {

	/** Serial id. */
	private static final long serialVersionUID = -2676184818646999266L;

	/**
	 * Default constructor.
	 */
	public PresentationUserAdapter() {
		// Default constructor.
	}

	/**
	 * Convert to a {@link PresentationUser}.
	 * 
	 * @param value
	 *            The value to convert.
	 * @return The converted user.
	 */
	public PresentationUser toPresentationUser(final User value) {
		final PresentationUser.Builder builder = PresentationUser.createBuilder();
		builder.identifier(value.getIdentifier()).displayName(value.getDisplayName()).mail(value.getMail());
		builder.firstName(value.getFirstName()).lastName(value.getLastName());
		return builder.build();
	}

	/**
	 * Convert to a {@link User}.
	 * 
	 * @param value
	 *            The value to convert.
	 * @return The converted user.
	 */
	public User toUser(final PresentationUser value) {
		final User.Builder builder = User.createBuilder();
		builder.identifier(value.getIdentifier()).displayName(value.getDisplayName()).mail(value.getMail());
		builder.firstName(value.getFirstName()).lastName(value.getLastName());
		return builder.build();
	}
}
