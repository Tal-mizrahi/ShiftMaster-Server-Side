package demo.objects;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import demo.boundaries.*;
import demo.services.interfaces.CommandService;
import demo.services.interfaces.EnhancedAdminService;
import demo.services.interfaces.EnhancedObjectService;
import demo.services.interfaces.UserService;

@Component
@Profile("manualTests")
public class Initializer implements CommandLineRunner {
	private EnhancedAdminService adminService;
	private CommandService commandSerivce;
	private EnhancedObjectService objectService;
	private UserService userService;

	public Initializer(EnhancedAdminService adminService, CommandService commandSerivce,
			EnhancedObjectService objectService, UserService userService) {
		this.adminService = adminService;
		this.commandSerivce = commandSerivce;
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

		Location herzeliyaBeachLoc = new Location(32.158687, 34.795102); 
		Location eilatCenteralLoc = new Location(29.555803, 34.952435); 

		System.err.println("Creating Objects:");
		ObjectBoundary obj1 = storeObjectInDatabase("schedual", true, herzeliyaBeachLoc, "enter");
		ObjectBoundary obj2 = storeObjectInDatabase("schedual", true, eilatCenteralLoc, "sched");
		ObjectBoundary obj3 = storeObjectInDatabase("schedual", false, null, "enter");
		ObjectBoundary obj4 = storeObjectInDatabase("schedual", false, herzeliyaBeachLoc, "sched");
		ObjectBoundary obj5 = storeObjectInDatabase("task", true, null, "enter");
		ObjectBoundary obj6 = storeObjectInDatabase("task", true, null, "taskMen");
		ObjectBoundary obj7 = storeObjectInDatabase("task", false, eilatCenteralLoc, "enter");
		ObjectBoundary obj8 = storeObjectInDatabase("task", false, herzeliyaBeachLoc, "taskMen");

		System.err.println("Creating commands:");
		storeCommandInDatabase("dummyApp", "doSomething", obj5.getObjectId());
		storeCommandInDatabase("dummyApp", "Hello", obj5.getObjectId());
	}

	private void createNewUserBoundary(String username, String avatar, RolesEnum role, String emailInitial) {
		NewUserBoundary user = new NewUserBoundary();
		user.setUsername(username);
		user.setEmail(emailInitial + "@gmail.com");
		user.setAvatar(avatar);
		user.setRole(role);
		userService.createNewUser(user);
	}

	private ObjectBoundary storeObjectInDatabase(String type, boolean active, Location location, String alias) {
		ObjectBoundary boundary = createNewObjectBoundary(type, active, alias, location);
		return this.objectService.createObject(boundary);
	}

	private ObjectBoundary createNewObjectBoundary(String type, boolean active, String alias, Location location) {
		ObjectBoundary boundary = new ObjectBoundary();
		boundary.setCreatedBy(new CreatedBy(new UserId("2024b.Tal.Mizrahi", "superapp@gmail.com")));
		boundary.setActive(active);
		boundary.setAlias(alias);
		boundary.setType(type);
		boundary.setLocation(location);
		return boundary;
	}

	private void storeCommandInDatabase(String miniapp, String command, ObjectId objId) {
		MiniAppCommandBoundary boundary = createMiniappCommandBoundary(command, objId);
		this.commandSerivce.invokeACommand(miniapp, boundary);
	}

	private MiniAppCommandBoundary createMiniappCommandBoundary(String command, ObjectId objId) {
		MiniAppCommandBoundary boundary = new MiniAppCommandBoundary();
		boundary.setTargetObject(new TargetObject(objId));
		boundary.setCommand(command);
		boundary.setInvokedBy(new InvokedBy(new UserId("2024b.Tal.Mizrahi", "miniapp@gmail.com")));
		return boundary;
	}

}

