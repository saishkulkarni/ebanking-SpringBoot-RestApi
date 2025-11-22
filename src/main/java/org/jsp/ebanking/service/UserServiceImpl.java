package org.jsp.ebanking.service;

import java.security.Principal;
import java.security.SecureRandom;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.jsp.ebanking.dto.BankBalanceDto;
import org.jsp.ebanking.dto.LoginDto;
import org.jsp.ebanking.dto.OtpDto;
import org.jsp.ebanking.dto.RazorpayDto;
import org.jsp.ebanking.dto.ResetPasswordDto;
import org.jsp.ebanking.dto.ResponseDto;
import org.jsp.ebanking.dto.SavingAccountDto;
import org.jsp.ebanking.dto.TransferDto;
import org.jsp.ebanking.dto.UserDto;
import org.jsp.ebanking.entity.BankTransactions;
import org.jsp.ebanking.entity.SavingBankAccount;
import org.jsp.ebanking.entity.User;
import org.jsp.ebanking.exception.DataExistsException;
import org.jsp.ebanking.exception.DataNotFoundException;
import org.jsp.ebanking.exception.ExpiredException;
import org.jsp.ebanking.exception.MissMatchException;
import org.jsp.ebanking.exception.PaymentFailedException;
import org.jsp.ebanking.mapper.SavingsBankMapper;
import org.jsp.ebanking.mapper.UserMapper;
import org.jsp.ebanking.repository.SavingAccountRepository;
import org.jsp.ebanking.repository.UserRepository;
import org.jsp.ebanking.util.JwtUtil;
import org.jsp.ebanking.util.MessageSendingHelper;
import org.jsp.ebanking.util.PaymentUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

	private final RedisService redisService;
	private final UserRepository userRepository;
	private final MessageSendingHelper messageSendingHelper;
	private final PasswordEncoder passwordEncoder;
	private final AuthenticationManager authenticationManager;
	private final JwtUtil jwtUtil;
	private final UserDetailsService userDetailsService;
	private final SavingAccountRepository savingAccountRepository;
	private final UserMapper userMapper;
	private final SavingsBankMapper bankMapper;
	private final PaymentUtil paymentUtil;

	@Override
	public ResponseEntity<ResponseDto> register(UserDto dto) {
		if (redisService.fetchUserDto(dto.getEmail()) == null) {
			if (!userRepository.existsByEmailOrMobile(dto.getEmail(), dto.getMobile())) {
				int otp = new SecureRandom().nextInt(1000, 10000);
				messageSendingHelper.sendOtp(dto.getName(), dto.getEmail(), otp);
				redisService.saveUserDto(dto);
				redisService.saveUserOtp(dto.getEmail(), otp);
				return ResponseEntity.status(201).body(new ResponseDto("Otp Sent Success, Verify to Continue", dto));
			} else {
				throw new DataExistsException(
						"Account Already Exists with " + dto.getEmail() + " or " + dto.getMobile());
			}
		} else {
			throw new DataExistsException(dto.getEmail() + " is Already being Verified if fails try after 15 mins");
		}
	}

	@Override
	public ResponseEntity<ResponseDto> verifyOtp(OtpDto dto) {
		int otp = redisService.fetchOtp(dto.getEmail());
		if (otp == 0)
			throw new ExpiredException("Otp Expired");
		else {
			if (otp == dto.getOtp()) {
				UserDto userDto = redisService.fetchUserDto(dto.getEmail());
				User user = userMapper.toEntity(userDto);
				userRepository.save(user);
				redisService.deleteUserDto(dto.getEmail());
				redisService.deleteUserOtp(dto.getEmail());
				return ResponseEntity.status(201).body(new ResponseDto("Account Created Success", userDto));
			} else {
				throw new MissMatchException("Otp Missmatch");
			}
		}
	}

	@Override
	public ResponseEntity<ResponseDto> resendOtp(String email) {
		if (redisService.fetchUserDto(email) == null)
			throw new DataNotFoundException(email + " doesnt exist");
		else {
			int otp = new SecureRandom().nextInt(1000, 10000);
			messageSendingHelper.sendOtp(redisService.fetchUserDto(email).getName(), email, otp);
			redisService.saveUserOtp(email, otp);
			return ResponseEntity.status(200)
					.body(new ResponseDto("Otp Re-Sent Success, Verify to Continue", redisService.fetchUserDto(email)));
		}
	}

	@Override
	public ResponseEntity<ResponseDto> forgotPassword(String email) {
		if (!userRepository.existsByEmail(email))
			throw new DataNotFoundException("Invalid Email " + email);
		else {
			int otp = new SecureRandom().nextInt(1000, 10000);
			messageSendingHelper.sendForgotPasswordOtp(email, otp);
			redisService.saveUserOtp(email, otp);
			return ResponseEntity.status(200)
					.body(new ResponseDto("Otp for Reseting Password has been sent to " + email, email));
		}
	}

	@Override
	public ResponseEntity<ResponseDto> resetPassword(ResetPasswordDto dto) {
		int otp = redisService.fetchOtp(dto.getEmail());
		if (otp == 0)
			throw new ExpiredException("Otp Expired Try Again");
		else {
			if (otp != dto.getOtp())
				throw new MissMatchException("Invalid Otp , Try Again");
			else {
				if (!userRepository.existsByEmail(dto.getEmail()))
					throw new DataNotFoundException("Account with " + dto.getEmail() + " doesnt exist, Try Again");
				else {
					User user = userRepository.findByEmail(dto.getEmail());
					user.setPassword(passwordEncoder.encode(dto.getPassword()));
					userRepository.save(user);
					redisService.deleteUserOtp(dto.getEmail());
					return ResponseEntity.status(200)
							.body(new ResponseDto("Password Reset Success", userMapper.toDto(user)));
				}
			}
		}
	}

	@Override
	public ResponseEntity<ResponseDto> login(LoginDto dto) {
		authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(dto.getEmail(), dto.getPassword()));
		UserDetails userDetails = userDetailsService.loadUserByUsername(dto.getEmail());
		String token = jwtUtil.generateToken(userDetails);
		LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
		map.put("token", token);
		map.put("user", userMapper.toDto(userRepository.findByEmail(dto.getEmail())));
		return ResponseEntity.ok(new ResponseDto("Login Success", map));
	}

	@Override
	public ResponseEntity<ResponseDto> viewSavingsAccount(Principal principal) {
		User user = getLoggedInUser(principal);
		SavingBankAccount bankAccount = user.getBankAccount();
		if (bankAccount == null)
			throw new DataNotFoundException("No Bank Account Exists for " + user.getName());
		if (!bankAccount.isActive())
			throw new DataExistsException("Waiting for Admins Approval");
		else {
			return ResponseEntity.ok(new ResponseDto("Account Found", bankAccount));
		}
	}

	@Override
	public ResponseEntity<ResponseDto> createSavingsAccount(Principal principal, SavingAccountDto accountDto) {
		User user = getLoggedInUser(principal);
		if (user.getBankAccount() != null) {
			if (user.getBankAccount().isActive())
				throw new DataExistsException("Account Already Exists and You can not new Create One");
			else
				throw new DataExistsException("Account Still Pending for Verification Wait for some time");

		} else {
			SavingBankAccount bankAccount = bankMapper.toEntity(accountDto);

			savingAccountRepository.save(bankAccount);
			user.setBankAccount(bankAccount);
			userRepository.save(user);

			return ResponseEntity.status(201).body(new ResponseDto("Account Created Success", bankAccount));
		}
	}

	@Override
	public ResponseEntity<ResponseDto> checkBalance(Principal principal) {
		User user = getLoggedInUser(principal);
		SavingBankAccount account = user.getBankAccount();
		if (account == null || !account.isActive())
			throw new DataNotFoundException("No Bank Accounts Found Linked with This User account");
		else {
			return ResponseEntity.ok(new ResponseDto("Account Found",
					new BankBalanceDto(account.getAccountNumber(), account.getBalance())));
		}
	}

	@Override
	public ResponseEntity<ResponseDto> deposit(Principal principal, Map<String, Double> map) {
		User user = getLoggedInUser(principal);
		SavingBankAccount account = user.getBankAccount();
		if (account == null)
			throw new DataNotFoundException("No Bank Accounts FOund Linked with This User account");
		else {
			Double amount = map.get("amount");
			RazorpayDto razorpayDto = paymentUtil.createOrder(amount);
			return ResponseEntity.ok(new ResponseDto("Payment Initialized Complete Payment to Proceed", razorpayDto));
		}
	}

	@Override
	public ResponseEntity<ResponseDto> confirmPayment(Double amount, String razorpay_payment_id, Principal principal) {
		User user = getLoggedInUser(principal);
		SavingBankAccount account = user.getBankAccount();
		if (account == null)
			throw new DataNotFoundException("No Bank Accounts FOund Linked with This User account");
		else {
			List<BankTransactions> transactions = account.getBankTransactions();
			if (transactions == null)
				transactions = new LinkedList<BankTransactions>();
			BankTransactions transaction = new BankTransactions(null, razorpay_payment_id, amount / 100, "DEPOSIT",
					null, account.getBalance(), account.getBalance() + amount / 100);
			transactions.add(transaction);
			account.setBalance(account.getBalance() + amount / 100);
			account.setBankTransactions(transactions);
			savingAccountRepository.save(account);
			return ResponseEntity.ok(new ResponseDto("Deposit Success", transaction));
		}
	}

	@Override
	@Transactional
	public ResponseEntity<ResponseDto> transfer(Principal principal, TransferDto dto) {
		User user = getLoggedInUser(principal);
		SavingBankAccount fromAccount = user.getBankAccount();
		SavingBankAccount toAccount = savingAccountRepository.findById(dto.getToAccountNumber())
				.orElseThrow(() -> new DataNotFoundException("Invalid ToAccount Number"));
		if (fromAccount == null)
			throw new DataNotFoundException("No Bank Accounts Found Linked with This User account");
		else {
			if (fromAccount.getAccountNumber() == toAccount.getAccountNumber())
				throw new MissMatchException("To account Number Can not be Same as From");
			if (!fromAccount.isActive() || fromAccount.isBlocked() || toAccount.isBlocked() || !toAccount.isActive())
				throw new PaymentFailedException("Account is Not Active or Blocked Contact Admin");
			else {
				if (fromAccount.getBalance() < dto.getAmount())
					throw new MissMatchException("Not Enough Balance in Your Account");
				else {
					List<BankTransactions> fromTransactions = fromAccount.getBankTransactions();
					if (fromTransactions == null)
						fromTransactions = new LinkedList<BankTransactions>();
					BankTransactions fromTransaction = new BankTransactions(null, "", dto.getAmount(), "DEBIT", null,
							fromAccount.getBalance(), fromAccount.getBalance() - dto.getAmount());
					fromTransactions.add(fromTransaction);
					fromAccount.setBalance(fromAccount.getBalance() - dto.getAmount());
					fromAccount.setBankTransactions(fromTransactions);
					savingAccountRepository.save(fromAccount);

					List<BankTransactions> toTransactions = toAccount.getBankTransactions();
					if (toTransactions == null)
						toTransactions = new LinkedList<BankTransactions>();
					BankTransactions toTransaction = new BankTransactions(null, "", dto.getAmount(), "CREDIT", null,
							toAccount.getBalance(), toAccount.getBalance() + dto.getAmount());
					toTransactions.add(toTransaction);
					toAccount.setBalance(toAccount.getBalance() + dto.getAmount());

					toAccount.setBankTransactions(toTransactions);
					savingAccountRepository.save(toAccount);

					return ResponseEntity.ok(new ResponseDto("Amount Transfered Success", dto));
				}
			}
		}

	}

	private User getLoggedInUser(Principal principal) {
		if (principal == null)
			throw new DataNotFoundException("Not Logged in , Invalid Session");
		String email = principal.getName();
		User user = userRepository.findByEmail(email);
		if (user == null)
			throw new DataNotFoundException("Not Logged in , Invalid Session");
		else
			return user;
	}

}
