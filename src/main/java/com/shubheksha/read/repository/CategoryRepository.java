package com.shubheksha.read.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.shubheksha.model.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

	List<Category> findByParentCategoryIsNullAndActive(String active);

	List<Category> findByParentCategoryAndActive(Long parentCategory, String active);

	List<Category> findByHomeCardAndActive(String homeCard, String active);

}
