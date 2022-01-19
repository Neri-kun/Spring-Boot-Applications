package com.libraryapp.services;

import java.util.ArrayList; 
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.libraryapp.DAO.UserRepository;
import com.libraryapp.entities.User;


@Service
@Transactional(readOnly = true)
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
	
	public Page<User> findPaginated(int pageNumber, int pageSize, String sortField, String sortDirection){
		return userRepo.findAll(getPagination(sortField, sortDirection, pageNumber,  pageSize));
	}
	
	public Page<User> findPaginatedByFirstName(int pageNumber, int pageSize, String sortField, String sortDirection, String firstName){
		return userRepo.findByFirstName(firstName, getPagination(sortField, sortDirection, pageNumber,  pageSize));
	}
	
	public Page<User> findPaginatedByEmail(int pageNumber, int pageSize, String sortField, String sortDirection, String email){
		return userRepo.findByEmail(email, getPagination(sortField, sortDirection, pageNumber,  pageSize));
	}
	
	public Page<User> findPaginatedByFirstNameAndEmail(int pageNumber, int pageSize, String sortField, String sortDirection,String firstName, String email){
		return userRepo.findByFirstNameAndEmail(firstName, email, getPagination(sortField, sortDirection, pageNumber,  pageSize));
	}
	
	public Page<User> findPaginatedByLastName(int pageNumber, int pageSize, String sortField, String sortDirection, String lastName){
		return userRepo.findByLastName(lastName, getPagination(sortField, sortDirection, pageNumber,  pageSize));
	}
	
	public Page<User> findPaginatedByFirstAndLastName(int pageNumber, int pageSize, String sortField, String sortDirection,String firstName, String lastName){
		return userRepo.findByFirstNameAndLastName(firstName, lastName, getPagination(sortField, sortDirection, pageNumber,  pageSize));
	}
	
	public Page<User> findPaginatedByLastNameAndEmail(int pageNumber, int pageSize, String sortField, String sortDirection,String lastName, String email){
		return userRepo.findByLastNameAndEmail(lastName,email,getPagination(sortField, sortDirection, pageNumber,  pageSize));
	}
	
	public Page<User> findPaginatedByLastNameAndPhoneNumber(int pageNumber, int pageSize, String sortField, String sortDirection,String lastName, String phoneNumber){
		return userRepo.findByLastNameAndPhoneNumber(lastName,phoneNumber,getPagination(sortField, sortDirection, pageNumber,  pageSize));
	}
	
	
	
	public Page<User> findPaginatedByFistNameAndLastNameAndEmail(int pageNumber, int pageSize, String sortField, String sortDirection,String fistName,String lastName, String email){
		return userRepo.findByFirstNameAndLastNameAndEmail(fistName, lastName, email, getPagination(sortField, sortDirection, pageNumber,  pageSize));
	}
	
	public Page<User> findPaginatedByFistNameAndLastNameAndPhoneNumber(int pageNumber, int pageSize, String sortField, String sortDirection,String fistName,String lastName, String phoneNumber){
		return userRepo.findByFirstNameAndLastNameAndPhoneNumber(fistName, lastName, phoneNumber, getPagination(sortField, sortDirection, pageNumber,  pageSize));
	}
	
	public Page<User> findPaginatedByFirstNameAndEmailAndPhoneNumber(int pageNumber, int pageSize, String sortField, String sortDirection,String fistName,String email, String phoneNumber){
		return userRepo.findByFirstNameAndEmailAndPhoneNumber(fistName, email, phoneNumber, getPagination(sortField, sortDirection, pageNumber,  pageSize));
	}
	
	
	public Page<User> findPaginatedByLastNameAndEmailAndPhoneNumber(int pageNumber, int pageSize, String sortField, String sortDirection,String fistName,String email, String phoneNumber){
		return userRepo.findByLastNameAndEmailAndPhoneNumber(fistName, email, phoneNumber, getPagination(sortField, sortDirection, pageNumber,  pageSize));
	}
	
	public Page<User> findPaginatedByPhoneNumber(int pageNumber, int pageSize, String sortField, String sortDirection, String phoneNumber){
		return userRepo.findByPhoneNumber(phoneNumber, getPagination(sortField, sortDirection, pageNumber,  pageSize));
	}
	
	public Page<User> findPaginatedByFirstNameAndPhoneNumber(int pageNumber, int pageSize, String sortField, String sortDirection, String lastName, String phoneNumber){
		return userRepo.findByFirstNameAndPhoneNumber(lastName, phoneNumber, getPagination(sortField, sortDirection, pageNumber,  pageSize));
	}
	
	public Page<User> findPaginatedByEmailAndPhoneNumber(int pageNumber, int pageSize, String sortField, String sortDirection, String email, String phoneNumber){
		return userRepo.findByEmailAndPhoneNumber(email, phoneNumber, getPagination(sortField, sortDirection, pageNumber,  pageSize));
	}
	
	
	public Page<User> findPaginatedByAllFilters(int pageNumber, int pageSize, String sortField, String sortDirection, String firstName, String lastName,String email, String phoneNumber){
		return userRepo.findByAllFilters(firstName, lastName, email, phoneNumber, getPagination(sortField, sortDirection, pageNumber,  pageSize));
	}
	
	public Pageable getPagination(String sortField, String sortDirection, int pageNumber, int pageSize) {
		
		Sort sort = sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortField).ascending() : Sort.by(sortField).descending();
		PageRequest pageable = PageRequest.of(pageNumber - 1 , pageSize,sort);
		return (Pageable) pageable;
	}
	
}
