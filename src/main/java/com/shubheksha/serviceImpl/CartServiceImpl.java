package com.shubheksha.serviceImpl;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shubheksha.dto.CartRequestDto;
import com.shubheksha.dto.CartResponseDto;
import com.shubheksha.model.Cart;
import com.shubheksha.model.CartProductMap;
import com.shubheksha.model.Inventory;
import com.shubheksha.model.Products;
import com.shubheksha.model.ProductsImages;
import com.shubheksha.read.repository.CartProductMapRepository;
import com.shubheksha.read.repository.CartRepository;
import com.shubheksha.read.repository.InventoryRepository;
import com.shubheksha.read.repository.ProductsImagesRepository;
import com.shubheksha.read.repository.ProductsRepository;
import com.shubheksha.service.CartService;
import com.shubheksha.utils.Constant;
import com.shubheksha.write.repository.CartProductMapWriteRepository;
import com.shubheksha.write.repository.CartWriteRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class CartServiceImpl implements CartService {

	@Autowired
	private CartWriteRepository cartWriteRepository;

	@Autowired
	private CartProductMapWriteRepository cartProductMapWriteRepository;

	@Autowired
	CartRepository cartRepository;

	@Autowired
	CartProductMapRepository cartProductMapRepository;

	@Autowired
	InventoryRepository inventoryRepository;

	@Autowired
	ProductsRepository productsRepository;

	@Autowired
	ProductsImagesRepository productsImagesRepository;

	@Override
	public Long saveCartDetails(List<CartRequestDto> request) {
		log.info("saving cart detail with details : {}", request.toString());
		AtomicLong cartId = new AtomicLong();
		try {
			// validating cart details
			final AtomicBoolean validateUnits = new AtomicBoolean(Boolean.TRUE);
			for (CartRequestDto prod : request) {
				Inventory inventory = inventoryRepository.findByProductId(prod.getProductId());
				if (ObjectUtils.isEmpty(inventory) || inventory.getUnitsAvailable() < prod.getNoOfUnit()) {
					validateUnits.set(Boolean.FALSE);
					break;
				}
			}
			if (!validateUnits.get()) {
				log.warn("Product units are not available");
				return null;
			}
			List<CartProductMap> cartProdMap = getCartEntity(request);
			final Cart cart = new Cart();
			if (CollectionUtils.isNotEmpty(cartProdMap)) {
				final Date currentDate = new Date();
				cart.setActive(Constant.YES);
				cart.setCreatedate(currentDate);
				cart.setModidate(currentDate);
				cartWriteRepository.save(cart);
				cartId.set(cart.getId());
				cartProdMap.forEach(obj -> obj.setCartId(cartId.get()));
				cartProductMapWriteRepository.saveAll(cartProdMap);
			} else {
				log.warn("No product details found in the request");
			}
		} catch (Exception e) {
			log.error("Exception Occured while saving cart detail : ", e);
		}
		return cartId.get();
	}

	private List<CartProductMap> getCartEntity(final List<CartRequestDto> request) {
		List<CartProductMap> cartProductMap = request.stream().filter(Objects::nonNull)
				.filter(product -> ObjectUtils.isNotEmpty(product.getProductId()))
				.filter(data -> data.getNoOfUnit() > 0 && data.getUnitPrice() > 0).map(this::mapCartReqToProductMap)
				.collect(Collectors.toList());
		return cartProductMap;
	}

	private CartProductMap mapCartReqToProductMap(CartRequestDto product) {
		CartProductMap cartProductMapObj = new CartProductMap();
		final Date currentDate = new Date();
		cartProductMapObj.setProductId(product.getProductId());
		cartProductMapObj.setUnit(product.getNoOfUnit());
		cartProductMapObj.setUnitPrice(product.getUnitPrice());
		cartProductMapObj.setTotalPrice(product.getUnitPrice() * product.getNoOfUnit());
		cartProductMapObj.setActive(Constant.YES);
		cartProductMapObj.setCreatedate(currentDate);
		cartProductMapObj.setModidate(currentDate);
		return cartProductMapObj;
	}

	@Override
	public CartResponseDto getCartDetail(Long cartId) {
		log.info("Getting cart detail for cart id : {}", cartId);
		CartResponseDto cartDetail = null;
		try {
			final Optional<Cart> orderObj = cartRepository.findById(cartId);
			if (orderObj.isPresent()) {
				final Cart cart = orderObj.get();
				cartDetail = mapOrderEntityToDto(cart);
				return cartDetail;
			} else {
				log.warn("No product found with product id : {}", cartId);
			}
		} catch (Exception e) {
			log.error("Exception Occured while fetching product detail : ", e);
		}
		return cartDetail;
	}

	private CartResponseDto mapOrderEntityToDto(final Cart cart) {
		final CartResponseDto cartDetails = new CartResponseDto();
		cartDetails.setCartId(cart.getId());
		final List<CartProductMap> cartProdMap = cartProductMapRepository.findByCartIdAndActive(cart.getId(),
				Constant.YES);
		final List<CartRequestDto> prodDetails = cartProdMap.stream().map(data -> {
			CartRequestDto obj = new CartRequestDto();
			obj.setNoOfUnit(data.getUnit());
			obj.setProductId(data.getProductId());
			obj.setUnitPrice(data.getUnitPrice());
			Optional<Products> prod = productsRepository.findById(data.getProductId());
			if (prod.isPresent()) {
				obj.setListPrice(prod.get().getListPrice());
				obj.setProductName(prod.get().getProductName());
				ProductsImages prodImage = productsImagesRepository
						.findTop1ByProductIdAndIsPrimaryAndActive(prod.get().getId(), Constant.YES, Constant.YES);
				if (ObjectUtils.isNotEmpty(prodImage)) {
					obj.setProductImg(prodImage.getImgUrl());
				}
			}
			return obj;
		}).collect(Collectors.toList());
		cartDetails.setProductList(prodDetails);
		cartDetails.setTotalAmount(
				prodDetails.stream().mapToDouble(data -> data.getNoOfUnit() * data.getUnitPrice()).sum());
		cartDetails
				.setTotalMRP(prodDetails.stream().mapToDouble(data -> data.getNoOfUnit() * data.getListPrice()).sum());
		return cartDetails;
	}
}
