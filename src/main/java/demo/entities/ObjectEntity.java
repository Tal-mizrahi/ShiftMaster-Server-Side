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
@Table(name = "OBJECTS_TABLE")
public class ObjectEntity {
	
		@Id
		private String objectId;
		private String type;
	    private String alias;
	    private boolean active;
	    
	    @Temporal(TemporalType.TIMESTAMP)
	    private Date creationTimestamp;
	    
	    private String createdBy;
	    	    
	    private double lng;
	    private double lat;
	    
		@Lob
		@Convert(converter = ApplicationMapToStringConverter.class)
	    private Map<String, Object> objectDetails;
	    
	    public ObjectEntity() {
	    } 

		public String getObjectId() {
			return objectId;
		}

		public void setObjectId(String objectId) {
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
		

	    public double getLat() {
			return lat;
		}

		public void setLat(double lat) {
			this.lat = lat;
		}

		public double getLng() {
			return lng;
		}

		public void setLng(double lng) {
			this.lng = lng;
		}


		public boolean getActive() {
			return active;
		}

		public void setActive(boolean active) {
			this.active = active;
		}

		public Date getCreationTimestamp() {
			return creationTimestamp;
		}

		public void setCreationTimestamp(Date creationTimestamp) {
			this.creationTimestamp = creationTimestamp;
		}


		public Map<String, Object> getObjectDetails() {
			return objectDetails;
		}

		public void setObjectDetails(Map<String, Object> objectDetails) {
			this.objectDetails = objectDetails;
		}

		public String getCreatedBy() {
			return createdBy;
		}

		public void setCreatedBy(String createdBy) {
			this.createdBy = createdBy;
		}

		@Override
		public String toString() {
			return "ObjectEntity [objectId=" + objectId 
					+ ", type=" + type 
					+ ", alias=" + alias 
					+", lat= " + lat
					+", lng= " + lng					
					+ ", active=" + active
					+ ", creationTimestamp=" + creationTimestamp 
					+ ", createdBy=" + createdBy 
					+ ", objectDetails=" + objectDetails + "]";
		}


}
