package com.shubheksha.write.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.shubheksha.model.Users;

public interface UsersWriteRepository extends JpaRepository<Users, Long> {

}
