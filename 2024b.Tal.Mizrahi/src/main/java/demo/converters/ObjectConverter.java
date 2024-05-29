package demo.converters;

import java.util.Date;
import java.util.HashMap;
import java.util.UUID;

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
		
		entity.setObjectId(boundary.getObjectId().getSuperApp() 
				   + "#" 
				   + boundary.getObjectId().getId());

		entity.setCreationTimesTamp(boundary.getCreationTimesTamp());
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
		entity.setCreatedBy(createdBy);
		
		if (
				boundary.getLocation() == null
				|| boundary.getLocation().getLat() == null
				|| boundary.getLocation().getLng() == null
				) {
			entity.setLocation("0.0#0.0");
		} else {
			entity.setLocation(
					boundary.getLocation().getLat()
					+ "#" 
					+ boundary.getLocation().getLng());
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
