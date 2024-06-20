package demo.entities;

import java.util.Date;
import java.util.Map;

import demo.converters.ApplicationMapToStringConverter;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

@Entity
@Table(name = "COMMANDS_TABLE")
public class CommandEntity {
	
	@Id
	private String commandId;

	private String miniAppName;
	private String command;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date invocationTimestamp;
	
	private String invokedBy;
	
	private String targetObject;
		
	@Lob
	@Convert(converter = ApplicationMapToStringConverter.class)
	private Map<String, Object> commandAttributes;
	
	public CommandEntity() {
	}

	public String getCommandId() {
		return commandId;
	}

	public void setCommandId(String commandId) {
		this.commandId = commandId;
	}
	
	public String getMiniAppName() {
		return miniAppName;
	}
	
	public void setMiniAppName(String miniAppName) {
		this.miniAppName = miniAppName;
	}
	
	public String getCommand() {
		return command;
	}
	
	public void setCommand(String command) {
		this.command = command;
	}
	
	public Date getInvocationTimestamp() {
		return invocationTimestamp;
	}
	
	public void setInvocationTimestamp(Date invocationTimestamp) {
		this.invocationTimestamp = invocationTimestamp;
	}
	
	 
	public String getInvokedBy() {
		return invokedBy;
	}
	
	public void setInvokedBy(String invokedBy) {
		this.invokedBy = invokedBy;
	}
	
	public Map<String, Object> getCommandAttributes() {
		return commandAttributes;
	}
	
	public void setCommandAttributes(Map<String, Object> commandAttributes) {
		this.commandAttributes = commandAttributes;
	}

	public String getTargetObject() {
		return targetObject;
	}
	
	public void setTargetObject(String targetObject) {
		this.targetObject = targetObject;
	}
	
	@Override
	public String toString() {
		return "CommandEntity [commandId=" + commandId 
				+ ", miniAppName=" + miniAppName 
				+ ", command=" + command
				+ ", invocationTimestamp=" + invocationTimestamp 
				+ ", invokedBy=" + invokedBy 
				+ ", targetObject=" + targetObject
				+ ", commandAttributes=" + commandAttributes + "]";
	}

		
 
}
