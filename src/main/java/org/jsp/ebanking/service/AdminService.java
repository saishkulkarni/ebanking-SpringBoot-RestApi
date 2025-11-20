package org.jsp.ebanking.service;

import org.jsp.ebanking.dto.ResponseDto;
import org.springframework.http.ResponseEntity;

public interface AdminService {

	ResponseEntity<ResponseDto> getPendingAccounts();

	ResponseEntity<ResponseDto> getUser(Long accountNumber);

	ResponseEntity<ResponseDto> approveBankAccount(Long accountNumber);

	ResponseEntity<ResponseDto> getAlluser();

	ResponseEntity<ResponseDto> getBankAccount(String email);

	ResponseEntity<ResponseDto> getBankTransactions(Long accountNumber);

	ResponseEntity<ResponseDto> blockAccount(Long accountNumber);

	ResponseEntity<ResponseDto> unblockAccount(Long accountNumber);

}
