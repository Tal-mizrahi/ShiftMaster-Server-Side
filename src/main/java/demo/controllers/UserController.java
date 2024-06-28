package demo.controllers;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import demo.boundaries.NewUserBoundary;
import demo.boundaries.UserBoundary;
import demo.services.exceptions.NotFoundException;
import demo.services.interfaces.UserService;

@RestController
@RequestMapping(path = {"/superapp/users"})
public class UserController {
	
	private UserService userService;

	public UserController(UserService userService) {
		this.userService = userService;
	} 
	
	@PostMapping(
			consumes = MediaType.APPLICATION_JSON_VALUE,
			produces = MediaType.APPLICATION_JSON_VALUE
			)
	public UserBoundary createUser(@RequestBody NewUserBoundary boundary) {
		return this.userService
				.createNewUser(boundary);
	}
	
	
	
	@GetMapping(
			path = { "/login/{superapp}/{email}" }, 
			produces = MediaType.APPLICATION_JSON_VALUE)
		public UserBoundary loginUser(
				@PathVariable("superapp") String superapp,
				@PathVariable("email") String email) {
			return this.userService
				.getUser(email, superapp)
				.orElseThrow(()->new NotFoundException("could not find user by email: " + email
						+ " and by superapp: " + superapp));
		}
	
    @PutMapping(
            path={"/{superapp}/{userEmail}"},
            consumes={MediaType.APPLICATION_JSON_VALUE})
    public void updateDetails(
    			@PathVariable("superapp") String superapp,
    			@PathVariable("userEmail") String email,
                @RequestBody UserBoundary boundary) {
    		userService.updateDetails(email,superapp, boundary);
    }
	
	
}
