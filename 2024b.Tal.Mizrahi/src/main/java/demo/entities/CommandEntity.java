package demo.entities;

import java.util.Date;
import java.util.Map;

import jakarta.persistence.Id;
import jakarta.persistence.MapKeyColumn;
import demo.CreatedBy;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;


@Entity
@Table(name = "COMMAND_TABLE")
public class CommandEntity {
	
	@Id private String commandId;

	private String miniAppName;
	private String command;
	private Date invocationTimeStamp;
//	private CreatedBy invokedBy;
//	private Map<String, Object> commandAttributes;
	
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
	
	public Date getInvocationTimeStamp() {
		return invocationTimeStamp;
	}
	
	public void setInvocationTimeStamp(Date invocationTimeStamp) {
		this.invocationTimeStamp = invocationTimeStamp;
	}
	
	/** On Wednesday we will learn how to use with
	 *  a map and with objects we created (CreatedBy object)
	 */
	
//	public CreatedBy getInvokedBy() {
//		return invokedBy;
//	}
//	
//	public void setInvokedBy(CreatedBy invokedBy) {
//		this.invokedBy = invokedBy;
//	}
//	
//	public Map<String, Object> getCommandAttributes() {
//		return commandAttributes;
//	}
//	
//	public void setCommandAttributes(Map<String, Object> commandAttributes) {
//		this.commandAttributes = commandAttributes;
//	}

//	@Override
//	public String toString() {
//		return "CommandEntity [commandId=" + commandId + ", miniAppName=" + miniAppName + ", command=" + command
//				+ ", invocationTimeStamp=" + invocationTimeStamp 
//				+ ", invokedBy=" + invokedBy + ", commandAttributes="
//				+ commandAttributes + "]";
//	}
//	
	
	
 
}
