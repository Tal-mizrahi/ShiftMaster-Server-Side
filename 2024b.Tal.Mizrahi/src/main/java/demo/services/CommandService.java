package demo.services;


import demo.boundaries.MiniAppCommandBoundary;

public interface CommandService {
	
	public MiniAppCommandBoundary invokeACommand(String miniAppName, MiniAppCommandBoundary boundary);

}
