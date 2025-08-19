package com.app.service;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.app.custom_exceptions.ApiException;
import com.app.custom_exceptions.InvalidInputException;
import com.app.custom_exceptions.ResourceAlreadyExistsException;
import com.app.custom_exceptions.ResourceNotFoundException;
import com.app.dao.PropertyAddressDao;
import com.app.dao.PropertyBlockoutDao;
import com.app.dao.PropertyDao;
import com.app.dao.PropertyImageDao;
import com.app.dao.UserDao;
import com.app.dto.ApiResponse;
import com.app.dto.SignUpDto;
import com.app.dto.UserRespDto;
import com.app.entities.Property;
import com.app.entities.PropertyBlockout;
import com.app.entities.PropertyImage;
import com.app.entities.User;
import com.app.entities.UserRole;

import lombok.AllArgsConstructor;

@Service
@Transactional
@AllArgsConstructor
public class UserServiceImpl implements UserService {
	
	private final UserDao userDao;
	private final ModelMapper modelMapper;
	private final PasswordEncoder passwordEncoder;
	private final PropertyDao propertyDao;
	private final PropertyImageDao propertyImageDao;
	private final PropertyBlockoutDao propertyBlockoutDao;
	private final PropertyAddressDao propertyAddressDao;
	private final OtpService otpService;

	@Override
	public UserRespDto signUp(SignUpDto dto) {
		
		//check for email duplication
		if(userDao.existsByEmail(dto.getEmail())) {
			throw new ResourceAlreadyExistsException("email Already exists");
		}
		
		//else  dto--->entity
		User user=modelMapper.map(dto, User.class);
		
		//encrypt password
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		
		// Save user with pending verification
        User savedUser = userDao.save(user);
        
        
     // Generate and send OTP
        otpService.generateAndSendOtp(dto.getEmail());
        
        
		//save entity and map persistent entity to respdto
		return modelMapper.map(savedUser, UserRespDto.class);
	}

	@Override
	public List<UserRespDto> getAllUsers() {
		// TODO Auto-generated method stub
		
		validateAdmin();
		List<User> users=userDao.findAll();// Retrieve all users, including soft-deleted
		
		return users.stream()
				.map(user->modelMapper.map(user, UserRespDto.class))
				.collect(Collectors.toList());
	}

	private User validateAdmin() {
		// TODO Auto-generated method stub
		String email = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        User admin = userDao.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + email));
        if (!admin.getRole().name().equals("ROLE_ADMIN")) {
            throw new InvalidInputException("Only admin can perform this operation");
        }
        return admin;
		
	}

	@Override
	public ApiResponse deleteUser(Long userId) {
		// TODO Auto-generated method stub
		validateAdmin();
		User user = userDao.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
        
		
		if (!user.isEnabled()) {
            throw new InvalidInputException("User is already soft-deleted");
        }
		
		//soft delete user
        user.setStatus(false);
        
        userDao.save(user);
        
        List<Property> properties= propertyDao.findByOwner(user);
        
        for(Property property:properties) {
        	if(property.isStatus()) {
        		property.setStatus(false);
        		propertyDao.save(property);
        		
        		// Soft-delete PropertyImage
        		 List<PropertyImage> images = propertyImageDao.findByProperty(property);
                 for (PropertyImage image : images) {
                     if (image.isImageStatus()) {
                         image.setImageStatus(false);
                         propertyImageDao.save(image);
                     }
                 }
                 
                 //Soft-delete PropertyBlockout
                 
                 List<PropertyBlockout> blockouts = propertyBlockoutDao.findByProperty(property);
                 for (PropertyBlockout blockout : blockouts) {
                     if (blockout.isStatus()) {
                         blockout.setStatus(false);
                         propertyBlockoutDao.save(blockout);
                     }
                 }
                 
                 // Soft-delete PropertyAddress
                 propertyAddressDao.findByProperty(property).ifPresent(address -> {
                     if (address.isStatus()) {
                         address.setStatus(false);
                         propertyAddressDao.save(address);
                     }
                 });
        	}
        	
        }
        
		return new ApiResponse("User, their properties, and associated images, blockouts, and addresses soft-deleted successfully with id"+userId);
	}

	
	
	
	// New method to verify OTP and activate user
    public ApiResponse verifyUserOtp(String email, String otpValue) {
        User user = userDao.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + email));
        if (!user.isPendingVerification()) {
            throw new InvalidInputException("User is already verified");
        }

        // Verify OTP
        ApiResponse otpResponse = otpService.verifyOtp(email, otpValue);

        // Activate user
        user.setPendingVerification(false);
        if (!user.isStatus()) {
            user.setStatus(true); // ensure user is enabled
        }
        userDao.save(user);


        return new ApiResponse("User verified successfully");
    }
    
    
    @Override
    public ApiResponse transferAdminRole(Long newAdminId) {
        // Validate that the caller is an admin
        User currentAdmin = validateAdmin();

        // Find the new admin user
        User newAdmin = userDao.findById(newAdminId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + newAdminId));

        // Prevent transferring to the same user or an already admin user
        if (newAdmin.getId().equals(currentAdmin.getId())) {
            throw new InvalidInputException("Cannot transfer admin role to the same user");
        }
        if (newAdmin.getRole() == UserRole.ROLE_ADMIN) {
            throw new InvalidInputException("User is already an admin");
        }
        
        if (newAdmin.isStatus() == false) {
            throw new InvalidInputException("User might be deleted");
        }
        
        
        // Ensure new admin is active and verified
        if (!newAdmin.isEnabled() || newAdmin.isPendingVerification()) {
            throw new InvalidInputException("New admin must be an active, verified user");
        }

        // Transfer admin role
        currentAdmin.setRole(UserRole.ROLE_CUSTOMER); // Demote current admin
        newAdmin.setRole(UserRole.ROLE_ADMIN); // Promote new user to admin
        userDao.save(currentAdmin);
        userDao.save(newAdmin);

        return new ApiResponse("Admin role transferred successfully to user with id: " + newAdminId);
    }
    
}
