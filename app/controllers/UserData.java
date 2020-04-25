package controllers;

import play.data.validation.Constraints;

/**
 * A form processing DTO that maps to the UserData form.
 *
 * Using a class specifically for form binding reduces the chances
 * of a parameter tampering attack and makes code clearer, because
 * you can define constraints against the class.
 */
public class UserData {

	/* Username as email */ 
	@Constraints.Required
	@Constraints.Email
    private String username;

    public UserData() {
    }

	public String getUsername() {
		return this.username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

}
