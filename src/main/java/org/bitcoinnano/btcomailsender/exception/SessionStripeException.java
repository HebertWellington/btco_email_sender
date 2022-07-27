package org.bitcoinnano.btcomailsender.exception;

public class SessionStripeException extends RuntimeException {
	private static final long serialVersionUID = -2957192643054599346L;

	public SessionStripeException(String msg) {
		super(msg);
	}
}
