package com.shubheksha.write.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.shubheksha.model.Inventory;

@Repository
public interface InventoryWriteRepository extends JpaRepository<Inventory, Long> {

}
