package com.libraryapp.DAO;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Repository;

import com.libraryapp.entities.Book;

@Repository
public interface BookRepository extends JpaRepository<Book, Long>,JpaSpecificationExecutor<Book> {
	
	List<Book> findAll(@Nullable Specification<Book> spec);
	
	Page<Book> findAll(@Nullable Specification<Book> spec, Pageable pageable);
}
