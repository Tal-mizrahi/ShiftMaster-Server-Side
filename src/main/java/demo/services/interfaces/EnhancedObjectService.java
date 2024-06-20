package demo.services.interfaces;

import java.util.List;
import java.util.Optional;

import demo.boundaries.ObjectBoundary;

public interface EnhancedObjectService extends ObjectService {

	public void updateObject(String objectId, String superapp, ObjectBoundary boundary, String userSuperapp, String email);
	
	public Optional<ObjectBoundary> getObjectById(String objectId, String superapp, String userSuperapp, String email);
	
	public List<ObjectBoundary> getAllObjects (String userSuperapp, String email, int size, int page);

	public List<ObjectBoundary> getAllObjectsByType (String type, String userSuperapp, String email, int size, int page);
	
	public List<ObjectBoundary> getAllObjectsByAlias (String alias, String userSuperapp, String email, int size, int page);

	public List<ObjectBoundary> getAllObjectsByAliasPattern (String pattern, String userSuperapp, String email, int size, int page);
	
	public List<ObjectBoundary> getAllObjectsByLocationRadius (double lat, double lng, double distance,String distanceUnits, String userSuperapp, String email, int size, int page);
	
}
 