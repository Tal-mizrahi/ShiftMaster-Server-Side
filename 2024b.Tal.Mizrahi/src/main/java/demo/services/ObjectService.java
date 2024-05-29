package demo.services;

import java.util.List;
import java.util.Optional;

import demo.boundaries.ObjectBoundary;

public interface ObjectService {

	public ObjectBoundary createObject(ObjectBoundary boundary);
	
	public void updateObject(String objectId, String superapp, ObjectBoundary boundary);
	
	public Optional<ObjectBoundary> getObjectById(String objectId, String superapp);
	
	public List<ObjectBoundary> getAllObjects ();
	
}
 