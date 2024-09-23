package com.shubheksha.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.shubheksha.dto.CategoryResponseDto;
import com.shubheksha.dto.CategoryTreeResponseDto;

public interface CategoryService {

	List<CategoryResponseDto> fetchAllParentCategories();

	List<CategoryResponseDto> fetchChildCategories(final Long parentCategory);

	List<CategoryTreeResponseDto> fetchAllCategories();

	Page<CategoryResponseDto> fetchAllCategories(
			Long categoryId, String categoryName, String slug, String title,
			String pageTitle, String description, String keywords, Integer pageNo, Integer pageSize, String sortParam,
			String sortDir);

	CategoryResponseDto getCategoryDetails(Long categoryId);

	CategoryResponseDto updateCategory(CategoryResponseDto request);

	boolean deactivateCategory(Long categoryId);

	CategoryResponseDto addCategory(CategoryResponseDto category);

}