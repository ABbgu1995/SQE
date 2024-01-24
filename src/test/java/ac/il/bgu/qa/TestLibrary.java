package ac.il.bgu.qa;

import ac.il.bgu.qa.errors.*;
import ac.il.bgu.qa.services.*;;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.*;
import org.junit.jupiter.params.provider.NullSource;
import org.mockito.*;
import ac.il.bgu.qa.services.DatabaseService;
import ac.il.bgu.qa.services.ReviewService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.List;

public class TestLibrary {

    @Mock
    private User mockUser;

    @Mock
    private DatabaseService mockDatabaseService;

    @Mock
    private ReviewService mockReviewService;

    @Mock
    private NotificationService mockNotificationService;

    private Library library;
    @Mock
    private Book mockBook;

    @Spy
    List<String> spyListReviews = spy(new ArrayList<>());
    @BeforeEach
    void init() {
        // 1. Arrange
        // 1.1 init the mocks
        MockitoAnnotations.openMocks(this);
        // 1.2 init the spy
        spyListReviews.clear();
        // 1.3 Create an instance of Library with mocks
        library = new Library(mockDatabaseService, mockReviewService);
    }

    // Test addBook method
    @Test
    void testAddBook() {
        // Mock Book
        // 1.4. Stubbing - Define behavior for mockBook
        when(mockBook.getISBN()).thenReturn("9780132130806");
        when(mockBook.getTitle()).thenReturn("Mock Book");
        when(mockBook.getAuthor()).thenReturn("Mock Author");
        when(mockBook.isBorrowed()).thenReturn(false);

        // Mock DatabaseService
        // 1.5. Stubbing - Define behavior for mockDatabaseService
        when(mockDatabaseService.getBookByISBN("1234567890123")).thenReturn(null);

        // Test addBook method, if the addBook method throws an exception during its execution, the test would fail.
        // 2. Action
        // 2.1. Call the method under test
        assertDoesNotThrow(() -> library.addBook(mockBook));

        // the addBook method is void so use verify that the function has been called
        // 3. Assertion
        // 3.1. Verify interactions
        verify(mockBook, times(3)).getISBN();
        verify(mockBook, times(2)).getTitle();
        verify(mockBook, times(1)).getAuthor();
        verify(mockBook, times(1)).isBorrowed();
        verify(mockDatabaseService).addBook("9780132130806", mockBook);
        verify(mockBook, never()).borrow();
    }

