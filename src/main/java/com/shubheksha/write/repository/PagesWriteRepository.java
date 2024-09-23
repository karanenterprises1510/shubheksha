package com.shubheksha.write.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.shubheksha.model.Pages;

@Repository
public interface PagesWriteRepository extends JpaRepository<Pages, Long> {
}
