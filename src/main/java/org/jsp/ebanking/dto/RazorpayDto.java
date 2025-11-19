package org.jsp.ebanking.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RazorpayDto {
	private String orderId;
	private Double amount;
	private String key;
	private String currency;
}
