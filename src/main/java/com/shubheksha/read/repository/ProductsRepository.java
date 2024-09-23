package com.shubheksha.read.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.shubheksha.model.Products;

@Repository
public interface ProductsRepository extends JpaRepository<Products, Long> {

	List<Products> findByCategoryAndActive(Long category, String active);

	Products findBySkuAndActive(String sku, String active);

}