    @Test
    public void testAddBookWithNullBook() {
        // 2. Action
        // 2.1. Call the method under test
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> library.addBook(null));
        // 3. Assertion
        // 3.1. Assert interactions
        assertEquals("Invalid book.", exception.getMessage());
    }

    @Test
    public void testAddBookWithNullISBN() {
        // 1.4. Stubbing - Define behavior for mockBook
        when(mockBook.getISBN()).thenReturn(null);

        // 2. Action
        // 2.1. Call the method under test
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> library.addBook(mockBook));

        // 3. Assertion
        // 3.1. Assert and verify interactions
        verify(mockBook, times(1)).getISBN();
        assertEquals("Invalid ISBN.", exception.getMessage());
    }

    @Test
    public void testAddBookWithInvalidISBN() {
        // 1.4. Stubbing - Define behavior for mockBook
        when(mockBook.getISBN()).thenReturn("invalidISBN");

        // 2. Action
        // 2.1. Call the method under test
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> library.addBook(mockBook));

        // 3. Assertion
        // 3.1. Assert interactions
        verify(mockBook, times(1)).getISBN();
        assertEquals("Invalid ISBN.", exception.getMessage());
    }

    @Test
    public void testAddBookWithNullTitle() {
        // 1.4. Stubbing - Define behavior for mockBook
        when(mockBook.getISBN()).thenReturn("9780132130806");
        when(mockBook.getTitle()).thenReturn(null);

        // 2. Action
        // 2.1. Call the method under test
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> library.addBook(mockBook));

        // 3. Assertion
        // 3.1. Assert interactions
        verify(mockBook, times(1)).getISBN();
        verify(mockBook, times(1)).getTitle();
        assertEquals("Invalid title.", exception.getMessage());
    }

    @Test
    public void testAddBookWithInvalidTitle() {
        // 1.4. Stubbing - Define behavior for mockBook
        when(mockBook.getISBN()).thenReturn("9780132130806");
        when(mockBook.getTitle()).thenReturn("");

        // 2. Action
        // 2.1. Call the method under test
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> library.addBook(mockBook));

        // 3. Assertion
        // 3.1. Assert interactions
        verify(mockBook, times(1)).getISBN();
        verify(mockBook, times(2)).getTitle();
        assertEquals("Invalid title.", exception.getMessage());
    }

    @Test
    public void testAddBookWithNullAuthor() {
        // 1.4. Stubbing - Define behavior for mockBook
        when(mockBook.getISBN()).thenReturn("9780132130806");
        when(mockBook.getTitle()).thenReturn("Mock Book");
        when(mockBook.getAuthor()).thenReturn(null);


        // 2. Action
        // 2.1. Call the method under test
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> library.addBook(mockBook));

        // 3. Assertion
        // 3.1. Assert interactions
        verify(mockBook, times(1)).getISBN();
        verify(mockBook, times(2)).getTitle();
        verify(mockBook, times(1)).getAuthor();
        assertEquals("Invalid author.", exception.getMessage());
    }

    @Test
    public void testAddBookWithInvalidAuthor() {
        // 1.4. Stubbing - Define behavior for mockBook
        when(mockBook.getISBN()).thenReturn("9780132130806");
        when(mockBook.getTitle()).thenReturn("Mock Book");
        when(mockBook.getAuthor()).thenReturn("");

        // 2. Action
        // 2.1. Call the method under test
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> library.addBook(mockBook));

        // 3. Assertion
        // 3.1. Assert interactions
        verify(mockBook, times(1)).getISBN();
        verify(mockBook, times(2)).getTitle();
        verify(mockBook, times(1)).getAuthor();
        assertEquals("Invalid author.", exception.getMessage());
    }

    @Test
    public void testAddBookWithInvalidBorrowed() {

        // 1.4. Stubbing - Define behavior for mockBook
        when(mockBook.getISBN()).thenReturn("9780132130806");
        when(mockBook.getTitle()).thenReturn("Mock Book");
        when(mockBook.getAuthor()).thenReturn("Mock Author");
        when(mockBook.isBorrowed()).thenReturn(true);

        // 2. Action
        // 2.1. Call the method under test
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> library.addBook(mockBook));

        // 3. Assertion
        // 3.1. Assert interactions
        verify(mockBook, times(1)).getISBN();
        verify(mockBook, times(2)).getTitle();
        verify(mockBook, times(1)).getAuthor();
        verify(mockBook, times(1)).isBorrowed();
        assertEquals("Book with invalid borrowed state.", exception.getMessage());
    }

    // Test borrowBook method
    @Test
    void testBorrowBook() {
        String userId = "987654321098";
        // Mock Book
        // 1.4. Stubbing - Define behavior for mockBook
        when(mockBook.isBorrowed()).thenReturn(false);

        // Mock DatabaseService
        // 1.5. Stubbing - Define behavior for mockDatabaseService
        when(mockDatabaseService.getBookByISBN("9780132130806")).thenReturn(mockBook);
        when(mockDatabaseService.getUserById(userId)).thenReturn(mockUser);

        // Test borrowBook method
        // 2. Action
        // 2.1. Call the method under test
        assertDoesNotThrow(() -> library.borrowBook("9780132130806", userId));

        // 3. Assertion
        // 3.1. Verify interactions
        verify(mockBook, times(1)).borrow();
        verify(mockDatabaseService, times(1)).borrowBook("9780132130806", userId);
        verify(mockDatabaseService, times(1)).getBookByISBN("9780132130806");
        verify(mockDatabaseService,times(1)).getUserById(userId);
    }

    @Test
    public void testBorrowBookWithInvalidISBN() {
        String userId = "123456789012";
        // 2. Action
        // 2.1. Call the method under test
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> library.borrowBook("Invalid ISBN", userId));

        // 3. Assertion
        // 3.1. Assert interactions
        assertEquals("Invalid ISBN.", exception.getMessage());
    }

    @Test
    public void testBorrowBookWithNullISBN() {
        String userId = "123456789012";
        // 2. Action
        // 2.1. Call the method under test
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> library.borrowBook(null, userId));

        // 3. Assertion
        // 3.1. Assert interactions
        assertEquals("Invalid ISBN.", exception.getMessage());
    }

    @Test
    public void testBorrowBookWithNotFound() {
        String userId = "123456789012";

        // 1.4. Stubbing - Define behavior for mockDatabaseService
        when(mockDatabaseService.getBookByISBN("9780132130806")).thenReturn(null);

        // 2. Action
        // 2.1. Call the method under test
        BookNotFoundException exception = assertThrows(BookNotFoundException.class, () -> library.borrowBook("9780132130806", userId));

        // 3. Assertion
        // 3.1. Assert interactions
        verify(mockDatabaseService, times(1)).getBookByISBN("9780132130806");
        assertEquals("Book not found!", exception.getMessage());
    }

    @Test
    public void testBorrowBookWithInvalidUser() {
        String userId = "Invalid user";

        // 1.4. Stubbing - Define behavior for mockDatabaseService
        when(mockDatabaseService.getBookByISBN("9780132130806")).thenReturn(mockBook);

        // 2. Action
        // 2.1. Call the method under test
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> library.borrowBook("9780132130806", userId));

        // 3. Assertion
        // 3.1. Assert interactions
        verify(mockDatabaseService, times(1)).getBookByISBN("9780132130806");
        assertEquals("Invalid user Id.", exception.getMessage());
    }

    @Test
    public void testBorrowBookWithNullUserId() {
        String userId = null;

        // 1.4. Stubbing - Define behavior for mockDatabaseService
        when(mockDatabaseService.getBookByISBN("9780132130806")).thenReturn(mockBook);

        // 2. Action
        // 2.1. Call the method under test
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> library.borrowBook("9780132130806", userId));

        // 3. Assertion
        // 3.1. Assert interactions
        verify(mockDatabaseService, times(1)).getBookByISBN("9780132130806");
        assertEquals("Invalid user Id.", exception.getMessage());
    }




    @Test
    public void testBorrowBookByNotRegisteredUser(){
        String userId = "123456789012";
        // 1.4. Stubbing - Define behavior for mockDatabaseService
        when(mockDatabaseService.getUserById(userId)).thenReturn(null);
        when(mockDatabaseService.getBookByISBN("9780132130806")).thenReturn(mockBook);

        // 2. Action
        // 2.1. Call the method under test
        UserNotRegisteredException exception = assertThrows(UserNotRegisteredException.class, () -> library.borrowBook("9780132130806", userId));

        // 3. Assertion
        // 3.1. Assert interactions
        verify(mockDatabaseService,times(1)).getUserById(userId);
        verify(mockDatabaseService, times(1)).getBookByISBN("9780132130806");
        assertEquals("User not found!", exception.getMessage());
    }



    @Test
    public void testBorrowBookWithBorrowedBook(){
        String userId = "123456789012";
        // 1.4. Stubbing - Define behavior for mockDatabaseService
        when(mockDatabaseService.getUserById(userId)).thenReturn(mockUser);
        when(mockDatabaseService.getBookByISBN("9780132130806")).thenReturn(mockBook);
        when(mockBook.isBorrowed()).thenReturn(true);

        // 2. Action
        // 2.1. Call the method under test
        BookAlreadyBorrowedException exception = assertThrows(BookAlreadyBorrowedException.class, () -> library.borrowBook("9780132130806", userId));

        // 3. Assertion
        // 3.1. Assert interactions
        verify(mockDatabaseService,times(1)).getUserById(userId);
        verify(mockDatabaseService, times(1)).getBookByISBN("9780132130806");
        verify(mockBook,times(1)).isBorrowed();
        assertEquals("Book is already borrowed!", exception.getMessage());
    }


    // Test registerUser method
    @Test
    void testRegisterUser() {
        // Mock User
        // 1.4. Stubbing - Define behavior for mockUser
        when(mockUser.getId()).thenReturn("123456789012");
        when(mockUser.getName()).thenReturn("Mock User");
        when(mockUser.getNotificationService()).thenReturn(mock(NotificationService.class));

        // Mock DatabaseService
        // 1.5. Stubbing - Define behavior for mockDatabaseService
        when(mockDatabaseService.getUserById("123456789012")).thenReturn(null);

        // Test registerUser method
        // 2. Action
        // 2.1. Call the method under test
        assertDoesNotThrow(() -> library.registerUser(mockUser));

        // 3. Assertion
        // 3.1. Verify interactions
        verify(mockUser, times(4)).getId();
        verify(mockUser, times(2)).getName();
        verify(mockUser, times(1)).getNotificationService();
        verify(mockDatabaseService, times(1)).registerUser("123456789012", mockUser);
    }

    @Test
    public void testRegisterUserWithNullUser() {
        // 2. Action
        // 2.1. Call the method under test
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> library.registerUser(null));

        // 3. Assertion
        // 3.1. Assert interactions
        assertEquals("Invalid user.", exception.getMessage());
    }

    @Test
    public void testRegisterUserWithInvalidId() {
        // 1.4. Stubbing - Define behavior for mockUser
        when(mockUser.getId()).thenReturn("1234567890123");

        // 2. Action
        // 2.1. Call the method under test
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> library.registerUser(mockUser));

        // 3. Assertion
        // 3.1. Assert and verify interactions
        verify(mockUser, times(2)).getId();
        assertEquals("Invalid user Id.", exception.getMessage());
    }

    @Test
    public void testRegisterUserWithNullId(){
        // 1.4. Stubbing - Define behavior for mockUser
        when(mockUser.getId()).thenReturn(null);

        // 2. Action
        // 2.1. Call the method under test
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> library.registerUser(mockUser));

        // 3. Assertion
        // 3.1. Assert and verify interactions
        verify(mockUser, times(1)).getId();
        assertEquals("Invalid user Id.", exception.getMessage());
    }

    @Test
    public void testRegisterUserWithInvalidIdName() {
        // 1.4. Stubbing - Define behavior for mockUser
        when(mockUser.getId()).thenReturn("123456789012");
        when(mockUser.getName()).thenReturn("");

        // 2. Action
        // 2.1. Call the method under test
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> library.registerUser(mockUser));

        // 3. Assertion
        // 3.1. Assert and verify interactions
        verify(mockUser, times(2)).getId();
        verify(mockUser, times(2)).getName();
        assertEquals("Invalid user name.", exception.getMessage());
    }

    @Test
    public void testRegisterUserWithNullName(){
        // 1.4. Stubbing - Define behavior for mockUser
        when(mockUser.getId()).thenReturn("123456789012");
        when(mockUser.getName()).thenReturn(null);

        // 2. Action
        // 2.1. Call the method under test
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> library.registerUser(mockUser));

        // 3. Assertion
        // 3.1. Assert and verify interactions
        verify(mockUser, times(2)).getId();
        verify(mockUser, times(1)).getName();
        assertEquals("Invalid user name.", exception.getMessage());
    }

    @Test
    public void testRegisterUserWithInvalidNotificationService() {
        // 1.4. Stubbing - Define behavior for mockUser
        when(mockUser.getId()).thenReturn("123456789012");
        when(mockUser.getName()).thenReturn("Mock User");
        when(mockUser.getNotificationService()).thenReturn(null);

        // 2. Action
        // 2.1. Call the method under test
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> library.registerUser(mockUser));

        // 3. Assertion
        // 3.1. Verify interactions
        verify(mockUser, times(2)).getId();
        verify(mockUser, times(2)).getName();
        assertEquals("Invalid notification service.", exception.getMessage());
    }

    @Test
    public void testRegisterUserAlreadyExists() {
        //Arrange
        // 1.4. Stubbing - Define behavior for mockUser
        when(mockUser.getId()).thenReturn("123456789012");
        when(mockUser.getName()).thenReturn("Mock User");
        when(mockUser.getNotificationService()).thenReturn(mock(NotificationService.class));

        // Mock DatabaseService
        // 1.5. Stubbing - Define behavior for mockDatabaseService
        when(mockDatabaseService.getUserById("123456789012")).thenReturn(mockUser);
        when(mockDatabaseService.getUserById(mockUser.getId())).thenReturn(mockUser);

        // 2. Action
        // 2.1. Call the method under test
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> library.registerUser(mockUser));
        assertEquals("User already exists.", exception.getMessage());

        // 3. Assertion
        // 3.1. Verify interactions
        verify(mockUser, times(4)).getId();
        verify(mockUser, times(2)).getName();
        verify(mockUser, times(1)).getNotificationService();
        // Verify that the database service's getUserById method was called with the correct ID.
        verify(mockDatabaseService, times(1)).getUserById(mockUser.getId());

        // Verify that the database service's registerUser method was not called.
        verify(mockDatabaseService, never()).registerUser(anyString(), any(User.class));
    }


    @Test
    void testReturnBook() {
        // 1.4. Stubbing - Define behavior for mockBook
        when(mockBook.isBorrowed()).thenReturn(true);
        // 1.5. Stubbing - Define behavior for mockDatabaseService
        when(mockDatabaseService.getBookByISBN("9780132130806")).thenReturn(mockBook);
        // 2. Action
        // 2.1. Call the method under test
        assertDoesNotThrow(() -> library.returnBook("9780132130806"));
        // 3. Assertion
        // 3.1. Verify interactions
        verify(mockBook, times(1)).isBorrowed();
        verify(mockBook, times(1)).returnBook();
        verify(mockDatabaseService, times(1)).returnBook("9780132130806");
        verify(mockDatabaseService, times(1)).getBookByISBN("9780132130806");
    }

    @Test
    public void testReturnBookWithInvalidISBN() {
        // 2. Action
        // 2.1. Call the method under test
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> library.returnBook("Invalid ISBN"));
        // 3. Assertion
        // 3.1. Assert interactions
        assertEquals("Invalid ISBN.", exception.getMessage());
    }

    @Test
    public void testReturnBookWithNullISBN() {
        // 2. Action
        // 2.1. Call the method under test
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> library.returnBook(null));
        // 3. Assertion
        // 3.1. Assert interactions
        assertEquals("Invalid ISBN.", exception.getMessage());
    }

    @Test
    public void testReturnBookWithInvalidBook() {
        // 1.4. Stubbing - Define behavior for mockDatabaseService
        when(mockDatabaseService.getBookByISBN("9780132130806")).thenReturn(null);
        // 2. Action
        // 2.1. Call the method under test
        BookNotFoundException exception = assertThrows(BookNotFoundException.class, () -> library.returnBook("9780132130806"));
        // 3. Assertion
        // 3.1. Assert interactions
        verify(mockDatabaseService, times(1)).getBookByISBN("9780132130806");
        assertEquals("Book not found!", exception.getMessage());
    }

    @Test
    public void testReturnBookWithBookNotBorrowed() {
        // 1.4. Stubbing - Define behavior for mockDatabaseService
        when(mockDatabaseService.getBookByISBN("9780132130806")).thenReturn(mockBook);
        // 1.5. Stubbing - Define behavior for mockBook
        when(mockBook.isBorrowed()).thenReturn(false);
        // 2. Action
        // 2.1. Call the method under test
        BookNotBorrowedException exception = assertThrows(BookNotBorrowedException.class, () -> library.returnBook("9780132130806"));
        // 3. Assertion
        // 3.1. Assert interactions
        verify(mockDatabaseService, times(1)).getBookByISBN("9780132130806");
        verify(mockBook, times(1)).isBorrowed();
        assertEquals("Book wasn't borrowed!", exception.getMessage());
    }


    // Test notifyUserWithBookReviews method

    @Test
    void testNotifyUserWithBookReviewsUsingSpy() {

        String ISBN = "9780132130806";
        String userId = "123456789012";
        // 1.4. Stubbing - Define behavior for mockUser
        when(mockBook.getISBN()).thenReturn(ISBN);
        when(mockUser.getId()).thenReturn(userId);
        when(mockBook.getTitle()).thenReturn("Book Title");

        // 1.5. Stubbing - Define behavior for mockDatabaseService
        when(mockDatabaseService.getBookByISBN(ISBN)).thenReturn(mockBook);
        when(mockDatabaseService.getUserById(userId)).thenReturn(mockUser);

        // 1.6. Stubbing - Define behavior for mockReviewService
        doReturn(spyListReviews).when(mockReviewService).getReviewsForBook(ISBN);

        // 1.7. Stubbing - Set the spyListReviews
        spyListReviews.add("Great book!");
        spyListReviews.add("Highly recommended!");

        // 2. Action
        // 2.1. Call the method under test
        assertDoesNotThrow(() -> library.notifyUserWithBookReviews(ISBN, userId));

        // Verify
        verify(mockBook, times(1)).getTitle();
        verify(mockDatabaseService, times(1)).getBookByISBN(ISBN);
        verify(mockDatabaseService, times(1)).getUserById(userId);
        verify(mockReviewService, times(1)).getReviewsForBook(ISBN);

        // Verify that the notification service was called with the correct parameters
        String expectedMessage = "Reviews for 'Book Title':\nGreat book!\nHighly recommended!";
        verify(mockUser, times(1)).sendNotification(expectedMessage);
    }

    @Test
    public void testNotifyUserWithBookReviewsInvalidISBN(){
        String ISBN = "InvalidISBN";
        String userId = "123456789012";
        // 2. Action
        // 2.1. Call the method under test
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> library.notifyUserWithBookReviews(ISBN, userId));
        // 3. Assertion
        // 3.1. Assert interactions
        assertEquals("Invalid ISBN.", exception.getMessage());
    }

    @Test
    public void testNotifyUserWithBookReviewsNullISBN(){
        String ISBN = null;
        String userId = "123456789012";
        // 2. Action
        // 2.1. Call the method under test
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> library.notifyUserWithBookReviews(ISBN, userId));
        // 3. Assertion
        // 3.1. Assert interactions
        assertEquals("Invalid ISBN.", exception.getMessage());
    }

    @Test
    public void testNotifyUserWithBookReviewsInvalidUserId(){
        String ISBN = "9780132130806";
        String userId = "Invalid User";
        // 2. Action
        // 2.1. Call the method under test
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> library.notifyUserWithBookReviews(ISBN, userId));
        // 3. Assertion
        // 3.1. Assert interactions
        assertEquals("Invalid user Id.", exception.getMessage());
    }

    @Test
    public void testNotifyUserWithBookReviewsNullUserId(){
        String ISBN = "9780132130806";
        String userId = null;
        // 2. Action
        // 2.1. Call the method under test
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> library.notifyUserWithBookReviews(ISBN, userId));
        // 3. Assertion
        // 3.1. Assert interactions
        assertEquals("Invalid user Id.", exception.getMessage());
    }

    @Test
    public void testNotifyUserWithBookReviewsBookNotFound(){
        String ISBN = "9780132130806";
        String userId = "123456789012";
        // 2. Action
        // 2.1. Call the method under test
        BookNotFoundException exception = assertThrows(BookNotFoundException.class, () -> library.notifyUserWithBookReviews(ISBN, userId));
        // 3. Assertion
        // 3.1. Assert interactions
        assertEquals("Book not found!", exception.getMessage());
    }

    @Test
    public void testNotifyUserWithBookReviewsWithNotRegisteredUser(){
        String ISBN = "9780132130806";
        String userId = "123456789012";
        // 1.4. Stubbing - Define behavior for mockDatabaseService
        when(mockDatabaseService.getBookByISBN(ISBN)).thenReturn(mockBook);
        when(mockDatabaseService.getUserById(userId)).thenReturn(null);
        // 2. Action
        // 2.1. Call the method under test
        UserNotRegisteredException exception = assertThrows(UserNotRegisteredException.class, () -> library.notifyUserWithBookReviews(ISBN, userId));
        // 3. Assertion
        // 3.1. Assert interactions
        verify(mockDatabaseService, times(1)).getBookByISBN(ISBN);
        verify(mockDatabaseService, times(1)).getUserById(userId);
        assertEquals("User not found!", exception.getMessage());
    }

    @Test
    public void testNotifyUserWithBookReviewsNoReviews(){
        String ISBN = "9780132130806";
        String userId = "123456789012";
        // 1.4. Stubbing - Define behavior for mockDatabaseService and mockReviewService
        when(mockDatabaseService.getBookByISBN(ISBN)).thenReturn(mockBook);
        when(mockDatabaseService.getUserById(userId)).thenReturn(mockUser);
        when(mockReviewService.getReviewsForBook(ISBN)).thenReturn(new ArrayList<>());
        // 2. Action
        // 2.1. Call the method under test
        NoReviewsFoundException exception = assertThrows(NoReviewsFoundException.class, () -> library.notifyUserWithBookReviews(ISBN, userId));
        // 3. Assertion
        // 3.1. Assert interactions
        verify(mockDatabaseService, times(1)).getBookByISBN(ISBN);
        verify(mockDatabaseService, times(1)).getUserById(userId);
        verify(mockReviewService, times(1)).getReviewsForBook(ISBN);
        assertEquals("No reviews found!", exception.getMessage());
    }

    @Test
    public void testNotifyUserWithBookNullReviews(){
        String ISBN = "9780132130806";
        String userId = "123456789012";
        // 1.4. Stubbing - Define behavior for mockDatabaseService and mockReviewService
        when(mockDatabaseService.getBookByISBN(ISBN)).thenReturn(mockBook);
        when(mockDatabaseService.getUserById(userId)).thenReturn(mockUser);
        when(mockReviewService.getReviewsForBook(ISBN)).thenReturn(null);
        // 2. Action
        // 2.1. Call the method under test
        NoReviewsFoundException exception = assertThrows(NoReviewsFoundException.class, () -> library.notifyUserWithBookReviews(ISBN, userId));
        // 3. Assertion
        // 3.1. Assert interactions
        verify(mockDatabaseService, times(1)).getBookByISBN(ISBN);
        verify(mockDatabaseService, times(1)).getUserById(userId);
        verify(mockReviewService, times(1)).getReviewsForBook(ISBN);
        assertEquals("No reviews found!", exception.getMessage());
    }

    @Test
    public void testNotifyUserWithBookReviews_ReviewException() throws ReviewException {
        String ISBN = "9780132130806";
        String userId = "123456789012";
        // 1.4. Stubbing - Define behavior for mockDatabaseService and mockReviewService
        when(mockDatabaseService.getBookByISBN(ISBN)).thenReturn(mockBook);
        when(mockDatabaseService.getUserById(userId)).thenReturn(mockUser);
        when(mockReviewService.getReviewsForBook(ISBN)).thenThrow(new ReviewException("Review service exception"));
        // 2. Action
        // 2.1. Call the method under test
        ReviewServiceUnavailableException exception = assertThrows(ReviewServiceUnavailableException.class,
                () -> library.notifyUserWithBookReviews(ISBN, userId));
        // 3. Assertion
        // 3.1. Assert interactions
        assertEquals("Review service unavailable!", exception.getMessage());
        verify(mockDatabaseService, times(1)).getBookByISBN(ISBN);
        verify(mockDatabaseService, times(1)).getUserById(userId);
        verify(mockReviewService, times(1)).getReviewsForBook(ISBN);

    }

    @Test
    public void testNotificationRetryFailure() throws NotificationException, ReviewException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream originalErr = System.err;
        System.setErr(new PrintStream(outputStream));
        try {
            String validISBN = "9780132130806";
            String validUserId = "123456789012";

            // 1.4. Stubbing - Define behavior for mockDatabaseService, mockReviewService and mockUser(sendNotification function)
            when(mockDatabaseService.getBookByISBN(validISBN)).thenReturn(mockBook);
            when(mockDatabaseService.getUserById(validUserId)).thenReturn(mockUser);
            spyListReviews = Arrays.asList("Review 1", "Review 2");
            when(mockReviewService.getReviewsForBook(validISBN)).thenReturn(spyListReviews);
            doThrow(new NotificationException("Notification failed!")).when(mockUser).sendNotification(anyString());
            // 2. Action
            // 2.1. Call the method under test
            NotificationException exception = assertThrows(NotificationException.class,
                    () -> new Library(mockDatabaseService, mockReviewService).notifyUserWithBookReviews(validISBN, validUserId));

            // 3. Assertion
            // 3.1. Assert interactions
            assertEquals("Notification failed!", exception.getMessage());
            // Verify that sendNotification was called 5 times
            verify(mockUser, times(5)).sendNotification(anyString());
            verify(mockBook, times(1)).getTitle();
            verify(mockDatabaseService, times(1)).getBookByISBN(validISBN);
            verify(mockDatabaseService, times(1)).getUserById(validUserId);
            verify(mockReviewService, times(1)).getReviewsForBook(validISBN);
            String expectedLog = "Notification failed! Retrying attempt 1/5\r\n" +
                    "Notification failed! Retrying attempt 2/5\r\n" +
                    "Notification failed! Retrying attempt 3/5\r\n" +
                    "Notification failed! Retrying attempt 4/5\r\n" +
                    "Notification failed! Retrying attempt 5/5\r\n";

            assertEquals(expectedLog.replaceAll("\\r\\n", "\n"), outputStream.toString().replaceAll("\\r\\n", "\n"));
            outputStream.reset();

        } finally {
            // Restore the original System.err
            System.setErr(originalErr);
        }
    }

    // Test getBookByISBN method
    @Test
    void testGetBookByISBN() {
        String userId = "987654321098";
        // 1.4. Stubbing - Define behavior for mockUser
        when(mockBook.isBorrowed()).thenReturn(false);

        // 1.5. Stubbing - Define behavior for mockDatabaseService
        when(mockDatabaseService.getBookByISBN("9780132130806")).thenReturn(mockBook);
        when(mockDatabaseService.getUserById(userId)).thenReturn(mockUser);

        // 2. Action
        // 2.1. Call the method under test
        // 3. Assertion
        // 3.1. Assert interactions
        assertDoesNotThrow(() -> {
            Book result = library.getBookByISBN("9780132130806", userId);
            assertEquals(mockBook, result);
        });
        verify(mockBook,times(1)).isBorrowed();
        verify(mockDatabaseService, times(2)).getBookByISBN("9780132130806");
        verify(mockDatabaseService, times(1)).getUserById(userId);
    }

    @Test
    public void testGetBookByISBNWithInvalidISBN() {
        String userId = "InvalidISBN";
        // 2. Action
        // 2.1. Call the method under test
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> library.getBookByISBN("Invalid ISBN", userId));
        // 3. Assertion
        // 3.1. Assert interactions
        assertEquals("Invalid ISBN.", exception.getMessage());
    }

    @Test
    public void testGetBookByISBNWithNullISBN() {
        String userId = null;
        // 2. Action
        // 2.1. Call the method under test
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> library.getBookByISBN("Invalid ISBN", userId));
        // 3. Assertion
        // 3.1. Assert interactions
        assertEquals("Invalid ISBN.", exception.getMessage());
    }


    @Test
    public void testGetBookByISBNWithInvalidUserId() {
        String userId = "InvalidUserId";
        // 2. Action
        // 2.1. Call the method under test
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> library.getBookByISBN("9780132130806", userId));
        // 3. Assertion
        // 3.1. Assert interactions
        assertEquals("Invalid user Id.", exception.getMessage());
    }

    @Test
    public void testGetBookByISBNWithNullUserId() {
        String userId = null;
        // 2. Action
        // 2.1. Call the method under test
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> library.getBookByISBN("9780132130806", userId));
        // 3. Assertion
        // 3.1. Assert interactions
        assertEquals("Invalid user Id.", exception.getMessage());
    }

    /* Parameterized test for invalid ISBNs */

    @ParameterizedTest
    @ValueSource(strings = {
            "",                // Empty string
            "123456789012",    // Too short ISBN
            "12345678901234", // Too long ISBN
            "abcdefghij1234",  // Contains non-digits
            "9780132130805", // Incorrect sum calculation
            "9780132130801", // Incorrect check digit
            "123456789012a",   // Contains a non-digit character
            "1234-5678-9012-3", // Contains hyphens in the wrong positions
            "123-456-789-01a3",  // Contains a non-digit character and hyphens
            "978-1A3456789123",
            "9781133113111"
            })
    void testInvalidISBN(String invalidISBN) {
        // 2. Action
        // 2.1. Call the method under test
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> library.addBook(new Book(invalidISBN, "Title", "Author")));
        // 3. Assertion
        // 3.1. Asserts interactions
        assertEquals("Invalid ISBN.", exception.getMessage());
    }
    @ParameterizedTest
    @NullSource
    void testNullISBN(String nullISBN) {
        // 2. Action
        // 2.1. Call the method under test
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> library.addBook(new Book(nullISBN, "Title", "Author")));
        // 3. Assertion
        // 3.1. Asserts interactions
        assertEquals("Invalid ISBN.", exception.getMessage());
    }
    ///////////////////////////////////////////////////////////////////
    @ParameterizedTest
    @ValueSource (strings = {""})
    void testInvalidTitle(String invalidTitle) {
        // 2. Action
        // 2.1. Call the method under test
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> library.addBook(new Book("9780132130806", invalidTitle, "Author")));
        // 3. Assertion
        // 3.1. Asserts interactions
        assertEquals("Invalid title.", exception.getMessage());
    }
    @ParameterizedTest
    @NullSource
    void testNullTitle(String nullTitle) {
        // 2. Action
        // 2.1. Call the method under test
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> library.addBook(new Book("9780132130806", nullTitle, "Author")));
        // 3. Assertion
        // 3.1. Asserts interactions
        assertEquals("Invalid title.", exception.getMessage());
    }
    ///////////////////////////////////////////////////////////////////

    // Parameterized test for invalid author names
    @ParameterizedTest
    @ValueSource(strings = {
            "",            // Empty string
            "123",         // Numeric name
            "-Author",     // Name starting with a special character
            "Author-",     // Name ending with a special character
            "-Author-",
            "Auth--or",    // Name with consecutive hyphens
            "Auth''or",    // Name with consecutive apostrophes
            "Auth\\or",    // Name with consecutive slash
    })
    void testInvalidAuthor(String invalidAuthor) {
        // 2. Action
        // 2.1. Call the method under test
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> library.addBook(new Book("9780132130806", "Title", invalidAuthor)));
        // 3. Assertion
        // 3.1. Asserts interactions
        assertEquals("Invalid author.", exception.getMessage());
    }
    @ParameterizedTest
    @NullSource
    void testNullAuthor(String nullAuthor) {
        // 2. Action
        // 2.1. Call the method under test
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> library.addBook(new Book("9780132130806", "Title", nullAuthor)));
        // 3. Assertion
        // 3.1. Asserts interactions
        assertEquals("Invalid author.", exception.getMessage());
    }
    ///////////////////////////////////////////////////////////////////

    /* Parameterized test for invalid user IDs */

    @ParameterizedTest
    @NullSource
    void testNullUserId(String nullUserId) {
        // 2. Action
        // 2.1. Call the method under test
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> library.registerUser(new User("Name", nullUserId, mock(NotificationService.class))));
        // 3. Assertion
        // 3.1. Asserts interactions
        assertEquals("Invalid user Id.", exception.getMessage());
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "",               // Empty string
            "1234567890123",  // Too long (13 digits)
            "abcdefghij123",  // Contains non-digits
            "12345678",       // Too short (8 digits)
            "12345678901a",   // Contains a non-digit character
            "123-456-789",    // Contains hyphens
            " 123456789012"   // Starts with a space
    })
    void testInvalidUserId(String invalidUserId) {
        // 2. Action
        // 2.1. Call the method under test
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> library.registerUser(new User("Name", invalidUserId, mock(NotificationService.class))));
        // 3. Assertion
        // 3.1. Asserts interactions
        assertEquals("Invalid user Id.", exception.getMessage());
    }
    ///////////////////////////////////////////////////////////////////

    @ParameterizedTest
    @NullSource
    void testNullName(String nullName) {
        // 2. Action
        // 2.1. Call the method under test
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> library.registerUser(new User(nullName, "123456789012", mock(NotificationService.class))));
        // 3. Assertion
        // 3.1. Asserts interactions
        assertEquals("Invalid user name.", exception.getMessage());
    }
    @ParameterizedTest
    @ValueSource (strings = {""})
        // 2. Action
        // 2.1. Call the method under test
    void testInvalidName(String invalidName) {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> library.registerUser(new User(invalidName, "123456789012", mock(NotificationService.class))));
        // 3. Assertion
        // 3.1. Asserts interactions
        assertEquals("Invalid user name.", exception.getMessage());
    }
    ///////////////////////////////////////////////////////////////////
    @ParameterizedTest
    @NullSource
    void testNullNotificationService(NotificationService nullNotificationService) {
        // 2. Action
        // 2.1. Call the method under test
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> library.registerUser(new User("Name", "123456789012", nullNotificationService)));
        // 3. Assertion
        // 3.1. Asserts interactions
        assertEquals("Invalid notification service.", exception.getMessage());
    }
    ///////////////////////////////////////////////////////////////////

}
