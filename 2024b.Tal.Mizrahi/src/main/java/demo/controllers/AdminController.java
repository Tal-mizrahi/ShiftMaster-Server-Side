package demo.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import demo.services.AdminService;

@RestController
@RequestMapping(path ={"/superapp/admin"})
public class AdminController {

		private AdminService adminService;

		public AdminController(AdminService adminService) {
			this.adminService = adminService;
		}

}
