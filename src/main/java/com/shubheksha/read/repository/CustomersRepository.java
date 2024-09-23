package com.shubheksha.read.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.shubheksha.model.Customers;

@Repository
public interface CustomersRepository extends JpaRepository<Customers, Long> {

	Customers findByMobileAndActive(String mobile, String active);
}
