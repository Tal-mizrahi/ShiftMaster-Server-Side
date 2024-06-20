package demo.services.interfaces;


import java.util.List;

import demo.boundaries.MiniAppCommandBoundary;

public interface CommandService {
	
	public List<Object> invokeACommand(String miniAppName, MiniAppCommandBoundary boundary);

}
 