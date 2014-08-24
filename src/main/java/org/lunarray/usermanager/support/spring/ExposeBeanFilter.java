package org.lunarray.usermanager.support.spring;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.lunarray.usermanager.support.ConfigurationValue;

/**
 * A filter that sets a value to the request as an attribute.
 * 
 * @author Pal Hargitai (pal@lunarray.org)
 */
public final class ExposeBeanFilter
		implements Filter {

	/** The tenant key. */
	private transient String key;
	/** The configuration value. */
	private transient ConfigurationValue value;

	/**
	 * Default constructor.
	 */
	public ExposeBeanFilter() {
		// Default constructor.
	}

	/** {@inheritDoc} */
	@Override
	public void destroy() {
	}

	/** {@inheritDoc} */
	@Override
	public void doFilter(final ServletRequest request, final ServletResponse response, final FilterChain chain) throws IOException,
			ServletException {
		request.setAttribute(this.key, this.value.get());
		chain.doFilter(request, response);
	}

	/**
	 * Gets the value for the key field.
	 * 
	 * @return The value for the key field.
	 */
	public String getKey() {
		return this.key;
	}

	/**
	 * Gets the value for the value field.
	 * 
	 * @return The value for the value field.
	 */
	public ConfigurationValue getValue() {
		return this.value;
	}

	/** {@inheritDoc} */
	@Override
	public void init(final FilterConfig filterConfig) throws ServletException {
	}

	/**
	 * Sets a new value for the key field.
	 * 
	 * @param key
	 *            The new value for the key field.
	 */
	public void setKey(final String key) {
		this.key = key;
	}

	/**
	 * Sets a new value for the value field.
	 * 
	 * @param value
	 *            The new value for the value field.
	 */
	public void setValue(final ConfigurationValue value) {
		this.value = value;
	}
}
