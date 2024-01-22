package com.libraryapp.controllers;

import java.math.BigDecimal; 
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.libraryapp.entities.Book;
import com.libraryapp.entities.Notification;
import com.libraryapp.entities.User;
import com.libraryapp.security.CurrentUserFinder;
import com.libraryapp.services.BookService;
import com.libraryapp.services.NotificationService;
import com.libraryapp.services.UserService;
import com.libraryapp.utils.FineCalculator;
import com.libraryapp.utils.ListInStringConverter;


@Controller
@RequestMapping(value="/employee")
public class EmployeeController {

	@Autowired
	UserService userService;
	
	@Autowired
	BookService bookService;
	
	@Autowired 
	CurrentUserFinder currentUserFinder;
	
	@Autowired
	NotificationService notifService;
	
	@Autowired
	FineCalculator fineCalculator;
	
	@Autowired
	ListInStringConverter listConverter;
	
	@Autowired
	private static final String USERS_ATTRIBUTE = "users";
	
	@Autowired
	private static final String TITLE_ATTRIBUTE = "title";
	
	@Autowired
	private static final String AUTHOR_ATTRIBUTE = "author";
	
	@Autowired
	private static final String SELECTED_BOOK_IDS_ATTRIBUTE = "selectedBookIds";
	
	@Autowired
	private static final String SELECTED_BOOKS_ATTRIBUTE = "selectedBooks";
	
		@GetMapping
		public String employeeHomePage(Model model) {
			long currentUserId = currentUserFinder.getCurrentUserId();
			User currentUser = userService.findById(currentUserId);
			model.addAttribute("currentUser", currentUser);
			return"employee/employee-home.html";
		}
				
		@GetMapping(value="/users/showusers")
		public String showUsers(Model model,
				@RequestParam (required=false) String sortField,
				@RequestParam (required=false) String sortDirection,
				@RequestParam (required=false)String firstName, 
				@RequestParam (required=false)String lastName,
				@RequestParam (required=false)String email,
				@RequestParam (required=false)String phoneNumber,
				@RequestParam (required=false)String showAllUsers,
				@RequestParam (required=false) String numberOfRegistrations
			) {	
			
		if(sortField != null || sortDirection != null || firstName != null || lastName != null || email != null || phoneNumber != null || showAllUsers != null || numberOfRegistrations != null)
			return showUsersPaginated(model,1,sortField,sortDirection,firstName,lastName,email,phoneNumber,showAllUsers,numberOfRegistrations);
		else
		{	
			LinkedHashMap<User, BigDecimal> usersAndFines = fineCalculator.getAllUsersWithFines(new ArrayList<>());
			model.addAttribute("usersWithFines", usersAndFines);
			return "employee/employee-show-users.html";
		}
	}
		
		
		@GetMapping(value="/users/showusers/page/{pageNo}")
		public String showUsersPaginated(Model model,
				@PathVariable (value = "pageNo") int pageNumber,
				@RequestParam("sortField") String sortField,
				@RequestParam("sortDir") String sortDir,
				@RequestParam (required=false)String firstName, 
				@RequestParam (required=false)String lastName,
				@RequestParam (required=false)String email,
				@RequestParam (required=false)String phoneNumber,
				@RequestParam (required=false)String showAllUsers,
				@RequestParam (required=false) String numberOfRegistrations) {
			
			int pageSize = Integer.parseInt(numberOfRegistrations);
			
			
			Page<User> page;
			
			page = userService.findUserByCustomQuerySpecifications(pageNumber, pageSize, sortField, sortDir, firstName, lastName, email, phoneNumber);
			
			List<User> users = page.getContent();
			
			model.addAttribute("currentPage", pageNumber);
			model.addAttribute("totalPages", page.getTotalPages());
			model.addAttribute("totalItems", page.getTotalElements());
			
			LinkedHashMap<User, BigDecimal> usersWithFines = fineCalculator.getAllUsersWithFines(users);
			model.addAttribute(USERS_ATTRIBUTE, users);
			model.addAttribute("usersWithFines", usersWithFines);
			
			model.addAttribute("sortField", sortField);
			model.addAttribute("sortDir", sortDir);
			model.addAttribute("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");
			
			model.addAttribute("firstname",firstName);
			model.addAttribute("lastname",lastName);
			model.addAttribute("email", email);
			model.addAttribute("phonenumber", phoneNumber);
			
			model.addAttribute("numberOfRegistrations", numberOfRegistrations);
			

			long startCount = (pageNumber - 1) * UserService.USERS_PER_PAGE + 1;
			model.addAttribute("startCount", startCount);
				
			long endCount = startCount + UserService.USERS_PER_PAGE - 1;
			if(endCount > page.getTotalElements()) {
				endCount = page.getTotalElements();
			}
			
			model.addAttribute("endCount", endCount);
			return "employee/employee-show-users.html";
		}
		
