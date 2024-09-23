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

import com.shubheksha.dto.CategoryResponseDto;
import com.shubheksha.dto.CategoryTreeResponseDto;
import com.shubheksha.service.CategoryService;
import com.shubheksha.utils.Constant;

@RestController
@CrossOrigin(allowedHeaders = "*", origins = "*")
@RequestMapping(value = "/category")
public class CategoryController {

	@Autowired
	CategoryService categoryService;

	@GetMapping("/get-parent-categories")
	public ResponseEntity<?> getParentCategories() {
		final Map<String, Object> result = new HashMap<>();
		String status = null;
		String message = null;
		try {
			final List<CategoryResponseDto> data = categoryService.fetchAllParentCategories();
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

	@GetMapping("/get-child-categories")
	public ResponseEntity<?> getChildCategories(@RequestParam Long parentId) {
		final Map<String, Object> result = new HashMap<>();
		String status = null;
		String message = null;
		try {
			final List<CategoryResponseDto> data = categoryService.fetchChildCategories(parentId);
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

	@GetMapping("/get-all-categories")
	public ResponseEntity<?> getAllCategories() {
		final Map<String, Object> result = new HashMap<>();
		String status = null;
		String message = null;
		try {
			final List<CategoryTreeResponseDto> data = categoryService.fetchAllCategories();
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

	@GetMapping("/get-categories")
	public ResponseEntity<?> getCategories(@RequestParam(required = false) Long categoryId,
			@RequestParam(required = false) String categoryName, @RequestParam(required = false) String slug,
			@RequestParam(required = false) String title, @RequestParam(required = false) String pageTitle,
			@RequestParam(required = false) String description, @RequestParam(required = false) String keywords,
			@RequestParam(required = false) Integer pageNo, @RequestParam(required = false) Integer pageSize,
			@RequestParam(required = false) String sortParam, @RequestParam(required = false) String sortDir) {
		final Map<String, Object> result = new HashMap<>();
		String status = null;
		String message = null;
		try {
			final Page<CategoryResponseDto> data = categoryService.fetchAllCategories(categoryId, categoryName, slug,
					title, pageTitle, description, keywords, pageNo, pageSize, sortParam, sortDir);
			if (data != null && data.hasContent()) {
				status = Constant.OK;
				message = Constant.RESPONSE_SUCCESS_MESSAGE;
				result.put(Constant.LIST, data.getContent());
				result.put("totalPages", data.getTotalPages());
				result.put("totalCategories", data.getTotalElements());
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

	@GetMapping("/category-detail")
	public ResponseEntity<?> fetchCategoryDetail(@RequestParam Long categoryId) {
		Map<String, Object> result = new HashMap<>();
		String status = null;
		String message = null;
		try {
			CategoryResponseDto data = categoryService.getCategoryDetails(categoryId);
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

	@PostMapping("/update-category")
	public ResponseEntity<?> updateCategory(@RequestBody CategoryResponseDto category) {
		Map<String, Object> result = new HashMap<>();
		String status = null;
		String message = null;
		try {
			CategoryResponseDto data = categoryService.updateCategory(category);
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

	@PostMapping("/add-category")
	public ResponseEntity<?> addCategory(@RequestBody CategoryResponseDto category) {
		Map<String, Object> result = new HashMap<>();
		String status = null;
		String message = null;
		try {
			CategoryResponseDto data = categoryService.addCategory(category);
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

	@PostMapping("/deactivate-category")
	public ResponseEntity<?> deactivateCategory(@RequestParam Long categoryId) {
		Map<String, Object> result = new HashMap<>();
		String status = null;
		String message = null;
		try {
			boolean data = categoryService.deactivateCategory(categoryId);
			if (!ObjectUtils.isEmpty(data)) {
				status = Constant.OK;
				message = data ? "Category Deactivated Successfully" : "Category Does not Deactivated";
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
}