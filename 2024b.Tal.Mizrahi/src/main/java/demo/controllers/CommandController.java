package demo.controllers;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import demo.boundaries.MiniAppCommandBoundary;
import demo.services.CommandService;

@RestController
@RequestMapping(path ={"/superapp/miniapp"})
public class CommandController {

	private CommandService commandService;

	public CommandController(CommandService commandService) {
		this.commandService = commandService;
	
	} 
	
	@PostMapping(
			path = { "/{miniAppName}"},
			consumes = MediaType.APPLICATION_JSON_VALUE,
			produces = MediaType.APPLICATION_JSON_VALUE
			)
	public MiniAppCommandBoundary[] createObject(
			@PathVariable("miniAppName") String miniAppName,
			@RequestBody MiniAppCommandBoundary boundary) {
		MiniAppCommandBoundary[] jsonArr = new MiniAppCommandBoundary[] {  commandService.invokeACommand(miniAppName, boundary) };
		
		return jsonArr;
	}
}
