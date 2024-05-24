package demo.converters;

import org.springframework.stereotype.Component;

import demo.RolesEnum;
import demo.UserId;
import demo.boundaries.NewUserBoundary;
import demo.boundaries.UserBoundary;
import demo.entities.UserEntity;

@Component
public class UserConverter { // from NewUserBoundry to UserEntity
	
	
	public UserEntity toEntity(NewUserBoundary boundary) {
		
		if ( boundary.getEmail().isBlank() ||
			 boundary.getUsername().isBlank() ||
			 boundary.getRole() == null ||
			// !isValidRole(boundary.getRole())||
			 boundary.getAvatar().isBlank()
			) {
				throw new RuntimeException("You must enter all the details correct!");
			} 
		UserEntity entity = new UserEntity();		
		entity.setUsername(boundary.getUsername());
		entity.setAvatar(boundary.getAvatar());
		entity.setRole(boundary.getRole());

		return entity;

	}

	public UserBoundary toBoundary(UserEntity entity) { // from UserEntity to UserBoundry
		UserBoundary boundary = new UserBoundary();

		UserId id = new UserId();
		String[] userId = entity.getUserId().split("#");
		id.setSuperApp(userId[0]);
		id.setEmail(userId[1]);
		boundary.setUserId(id);
		boundary.setRole(entity.getRole());
		boundary.setUsername(entity.getUsername());
		boundary.setAvatar(entity.getAvatar());
		return boundary;
	}
	
	public  boolean isValidRole(RolesEnum role) {
		if(role == null)
			return false;		
		try {
			RolesEnum.valueOf(role.name());			
			return true;
		} catch(Exception e) {
			return false;
		}
	}

}
