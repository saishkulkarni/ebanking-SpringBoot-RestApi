package org.jsp.ebanking.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class DataExistsException extends RuntimeException {
	private String message = "Data Already Exists";
}
