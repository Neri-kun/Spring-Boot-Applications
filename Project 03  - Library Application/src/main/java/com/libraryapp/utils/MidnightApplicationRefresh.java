package com.libraryapp.utils;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.libraryapp.entities.Book;
import com.libraryapp.entities.Notification;
import com.libraryapp.entities.User;
import com.libraryapp.services.BookService;
import com.libraryapp.services.NotificationService;
import com.libraryapp.services.UserService;

@Component
public class MidnightApplicationRefresh {

	@Autowired
	BookService bookService;
	
	@Autowired
	UserService userService;
	
	@Autowired
	NotificationService notificationService;
	
		/**
		 * Removes overdue reservations and notifications.
		 */
		public void midnightApplicationRefresher() {
			
			for (Book book : bookService.findAll()) {
				if (book.getEndReservationDate() != null) {
					if (book.getEndReservationDate().compareTo(LocalDate.now()) == -1) {
						if (book.getReservedByUser() != null) {
							User user = book.getReservedByUser();
							book.setReservedByUser(null);
							userService.save(user);
						}
						book.setStartReservationDate(null);
						book.setEndReservationDate(null);	
						book.setReadyForPickup(false);
						bookService.save(book);
					}	
				}
			}
	
			for (Notification notification : notificationService.findAll()) {	
				if (notification.getValidUntilDate() != null) {
					if (notification.getValidUntilDate().compareTo(LocalDate.now()) == -1) {
						notificationService.deleteById(notification.getNotificationId());
					}
				}	
			}
		}
}
