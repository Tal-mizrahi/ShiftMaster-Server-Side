package demo.services.interfaces;

import java.util.List;

import demo.boundaries.MiniAppCommandBoundary;
import demo.boundaries.UserBoundary;

public interface AdminService {

	@Deprecated
	public void deleteAllUsers();
	
	@Deprecated
	public void deleteAllObjects();
	
	@Deprecated
	public void deleteAllCommandsHistory();
	
	@Deprecated
	public List<UserBoundary> getAllUsers();
	
	@Deprecated
	public List<MiniAppCommandBoundary> getAllCommands();
	
	@Deprecated
	public List<MiniAppCommandBoundary> getCommandsOfSpecificMiniApp(String miniAppName);
}
 