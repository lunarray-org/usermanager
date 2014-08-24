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
package org.lunarray.usermanager.support.jsf;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

/**
 * The message utilities.
 * 
 * @author Pal Hargitai (pal@lunarray.org)
 */
public enum MessageUtils {
	/** The util instance. */
	INSTANCE;

	/**
	 * Add an exception message.
	 * 
	 * @param exception
	 *            The exception massge.
	 */
	public static void addExceptionMessage(final Exception exception) {
		final FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, exception.getMessage(), exception.getMessage());
		FacesContext.getCurrentInstance().addMessage(null, msg);
	}

	/**
	 * Add an info message.
	 * 
	 * @param summary
	 *            The message summary.
	 * @param detail
	 *            The message detail.
	 */
	public static void addInfoMessage(final String summary, final String detail) {
		final FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, summary, detail);
		FacesContext.getCurrentInstance().addMessage(null, msg);
	}

	/**
	 * Add a warning message.
	 * 
	 * @param summary
	 *            The message summary.
	 * @param detail
	 *            The message detail.
	 */
	public static void addWarnMessage(final String summary, final String detail) {
		final FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_WARN, summary, detail);
		FacesContext.getCurrentInstance().addMessage(null, msg);
	}
}
