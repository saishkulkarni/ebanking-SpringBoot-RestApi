package org.jsp.ebanking.service;

import org.jsp.ebanking.dto.ResponseDto;
import org.springframework.http.ResponseEntity;

public interface AdminService {

	ResponseEntity<ResponseDto> getPendingAccounts();

	ResponseEntity<ResponseDto> getUser(Long accountNumber);

	ResponseEntity<ResponseDto> approveBankAccount(Long accountNumber);

}
