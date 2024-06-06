package demo.crud;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import demo.entities.ObjectEntity;

public interface ObjectCrud extends JpaRepository<ObjectEntity, String> {
	
	// SELECT * FROM OBJECT_TABLE WHERE objectId  = ? AND active = ?
	public Optional<ObjectEntity> findByObjectIdAndActive(@Param("objectId") String objectId, @Param("active") boolean active);
	
	public List<ObjectEntity> findAllByActive(@Param("active") boolean active, Pageable pageable);
}  
