package com.bankslips.api.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bankslips.api.entity.BankslipEntity;

/**
 * Extended Interface JpaRepository for List for Bankslips
 * 
 * @author Cristiano Firmino
 *
 */
@Repository
public interface ListRepository extends JpaRepository<BankslipEntity, String> {

	Optional<BankslipEntity> findByCustomer(String customer);
}
