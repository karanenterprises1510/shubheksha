package com.shubheksha.dto;

import java.io.Serializable;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class CategoryTreeResponseDto implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long categoryId;
	private String categoryName;
	private List<CategoryTreeResponseDto> childCategories;
}
