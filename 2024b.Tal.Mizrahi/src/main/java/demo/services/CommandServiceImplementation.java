package demo.services;

import java.util.Date;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
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
import demo.objects.RolesEnum;
import demo.objects.UserId;

@Service
public class CommandServiceImplementation implements CommandService {
	private CommandCrud commandCrud;
	private CommandConverter commandConverter;
	private UserCrud userCrud;
    private  ObjectCrud objectCrud;
	private String springApplicationName;

	@Value("${spring.application.name:supperApp}")
	public void setSpringApplicationName(String springApplicationName) {
		this.springApplicationName = springApplicationName;
		System.err.println("The Spring Application name is: " + this.springApplicationName);
	}

	public CommandServiceImplementation(CommandCrud commandCrud, CommandConverter commandConverter, UserCrud userCrud, ObjectCrud objectCrud) {
		this.commandCrud = commandCrud;
		this.commandConverter = commandConverter;
		this.userCrud = userCrud;
		this.objectCrud = objectCrud;
	}

	@Override
	@Transactional(readOnly = false)
	public MiniAppCommandBoundary invokeACommand(String miniAppName, MiniAppCommandBoundary boundary) {
		
		if (boundary.getInvokedBy() == null || boundary.getInvokedBy().getUserId() == null
				|| boundary.getInvokedBy().getUserId().getSuperApp() == null
				|| boundary.getInvokedBy().getUserId().getEmail() == null) {
			throw new BadInputException(
					"You must enter who invoked the command by " + "giving the superapp name and valid email!");
		}
		
		checkPermission(boundary.getInvokedBy().getUserId());
		
		String objId = boundary.getTargetObject().getObjectId().getId();
		String objSupApp= boundary.getTargetObject().getObjectId().getSuperApp();
		
		if (boundary.getTargetObject() == null 
				|| boundary.getTargetObject().getObjectId() == null
				|| objId == null
				|| objSupApp == null) {
			throw new BadInputException("You must enter target object!");
		}
		
		objectCrud.findByObjectIdAndActive(objSupApp+ "#" + objId, true)
		.orElseThrow(()->new NotFoundException("ObjectEntity with id: " 
				+ objId 
				+ " and superapp name: " + objSupApp + " Does not exist in database"));
		
		boundary.setInvocationTimesTamp(new Date());
		boundary.setCommandId(
				new CommandId(
						springApplicationName
						, miniAppName
						, UUID.randomUUID().toString()));
		if (boundary.getCommand() == null 
				|| boundary.getCommand().isBlank()) {
			throw new BadInputException("You must enter command");
		}
	
		CommandEntity entity = commandConverter.toEntity(boundary);
		

		entity = commandCrud.save(entity);
		System.err.println("Saved in DB the object: " + entity);
		return commandConverter.toBoundary(entity);
	}
	
	public void checkPermission(UserId userId) {
		String id = userId.getSuperApp() 
				+ "#" 
				+ userId.getEmail();
		UserEntity entity = this.userCrud
				.findById(id)
				.orElseThrow(() -> new NotFoundException("UserEntity with email: " + userId.getEmail() 
						+ " and superapp " + userId.getSuperApp() + " Does not exist in database"));
		if (!entity.getRole().equals(RolesEnum.MINIAPP_USER))
			throw new ForbiddenException("UserEntity with email: " + userId.getEmail() 
						+ " and superapp " + userId.getSuperApp() + " not have the permission");
	}

}
