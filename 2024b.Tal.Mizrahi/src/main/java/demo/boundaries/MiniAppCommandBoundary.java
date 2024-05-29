package demo.boundaries;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import demo.objects.CommandId;
import demo.objects.InvokedBy;
import demo.objects.TargetObject;

public class MiniAppCommandBoundary {
	
	private CommandId commandId;
	private String command;
	private TargetObject targetObject;
	private Date invocationTimesTamp; 
	private InvokedBy invokedBy;
	private Map<String, Object> commandAttribute;
	
	public MiniAppCommandBoundary() {
		commandAttribute = new HashMap<>();
	}

	public CommandId getCommandId() {
		return commandId;
	}

	public void setCommandId(CommandId commandId) {
		this.commandId = commandId;
	}

	public String getCommand() {
		return command;
	}

	public void setCommand(String command) {
		this.command = command;
	}

	public TargetObject getTargetObject() {
		return targetObject;
	}

	public void setTargetObject(TargetObject targetObject) {
		this.targetObject = targetObject;
	}

	public Date getInvocationTimesTamp() {
		return invocationTimesTamp;
	}

	public void setInvocationTimesTamp(Date invocationTimesTamp) {
		this.invocationTimesTamp = invocationTimesTamp;
	}

	public InvokedBy getInvokedBy() {
		return invokedBy;
	}

	public void setInvokedBy(InvokedBy invokedBy) {
		this.invokedBy = invokedBy;
	}

	public Map<String, Object> getCommandAttribute() {
		return commandAttribute;
	}

	public void setCommandAttribute(Map<String, Object> commandAttribute) {
		this.commandAttribute = commandAttribute;
	}

	@Override
	public String toString() {
		return "MiniAppCommandBoundary [commandId=" + commandId 
				+ ", command=" + command 
				+ ", targetObject=" + targetObject 
				+ ", invocationTimesTamp=" + invocationTimesTamp 
				+ ", invokedBy=" + invokedBy
				+ ", commandAttribute=" + commandAttribute + "]";
	} 
	
	
	
	
	
}
