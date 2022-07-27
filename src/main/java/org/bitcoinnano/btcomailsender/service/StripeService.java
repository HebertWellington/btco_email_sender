package org.bitcoinnano.btcomailsender.service;

import org.bitcoinnano.btcomailsender.exception.PaymentException;
import org.bitcoinnano.btcomailsender.exception.SessionStripeException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;


@Service
public class StripeService {

	@Value("${stripe.api.key}")
	private String stripeApiKey;
	
	public Session validateStripeSession(String sessionId) throws PaymentException {
		Stripe.apiKey = this.stripeApiKey;
		
		Session session = null;
		String status = null;

		try {
			session = Session.retrieve(sessionId);
			status = session.getPaymentStatus();

		} catch (StripeException e) {
			throw new PaymentException("Erro ao buscar Id da Session no Stripe", e.getStatusCode(), e.getCode(), e);
		}
		if (!"paid".equals(status)) {
			throw new SessionStripeException("Payment not succeeded. Payment Status: " + session.getPaymentStatus());
		}
		String statusSession = session.getStatus();
		if (!"complete".equals(statusSession)) {
			throw new SessionStripeException("Payment not succeeded. Status: " + session.getStatus());
		}

		return session;

	}

}