		@GetMapping(value="/users/showuserinfo")
		public String showUserInfo(@RequestParam Long userId, 
									@RequestParam BigDecimal fine,
									Model model) {
			User user = userService.findById(userId);
			model.addAttribute("booksInUse", fineCalculator.getBooksWithFines(user.getBooks()));
			model.addAttribute("fine", fine);
			model.addAttribute("user", user);
			return "employee/employee-show-user-info.html";
		}
		
		@GetMapping(value="/books")
		public String books() {
			return "employee/employee-books.html";
		}
	
		@GetMapping(value="/books/showbooks")
		public String showBooks(
				Model model,
				@RequestParam(required=false) String sortField,
				@RequestParam(required=false) String sortDirection,
				@RequestParam (required=false) String title,
				@RequestParam (required=false) String author,
				@RequestParam (required=false) String showAllBooks,
				@RequestParam(required=false) String numberOfRegistrations
				)
		{
			
			if(showAllBooks != null || title !=null || author != null || sortField != null || sortDirection != null || numberOfRegistrations != null) {
				if(showAllBooks != null)
					return showBooksPaginated(model,1,sortField,sortDirection,"","",numberOfRegistrations);
				else
					return showBooksPaginated(model,1,sortField,sortDirection,title,author,numberOfRegistrations);
			}
			else {
				List<Book> books = new ArrayList<Book>();
				model.addAttribute("books", books);
				return "employee/employee-show-books.html";
			}
		}
		
		@GetMapping(value="/books/showbooks/page/{pageNo}")
		public String showBooksPaginated(Model model,
				@PathVariable (value = "pageNo") int pageNumber,
				@RequestParam("sortField") String sortField,
				@RequestParam("sortDir") String sortDir,
				@RequestParam (required=false) String title,
				@RequestParam (required=false) String author,
				@RequestParam (required=false) String numberOfRegistrations) {
			
			int pageSize = Integer.parseInt(numberOfRegistrations);
			
			
			Page<Book> page = bookService.findBookByCustomQuerySpecifications(pageNumber, pageSize, sortField, sortDir, title, author);
			
			List <Book> books = page.getContent();
			
			model.addAttribute("currentPage", pageNumber);
			model.addAttribute("totalPages", page.getTotalPages());
			model.addAttribute("totalItems", page.getTotalElements());
			
			model.addAttribute("books", books);
			
			model.addAttribute(TITLE_ATTRIBUTE,title);
			model.addAttribute(AUTHOR_ATTRIBUTE, author);
			
			model.addAttribute("sortField", sortField);
			model.addAttribute("sortDir", sortDir);
			model.addAttribute("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");
			
			model.addAttribute("numberOfRegistrations", numberOfRegistrations);
			
			long startCount = (pageNumber - 1) * BookService.BOOKS_PER_PAGE + 1;
			model.addAttribute("startCount", startCount);
				
			long endCount = startCount + BookService.BOOKS_PER_PAGE - 1;
			if(endCount > page.getTotalElements()) {
				endCount = page.getTotalElements();
			}
			
			model.addAttribute("endCount", endCount);
			
			return "employee/employee-show-books.html";
		}
		
		@GetMapping(value="/books/newbook")
		public String newBook(Model model) {
			model.addAttribute("book", new Book());
			return "employee/employee-new-book.html";
		}
		
		@PostMapping(value="/books/save")
		public String saveBook(Book book) {
			bookService.save(book);
			return "redirect:/employee/books/booksaved";
		}
		
		@GetMapping(value="/books/booksaved")
		public String bookSaved() {
			return "employee/employee-book-saved.html";
		}
		
		@GetMapping(value="/books/areyousuretodeletebook")
		public String areYouSureToDeleteBook(@RequestParam Long deleteBookId,
											@RequestParam(required=false) String title,
											@RequestParam(required=false) String author,
											Model model) {
			Book book = bookService.findById(deleteBookId);
			model.addAttribute("deleteBook", book);
			model.addAttribute(TITLE_ATTRIBUTE, title);
			model.addAttribute(AUTHOR_ATTRIBUTE, author);
			return "employee/employee-delete-book.html";
		}
		
		@DeleteMapping(value="/books/deletebook")
		public String deleteBook(@RequestParam Long deleteBookId) {
			bookService.deleteById(deleteBookId);
			return "redirect:/employee/books/bookdeleted";
		}
		
