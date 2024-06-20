//package demo.objects;
//
//import java.util.Collections;
//import java.util.stream.Stream;
//
//import org.springframework.boot.CommandLineRunner;
//import org.springframework.context.annotation.Profile;
//import org.springframework.stereotype.Component;
//
//import demo.boundaries.NewUserBoundary;
//import demo.boundaries.ObjectBoundary;
//import demo.services.AdminService;
//import demo.services.ObjectService;
//import demo.services.UserService;
//
//@Component
//@Profile("manualTests")
//public class UserInitilaizer implements CommandLineRunner {
//
//	private UserService userService;
//	private ObjectService objectService;
//	private AdminService adminService;
//
//	public UserInitilaizer(UserService userService, ObjectService objectService, AdminService adminService) {
//		this.userService = userService;
//		this.objectService = objectService;
//		this.adminService = adminService;
//	}
//
//	@Override
//	public void run(String... args) throws Exception {
//
//		storeUserInDatabaseWithRole("admin@gmail.com", RolesEnum.ADMIN);
//		storeUserInDatabaseWithRole("miniappUser@gmail.com", RolesEnum.MINIAPP_USER);
//		storeUserInDatabaseWithRole("superappUser@gmail.com", RolesEnum.SUPERAPP_USER);
//		storeObjectInDatabaseWithActive(true, "object", "form", "form", "page");
//		storeObjectInDatabaseWithActive(false, "object", "form", "form", "page");
//
//	}
//
//	private void storeUserInDatabaseWithRole(String email, RolesEnum role) {
//
//		NewUserBoundary newUserBoundary = new NewUserBoundary();
//		newUserBoundary.setEmail(email);
//		newUserBoundary.setRole(role);
//		newUserBoundary.setAvatar("j");
//		newUserBoundary.setUsername("user");
//		this.userService.createNewUser(newUserBoundary);
//	}
//
//	private void storeObjectInDatabaseWithActive(String alias, boolean active, String... type) {
//		Stream.of(type).map(first -> createNewObject(alias, active, first))
//				.map(boundaryNotInDb -> this.objectService.createObject(boundaryNotInDb))
//				.forEach(boundary -> System.err.println("created: " + boundary));
//	}
//
//	private ObjectBoundary createNewObject(String alias, boolean active, String type, String ) {
//		ObjectBoundary rv = new ObjectBoundary();
//		rv.setCreatedBy(new CreatedBy(new UserId("2024b.Tal.Mizrahi", "superapp@gmail.com")));
//		rv.setActive(active);
//		rv.setAlias(alias);
//		rv.setType(type);
//		return rv;
//	}
//
//	private CreatedBy createSuperappUser() {
//		CreatedBy user = new CreatedBy();
//		UserId userId = new UserId();
//		userId.setSuperApp("2024b.Tal.Mizrahi");
//		userId.setEmail("superappUser@gmail.com");
//		user.setUserId(userId);
//		return user;
//	}
//
//}