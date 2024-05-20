package demo.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import demo.services.ObjectService;

@RestController
@RequestMapping(path ={"/superapp/objects"})
public class ObjectController {
	
	private ObjectService objectService;

	public ObjectController(ObjectService objectService) {
		this.objectService = objectService;
	}
}
