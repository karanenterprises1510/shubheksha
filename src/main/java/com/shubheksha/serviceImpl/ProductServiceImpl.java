package com.shubheksha.serviceImpl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import com.shubheksha.dto.ProdInventoryReqDto;
import com.shubheksha.dto.ProductIdentifierResponseDto;
import com.shubheksha.dto.ProductImageDto;
import com.shubheksha.dto.ProductInventoryRespDto;
import com.shubheksha.dto.ProductResponseDto;
import com.shubheksha.model.Category;
import com.shubheksha.model.IdentifierMaster;
import com.shubheksha.model.Inventory;
import com.shubheksha.model.Products;
import com.shubheksha.model.ProductsImages;
import com.shubheksha.read.cust.repository.ProductDataCustomRepository;
import com.shubheksha.read.repository.CategoryRepository;
import com.shubheksha.read.repository.IdentifierMasterRepository;
import com.shubheksha.read.repository.InventoryRepository;
import com.shubheksha.read.repository.ProductsImagesRepository;
import com.shubheksha.read.repository.ProductsRepository;
import com.shubheksha.service.ProductService;
import com.shubheksha.utils.Constant;
import com.shubheksha.write.repository.InventoryWriteRepository;
import com.shubheksha.write.repository.ProductsImagesWriteRepository;
import com.shubheksha.write.repository.ProductsWriteRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ProductServiceImpl implements ProductService {

	@Autowired
	private ProductDataCustomRepository productCustRepo;

	@Autowired
	private ProductsRepository productsRepository;

	@Autowired
	private CategoryRepository categoryRepository;

	@Autowired
	private InventoryWriteRepository inventoryWriteRepository;

	@Autowired
	private InventoryRepository inventoryRepository;

	@Autowired
	private ProductsWriteRepository productWriteRepo;

	@Autowired
	private IdentifierMasterRepository identifierMasterRepo;

	@Autowired
	private ProductsImagesRepository productsImagesRepo;

	@Autowired
	ProductsImagesWriteRepository productsImagesWriteRepo;

	@Override
	public Page<ProductResponseDto> fetchAllProducts(List<Long> productIds, Long categoryId, String productName,
			Integer sku, Double offerPrice, Double listPrice, String keywords, Integer identifier, Integer pageNo,
			Integer pageSize, String sortParam, String sortDir) {
		log.info(
				"Getting products based on category id : {}, product name : {}, sku : {}, offerPrice : {}, listPrice : {}, keywords : {}",
				categoryId, productName, sku, offerPrice, listPrice, keywords);
		Page<ProductResponseDto> responseList = Page.empty();
		try {
			List<Long> categoryIds = new ArrayList<>();
			if (!ObjectUtils.isEmpty(categoryId)) {
				categoryIds.add(categoryId);
				List<Category> childCategories = categoryRepository.findByParentCategoryAndActive(categoryId,
						Constant.YES);
				if (CollectionUtils.isNotEmpty(childCategories)) {
					categoryIds.addAll(childCategories.stream().map(Category::getId).collect(Collectors.toList()));
				}
			}
			responseList = productCustRepo.findProductData(productIds, categoryIds, productName, sku, offerPrice,
					listPrice, keywords, identifier, pageNo, pageSize, sortParam, sortDir);
			if (responseList.hasContent()) {
				responseList.getContent().parallelStream().forEach(prod -> {
					Inventory inventory = inventoryRepository.findByProductId(prod.getProductId());
					if (!ObjectUtils.isEmpty(inventory)) {
						prod.setInStock(inventory.getUnitsAvailable() > 0);
						prod.setUnitsAvailable(inventory.getUnitsAvailable());
					}
					List<ProductsImages> images = productsImagesRepo.findByProductIdAndActive(prod.getProductId(),
							Constant.YES);
					if (CollectionUtils.isNotEmpty(images)) {
						List<ProductImageDto> imgList = getImgDTOList(images);
						prod.setImageList(imgList);
					}
				});
			}
			return responseList;
		} catch (Exception e) {
			log.error("Exception occured while fetching product data : ", e);
		}
		return null;
	}

	private List<ProductImageDto> getImgDTOList(final List<ProductsImages> images) {
		final List<ProductImageDto> imgList = images.stream().map(img -> {
			ProductImageDto imgDto = new ProductImageDto();
			imgDto.setImageId(img.getId());
			imgDto.setProductImg(img.getImgName());
			imgDto.setProductImgCaption(img.getImgCaption());
			imgDto.setProductImgUrl(img.getImgUrl());
			imgDto.setPrimary(Constant.YES.equals(img.getIsPrimary()));
			return imgDto;
		}).collect(Collectors.toList());
		return imgList;
	}

	@Override
	public ProductResponseDto getProductDetails(final Long productId) {
		log.info("Getting product detail for product id : {}", productId);
		ProductResponseDto prodDetail = null;
		try {
			final Optional<Products> prodObj = productsRepository.findById(productId);
			if (prodObj.isPresent()) {
				final Products prod = prodObj.get();
				prodDetail = mapProductEntityToDto(prod);
				return prodDetail;
			} else {
				log.warn("No product found with product id : {}", productId);
			}
		} catch (Exception e) {
			log.error("Exception Occured while fetching product detail : ", e);
		}
		return prodDetail;
	}

	private ProductResponseDto mapProductEntityToDto(final Products prod) {
		ProductResponseDto prodDetail;
		prodDetail = new ProductResponseDto();
		prodDetail.setActive(prod.getActive());
		prodDetail.setCanonicalUrl(prod.getCanonicalUrl());
		prodDetail.setCreateDate(prod.getCreatedate());
		prodDetail.setDescription(prod.getDescription());
		prodDetail.setKeywords(prod.getKeywords());
		if (prod.getIdentifier() != null) {
			Optional<IdentifierMaster> im = identifierMasterRepo.findById(prod.getIdentifier());
			if (im.isPresent()) {
				prodDetail.setIdentifier(im.get().getIdentifier());
			}
		}
		prodDetail.setListPrice(prod.getListPrice());
		prodDetail.setMetaDescription(prod.getMetaDesc());
		prodDetail.setModiDate(prod.getModidate());
		prodDetail.setOfferPrice(prod.getOfferPrice());
		prodDetail.setPermalink(prod.getPermalink());
		if (!ObjectUtils.isEmpty(prod.getCategory())) {
			prodDetail.setProductCategory(prod.getCategory());
			Optional<Category> categoryObj = categoryRepository.findById(prod.getCategory());
			if (categoryObj.isPresent()) {
				prodDetail.setProductCatName(categoryObj.get().getCategoryName());
			}
		}
		prodDetail.setProductId(prod.getId());
		prodDetail.setProductName(prod.getProductName());
		prodDetail.setProductTitle(prod.getProductTitle());
		prodDetail.setRelatedProducts(prod.getRelatedProducts());
		prodDetail.setSku(prod.getSku());
		prodDetail.setSlug(prod.getSlug());
		List<ProductsImages> images = productsImagesRepo.findByProductIdAndActive(prod.getId(), Constant.YES);
		if (CollectionUtils.isNotEmpty(images)) {
			prodDetail.setImageList(getImgDTOList(images));
		}
		Inventory inventory = inventoryRepository.findByProductId(prod.getId());
		if (!ObjectUtils.isEmpty(inventory)) {
			prodDetail.setInStock(inventory.getUnitsAvailable() > 0);
			prodDetail.setUnitsAvailable(inventory.getUnitsAvailable());
		}
		return prodDetail;
	}

	@Override
	public boolean updateProdInventory(ProdInventoryReqDto request) {
		log.info("Updating product inventory for product id : {}", request.getProductId());
		Optional<Products> product = productsRepository.findById(request.getProductId());
		if (product.isPresent()) {
			if (request.getUnit() != null && request.getUnit() >= 0) {
				Inventory inventory = null;
				inventory = inventoryRepository.findByProductId(request.getProductId());
				if (ObjectUtils.isEmpty(inventory)) {
					inventory = new Inventory();
					Date currentDate = new Date();
					inventory.setCreatedate(currentDate);
					inventory.setModidate(currentDate);
					inventory.setProductId(request.getProductId());
				}
				inventory.setUnitsAvailable(request.getUnit());
				inventoryWriteRepository.save(inventory);
				return true;
			} else {
				log.warn("Please validate product inventory unit");
			}
		} else {
			log.warn("Product ID : {} does not exist", request.getProductId());
		}
		return false;
	}

	@Override
	public List<ProductInventoryRespDto> getProdInventory(Long categoryId) {
		log.info("Getting product inventories");
		List<ProductInventoryRespDto> result = null;
		List<Inventory> inventoryList = null;
		try {
			if (ObjectUtils.isEmpty(categoryId)) {
				inventoryList = inventoryRepository.findAll();
			} else {
				List<Products> products = productsRepository.findByCategoryAndActive(categoryId, Constant.YES);
				if (CollectionUtils.isNotEmpty(products)) {
					inventoryList = inventoryRepository.findByProductIdIn(products.stream().filter(Objects::nonNull)
							.map(Products::getCategory).collect(Collectors.toList()));
				} else {
					log.warn("No product found under category id : {}", categoryId);
				}
			}
			if (CollectionUtils.isNotEmpty(inventoryList)) {
				result = inventoryList.stream().filter(Objects::nonNull).map(this::mapProductToInventoryObj)
						.collect(Collectors.toList());
			} else {
				log.warn("No product inventory found");
			}
		} catch (Exception e) {
			log.error("Exception Occured while getting product inventory : ", e);
		}
		return result;
	}

	private ProductInventoryRespDto mapProductToInventoryObj(Inventory inventory) {
		if (ObjectUtils.isEmpty(inventory)) {
			return null;
		}
		final ProductInventoryRespDto resp = new ProductInventoryRespDto();
		resp.setProductId(inventory.getProductId());
		Optional<Products> product = productsRepository.findById(inventory.getProductId());
		resp.setProductName(product.get().getProductName());
		Optional<Category> category = categoryRepository.findById(product.get().getCategory());
		resp.setCategoryId(category.get().getId());
		resp.setCategoryName(category.get().getCategoryName());
		resp.setSku(product.get().getSku());
		resp.setUnit(inventory.getUnitsAvailable());
		return resp;
	}

	@Override
	public ProductResponseDto updateProduct(ProductResponseDto request) {
		ProductResponseDto response = null;
		if (!ObjectUtils.isEmpty(request)) {
			if (!ObjectUtils.isEmpty(request.getProductId())) {
				Optional<Products> dbProdObj = productsRepository.findById(request.getProductId());
				if (dbProdObj.isPresent()) {
					Products dbProd = dbProdObj.get();
					if (StringUtils.isNotEmpty(request.getCanonicalUrl())
							&& !request.getCanonicalUrl().equals(dbProd.getCanonicalUrl())) {
						dbProd.setCanonicalUrl(request.getCanonicalUrl());
					}
					if (StringUtils.isNotEmpty(request.getDescription())
							&& !request.getDescription().equals(dbProd.getDescription())) {
						dbProd.setDescription(request.getDescription());
					}
					if (StringUtils.isNoneBlank(request.getKeywords())
							&& !request.getKeywords().equals(dbProd.getKeywords())) {
						dbProd.setKeywords(request.getKeywords());
					}
					if (request.getIdentifierId() != null
							&& !request.getIdentifierId().equals(dbProd.getIdentifier())) {
						dbProd.setIdentifier(request.getIdentifierId());
					}
					if (request.getListPrice() != null && !request.getListPrice().equals(dbProd.getListPrice())) {
						dbProd.setListPrice(request.getListPrice());
					}
					if (StringUtils.isNoneEmpty(request.getMetaDescription())
							&& !request.getMetaDescription().equals(dbProd.getMetaDesc())) {
						dbProd.setMetaDesc(request.getMetaDescription());
					}
					if (request.getOfferPrice() != null && !request.getOfferPrice().equals(dbProd.getOfferPrice())) {
						dbProd.setOfferPrice(request.getOfferPrice());
					}
					if (request.getProductCategory() != null
							&& !request.getProductCategory().equals(dbProd.getCategory())) {
						Optional<Category> category = categoryRepository.findById(dbProd.getCategory());
						if (category.isPresent() && category.get().getActive().equals(Constant.YES)) {
							dbProd.setOfferPrice(request.getOfferPrice());
						} else {
							log.warn("Category is not valid");
							return response;
						}
					}
					if (StringUtils.isNoneEmpty(request.getProductName())
							&& !request.getProductName().equals(dbProd.getProductName())) {
						dbProd.setProductName(request.getProductName());
					}
					if (StringUtils.isNoneEmpty(request.getProductTitle())
							&& !request.getProductTitle().equals(dbProd.getProductTitle())) {
						dbProd.setProductTitle(request.getProductTitle());
					}
					if (StringUtils.isNoneEmpty(request.getRelatedProducts())
							&& !request.getRelatedProducts().equals(dbProd.getRelatedProducts())) {
						dbProd.setRelatedProducts(request.getRelatedProducts());
					}
					if (request.getSku() != null && !request.getSku().equals(dbProd.getSku())) {
						dbProd.setSku(request.getSku());
					}
					if (StringUtils.isNoneEmpty(request.getSlug()) && !request.getSlug().equals(dbProd.getSlug())) {
						dbProd.setSlug(request.getSlug());
					}
					dbProd.setModidate(new Date());
					if (CollectionUtils.isNotEmpty(request.getImageList())) {
						List<ProductsImages> imageList = request.getImageList().stream().map(prod -> {
							ProductsImages prodImg = new ProductsImages();
							prodImg.setProductId(dbProd.getId());
							prodImg.setImgCaption(prod.getProductImgCaption());
							prodImg.setImgName(prod.getProductImg());
							prodImg.setImgUrl(prod.getProductImgUrl());
							prodImg.setActive(Constant.YES);
							prodImg.setIsPrimary(prod.isPrimary() ? Constant.YES : Constant.NO);
							return prodImg;
						}).collect(Collectors.toList());
						List<ProductsImages> existingImgs = productsImagesRepo
								.findByProductIdAndActive(request.getProductId(), Constant.YES);
						if (CollectionUtils.isNotEmpty(existingImgs)) {
							existingImgs.forEach(existingImg -> {
								existingImg.setActive(Constant.NO);
								existingImg.setModidate(new Date());
							});
							productsImagesWriteRepo.saveAll(existingImgs);
						}
						productsImagesWriteRepo.saveAll(imageList);
					}
					productWriteRepo.save(dbProd);
					response = request;
				} else {
					log.warn("No product found with product id - {}", request.getProductId());
				}
			} else {
				log.warn("no product id found");
			}
		} else {
			log.warn("empty requets");
		}
		return response;
	}

	@Override
	public boolean deactivateProduct(Long productId) {
		if (!ObjectUtils.isEmpty(productId)) {
			Optional<Products> productObj = productsRepository.findById(productId);
			if (productObj.isPresent()) {
				if (productObj.get().getActive().equals(Constant.YES)) {
					productObj.get().setActive(Constant.NO);
					productWriteRepo.save(productObj.get());
					log.info("product id : {} is deactivated", productId);
					return true;
				} else {
					log.warn("product is not active");
				}
			} else {
				log.warn("Invalid product id");
			}
		} else {
			log.warn("product id is blank");
		}
		return false;
	}

	@Override
	public ProductResponseDto addProduct(ProductResponseDto product) {
		ProductResponseDto response = null;
		Products dbProduct = new Products();
		if (!ObjectUtils.isEmpty(product)) {
			if (StringUtils.isEmpty(product.getDescription())) {
				log.warn("Product Description is blank");
				return null;
			} else {
				dbProduct.setDescription(product.getDescription());
			}
			if (StringUtils.isEmpty(product.getKeywords())) {
				log.warn("Keywords can not be empty");
				return null;
			} else {
				dbProduct.setKeywords(product.getKeywords());
			}
			if (product.getListPrice() == null || product.getListPrice() <= 0) {
				log.warn("Please validate List Price");
				return null;
			} else {
				dbProduct.setListPrice(product.getListPrice());
			}
			if (StringUtils.isEmpty(product.getMetaDescription())) {
				log.warn("Meta Description can not be empty");
				return null;
			} else {
				dbProduct.setMetaDesc(product.getMetaDescription());
			}
			if (product.getOfferPrice() == null || product.getOfferPrice() <= 0) {
				log.warn("Please validate Offer price");
				return null;
			} else {
				dbProduct.setOfferPrice(product.getOfferPrice());
			}
			if (product.getProductCategory() == null) {
				log.warn("Product Category can not be empty");
				return null;
			} else {
				Optional<Category> category = categoryRepository.findById(product.getProductCategory());
				if (category.isPresent() && category.get().getActive().equals(Constant.YES)) {
					dbProduct.setCategory(product.getProductCategory());
				} else {
					log.warn("Category is not valid");
					return null;
				}
			}
			if (product.getIdentifierId() != null) {
				dbProduct.setIdentifier(product.getIdentifierId());
			}
			if (StringUtils.isEmpty(product.getProductName())) {
				log.warn("Product name can not be empty");
				return null;
			} else {
				dbProduct.setProductName(product.getProductName());
			}
			if (StringUtils.isEmpty(product.getProductTitle())) {
				log.warn("Product Title can not be empty");
				return null;
			} else {
				dbProduct.setProductTitle(product.getProductTitle());
			}
			dbProduct.setRelatedProducts(product.getRelatedProducts());
			if (product.getSku() == null) {
				log.warn("SKU can not be empty");
				return null;
			} else {
				Products prodWithSku = productsRepository.findBySkuAndActive(product.getSku(), Constant.YES);
				if (!ObjectUtils.isEmpty(prodWithSku)) {
					log.warn("Product with SKU - {} already exists", product.getSku());
					return null;
				} else {
					dbProduct.setSku(product.getSku());
				}
			}
			if (StringUtils.isEmpty(product.getSlug())) {
				log.warn("Product slug can not be empty");
				return null;
			} else {
				dbProduct.setSlug(product.getSlug());
			}

			Date currentDate = new Date();
			dbProduct.setActive(Constant.YES);
			dbProduct.setCreatedate(currentDate);
			dbProduct.setModidate(currentDate);
			dbProduct.setCreatedby(0l);
			dbProduct.setModifiedby(0l);
			productWriteRepo.save(dbProduct);
			if (CollectionUtils.isNotEmpty(product.getImageList())) {
				List<ProductsImages> imageList = product.getImageList().stream().map(prod -> {
					ProductsImages prodImg = new ProductsImages();
					prodImg.setProductId(dbProduct.getId());
					prodImg.setImgCaption(prod.getProductImgCaption());
					prodImg.setImgName(prod.getProductImg());
					prodImg.setImgUrl(prod.getProductImgUrl());
					prodImg.setActive(Constant.YES);
					prodImg.setIsPrimary(prod.isPrimary() ? Constant.YES : Constant.NO);
					return prodImg;
				}).collect(Collectors.toList());
				productsImagesWriteRepo.saveAll(imageList);
			}
			response = product;
		} else {
			log.warn("empty requets");
		}
		return response;
	}

	@Override
	public List<String> getProductNames(String keyword) {
		try {
			if (StringUtils.isNoneEmpty(keyword)) {
				return productsRepository.getProductNameList(keyword);
			} else {
				log.warn("Please type keywords");
			}
		} catch (Exception e) {
			log.error("Exception occured while fetching product name auto suggest : ", e);
		}
		return null;
	}

	@Override
	public List<ProductIdentifierResponseDto> getProductIdentifierList() {
		try {
			List<IdentifierMaster> identifiers = identifierMasterRepo.findAll();
			if (CollectionUtils.isNotEmpty(identifiers)) {
				return identifiers.stream().filter(id -> id.getActive().equals(Constant.YES)).map(data -> {
					ProductIdentifierResponseDto obj = new ProductIdentifierResponseDto();
					obj.setIdentifierId(data.getId());
					obj.setIdentifierName(data.getIdentifier());
					obj.setImgLink(data.getImage());
					return obj;
				}).collect(Collectors.toList());
			}
		} catch (Exception e) {
			log.error("Exception occured while fetching product identifier list : ", e);
		}
		return null;
	}
}
