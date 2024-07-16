package demo.services.commandService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Component;

import demo.boundaries.MiniAppCommandBoundary;

@Component
public class DefaultCommand implements CommandAbstraction {

	@Override
	public List<Object> invokeCommand(MiniAppCommandBoundary boundary) {
		if (boundary.getCommandAttributes() == null)
			boundary.setCommandAttributes(new HashMap<>());
		boundary.getCommandAttributes().put("result", "DefaultGame - Echoing input");
		
		List<Object> rv = new ArrayList<>();
		rv.add(boundary);
		return rv;
	}
	

}
