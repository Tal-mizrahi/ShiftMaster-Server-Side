package demo.converters;

import java.util.HashMap;
import org.springframework.stereotype.Component;

import demo.boundaries.ObjectBoundary;
import demo.entities.ObjectEntity;
import demo.objects.CreatedBy;
import demo.objects.Location;
import demo.objects.ObjectId;
import demo.objects.UserId;

@Component
public class ObjectConverter {

	public ObjectEntity toEntity(ObjectBoundary boundary) {
		ObjectEntity entity = new ObjectEntity();

		entity.setObjectId(
				boundary.getObjectId().getSuperapp() 
				+ "#" 
				+ boundary.getObjectId().getId());

		entity.setCreationTimestamp(boundary.getCreationTimestamp());
		entity.setType(boundary.getType());
		entity.setAlias(boundary.getAlias());
		
		String createdBy = boundary.getCreatedBy().getUserId().getSuperapp() 
						   + "#"
						   + boundary.getCreatedBy().getUserId().getEmail();
		entity.setCreatedBy(createdBy);

		if (boundary.getLocation() == null || boundary.getLocation().getLat() == null
				|| boundary.getLocation().getLng() == null) {
			entity.setLat(32.115139);
			entity.setLng(34.817804);
		} else {
			entity.setLat(boundary.getLocation().getLat());
			entity.setLng(boundary.getLocation().getLng());
		}

		entity.setActive(boundary.getActive());
		if (boundary.getObjectDetails() != null)
			entity.setObjectDetails(boundary.getObjectDetails());
		else
			entity.setObjectDetails(new HashMap<>());
		return entity;
	}

	public ObjectBoundary toBoundary(ObjectEntity entity) {
		ObjectBoundary boundary = new ObjectBoundary();

		String[] id = entity.getObjectId().split("#");
		boundary.setObjectId(new ObjectId(id[0], id[1]));
		boundary.setType(entity.getType());
		boundary.setAlias(entity.getAlias());
		boundary.setLocation(new Location(entity.getLat(), entity.getLng()));
		boundary.setActive(entity.getActive());
		String[] createdBy = entity.getCreatedBy().split("#");
		boundary.setCreatedBy(new CreatedBy(new UserId(createdBy[0], createdBy[1])));
		boundary.setCreationTimestamp(entity.getCreationTimestamp());
		boundary.setObjectDetails(entity.getObjectDetails());
		return boundary;
	}

}
