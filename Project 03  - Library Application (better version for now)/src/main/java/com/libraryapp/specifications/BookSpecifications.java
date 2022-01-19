package com.libraryapp.specifications;

import org.springframework.data.jpa.domain.Specification;

import com.libraryapp.entities.Book;

public class BookSpecifications {
	
	public static Specification<Book> likeTitle (String title){
		if(title == null) {	//possible to remove this condition, not only in this particular case but for all specifications
			return null;
		}
		
		return (root, query, cb) ->{
			return cb.like(root.get("title"), title + "%");
		};
	}
	
	public static Specification<Book> likeAuthor(String author) {
		if (author == null) {
			return null;
		}
		return (root, query, cb) -> {
			return cb.like(root.get("author"),  author + "%");
		};
	}
	
}
