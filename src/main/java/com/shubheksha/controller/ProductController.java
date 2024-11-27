package com.shubheksha.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.shubheksha.dto.ProdInventoryReqDto;
import com.shubheksha.dto.ProductInventoryRespDto;
import com.shubheksha.dto.ProductResponseDto;
import com.shubheksha.service.ProductService;
import com.shubheksha.utils.Constant;

@RestController
@CrossOrigin(allowedHeaders = "*", origins = "*")
@RequestMapping(value = "/product")
public class ProductController {

	@Autowired
	ProductService productService;

	@GetMapping("/get-products")
	public ResponseEntity<?> getProducts(@RequestParam(required = false) Long categoryId,
			@RequestParam(required = false) String productName, @RequestParam(required = false) Integer sku,
			@RequestParam(required = false) Double offerPrice, @RequestParam(required = false) Double listPrice,
			@RequestParam(required = false) String keywords, @RequestParam(required = false) Integer pageNo,
			@RequestParam(required = false) Integer pageSize, @RequestParam(required = false) String sortParam,
			@RequestParam(required = false) String sortDir) {
		final Map<String, Object> result = new HashMap<>();
		String status = null;
		String message = null;
		try {
			final Page<ProductResponseDto> data = productService.fetchAllProducts(categoryId, productName, sku,
					offerPrice, listPrice, keywords, pageNo, pageSize, sortParam, sortDir);
			if (data != null && data.hasContent()) {
				status = Constant.OK;
				message = Constant.RESPONSE_SUCCESS_MESSAGE;
				result.put(Constant.LIST, data.getContent());
				result.put("totalPages", data.getTotalPages());
				result.put("totalProducts", data.getTotalElements());
			} else {
				status = Constant.NO_DATA;
				message = Constant.NO_DATA_MESSAGE;
			}
		} catch (Exception e) {
			status = Constant.SERVER_ERROR;
			message = Constant.RESPONSE_UNSUCCESS_MESSAGE;
		}
		result.put(Constant.STATUS, status);
		result.put(Constant.MESSAGE, message);
		return ResponseEntity.ok().body(result);
	}

	@GetMapping("/product-detail")
	public ResponseEntity<?> fetchProductDetail(@RequestParam Long productId) {
		Map<String, Object> result = new HashMap<>();
		String status = null;
		String message = null;
		try {
			ProductResponseDto data = productService.getProductDetails(productId);
			if (!ObjectUtils.isEmpty(data)) {
				status = Constant.OK;
				message = Constant.RESPONSE_SUCCESS_MESSAGE;
				result.put(Constant.DATA, data);
			} else {
				status = Constant.NO_DATA;
				message = Constant.NO_DATA_MESSAGE;
			}
		} catch (Exception e) {
			status = Constant.SERVER_ERROR;
			message = Constant.RESPONSE_UNSUCCESS_MESSAGE;
		}
		result.put(Constant.STATUS, status);
		result.put(Constant.MESSAGE, message);
		return ResponseEntity.ok().body(result);
	}

	@PostMapping("/update-product-inventory")
	public ResponseEntity<?> updateProductInverntory(@RequestBody ProdInventoryReqDto request) {
		Map<String, Object> result = new HashMap<>();
		String status = null;
		String message = null;
		try {
			boolean data = productService.updateProdInventory(request);
			if (!ObjectUtils.isEmpty(data)) {
				status = Constant.OK;
				message = Constant.RESPONSE_SUCCESS_MESSAGE;
				result.put(Constant.DATA, data);
			} else {
				status = Constant.NO_DATA;
				message = Constant.NO_DATA_MESSAGE;
			}
		} catch (Exception e) {
			status = Constant.SERVER_ERROR;
			message = Constant.RESPONSE_UNSUCCESS_MESSAGE;
		}
		result.put(Constant.STATUS, status);
		result.put(Constant.MESSAGE, message);
		return ResponseEntity.ok().body(result);
	}

