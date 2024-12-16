package com.shubheksha.read.cust.repository;

import java.util.List;

import org.springframework.data.domain.Page;

import com.shubheksha.dto.ProductResponseDto;

public interface ProductDataCustomRepository {

	Page<ProductResponseDto> findProductData(List<Long> productIds, List<Long> categoryId, String productName, Integer sku, Double offerPrice,
			Double listPrice, String keywords, Integer identifier, Integer pageNo, Integer pageSize, String sortParam, String sortDir);
}
