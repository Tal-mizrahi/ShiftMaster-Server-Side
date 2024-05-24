package demo.controllers;

import java.util.List;
import java.util.Optional;

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
            path={"/${spring.application.name}/{id}"},
            consumes={MediaType.APPLICATION_JSON_VALUE},
            produces={MediaType.APPLICATION_JSON_VALUE})
	public void updateObject(@PathVariable String objectId, ObjectBoundary boundary) {
		
	}
	
	@GetMapping(
			path = { "${spring.application.name}/{id}" }, 
			produces = MediaType.APPLICATION_JSON_VALUE)
	public Optional<ObjectBoundary> getObjectById(@PathVariable String objectId) {
		
		return null;
		
	}
	
	@GetMapping(
			produces = MediaType.APPLICATION_JSON_VALUE)
	public List<ObjectBoundary> getAllObjects () {
		
		return null;
	}
}
