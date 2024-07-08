package demo.services.commandService;

import java.util.List;

import demo.boundaries.MiniAppCommandBoundary;

public interface CommandAbstraction {
	
	public List<Object> invokeCommand(MiniAppCommandBoundary boundary);

}
