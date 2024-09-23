package com.shubheksha.write.repository;

import org.springframework.data.repository.CrudRepository;

import com.shubheksha.model.Orders;

public interface OrdersWriteRepository extends CrudRepository<Orders, Long> {

}
