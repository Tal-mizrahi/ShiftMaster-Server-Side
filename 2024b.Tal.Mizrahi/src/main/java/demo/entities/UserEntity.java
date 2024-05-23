package demo.entities;

import jakarta.persistence.Id;

import demo.RolesEnum;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "USER_TABLE")
public class UserEntity {

	@Id private String email; 
	//private String superApp; we need to choose if to save it to the DB
    private String userName;
	private RolesEnum role;
    private String avatar; // need to choose if to save it to the DB

	public UserEntity() {
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
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
		return "UserEntity [email=" + email 
				+ ", userName=" + userName 
				+ ", role=" + role 
				+ ", avatar=" + avatar + "]";
	}

	
    
    
}
