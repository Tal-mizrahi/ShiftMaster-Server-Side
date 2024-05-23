package demo;

import jakarta.persistence.Embeddable;

@Embeddable
public class ObjectId {

	private String superAppName;
	private String id;

	
	public ObjectId() {
	}


	public String getSuperAppName() {
		return superAppName;
	}


	public void setSuperAppName(String superAppName) {
		this.superAppName = superAppName;
	}


	public String getId() {
		return id;
	}


	public void setId(String id) {
		this.id = id;
	}


	@Override
	public String toString() {
		return "ObjectId [superApp=" + superAppName + ", id=" + id + "]";
	}
	
	
	
} 
