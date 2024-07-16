package demo.objects;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import demo.boundaries.*;
import demo.services.exceptions.BadRequestException;
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
	private String springApplicationName;


	@Value("${spring.application.name:supperApp}")
	public void setSpringApplicationName(String springApplicationName) {
		this.springApplicationName = springApplicationName;
		System.err.println("The Spring Application name is: " + this.springApplicationName);
	}
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
		
		
		createNewUserBoundary("super user", "su", RolesEnum.ADMIN, "suAdmin@gmail.com");
		adminService.deleteAllCommandsHistory("2024b.Tal.Mizrahi", "suAdmin@gmail.com");
		adminService.deleteAllObjects("2024b.Tal.Mizrahi", "suAdmin@gmail.com");
		adminService.deleteAllUsers("2024b.Tal.Mizrahi", "suAdmin@gmail.com");

		//private UserBoundary workerRegister(String username, String email, String password)
		System.err.println("Creating workers:");
		
		UserBoundary tal = teamLeaderRegister("Tal Mizrahi", "talmiz@gmail.com", "talmiz212");
		ArrayList<UserBoundary> allWorkers = new ArrayList<>();
		
		allWorkers.add(workerRegister("Hadas Maayan", "hadas@gmail.com", "hadas123"));
		allWorkers.add(workerRegister("Noam Ben Benyamin", "noam@gmail.com", "noam123"));
		allWorkers.add(workerRegister("Adir Zadok", "adir@gmail.com", "adir123"));
		allWorkers.add(workerRegister("Katya Trofimenko", "katya@gmail.com", "katya123"));
		allWorkers.add(workerRegister("Kristina Kolesnyk", "kristina@gmail.com", "kristina123"));
		allWorkers.add(workerRegister("Tom Cohen", "tom@gmail.com", "tom123"));
		allWorkers.add(workerRegister("Deganit Armon", "deganit@gmail.com", "deganit123"));
		allWorkers.add(workerRegister("Hadar Binsky", "hadar@gmail.com", "hadar123"));
		allWorkers.add(workerRegister("Guy Isacov", "guy@gmail.com", "gut123"));
		allWorkers.add(workerRegister("Sharon Handzel", "sharon@gmail.com", "sharon123"));
		
		
//		createNewUserBoundary("Tal Miz", "TM", RolesEnum.SUPERAPP_USER, "superapp");
//		createNewUserBoundary("John Doe", "JD", RolesEnum.ADMIN, "admin");
//		createNewUserBoundary("Jane Smith", "JS", RolesEnum.MINIAPP_USER, "miniapp");
		
		createShiftSchedule(tal.getUserId(), "Mon Jul 15 2024", allWorkers);
		Location herzeliyaBeachLoc = new Location(32.158687, 34.795102); 
		Location eilatCenteralLoc = new Location(29.555803, 34.952435); 

		System.err.println("Creating Objects:");
