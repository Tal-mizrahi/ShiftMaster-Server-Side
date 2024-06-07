package demo.services;

import java.util.List;
import java.util.Optional;

import demo.boundaries.ObjectBoundary;

public interface ObjectService {

	public ObjectBoundary createObject(ObjectBoundary boundary);
	
	public void updateObject(String objectId, String superapp, ObjectBoundary boundary, String userSuperapp, String email);
	
	public Optional<ObjectBoundary> getObjectById(String objectId, String superapp, String userSuperapp, String email);
	
	public List<ObjectBoundary> getAllObjects (String userSuperapp, String email, int size, int page);
}
 