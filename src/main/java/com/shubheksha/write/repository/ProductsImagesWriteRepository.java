package com.shubheksha.write.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.shubheksha.model.ProductsImages;

@Repository
public interface ProductsImagesWriteRepository extends JpaRepository<ProductsImages, Long> {

}