	@GetMapping("/get-product-inventory")
	public ResponseEntity<?> getProductInventory(@RequestParam(required = false) Long categoryId) {
		Map<String, Object> result = new HashMap<>();
		String status = null;
		String message = null;
		try {
			List<ProductInventoryRespDto> list = productService.getProdInventory(categoryId);
			if (CollectionUtils.isNotEmpty(list)) {
				status = Constant.OK;
				message = Constant.RESPONSE_SUCCESS_MESSAGE;
				result.put(Constant.LIST, list);
			} else {
				status = Constant.NO_DATA;
				message = Constant.NO_DATA_MESSAGE;
			}
		} catch (Exception e) {
			status = Constant.SERVER_ERROR;
			message = Constant.RESPONSE_UNSUCCESS_MESSAGE;
		}
		result.put(Constant.STATUS, status);
		result.put(Constant.MESSAGE, message);
		return ResponseEntity.ok().body(result);
	}

	@PostMapping("/update-product")
	public ResponseEntity<?> updateProduct(@RequestBody ProductResponseDto product) {
		Map<String, Object> result = new HashMap<>();
		String status = null;
		String message = null;
		try {
			ProductResponseDto data = productService.updateProduct(product);
			if (!ObjectUtils.isEmpty(data)) {
				status = Constant.OK;
				message = Constant.RESPONSE_SUCCESS_MESSAGE;
				result.put(Constant.DATA, data);
			} else {
				status = Constant.NO_DATA;
				message = Constant.NO_DATA_MESSAGE;
			}
		} catch (Exception e) {
			status = Constant.SERVER_ERROR;
			message = Constant.RESPONSE_UNSUCCESS_MESSAGE;
		}
		result.put(Constant.STATUS, status);
		result.put(Constant.MESSAGE, message);
		return ResponseEntity.ok().body(result);
	}

	@PostMapping("/add-product")
	public ResponseEntity<?> addProduct(@RequestBody ProductResponseDto product) {
		Map<String, Object> result = new HashMap<>();
		String status = null;
		String message = null;
		try {
			ProductResponseDto data = productService.addProduct(product);
			if (!ObjectUtils.isEmpty(data)) {
				status = Constant.OK;
				message = Constant.RESPONSE_SUCCESS_MESSAGE;
				result.put(Constant.DATA, data);
			} else {
				status = Constant.NO_DATA;
				message = Constant.NO_DATA_MESSAGE;
			}
		} catch (Exception e) {
			status = Constant.SERVER_ERROR;
			message = Constant.RESPONSE_UNSUCCESS_MESSAGE;
		}
		result.put(Constant.STATUS, status);
		result.put(Constant.MESSAGE, message);
		return ResponseEntity.ok().body(result);
	}

	@PostMapping("/deactivate-product")
	public ResponseEntity<?> deactivateProduct(@RequestParam Long productId) {
		Map<String, Object> result = new HashMap<>();
		String status = null;
		String message = null;
		try {
			boolean data = productService.deactivateProduct(productId);
			if (!ObjectUtils.isEmpty(data)) {
				status = Constant.OK;
				message = data ? "Product Deactivated Successfully" : "Product Does not Deactivated";
				result.put(Constant.DATA, data);
			} else {
				status = Constant.NO_DATA;
				message = Constant.NO_DATA_MESSAGE;
			}
		} catch (Exception e) {
			status = Constant.SERVER_ERROR;
			message = Constant.RESPONSE_UNSUCCESS_MESSAGE;
		}
		result.put(Constant.STATUS, status);
		result.put(Constant.MESSAGE, message);
		return ResponseEntity.ok().body(result);
	}
	
	@GetMapping("/get-product-name-list")
	public ResponseEntity<?> getProductsNameList(@RequestParam(required = false) String keyword) {
		final Map<String, Object> result = new HashMap<>();
		String status = null;
		String message = null;
		try {
			final List<String> data = productService.getProductNames(keyword);
			if (CollectionUtils.isNotEmpty(data)) {
				status = Constant.OK;
				message = Constant.RESPONSE_SUCCESS_MESSAGE;
				result.put(Constant.LIST, data);
			} else {
				status = Constant.NO_DATA;
				message = Constant.NO_DATA_MESSAGE;
			}
		} catch (Exception e) {
			status = Constant.SERVER_ERROR;
			message = Constant.RESPONSE_UNSUCCESS_MESSAGE;
		}
		result.put(Constant.STATUS, status);
		result.put(Constant.MESSAGE, message);
		return ResponseEntity.ok().body(result);
	}

}