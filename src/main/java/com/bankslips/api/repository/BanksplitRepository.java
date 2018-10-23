package com.bankslips.api.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import com.bankslips.api.entity.Bankslip;

public interface BanksplitRepository extends CrudRepository<Bankslip, String> {

	@Transactional(readOnly = true)
	Optional<Bankslip> findByCustomer(String customer);

}
