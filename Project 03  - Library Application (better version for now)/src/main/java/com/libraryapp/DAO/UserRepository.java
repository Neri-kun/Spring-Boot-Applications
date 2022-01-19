package com.libraryapp.DAO;

import java.util.List;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Repository;
import com.libraryapp.entities.User;

@Repository
public interface UserRepository extends  JpaRepository<User, Long>,JpaSpecificationExecutor<User> {
	
	List<User> findAll(@Nullable Specification<User> spec);
	
	Page<User> findAll(@Nullable Specification<User> spec, Pageable pageable);
	
	@Query("SELECT u FROM User u WHERE CONCAT(u.firstName, ' ', u.lastName, ' ') LIKE %?1%")
	public List<User> search(String keyword);
	
}
