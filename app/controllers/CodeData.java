package controllers;

import play.data.validation.Constraints;

/**
 * A form processing DTO that maps to the CodeData form.
 *
 * Using a class specifically for form binding reduces the chances
 * of a parameter tampering attack and makes code clearer, because
 * you can define constraints against the class.
 */
public class CodeData {

	/* Username as email */ 
	@Constraints.Required
	@Constraints.Email
	private String username;
	
	/* ValidationCode */ 
	@Constraints.Required
	private String code;

	public CodeData() {
    }

	public String getUsername() {
		return this.username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getCode() {
		return this.code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public int getCodeAsInt() {
		return Integer.parseInt(this.code);
	}
	
}
