package org.bitcoinnano.btcomailsender.exception;

import lombok.Getter;

@Getter
public class PaymentException extends Exception {
	private static final long serialVersionUID = 4452400707617549240L;

	private Integer statusCode;
	private String code;

	public PaymentException(String message, Integer statusCode, String code, Throwable t) {
		super(message, t);
		this.statusCode = statusCode;
		this.code = code;
	}
	

}
