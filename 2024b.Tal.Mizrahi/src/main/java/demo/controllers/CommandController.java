package demo.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import demo.services.CommandService;

@RestController
@RequestMapping(path ={"/superapp/miniapp"})
public class CommandController {

	private CommandService commandService;

	public CommandController(CommandService commandService) {
		this.commandService = commandService;
	}
}
