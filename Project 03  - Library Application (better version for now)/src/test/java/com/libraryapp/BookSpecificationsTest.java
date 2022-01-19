package com.libraryapp;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.libraryapp.DAO.BookRepository;
import com.libraryapp.entities.Book;
import com.libraryapp.specifications.BookSpecifications;

@SpringBootTest
public class BookSpecificationsTest {
	
	@Autowired
	private BookRepository bookRepository;
	
	Book book;
	
	@Test
	void search_with_one_spec__shouldWork() {
		List<Book> books = bookRepository.findAll(BookSpecifications.likeAuthor("B"));
		int count = 0;
		for(Book book : books) {
			System.out.println(book.getAuthor());
			count++;
		}
		System.out.println(count);
		assertNotNull(books);
		assertEquals(1, books.size());
	}
	
	@Test
	void search_with_only_title_spec() {
		
		List<Book> books = bookRepository.findAll(BookSpecifications.likeTitle("C"));
		for(Book book : books) {
			System.out.println(book.getTitle() + "is written by:" + book.getAuthor());
		
		}
		System.out.println(books.size());
		assertNotNull(books);
		assertEquals(1, books.size());
	}
	
	@Test
	void search_with_multiple_specs() {
		
		List<Book> books = bookRepository.findAll(BookSpecifications.likeTitle("A").and(BookSpecifications.likeAuthor("R")));
		
		for(Book book : books) {
			System.out.println(book.getTitle() + "is written by:" + book.getAuthor());
		}
		
		System.out.println(books.size());
		assertNotNull(books);
		assertEquals(1, books.size());
	}
}
