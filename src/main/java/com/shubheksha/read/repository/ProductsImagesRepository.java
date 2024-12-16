package com.shubheksha.read.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.shubheksha.model.ProductsImages;

@Repository
public interface ProductsImagesRepository extends JpaRepository<ProductsImages, Long> {

	List<ProductsImages> findByProductIdAndActive(Long productId, String active);

}
