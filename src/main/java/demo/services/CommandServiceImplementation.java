package demo.services;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import demo.boundaries.MiniAppCommandBoundary;
import demo.converters.CommandConverter;
import demo.crud.CommandCrud;
import demo.crud.ObjectCrud;
import demo.crud.UserCrud;
import demo.entities.CommandEntity;
import demo.entities.UserEntity;
import demo.objects.CommandId;
import demo.objects.InputValidation;
import demo.objects.RolesEnum;
import demo.objects.UserId;
import demo.services.commandService.CommandAbstraction;
import demo.services.commandService.DefaultCommand;
import demo.services.exceptions.BadRequestException;
import demo.services.exceptions.ForbiddenException;
import demo.services.exceptions.NotFoundException;
import demo.services.interfaces.CommandService;

@Service
public class CommandServiceImplementation implements CommandService {
	private CommandCrud commandCrud;
	private CommandConverter commandConverter;
	private UserCrud userCrud;
    private  ObjectCrud objectCrud;
	private String springApplicationName;
	private ApplicationContext applicationContext;
	private DefaultCommand defaultCommand;
	private Log logger = LogFactory.getLog(CommandServiceImplementation.class);
	
	
	
	@Value("${spring.application.name:supperApp}")
	public void setSpringApplicationName(String springApplicationName) {
		this.springApplicationName = springApplicationName;
		System.err.println("The Spring Application name is: " + this.springApplicationName);
	}

	public CommandServiceImplementation(CommandCrud commandCrud, CommandConverter commandConverter, UserCrud userCrud, ObjectCrud objectCrud
			,ApplicationContext applicationContext
			,DefaultCommand defaultCommand) {
		
		this.defaultCommand = defaultCommand;
		this.applicationContext = applicationContext;
		this.commandCrud = commandCrud;
		this.commandConverter = commandConverter;
		this.userCrud = userCrud;
		this.objectCrud = objectCrud;
	}

	@Override
	@Transactional(readOnly = false)
	public List<Object> invokeACommand(String miniAppName, MiniAppCommandBoundary boundary) {
		
		InputValidation.checkIfValidMiniappInput(boundary);
		
		checkUserPermission(boundary.getInvokedBy().getUserId());
		
		String objId = boundary.getTargetObject().getObjectId().getId();
		String objSupApp= boundary.getTargetObject().getObjectId().getSuperapp();
		
		
		objectCrud.findByObjectIdAndActiveTrue(objSupApp+ "#" + objId)
		.orElseThrow(()->new NotFoundException("ObjectEntity with id: " 
				+ objId 
				+ " and superapp name: " + objSupApp + " Does not exist in database"));
		
		boundary.setInvocationTimestamp(new Date());
		boundary.setCommandId(
				new CommandId(
						springApplicationName
						, miniAppName
						, UUID.randomUUID().toString()));
		
		
		CommandEntity entity = commandConverter.toEntity(boundary);
		
		entity = commandCrud.save(entity);
		System.err.println("Saved in DB the object: " + entity);
		
		
		return handleCommand(entity.getCommand(), boundary);
	}
	
	public List<Object> handleCommand(String command, MiniAppCommandBoundary input) {
	
		this.logger.debug("** Invoke a command: "+ command);
		CommandAbstraction rv = null;
		
		try {
			rv = this.applicationContext.getBean(command,CommandAbstraction.class);
		} catch (Exception e) {
			this.logger.warn("could not find game implementation: " + command + " - using default game!");
			rv = this.defaultCommand;
		}
		this.logger.trace("** command implementation: " + rv.getClass().getSimpleName() );
		
		return rv.invokeCommand(input);
	}
	
	public void checkUserPermission(UserId userId) {
		String id = userId.getSuperapp() 
				+ "#" 
				+ userId.getEmail();
		UserEntity entity = this.userCrud
				.findById(id)
				.orElseThrow(() -> new NotFoundException("UserEntity with email: " + userId.getEmail() 
						+ " and superapp " + userId.getSuperapp() + " Does not exist in database"));
		if (!entity.getRole().equals(RolesEnum.MINIAPP_USER))
			throw new ForbiddenException("UserEntity with email: " + userId.getEmail() 
						+ " and superapp " + userId.getSuperapp() + " dont have MINIAPP_USER permission");
	}
	
	
	
	
	

}
