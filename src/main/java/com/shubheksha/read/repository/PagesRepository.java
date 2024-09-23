package com.shubheksha.read.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.shubheksha.model.Pages;

@Repository
public interface PagesRepository extends JpaRepository<Pages, Long> {
}
