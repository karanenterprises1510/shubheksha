package com.shubheksha.read.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.shubheksha.model.Users;

public interface UsersRepository extends JpaRepository<Users, Long> {

}
