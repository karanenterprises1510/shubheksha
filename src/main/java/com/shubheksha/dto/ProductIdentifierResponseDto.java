package com.shubheksha.dto;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ProductIdentifierResponseDto implements Serializable {

	private static final long serialVersionUID = 1L;

	private Integer identifierId;
	private String identifierName;
}
