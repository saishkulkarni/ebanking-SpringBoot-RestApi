package org.jsp.ebanking.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class UserDto {
	@NotNull(message = "Name is Required")
	private String name;
	@NotNull(message = "Email is Required")
	@Email(message = "Enter Proper Email Address")
	private String email;
	@Pattern(regexp = "^(\\+\\d{1,3}[ -]?)?\\d{10}$", message = "Enter Proper Mobile Number")
	private String mobile;
	@Pattern(regexp = "^\\d{2}-\\d{2}-\\d{4}$", message = "Enter Proper DOB")
	private String dob;
	@Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,16}$", message = "Select a Strong Password")
	private String password;
	@Pattern(regexp = "^(ADMIN|USER)$", message = "Enter Proper Role")
	private String role;
}