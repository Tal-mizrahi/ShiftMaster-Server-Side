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
		if (boundary.getCommandAttribute() == null)
			boundary.setCommandAttribute(new HashMap<>());
		boundary.getCommandAttribute().put("result", "DefaultGame - Echoing input");
		
		List<Object> rv = new ArrayList<>();
		rv.add(boundary);
		return rv;
	}
	

}
