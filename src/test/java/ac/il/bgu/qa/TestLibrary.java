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
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
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
        MockitoAnnotations.openMocks(this);
        spyListReviews.clear();
        library = new Library(mockDatabaseService, mockReviewService);
    }

    // Test addBook method
    @Test
    void testAddBook() {
        // Mock Book
        when(mockBook.getISBN()).thenReturn("9780132130806");
        when(mockBook.getTitle()).thenReturn("Mock Book");
        when(mockBook.getAuthor()).thenReturn("Mock Author");
        when(mockBook.isBorrowed()).thenReturn(false);

        // Mock DatabaseService
        when(mockDatabaseService.getBookByISBN("1234567890123")).thenReturn(null);

        // Test addBook method, if the addBook method throws an exception during its execution, the test would fail.
        assertDoesNotThrow(() -> library.addBook(mockBook));

        // the addBook method is void so use verify that the function has been called
        verify(mockDatabaseService).addBook("9780132130806", mockBook);

        // Verify that borrow is not called on the mockBook
        verify(mockBook, never()).borrow();
    }

    @Test
    public void testAddBookWithNullBook() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> library.addBook(null));
        assertEquals("Invalid book.", exception.getMessage());
    }

    @Test
    public void testAddBookWithNullISBN() {
        when(mockBook.getISBN()).thenReturn(null);
        when(mockBook.getTitle()).thenReturn("Mock Book");
        when(mockBook.getAuthor()).thenReturn("Mock Author");
        when(mockBook.isBorrowed()).thenReturn(false);
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> library.addBook(mockBook));
        assertEquals("Invalid ISBN.", exception.getMessage());
    }

    @Test
    public void testAddBookWithInvalidISBN() {
        when(mockBook.getISBN()).thenReturn("invalidISBN");
        when(mockBook.getTitle()).thenReturn("Mock Book");
        when(mockBook.getAuthor()).thenReturn("Mock Author");
        when(mockBook.isBorrowed()).thenReturn(false);
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> library.addBook(mockBook));
        assertEquals("Invalid ISBN.", exception.getMessage());
    }

    @Test
    public void testAddBookWithNullTitle() {
        when(mockBook.getISBN()).thenReturn("9780132130806");
        when(mockBook.getTitle()).thenReturn(null);
        when(mockBook.getAuthor()).thenReturn("Mock Author");
        when(mockBook.isBorrowed()).thenReturn(false);
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> library.addBook(mockBook));
        assertEquals("Invalid title.", exception.getMessage());
    }

    @Test
    public void testAddBookWithInvalidTitle() {
        when(mockBook.getISBN()).thenReturn("9780132130806");
        when(mockBook.getTitle()).thenReturn("");
        when(mockBook.getAuthor()).thenReturn("Mock Author");
        when(mockBook.isBorrowed()).thenReturn(false);
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> library.addBook(mockBook));
        assertEquals("Invalid title.", exception.getMessage());
    }

    @Test
    public void testAddBookWithNullAuthor() {
        when(mockBook.getISBN()).thenReturn("9780132130806");
        when(mockBook.getTitle()).thenReturn("Mock Book");
        when(mockBook.getAuthor()).thenReturn(null);
        when(mockBook.isBorrowed()).thenReturn(false);
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> library.addBook(mockBook));
        assertEquals("Invalid author.", exception.getMessage());
    }

    @Test
    public void testAddBookWithInvalidAuthor() {
        when(mockBook.getISBN()).thenReturn("9780132130806");
        when(mockBook.getTitle()).thenReturn("Mock Book");
        when(mockBook.getAuthor()).thenReturn("");
        when(mockBook.isBorrowed()).thenReturn(false);
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> library.addBook(mockBook));
        assertEquals("Invalid author.", exception.getMessage());
    }

    @Test
    public void testAddBookWithInvalidBorrowed() {
        when(mockBook.getISBN()).thenReturn("9780132130806");
        when(mockBook.getTitle()).thenReturn("Mock Book");
        when(mockBook.getAuthor()).thenReturn("Mock Author");
        when(mockBook.isBorrowed()).thenReturn(true);
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> library.addBook(mockBook));
        assertEquals("Book with invalid borrowed state.", exception.getMessage());
    }

    // Test registerUser method
    @Test
    void testRegisterUser() {
        // Mock User
        when(mockUser.getId()).thenReturn("123456789012");
        when(mockUser.getName()).thenReturn("Mock User");
        when(mockUser.getNotificationService()).thenReturn(mock(NotificationService.class));

        // Mock DatabaseService
        when(mockDatabaseService.getUserById("123456789012")).thenReturn(null);

        // Test registerUser method
        assertDoesNotThrow(() -> library.registerUser(mockUser));
        verify(mockDatabaseService, times(1)).registerUser("123456789012", mockUser);
    }

    @Test
    public void testRegisterUserWithNullUser() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> library.registerUser(null));
        assertEquals("Invalid user.", exception.getMessage());
    }

    @Test
    public void testRegisterUserWithInvalidId() {
        when(mockUser.getId()).thenReturn("1234567890123");
        when(mockUser.getName()).thenReturn("Mock User");
        when(mockUser.getNotificationService()).thenReturn(mock(NotificationService.class));
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> library.registerUser(mockUser));
        assertEquals("Invalid user Id.", exception.getMessage());
    }

    @Test
    public void testRegisterUserWithNullId(){
        when(mockUser.getId()).thenReturn(null);
        when(mockUser.getName()).thenReturn("Mock User");
        when(mockUser.getNotificationService()).thenReturn(mock(NotificationService.class));
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> library.registerUser(mockUser));
        assertEquals("Invalid user Id.", exception.getMessage());
    }

    @Test
    public void testRegisterUserWithInvalidIdName() {
        when(mockUser.getId()).thenReturn("123456789012");
        when(mockUser.getName()).thenReturn("Mock User");
        when(mockUser.getNotificationService()).thenReturn(mock(NotificationService.class));
        User invalidNameUserEmptyStr = new User("", "123456789012", mock(NotificationService.class));
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> library.registerUser(invalidNameUserEmptyStr));
        assertEquals("Invalid user name.", exception.getMessage());
        User invalidNameUserNull = new User(null , "123456789012", mock(NotificationService.class));
        IllegalArgumentException exception2 = assertThrows(IllegalArgumentException.class, () -> library.registerUser(invalidNameUserNull));
        assertEquals("Invalid user name.", exception2.getMessage());
    }

    @Test
    public void testRegisterUserWithInvalidNotificationService() {
        when(mockUser.getId()).thenReturn("123456789012");
        when(mockUser.getName()).thenReturn("Mock User");
        when(mockUser.getNotificationService()).thenReturn(null);
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> library.registerUser(mockUser));
        assertEquals("Invalid notification service.", exception.getMessage());
    }

    @Test
    public void testRegisterUserAlreadyExists() {
        //Arrange
        when(mockUser.getId()).thenReturn("123456789012");
        when(mockUser.getName()).thenReturn("Mock User");
        when(mockUser.getNotificationService()).thenReturn(mock(NotificationService.class));

        // Mock DatabaseService
        when(mockDatabaseService.getUserById("123456789012")).thenReturn(mockUser);

        // Mock the behavior of the database service to return an existing user when queried with the ID.
        when(mockDatabaseService.getUserById(mockUser.getId())).thenReturn(mockUser);

        // Act and Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> library.registerUser(mockUser));
        assertEquals("User already exists.", exception.getMessage());

        // Verify that the database service's getUserById method was called with the correct ID.
        verify(mockDatabaseService, times(1)).getUserById(mockUser.getId());

        // Verify that the database service's registerUser method was not called.
        verify(mockDatabaseService, never()).registerUser(anyString(), any(User.class));
    }

    // Test borrowBook method
    @Test
    void testBorrowBook() {
        // Mock Book
        when(mockBook.getISBN()).thenReturn("9780132130806");
        when(mockBook.isBorrowed()).thenReturn(false);

        // Mock DatabaseService
        when(mockDatabaseService.getBookByISBN("9780132130806")).thenReturn(mockBook);

        // Mock UserId
        String userId = "987654321098";

        // Mock DatabaseService
        when(mockDatabaseService.getUserById(userId)).thenReturn(mockUser);

        // Test borrowBook method
        assertDoesNotThrow(() -> library.borrowBook("9780132130806", userId));
        verify(mockBook, times(1)).borrow();
        verify(mockDatabaseService, times(1)).borrowBook("9780132130806", userId);
    }

    @Test
    public void testBorrowBookWithInvalidISBN() {
        String userId = "InvalidISBN";
        when(mockDatabaseService.getUserById(userId)).thenReturn(mockUser);
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> library.borrowBook("Invalid ISBN", userId));
        assertEquals("Invalid ISBN.", exception.getMessage());
    }

    @Test
    public void testBorrowBookWithNullISBN() {
        String userId = "XXXXXXXXXXXX";
        when(mockDatabaseService.getUserById(userId)).thenReturn(mockUser);
        IllegalArgumentException exception2 = assertThrows(IllegalArgumentException.class, () -> library.borrowBook(null, userId));
        assertEquals("Invalid ISBN.", exception2.getMessage());
    }

    @Test
    public void testBorrowBookWithInvalidUserId() {
        String userId = "InvalidUserId";
        when(mockDatabaseService.getUserById(userId)).thenReturn(null);
        when(mockDatabaseService.getBookByISBN("9780132130806")).thenReturn(mockBook);
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> library.borrowBook("9780132130806", userId));
        assertEquals("Invalid user Id.", exception.getMessage());
    }

    @Test
    public void testBorrowBookWithNullUserId() {
        String userId = "InvalidUserId";
        when(mockDatabaseService.getUserById(userId)).thenReturn(null);
        when(mockDatabaseService.getBookByISBN("9780132130806")).thenReturn(mockBook);
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> library.borrowBook("9780132130806", null));
        assertEquals("Invalid user Id.", exception.getMessage());
    }

    @Test
    public void testBorrowBookWithInvalidBook() {
        String userId = "123456789012";
        when(mockDatabaseService.getUserById(userId)).thenReturn(mockUser);
        when(mockDatabaseService.getBookByISBN("9780132130806")).thenReturn(null);
        BookNotFoundException exception = assertThrows(BookNotFoundException.class, () -> library.borrowBook("9780132130806", userId));
        assertEquals("Book not found!", exception.getMessage());
    }

    @Test
    public void testBorrowBookWithInvalidBookBorrowed() {
        String userId = "123456789012";
        when(mockDatabaseService.getUserById(userId)).thenReturn(mockUser);
        when(mockDatabaseService.getBookByISBN("9780132130806")).thenReturn(mockBook);
        when(mockBook.isBorrowed()).thenReturn(true);
        BookAlreadyBorrowedException exception = assertThrows(BookAlreadyBorrowedException.class, () -> library.borrowBook("9780132130806", userId));
        assertEquals("Book is already borrowed!", exception.getMessage());
    }

    @Test
    public void testBorrowBookByNotRegisteredUser(){
        String userId = "123456789012";
        when(mockDatabaseService.getUserById(userId)).thenReturn(null);
        when(mockDatabaseService.getBookByISBN("9780132130806")).thenReturn(mockBook);
        UserNotRegisteredException exception = assertThrows(UserNotRegisteredException.class, () -> library.borrowBook("9780132130806", userId));
        assertEquals("User not found!", exception.getMessage());
    }

    @Test
    void testReturnBook() {
        // Mock Book
        when(mockBook.getISBN()).thenReturn("9780132130806");
        when(mockBook.isBorrowed()).thenReturn(true);

        // Mock DatabaseService
        when(mockDatabaseService.getBookByISBN("9780132130806")).thenReturn(mockBook);

        // Test returnBook method
        assertDoesNotThrow(() -> library.returnBook("9780132130806"));
        verify(mockBook, times(1)).returnBook();
        verify(mockDatabaseService, times(1)).returnBook("9780132130806");
    }

    @Test
    public void testReturnBookWithInvalidISBN() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> library.returnBook("Invalid ISBN"));
        assertEquals("Invalid ISBN.", exception.getMessage());
    }

    @Test
    public void testReturnBookWithNullISBN() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> library.returnBook(null));
        assertEquals("Invalid ISBN.", exception.getMessage());
    }

    @Test
    public void testReturnBookWithInvalidBook() {
        when(mockDatabaseService.getBookByISBN("9780132130806")).thenReturn(null);
        BookNotFoundException exception = assertThrows(BookNotFoundException.class, () -> library.returnBook("9780132130806"));
        assertEquals("Book not found!", exception.getMessage());
    }

    @Test
    public void testReturnBookWithBookNotBorrowed() {
        when(mockDatabaseService.getBookByISBN("9780132130806")).thenReturn(mockBook);
        when(mockBook.isBorrowed()).thenReturn(false);
        BookNotBorrowedException exception = assertThrows(BookNotBorrowedException.class, () -> library.returnBook("9780132130806"));
        assertEquals("Book wasn't borrowed!", exception.getMessage());
    }


    // Test notifyUserWithBookReviews method

    @Test
    void testNotifyUserWithBookReviewsUsingSpy() {
        // Mock data
        String ISBN = "9780132130806";
        String userId = "123456789012";

        // Mock Book
        when(mockBook.getISBN()).thenReturn(ISBN);
        when(mockBook.isBorrowed()).thenReturn(false);

        // Mock User
        when(mockUser.getId()).thenReturn(userId);
        when(mockUser.getName()).thenReturn("Mock User");
        when(mockUser.getNotificationService()).thenReturn(mockNotificationService);

        // Mock DatabaseService
        when(mockDatabaseService.getBookByISBN(ISBN)).thenReturn(mockBook);
        when(mockDatabaseService.getUserById(userId)).thenReturn(mockUser);

        doReturn(spyListReviews).when(mockReviewService).getReviewsForBook(ISBN);

        // Add some reviews to the spy list
        spyListReviews.add("Great book!");
        spyListReviews.add("Highly recommended!");

        // Test notifyUserWithBookReviews method
        assertDoesNotThrow(() -> library.notifyUserWithBookReviews(ISBN, userId));

//        // Verify that the notification service was called with the correct parameters
//        String expectedMessage = "Book reviews for ISBN 9780132130806:\n1. Great book!\n2. Highly recommended!";
//        verify(mockNotificationService, times(1)).notifyUser(userId, expectedMessage);
    }

    @Test
    public void testNotifyUserWithBookReviewsInvalidISBN(){
        String ISBN = "InvalidISBN";
        String userId = "123456789012";
        when(mockDatabaseService.getBookByISBN(ISBN)).thenReturn(null);
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> library.notifyUserWithBookReviews(ISBN, userId));
        assertEquals("Invalid ISBN.", exception.getMessage());
    }

    @Test
    public void testNotifyUserWithBookReviewsNullISBN(){
        String ISBN = null;
        String userId = "123456789012";
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> library.notifyUserWithBookReviews(ISBN, userId));
        assertEquals("Invalid ISBN.", exception.getMessage());
    }

    @Test
    public void testNotifyUserWithBookReviewsInvalidUserId(){
        String ISBN = "9780132130806";
        String userId = "Invalid User";
        when(mockDatabaseService.getBookByISBN(ISBN)).thenReturn(mockBook);
        when(mockDatabaseService.getUserById(userId)).thenReturn(null);
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> library.notifyUserWithBookReviews(ISBN, userId));
        assertEquals("Invalid user Id.", exception.getMessage());
    }

    @Test
    public void testNotifyUserWithBookReviewsNullUserId(){
        String ISBN = "9780132130806";
        String userId = null;
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> library.notifyUserWithBookReviews(ISBN, userId));
        assertEquals("Invalid user Id.", exception.getMessage());
    }

    @Test
    public void testNotifyUserWithBookReviewsInvalidBook(){
        String ISBN = "9780132130806";
        String userId = "123456789012";
        when(mockDatabaseService.getBookByISBN(ISBN)).thenReturn(null);
        BookNotFoundException exception = assertThrows(BookNotFoundException.class, () -> library.notifyUserWithBookReviews(ISBN, userId));
        assertEquals("Book not found!", exception.getMessage());
    }

    @Test
    public void testNotifyUserWithBookReviewsWithNotRegisteredUser(){
        String ISBN = "9780132130806";
        String userId = "123456789012";
        when(mockDatabaseService.getBookByISBN(ISBN)).thenReturn(mockBook);
        when(mockDatabaseService.getUserById(userId)).thenReturn(null);
        UserNotRegisteredException exception = assertThrows(UserNotRegisteredException.class, () -> library.notifyUserWithBookReviews(ISBN, userId));
        assertEquals("User not found!", exception.getMessage());
    }

    @Test
    public void testNotifyUserWithBookReviewsNoReviews(){
        String ISBN = "9780132130806";
        String userId = "123456789012";
        when(mockDatabaseService.getBookByISBN(ISBN)).thenReturn(mockBook);
        when(mockDatabaseService.getUserById(userId)).thenReturn(mockUser);
        when(mockReviewService.getReviewsForBook(ISBN)).thenReturn(new ArrayList<>());
        NoReviewsFoundException exception = assertThrows(NoReviewsFoundException.class, () -> library.notifyUserWithBookReviews(ISBN, userId));
        assertEquals("No reviews found!", exception.getMessage());
    }

    @Test
    public void testNotifyUserWithBookReviews_ReviewException() throws ReviewException {
        String ISBN = "9780132130806";
        String userId = "123456789012";

        // Mock necessary dependencies
        when(mockDatabaseService.getBookByISBN(ISBN)).thenReturn(mockBook);
        when(mockDatabaseService.getUserById(userId)).thenReturn(mockUser);

        // Mock ReviewService to throw ReviewException
        when(mockReviewService.getReviewsForBook(ISBN)).thenThrow(new ReviewException("Review service exception"));

        // Test and expect ReviewServiceUnavailableException
        ReviewServiceUnavailableException exception = assertThrows(ReviewServiceUnavailableException.class,
                () -> library.notifyUserWithBookReviews(ISBN, userId));

        assertEquals("Review service unavailable!", exception.getMessage());

        // Verify that the review service was called with the correct ISBN
        verify(mockReviewService, times(1)).getReviewsForBook(ISBN);
    }

