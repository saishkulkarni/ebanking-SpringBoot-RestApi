package org.jsp.ebanking.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class BankTransactions {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String payment_id;
	private Double amount;
	private String type;
	@CreationTimestamp
	private LocalDateTime createdTime;
	private Double balanceBeforeTransaction;
	private Double balanceAfterTransaction;
}
