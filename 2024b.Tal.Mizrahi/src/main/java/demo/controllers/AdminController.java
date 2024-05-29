package demo.controllers;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import demo.boundaries.MiniAppCommandBoundary;
import demo.boundaries.UserBoundary;
import demo.services.AdminService;

@RestController
@RequestMapping(path ={"/superapp/admin"})
public class AdminController {

		private AdminService adminService;

		public AdminController(AdminService adminService) {
			this.adminService = adminService;
		}
		
		@DeleteMapping (
				path = { "/admin/users" })
		public void deleteAllUsers() {
			this.adminService.deleteAllUsers();
		}

		@DeleteMapping (
				path = { "/objects" })
		public void deleteAllObjects() {
			this.adminService.deleteAllObjects();
			
		}

		@DeleteMapping (
				path = { "/miniapp" })
		public void deleteAllCommandsHistory() {
			this.adminService.deleteAllCommandsHistory();
		}

		@GetMapping(
				path = {"/users"},
				produces = MediaType.APPLICATION_JSON_VALUE)
		public UserBoundary[] getAllUsers() {
			return adminService
					.getAllUsers()
					.toArray(new UserBoundary[0]);
		}

		@GetMapping(
				path = {"/miniapp"},
				produces = MediaType.APPLICATION_JSON_VALUE)
		public MiniAppCommandBoundary[] getAllCommands() {
			return adminService
					.getAllCommands()
					.toArray(new MiniAppCommandBoundary[0]);
		}

		@GetMapping(
				path = {"/miniapp/{miniAppName}"},
				produces = MediaType.APPLICATION_JSON_VALUE)
		public MiniAppCommandBoundary[] getCommandsOfSpecificMiniApp(
				@PathVariable("miniAppName") String miniAppName) {
			return adminService
					.getCommandsOfSpecificMiniApp(miniAppName)
					.toArray(new MiniAppCommandBoundary[0]);
		}
}