//    @Test
//    void testSendNotificationWithRetries() {
//
//    }

//    @Test
//    public void testNotificationRetryFailure() {
//        String notificationMessage = "Test message";
//
//        // Mock User
//        when(mockUser.getId()).thenReturn("123456789012");
//        when(mockUser.getName()).thenReturn("Mock User");
//        when(mockUser.getNotificationService()).thenReturn(mock(NotificationService.class));
//
//        // Mock NotificationService to throw NotificationException for all 5 attempts
//        doThrow(new NotificationException("Notification failed!")).when(mockUser).sendNotification(notificationMessage);
//
//        // Execute the method under test and expect NotificationException
//        NotificationException exception = assertThrows(NotificationException.class,
//                () -> library.notifyUserWithBookReviews("9780132130806", "123456789012"));
//
//        // Verify that sendNotification was called 5 times
//        verify(mockUser, times(5)).sendNotification(notificationMessage);
//
//        // Verify the exception message
//        assertEquals("Notification failed!", exception.getMessage());
//    }




    // Test getBookByISBN method
    @Test
    void testGetBookByISBN() {
        // Mock Book
        when(mockBook.getISBN()).thenReturn("9780132130806");
        when(mockBook.isBorrowed()).thenReturn(false);

        // Mock DatabaseService
        when(mockDatabaseService.getBookByISBN("9780132130806")).thenReturn(mockBook);

        // Mock UserId
        String userId = "987654321098";

        // Mock DatabaseService
        when(mockDatabaseService.getUserById(userId)).thenReturn(mockUser);

        // Test getBookByISBN method
        assertDoesNotThrow(() -> {
            Book result = library.getBookByISBN("9780132130806", userId);
            assertEquals(mockBook, result);
        });
    }

    @Test
    public void testGetBookByISBNWithInvalidISBN() {
        String userId = "InvalidISBN";
        when(mockDatabaseService.getUserById(userId)).thenReturn(mockUser);
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> library.getBookByISBN("Invalid ISBN", userId));
        assertEquals("Invalid ISBN.", exception.getMessage());
    }

    @Test
    public void testGetBookByISBNWithNullISBN() {
        String userId = null;
        when(mockDatabaseService.getUserById(userId)).thenReturn(mockUser);
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> library.getBookByISBN("Invalid ISBN", userId));
        assertEquals("Invalid ISBN.", exception.getMessage());
    }


    @Test
    public void testGetBookByISBNWithInvalidUserId() {
        String userId = "InvalidUserId";
        when(mockDatabaseService.getUserById(userId)).thenReturn(null);
        when(mockDatabaseService.getBookByISBN("9780132130806")).thenReturn(mockBook);
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> library.getBookByISBN("9780132130806", userId));
        assertEquals("Invalid user Id.", exception.getMessage());
    }

    @Test
    public void testGetBookByISBNWithNullUserId() {
        String userId = null;
        when(mockDatabaseService.getUserById(userId)).thenReturn(null);
        when(mockDatabaseService.getBookByISBN("9780132130806")).thenReturn(mockBook);
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> library.getBookByISBN("9780132130806", userId));
        assertEquals("Invalid user Id.", exception.getMessage());
    }






    /* Parameterized test for invalid ISBNs */

    @ParameterizedTest
    @ValueSource(strings = {
            "",                // Empty string
            "123456789012",    // Too short ISBN
            "abcdefghij1234",  // Contains non-digits
            "123456789012a",   // Contains a non-digit character
            "1234-5678-9012-3", // Contains hyphens in the wrong positions
            "123-456-789-01a3"  // Contains a non-digit character and hyphens
            })
    void testInvalidISBN(String invalidISBN) {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> library.addBook(new Book(invalidISBN, "Title", "Author")));
        assertEquals("Invalid ISBN.", exception.getMessage());
    }
    @ParameterizedTest
    @NullSource
    void testNullISBN(String nullISBN) {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> library.addBook(new Book(nullISBN, "Title", "Author")));
        assertEquals("Invalid ISBN.", exception.getMessage());
    }
    ///////////////////////////////////////////////////////////////////
    @ParameterizedTest
    @ValueSource (strings = {""})
    void testInvalidTitle(String invalidTitle) {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> library.addBook(new Book("9780132130806", invalidTitle, "Author")));
        assertEquals("Invalid title.", exception.getMessage());
    }
    @ParameterizedTest
    @NullSource
    void testNullTitle(String nullTitle) {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> library.addBook(new Book("9780132130806", nullTitle, "Author")));
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
            "Auth--or",    // Name with consecutive hyphens
            "Auth''or",    // Name with consecutive apostrophes
