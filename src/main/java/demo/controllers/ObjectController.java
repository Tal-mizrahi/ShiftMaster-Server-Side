package demo.controllers;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import demo.boundaries.ObjectBoundary;
import demo.services.exceptions.NotFoundException;
import demo.services.interfaces.EnhancedObjectService;

@RestController
@RequestMapping(path ={"/superapp/objects"})
@CrossOrigin
public class ObjectController {
	
	private EnhancedObjectService objectService;

	public ObjectController(EnhancedObjectService objectService) {
		this.objectService = objectService;
	}
	
	@PostMapping(
			consumes = MediaType.APPLICATION_JSON_VALUE,
			produces = MediaType.APPLICATION_JSON_VALUE
			)
	public ObjectBoundary createObject(@RequestBody ObjectBoundary boundary) {
		
		return objectService.createObject(boundary);
	}
	
	
    @PutMapping(
            path={"/{superapp}/{id}"},
            consumes={MediaType.APPLICATION_JSON_VALUE},
            produces={MediaType.APPLICATION_JSON_VALUE})
	public void updateObject(
			@PathVariable("superapp") String superapp,
			@PathVariable("id") String objectId,
			@RequestParam(name = "userSuperapp", required = true) String userSuperapp,
			@RequestParam(name = "userEmail", required = true) String email,
			@RequestBody ObjectBoundary boundary) {
    	
    	this.objectService.updateObject(objectId, superapp, boundary, userSuperapp, email);
		
	} 
	
	@GetMapping(
			path = { "/{superapp}/{id}" }, 
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ObjectBoundary getObjectById(
			@PathVariable("superapp") String superapp,
			@PathVariable("id") String objectId,
			@RequestParam(name = "userSuperapp", required = true) String userSuperapp,
			@RequestParam(name = "userEmail", required = true) String email) {
		
		return this.objectService
				.getObjectById(objectId, superapp, userSuperapp, email)
				.orElseThrow(()->new NotFoundException("ObjectEntity with id: " + objectId 
						+ " and superapp name: " + superapp + " Does not exist in database"));
	}
	
	@GetMapping(
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ObjectBoundary[] getAllObjects (
			@RequestParam(name = "userSuperapp", required = true) String userSuperapp,
			@RequestParam(name = "userEmail", required = true) String email,
			@RequestParam(name = "size", required = false, defaultValue = "10") int size,
			@RequestParam(name = "page", required = false, defaultValue = "0") int page) {
		
		return this.objectService.getAllObjects(userSuperapp, email, size, page)
				.toArray(new ObjectBoundary[0]);
	}
	

}
