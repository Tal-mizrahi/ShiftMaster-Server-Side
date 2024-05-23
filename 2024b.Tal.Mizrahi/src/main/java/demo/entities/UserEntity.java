package demo.entities;

import demo.RolesEnum;
import demo.UserId;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;

@Entity
@Table(name = "USER_TABLE")
public class UserEntity {

	@EmbeddedId
	private UserId userId;
    private String userName;
    
    @Enumerated(EnumType.STRING)
	private RolesEnum role;
    
	private String avatar; // need to choose if to save it to the DB

	
	public UserEntity() {
	}

	public UserId getEmail() {
		return userId;
	}

	public void setUserId(UserId userId) {
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
		return "UserEntity [userId=" + userId 
				+ ", userName=" + userName 
				+ ", role=" + role 
				+ ", avatar=" + avatar + "]";
	}

	
    
    
}
