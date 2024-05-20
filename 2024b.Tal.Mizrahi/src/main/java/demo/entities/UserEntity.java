package demo.entities;

import jakarta.persistence.Id;

import demo.RolesEnum;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "USER_TABLE")
public class UserEntity {

    
	@Id private String id;
    private String userName;

	private RolesEnum role;
    private String avatar;

	public UserEntity() {
	}
	
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
}
