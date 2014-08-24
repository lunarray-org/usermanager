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
package org.lunarray.usermanager.presentation.page;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import org.apache.shiro.SecurityUtils;
import org.lunarray.usermanager.presentation.domain.PresentationUser;
import org.lunarray.usermanager.presentation.session.LayoutSession;
import org.lunarray.usermanager.service.exceptions.ServiceException;

/**
 * The layout page.
 * 
 * @author Pal Hargitai (pal@lunarray.org)
 */
public final class LayoutPage {

	/** The cached recovery request count. */
	private int count = -1;
	/** The layout session. */
	private transient LayoutSession layoutSession;

	/**
	 * Default constructor.
	 */
	public LayoutPage() {
		// Default constructor.
	}

	/**
	 * Gets the value for the count field.
	 * 
	 * @return The value for the count field.
	 */
	public int getCount() {
		return this.count;
	}

	/**
	 * The current subject identifier.
	 * 
	 * @return The subject identifier.
	 */
	public String getIdentifier() {
		return SecurityUtils.getSubject().getPrincipal().toString();
	}

	/**
	 * Gets the value for the layoutSession field.
	 * 
	 * @return The value for the layoutSession field.
	 */
	public LayoutSession getLayoutSession() {
		return this.layoutSession;
	}

	/**
	 * The current logged in user.
	 * 
	 * @return The user.
	 */
	public PresentationUser getUser() {
		PresentationUser result = null;
		try {
			result = this.layoutSession.getUser(this.getIdentifier());
		} catch (final ServiceException e) {
			final FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage(), e.getMessage());
			FacesContext.getCurrentInstance().addMessage(null, msg);
		}
		return result;
	}

	/**
	 * Log out the user.
	 * 
	 * @return The navigation rule.
	 */
	public String logout() {
		SecurityUtils.getSubject().logout();
		return "pretty:login";
	}

	/**
	 * Sets a new value for the count field.
	 * 
	 * @param count
	 *            The new value for the count field.
	 */
	public void setCount(final int count) {
		this.count = count;
	}

	/**
	 * Sets a new value for the layoutSession field.
	 * 
	 * @param layoutSession
	 *            The new value for the layoutSession field.
	 */
	public void setLayoutSession(final LayoutSession layoutSession) {
		this.layoutSession = layoutSession;
	}
}
