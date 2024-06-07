package demo.services;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import demo.boundaries.ObjectBoundary;
import demo.converters.ObjectConverter;
import demo.crud.ObjectCrud;
import demo.crud.UserCrud;
import demo.entities.ObjectEntity;
import demo.entities.UserEntity;
import demo.objects.ObjectId;
import demo.objects.RolesEnum;
import demo.objects.UserId;

@Service
public class ObjectServiceImplementation implements ObjectService {
	private ObjectCrud objectCrud;
	private UserCrud userCrud;
	private ObjectConverter objectConverter;
	private String springApplicationName;

	@Value("${spring.application.name:supperApp}")
	public void setSpringApplicationName(String springApplicationName) {
		this.springApplicationName = springApplicationName;
		System.err.println("The Spring Application name is: " + this.springApplicationName);
	}

	public ObjectServiceImplementation(ObjectCrud objectCrud, ObjectConverter objectConverter, UserCrud userCrud) {
		this.objectCrud = objectCrud;
		this.objectConverter = objectConverter;
		this.userCrud = userCrud;
	}

	@Override
	@Transactional(readOnly = false)
	public ObjectBoundary createObject(ObjectBoundary boundary) {
		
		if (boundary.getCreatedBy() == null || boundary.getCreatedBy().getUserId() == null
				|| boundary.getCreatedBy().getUserId().getSuperApp() == null
				|| boundary.getCreatedBy().getUserId().getEmail() == null) {
			throw new BadInputException("You must specify the superapp name and the email!");
		}
		
		if ( getUserRole(boundary.getCreatedBy().getUserId()) != RolesEnum.SUPERAPP_USER)
			throw new ForbiddenException("Only user with SUPERAPP_USER role can create an object!");
		
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

		ObjectEntity entity = objectConverter.toEntity(boundary);
		entity = objectCrud.save(entity);
		System.err.println("Saved in DB the object: " + entity);
		return objectConverter.toBoundary(entity);
	}

	@Override
	@Transactional(readOnly = false)
	public void updateObject(String objectId, String superapp, ObjectBoundary boundary, String userSuperapp, String email) {
		String id = superapp + "#" + objectId;
		ObjectEntity entity = this.objectCrud.findById(id).orElseThrow(() -> new NotFoundException(
				"ObjectEntity with id: " + objectId + " and superapp " + superapp + " Does not exist in database"));

		if ( getUserRole(boundary.getCreatedBy().getUserId()) != RolesEnum.SUPERAPP_USER)
			throw new ForbiddenException("Only user with SUPERAPP_USER role can update an object!");
		
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
	public Optional<ObjectBoundary> getObjectById(String objectId, String superapp, String userSuperapp, String email) {
		String id = superapp + "#" + objectId;
		Optional<ObjectEntity> optionalEntity;
		if (getUserRole(new UserId(userSuperapp, email)) == RolesEnum.SUPERAPP_USER)
			optionalEntity = this.objectCrud.findById(id);
		else // If the user is MINIAPP - get the object if active is true
			optionalEntity = this.objectCrud.findByObjectIdAndActive(id, true);
		
		return optionalEntity.map(this.objectConverter::toBoundary);
	}

	@Override
	@Transactional(readOnly = true)
	public List<ObjectBoundary> getAllObjects(String userSuperapp, String email, int size, int page) {
		List<ObjectEntity> allEntities;
		if(getUserRole(new UserId(userSuperapp, email)) == RolesEnum.SUPERAPP_USER)
			allEntities = objectCrud.findAll(PageRequest.of(page, size, Direction.DESC, "creationTimesTamp")).toList();
		else
			allEntities = objectCrud.findAllByActive(true, PageRequest.of(page, size, Direction.DESC, "creationTimesTamp"));
		return   allEntities // List<ObjectEntity>
				.stream() // Stream<ObjectEntity>
				.peek(entity -> System.err.println("* " + entity)) // Prints all items.
				.map(this.objectConverter::toBoundary) // Stream<ObjectBoundary>
				.toList(); // List<ObjectBoundary>
	}
	
	
	public RolesEnum getUserRole(UserId userId) {
		String id = userId.getSuperApp() 
				+ "#" 
				+ userId.getEmail();
		UserEntity entity = this.userCrud
				.findById(id)
				.orElseThrow(() -> new NotFoundException("UserEntity with email: " + userId.getEmail() 
				+ " and superapp " + userId.getSuperApp() + " Does not exist in database"));
		if (entity.getRole() == RolesEnum.ADMIN)
			throw new ForbiddenException("ADMIN users are not allowed!");
		return entity.getRole();
	}

}

