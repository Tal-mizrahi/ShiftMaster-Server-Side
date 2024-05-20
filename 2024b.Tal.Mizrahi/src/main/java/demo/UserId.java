package demo;

public class UserId {
	
	private String superApp;
	private String email;
	
	public UserId() {
	}

	public String getSuperApp() {
		return superApp;
	}

	public void setSuperApp(String superApp) {
		this.superApp = superApp;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Override
	public String toString() {
		return "UserId [superApp=" + superApp + ", email=" + email + "]";
	}
	
	

}
