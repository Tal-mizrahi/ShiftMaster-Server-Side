package demo.services;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.geo.Metrics;
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
import demo.services.exceptions.BadRequestException;
import demo.services.exceptions.ForbiddenException;
import demo.services.exceptions.NotFoundException;
import demo.services.interfaces.EnhancedObjectService;

@Service
public class ObjectServiceImplementation implements EnhancedObjectService {
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
	@Deprecated
	public void updateObject(String objectId, String superapp, ObjectBoundary boundary) {
		throw new BadRequestException("This operation is deprecated");
//		String id = superapp + "#" + objectId;
//		ObjectEntity entity = this.objectCrud.findById(id).orElseThrow(() -> new NotFoundException(
//				"ObjectEntity with id: " + objectId + " and superapp " + superapp + " Does not exist in database"));
//
//		if (boundary.getType() != null && !boundary.getType().isBlank())
//			entity.setType(boundary.getType());
//
//		if (boundary.getAlias() != null && !boundary.getAlias().isBlank())
//			entity.setAlias(boundary.getAlias());
//
//		if (boundary.getActive() != null)
//			entity.setActive(boundary.getActive());
//
//		if (boundary.getObjectDetails() != null)
//			entity.setObjectDetails(boundary.getObjectDetails());
//
//		if (boundary.getLocation() != null && boundary.getLocation().getLat() != null
//				&& boundary.getLocation().getLng() != null) {
//
//			String location = boundary.getLocation().getLat() + "#" + boundary.getLocation().getLng();
//			entity.setLocation(location);
//		}
//
//		entity = this.objectCrud.save(entity);
//
//		System.err.println("updated in database: " + entity);

	}

	@Override
	@Deprecated
	public Optional<ObjectBoundary> getObjectById(String objectId, String superapp) {
		throw new BadRequestException("This operation is deprecated");
//		String id = superapp + "#" + objectId;
//		Optional<ObjectEntity> optionalEntity = this.objectCrud.findById(id);
//
//		if (optionalEntity.isEmpty()) {
//			throw new NotFoundException("ObjectEntity with id: " + objectId + " and superapp name: " + superapp
//					+ " Does not exist in database");
//		}
//
//		return optionalEntity.map(this.objectConverter::toBoundary);
	}

	@Override
	@Deprecated
	public List<ObjectBoundary> getAllObjects() {
		throw new BadRequestException("This operation is deprecated");
//		return this.objectCrud.findAll() // List<ObjectEntity>
//				.stream() // Stream<ObjectEntity>
//				.peek(entity -> System.err.println("* " + entity)) // Prints all items.
//				.map(this.objectConverter::toBoundary) // Stream<ObjectBoundary>
//				.toList(); // List<ObjectBoundary>
	}

	@Override
	@Transactional(readOnly = false)
	public ObjectBoundary createObject(ObjectBoundary boundary) {

		if (boundary.getCreatedBy() == null || boundary.getCreatedBy().getUserId() == null
				|| boundary.getCreatedBy().getUserId().getSuperapp() == null
				|| boundary.getCreatedBy().getUserId().getEmail() == null) {
			throw new BadRequestException("You must specify the superapp name and the email!");
		}

		if (getUserRole(boundary.getCreatedBy().getUserId()) != RolesEnum.SUPERAPP_USER)
			throw new ForbiddenException("Only user with SUPERAPP_USER role can create an object!");

		boundary.setCreationTimestamp(new Date());
		boundary.setObjectId(new ObjectId(springApplicationName, UUID.randomUUID().toString()));

		if (boundary.getActive() == null) { // If active is null, then we set by default active : true
			boundary.setActive(true);
		}

		if (boundary.getType() == null || boundary.getType().isBlank()) {
			throw new BadRequestException("Object type must be not null or empt!");
		}

		if (boundary.getAlias() == null || boundary.getAlias().isBlank()) {

			throw new BadRequestException("Object alias must be not null or empty!");
		}

		ObjectEntity entity = objectConverter.toEntity(boundary);
		entity = objectCrud.save(entity);
		System.err.println("Saved in DB the object: " + entity);
		return objectConverter.toBoundary(entity);
	}

	@Override
	@Transactional(readOnly = false)
	public void updateObject(String objectId, String superapp, ObjectBoundary boundary, String userSuperapp,
			String email) {
		String id = superapp + "#" + objectId;
		if (getUserRole(new UserId(userSuperapp, email)) != RolesEnum.SUPERAPP_USER)
			throw new ForbiddenException("Only user with SUPERAPP_USER role can update an object!");
		
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
			entity.setLat(boundary.getLocation().getLat());
			entity.setLng(boundary.getLocation().getLng());
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
			optionalEntity = this.objectCrud.findByObjectIdAndActiveTrue(id);

		return optionalEntity.map(this.objectConverter::toBoundary);
	}

	@Override
	@Transactional(readOnly = true)
	public List<ObjectBoundary> getAllObjects(String userSuperapp, String email, int size, int page) {
		List<ObjectEntity> allEntities;
		if (getUserRole(new UserId(userSuperapp, email)) == RolesEnum.SUPERAPP_USER)
			allEntities = objectCrud
					.findAll(PageRequest.of(page, size, Direction.DESC, "creationTimestamp", "objectId")).toList();
		else
			allEntities = objectCrud
					.findAllByActiveTrue(PageRequest.of(page, size, Direction.DESC, "creationTimestamp", "objectId"));
		// allEntities = objectCrud.findAllByActive(true, PageRequest.of(page, size,
		// Direction.DESC, "creationTimesTamp"));
		return allEntities // List<ObjectEntity>
				.stream() // Stream<ObjectEntity>
				.peek(entity -> System.err.println("* " + entity)) // Prints all items.
				.map(this.objectConverter::toBoundary) // Stream<ObjectBoundary>
				.toList(); // List<ObjectBoundary>
	}

