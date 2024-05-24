package demo.services;

import java.util.List;
import java.util.Optional;

import demo.boundaries.ObjectBoundary;

public interface ObjectService {

	public ObjectBoundary createObject(ObjectBoundary boundary);
	
	public void updateObject(String objectId, ObjectBoundary boundary);
	
	public Optional<ObjectBoundary> getObjectById(String objectId);
	
	public List<ObjectBoundary> getAllObjects ();
	
}
