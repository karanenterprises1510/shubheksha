package com.shubheksha.write.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.shubheksha.model.Customers;

@Repository
public interface CustomersWriteRepository extends JpaRepository<Customers, Long> {

}
