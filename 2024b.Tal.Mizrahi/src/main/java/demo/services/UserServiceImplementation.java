package demo.services;


import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import demo.UserId;
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

	public UserServiceImplementation(
			UserCrud userCrud,
			UserConverter userConverter) {
		this.userCrud = userCrud;
		this.userConverter = userConverter;
	}
	
	@Override
	@Transactional(readOnly = false)
	public UserBoundary createNewUser(NewUserBoundary newUserBoundary) {
		UserEntity userBoundary = new UserEntity();
		
		// Create UserId and setUserId
		String id = newUserBoundary.getEmail() + "#" + springApplicationName;
		userId.setEmail(newUserBoundary.getEmail());
		userId.setSuperApp(springApplicationName);
		userBoundary.setUserId(userId);
		
		// Set role, username, avatar
		userBoundary.setRole(newUserBoundary.getRole());
		userBoundary.setUsername(newUserBoundary.getUsername());
		userBoundary.setAvatar(newUserBoundary.getAvatar());
		
		return userBoundary;
	}

	@Override
	public Optional<UserBoundary> getUserByEmail(String email) {
		// TODO Auto-generated method stub
		return Optional.empty();
	}

	@Override
	public void updateDetailsByEmail(UserBoundary userBoundary) {
		// TODO Auto-generated method stub
		
	}


}
