//package com.shubheksha.service.common;
//
//import java.util.ArrayList;
//import java.util.HashSet;
//import java.util.List;
//import java.util.Optional;
//import java.util.Set;
//import java.util.concurrent.ConcurrentHashMap;
//
//import org.apache.commons.collections4.CollectionUtils;
//import org.apache.commons.lang3.StringUtils;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Lazy;
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.stereotype.Service;
//import org.springframework.util.ObjectUtils;
//
//import com.shubheksha.model.Inventory;
//import com.shubheksha.model.Orders;
//import com.shubheksha.model.Pages;
//import com.shubheksha.model.RoleMaster;
//import com.shubheksha.model.Users;
//import com.shubheksha.read.repository.CartOrderMapRepository;
//import com.shubheksha.read.repository.MediaRepository;
//import com.shubheksha.read.repository.RoleMasterReadRepository;
//import com.shubheksha.read.repository.UsersRepository;
//import com.shubheksha.service.TeamMembersService;
//import com.shubheksha.utils.Constant;
//
//import lombok.extern.slf4j.Slf4j;
//
//@Slf4j
//@Service
//public class LocalCacheService {
//
//	private static ConcurrentHashMap<String, RoleMaster> roleMasterCache = new ConcurrentHashMap<>();
//	private static ConcurrentHashMap<String, Users> dispositionMasterCache = new ConcurrentHashMap<>();
//	private static ConcurrentHashMap<String, Inventory> orgMasterCache = new ConcurrentHashMap<>();
//	private static ConcurrentHashMap<Long, Set<Pages>> userHierarchyMap = new ConcurrentHashMap<>();
//	private static ConcurrentHashMap<String, List<Orders>> cndGroupMasterMap = new ConcurrentHashMap<>();
//	private static ConcurrentHashMap<Long, Orders> cndMasterMap = new ConcurrentHashMap<>();
//	
//	@Autowired
//	RoleMasterReadRepository roleMasterReadRepository;
//	
//	@Autowired
//	CartOrderMapRepository dispositionMasterRepository;
//	
//	@Autowired
//	UsersRepository organizationMasterReadRepository;
//	
//	@Lazy
//	@Autowired
//	TeamMembersService teamMembersService;
//	
//	@Autowired
//	MediaRepository mbDialTpcndReadRepository;
//	
//	@Scheduled(cron = "0 0 0/24 ? * *")
//	public void clearCache() {
//		
//		log.info("Clearing caches...");
//		
//		roleMasterCache.clear();
//		log.info("roleMasterCache cleared");
//		
//		dispositionMasterCache.clear();
//		log.info("dispositionMasterCache cleared");
//		
//		orgMasterCache.clear();
//		log.info("orgMasterCache cleared");
//		
//		userHierarchyMap.clear();
//		log.info("userHierarchyMap cleared");
//		
//		cndGroupMasterMap.clear();
//		log.info("cndGroupMasterMap cleared");
//		
//		cndMasterMap.clear();
//		log.info("cndMasterMap cleared");
//	}
//	
//	public RoleMaster getRoleMasterById(Long id) {
//		
//		if(id == null) {
//			return null;
//		}
//		if(!ObjectUtils.isEmpty(roleMasterCache.get(id.toString()))) {
//			return roleMasterCache.get(id.toString());
//		}
//		Optional<RoleMaster> roleMaster = roleMasterReadRepository.findByMbdrmidAndIsactiveAndDeleted(id, "Y", "N");
//		if(roleMaster.isEmpty()) {
//			return null;
//		}
//		roleMasterCache.put(id.toString(), roleMaster.get());
//		return roleMaster.get();
//	}
//	
//	public String getRoleDescById(Long id) {
//		
//		if(id == null) {
//			return StringUtils.EMPTY;
//		}
//		if(!ObjectUtils.isEmpty(roleMasterCache.get(id.toString()))) {
//			return roleMasterCache.get(id.toString()).getMbdroledesc();
//		}
//		Optional<RoleMaster> roleMaster = roleMasterReadRepository.findByMbdrmidAndIsactiveAndDeleted(id, "Y", "N");
//		if(roleMaster.isEmpty()) {
//			return StringUtils.EMPTY;
//		}
//		roleMasterCache.put(id.toString(), roleMaster.get());
//		return roleMaster.get().getMbdroledesc();
//	}
//	
//	public RoleMaster getRoleMasterByDesc(String desc, Long orgid) {
//		
//		if(StringUtils.isBlank(desc)) {
//			return null;
//		}
//		if(!ObjectUtils.isEmpty(roleMasterCache.get(desc))) {
//			return roleMasterCache.get(desc);
//		}
////		Optional<RoleMaster> roleMaster = roleMasterReadRepository.findByMbdroledesc(desc, orgid);
//		Optional<RoleMaster> roleMaster = roleMasterReadRepository.findByMbdroledesc(desc);
//		if(roleMaster.isEmpty()) {
//			return null;
//		}
//		roleMasterCache.put(desc, roleMaster.get());
//		return roleMaster.get();
//	}
//	
//	public Long getRoleIdByDesc(String desc, Long orgid) {
//		
//		if(StringUtils.isBlank(desc)) {
//			return null;
//		}
//		if(!ObjectUtils.isEmpty(roleMasterCache.get(desc))) {
//			return roleMasterCache.get(desc).getMbdrmid();
//		}
////		Optional<RoleMaster> roleMaster = roleMasterReadRepository.findByMbdroledesc(desc, orgid);
//		Optional<RoleMaster> roleMaster = roleMasterReadRepository.findByMbdroledesc(desc);
//		if(roleMaster.isEmpty()) {
//			return null;
//		}
//		roleMasterCache.put(desc, roleMaster.get());
//		return roleMaster.get().getMbdrmid();
//	}
//	
//	public Users getDispositionMasterById(Long id) {
//		
//		if(id == null) {
//			return null;
//		}
//		if(!ObjectUtils.isEmpty(dispositionMasterCache.get(id.toString()))) {
//			return dispositionMasterCache.get(id.toString());
//		}
//		Optional<Users> dispositionMaster = dispositionMasterRepository.findByMbdRfNumAndIsActiveAndDeleted(id, "Y", "N");
//		if(dispositionMaster.isEmpty()) {
//			return null;
//		}
//		dispositionMasterCache.put(id.toString(), dispositionMaster.get());
//		return dispositionMaster.get();
//	}
//	
//	public String getDispositionDescById(Long id) {
//		
//		if(id == null) {
//			return StringUtils.EMPTY;
//		}
//		if(!ObjectUtils.isEmpty(dispositionMasterCache.get(id.toString()))) {
//			return dispositionMasterCache.get(id.toString()).getDescription();
//		}
//		Optional<Users> dispositionMaster = dispositionMasterRepository.findByMbdRfNumAndIsActiveAndDeleted(id, "Y", "N");
//		if(dispositionMaster.isEmpty()) {
//			return StringUtils.EMPTY;
//		}
//		dispositionMasterCache.put(id.toString(), dispositionMaster.get());
//		return dispositionMaster.get().getDescription();
//	}
//	
//	public String getOrgnameByOrgId(Long orgId) {
//
//		if (orgId == null) {
//			return StringUtils.EMPTY;
//		}
//		if (!ObjectUtils.isEmpty(orgMasterCache.get(orgId.toString()))) {
//			return orgMasterCache.get(orgId.toString()).getMbdomname();
//		}
//		Optional<Inventory> orgMaster = organizationMasterReadRepository.findById(orgId);
//		if (orgMaster.isEmpty()) {
//			return StringUtils.EMPTY;
//		}
//		orgMasterCache.put(orgId.toString(), orgMaster.get());
//		return orgMaster.get().getMbdomname();
//	}
//	
//	public Set<Pages> getUserHierarchyByUserId(Long userId) {
//		Set<Pages> userHierarchy = new HashSet<Pages>();
//		if (userId == null) {
//			return userHierarchy;
//		}
//		if (!ObjectUtils.isEmpty(userHierarchyMap.get(userId))) {
//			return userHierarchyMap.get(userId);
//		}
//		userHierarchy = teamMembersService.getUserHierarchy(userId);
//		if (userHierarchy.isEmpty()) {
//			return userHierarchy;
//		}
//		userHierarchyMap.put(userId, userHierarchy);
//		return userHierarchy;
//	}
//	
//	public List<Orders> getCndListByCndGroup(String group, Long orgId) {
//		List<Orders> cndList = new ArrayList<Orders>();
//		if (StringUtils.isEmpty(group)) {
//			return cndList;
//		}
//		if (!CollectionUtils.isEmpty(cndGroupMasterMap.get(group))) {
//			return cndGroupMasterMap.get(group);
//		}
////		cndList = mbDialTpcndReadRepository.findByCndgroupAndMbdorgidAndIsactiveAndDeleted(group, orgId, Constant.YES,
////				Constant.NO);
//		cndList = mbDialTpcndReadRepository.findByCndgroupAndIsactiveAndDeleted(group, Constant.YES,
//		Constant.NO);
//		if (CollectionUtils.isEmpty(cndList)) {
//			cndGroupMasterMap.put(group, cndList);
//		}
//		return cndList;
//	}
//
//	public Orders getCndByCndrfnum(Long cndrfnum) {
//		if (ObjectUtils.isEmpty(cndrfnum)) {
//			return null;
//		}
//		if (!ObjectUtils.isEmpty(cndMasterMap.get(cndrfnum))) {
//			return cndMasterMap.get(cndrfnum);
//		}
//		Orders cndList = mbDialTpcndReadRepository.findByCndrfnum(cndrfnum);
//		if (ObjectUtils.isEmpty(cndList)) {
//			return null;
//		}
//		cndMasterMap.put(cndrfnum, cndList);
//		return cndList;
//	}
//	
//	public String getCndDescriptionByRfnum(Long cndrfnum) {
//		if (ObjectUtils.isEmpty(cndrfnum)) {
//			return null;
//		}
//		if (!ObjectUtils.isEmpty(cndMasterMap.get(cndrfnum))) {
//			return cndMasterMap.get(cndrfnum).getCnddesc();
//		}
//		return null;
//	}
//	
//	public void clearUserHierarchy() {
//		userHierarchyMap.clear();
//		log.info("userHierarchyMap cleared");
//	}
//	
//	public void clearCacheByGroup(String group) {
//		log.info("Clearing cache for group : {}", group);
//		switch (group) {
//		case "role": {
//			roleMasterCache.clear();
//			log.info("roleMasterCache cleared");
//			break;
//		}
//		case "disposition": {
//			dispositionMasterCache.clear();
//			log.info("dispositionMasterCache cleared");
//			break;
//		}
//		case "org": {
//			orgMasterCache.clear();
//			log.info("orgMasterCache cleared");
//			break;
//		}
//		case "user": {
//			userHierarchyMap.clear();
//			log.info("userHierarchyMap cleared");
//			break;
//		}
//		case "cndGroup": {
//			cndGroupMasterMap.clear();
//			log.info("cndGroupMasterMap cleared");
//			break;
//		}
//		case "cnd": {
//			cndMasterMap.clear();
//			log.info("cndMasterMap cleared");
//			break;
//		}
//		default:
//			throw new IllegalArgumentException("Unexpected value: " + group);
//		}
//	}
//	
//}
