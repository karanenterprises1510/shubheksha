package com.shubheksha.read.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.shubheksha.model.MasterData;

public interface MasterDataRepository extends CrudRepository<MasterData, Long> {

	List<MasterData> findByActive(String active);

}
