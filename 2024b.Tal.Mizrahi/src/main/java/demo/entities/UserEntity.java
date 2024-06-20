package demo.entities;

import demo.objects.RolesEnum;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "USERS_TABLE")
public class UserEntity {

	@Id
	private String userId;
    private String username;
    
    @Enumerated(EnumType.STRING)
	private RolesEnum role;
    
	private String avatar; 

	public UserEntity() {
	} 
	
	
	public String getUserId() {
	return userId;
}

public void setUserId(String userId) {
	this.userId = userId;
}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public RolesEnum getRole() {
		return role;
	}

	public void setRole(RolesEnum role) {
		this.role = role;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	@Override
	public String toString() {
		return "UserEntity [userId=" + userId 
				+ ", username=" + username 
				+ ", role=" + role 
				+ ", avatar=" + avatar + "]";
	}
	
}
