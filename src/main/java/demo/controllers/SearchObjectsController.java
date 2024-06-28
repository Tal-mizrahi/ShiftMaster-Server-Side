package demo.controllers;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import demo.boundaries.ObjectBoundary;
import demo.services.interfaces.EnhancedObjectService;

@RestController
@RequestMapping(path ={"/superapp/objects/search"})
public class SearchObjectsController {

	private EnhancedObjectService objectService;

	public SearchObjectsController(EnhancedObjectService objectService) {
		super();
		this.objectService = objectService;
	}

	@GetMapping(
			path = { "/byType/{type}" },
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ObjectBoundary[] getObjectsByType (
			@PathVariable ("type") String type,
			@RequestParam(name = "userSuperapp", required = true) String userSuperapp,
			@RequestParam(name = "userEmail", required = true) String email,
			@RequestParam(name = "size", required = false, defaultValue = "10") int size,
			@RequestParam(name = "page", required = false, defaultValue = "0") int page) {
		
		return this.objectService.getAllObjectsByType(type, userSuperapp, email, size, page)
				.toArray(new ObjectBoundary[0]);
	}
	
	@GetMapping(
			path = { "/byAlias/{alias}" },
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ObjectBoundary[] getObjectsByAlias (
			@PathVariable("alias") String alias,
			@RequestParam(name = "userSuperapp", required = true) String userSuperapp,
			@RequestParam(name = "userEmail", required = true) String email,
			@RequestParam(name = "size", required = false, defaultValue = "10") int size,
			@RequestParam(name = "page", required = false, defaultValue = "0") int page) {
		
		return this.objectService.getAllObjectsByAlias(alias,userSuperapp, email, size, page)
				.toArray(new ObjectBoundary[0]);
	}

	@GetMapping(
			path = { "/byAliasPattern/{pattern}" },
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ObjectBoundary[] getObjectsByAliasPattern (
			@PathVariable("pattern") String pattern,
			@RequestParam(name = "userSuperapp", required = true) String userSuperapp,
			@RequestParam(name = "userEmail", required = true) String email,
			@RequestParam(name = "size", required = false, defaultValue = "10") int size,
			@RequestParam(name = "page", required = false, defaultValue = "0") int page) {
		
		return this.objectService.getAllObjectsByAliasPattern(pattern,userSuperapp, email, size, page)
				.toArray(new ObjectBoundary[0]);
	}
	
	@GetMapping(
			path = { "/byLocation/{lat}/{lng}/{distance}" },
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ObjectBoundary[] getObjectsByLocationRadius (
			@PathVariable("lat") double lat,
			@PathVariable("lng") double lng,
			@PathVariable("distance") double distance,
			@RequestParam(name = "distanceUnits", required = false, defaultValue = "NEUTRAL") String distanceUnits,
			@RequestParam(name = "userSuperapp", required = true) String userSuperapp,
			@RequestParam(name = "userEmail", required = true) String email,
			@RequestParam(name = "size", required = false, defaultValue = "10") int size,
			@RequestParam(name = "page", required = false, defaultValue = "0") int page) {
		
		return this.objectService.getAllObjectsByLocationRadius(lat, lng, distance, distanceUnits, userSuperapp, email, size, page)
				.toArray(new ObjectBoundary[0]);
	}
}
