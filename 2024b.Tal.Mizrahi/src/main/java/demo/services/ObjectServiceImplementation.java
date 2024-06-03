package demo.services;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import demo.boundaries.ObjectBoundary;
import demo.converters.ObjectConverter;
import demo.crud.ObjectCrud;
import demo.entities.ObjectEntity;
import demo.objects.InputValidation;
import demo.objects.ObjectId;

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
	@Transactional(readOnly = false)
	public ObjectBoundary createObject(ObjectBoundary boundary) {
		boundary.setCreationTimesTamp(new Date());
		boundary.setObjectId(new ObjectId(springApplicationName, UUID.randomUUID().toString()));

		if (boundary.getActive() == null) { // If active is null, then we set by default active : true 
			boundary.setActive(true);
		}

		if (boundary.getType() == null || boundary.getType().isBlank()) {
			throw new BadInputException("Object type must be not null or empt!");
		}

		if (boundary.getAlias() == null || boundary.getAlias().isBlank()) {

			throw new BadInputException("Object alias must be not null or empty!");
		}

		if (boundary.getCreatedBy() == null || boundary.getCreatedBy().getUserId() == null
				|| boundary.getCreatedBy().getUserId().getSuperApp() == null
				|| boundary.getCreatedBy().getUserId().getSuperApp().isBlank()
				|| !InputValidation.isValidEmail(boundary.getCreatedBy().getUserId().getEmail())) {
			throw new BadInputException("You must specify the superapp name and the email!");
		}
		ObjectEntity entity = objectConverter.toEntity(boundary);
		entity = objectCrud.save(entity);
		System.err.println("Saved in DB the object: " + entity);
		return objectConverter.toBoundary(entity);
	}

	@Override
	@Transactional(readOnly = false)
	public void updateObject(String objectId, String superapp, ObjectBoundary boundary) {
		String id = superapp + "#" + objectId;
		ObjectEntity entity = this.objectCrud.findById(id).orElseThrow(() -> new NotFoundException(
				"ObjectEntity with id: " + objectId + " and superapp " + superapp + " Does not exist in database"));

		if (boundary.getType() != null && !boundary.getType().isBlank())
			entity.setType(boundary.getType());

		if (boundary.getAlias() != null && !boundary.getAlias().isBlank())
			entity.setAlias(boundary.getAlias());

		if (boundary.getActive() != null)
			entity.setActive(boundary.getActive());

		if (boundary.getObjectDetails() != null)
			entity.setObjectDetails(boundary.getObjectDetails());

		if (boundary.getLocation() != null && boundary.getLocation().getLat() != null
				&& boundary.getLocation().getLng() != null) {

			String location = boundary.getLocation().getLat() + "#" + boundary.getLocation().getLng();
			entity.setLocation(location);
		}

		entity = this.objectCrud.save(entity);

		System.err.println("updated in database: " + entity);

	}

	@Override
	@Transactional(readOnly = true)
	public Optional<ObjectBoundary> getObjectById(String objectId, String superapp) {
		String id = superapp + "#" + objectId;
		Optional<ObjectEntity> optionalEntity = this.objectCrud.findById(id);

		if (optionalEntity.isEmpty()) {
			throw new NotFoundException("ObjectEntity with id: " + objectId + " and superapp name: " + superapp
					+ " Does not exist in database");
		}

		return optionalEntity.map(this.objectConverter::toBoundary);
	}

	@Override
	@Transactional(readOnly = true)
	public List<ObjectBoundary> getAllObjects() {
		return this.objectCrud.findAll() // List<ObjectEntity>
				.stream() // Stream<ObjectEntity>
				.peek(entity -> System.err.println("* " + entity)) // Prints all items.
				.map(this.objectConverter::toBoundary) // Stream<ObjectBoundary>
				.toList(); // List<ObjectBoundary>
	}

}
