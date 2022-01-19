package com.libraryapp.specifications;

import org.springframework.data.jpa.domain.Specification;

import com.libraryapp.entities.User;


public final class UserSpecifications {
	
	
	public static Specification<User> likeFirstName (String firstName){
		if(firstName == null) {
			return null;
		}
		
		return (root, query, cb) ->{
			return cb.like(root.get("firstName"),"%"+ firstName + "%");
		};
	}
	
	public static Specification<User> likeLastName(String lastName) {
		if (lastName == null) {
			return null;
		}
		return (root, query, cb) -> {
			return cb.like(root.get("lastName"),  lastName + "%");
		};
	}
	
	public static Specification<User> likeEmail(String email) {
		if (email == null) {
			return null;
		}
		return (root, query, cb) -> {
			return cb.like(root.get("email"),  email + "%");
		};
	}
	
	public static Specification<User> likePhoneNumber(String phoneNumber) {
		if (phoneNumber == null) {
			return null;
		}
		return (root, query, cb) -> {
			return cb.like(root.get("phoneNumber"),  phoneNumber + "%");
		};
	}
}
