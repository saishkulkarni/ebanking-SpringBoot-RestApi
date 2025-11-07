package org.jsp.ebanking.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class DataNotFoundException extends RuntimeException {
	private String message = "Data Not Found";
}
