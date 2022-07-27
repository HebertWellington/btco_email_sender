package org.bitcoinnano.btcomailsender.entity;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.bitcoinnano.btcomailsender.type.SendStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class MailLog {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String customerEmail;

	private String sessionId;

	@Enumerated(EnumType.STRING)
	private SendStatus status;

	private LocalDateTime createdAt;

	private LocalDateTime updatedAt;

}
