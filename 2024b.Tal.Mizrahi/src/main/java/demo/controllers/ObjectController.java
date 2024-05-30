package demo.controllers;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import demo.boundaries.ObjectBoundary;
import demo.services.ObjectService;
import demo.services.NotFoundException;

@RestController
@RequestMapping(path ={"/superapp/objects"})
public class ObjectController {
	
	private ObjectService objectService;

	public ObjectController(ObjectService objectService) {
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
			@PathVariable("id") String objectId,
			@PathVariable("superapp") String superapp,
			@RequestBody ObjectBoundary boundary) {
    	
    	this.objectService.updateObject(objectId, superapp, boundary);
		
	} 
	
	@GetMapping(
			path = { "/{superapp}/{id}" }, 
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ObjectBoundary getObjectById(
			@PathVariable("id") String objectId,
			@PathVariable("superapp") String superapp) {
		
		return this.objectService
				.getObjectById(objectId, superapp)
				.orElseThrow(()->new NotFoundException("ObjectEntity with id: " + objectId 
						+ " and superapp name: " + superapp + " Does not exist in database"));
	}
	
	@GetMapping(
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ObjectBoundary[] getAllObjects () {
		
		return this.objectService.getAllObjects()
				.toArray(new ObjectBoundary[0]);
	}
}
