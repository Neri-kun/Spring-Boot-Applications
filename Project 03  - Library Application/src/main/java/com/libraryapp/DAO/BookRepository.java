package com.libraryapp.DAO;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.libraryapp.entities.Book;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
	
}
