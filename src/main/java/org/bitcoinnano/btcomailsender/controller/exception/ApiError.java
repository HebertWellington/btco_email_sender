package org.bitcoinnano.btcomailsender.controller.exception;

import java.io.Serializable;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApiError implements Serializable {

	private static final long serialVersionUID = -2941472724338616283L;

	private int code;
	private String msg;
	private Date date;

}
