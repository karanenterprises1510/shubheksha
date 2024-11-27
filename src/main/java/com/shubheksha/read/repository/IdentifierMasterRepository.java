package com.shubheksha.read.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.shubheksha.model.IdentifierMaster;

@Repository
public interface IdentifierMasterRepository extends JpaRepository<IdentifierMaster, Integer> {

}
