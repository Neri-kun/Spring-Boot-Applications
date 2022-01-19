package com.libraryapp.searchcriteria;

import java.util.Optional;

/*import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
@Builder*/
public class UserSearchCriteria {
	
	private Optional<String> firstName;
	private Optional<String> lastName;
	private Optional<String> email;
	private Optional<String> phoneNumber;
	public Optional<String> getFirstName() {
		return firstName;
	}
	public void setFirstName(Optional<String> firstName) {
		this.firstName = firstName;
	}
	public Optional<String> getLastName() {
		return lastName;
	}
	public void setLastName(Optional<String> lastName) {
		this.lastName = lastName;
	}
	public Optional<String> getEmail() {
		return email;
	}
	public void setEmail(Optional<String> email) {
		this.email = email;
	}
	public Optional<String> getPhoneNumber() {
		return phoneNumber;
	}
	public void setPhoneNumber(Optional<String> phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	
	
}
