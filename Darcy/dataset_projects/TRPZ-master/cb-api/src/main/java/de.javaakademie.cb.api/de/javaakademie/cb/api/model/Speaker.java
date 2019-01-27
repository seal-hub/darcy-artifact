package de.javaakademie.cb.api.model;

/**
 * Speaker.
 * 
 * @author Guido.Oelmann
 */
public class Speaker {

	private String id;
	private String firstName;
	private String lastName;
	private String academicDegreeTitle;
	private String language;
	private String organization;
	private String biography;
	private String picture;

	public Speaker() {
	}

	public Speaker(String firstName, String lastName, String academicDegreeTitle, String language, String organization,
			String biography, String picture) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.academicDegreeTitle = academicDegreeTitle;
		this.language = language;
		this.organization = organization;
		this.biography = biography;
		this.picture = picture;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getAcademicDegreeTitle() {
		return academicDegreeTitle;
	}

	public void setAcademicDegreeTitle(String academicDegreeTitle) {
		this.academicDegreeTitle = academicDegreeTitle;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public String getOrganization() {
		return organization;
	}

	public void setOrganization(String organization) {
		this.organization = organization;
	}

	public String getBiography() {
		return biography;
	}

	public void setBiography(String biography) {
		this.biography = biography;
	}

	public String getPicture() {
		return picture;
	}

	public void setPicture(String picture) {
		this.picture = picture;
	}

	public String toString() {
		return "Speaker{" + "id='" + id + '\'' + ", firstName='" + firstName + ", lastName='" + lastName + '\'' + '\''
				+ ", biography='" + biography + '\'' + ", language='" + language + '\'' + ", picture='" + picture + '\''
				+ ", organization='" + organization + '\'' + '}';
	}
}