package demo.objects;

public class ObjectId {

	private String superapp;
	private String id;

	public ObjectId() {
	}
	
	public ObjectId(String superapp, String id) {
		this.superapp = superapp;
		this.id = id;
	} 
	

	public String getSuperapp() {
		return superapp;
	}


	public void setSuperapp(String superapp) {
		this.superapp = superapp;
	}


	public String getId() {
		return id;
	}


	public void setId(String id) {
		this.id = id;
	}


	@Override
	public String toString() {
		return "ObjectId [superapp=" + superapp + ", id=" + id + "]";
	}
	
	
	
} 
