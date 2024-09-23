package com.shubheksha.write.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.shubheksha.model.Role;

@Repository
public interface RoleWriteRepository extends JpaRepository<Role, Long> {

}
