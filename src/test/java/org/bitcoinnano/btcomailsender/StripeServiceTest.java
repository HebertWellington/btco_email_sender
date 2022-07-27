package org.bitcoinnano.btcomailsender;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import org.bitcoinnano.btcomailsender.exception.PaymentException;
import org.bitcoinnano.btcomailsender.exception.SessionStripeException;
import org.bitcoinnano.btcomailsender.service.StripeService;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;

public class StripeServiceTest {

	StripeService stripeService = new StripeService();

	@Test
	public void DeveRetornarSessionComSessionIdValido() throws StripeException, PaymentException {
		// cenario
		String sessionId = "valid";
		Session ses = new Session();
		ses.setId(sessionId);
		ses.setPaymentStatus("paid");
		ses.setStatus("complete");

		try (MockedStatic<Session> session = Mockito.mockStatic(Session.class)) {
			session.when(() -> Session.retrieve(sessionId)).thenReturn(ses);

			// acao
			stripeService.validateStripeSession(sessionId);

			// verificacao
			assertThat(Session.retrieve(sessionId)).isEqualTo(ses);
		}

	}

	@Test
	public void DeveRetornarPaymentExceptionComSessionIdInvalido() {
		// cenario
		String sessionId = "invalid";

		try (MockedStatic<Session> session = Mockito.mockStatic(Session.class)) {
			session.when(() -> Session.retrieve(sessionId)).thenThrow(Mockito.mock(StripeException.class));

			// acao
			try {
				stripeService.validateStripeSession(sessionId);
				fail();

				// verificacao
			} catch (PaymentException e) {
				assertEquals(e.getMessage(), "Erro ao buscar Id da Session no Stripe");
			}
		}
	}

	@Test
	public void DeveRetornarSessionStripeExceptionComStatusExpired() throws PaymentException, StripeException {
		// cenario
		String sessionId = "valid";
		Session ses = new Session();
		ses.setId(sessionId);
		ses.setPaymentStatus("paid");
		ses.setStatus("expired");

		try (MockedStatic<Session> session = Mockito.mockStatic(Session.class)) {
			session.when(() -> Session.retrieve(sessionId)).thenReturn(ses);

		// acao
			try {
				stripeService.validateStripeSession(sessionId);
				fail();

		// verificacao
			} catch (SessionStripeException e) {
				assertEquals(e.getMessage(), "Payment not succeeded. Status: " + ses.getStatus());
			}
		}
	}
	
	@Test
	public void DeveRetornarSessionStripeExceptionComPaymentStatusUnpaid() throws PaymentException, StripeException {
		// cenario
		String sessionId = "valid";
		Session ses = new Session();
		ses.setId(sessionId);
		ses.setPaymentStatus("unpaid");
		ses.setStatus("complete");

		try (MockedStatic<Session> session = Mockito.mockStatic(Session.class)) {
			session.when(() -> Session.retrieve(sessionId)).thenReturn(ses);

		// acao
			try {
				stripeService.validateStripeSession(sessionId);
				fail();

		// verificacao
			} catch (SessionStripeException e) {
				assertEquals(e.getMessage(), "Payment not succeeded. Payment Status: " + ses.getPaymentStatus());
			}
		}
	}
}