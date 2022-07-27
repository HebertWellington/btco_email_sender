package org.bitcoinnano.btcomailsender.service;

import org.bitcoinnano.btcomailsender.exception.SendEmailException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.amazonaws.ResponseMetadata;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceClientBuilder;
import com.amazonaws.services.simpleemail.model.CreateTemplateRequest;
import com.amazonaws.services.simpleemail.model.CreateTemplateResult;
import com.amazonaws.services.simpleemail.model.DeleteTemplateRequest;
import com.amazonaws.services.simpleemail.model.Destination;
import com.amazonaws.services.simpleemail.model.SendTemplatedEmailRequest;
import com.amazonaws.services.simpleemail.model.Template;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class MailSenderService {

	static final String FROM = "infosolutions@gmail.com";
	
	private AmazonSimpleEmailService client;

	@Value("${frontend.url}")
	private String frontendUrl;

	public MailSenderService() throws SendEmailException {
		ProfileCredentialsProvider credentialsProvider = new ProfileCredentialsProvider();
		try {
			credentialsProvider.getCredentials();
		} catch (Exception e) {
			throw new SendEmailException("Erro ao recuperar credenciais AWS: " + e.getMessage(), e);
		}

		this.client = AmazonSimpleEmailServiceClientBuilder.standard()
				.withCredentials(credentialsProvider)
				.withRegion("us-east-1").build();
	}

	public void sendEmail(String toName, String toEmail, String sessionId) throws SendEmailException {
		// Construct an object to contain the recipient address.
		Destination destination = new Destination().withToAddresses(new String[] { toEmail });

		String link = frontendUrl + "/instructions?sessionId=" + sessionId;
		// Assemble the email.
		SendTemplatedEmailRequest request = new SendTemplatedEmailRequest()
				.withSource(FROM)
				.withDestination(destination)
				.withTemplate("Instructions")
				.withTemplateData("{ \"name\": \"%s\", \"link\": \"%s\" }".formatted(toName, link));

		try {
			log.info("Attempting to send an email through Amazon SES by using the AWS SDK for Java...");

			// Send the email.
			this.client.sendTemplatedEmail(request);
			log.info("Email sent!");

		} catch (Exception ex) {
			throw new SendEmailException("Erro ao enviar email: " + ex.getMessage(), ex);
		}
	}

	public ResponseMetadata createTemplate() {
		try {
			DeleteTemplateRequest deleteTemplateRequest = new DeleteTemplateRequest();
			deleteTemplateRequest.setTemplateName("Instructions");
			this.client.deleteTemplate(deleteTemplateRequest);
		} catch (Exception e) {
			log.info("Template not exists.");
		}
        Template template = new Template();
        template.setTemplateName("Instructions");
        template.setSubjectPart("Thank you! Get your Bitcoin Nanos now!");
        template.setTextPart("Follow the instructions bellow to receive your coins.");
        template.setHtmlPart("Dear {{name}},<br /><br />Click on link to open the instructions:<br /><br />{{link}}<br /><br />Thank you!");
        CreateTemplateRequest createTemplateRequest = new CreateTemplateRequest();
        createTemplateRequest.setTemplate(template);
        CreateTemplateResult result = this.client.createTemplate(createTemplateRequest);
        return result.getSdkResponseMetadata();
    }

}
