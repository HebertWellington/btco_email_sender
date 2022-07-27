package org.bitcoinnano.btcomailsender.repository;

import org.bitcoinnano.btcomailsender.entity.MailLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MailLogRepository extends JpaRepository<MailLog, Long> {

}
