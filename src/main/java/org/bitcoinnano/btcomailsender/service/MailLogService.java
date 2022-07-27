package org.bitcoinnano.btcomailsender.service;

import java.time.LocalDateTime;

import org.bitcoinnano.btcomailsender.entity.MailLog;
import org.bitcoinnano.btcomailsender.repository.MailLogRepository;
import org.bitcoinnano.btcomailsender.type.SendStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MailLogService {

	@Autowired
	MailLogRepository mailLogRepository;

	public MailLog addMailLog(String customerEmail, String sessionId, SendStatus status) {
		return mailLogRepository.save(MailLog.builder()
				.customerEmail(customerEmail)
				.sessionId(sessionId)
				.status(status)
				.createdAt(LocalDateTime.now())
				.updatedAt(LocalDateTime.now())
				.build());
	}

	public MailLog update(MailLog mailLog) {
		return mailLogRepository.save(mailLog);
	}

}