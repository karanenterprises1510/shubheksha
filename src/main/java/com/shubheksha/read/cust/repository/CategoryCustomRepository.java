package com.shubheksha.read.cust.repository;

import org.springframework.data.domain.Page;

import com.shubheksha.dto.CategoryResponseDto;

public interface CategoryCustomRepository {

	Page<CategoryResponseDto> findCategoryData(Long categoryId, String categoryName, String slug, String title,
			String pageTitle, String description, String keywords, Integer pageNo, Integer pageSize, String sortParam,
			String sortDir);
}
