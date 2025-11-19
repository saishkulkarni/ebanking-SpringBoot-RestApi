package org.jsp.ebanking.entity;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class SavingBankAccount {
	@Id
	@GeneratedValue(generator = "ACNO")
	@SequenceGenerator(name = "ACNO", initialValue = 1000100101, allocationSize = 1)
	private Long accountNumber;
	@Column(nullable = false)
	private String address;
	@Column(nullable = false)
	private String ifscCode;
	@Column(nullable = false)
	private String fullName;
	@Column(nullable = false)
	private String panNumber;
	@Column(nullable = false)
	private Long aadharNumber;
	@Column(nullable = false)
	private String branch;
	@Column(nullable = false)
	private Double balance;
	private boolean active;
	private boolean blocked;

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	List<BankTransactions> bankTransactions;
}
