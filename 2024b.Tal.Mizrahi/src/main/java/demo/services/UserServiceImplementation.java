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
		
		if (!optionalEntity.isEmpty()) {
			System.err.println("Read from Database: " + optionalEntity.get());
		}else {
			System.err.println("UserEntity with email: " + email 
					+ " and superapp " + superapp + " Does not exist in database");
		}
		
		return optionalEntity
				.map(this.userConverter::toBoundary);
	}

	@Override
	public void updateDetails(String email, String superapp, UserBoundary update) {
		String id = superapp + "#" + email;
		UserEntity entity = this.userCrud
				.findById(id)
				.orElseThrow(() -> new RuntimeException("UserEntity with email: " + email 
						+ " and superapp " + superapp + " Does not exist in database"));

		if (update.getUsername() != null)
			entity.setUsername(update.getUsername());
		
		if (update.getRole() != null) 
			entity.setRole(update.getRole());
			
		if (update.getAvatar() != null) 
				entity.setAvatar(update.getAvatar());
		
		this.userCrud.save(entity);
	
		System.err.println("updated in database: " + entity );

	}

}