		@GetMapping(value="/books/bookdeleted")
		public String bookDeleted() {
			return "employee/employee-book-deleted.html";
		}
		
		@GetMapping(value="/books/changebookinfo")
		public String changeBookInfo(@RequestParam Long changeBookId, Model model) {
			Book book = bookService.findById(changeBookId);
			model.addAttribute("book", book);
			return "employee/employee-change-book-info.html";
		}
		
		@PutMapping(value="/books/savebookchange")
		public String updatebookinfo(@RequestParam (required=false) String removeCurrentUser,
									@RequestParam (required=false) String removeReservation,
									Book book) {
			if (removeCurrentUser != null) bookService.removeCurrentUserOfBook(book);
			if (removeReservation != null) bookService.removeReservation(book);
			bookService.save(book);
			return "redirect:/employee/books/bookinfochanged";
		}
		
		@GetMapping(value="/books/bookinfochanged")
		public String bookInfoChanged() {
			return "employee/employee-book-information-changed.html";
		}
			
		@GetMapping(value="/orders")
		public String orders(@RequestParam (required = false) String firstName,
							 @RequestParam (required = false) String lastName,
							 @RequestParam (required = false) Long userId,
							 @RequestParam (required = false) String title,
							 @RequestParam (required = false) String author,
							 @RequestParam (required = false) Long selectedBookId,
							 @RequestParam (required = false) Long removeBookId,
							 @RequestParam (required = false) String selectedBookIdsInString,
							 Model model) {
			
			List<User> users = userService.userSearcher(firstName, lastName);
			LinkedHashMap<User, BigDecimal> usersAndFines = fineCalculator.getAllUsersWithFines(users);
			
			List<Book> searchedBooks = bookService.searchBooks(title, author);
			
			User user = null;
			if (userId != null) user = userService.findById(userId);
			
			Set<Long> selectedBookIds = new LinkedHashSet<Long>();
			if (selectedBookIdsInString != null) selectedBookIds = listConverter.convertListInStringToSetInLong(selectedBookIdsInString);			
			if (selectedBookId != null) selectedBookIds.add(selectedBookId);
			if (removeBookId != null) selectedBookIds.remove(removeBookId);
			
			List<Book> selectedBookObjects = bookService.convertIdsCollectionToBooksList(selectedBookIds);

			model.addAttribute("selectedBookObjects", selectedBookObjects);
			model.addAttribute(SELECTED_BOOK_IDS_ATTRIBUTE, selectedBookIds);
			model.addAttribute(TITLE_ATTRIBUTE, title);
			model.addAttribute(AUTHOR_ATTRIBUTE, author);
			model.addAttribute("searchedBooks", searchedBooks);
			model.addAttribute(USERS_ATTRIBUTE, usersAndFines);
			model.addAttribute("userId", userId);
			model.addAttribute("user", user);
			
			return "employee/employee-orders.html";
		}
		
		
		
		@GetMapping(value="/confirmorder")
		public String confirmOrder(@RequestParam String selectedBookIdsInString,
								   @RequestParam Long userId,
								   Model model) {
			Set<Long> selectedBookIds = listConverter.convertListInStringToSetInLong(selectedBookIdsInString);
			List<Book> selectedBooks = bookService.convertIdsCollectionToBooksList(selectedBookIds);
			User user = userService.findById(userId);
			
			model.addAttribute(SELECTED_BOOK_IDS_ATTRIBUTE, selectedBookIds);
			model.addAttribute("user", user);
			model.addAttribute(SELECTED_BOOKS_ATTRIBUTE, selectedBooks);
			
			return "employee/employee-confirm-order.html";
		}
		
		@PutMapping(value="/saveorder")
		public String saveOrder(@RequestParam Long userId,
								@RequestParam String selectedBookIdsInString) {
			Set<Long> selectedBookIds = listConverter.convertListInStringToSetInLong(selectedBookIdsInString);
			User user = userService.findById(userId);
			bookService.saveBookOrder(selectedBookIds, user);
			return "redirect:/employee/ordersaved";
		}
		
