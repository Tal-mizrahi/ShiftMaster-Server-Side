package demo.crud;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import demo.entities.ObjectEntity;

public interface ObjectCrud extends JpaRepository<ObjectEntity, String> {

	// SELECT * FROM OBJECT_TABLE WHERE objectId = ? AND active = ?
	public Optional<ObjectEntity> findByObjectIdAndActiveTrue(@Param("objectId") String objectId);

	public List<ObjectEntity> findAllByActiveTrue(Pageable pageable);

	public List<ObjectEntity> findAllByType(@Param("type") String type, Pageable pageable);

	public List<ObjectEntity> findAllByAlias(@Param("alias") String alias, Pageable pageable);

	public List<ObjectEntity> findAllByTypeAndActiveTrue(@Param("type") String type, Pageable pageable);

	public List<ObjectEntity> findAllByAliasAndActiveTrue(@Param("alias") String alias, Pageable pageable);
		
	public List<ObjectEntity> findAllByAliasLikeIgnoreCase(@Param("pattern") String pattern, Pageable pageable);
		
	public List<ObjectEntity> findAllByAliasLikeIgnoreCaseAndActiveTrue(@Param("pattern") String pattern, Pageable pageable);
	
	@Query(value = "SELECT * FROM objects_table obj " 
           + "WHERE ST_DWithin(ST_MakePoint(obj.lng, obj.lat)::geography, ST_MakePoint(:lng, :lat)::geography, :radius) "
			, nativeQuery = true)
	public List<ObjectEntity> findAllByLocationRadius(@Param("lat") double lat, @Param("lng") double lng, @Param("radius") double radius, Pageable pageable);
	
	@Query(value = "SELECT * FROM objects_table obj " 
			+ "WHERE ST_DWithin(ST_MakePoint(obj.lng, obj.lat)::geography, ST_MakePoint(:lng, :lat)::geography, :radius) "
			+ "AND obj.active=true"
			, nativeQuery = true)
	public List<ObjectEntity> findAllByLocationRadiusAndActiveTrue(@Param("lat") double lat, @Param("lng") double lng, @Param("radius") double radius, Pageable pageable);

	public List<ObjectEntity> findAllByCreatedByAndTypeAndAlias(@Param("createdBy") String createdBy, @Param("type") String type,@Param("alias") String alias, Pageable pageable);


}
