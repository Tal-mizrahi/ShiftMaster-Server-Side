package demo.entities;

import java.util.Date;
import java.util.Map;

import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.MapKeyColumn;
import demo.CommandId;
import demo.CreatedBy;
import demo.InvokedBy;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Embedded;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.persistence.Transient;


@Entity
@Table(name = "COMMAND_TABLE")
public class CommandEntity {
	
	@EmbeddedId
	private CommandId commandId;

	private String miniAppName;
	private String command;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date invocationTimeStamp;
	
	@Embedded
	private InvokedBy invokedBy;
		
	@Transient
	//@Lob
	//@Convert(converter = CommandApplicationMapToStringConverter.class)
	private Map<String, Object> commandAttributes;
	
	public CommandEntity() {
	}

	public CommandId getCommandId() {
		return commandId;
	}

	public void setCommandId(CommandId commandId) {
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
	
	 
	public InvokedBy getInvokedBy() {
		return invokedBy;
	}
	
	public void setInvokedBy(InvokedBy invokedBy) {
		this.invokedBy = invokedBy;
	}
	
	public Map<String, Object> getCommandAttributes() {
		return commandAttributes;
	}
	
	public void setCommandAttributes(Map<String, Object> commandAttributes) {
		this.commandAttributes = commandAttributes;
	}

	@Override
	public String toString() {
		return "CommandEntity [commandId=" + commandId 
				+ ", miniAppName=" + miniAppName 
				+ ", command=" + command
				+ ", invocationTimeStamp=" + invocationTimeStamp 
				+ ", invokedBy=" + invokedBy 
				+ ", commandAttributes=" + commandAttributes + "]";
	}
		
 
}
