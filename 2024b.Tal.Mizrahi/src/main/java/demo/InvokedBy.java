package demo;

public class InvokedBy {
	
	private UserId userId;

	public InvokedBy() {
		
	}

	public UserId getUserId() {
		return userId;
	}

	public void setUserId(UserId userId) {
		this.userId = userId;
	}

	@Override
	public String toString() {
		return "InvokedBy [userId=" + userId + "]";
	}
	

}
