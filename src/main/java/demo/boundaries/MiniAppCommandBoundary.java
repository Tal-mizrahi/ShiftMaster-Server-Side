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
	private Date invocationTimestamp; 
	private InvokedBy invokedBy;
	private Map<String, Object> commandAttribute;
	
	public MiniAppCommandBoundary() {}

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

	public Date getInvocationTimestamp() {
		return invocationTimestamp;
	}

	public void setInvocationTimestamp(Date invocationTimestamp) {
		this.invocationTimestamp = invocationTimestamp;
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
				+ ", invocationTimestamp=" + invocationTimestamp 
				+ ", invokedBy=" + invokedBy
				+ ", commandAttribute=" + commandAttribute + "]";
	} 
	
	
	
	
	
}
