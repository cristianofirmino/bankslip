package com.bankslips.api.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.bankslips.api.entity.BankslipEntity;

@Repository
public interface BankslipstRepository extends CrudRepository<BankslipEntity, String> {

	Optional<BankslipEntity> findByCustomer(String customer);

}
