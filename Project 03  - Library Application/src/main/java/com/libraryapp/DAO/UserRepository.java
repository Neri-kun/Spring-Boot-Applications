package com.libraryapp.DAO;

import java.util.List;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Repository;


import com.libraryapp.entities.User;

@Repository
public interface UserRepository extends  JpaRepository<User, Long>, JpaSpecificationExecutor<User> {
	
	
	
	List<User> findAll(@Nullable Specification<User> spec);
	
	@Query("SELECT u FROM User u WHERE CONCAT(u.firstName, ' ', u.lastName, ' ') LIKE %?1%")
	public List<User> search(String keyword);
	
	
	@Query("SELECT u FROM User u WHERE u.firstName LIKE ?1%")
	public Page<User> findByFirstName(String firstName, Pageable pageable);
	
	@Query("SELECT u FROM User u WHERE u.phoneNumber LIKE ?1%")
	public Page<User> findByPhoneNumber(String phoneNumber, Pageable pageable);
	
	@Query("SELECT u FROM User u WHERE u.firstName LIKE ?1% AND u.phoneNumber LIKE ?2%")
	public Page<User> findByFirstNameAndPhoneNumber(String firstName, String phoneNumber, Pageable pageable);
	
	@Query("SELECT u FROM User u WHERE u.email LIKE ?1% AND u.phoneNumber LIKE ?2%")
	public Page<User> findByEmailAndPhoneNumber(String firstName, String phoneNumber, Pageable pageable);
	
	@Query("SELECT u FROM User u WHERE u.lastName LIKE ?1% AND u.phoneNumber LIKE ?2%")
	public Page<User> findByLastNameAndPhoneNumber(String lastName, String phoneNumber, Pageable pageable);
	
	@Query("SELECT u FROM User u WHERE u.lastName LIKE ?1%")
	public Page<User> findByLastName(String lastName, Pageable pageable);
	
	@Query("SELECT u FROM User u WHERE u.firstName LIKE ?1% AND u.lastName LIKE ?2%")
	public Page<User> findByFirstNameAndLastName(String firstName,String lastName, Pageable pageable);
	
	@Query("SELECT u FROM User u WHERE u.email LIKE ?1%")
	public Page<User> findByEmail(String email, Pageable pageable);
	
	@Query("SELECT u FROM User u WHERE u.firstName LIKE ?1% AND u.email LIKE ?2%")
	public Page<User> findByFirstNameAndEmail(String firstName,String email, Pageable pageable);
	
	@Query("SELECT u FROM User u WHERE u.lastName LIKE ?1% AND u.email LIKE ?2%")
	public Page<User> findByLastNameAndEmail(String lastName,String email, Pageable pageable);
	
	@Query("SELECT u FROM User u WHERE u.firstName LIKE ?1% AND u.lastName LIKE ?2% AND u.email LIKE ?3%")
	public Page<User> findByFirstNameAndLastNameAndEmail(String firstName, String lastName, String email, Pageable pageable);
	
	@Query("SELECT u FROM User u WHERE u.firstName LIKE ?1% AND u.lastName LIKE ?2% AND u.phoneNumber LIKE ?3%")
	public Page<User> findByFirstNameAndLastNameAndPhoneNumber(String firstName, String lastName, String phoneNumber, Pageable pageable);
	
	@Query("SELECT u FROM User u WHERE u.firstName LIKE ?1% AND u.email LIKE ?2% AND u.phoneNumber LIKE ?3%")
	public Page<User> findByFirstNameAndEmailAndPhoneNumber(String firstName, String email, String phoneNumber, Pageable pageable);
	
	@Query("SELECT u FROM User u WHERE u.lastName LIKE ?1% AND u.email LIKE ?2% AND u.phoneNumber LIKE ?3%")
	public Page<User> findByLastNameAndEmailAndPhoneNumber(String firstName, String email, String phoneNumber, Pageable pageable);
	
	@Query("SELECT u FROM User u WHERE u.firstName LIKE ?1% AND u.lastName LIKE ?2% AND u.email LIKE ?3% AND u.phoneNumber LIKE ?4%")
	public Page<User> findByAllFilters(String firstName, String lastName, String email, String phoneNumber, Pageable pageable);
}
