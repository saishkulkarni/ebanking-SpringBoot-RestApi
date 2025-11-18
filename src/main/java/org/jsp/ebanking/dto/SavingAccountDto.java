package org.jsp.ebanking.dto;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class SavingAccountDto {
	@Size(min = 5,max = 50, message = "Enter Proper Name between 5 ~ 50 Charecters")
	private String fullName;
	@Size(min = 20,max = 250, message = "Enter Proper Address between 20 ~ 250 Charecters")
	private String address;
	@Pattern(regexp = "^[A-Z]{5}[0-9]{4}[A-Z]{1}$",message = "Enter Proper PAN Number")
	private String pan;
	@Pattern(regexp = "^[2-9]{1}[0-9]{11}$",message = "Enter Proper Aadhar Number")
	private String aadhar;
}
