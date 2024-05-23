package demo.services;

import java.util.Optional;

import demo.boundaries.NewUserBoundary;
import demo.boundaries.UserBoundary;

public interface UserService {

	public UserBoundary createNewUser(NewUserBoundary newUserBoundary);
	
	public Optional<UserBoundary> getUserByEmail(String email);
	
	public void updateDetailsByEmail (UserBoundary userBoundary);
}
