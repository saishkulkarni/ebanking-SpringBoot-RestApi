package org.jsp.ebanking.service;

import java.util.List;

import org.jsp.ebanking.dto.ResponseDto;
import org.jsp.ebanking.entity.SavingBankAccount;
import org.jsp.ebanking.entity.User;
import org.jsp.ebanking.exception.DataNotFoundException;
import org.jsp.ebanking.repository.SavingAccountRepository;
import org.jsp.ebanking.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

	private final SavingAccountRepository savingAccountRepository;
	private final UserRepository userRepository;

	@Override
	public ResponseEntity<ResponseDto> getPendingAccounts() {
		List<SavingBankAccount> list = savingAccountRepository.findByActiveFalse();
		if (list.isEmpty())
			throw new DataNotFoundException("No Accounts Pending for Verfication");
		else {
			return ResponseEntity.ok(new ResponseDto("Accounts Found", list));
		}
	}

	@Override
	public ResponseEntity<ResponseDto> getUser(Long accountNumber) {
		User user = userRepository.findByBankAccount_accountNumber(accountNumber)
				.orElseThrow(() -> new DataNotFoundException("No User Details Found"));
		return ResponseEntity.ok(new ResponseDto("User Details Found", user));
	}

	@Override
	public ResponseEntity<ResponseDto> approveBankAccount(Long accountNumber) {
		SavingBankAccount account = savingAccountRepository.findById(accountNumber)
				.orElseThrow(() -> new DataNotFoundException("No Account Details Found"));
		account.setActive(true);
		savingAccountRepository.save(account);
		return ResponseEntity.ok(new ResponseDto("Status Updated Success", account));
	}

}
