package demo.controllers;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import demo.boundaries.MiniAppCommandBoundary;
import demo.boundaries.UserBoundary;
import demo.services.interfaces.EnhancedAdminService;

@RestController
@RequestMapping(path ={"/superapp/admin"})
public class AdminController {

		private EnhancedAdminService adminService;

		public AdminController(EnhancedAdminService adminService) {
			this.adminService = adminService;
		}
		
		//?userSuperapp={userSuperapp}&userEmail={email}
		
		@DeleteMapping (
				path = { "/users" })
		public void deleteAllUsers(
				@RequestParam(name = "userSuperapp", required = true) String userSuperapp,
				@RequestParam(name = "userEmail", required = true) String email){
			this.adminService.deleteAllUsers(userSuperapp, email);
		}

		@DeleteMapping (
				path = { "/objects" })
		public void deleteAllObjects(
				@RequestParam(name = "userSuperapp", required = true) String userSuperapp,
				@RequestParam(name = "userEmail", required = true) String email) {
			this.adminService.deleteAllObjects(userSuperapp, email);
			
		}

		@DeleteMapping (
				path = { "/miniapp" })
		public void deleteAllCommandsHistory(
				@RequestParam(name = "userSuperapp", required = true) String userSuperapp,
				@RequestParam(name = "userEmail", required = true) String email) {
			this.adminService.deleteAllCommandsHistory(userSuperapp, email);
		} 
		
		//?userSuperapp={userSuperapp}&userEmail={email}&size={size}&page={page}

		@GetMapping(
				path = {"/users"},
				produces = MediaType.APPLICATION_JSON_VALUE)
		public UserBoundary[] getAllUsers(
				@RequestParam(name = "userSuperapp", required = true) String userSuperapp,
				@RequestParam(name = "userEmail", required = true) String email,
				@RequestParam(name = "size", required = false, defaultValue = "10") int size,
				@RequestParam(name = "page", required = false, defaultValue = "0") int page) {
			return adminService
					.getAllUsers(userSuperapp, email, size, page)
					.toArray(new UserBoundary[0]);
		}

		@GetMapping(
				path = {"/miniapp"},
				produces = MediaType.APPLICATION_JSON_VALUE)
		public MiniAppCommandBoundary[] getAllCommands(
				@RequestParam(name = "userSuperapp", required = true) String userSuperapp,
				@RequestParam(name = "userEmail", required = true) String email,
				@RequestParam(name = "size", required = false, defaultValue = "10") int size,
				@RequestParam(name = "page", required = false, defaultValue = "0") int page) {
			return adminService
					.getAllCommands(userSuperapp, email, size, page)
					.toArray(new MiniAppCommandBoundary[0]);
		}

		@GetMapping(
				path = {"/miniapp/{miniAppName}"},
				produces = MediaType.APPLICATION_JSON_VALUE)
		public MiniAppCommandBoundary[] getCommandsOfSpecificMiniApp(
				@PathVariable("miniAppName") String miniAppName,
				@RequestParam(name = "userSuperapp", required = true) String userSuperapp,
				@RequestParam(name = "userEmail", required = true) String email,
				@RequestParam(name = "size", required = false, defaultValue = "10") int size,
				@RequestParam(name = "page", required = false, defaultValue = "0") int page) {
			return adminService
					.getCommandsOfSpecificMiniApp(miniAppName, userSuperapp, email, size, page)
					.toArray(new MiniAppCommandBoundary[0]);
		}
}
