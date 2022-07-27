package org.bitcoinnano.btcomailsender.exception;

public class SendEmailException extends Exception  {
		private static final long serialVersionUID = 8613762968771346678L;

		public SendEmailException(String message, Throwable t) {
			super(message, t);
		}
}
