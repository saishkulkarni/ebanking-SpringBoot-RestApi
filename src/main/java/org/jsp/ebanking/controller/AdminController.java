package org.jsp.ebanking.controller;

import org.jsp.ebanking.dto.AccountNumberDto;
import org.jsp.ebanking.dto.ResponseDto;
import org.jsp.ebanking.service.AdminService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/admin")
public class AdminController {

	private final AdminService adminService;

	@GetMapping("/banks/pending")
	public ResponseEntity<ResponseDto> getPendingAccounts() {
		return adminService.getPendingAccounts();
	}

	@GetMapping("/user")
	public ResponseEntity<ResponseDto> getUser(@RequestBody AccountNumberDto accountNumberDto) {
		return adminService.getUser(accountNumberDto.getAccountNumber());
	}

	@PatchMapping("/approve/saving")
	public ResponseEntity<ResponseDto> approveAccount(@RequestBody AccountNumberDto accountNumberDto) {
		return adminService.approveBankAccount(accountNumberDto.getAccountNumber());
	}
}
