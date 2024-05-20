package demo.crud;

import org.springframework.data.jpa.repository.JpaRepository;

import demo.entities.UserEntity;

public interface UserCrud extends JpaRepository<UserEntity, String> {

}
