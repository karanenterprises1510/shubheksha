package com.shubheksha.write.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.shubheksha.model.CartProductMap;

@Repository
public interface CartProductMapWriteRepository extends JpaRepository<CartProductMap, Long> {

}
