package com.shubheksha.serviceImpl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import com.shubheksha.dto.CategoryResponseDto;
import com.shubheksha.dto.CategoryTreeResponseDto;
import com.shubheksha.model.Category;
import com.shubheksha.read.cust.repository.CategoryCustomRepository;
import com.shubheksha.read.repository.CategoryRepository;
import com.shubheksha.service.CategoryService;
import com.shubheksha.utils.Constant;
import com.shubheksha.write.repository.CategoryWriteRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class CategoryServiceImpl implements CategoryService {
	@Autowired
	private CategoryRepository categoryRepo;

	@Autowired
	private CategoryCustomRepository categoryCustRepo;

	@Autowired
	CategoryWriteRepository categoryWriteRepo;

	@Override
	public List<CategoryResponseDto> fetchAllParentCategories() {
		log.info("Getting parent categories");
		try {
			final List<Category> parentCategories = categoryRepo.findByParentCategoryIsNullAndActive(Constant.YES);
			if (!parentCategories.isEmpty()) {
				return parentCategories.stream().map(this::mapCatEntityToCatDTO).filter(Objects::nonNull)
						.collect(Collectors.toList());
			} else {
				log.warn("No parent category found in the system");
			}
		} catch (Exception e) {
			log.error("Exception occured while fetching parent categories : ", e);
		}
		return null;
	}

	@Override
	public List<CategoryResponseDto> fetchChildCategories(final Long parentCategory) {
		log.info("Getting child categories for parent category : {}", parentCategory);
		try {
			final List<Category> childCategories = categoryRepo.findByParentCategoryAndActive(parentCategory,
					Constant.YES);
			if (!childCategories.isEmpty()) {
				return childCategories.stream().map(this::mapCatEntityToCatDTO).filter(Objects::nonNull)
						.collect(Collectors.toList());
			} else {
				log.warn("No child category found in the system for parent category : {}", parentCategory);
			}
		} catch (Exception e) {
			log.error("Exception occured while fetching child categories for parent category: {} - ", parentCategory,
					e);
		}
		return null;
	}

	private CategoryResponseDto mapCatEntityToCatDTO(final Category categoryDBObj) {
		CategoryResponseDto catDTO = null;
		if (!ObjectUtils.isEmpty(categoryDBObj)) {
			catDTO = new CategoryResponseDto();
			catDTO.setCategoryId(categoryDBObj.getId());
			catDTO.setCategoryName(categoryDBObj.getCategoryName());
			catDTO.setCatSlug(categoryDBObj.getSlug());
			catDTO.setCatTitle(categoryDBObj.getTitle());
			catDTO.setDescription(categoryDBObj.getDescription());
			catDTO.setKeywords(categoryDBObj.getKeywords());
			catDTO.setPageTitle(categoryDBObj.getPageTitle());
			catDTO.setCatImg(categoryDBObj.getCategoryImg());
			if (!ObjectUtils.isEmpty(categoryDBObj.getParentCategory())) {
				catDTO.setParentId(categoryDBObj.getParentCategory());
				Optional<Category> parentCat = categoryRepo.findById(categoryDBObj.getParentCategory());
				if (parentCat.isPresent()) {
					catDTO.setParentName(parentCat.get().getCategoryName());
				}
			}
		}
		return catDTO;
	}

	@Override
	public List<CategoryTreeResponseDto> fetchAllCategories() {
		log.info("Getting all categories");
		try {
			final List<Category> parentCategories = categoryRepo.findByParentCategoryIsNullAndActive(Constant.YES);
			if (!parentCategories.isEmpty()) {
				final List<CategoryTreeResponseDto> categories = new ArrayList<>();
				parentCategories.forEach(parentCat -> {
					final CategoryTreeResponseDto category = new CategoryTreeResponseDto();
					category.setCategoryId(parentCat.getId());
					category.setCategoryName(parentCat.getCategoryName());
					fetchCategoryTree(parentCat.getId(), category);
					categories.add(category);
				});
				return categories;
			} else {
				log.warn("No parent category found in the system");
			}
		} catch (Exception e) {
			log.error("Exception occured while fetching categories - ", e);
		}
		return null;
	}

	private void fetchCategoryTree(final Long parentId, final CategoryTreeResponseDto category) {
		final List<Category> childCategories = categoryRepo.findByParentCategoryAndActive(parentId, Constant.YES);
		if (!childCategories.isEmpty()) {
			final List<CategoryTreeResponseDto> catList = new ArrayList<>();
			childCategories.forEach(childCat -> {
				final CategoryTreeResponseDto childCategory = new CategoryTreeResponseDto();
				childCategory.setCategoryId(childCat.getId());
				childCategory.setCategoryName(childCat.getCategoryName());
				fetchCategoryTree(childCat.getId(), childCategory);
				catList.add(childCategory);
			});
			category.setChildCategories(catList);
		}
	}

	@Override
	public Page<CategoryResponseDto> fetchAllCategories(Long categoryId, String categoryName, String slug, String title,
			String pageTitle, String description, String keywords, Integer pageNo, Integer pageSize, String sortParam,
			String sortDir) {
		log.info(
				"Getting categories based on category id : {}, categoryName : {}, slug : {}, title : {}, pagetitle : {}, descriptiion : {}, keywords : {}",
				categoryId, categoryName, slug, title, pageTitle, description, keywords);
		Page<CategoryResponseDto> responseList = Page.empty();
		try {
			responseList = categoryCustRepo.findCategoryData(categoryId, categoryName, slug, title, pageTitle,
					description, keywords, pageNo, pageSize, sortParam, sortDir);
			return responseList;
		} catch (Exception e) {
			log.error("Exception occured while fetching category data : ", e);
		}
		return null;
	}

	@Override
	public CategoryResponseDto getCategoryDetails(Long categoryId) {
		log.info("Getting category detail for category id : {}", categoryId);
		CategoryResponseDto categoryDetail = null;
		try {
			final Optional<Category> categoryObj = categoryRepo.findById(categoryId);
			if (categoryObj.isPresent()) {
				final Category user = categoryObj.get();
				categoryDetail = mapCategoryEntityToDto(user);
				return categoryDetail;
			} else {
				log.warn("No user found with user id : {}", categoryId);
			}
		} catch (Exception e) {
			log.error("Exception Occured while fetching user detail : ", e);
		}
		return categoryDetail;
	}

	@Override
	public CategoryResponseDto updateCategory(CategoryResponseDto request) {
		CategoryResponseDto response = null;
		if (!ObjectUtils.isEmpty(request)) {
			if (!ObjectUtils.isEmpty(request.getCategoryId())) {
				Optional<Category> dbCatObj = categoryRepo.findById(request.getCategoryId());
				if (dbCatObj.isPresent()) {
					Category dbCat = dbCatObj.get();
					if (StringUtils.isNotEmpty(request.getCategoryName())
							&& !request.getCategoryName().equals(dbCat.getCategoryName())) {
						dbCat.setCategoryName(request.getCategoryName());
					}
					if (StringUtils.isNotEmpty(request.getCatSlug()) && !request.getCatSlug().equals(dbCat.getSlug())) {
						dbCat.setSlug(request.getCatSlug());
					}
					if (StringUtils.isNoneBlank(request.getCatTitle())
							&& !request.getCatTitle().equals(dbCat.getTitle())) {
						dbCat.setTitle(request.getCatTitle());
					}
					if (StringUtils.isNoneBlank(request.getDescription())
							&& !request.getDescription().equals(dbCat.getDescription())) {
						dbCat.setDescription(request.getDescription());
					}
					if (StringUtils.isNoneBlank(request.getKeywords())
							&& !request.getKeywords().equals(dbCat.getKeywords())) {
						dbCat.setKeywords(request.getKeywords());
					}
					if (StringUtils.isNoneBlank(request.getPageTitle())
							&& !request.getPageTitle().equals(dbCat.getPageTitle())) {
						dbCat.setPageTitle(request.getPageTitle());
					}
					if (StringUtils.isNoneBlank(request.getCatImg())
							&& !request.getCatImg().equals(dbCat.getCategoryImg())) {
						dbCat.setCategoryImg(request.getCatImg());
					}
					dbCat.setModidate(new Date());
					categoryWriteRepo.save(dbCat);
					response = request;
				} else {
					log.warn("No category found with category id - {}", request.getCategoryId());
				}
			} else {
				log.warn("no category id found");
			}
		} else {
			log.warn("empty requets");
		}
		return response;
	}

	@Override
	public boolean deactivateCategory(Long categoryId) {
		if (!ObjectUtils.isEmpty(categoryId)) {
			Optional<Category> catObj = categoryRepo.findById(categoryId);
			if (catObj.isPresent()) {
				if (catObj.get().getActive().equals(Constant.YES)) {
					catObj.get().setActive(Constant.NO);
					categoryWriteRepo.save(catObj.get());
					log.info("category id : {} is deactivated", categoryId);
					return true;
				} else {
					log.warn("category is not active");
				}
			} else {
				log.warn("Invalid category id");
			}
		} else {
			log.warn("category id is blank");
		}
		return false;
	}

	@Override
	public CategoryResponseDto addCategory(CategoryResponseDto category) {
		CategoryResponseDto response = null;
		Category dbCategory = new Category();
		if (!ObjectUtils.isEmpty(category)) {
			if (StringUtils.isEmpty(category.getCategoryName())) {
				log.warn("Category Name is blank");
				return null;
			} else {
				dbCategory.setCategoryName(category.getCategoryName());
			}
			if (StringUtils.isEmpty(category.getCatSlug())) {
				log.warn("Cat Slug is blank");
				return null;
			} else {
				dbCategory.setSlug(category.getCatSlug());
			}
			if (StringUtils.isEmpty(category.getCatTitle())) {
				log.warn("Cat Title can not be empty");
				return null;
			} else {
				dbCategory.setTitle(category.getCatTitle());
			}
			if (StringUtils.isEmpty(category.getDescription())) {
				log.warn("Cat Description can not be empty");
				return null;
			} else {
				dbCategory.setDescription(category.getDescription());
			}
			if (StringUtils.isEmpty(category.getKeywords())) {
				log.warn("Keywords can not be empty");
				return null;
			} else {
				dbCategory.setKeywords(category.getKeywords());
			}
			if (StringUtils.isEmpty(category.getPageTitle())) {
				log.warn("Page title can not be empty");
				return null;
			} else {
				dbCategory.setPageTitle(category.getPageTitle());
			}
			if (category.getParentId() != null) {
				Optional<Category> parentCat = categoryRepo.findById(category.getParentId());
				if (parentCat.isEmpty()) {
					log.warn("Parent Cat Id : {} does not exists", category.getParentId());
					return null;
				} else {
					dbCategory.setParentCategory(category.getParentId());
				}
			}
			if (StringUtils.isNotEmpty(category.getCatImg())) {
				dbCategory.setCategoryImg(category.getCatImg());
			}
			Date currentDate = new Date();
			dbCategory.setActive(Constant.YES);
			dbCategory.setCreatedate(currentDate);
			dbCategory.setModidate(currentDate);
			categoryWriteRepo.save(dbCategory);
			response = category;
		} else {
			log.warn("empty requets");
		}
		return response;
	}

	private CategoryResponseDto mapCategoryEntityToDto(final Category category) {
		CategoryResponseDto categoryDetail = new CategoryResponseDto();
		categoryDetail.setCategoryId(category.getId());
		categoryDetail.setCategoryName(category.getCategoryName());
		categoryDetail.setCatSlug(category.getSlug());
		categoryDetail.setCatTitle(category.getTitle());
		categoryDetail.setDescription(category.getDescription());
		categoryDetail.setChildCategories(fetchChildCategories(category.getId()));
		categoryDetail.setKeywords(category.getKeywords());
		categoryDetail.setPageTitle(category.getPageTitle());
		categoryDetail.setCatImg(category.getCategoryImg());
		if (!ObjectUtils.isEmpty(category.getParentCategory())) {
			categoryDetail.setParentId(category.getParentCategory());
			Optional<Category> parentCat = categoryRepo.findById(category.getParentCategory());
			if (parentCat.isPresent()) {
				categoryDetail.setParentName(parentCat.get().getCategoryName());
			}
		}
		return categoryDetail;
	}
}
