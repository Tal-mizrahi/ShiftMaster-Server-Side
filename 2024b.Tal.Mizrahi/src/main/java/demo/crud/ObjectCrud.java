package demo.crud;

import org.springframework.data.jpa.repository.JpaRepository;

import demo.entities.ObjectEntity;

public interface ObjectCrud extends JpaRepository<ObjectEntity, String> {
	
}  
