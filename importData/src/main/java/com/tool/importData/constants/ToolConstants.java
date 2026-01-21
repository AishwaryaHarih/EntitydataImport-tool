package com.tool.importData.constants;

public class ToolConstants {
	public static final String COUNTRY = "Country";
	public static final String COUNTRY_NAME = "countryName";
	public static final String ISO_CODE = "isoCode";
	public static final String POPULATION = "population";
	public static final String REQUIRED_COUNTRY_FIELDS_MISSING = "The fields- countryName,isoCode, population are required";
	public static final String ISO_CODE_VALIDATION = "The ISO Code must be of two letter alphabets, other formats are not allowed";
	public static final int MAX_LENGTH_COUNTRY_NAME = 100;
	public static final String INVALID_COUNTRY_NAME = "The Entered country name is invalid, Please enter the valid Country name";
	public static final String POPULATION_MUST_BE_POSITIVE_INTEGER = "The country population must be a positive Integer ";
	public static final String ENTITY_NOT_FOUND = "No processor found for the entity: ";
	public static final String FAILED_IMPORT = "Import failed: ";
	public static final String JOB_NOT_FOUND = "Import job not found: ";
	public static final String USER = "user";

	public static final String USERNAME = "username";
	public static final String EMAIL = "email";
	public static final String ROLE = "role";
	public static final String ACTIVE = "active";

	public static final int MAX_LENGTH_USERNAME = 50;

	public static final String REQUIRED_USER_FIELDS_MISSING = "Required user fields are missing";

	public static final String INVALID_USERNAME = "Invalid username";

	public static final String INVALID_EMAIL = "Invalid email format";

	public static final String INVALID_ROLE = "Invalid role value";
	
	public static final String ACTIVE_MUST_BE_TRUEORFALSE = "Active must be true or false";
	
	public static final String USER_ALREADY_EXISTS = "User already exists with email: ";
}
