package org.bitcoinnano.btcomailsender.controller;

import java.time.LocalDateTime;

import org.bitcoinnano.btcomailsender.entity.MailLog;
import org.bitcoinnano.btcomailsender.exception.PaymentException;
import org.bitcoinnano.btcomailsender.exception.SendEmailException;
import org.bitcoinnano.btcomailsender.service.MailLogService;
import org.bitcoinnano.btcomailsender.service.MailSenderService;
import org.bitcoinnano.btcomailsender.service.StripeService;
import org.bitcoinnano.btcomailsender.type.SendStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.JsonObject;
import com.stripe.model.checkout.Session;
import com.stripe.net.ApiResource;

@RestController
@RequestMapping(value = "sendEmail")
public class MailController {

	@Autowired
	MailSenderService mailSenderService;

	@Autowired
	StripeService stripeService;

	@Autowired
	MailLogService mailLogService;

	/**
	 * Valida sessionId no Stripe e envia email com instrucoes
	 * 
	 * @param sessionId
	 * 
	 * @return status do processo
	 * @throws PaymentException
	 * @throws SendEmailException
	 */
	@PostMapping
	public ResponseEntity<?> sendEmail(@RequestParam(value = "sessionId") String sessionId)
			throws PaymentException, SendEmailException {

		// adiciona log no banco
		MailLog mailLog = mailLogService.addMailLog("", sessionId, SendStatus.NOT_SENT);

		// validar sessionId na API do Stripe
		Session session = stripeService.validateStripeSession(sessionId);

		// enviar email com link de instrucoes para receber coins
		JsonObject jsonObject = ApiResource.GSON.fromJson(session.getLastResponse().body(), JsonObject.class);
		String customerName = jsonObject.get("customer_details").getAsJsonObject().get("name").getAsString();
		mailSenderService.sendEmail(customerName, session.getCustomerDetails().getEmail(), sessionId);

		// atualiza log no banco
		mailLog.setUpdatedAt(LocalDateTime.now());
		mailLog.setCustomerEmail(session.getCustomerDetails().getEmail());
		mailLog.setStatus(SendStatus.SENT);
		mailLogService.update(mailLog);

		// retornar status com advice conforme status da validacao do sessionId e do
		// envio do email
		return ResponseEntity.ok().build();
	}

}
