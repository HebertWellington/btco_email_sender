package org.bitcoinnano.btcomailsender.controller.exception;

import java.util.Date;

import org.bitcoinnano.btcomailsender.exception.PaymentException;
import org.bitcoinnano.btcomailsender.exception.SendEmailException;
import org.bitcoinnano.btcomailsender.exception.SessionStripeException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@ControllerAdvice
public class ResourceHandlerException extends ResponseEntityExceptionHandler {

	@ExceptionHandler(PaymentException.class)
	public ResponseEntity<ApiError> handlerNotFoundExecption(PaymentException ex) {
		log.error("Erro ao recuperar session do Stripe: ", ex);
		HttpStatus httpStatus = HttpStatus.NOT_FOUND;

		if (ex.getStatusCode() != null) {
			try {
				httpStatus = HttpStatus.valueOf(ex.getStatusCode());
			} catch (Exception e) {
				log.warn("HTTP Status não encontrado");
			}
		}
		ApiError error = new ApiError(httpStatus.value(), ex.getMessage(), new Date());
		return ResponseEntity.status(httpStatus).body(error);
	}

	@ExceptionHandler(SessionStripeException.class)
	public ResponseEntity<ApiError> handlerNotFoundExecption(SessionStripeException ex) {
		log.error("Erro na validacao do pagamento: ", ex);
		ApiError error = new ApiError(HttpStatus.CONFLICT.value(), ex.getMessage(), new Date());
		return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
	}

	@ExceptionHandler(SendEmailException.class)
	public ResponseEntity<ApiError> handlerNotFoundExecption(SendEmailException ex) {
		log.error("Erro ao enviar email: ", ex);
		ApiError error = new ApiError(HttpStatus.INTERNAL_SERVER_ERROR.value(), ex.getMessage(), new Date());
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
	}
}
