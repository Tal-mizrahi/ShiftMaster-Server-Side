package demo.crud;

import org.springframework.data.jpa.repository.JpaRepository;

import demo.entities.CommandEntity;

public interface CommandCrud extends JpaRepository<CommandEntity, String>{

}
