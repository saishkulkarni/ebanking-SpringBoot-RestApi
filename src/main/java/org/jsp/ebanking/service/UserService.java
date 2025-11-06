package org.jsp.ebanking.service;

import org.jsp.ebanking.dto.OtpDto;
import org.jsp.ebanking.dto.ResponseDto;
import org.jsp.ebanking.dto.UserDto;
import org.springframework.http.ResponseEntity;

public interface UserService {

	ResponseEntity<ResponseDto> register(UserDto dto);

	ResponseEntity<ResponseDto> verifyOtp(OtpDto dto);
}
