package com.shubheksha.serviceImpl;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shubheksha.model.MasterData;
import com.shubheksha.read.repository.MasterDataRepository;
import com.shubheksha.service.MasterDataService;
import com.shubheksha.utils.Constant;

@Service
public class MasterDataServiceImpl implements MasterDataService {

	@Autowired
	private MasterDataRepository masterRepository;

	@Override
	public Map<String, String> getAllMasterData() {
		List<MasterData> masterData = masterRepository.findByActive(Constant.YES);
		return masterData.stream().filter(Objects::nonNull)
				.collect(Collectors.toMap(MasterData::getKey, val -> val.getValue()));
	}
}