//            "Auth..or",    // Name with consecutive dots
//            "Auth or",     // Name with a special character after a space
//            "Auth. or"     // Name with a special character before a space
    })
    void testInvalidAuthor(String invalidAuthor) {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> library.addBook(new Book("9780132130806", "Title", invalidAuthor)));
        assertEquals("Invalid author.", exception.getMessage());
    }
    @ParameterizedTest
    @NullSource
    void testNullAuthor(String nullAuthor) {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> library.addBook(new Book("9780132130806", "Title", nullAuthor)));
        assertEquals("Invalid author.", exception.getMessage());
    }
    ///////////////////////////////////////////////////////////////////

    /* Parameterized test for invalid user IDs */

    @ParameterizedTest
    @NullSource
    void testNullUserId(String nullUserId) {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> library.registerUser(new User("Name", nullUserId, mock(NotificationService.class))));
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
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> library.registerUser(new User("Name", invalidUserId, mock(NotificationService.class))));
        assertEquals("Invalid user Id.", exception.getMessage());
    }
    ///////////////////////////////////////////////////////////////////

    @ParameterizedTest
    @NullSource
    void testNullName(String nullName) {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> library.registerUser(new User(nullName, "123456789012", mock(NotificationService.class))));
        assertEquals("Invalid user name.", exception.getMessage());
    }
    @ParameterizedTest
    @ValueSource (strings = {""})
    void testInvalidName(String invalidName) {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> library.registerUser(new User(invalidName, "123456789012", mock(NotificationService.class))));
        assertEquals("Invalid user name.", exception.getMessage());
    }
    ///////////////////////////////////////////////////////////////////
    @ParameterizedTest
    @NullSource
    void testNullNotificationService(NotificationService nullNotificationService) {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> library.registerUser(new User("Name", "123456789012", nullNotificationService)));
        assertEquals("Invalid notification service.", exception.getMessage());
    }
    ///////////////////////////////////////////////////////////////////


}
