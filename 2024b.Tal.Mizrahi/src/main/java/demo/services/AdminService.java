package demo.services;

import java.util.List;

import demo.boundaries.MiniAppCommandBoundary;
import demo.boundaries.UserBoundary;

public interface AdminService {

	public void deleteAllUsers();
	
	public void deleteAllObjects();
	
	public void deleteAllCommandsHistory();
	
	public List<UserBoundary> getAllUsers();
	
	public List<MiniAppCommandBoundary> getAllCommands();
	
	public List<MiniAppCommandBoundary> getCommandsOfSpecificMiniApp(String miniAppName);
}
 