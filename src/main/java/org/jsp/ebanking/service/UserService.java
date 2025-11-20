package org.jsp.ebanking.service;

import java.security.Principal;
import java.util.Map;

import org.jsp.ebanking.dto.LoginDto;
import org.jsp.ebanking.dto.OtpDto;
import org.jsp.ebanking.dto.ResetPasswordDto;
import org.jsp.ebanking.dto.ResponseDto;
import org.jsp.ebanking.dto.SavingAccountDto;
import org.jsp.ebanking.dto.TransferDto;
import org.jsp.ebanking.dto.UserDto;
import org.springframework.http.ResponseEntity;

public interface UserService {

	ResponseEntity<ResponseDto> register(UserDto dto);

	ResponseEntity<ResponseDto> verifyOtp(OtpDto dto);

	ResponseEntity<ResponseDto> resendOtp(String email);

	ResponseEntity<ResponseDto> forgotPassword(String email);

	ResponseEntity<ResponseDto> resetPassword(ResetPasswordDto dto);

	ResponseEntity<ResponseDto> login(LoginDto dto);

	ResponseEntity<ResponseDto> viewSavingsAccount(Principal principal);

	ResponseEntity<ResponseDto> createSavingsAccount(Principal principal, SavingAccountDto accountDto);

	ResponseEntity<ResponseDto> checkBalance(Principal prinicpal);

	ResponseEntity<ResponseDto> deposit(Principal principal, Map<String, Double> map);

	ResponseEntity<ResponseDto> confirmPayment(Double amount, String razorpay_payment_id, Principal principal);

	ResponseEntity<ResponseDto> transfer(Principal principal, TransferDto dto);

}
