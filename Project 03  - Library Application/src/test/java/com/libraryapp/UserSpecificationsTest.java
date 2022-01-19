package com.libraryapp;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.jpa.domain.Specification;

import com.libraryapp.DAO.UserRepository;
import com.libraryapp.entities.User;
import com.libraryapp.specifications.UserSpecifications;
import org.junit.jupiter.api.Test;
@SpringBootTest
public class UserSpecificationsTest {
	
	@Autowired
	private UserRepository userRepository;
	
	User user;
	
	@Test
	void search_with_one_spec__shouldWork() {
		List<User> users = userRepository.findAll(UserSpecifications.likeFirstName("D"));
		int count = 0;
		for(User user : users) {
			System.out.println(user.getFirstName());
			count++;
		}
		System.out.println(count);
		assertNotNull(users);
		assertEquals(1, users.size());
		
		
	}
	
	@Test
	void search_with_another_one_spec__shouldWork() {
		List<User> users = userRepository.findAll(UserSpecifications.likeLastName("N"));
		int count = 0;
		for(User user : users) {
			System.out.println(user.getFirstName());
			count++;
		}
		System.out.println(count);
		assertNotNull(users);
		assertEquals(1, users.size());
	}
	
	@Test
	void search_with_multiple_specs_shouldWork() {
		
		Specification<User> specs = Specification.where(UserSpecifications.likeFirstName("D")).and(UserSpecifications.likeLastName("N"));
		List<User> users = userRepository.findAll(specs);
		int count = 0;
		for(User user : users) {
			System.out.println(user.getFirstName());
			count++;
		}
		System.out.println(count);
		assertNotNull(users);
		assertEquals(1, users.size());
	}
}