//		ObjectBoundary obj1 = storeObjectInDatabase("schedual", true, herzeliyaBeachLoc, "enter");
//		ObjectBoundary obj2 = storeObjectInDatabase("schedual", true, eilatCenteralLoc, "sched");
//		ObjectBoundary obj3 = storeObjectInDatabase("schedual", false, null, "enter");
//		ObjectBoundary obj4 = storeObjectInDatabase("schedual", false, herzeliyaBeachLoc, "sched");
//		ObjectBoundary obj5 = storeObjectInDatabase("task", true, null, "enter");
//		ObjectBoundary obj6 = storeObjectInDatabase("task", true, null, "taskMen");
//		ObjectBoundary obj7 = storeObjectInDatabase("task", false, eilatCenteralLoc, "enter");
//		ObjectBoundary obj8 = storeObjectInDatabase("task", false, herzeliyaBeachLoc, "taskMen");
//
//		System.err.println("Creating commands:");
//		storeCommandInDatabase("dummyApp", "doSomething", obj5.getObjectId());
//		storeCommandInDatabase("dummyApp", "Hello", obj5.getObjectId());
		
		//System.err.println(calculateNextMonday());
	}
	
	private UserBoundary workerRegister(String username, String email, String password) {
		UserBoundary worker = createNewUserBoundary(username, "before change", RolesEnum.SUPERAPP_USER, email);
		System.err.println("user entity created: " + worker);
		Map<String, Object> availableShifts = new HashMap<>();
		 String[][] shiftsArray = new String[7][3];
	        for (String[] row : shiftsArray) {
	            Arrays.fill(row, worker.getUsername());
	        }
		availableShifts.put("availableShifts", shiftsArray);
		ObjectBoundary shiftObject = storeObjectInDatabase("availableShifts", true, null,  calculateNextMonday(),worker.getUserId(), availableShifts);
		
		Map<String, Object> userDetails = new HashMap<>();
		userDetails.put("password", password);
		userDetails.put("username", worker.getUsername());
		userDetails.put("availableShifts", shiftObject.getObjectId().getSuperapp() + "#" + shiftObject.getObjectId().getId());
		
		ObjectBoundary userObject = storeObjectInDatabase("Worker", true, null, "default",worker.getUserId(), userDetails);
		userService.updateDetails(worker.getUserId().getEmail(), worker.getUserId().getSuperapp(), createUserBoundary(null, userObject.getObjectId().getSuperapp() + "#" + userObject.getObjectId().getId(), null));
		return worker;
	}
	
	private UserBoundary teamLeaderRegister(String username, String email, String password) {
		UserBoundary teamLeader = createNewUserBoundary(username, "before change", RolesEnum.SUPERAPP_USER, email);
		Map<String, Object> userDetails = new HashMap<>();
		userDetails.put("password", password);
		userDetails.put("username", teamLeader.getUsername());		
		ObjectBoundary userObject = storeObjectInDatabase("Worker", true, null, "default",teamLeader.getUserId(), userDetails);
		userService.updateDetails(teamLeader.getUserId().getEmail(), teamLeader.getUserId().getSuperapp(), createUserBoundary(null, userObject.getObjectId().getSuperapp() + "#" + userObject.getObjectId().getId(), null));
		return teamLeader;
	}
	
	private void createNote(UserId userId, String title, String reminderDate, String content, String active) {
		Map<String, Object> noteDetails = new HashMap<>();
		noteDetails.put("title", title);
		noteDetails.put("reminderDate", reminderDate);
		noteDetails.put("content", content);
		ObjectBoundary noteObject = storeObjectInDatabase("note", true, null, active, userId, noteDetails);
	}
	
	private void createTask(UserId userId,String alias, String title, String description, String assignee, String priority, String openDate, String lastUpdate) {
		Map<String, Object> taskDetails = new HashMap<>();
		taskDetails.put("title", title);
		taskDetails.put("description", description);
		taskDetails.put("opener", userId.getSuperapp() + "#" + userId.getEmail());
		taskDetails.put("assignee", assignee);
		taskDetails.put("priority", priority);
		taskDetails.put("openDate", openDate);
		taskDetails.put("lastUpdate", lastUpdate);
		ObjectBoundary taskObject = storeObjectInDatabase("task", true, null, alias ,userId, taskDetails);
		
	}
	
	private void createShiftSchedule(UserId teamLeaderId, String date, ArrayList<UserBoundary> allWorkers) {
	    if (allWorkers.size() < 9) {
	        throw new BadRequestException("At least 9 names are required.");
	    }

	    String[][][] shiftSchedule = new String[7][3][];

	    // Initialize all slots to empty arrays
	    for (int day = 0; day < 7; day++) {
	        for (int shift = 0; shift < 3; shift++) {
	            shiftSchedule[day][shift] = new String[0];
	        }
	    }

	    // Assign names to the shifts
	    for (int day = 0; day < 7; day++) {
	        shiftSchedule[day][0] = new String[]{
	            allWorkers.get(0).getUsername(),
	            allWorkers.get(1).getUsername(),
	            allWorkers.get(2).getUsername(),
	            allWorkers.get(3).getUsername()
	        };
	        
	        shiftSchedule[day][1] = new String[]{
	            allWorkers.get(4).getUsername(),
	            allWorkers.get(5).getUsername(),
	            allWorkers.get(6).getUsername()
	        };
	        
	        shiftSchedule[day][2] = new String[]{
	            allWorkers.get(7).getUsername(),
	            allWorkers.get(8).getUsername()
	        };
	    }

	    Map<String, Object> shiftDetails = new HashMap<>();
	    shiftDetails.put("shiftSchedule", shiftSchedule);
	    storeObjectInDatabase("shiftSchedule", true, null, date, teamLeaderId, shiftDetails);
	}

	
	
	

	private UserBoundary createNewUserBoundary(String username, String avatar, RolesEnum role, String email) {
		NewUserBoundary user = new NewUserBoundary();
		user.setUsername(username);
		user.setEmail(email);
		user.setAvatar(avatar);
		user.setRole(role);
		System.err.println("new user boundary: " + user);
		return userService.createNewUser(user);
	}
	
	private UserBoundary createUserBoundary(String username, String avatar, RolesEnum role) {
		UserBoundary user = new UserBoundary();
		user.setUsername(username);
		user.setAvatar(avatar);
		user.setRole(role);
		return user;
	}
	


	private ObjectBoundary storeObjectInDatabase(String type, boolean active, Location location, String alias, UserId userId, Map<String, Object> objectDetails) {
		ObjectBoundary boundary = createNewObjectBoundary(type, active, alias, location, userId, objectDetails);
		return this.objectService.createObject(boundary);
	}

	private ObjectBoundary createNewObjectBoundary(String type, boolean active, String alias, Location location, UserId userId, Map<String, Object> objectDetails) {
		ObjectBoundary boundary = new ObjectBoundary();
		boundary.setCreatedBy(new CreatedBy(userId));
		boundary.setActive(active);
		boundary.setAlias(alias);
		boundary.setType(type);
		boundary.setLocation(location);
		boundary.setObjectDetails(objectDetails);
		return boundary;
	}

//	private void storeCommandInDatabase(String miniapp, String command, ObjectId objId) {
//		MiniAppCommandBoundary boundary = createMiniappCommandBoundary(command, objId);
//		this.commandSerivce.invokeACommand(miniapp, boundary);
//	}
//
//	private MiniAppCommandBoundary createMiniappCommandBoundary(String command, ObjectId objId) {
//		MiniAppCommandBoundary boundary = new MiniAppCommandBoundary();
//		boundary.setTargetObject(new TargetObject(objId));
//		boundary.setCommand(command);
//		boundary.setInvokedBy(new InvokedBy(new UserId("2024b.Tal.Mizrahi", "miniapp@gmail.com")));
//		return boundary;
//	}

    public String calculateNextMonday() {
    	   LocalDate today = LocalDate.now();
           LocalDate nextMonday = today.with(TemporalAdjusters.next(DayOfWeek.MONDAY));
           DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEE MMM dd yyyy", Locale.ENGLISH);
           return nextMonday.format(formatter);
    }
    
    public static String getCurrentDateTimeInISOFormat() {
        // Get the current date and time with timezone information
        ZonedDateTime now = ZonedDateTime.now();
        
        // Define the formatter to format the date and time in the required ISO 8601 format
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        
        // Format the current date and time to the required format
        return now.format(formatter);
    }

}

