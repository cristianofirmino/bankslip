package com.bankslips.api.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.bankslips.api.entity.BankslipEntity;

/**
 * Extended Interface for BankslipEntity
 * @author Cristiano Firmino
 *
 */
@Repository
public interface BankslipstRepository extends CrudRepository<BankslipEntity, String> {

	Optional<BankslipEntity> findByCustomer(String customer);

}
