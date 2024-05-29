package demo.objects;

public class ObjectId {

	private String superApp;
	private String id;

	public ObjectId() {
	}
	
	public ObjectId(String superApp, String id) {
		this.superApp = superApp;
		this.id = id;
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
