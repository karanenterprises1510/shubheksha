package com.shubheksha.write.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.shubheksha.model.Category;

@Repository
public interface CategoryWriteRepository extends JpaRepository<Category, Long> {

}
