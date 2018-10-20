package com.bankslip.api.bankslip.repository;

import org.springframework.data.repository.CrudRepository;

import com.bankslip.api.bankslip.entity.Bankslip;

public interface BanksplitRepository extends CrudRepository<Bankslip, String> {

}
