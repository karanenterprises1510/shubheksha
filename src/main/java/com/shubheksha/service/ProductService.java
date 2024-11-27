package com.shubheksha.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.shubheksha.dto.ProdInventoryReqDto;
import com.shubheksha.dto.ProductInventoryRespDto;
import com.shubheksha.dto.ProductResponseDto;

public interface ProductService {

	Page<ProductResponseDto> fetchAllProducts(final Long categoryId, final String productName, final Integer sku,
			final Double offerPrice, final Double listPrice, final String keywords, final Integer pageNo,
			final Integer pageSize, final String sortParam, final String sortDir);

	ProductResponseDto getProductDetails(final Long productId);

	boolean updateProdInventory(ProdInventoryReqDto request);
	
	List<ProductInventoryRespDto> getProdInventory(Long categoryId);

	ProductResponseDto updateProduct(ProductResponseDto request);

	boolean deactivateProduct(Long productId);

	ProductResponseDto addProduct(ProductResponseDto product);
	
	List<String> getProductNames(String keyword);

}