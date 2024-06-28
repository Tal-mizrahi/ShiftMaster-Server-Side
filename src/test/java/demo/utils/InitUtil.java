package demo.utils;

import java.util.Map;

import demo.boundaries.MiniAppCommandBoundary;
import demo.boundaries.NewUserBoundary;
import demo.boundaries.ObjectBoundary;
import demo.objects.CreatedBy;
import demo.objects.InvokedBy;
import demo.objects.Location;
import demo.objects.ObjectId;
import demo.objects.RolesEnum;
import demo.objects.TargetObject;
import demo.objects.UserId;

public class InitUtil {
	
	public NewUserBoundary createNewUser(
			String email,
			RolesEnum role,
			String username,
			String avatar) {
		return new NewUserBoundary(email, role, username, avatar);
	}
	
	public ObjectBoundary createNewObject(
			String type,
		    String alias,
		    Location location,
		    Boolean active,
		    UserId userId,
		    Map<String, Object> objectDetails) {
		
		return new ObjectBoundary(type, alias, location, active, new CreatedBy(userId), objectDetails);
	}
	
	public MiniAppCommandBoundary createNewCommand(
			String command,
			ObjectId objectId,
			UserId userId,
			Map<String, Object> commandAttribute) {
		return new MiniAppCommandBoundary(command, new TargetObject(objectId), new InvokedBy(userId), commandAttribute);
	}


}
