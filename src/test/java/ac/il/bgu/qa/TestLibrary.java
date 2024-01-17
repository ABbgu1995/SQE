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
    private DatabaseService mockDatabaseService1;
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
        verify(mockDatabaseService, times(1)).addBook("9780132130806", mockBook);

        // Verify that borrow is not called on the mockBook
        verify(mockBook, never()).borrow();
    }

    @Test
    public void testAddBookWithNullBook() {
        assertThrows(IllegalArgumentException.class, () -> library.addBook(null));
    }

    @Test
    public void testAddBookWithInvalidISBN() {
        when(mockBook.getISBN()).thenReturn("9780132130806");
        when(mockBook.getTitle()).thenReturn("Mock Book");
        when(mockBook.getAuthor()).thenReturn("Mock Author");
        when(mockBook.isBorrowed()).thenReturn(false);
        Book invalidISBNBook = new Book("Invalid ISBN", null, "Author", false);
        assertThrows(IllegalArgumentException.class, () -> library.addBook(invalidISBNBook));
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
    void testReturnBook() {
        // Mock Book
        when(mockBook.getISBN()).thenReturn("1234567890123");
        when(mockBook.isBorrowed()).thenReturn(true);

        // Mock DatabaseService
        when(mockDatabaseService.getBookByISBN("1234567890123")).thenReturn(mockBook);

        // Test returnBook method
        assertDoesNotThrow(() -> library.returnBook("1234567890123"));
        verify(mockBook, times(1)).returnBook();
        verify(mockDatabaseService, times(1)).returnBook("1234567890123");
    }

    // Test notifyUserWithBookReviews method

    // Test getBookByISBN method
    @Test
    void testGetBookByISBN() {
        // Mock Book
        when(mockBook.getISBN()).thenReturn("1234567890123");
        when(mockBook.isBorrowed()).thenReturn(false);

        // Mock DatabaseService
        when(mockDatabaseService.getBookByISBN("1234567890123")).thenReturn(mockBook);

        // Mock UserId
        String userId = "987654321098";

        // Mock DatabaseService
        when(mockDatabaseService.getUserById(userId)).thenReturn(mockUser);

        // Test getBookByISBN method
        assertDoesNotThrow(() -> {
            Book result = library.getBookByISBN("1234567890123", userId);
            assertEquals(mockBook, result);
        });
    }

    // Parameterized test for invalid ISBNs
    @ParameterizedTest
    @ValueSource(strings = {"123", "12345678901234", "invalid"})
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
                () -> library.addBook(new Book("1234567890123", "Title", invalidAuthor)));
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
