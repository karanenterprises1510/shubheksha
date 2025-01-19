package com.shubheksha.utils;

import java.nio.charset.StandardCharsets;
import java.util.Map;

import org.apache.velocity.app.VelocityEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.ui.velocity.VelocityEngineUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class VelocityUtility {

	@Autowired
	private VelocityEngine velocityEngine;

	public String getVmContent(final String vmfilename, final Map<String, Object> dataMap) {
		log.debug("getting content from velocity file : {}", vmfilename);
		final String vmFile = "VM/" + vmfilename;
		final String content = VelocityEngineUtils.mergeTemplateIntoString(velocityEngine, vmFile,
				StandardCharsets.UTF_8.name(), dataMap);
		log.debug("Content from velocity file : {}", content);
		return content;
	}
}
