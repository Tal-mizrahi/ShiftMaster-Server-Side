package demo.services;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import demo.boundaries.ObjectBoundary;
import demo.converters.ObjectConverter;
import demo.crud.ObjectCrud;
import demo.entities.ObjectEntity;

@Service
public class ObjectServiceImplementation implements ObjectService {
	private ObjectCrud objectCrud;
	private ObjectConverter objectConverter;
	private String springApplicationName;

	@Value("${spring.application.name:supperApp}")
	public void setSpringApplicationName(String springApplicationName) {
		this.springApplicationName = springApplicationName;
		System.err.println("The Spring Application name is: " + this.springApplicationName);
	}

	public ObjectServiceImplementation(ObjectCrud objectCrud, ObjectConverter objectConverter) {
		this.objectCrud = objectCrud;
		this.objectConverter = objectConverter;
	}

	@Override
	public ObjectBoundary createObject(ObjectBoundary boundary) {
		ObjectEntity entity = objectConverter.toEntity(boundary);
		
		entity.setObjectId(springApplicationName 
						   + "#" 
						   + UUID.randomUUID().toString());
		
		entity = objectCrud.save(entity);
		System.err.println("Saved in DB the object: " + entity);
		return objectConverter.toBoundary(entity);
	}

	@Override
	public void updateObject(String objectId, ObjectBoundary boundary) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Optional<ObjectBoundary> getObjectById(String objectId) {
		// TODO Auto-generated method stub
		return Optional.empty();
	}

	@Override
	public List<ObjectBoundary> getAllObjects() {
		// TODO Auto-generated method stub
		return null;
	}

}
