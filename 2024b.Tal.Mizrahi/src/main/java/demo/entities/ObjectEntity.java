package demo.entities;

import java.util.Date;
import java.util.Map;

import jakarta.persistence.Id;

import demo.CreatedBy;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "OBJECT_TABLE")
public class ObjectEntity {
	
	   @Id private String objectID;
		private String type;
	    private String alias;
	    private Boolean active;
	    private Date creationTimeStamp;
//		private CreatedBy createdBy;
//	    private Map<String, Object> objectDetails;
	    
	    public ObjectEntity() {
	    }

		public String getObjectID() {
			return objectID;
		}

		public void setObjectID(String objectID) {
			this.objectID = objectID;
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

		public Date getCreationTimeStamp() {
			return creationTimeStamp;
		}

		public void setCreationTimeStamp(Date creationTimeStamp) {
			this.creationTimeStamp = creationTimeStamp;
		}

		/** On Wednesday we will learn how to use with
		 *  a map and with objects we created (CreatedBy object)
		 */
		
//		public CreatedBy getCreatedBy() {
//			return createdBy;
//		}
		
//		public void setCreatedBy(CreatedBy createdBy) {
//			this.createdBy = createdBy;
//		}
//		
//		public Map<String, Object> getObjectDetails() {
//			return objectDetails;
//		}
//		
//		public void setObjectDetails(Map<String, Object> objectDetails) {
//			this.objectDetails = objectDetails;
//		}
		
//		@Override
//		public String toString() {
//			return "ObjectEntity [objectID=" + objectID + ", type=" + type + ", alias=" + alias + ", active=" + active
//					+ ", creationTimeStamp=" + creationTimeStamp + ", createdBy=" + createdBy + ", objectDetails="
//					+ objectDetails + "]";
//		}
		


}
