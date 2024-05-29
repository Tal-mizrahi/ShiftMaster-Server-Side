package demo.services;

import java.util.Date;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import demo.boundaries.MiniAppCommandBoundary;
import demo.converters.CommandConverter;
import demo.crud.CommandCrud;
import demo.entities.CommandEntity;
import demo.objects.CommandId;

@Service
public class CommandServiceImplementation implements CommandService {
	private CommandCrud commandCrud;
	private CommandConverter commandConverter;
	private String springApplicationName;

	@Value("${spring.application.name:supperApp}")
	public void setSpringApplicationName(String springApplicationName) {
		this.springApplicationName = springApplicationName;
		System.err.println("The Spring Application name is: " + this.springApplicationName);
	}

	public CommandServiceImplementation(CommandCrud commandCrud, CommandConverter commandConverter) {
		this.commandCrud = commandCrud;
		this.commandConverter = commandConverter;
	}
 
	@Override
	@Transactional(readOnly = false)
	public MiniAppCommandBoundary invokeACommand(String miniAppName, MiniAppCommandBoundary boundary) {
		boundary.setInvocationTimesTamp(new Date());
		boundary.setCommandId(new CommandId(springApplicationName
				, miniAppName
				, UUID.randomUUID().toString()));
		if (boundary.getCommand() == null) {
			throw new BadInputException("You must enter command"); 
		}
		if (
				boundary.getTargetObject() == null
				|| boundary.getTargetObject().getObjectId() == null
				|| boundary.getTargetObject().getObjectId().getId() == null
				|| boundary.getTargetObject().getObjectId().getSuperApp() == null
				) {
			throw new BadInputException("You must enter target object!");
		}
		if (
				boundary.getInvokedBy() == null
				|| boundary.getInvokedBy().getUserId() == null
				|| boundary.getInvokedBy().getUserId().getSuperApp() == null
				|| boundary.getInvokedBy().getUserId().getSuperApp() == null
				) {
			throw new BadInputException("You must enter who invoked the command!");
		}
		
		CommandEntity entity = commandConverter.toEntity(boundary);
		entity = commandCrud.save(entity);
		System.err.println("Saved in DB the object: " + entity);
		return commandConverter.toBoundary(entity);
	}
	

}
