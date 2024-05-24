package demo.converters;

import org.springframework.stereotype.Component;

import demo.CreatedBy;
import demo.Location;
import demo.ObjectId;
import demo.UserId;
import demo.boundaries.ObjectBoundary;
import demo.entities.ObjectEntity;

@Component
public class ObjectConverter {

	public ObjectEntity toEntity(ObjectBoundary boundary) {
		ObjectEntity entity = new ObjectEntity();

		entity.setType(boundary.getType());
		entity.setAlias(boundary.getAlias());
		String createdBy = boundary
				.getCreatedBy()
				.getUserId()
				.getSuperApp()
				+ "#" 
				+ boundary
				.getCreatedBy()
				.getUserId()
				.getEmail();
		String lat = boundary.getLocation().getLat() == null ? "0" :  boundary.getLocation().getLat().toString();
		String lng = boundary.getLocation().getLng() == null ? "0" :  boundary.getLocation().getLng().toString();
		entity.setLocation(lat + "#" + lng);
		entity.setActive(boundary.getActive());
		entity.setCreatedBy(createdBy);
		entity.setObjectDetails(boundary.getObjectDetails());
		
		return entity;
	}

	public ObjectBoundary toBoundary(ObjectEntity entity) {
		ObjectBoundary boundary = new ObjectBoundary();
		
		String[] id = entity.getObjectId().split("#");
		boundary.setObjectId(new ObjectId(id[0], id[1]));
		System.err.println(id);
		boundary.setType(entity.getType());
		boundary.setAlias(entity.getAlias());
		String[] location = entity.getLocation().split("#");
		boundary.setLocation(new Location(Double.parseDouble(location[0]) 
				, Double.parseDouble(location[1])));
		boundary.setActive(entity.getActive());
		String[] createdBy = entity.getCreatedBy().split("#");
		boundary.setCreatedBy(new CreatedBy(new UserId(createdBy[0], createdBy[1])));
		boundary.setCreationTimesTamp(entity.getCreationTimesTamp());
		boundary.setObjectDetails(entity.getObjectDetails());
		return boundary;
	}

}
