package demo;

public class ObjectId {

	private String superApp;
	private String id;

	
	public ObjectId() {
	}


	public String getSuperApp() {
		return superApp;
	}


	public void setSuperApp(String superApp) {
		this.superApp = superApp;
	}


	public String getId() {
		return id;
	}


	public void setId(String id) {
		this.id = id;
	}


	@Override
	public String toString() {
		return "ObjectId [superApp=" + superApp + ", id=" + id + "]";
	}
	
	
	
} 
