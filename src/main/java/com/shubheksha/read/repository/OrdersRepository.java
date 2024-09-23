package com.shubheksha.read.repository;

import org.springframework.data.repository.CrudRepository;

import com.shubheksha.model.Orders;

public interface OrdersRepository extends CrudRepository<Orders, Long> {

}
