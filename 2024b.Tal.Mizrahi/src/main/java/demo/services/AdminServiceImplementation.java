package demo.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import demo.boundaries.MiniAppCommandBoundary;
import demo.boundaries.UserBoundary;
import demo.converters.CommandConverter;
import demo.converters.UserConverter;
import demo.crud.CommandCrud;
import demo.crud.ObjectCrud;
import demo.crud.UserCrud;
import demo.entities.UserEntity;
import demo.objects.RolesEnum;

@Service
public class AdminServiceImplementation implements AdminService {

	private UserCrud userCrud;
	private UserConverter userConverter;
	private ObjectCrud objectCrud;
	private CommandCrud commandCrud;
	private CommandConverter commandConverter;
	
	private String springApplicationName;

	@Value("${spring.application.name:supperApp}")
	public void setSpringApplicationName(String springApplicationName) {
		this.springApplicationName = springApplicationName;
		System.err.println("The Spring Application name is: " + this.springApplicationName);
	} 
	

	public AdminServiceImplementation(UserCrud userCrud, UserConverter userConverter, ObjectCrud objectCrud,
			CommandCrud commandCrud, CommandConverter commandConverter) {
	
		this.userCrud = userCrud;
		this.userConverter = userConverter;
		this.objectCrud = objectCrud;
		this.commandCrud = commandCrud;
		this.commandConverter = commandConverter;
	}

	@Override
	@Transactional(readOnly = false)
	public void deleteAllUsers(String userSuperapp, String email) {
		
		checkAdminPermission(userSuperapp, email);
		this.userCrud.deleteAll();
		System.err.println("All user entries Deleted");
		
	}

	@Override
	@Transactional(readOnly = false)
	public void deleteAllObjects(String userSuperapp, String email) {
		checkAdminPermission(userSuperapp, email);
		this.objectCrud.deleteAll();
		System.err.println("All object entries Deleted");
		
	}

	@Override
	@Transactional(readOnly = false)
	public void deleteAllCommandsHistory(String userSuperapp, String email) {
		checkAdminPermission(userSuperapp, email);
		this.commandCrud.deleteAll();
		System.err.println("All commands entries Deleted");
		
	}

	@Override
	@Transactional(readOnly = true)
	public List<UserBoundary> getAllUsers(String userSuperapp, String email, int size, int page) {
		checkAdminPermission(userSuperapp, email);
		return this.userCrud.findAll(PageRequest.of(page, size, Direction.ASC, "role", "userId")) // List<UserEntity>
				.stream() // Stream<DemoEntity>
				.peek(entity -> System.err.println("* " + entity)) // Prints all items.
				.map(userConverter::toBoundary) // Stream<userBoundary>
				.toList(); // List<UserBoundary>
	}

	@Override
	@Transactional(readOnly = true)
	public List<MiniAppCommandBoundary> getAllCommands(String userSuperapp, String email, int size, int page) {
		checkAdminPermission(userSuperapp, email);
		return this.commandCrud.findAll(PageRequest.of(page, size, Direction.DESC, "commandId", "invocationTimesTamp")) // List<UserEntity>
				.stream() // Stream<DemoEntity>
				.peek(entity -> System.err.println("* " + entity)) // Prints all items.
				.map(this.commandConverter::toBoundary) // Stream<CommandBoundary>
				.toList(); // List<CommandBoundary>
	}

	@Override
	@Transactional(readOnly = true)
	public List<MiniAppCommandBoundary> getCommandsOfSpecificMiniApp(String miniAppName, String userSuperapp, String email, int page, int size) {
		checkAdminPermission(userSuperapp, email);
		return commandCrud
				.findAllByMiniAppName(miniAppName, PageRequest.of(page, size, Direction.ASC, "command"))
				.stream()
				.map(commandConverter::toBoundary)
				.peek(System.err :: println)
				.toList();
	}
	
	public void checkAdminPermission(String userSuperapp, String email) {
		String id = userSuperapp + "#" + email;
		UserEntity entity = this.userCrud
				.findById(id)
				.orElseThrow(() -> new NotFoundException("UserEntity with email: " + email 
						+ " and superapp " + userSuperapp + " Does not exist in database"));
		if (!entity.getRole().equals(RolesEnum.ADMIN))
			throw new ForbiddenException("UserEntity with email: " + email 
						+ " and superapp " + userSuperapp + " not have the permission");
	}

}
