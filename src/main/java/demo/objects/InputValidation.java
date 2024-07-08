package demo.objects;

import java.util.regex.Pattern;

import demo.boundaries.MiniAppCommandBoundary;
import demo.services.exceptions.BadRequestException;

public class InputValidation {

	public InputValidation() {
	}

	public static boolean isValidEmail(String email) {
		if(email == null || email.isBlank())
			return false;
		String emailReg = "^[a-zA-Z]+[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
		Pattern pattern = Pattern.compile(emailReg);
		return pattern.matcher(email).matches(); // Return true if the email is valid, false if invalid
	}

	public static boolean isValidRole(String role) {
		for (RolesEnum r : RolesEnum.values())
			if (r.name().equals(role))
				return true;
		return false;

	}

	public static void checkIfValidMiniappInput(MiniAppCommandBoundary boundary) {
		if (boundary.getInvokedBy() == null || boundary.getInvokedBy().getUserId() == null
				|| boundary.getInvokedBy().getUserId().getSuperapp() == null
				|| boundary.getInvokedBy().getUserId().getEmail() == null) {
			throw new BadRequestException(
					"You must enter who invoked the command by " + "giving the superapp name and valid email!");
		}
		
		if (boundary.getTargetObject() == null 
				|| boundary.getTargetObject().getObjectId() == null
				|| boundary.getTargetObject().getObjectId().getSuperapp() == null
				|| boundary.getTargetObject().getObjectId().getId() == null) {
			throw new BadRequestException("You must enter target object!");
		}
		
		if (boundary.getCommand() == null 
				|| boundary.getCommand().isBlank()) {
			throw new BadRequestException("You must enter command");
		}
	
	}
}
