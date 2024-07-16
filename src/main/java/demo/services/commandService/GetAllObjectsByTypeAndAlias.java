package demo.services.commandService;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;

import demo.boundaries.MiniAppCommandBoundary;
import demo.converters.ObjectConverter;
import demo.crud.ObjectCrud;
import demo.services.interfaces.EnhancedObjectService;

@Component("getAllObjectsByTypeAndAlias")
public class GetAllObjectsByTypeAndAlias implements CommandAbstraction {

	private ObjectCrud objectCrud;
	private ObjectConverter objectConverter;
	private final Log logger = LogFactory.getLog(GetAllObjectsByUserTypeAnsAlias.class);

	public GetAllObjectsByTypeAndAlias(EnhancedObjectService objectService, ObjectCrud objectCrud,
			ObjectConverter objectConverter) {
		this.objectConverter = objectConverter;
		this.objectCrud = objectCrud;
	}

	@Override
	public List<Object> invokeCommand(MiniAppCommandBoundary boundary) {
		String type, alias;
		List<Object> rv = new ArrayList<Object>();
		try {
			type = boundary.getCommandAttributes().get("type").toString();
			alias = boundary.getCommandAttributes().get("alias").toString();
		} catch (Exception exception) {
			boundary.getCommandAttributes().put("notice", "please provide details.type and details.alias");
			rv.add(boundary);
			return rv;
		}
		rv.addAll(objectCrud.findAllByTypeAndAliasAndActiveTrue(type, alias)
				.stream()
				.map(this.objectConverter::toBoundary).toList());
		return rv;
	}
}
