package com.shubheksha.write.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.shubheksha.model.Products;

@Repository
public interface ProductsWriteRepository extends JpaRepository<Products, Long> {

}