		@GetMapping(value="/ordersaved")
		public String orderSaved() {
			return "employee/employee-order-saved.html";
		}
	
		
		@GetMapping(value="/returnedbooks")
		public String returnedBooks(@RequestParam (required = false) Long userId,
									@RequestParam (required = false) String firstName,
									@RequestParam (required = false) String lastName,
									@RequestParam (required = false) Long selectedBookId,
									@RequestParam (required = false) Long removeBookId,
									@RequestParam (required = false) String selectedBookIdsInString,
									Model model) {
		
			List<User> users = userService.userSearcher(firstName, lastName);
					
			User user = null;
			if (userId != null) user = userService.findById(userId);
			
			LinkedHashMap<Book, BigDecimal> booksInUseByUser = null;
			if (user != null) booksInUseByUser = fineCalculator.getBooksWithFines(user.getBooks());
			
			Set<Long> selectedBookIds = new LinkedHashSet<Long>();
			if (selectedBookIdsInString != null) selectedBookIds = listConverter.convertListInStringToSetInLong(selectedBookIdsInString);
			if (removeBookId != null) selectedBookIds.remove(removeBookId);
			if (selectedBookId != null) selectedBookIds.add(selectedBookId);
			
			LinkedHashMap<Book, BigDecimal> selectedBooks = fineCalculator.getBooksWithFines(bookService.convertIdsCollectionToBooksList(selectedBookIds));
			BigDecimal fineToPay = fineCalculator.getTotalFine(bookService.convertIdsCollectionToBooksList(selectedBookIds));
			
			model.addAttribute(SELECTED_BOOK_IDS_ATTRIBUTE, selectedBookIds);
			model.addAttribute("fineToPay", fineToPay);
			model.addAttribute(SELECTED_BOOKS_ATTRIBUTE, selectedBooks);
			model.addAttribute("booksInUseByUser", booksInUseByUser);
			model.addAttribute(USERS_ATTRIBUTE, users);
			model.addAttribute("user", user);
			model.addAttribute("firstName", firstName);
			model.addAttribute("lastName", lastName);
			model.addAttribute("userId", userId);
			return "employee/employee-returned-books.html";
		}
		
		@GetMapping(value="/confirmreturnedbooks")
		public String confirmReturnedBooks(@RequestParam Long userId,
										   @RequestParam BigDecimal fineToPay,
										   @RequestParam String selectedBookIdsInString,
										   Model model) {
			Set<Long> selectedBookIds = listConverter.convertListInStringToSetInLong(selectedBookIdsInString);
			List<Book> selectedBooks = bookService.convertIdsCollectionToBooksList(selectedBookIds);
						
			model.addAttribute(SELECTED_BOOKS_ATTRIBUTE, selectedBooks);
			model.addAttribute(SELECTED_BOOK_IDS_ATTRIBUTE, selectedBookIds);
			model.addAttribute("user", userService.findById(userId));
			model.addAttribute("fineToPay", fineToPay);
			return "employee/employee-confirm-returned-books.html";
		}
		
		
		@PutMapping(value="/savereturnedbooks")
		public String saveReturnedBooks(@RequestParam String selectedBookIdsInString) {
			Set<Long> returnedBooks = listConverter.convertListInStringToSetInLong(selectedBookIdsInString);
			List<Book> books = bookService.convertIdsCollectionToBooksList(returnedBooks);
			bookService.removeCurrentUserOfMultipleBooks(books);
			return "redirect:/employee/booksreturned";
		}
		
		@GetMapping(value="/booksreturned")
		public String booksReturned() {
			return "employee/employee-books-returned.html";
		}
		
		@GetMapping(value="/reservations")
		public String reservations(Model model) {
			model.addAttribute("unprocessedReservations", bookService.getUnprocessedBookReservations());
			model.addAttribute("processedReservations", bookService.getProcessedBookReservations());
			return "employee/employee-reservations.html";
		}
		
		@PutMapping(value="/setreadyforpickup")
		public String setReadyForPickup(@RequestParam Long bookId, 
										@RequestParam Long userId,
										Model model) {
			model.addAttribute("user", userService.findById(userId));
			model.addAttribute("book", bookService.findById(bookId));
			return "employee/employee-reservation-ready-for-pick-up.html";
		}
		
		@PutMapping(value="/updatebookreservation")
		public String updateBookReservation(@RequestParam Long bookId,
											@RequestParam Long userId) {
			
			Book book = bookService.findById(bookId);
			Notification notification = new Notification(LocalDate.now(), book.getEndReservationDate(), "Your reservation is ready for pick-up until " + 
														book.getEndReservationDate() + ". " + book.getTitle() + " by " + book.getAuthor() + "."); 
												
			notification.setValidUntilDate(book.getEndReservationDate());
			notification.setNotificationReceiver(userService.findById(userId));
			notifService.save(notification);
			userService.saveById(userId);
			book.setReadyForPickup(true);
			bookService.save(book);
			return "redirect:/employee/reservations";
		}
}