	@Override
	@Transactional(readOnly = true)
	public List<ObjectBoundary> getAllObjectsByType(String type, String userSuperapp, String email, int size,
			int page) {

		List<ObjectEntity> allEntities;
		if (getUserRole(new UserId(userSuperapp, email)) == RolesEnum.SUPERAPP_USER)
			allEntities = objectCrud.findAllByType(type,
					PageRequest.of(page, size, Direction.DESC, "creationTimestamp", "objectId"));
		else
			allEntities = objectCrud.findAllByTypeAndActiveTrue(type,
					PageRequest.of(page, size, Direction.DESC, "creationTimestamp", "objectId"));

		System.err.println(allEntities);
		return allEntities // List<ObjectEntity>
				.stream() // Stream<ObjectEntity>
				.peek(entity -> System.err.println("* " + entity)) // Prints all items.
				.map(this.objectConverter::toBoundary) // Stream<ObjectBoundary>
				.toList(); // List<ObjectBoundary>

	}

	@Override
	@Transactional(readOnly = true)
	public List<ObjectBoundary> getAllObjectsByAlias(String alias, String userSuperapp, String email, int size,
			int page) {

		List<ObjectEntity> allEntities;
		if (getUserRole(new UserId(userSuperapp, email)) == RolesEnum.SUPERAPP_USER)
			allEntities = objectCrud.findAllByAlias(alias,
					PageRequest.of(page, size, Direction.DESC, "creationTimestamp", "objectId"));
		else
			allEntities = objectCrud.findAllByAliasAndActiveTrue(alias,
					PageRequest.of(page, size, Direction.DESC, "creationTimestamp", "objectId"));
		return allEntities // List<ObjectEntity>
				.stream() // Stream<ObjectEntity>
				.peek(entity -> System.err.println("* " + entity)) // Prints all items.
				.map(this.objectConverter::toBoundary) // Stream<ObjectBoundary>
				.toList(); // List<ObjectBoundary>
	}

	@Override
	@Transactional(readOnly = true)
	public List<ObjectBoundary> getAllObjectsByAliasPattern(String pattern, String userSuperapp, String email, int size,
			int page) {

		List<ObjectEntity> allEntities;
		if (getUserRole(new UserId(userSuperapp, email)) == RolesEnum.SUPERAPP_USER)
			allEntities = objectCrud.findAllByAliasLikeIgnoreCase(pattern,
					PageRequest.of(page, size, Direction.DESC, "creationTimestamp", "objectId"));
		else
			allEntities = objectCrud.findAllByAliasLikeIgnoreCaseAndActiveTrue(pattern,
					PageRequest.of(page, size, Direction.DESC, "creationTimestamp", "objectId"));
		return allEntities // List<ObjectEntity>
				.stream() // Stream<ObjectEntity>
				.peek(entity -> System.err.println("* " + entity)) // Prints all items.
				.map(this.objectConverter::toBoundary) // Stream<ObjectBoundary>
				.toList(); // List<ObjectBoundary>
	}

	@Override
	@Transactional(readOnly = true)
	public List<ObjectBoundary> getAllObjectsByLocationRadius(double lat, double lng, double distance,
			String distanceUnits, String userSuperapp, String email, int size, int page) {
		List<ObjectEntity> allEntities;

		double numericUnit = getNumericDistanceUnit(distanceUnits);

		if (getUserRole(new UserId(userSuperapp, email)) == RolesEnum.SUPERAPP_USER)
			allEntities = objectCrud.findAllByLocationRadius(lat, lng, distance, numericUnit,
					PageRequest.of(page, size, Direction.DESC, "creationTimestamp", "objectId"));
		else
			allEntities = objectCrud.findAllByLocationRadiusAndActiveTrue(lat, lng, distance, numericUnit,
					PageRequest.of(page, size, Direction.DESC, "creationTimestamp", "objectId"));
		return allEntities // List<ObjectEntity>
				.stream() // Stream<ObjectEntity>
				.peek(entity -> System.err.println("* " + entity)) // Prints all items.
				.map(this.objectConverter::toBoundary) // Stream<ObjectBoundary>
				.toList(); // List<ObjectBoundary>
	}

	public RolesEnum getUserRole(UserId userId) {
		String id = userId.getSuperapp() + "#" + userId.getEmail();
		UserEntity entity = this.userCrud.findById(id).orElseThrow(() -> new NotFoundException("UserEntity with email: "
				+ userId.getEmail() + " and superapp " + userId.getSuperapp() + " Does not exist in database"));
		if (entity.getRole() == RolesEnum.ADMIN)
			throw new ForbiddenException("ADMIN users are not allowed!");
		return entity.getRole();
	}

	public double getNumericDistanceUnit(String distanceUnits) {

		if (distanceUnits == null)
			return Metrics.NEUTRAL.getMultiplier();
		Metrics unit = null;
		for (Metrics metUnit : Metrics.values())
			if (metUnit.name().equalsIgnoreCase(distanceUnits))
				unit = metUnit;
		if (unit == null)
			throw new BadRequestException("Distance unit " + distanceUnits + " is invalid!\n");
		return unit.getMultiplier();
	}

}
