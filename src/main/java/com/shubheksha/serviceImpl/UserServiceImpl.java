package com.shubheksha.serviceImpl;

import java.util.Date;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import com.shubheksha.config.ShubhekshaPasswordEncoder;
import com.shubheksha.dto.UserResponseDto;
import com.shubheksha.model.Role;
import com.shubheksha.model.Users;
import com.shubheksha.read.cust.repository.UserDataCustomRepository;
import com.shubheksha.read.repository.RoleRepository;
import com.shubheksha.read.repository.UsersRepository;
import com.shubheksha.service.UserService;
import com.shubheksha.utils.Constant;
import com.shubheksha.write.repository.UsersWriteRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

	@Autowired
	private UserDataCustomRepository userCustRepo;

	@Autowired
	private UsersRepository usersRepository;

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private UsersWriteRepository usersWriteRepository;

	private ShubhekshaPasswordEncoder encoder = new ShubhekshaPasswordEncoder();

	private UserResponseDto mapUsersEntityToDto(final Users user) {
		UserResponseDto userDetail = new UserResponseDto();
		userDetail.setEmail(user.getEmail());
		userDetail.setMobile(user.getMobile().toString());
		userDetail.setName(user.getName());
		userDetail.setPassword(user.getPlainPassword());
		userDetail.setRoleId(user.getRole());
		if (!ObjectUtils.isEmpty(user.getRole())) {
			Optional<Role> roleObj = roleRepository.findById(user.getRole().longValue());
			if (roleObj.isPresent()) {
				userDetail.setRoleDesc(roleObj.get().getRole());
			}
		}
		userDetail.setUserId(user.getId());
		userDetail.setUserName(user.getUsername());
		return userDetail;
	}

	@Override
	public Page<UserResponseDto> fetchAllUsers(Long userId, String userName, String email, String mobile, String name,
			Integer role, Integer pageNo, Integer pageSize, String sortParam, String sortDir) {
		log.info("Getting users based on user id : {}, userName : {}, email : {}, mobile : {}, name : {}, role : {}",
				userId, userName, email, mobile, name, role);
		Page<UserResponseDto> responseList = Page.empty();
		try {
			responseList = userCustRepo.findUserData(userId, userName, email, mobile, name, role, pageNo, pageSize,
					sortParam, sortDir);
			return responseList;
		} catch (Exception e) {
			log.error("Exception occured while fetching user data : ", e);
		}
		return null;
	}

	@Override
	public UserResponseDto getUserDetails(Long userId) {
		log.info("Getting user detail for user id : {}", userId);
		UserResponseDto userDetail = null;
		try {
			final Optional<Users> userObj = usersRepository.findById(userId);
			if (userObj.isPresent()) {
				final Users user = userObj.get();
				userDetail = mapUsersEntityToDto(user);
				return userDetail;
			} else {
				log.warn("No user found with user id : {}", userId);
			}
		} catch (Exception e) {
			log.error("Exception Occured while fetching user detail : ", e);
		}
		return userDetail;
	}

	@Override
	public UserResponseDto updateUser(UserResponseDto request) {
		UserResponseDto response = null;
		if (!ObjectUtils.isEmpty(request)) {
			if (!ObjectUtils.isEmpty(request.getUserId())) {
				Optional<Users> dbUserObj = usersRepository.findById(request.getUserId());
				if (dbUserObj.isPresent()) {
					Users dbUser = dbUserObj.get();
					if (StringUtils.isNotEmpty(request.getEmail()) && !request.getEmail().equals(dbUser.getEmail())) {
						dbUser.setEmail(request.getEmail());
					}
					if (StringUtils.isNotEmpty(request.getMobile())
							&& !request.getMobile().equals(dbUser.getMobile().toString())) {
						dbUser.setMobile(Double.valueOf(request.getMobile()));
					}
					if (StringUtils.isNoneBlank(request.getName()) && !request.getName().equals(dbUser.getName())) {
						dbUser.setName(request.getName());
					}
					if (StringUtils.isNoneBlank(request.getPassword())
							&& !request.getPassword().equals(dbUser.getPassword())) {
						dbUser.setPlainPassword(request.getPassword());
						dbUser.setPassword(encoder.encode(request.getPassword()));
					}
					if (StringUtils.isNoneBlank(request.getUserName())
							&& !request.getUserName().equals(dbUser.getUsername())) {
						dbUser.setUsername(request.getUserName());
					}
					Date currentDate = new Date();
					dbUser.setModidate(currentDate);
					usersWriteRepository.save(dbUser);
					response = request;
				} else {
					log.warn("No user found with user id - {}", request.getUserId());
				}
			} else {
				log.warn("no user id found");
			}
		} else {
			log.warn("empty requets");
		}
		return response;
	}

	@Override
	public boolean deactivateUser(Long userId) {
		if (!ObjectUtils.isEmpty(userId)) {
			Optional<Users> userObj = usersRepository.findById(userId);
			if (userObj.isPresent()) {
				if (userObj.get().getActive().equals(Constant.YES)) {
					userObj.get().setActive(Constant.NO);
					usersWriteRepository.save(userObj.get());
					log.info("user id : {} is deactivated", userId);
					return true;
				} else {
					log.warn("User is not active");
				}
			} else {
				log.warn("Invalid user id");
			}
		} else {
			log.warn("user id is blank");
		}
		return false;
	}

	@Override
	public UserResponseDto addUser(UserResponseDto user) {
		UserResponseDto response = null;
		Users dbUser = new Users();
		if (!ObjectUtils.isEmpty(user)) {
			if (StringUtils.isEmpty(user.getEmail()) || !Constant.isValidEmailId(user.getEmail())) {
				log.warn("Email id is blank or invalid");
				return null;
			} else {
				dbUser.setEmail(user.getEmail());
			}
			if (StringUtils.isEmpty(user.getMobile()) || !Constant.isValidMobileNumber(user.getMobile())) {
				log.warn("Mobile number is blank or invalid");
				return null;
			} else {
				dbUser.setMobile(Double.valueOf(user.getMobile()));
			}
			if (StringUtils.isBlank(user.getName())) {
				log.warn("Name can not be empty");
				return null;
			} else {
				dbUser.setName(user.getName());
			}
			if (StringUtils.isBlank(user.getPassword())) {
				log.warn("Password can not be empty");
				return null;
			} else {
				dbUser.setPlainPassword(user.getPassword());
				dbUser.setPassword(encoder.encode(user.getPassword()));
			}
			if (StringUtils.isBlank(user.getUserName())) {
				log.warn("username can not be empty");
				return null;
			} else {
				dbUser.setUsername(user.getUserName());
			}
			if (user.getRoleId() == null) {
				log.warn("role can not be empty");
				return null;
			} else {
				dbUser.setRole(user.getRoleId());
			}
			Date currentDate = new Date();
			dbUser.setActive(Constant.YES);
			dbUser.setCreatedate(currentDate);
			dbUser.setModidate(currentDate);
			usersWriteRepository.save(dbUser);
			response = user;
		} else {
			log.warn("empty requets");
		}
		return response;
	}
}
