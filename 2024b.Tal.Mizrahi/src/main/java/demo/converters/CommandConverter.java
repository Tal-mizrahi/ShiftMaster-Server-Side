package demo.converters;

import java.util.HashMap;

import org.springframework.stereotype.Component;

import demo.boundaries.MiniAppCommandBoundary;
import demo.entities.CommandEntity;
import demo.objects.CommandId;
import demo.objects.InvokedBy;
import demo.objects.ObjectId;
import demo.objects.TargetObject;
import demo.objects.UserId;

@Component
public class CommandConverter {

	public CommandEntity toEntity(MiniAppCommandBoundary boundary) {
		CommandEntity entity = new CommandEntity();
		entity.setCommandId(
				boundary.getCommandId().getSuperApp()
				+ "#"
				+ boundary.getCommandId().getMiniApp()
				+ "#"
				+ boundary.getCommandId().getId() );
		
		entity.setMiniAppName(boundary.getCommandId().getMiniApp());
		entity.setInvocationTimesTamp(boundary.getInvocationTimesTamp());
		entity.setCommand(boundary.getCommand());
		entity.setInvokedBy(
				boundary.getInvokedBy().getUserId().getSuperApp()
				+ "#"
				+ boundary.getInvokedBy().getUserId().getEmail());
		if (boundary.getCommandAttribute() != null)
			entity.setCommandAttributes(boundary.getCommandAttribute());
		else 
			entity.setCommandAttributes(new HashMap<>());
		entity.setTargetObject(
				boundary.getTargetObject().getObjectId().getSuperApp()
				+ "#"
				+ boundary.getTargetObject().getObjectId().getId() );
		return entity;
	} 
	
	public MiniAppCommandBoundary toBoundary(CommandEntity entity) {
		MiniAppCommandBoundary boundary = new MiniAppCommandBoundary();
		String[] commandId = entity.getCommandId().split("#");
		boundary.setCommandId(new CommandId(
				  commandId[0]
				, commandId[1]
				, commandId[2]));
		boundary.setCommand(entity.getCommand());
		boundary.setCommandAttribute(entity.getCommandAttributes());
		boundary.setInvocationTimesTamp(entity.getInvocationTimesTamp());
		String[] invokedBy = entity.getInvokedBy().split("#");
		boundary.setInvokedBy(
				new InvokedBy(
						new UserId(invokedBy[0], invokedBy[1])));
		String[] targetObject = entity.getTargetObject().split("#");
		boundary.setTargetObject(
				new TargetObject(
						new ObjectId(targetObject[0], targetObject[1])));
		
		
		return boundary;
	}
	
	
}
