package demo.converters;

import org.springframework.stereotype.Component;

import demo.boundaries.NewUserBoundary;
import demo.boundaries.UserBoundary;
import demo.entities.UserEntity;
import demo.objects.UserId;

@Component
public class UserConverter { // from NewUserBoundry to UserEntity
	
	
	public UserEntity toEntity(NewUserBoundary boundary) { //
	
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
		id.setSuperapp(userId[0]);
		id.setEmail(userId[1]);
		boundary.setUserId(id);
		boundary.setRole(entity.getRole());
		boundary.setUsername(entity.getUsername());
		boundary.setAvatar(entity.getAvatar());
		return boundary;
	}
	
}
