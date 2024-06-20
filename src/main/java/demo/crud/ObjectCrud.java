package demo.crud;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.geo.Circle;
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
	
	@Query("SELECT obj FROM ObjectEntity obj "
			+ "WHERE "
			+ "(:distanceUnits"
			+                    "*DEGREES(ACOS(COS(RADIANS(:lat))"
			+                    "*COS(RADIANS(obj.lat))"
			+                    "*COS(RADIANS(:lng - obj.lng))"
			+                    "+SIN(RADIANS(:lat))"
			+                   "*SIN(RADIANS(obj.lat)))))"
			+ " <= :radius")
	public List<ObjectEntity> findAllByLocationRadius(@Param("lat") double lat, @Param("lng") double lng, @Param("radius") double radius, @Param("distanceUnits") double distanceUnits, Pageable pageable);
	
	@Query("SELECT obj FROM ObjectEntity obj "
			+ "WHERE "
			+ ":distanceUnits"
			+                    "*DEGREES(ACOS(COS(RADIANS(:lat))"
			+                    "*COS(RADIANS(obj.lat))"
			+                    "*COS(RADIANS(:lng - obj.lng))"
			+                    "+SIN(RADIANS(:lat))"
			+                   "*SIN(RADIANS(obj.lat))))"
			+ " <= :radius"
			+ " and obj.active=true")
	public List<ObjectEntity> findAllByLocationRadiusAndActiveTrue(@Param("lat") double lat, @Param("lng") double lng, @Param("radius") double radius, @Param("distanceUnits") double distanceUnits, Pageable pageable);
}
