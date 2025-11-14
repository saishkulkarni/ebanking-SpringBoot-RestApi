package org.jsp.ebanking.repository;

import java.util.List;

import org.jsp.ebanking.entity.SavingBankAccount;
import org.springframework.data.jpa.repository.JpaRepository;


public interface SavingAccountRepository extends JpaRepository<SavingBankAccount, Long> {

	List<SavingBankAccount> findByActiveFalse();

}
