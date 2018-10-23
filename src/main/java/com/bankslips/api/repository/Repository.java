package com.bankslips.api.repository;

import org.springframework.data.repository.CrudRepository;

import com.bankslips.api.entity.AbstractEntity;

public interface Repository extends CrudRepository<AbstractEntity, String> {

}
