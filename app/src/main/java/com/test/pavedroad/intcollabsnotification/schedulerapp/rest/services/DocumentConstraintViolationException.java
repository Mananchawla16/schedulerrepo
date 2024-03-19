package com.test.pavedroad.intcollabsnotification.schedulerapp.rest.services;

/**
 * Models a {@code Document} constraint violation.
 * 
 *
 */
public class DocumentConstraintViolationException extends
		DocumentServiceException {

	private static final long serialVersionUID = 1L;

	/**
	 * Constructs exception without message or cause.
	 */
	public DocumentConstraintViolationException() {
		super();
	}

	/**
	 * Construct with a message {@code String} that is returned by the inherited
	 * {@code Throwable.getMessage}.
	 * 
	 * @param message
	 *            the message that is returned by the inherited
	 *            {@code Throwable.getMessage}
	 */
	public DocumentConstraintViolationException(String message) {
		super(message);
	}

	/**
	 * Construct with a {@code Throwable} cause that is returned by the
	 * inherited {@code Throwable.getCause}. The {@code Throwable.getMessage}
	 * will display the output from {@code toString} called on the {@code cause}
	 * .
	 * @param cause
	 *            the cause that is returned by the inherited
	 *            {@code Throwable.getCause}
	 */
	public DocumentConstraintViolationException(Throwable cause) {
		super(cause);
	}

	/**
	 * Construct with both a {@code String} message and a {@code Throwable}
	 * cause. The {@code message} is returned by the inherited
	 * {@code Throwable.getMessage}. The cause that is returned by the inherited
	 * {@code Throwable.getCause}.
	 * 
	 * @param message
	 *            the message that is returned by the inherited
	 *            {@code Throwable.getMessage}
	 * @param cause
	 *            the cause that is returned by the inherited
	 *            {@code Throwable.getCause}
	 */
	public DocumentConstraintViolationException(String message, Throwable cause) {
		super(message, cause);
	}
}
