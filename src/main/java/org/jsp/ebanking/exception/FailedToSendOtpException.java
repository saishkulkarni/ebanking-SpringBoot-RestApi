package org.jsp.ebanking.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class FailedToSendOtpException extends RuntimeException {
	private String message = "Failed to Send OTP";
}
