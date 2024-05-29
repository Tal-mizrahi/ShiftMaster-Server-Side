package demo.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import demo.boundaries.MiniAppCommandBoundary;
import demo.boundaries.UserBoundary;
import demo.converters.CommandConverter;
import demo.converters.ObjectConverter;
import demo.converters.UserConverter;
import demo.crud.CommandCrud;
import demo.crud.ObjectCrud;
import demo.crud.UserCrud;

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
	public void deleteAllUsers() {
		this.userCrud.deleteAll();
		System.err.println("All user entries Deleted");
		
	}

	@Override
	@Transactional(readOnly = false)
	public void deleteAllObjects() {
		this.objectCrud.deleteAll();
		System.err.println("All object entries Deleted");
		
	}

	@Override
	@Transactional(readOnly = false)
	public void deleteAllCommandsHistory() {
		this.commandCrud.deleteAll();
		System.err.println("All commands entries Deleted");
		
	}

	@Override
	@Transactional(readOnly = true)
	public List<UserBoundary> getAllUsers() {
		return this.userCrud.findAll() // List<UserEntity>
				.stream() // Stream<DemoEntity>
				.peek(entity -> System.err.println("* " + entity)) // Prints all items.
				.map(userConverter::toBoundary) // Stream<userBoundary>
				.toList(); // List<UserBoundary>
	}

	@Override
	@Transactional(readOnly = true)
	public List<MiniAppCommandBoundary> getAllCommands() {
		return this.commandCrud.findAll() // List<UserEntity>
				.stream() // Stream<DemoEntity>
				.peek(entity -> System.err.println("* " + entity)) // Prints all items.
				.map(this.commandConverter::toBoundary) // Stream<CommandBoundary>
				.toList(); // List<CommandBoundary>
	}

	@Override
	@Transactional(readOnly = true)
	public List<MiniAppCommandBoundary> getCommandsOfSpecificMiniApp(String miniAppName) {
		return commandCrud
				.findAllByMiniAppName(miniAppName)
				.stream()
				.map(commandConverter::toBoundary)
				.peek(System.err :: println)
				.toList();
	}

}
