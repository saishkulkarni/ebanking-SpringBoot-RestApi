package org.jsp.ebanking.controller;

import org.jsp.ebanking.dto.ResponseDto;
import org.jsp.ebanking.service.AdminService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
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

	@GetMapping("/user/{accountNumber}")
	public ResponseEntity<ResponseDto> getUser(@PathVariable Long accountNumber) {
		return adminService.getUser(accountNumber);
	}

	@PatchMapping("/approve/saving/{accountNumber}")
	public ResponseEntity<ResponseDto> approveAccount(@PathVariable Long accountNumber) {
		return adminService.approveBankAccount(accountNumber);
	}
}
