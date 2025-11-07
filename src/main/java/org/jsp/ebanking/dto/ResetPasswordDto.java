package org.jsp.ebanking.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class ResetPasswordDto {
	@NotNull(message = "Email is Required")
	@Email(message = "Enter Proper Email Address")
	private String email;
	private int otp;
	@Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,16}$", message = "Select a Strong Password")
	private String password;
}
