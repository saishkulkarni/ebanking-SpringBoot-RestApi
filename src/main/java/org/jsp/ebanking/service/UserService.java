package org.jsp.ebanking.service;

import org.jsp.ebanking.dto.LoginDto;
import org.jsp.ebanking.dto.OtpDto;
import org.jsp.ebanking.dto.ResetPasswordDto;
import org.jsp.ebanking.dto.ResponseDto;
import org.jsp.ebanking.dto.UserDto;
import org.springframework.http.ResponseEntity;

public interface UserService {

	ResponseEntity<ResponseDto> register(UserDto dto);

	ResponseEntity<ResponseDto> verifyOtp(OtpDto dto);

	ResponseEntity<ResponseDto> resendOtp(String email);

	ResponseEntity<ResponseDto> forgotPassword(String email);

	ResponseEntity<ResponseDto> resetPassword(ResetPasswordDto dto);

	ResponseEntity<ResponseDto> login(LoginDto dto);

}
