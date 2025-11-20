package org.jsp.ebanking.dto;

import lombok.Data;

@Data
public class TransferDto {
	private Double amount;
	private Long toAccountNumber;
}
