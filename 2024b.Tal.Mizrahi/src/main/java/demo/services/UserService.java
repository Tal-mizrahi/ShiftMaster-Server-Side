package demo.services;

import java.util.Optional;

import demo.boundaries.NewUserBoundary;
import demo.boundaries.UserBoundary;

public interface UserService {

	public UserBoundary createNewUser(NewUserBoundary boundary);
	
	public Optional<UserBoundary> getUser(String email, String superapp);
	
	public void updateDetails (String email, String superapp, UserBoundary update);
}
