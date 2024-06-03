package demo.boundaries;

import java.util.Date;
import java.util.Map;

import demo.objects.CreatedBy;
import demo.objects.Location;
import demo.objects.ObjectId;

public class ObjectBoundary {
	
	private ObjectId objectId;
    private String type;
    private String alias;
    
    private Location location;
    private Boolean active;
    private Date creationTimesTamp;
    private CreatedBy createdBy;
    private Map<String, Object> objectDetails;
	
    public ObjectBoundary() {}

	public ObjectId getObjectId() {
		return objectId;
	}

	public void setObjectId(ObjectId objectId) {
		this.objectId = objectId;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

	public Date getCreationTimesTamp() {
		return creationTimesTamp;
	}

	public void setCreationTimesTamp(Date creationTimeStamp) {
		this.creationTimesTamp = creationTimeStamp;
	}

	public CreatedBy getCreatedBy() {
		return createdBy;
	} 

	public void setCreatedBy(CreatedBy createdBy) {
		this.createdBy = createdBy;
	}

	public Map<String, Object> getObjectDetails() {
		return objectDetails;
	}

	public void setObjectDetails(Map<String, Object> objectDetails) {
		this.objectDetails = objectDetails;
	}

	public Location getLocation() {
		return location;
	}
	
	public void setLocation(Location location) {
		this.location = location;
	}
		
	@Override
	public String toString() {
		return "ObjectBoundary [objectId=" + objectId 
				+ ", type=" + type 
				+ ", alias=" + alias 
				+ ", active=" + active
				+ ", location= " + location
				+ ", creationTimeStamp=" + creationTimesTamp 
				+ ", createdBy=" + createdBy 
				+ ", objectDetails=" + objectDetails + "]";
		
	}

    
    
    
}
