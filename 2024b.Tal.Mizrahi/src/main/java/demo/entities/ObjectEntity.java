package demo.entities;

import java.util.Date;
import java.util.Map;

import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import demo.CreatedBy;
import demo.Location;
import demo.ObjectId;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Embedded;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.persistence.Transient;

@Entity
@Table(name = "OBJECT_TABLE")
public class ObjectEntity {
	
		@EmbeddedId
		private ObjectId objectId;
		private String type;
	    private String alias;
	    private boolean active;
	    
	    @Temporal(TemporalType.TIMESTAMP)
	    private Date creationTimesTamp;
	    
	    @Embedded
	    private CreatedBy createdBy;
	    
	    @Transient
	    @Embedded
	    private Location location;
	    
	    
		@Transient
		//@Lob
		//@Convert(converter = ObjectApplicationMapToStringConverter.class)
	    private Map<String, Object> objectDetails;
	    
	    public ObjectEntity() {
	    }

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

		public boolean getActive() {
			return active;
		}

		public void setActive(boolean active) {
			this.active = active;
		}

		public Date getCreationTimesTamp() {
			return creationTimesTamp;
		}

		public void setCreationTimesTamp(Date creationTimeStamp) {
			this.creationTimesTamp = creationTimeStamp;
		}


		public Map<String, Object> getObjectDetails() {
			return objectDetails;
		}

		public void setObjectDetails(Map<String, Object> objectDetails) {
			this.objectDetails = objectDetails;
		}

		public CreatedBy getCreatedBy() {
			return createdBy;
		}

		public void setCreatedBy(CreatedBy createdBy) {
			this.createdBy = createdBy;
		}

		@Override
		public String toString() {
			return "ObjectEntity [objectId=" + objectId 
					+ ", type=" + type 
					+ ", alias=" + alias 
					+ ", active=" + active
					+ ", creationTimeStamp=" + creationTimesTamp 
					+ ", createdBy=" + createdBy 
					+ ", objectDetails=" + objectDetails + "]";
		}




}
