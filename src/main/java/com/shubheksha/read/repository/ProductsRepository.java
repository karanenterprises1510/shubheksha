package com.shubheksha.read.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.shubheksha.model.Products;

import io.lettuce.core.dynamic.annotation.Param;

@Repository
public interface ProductsRepository extends JpaRepository<Products, Long> {

	List<Products> findByCategoryAndActive(Long category, String active);

	Products findBySkuAndActive(String sku, String active);
	
	@Query(value = "select productName from Products where productName like %:keyword% and active = 'Y'")
	List<String> getProductNameList(@Param("keyword") String keyword);

}
