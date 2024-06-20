package demo.services.interfaces;

import java.util.List;
import java.util.Optional;

import demo.boundaries.ObjectBoundary;

public interface ObjectService {

	public ObjectBoundary createObject(ObjectBoundary boundary);
	
	@Deprecated
	public void updateObject(String objectId, String superapp, ObjectBoundary boundary);
	
	@Deprecated
	public Optional<ObjectBoundary> getObjectById(String objectId, String superapp);
	
	@Deprecated
	public List<ObjectBoundary> getAllObjects ();
	
}
 