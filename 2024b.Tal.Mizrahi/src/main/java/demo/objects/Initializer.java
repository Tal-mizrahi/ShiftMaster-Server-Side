package demo.objects;

import java.util.stream.Stream;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import demo.boundaries.*;
import demo.services.interfaces.AdminService;
import demo.services.interfaces.CommandService;
import demo.services.interfaces.EnhancedAdminService;
import demo.services.interfaces.EnhancedObjectService;
import demo.services.interfaces.UserService;

@Component
@Profile("manualTests")
public class Initializer implements CommandLineRunner {
	private EnhancedAdminService adminService;
	private CommandService commandSerivse;
	private EnhancedObjectService objectService;
	private UserService userService;


	public Initializer(EnhancedAdminService adminService, CommandService commandSerivse, EnhancedObjectService objectService,
			UserService userService) {
		this.adminService = adminService;
		this.commandSerivse = commandSerivse;
		this.objectService = objectService;
		this.userService = userService;
	}

	@Override
	public void run(String... args) throws Exception {
		
		// Create admin user to delete all existing DB
		createNewUserBoundary("super user", "su", RolesEnum.ADMIN, "suAdmin");
		adminService.deleteAllCommandsHistory("2024b.Tal.Mizrahi", "suAdmin@gmail.com");
		adminService.deleteAllObjects("2024b.Tal.Mizrahi", "suAdmin@gmail.com");
		adminService.deleteAllUsers("2024b.Tal.Mizrahi", "suAdmin@gmail.com");
		 

		System.err.println("Creating users:");
		createNewUserBoundary("Tal Miz", "TM", RolesEnum.SUPERAPP_USER, "superapp");
		createNewUserBoundary("John Doe", "JD", RolesEnum.ADMIN, "admin");
		createNewUserBoundary("Jane Smith", "JS", RolesEnum.MINIAPP_USER, "miniapp");
		
		System.err.println("Creating Objects:");
		storeObjectInDatabase("schedual", true, "enter", "sched");
		storeObjectInDatabase("schedual", false, "enter", "sched");
		storeObjectInDatabase("task", true, "enter", "taskMen");
		storeObjectInDatabase("task", false, "enter", "taskMen");

	}

	private void createNewUserBoundary(String username, String avatar, RolesEnum role, String emailInitial) {
		NewUserBoundary user = new NewUserBoundary();
		user.setUsername(username);
		user.setEmail(emailInitial + "@gmail.com");
		user.setAvatar(avatar);
		user.setRole(role);
		userService.createNewUser(user);
	}
	
	private void storeObjectInDatabase(String type, boolean active, String... alias) {
		Stream.of(alias).map(theAlias -> createNewObject(type, active, theAlias))
				.map(newBoundary -> this.objectService.createObject(newBoundary))
				.forEach(System.err::println);
	}

	private ObjectBoundary createNewObject(String type, boolean active, String alias ) {
		ObjectBoundary rv = new ObjectBoundary();
		rv.setCreatedBy(new CreatedBy(new UserId("2024b.Tal.Mizrahi", "superapp@gmail.com")));
		rv.setActive(active);
		rv.setAlias(alias);
		rv.setType(type);
		return rv;
	}

}
/*
 * Stream.of("Tal Miz").map(userName, -> { NewUserBoundary user = new
 * NewUserBoundary(); user.setUsername(userName); user.setEmail(userName +
 * "@gmail.com"); user.setAvatar("TM"); user.setRole(RolesEnum.SUPERAPP_USER);
 * 
 * version += 0.5;
 * 
 * return user;
 * }).map(this.userService::createNewUser).forEach(System.err::println);
 */

/*
 * version = 1.0; System.err.println("Creating objects:  ");
 * 
 * Stream.of("").map(userName -> { NewUserBoundary user = new NewUserBoundary();
 * user.setUsername(userName); user.setEmail(userName + "@gmail.com");
 * user.setAvatar("TM"); user.setRole(RolesEnum.ADMIN);
 * 
 * version += 0.5;
 * 
 * return user;
 * }).map(this.userService::createNewUser).forEach(System.err::println);
 * 
 * version = 1.0; System.err.println("Creating objects:  "); }
 */

/*
 * version = 1.0; System.err.println("Creating people woth last name: Smith");
 * Stream.of("Jane", "Jill", "John", "Joanna", "James", "Jack") .map(firstName
 * -> { DemoBoundary b = new DemoBoundary("Message by " + firstName + " Smith");
 * b.setName(new NameBoundary(firstName, "Smith")); b.setIsImportant(true);
 * b.setStatus(StatusEnum.ACTIVE); b.setVersion(version);
 * b.setDetails(Collections.singletonMap("expected", true)); version += 0.5;
 * return b; }).map(this.demoService::create).forEach(System.err::println);
 */