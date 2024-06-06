package demo.services;

import java.util.List;

import demo.boundaries.MiniAppCommandBoundary;
import demo.boundaries.UserBoundary;

public interface AdminService {

	public void deleteAllUsers(String userSuperapp, String email);
	
	public void deleteAllObjects(String userSuperapp, String email);
	
	public void deleteAllCommandsHistory(String userSuperapp, String email);
	
	public List<UserBoundary> getAllUsers(String userSuperapp, String email, int size, int page);
	
	public List<MiniAppCommandBoundary> getAllCommands(String userSuperapp, String email, int size, int page);
	
	public List<MiniAppCommandBoundary> getCommandsOfSpecificMiniApp(String miniAppName, String userSuperapp, String email, int size, int page);
}
 