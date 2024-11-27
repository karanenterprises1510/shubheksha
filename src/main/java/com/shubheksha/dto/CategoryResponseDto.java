package com.shubheksha.dto;

import java.io.Serializable;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class CategoryResponseDto implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long categoryId;
	private String categoryName;
	private Long parentId;
	private String parentName;
	private String catSlug;
	private String catTitle;
	private String pageTitle;
	private String description;
	private String keywords;
	private String catImg;
	private List<CategoryResponseDto> childCategories;
}
