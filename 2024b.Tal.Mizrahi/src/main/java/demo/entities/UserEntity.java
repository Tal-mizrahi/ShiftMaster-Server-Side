package demo.entities;

import jakarta.persistence.Id;

import demo.RolesEnum;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "USER_TABLE")
public class UserEntity {

	@Id private String userId; // need to check on Wednesday if it can be UserId object
    private String userName;
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

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
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
		return "UserEntity [userId=" + userId + ", userName=" + userName + ", role=" + role + ", avatar=" + avatar
				+ "]";
	}
    
    
}
