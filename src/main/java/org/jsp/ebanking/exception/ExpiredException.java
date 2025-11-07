package org.jsp.ebanking.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class ExpiredException extends RuntimeException {
	private String message;
}
