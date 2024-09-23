package com.shubheksha.read.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.shubheksha.model.CartProductMap;

@Repository
public interface CartProductMapRepository extends JpaRepository<CartProductMap, Long> {

	List<CartProductMap> findByCartIdAndActive(Long cartId, String active);

}
