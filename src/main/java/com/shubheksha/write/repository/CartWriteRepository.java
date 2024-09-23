package com.shubheksha.write.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.shubheksha.model.Cart;

@Repository
public interface CartWriteRepository extends JpaRepository<Cart, Long> {
}
