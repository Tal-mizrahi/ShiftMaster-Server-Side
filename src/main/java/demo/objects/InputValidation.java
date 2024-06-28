package demo.objects;

import java.util.regex.Pattern;

public class InputValidation {

	public InputValidation() {
	}

	public static boolean isValidEmail(String email) {
		if(email == null)
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

}
