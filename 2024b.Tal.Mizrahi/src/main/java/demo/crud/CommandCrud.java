package demo.crud;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import demo.entities.CommandEntity;

public interface CommandCrud extends JpaRepository<CommandEntity, String>{
	
	// SELECT * FROM COMMAND_TABLE WHERE miniAppName  = ?
	public List<CommandEntity> findAllByMiniAppName(@Param("miniAppName") String miniAppName);

} 
