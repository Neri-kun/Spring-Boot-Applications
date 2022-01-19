package com.libraryapp.services;

import java.util.ArrayList; 
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Service;
import com.libraryapp.DAO.UserRepository;
import com.libraryapp.entities.User;
import com.libraryapp.specifications.UserSpecifications;


@Service
public class UserService {

	@Autowired
	UserRepository userRepo;
	
	public static final int USERS_PER_PAGE = 5;
	
	public void save(User user) {
		userRepo.save(user);
	}
	
	public void saveById(Long userId) {
		User user = userRepo.findById(userId).get();
		userRepo.save(user);
	}
	
	public User findById(long id) {
		User user = userRepo.findById(id).get();
		return user;
	}
	
	public List<User> findAll(){
		return (List<User>) userRepo.findAll();
	}
	
	public List<User> userSearcher(String firstName, String lastName){
		if (firstName != null && lastName != null) return getByFullName(firstName, lastName);
		else if (firstName == null && lastName != null) return getByLastName(lastName);
		else if (firstName != null && lastName == null) return getByFirstName(firstName);
		else return new ArrayList<User>();
	}
	
	public List<User> getByFirstName(String firstName){
		List<User> users = new ArrayList<User>();
		for (User user : userRepo.findAll()) {
			if (user.getFirstName().toLowerCase().contains(firstName.toLowerCase())) {
				users.add(user);
			}
		}
		return users;
	}
	
	public List<User> getByLastName(String lastName){
		List<User> users = new ArrayList<User>();
		for (User user : userRepo.findAll()) {
			if(user.getLastName().toLowerCase().contains(lastName.toLowerCase())) {
				users.add(user);
			}
		}
		return users;
	}
	
	public List<User> getByFullName(String firstName, String lastName){
		List<User> users = new ArrayList<User>();
		for (User user : userRepo.findAll()) {
			if (user.getFirstName().toLowerCase().contains(firstName.toLowerCase()) &&
				user.getLastName().toLowerCase().contains(lastName.toLowerCase())) {
				users.add(user);
			}
		}
		return users;
	}
	
	public boolean isUsernameAlreadyTaken(String username) {
		
		for(User user : userRepo.findAll()) {
			if(user.getUserName().equals(username)) {
				return true;
			}			
		}
		return false;
	}
	
	public List<User> listAll(String keyword) {
		if (keyword != null) {
			return userRepo.search(keyword);
		}
		return userRepo.findAll();
	}
	
	public Pageable getPagination(String sortField, String sortDirection, int pageNumber, int pageSize) {
		
		Sort sort = sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortField).ascending() : Sort.by(sortField).descending();
		PageRequest pageable = PageRequest.of(pageNumber - 1 , pageSize,sort);
		return (Pageable) pageable;
	}
	
	public Page<User> findUserByCustomQuerySpecifications(int pageNumber, int pageSize, String sortField, String sortDirection,
					String firstName, String lastName, String email, String phoeNumber){
		
		Specification <User> specs = Specification.where(UserSpecifications.likeFirstName(firstName)).and(UserSpecifications.likeLastName(lastName))
									 .and(UserSpecifications.likeEmail(email)).and(UserSpecifications.likePhoneNumber(phoeNumber));
		
		return userRepo.findAll(specs, getPagination(sortField,sortDirection,pageNumber,pageSize));
	}
}
