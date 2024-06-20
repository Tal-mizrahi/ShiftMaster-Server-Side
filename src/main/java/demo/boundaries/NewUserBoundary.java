package demo.boundaries;

import demo.objects.RolesEnum;

public class NewUserBoundary {
	
	private String email;
	private RolesEnum role;
	private String username;
	private String avatar;
	
	public NewUserBoundary() {

	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public RolesEnum getRole() {
		return role;
	}

	public void setRole(RolesEnum role) {
		this.role = role;
	}

	public String getUsername() {
		return username;
	} 

	public void setUsername(String username) {
		this.username = username;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}
	

	@Override
	public String toString() {
		return "NewUserBoundary [email=" + email +
				", role=" + role + 
				", username=" + username 
				+ ", avatar=" + avatar
				+ "]";
	}
	
	
	
}
