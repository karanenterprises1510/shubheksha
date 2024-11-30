package com.shubheksha.serviceImpl;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shubheksha.dto.CartRequestDto;
import com.shubheksha.dto.CartResponseDto;
import com.shubheksha.dto.OrderRequestDto;
import com.shubheksha.dto.OrderResponseDto;
import com.shubheksha.model.Cart;
import com.shubheksha.model.CartProductMap;
import com.shubheksha.model.Customers;
import com.shubheksha.model.Inventory;
import com.shubheksha.model.Orders;
import com.shubheksha.read.repository.CartProductMapRepository;
import com.shubheksha.read.repository.CartRepository;
import com.shubheksha.read.repository.CustomersRepository;
import com.shubheksha.read.repository.InventoryRepository;
import com.shubheksha.read.repository.OrdersRepository;
import com.shubheksha.service.OrderService;
import com.shubheksha.service.common.TwilioService;
import com.shubheksha.utils.Constant;
import com.shubheksha.write.repository.CustomersWriteRepository;
import com.shubheksha.write.repository.InventoryWriteRepository;
import com.shubheksha.write.repository.OrdersWriteRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class OrderServiceImpl implements OrderService {

	@Autowired
	private OrdersWriteRepository orderWriteRepository;

	@Autowired
	private CustomersWriteRepository customersWriteRepository;

	@Autowired
	private OrdersRepository orderRepository;

	@Autowired
	CartRepository cartRepository;

	@Autowired
	CartProductMapRepository cartProductMapRepository;

	@Autowired
	CustomersRepository customersRepository;

	@Autowired
	TwilioService twilioService;

	@Autowired
	InventoryWriteRepository inventoryWriteRepository;

	@Autowired
	InventoryRepository inventoryRepository;

	private Orders processOrder(final OrderRequestDto request) {
		Orders order = null;
		if (ObjectUtils.isNotEmpty(request)) {
			if (ObjectUtils.isNotEmpty(request.getOrderAmt())) {
				if (StringUtils.isNotEmpty(request.getName()) && StringUtils.isNotEmpty(request.getEmail())
						&& StringUtils.isNotEmpty(request.getMobile()) && StringUtils.isNotEmpty(request.getCity())
						&& StringUtils.isNotEmpty(request.getState()) && request.getPincode() > 0) {
					if (Constant.isValidEmailId(request.getEmail())) {
						if (Constant.isValidMobileNumber(request.getMobile())) {
							Customers existingCust = customersRepository.findByMobileAndActive(request.getMobile(),
									Constant.YES);
							Long customerId = null;
							if (ObjectUtils.isEmpty(existingCust)) {
								Customers customer = getCustomerEntity(request);
								customer = customersWriteRepository.save(customer);
								customerId = customer.getId();
							} else {
								customerId = existingCust.getId();
							}
							order = getOrderEntity(request, customerId);
							order = orderWriteRepository.save(order);
							// Sending message to seller.
							String msg = "New order recived on shubheksha.com \nOrder Amt : " + order.getOrderAmt()
									+ "\nOrder ID - " + order.getId()
									+ "\nKindly approve or reject on clicking the link https://www.google.co.in";
							twilioService.sendSms(Constant.SELLER_MOBILE, msg);
						} else {
							log.warn("customer mobile id is not valid : {}", request.getMobile());
						}
					} else {
						log.warn("customer email id is not valid : {}", request.getEmail());
					}
				} else {
					log.warn("customer details are not valid : {}", request.toString());
				}
			} else {
				log.warn("order amount is empty");
			}
		} else {
			log.warn("order save request is empty");
		}
		return order;
	}

	private Customers getCustomerEntity(final OrderRequestDto request) {
		final Date currentDate = new Date();
		final Customers customer = new Customers();
		customer.setName(request.getName());
		customer.setEmail(request.getEmail());
		customer.setMobile(request.getMobile());
		customer.setAddress(request.getAddress());
		customer.setCity(request.getCity());
		customer.setState(request.getState());
		customer.setPincode(request.getPincode());
		customer.setGst(request.getGst());
		customer.setActive(Constant.YES);
		customer.setCreatedate(currentDate);
		customer.setModidate(currentDate);
		return customer;
	}

	private Orders getOrderEntity(final OrderRequestDto request, final Long customerId) {
		Orders order = null;
		final Date currentDate = new Date();
		order = new Orders();
		order.setCustomerId(customerId);
		order.setOrderAmt(request.getOrderAmt());
		order.setApproved(Constant.NO);
		order.setCartId(request.getCartId());
		order.setCreatedby(0l);
		order.setModifiedby(0l);
		order.setActive(Constant.YES);
		order.setCreatedate(currentDate);
		order.setModidate(currentDate);
		return order;
	}

	@Override
	public Long saveOrderDetails(final OrderRequestDto request) {
		log.info("saving order detail for cart id : {}", request.getCartId());
		Long cartId = null;
		try {
			Orders order = processOrder(request);
			order = orderWriteRepository.save(order);
			if (ObjectUtils.isNotEmpty(order)) {
				cartId = order.getId();
			} else {
				log.warn("Order not saved with cart id : {}", request.getCartId());
			}
		} catch (Exception e) {
			log.error("Exception Occured while saving order detail : ", e);
		}
		return cartId;
	}

	@Override
	public boolean approveOrder(Long orderId) {
		log.info("approving order id : {}", orderId);
		Optional<Orders> order = orderRepository.findById(orderId);
		if (order.isPresent()) {
			Orders orderObj = order.get();
			orderObj.setApproved(Constant.YES);
			orderObj.setModidate(new Date());
			orderWriteRepository.save(orderObj);
			Optional<Cart> cart = cartRepository.findById(orderObj.getCartId());
			if (cart.isPresent()) {
				Cart cartObj = cart.get();
				List<CartProductMap> cartProdMap = cartProductMapRepository.findByCartIdAndActive(cartObj.getId(),
						Constant.YES);
				if (CollectionUtils.isNotEmpty(cartProdMap)) {
					Map<Long, List<CartProductMap>> productMap = cartProdMap.stream()
							.collect(Collectors.groupingBy(CartProductMap::getProductId));
					List<Inventory> productInventories = inventoryRepository
							.findByProductIdIn(productMap.keySet().stream().collect(Collectors.toList()));
					productInventories.forEach(inventory -> {
						if (productMap.containsKey(inventory.getProductId())) {
							inventory.setUnitsAvailable(inventory.getUnitsAvailable()
									- productMap.get(inventory.getProductId()).get(0).getUnit());
						}
					});
					inventoryWriteRepository.saveAll(productInventories);
				}
			}
			// Sending confirmation message to customer.
			Optional<Customers> customer = customersRepository.findById(order.get().getCustomerId());
			if (customer.isPresent()) {
				String custMsg = "Thanks for shopping with shubheksha.com. \nYour order has been confirmed by the seller "
						+ "and will be delivered to you soon.\nClick here to check the order details https://www.google.co.in.";
				twilioService.sendSms(customer.get().getMobile(), custMsg);
			}
			return true;
		} else {
			log.warn("Order id : {} does not exists", orderId);
		}
		return false;
	}

	@Override
	public boolean rejectOrder(Long orderId) {
		log.info("rejecting order id : {}", orderId);
		Optional<Orders> order = orderRepository.findById(orderId);
		if (order.isPresent()) {
			Orders orderObj = order.get();
			orderObj.setApproved(Constant.REJECTED);
			orderObj.setModidate(new Date());
			orderWriteRepository.save(orderObj);
			return true;
		} else {
			log.warn("Order id : {} does not exists", orderId);
		}
		return false;
	}

	@Override
	public OrderResponseDto getOrderDetail(Long orderId) {
		log.info("Getting order detail for product id : {}", orderId);
		OrderResponseDto orderDetail = null;
		try {
			final Optional<Orders> orderObj = orderRepository.findById(orderId);
			if (orderObj.isPresent()) {
				final Orders order = orderObj.get();
				orderDetail = mapOrderEntityToDto(order);
				return orderDetail;
			} else {
				log.warn("No product found with product id : {}", orderId);
			}
		} catch (Exception e) {
			log.error("Exception Occured while fetching product detail : ", e);
		}
		return orderDetail;
	}

	private OrderResponseDto mapOrderEntityToDto(final Orders order) {
		final OrderResponseDto orderDetail = new OrderResponseDto();
		orderDetail.setApproved(Constant.YES.equals(order.getApproved()));
		orderDetail.setOrderId(order.getId());
		orderDetail.setOrderAmount(order.getOrderAmt());

		Optional<Cart> cart = cartRepository.findById(order.getCartId());
		if (cart.isPresent()) {
			final CartResponseDto cartDetails = new CartResponseDto();
			cartDetails.setCartId(cart.get().getId());
			final List<CartProductMap> cartProdMap = cartProductMapRepository.findByCartIdAndActive(cart.get().getId(),
					Constant.YES);
			final List<CartRequestDto> prodDetails = cartProdMap.stream().map(data -> {
				CartRequestDto obj = new CartRequestDto();
				obj.setNoOfUnit(data.getUnit());
				obj.setProductId(data.getProductId());
				obj.setUnitPrice(data.getUnitPrice());
				return obj;
			}).collect(Collectors.toList());
			cartDetails.setProductList(prodDetails);
			cartDetails.setTotalAmount(
					prodDetails.stream().mapToDouble(data -> data.getNoOfUnit() * data.getUnitPrice()).sum());
			orderDetail.setCartDetails(cartDetails);
		}
		final Optional<Customers> customer = customersRepository.findById(order.getCustomerId());
		if (customer.isPresent()) {
			orderDetail.setCustAddress(customer.get().getAddress());
			orderDetail.setCustCity(customer.get().getCity());
			orderDetail.setCustEmail(customer.get().getEmail());
			orderDetail.setCustGst(customer.get().getGst());
			orderDetail.setCustId(customer.get().getId());
			orderDetail.setCustName(customer.get().getName());
			orderDetail.setCustPhone(customer.get().getMobile());
			orderDetail.setCustPincode(customer.get().getPincode());
			orderDetail.setCustState(customer.get().getState());
		}
		return orderDetail;
	}
}
