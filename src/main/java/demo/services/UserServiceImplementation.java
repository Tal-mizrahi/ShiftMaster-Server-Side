package demo.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import demo.boundaries.NewUserBoundary;
import demo.boundaries.UserBoundary;
import demo.converters.UserConverter;
import demo.crud.UserCrud;
import demo.entities.UserEntity;
import demo.objects.InputValidation;
import demo.services.exceptions.BadRequestException;
import demo.services.exceptions.ConflictException;
import demo.services.exceptions.NotFoundException;
import demo.services.interfaces.UserService;

@Service
public class UserServiceImplementation implements UserService {


	private UserCrud userCrud;
	private UserConverter userConverter;
	private String springApplicationName;

	@Value("${spring.application.name:supperApp}")
	public void setSpringApplicationName(String springApplicationName) {
		this.springApplicationName = springApplicationName;
		System.err.println("The Spring Application name is: " + this.springApplicationName);
	}

	public UserServiceImplementation(UserCrud userCrud, UserConverter userConverter) {
		this.userCrud = userCrud;
		this.userConverter = userConverter;
	}

	@Override
	@Transactional(readOnly = false)
	public UserBoundary createNewUser(NewUserBoundary boundary) {
		
		// check if the email is not null and valid
		if (!InputValidation.isValidEmail(boundary.getEmail())) { 
			throw new BadRequestException("You must enter valid email! ");
		}
		
		if (userCrud.existsById(springApplicationName + "#" + boundary.getEmail()))
			throw new ConflictException("This user is already exists!");
			
		if(
				boundary.getUsername() == null 
				|| boundary.getUsername().isBlank()) {
			throw new BadRequestException("You must enter username!");
		} 
		
		if(
				boundary.getAvatar() == null 
				|| boundary.getAvatar().isBlank()) {
			throw new BadRequestException("You must enter avatar!");
		} 
		
		// check if the role is null and if the role is valid 
		if (
				boundary.getRole() == null 
				|| !InputValidation.isValidRole(boundary.getRole().name())) {
			throw new BadRequestException("You must enter the userRole - ADMIN, SUPERAPP_USER, MINIAPP_USER");
		}
		
		UserEntity entity = userConverter.toEntity(boundary);
		entity.setUserId(springApplicationName 
						+ "#" 
						+ boundary.getEmail());
		entity = userCrud.save(entity);
		
		System.err.println("stored in database: "+ entity);
		
		return userConverter.toBoundary(entity);
	} 

	@Override
	@Transactional(readOnly = true)
	public Optional<UserBoundary> getUser(String email, String superapp) {
		String id = superapp + "#" + email;
		Optional<UserEntity> optionalEntity = this.userCrud.findById(id);	
		return optionalEntity
				.map(this.userConverter::toBoundary);
	}

	@Override
	@Transactional(readOnly = false)
	public void updateDetails(String email, String superapp, UserBoundary update) {
		String id = superapp + "#" + email;
		UserEntity entity = this.userCrud
				.findById(id)
				.orElseThrow(() -> new NotFoundException("UserEntity with email: " + email 
						+ " and superapp " + superapp + " Does not exist in database"));

		if (update.getUsername() != null && !update.getUsername().isBlank())
			entity.setUsername(update.getUsername());
		
		if (update.getRole() != null) 
			entity.setRole(update.getRole());
			
		if (update.getAvatar() != null && !update.getAvatar().isBlank()) 
				entity.setAvatar(update.getAvatar());
		
		this.userCrud.save(entity);
	
		System.err.println("updated in database: " + entity );

	}

}
