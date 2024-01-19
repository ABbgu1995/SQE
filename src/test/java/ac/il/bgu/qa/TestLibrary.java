package ac.il.bgu.qa;

import ac.il.bgu.qa.errors.*;
import ac.il.bgu.qa.services.*;;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.*;
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
    @BeforeEach
    void init() {
        MockitoAnnotations.openMocks(this);
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
        assertThrows(IllegalArgumentException.class, () -> library.addBook(null));
    }

    @Test
    public void testAddBookWithNullISBN() {
        when(mockBook.getISBN()).thenReturn("9780132130806");
        when(mockBook.getTitle()).thenReturn("Mock Book");
        when(mockBook.getAuthor()).thenReturn("Mock Author");
        when(mockBook.isBorrowed()).thenReturn(false);
        Book invalidISBNBook = new Book(null, null, "Author");
        assertThrows(IllegalArgumentException.class, () -> library.addBook(invalidISBNBook));
    }

    @Test
    public void testAddBookWithInvalidTitle() {
        when(mockBook.getISBN()).thenReturn("9780132130806");
        when(mockBook.getTitle()).thenReturn("Mock Book");
        when(mockBook.getAuthor()).thenReturn("Mock Author");
        when(mockBook.isBorrowed()).thenReturn(false);
        Book invalidTitleBookEmptyStr = new Book("9780132130806", "", "Author");
        assertThrows(IllegalArgumentException.class, () -> library.addBook(invalidTitleBookEmptyStr));
        Book invalidTitleBookNull = new Book("9780132130806", null, "Author");
        assertThrows(IllegalArgumentException.class, () -> library.addBook(invalidTitleBookNull));
    }

    @Test
    public void testAddBookWithInvalidAuthor() {
        when(mockBook.getISBN()).thenReturn("9780132130806");
        when(mockBook.getTitle()).thenReturn("Mock Book");
        when(mockBook.getAuthor()).thenReturn("Mock Author");
        when(mockBook.isBorrowed()).thenReturn(false);
        Book invalidAuthorBookEmptyStr = new Book("9780132130806", "Title", "");
        assertThrows(IllegalArgumentException.class, () -> library.addBook(invalidAuthorBookEmptyStr));
        Book invalidAuthorBookNull = new Book("9780132130806", "Title", null);
        assertThrows(IllegalArgumentException.class, () -> library.addBook(invalidAuthorBookNull));
    }

    @Test
    public void testAddBookWithInvalidBorrowed() {
        when(mockBook.getISBN()).thenReturn("9780132130806");
        when(mockBook.getTitle()).thenReturn("Mock Book");
        when(mockBook.getAuthor()).thenReturn("Mock Author");
        when(mockBook.isBorrowed()).thenReturn(false);
        Book invalidBorrowedBook = new Book("9780132130806", "Title", "Author");
        invalidBorrowedBook.borrow();
        assertThrows(IllegalArgumentException.class, () -> library.addBook(invalidBorrowedBook));
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
        when(mockUser.getId()).thenReturn("123456789012");
        when(mockUser.getName()).thenReturn("Mock User");
        when(mockUser.getNotificationService()).thenReturn(mock(NotificationService.class));
        User invalidIdUser = new User("Mock User", "Invalid ID", mock(NotificationService.class));
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> library.registerUser(invalidIdUser));
        assertEquals("Invalid user Id.", exception.getMessage());
    }

    @Test
    public void testRegisterUserWithInvalIdName() {
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
        when(mockUser.getNotificationService()).thenReturn(mock(NotificationService.class));
        User invalidNotificationServiceUser = new User("Mock User", "123456789012", null);
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> library.registerUser(invalidNotificationServiceUser));
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
    public void testBorrowBookWithInvalidUserId() {
        String userId = "InvalidUserId";
        when(mockDatabaseService.getUserById(userId)).thenReturn(null);
        when(mockDatabaseService.getBookByISBN("9780132130806")).thenReturn(mockBook);
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> library.borrowBook("9780132130806", userId));
        assertEquals("Invalid user Id.", exception.getMessage());
        IllegalArgumentException exception2 = assertThrows(IllegalArgumentException.class, () -> library.borrowBook("9780132130806", null));
        assertEquals("Invalid user Id.", exception2.getMessage());
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
    public void testReturnBookWithInvalidBook() {
        when(mockDatabaseService.getBookByISBN("9780132130806")).thenReturn(null);
        BookNotFoundException exception = assertThrows(BookNotFoundException.class, () -> library.returnBook("9780132130806"));
        assertEquals("Book not found!", exception.getMessage());
    }


    @Test
    public void testReturnBookWithInvalidBookNotBorrowed() {
        when(mockDatabaseService.getBookByISBN("9780132130806")).thenReturn(mockBook);
        when(mockBook.isBorrowed()).thenReturn(false);
        BookNotBorrowedException exception = assertThrows(BookNotBorrowedException.class, () -> library.returnBook("9780132130806"));
        assertEquals("Book wasn't borrowed!", exception.getMessage());
    }


    // Test notifyUserWithBookReviews method

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
    public void testGetBookByISBNWithInvalidUserId() {
        String userId = "InvalidUserId";
        when(mockDatabaseService.getUserById(userId)).thenReturn(null);
        when(mockDatabaseService.getBookByISBN("9780132130806")).thenReturn(mockBook);
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> library.getBookByISBN("9780132130806", userId));
        assertEquals("Invalid user Id.", exception.getMessage());
    }

    // Parameterized test for invalid ISBNs
    @ParameterizedTest
    @ValueSource(strings = {"123", "12345678901234", "invalid", ""})
    void testInvalidISBN(String invalidISBN) {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> library.addBook(new Book(invalidISBN, "Title", "Author")));
        assertEquals("Invalid ISBN.", exception.getMessage());
    }

    // Parameterized test for invalid author names
    @ParameterizedTest
    @ValueSource(strings = {"", "123", "Author123", "Author$Name", "Author--Name"})
    void testInvalidAuthor(String invalidAuthor) {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> library.addBook(new Book("9780132130806", "Title", invalidAuthor)));
        assertEquals("Invalid author.", exception.getMessage());
    }

    // Parameterized test for invalid user IDs
    @ParameterizedTest
    @ValueSource(strings = {"", "123", "invalid"})
    void testInvalidUserId(String invalidUserId) {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> library.registerUser(new User("Name", invalidUserId, mock(NotificationService.class))));
        assertEquals("Invalid user Id.", exception.getMessage());
    }

    // Add more tests as needed...

}
