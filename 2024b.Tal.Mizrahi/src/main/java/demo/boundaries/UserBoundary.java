package demo.boundaries;

import demo.objects.RolesEnum;
import demo.objects.UserId;

public class UserBoundary {
	
	private UserId userId;
	private RolesEnum role;
	private String username;
	private String avatar;	

	public UserBoundary() {}
	
	public UserId getUserId() {
		return userId;
	}

	public void setUserId(UserId userId) {
		this.userId = userId;
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
		return "UserBoundary [userId=" + userId 
				+ ", role=" + role 
				+ ", username=" + username 
				+ ", avatar=" + avatar
				+ "]";
	}
	
	
	
	
	
}
