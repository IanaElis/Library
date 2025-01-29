package controllers;

import entity.*;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testfx.framework.junit.ApplicationTest;
import services.BorrowingService;
import services.UserService;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;
import static org.testfx.api.FxAssert.verifyThat;
import static org.testfx.matcher.control.TableViewMatchers.hasTableCell;
import static org.testfx.util.WaitForAsyncUtils.waitForFxEvents;

public class ReadersPaneControllerTest extends ApplicationTest{

    @Mock
    private UserService userService;
    private ReadersPaneController readersPaneController;
    private AlertMessage mockAlertMessage;
    @Mock
    private BorrowingService borrowingService;
    @Mock
    Book book;
    @Mock
    private User loggedUser;

    @Override
    public void start(Stage stage) throws Exception {
        MockitoAnnotations.openMocks(this);

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/readersPane.fxml"));
        Parent root = loader.load();
        readersPaneController = loader.getController();
        readersPaneController.setParam(userService, borrowingService);
        readersPaneController.setLoggedUser(loggedUser);
        mockAlertMessage = spy(readersPaneController.alert);
        readersPaneController.alert = mockAlertMessage;

        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }


    @Test
    public void testShowReadersAndBorBooks() {
        User mockUser1 = new User();
        mockUser1.setUserId(1);
        mockUser1.setName("John Doe");
        mockUser1.setEmail("john@example.com");
        mockUser1.setPhoneNumber(1234567890);
        mockUser1.setApprovalDate(LocalDate.now());

        User mockUser2 = new User();
        mockUser2.setUserId(2);
        mockUser2.setName("Jane Smith");
        mockUser2.setEmail("jane@example.com");
        mockUser2.setPhoneNumber(987654321);
        mockUser2.setApprovalDate(LocalDate.now());

        Borrowing mockBorrowing1 = new Borrowing(new BorrowingStatus(1,null), mockUser1, book, LocalDate.now(), LocalDate.now().plusDays(7), null, false);
        Borrowing mockBorrowing2 = new Borrowing(new BorrowingStatus(2,null), mockUser2, book, LocalDate.now(), LocalDate.now().plusDays(14), null, false);

        when(userService.getAllReaders()).thenReturn(Arrays.asList(mockUser1, mockUser2));
        when(borrowingService.getAllBorrowingsByUser(1)).thenReturn(Arrays.asList(mockBorrowing1));
        when(borrowingService.getAllBorrowingsByUser(2)).thenReturn(Arrays.asList(mockBorrowing2));

        readersPaneController.showReadersAndBorBooks();

        waitForFxEvents();

        verifyThat("#readers_table", hasTableCell(mockUser1.getUserId()));
        verifyThat("#readers_table", hasTableCell(mockUser2.getUserId()));
    }

    @Test
    public void testUpdateReader() {
        User mockUser = new User();
        mockUser.setUserId(1);
        mockUser.setName("John Doe");
        mockUser.setEmail("john@example.com");
        mockUser.setPassword("password");

        doNothing().when(userService).updateUser(mockUser);

        readersPaneController.readers.add(mockUser);

        clickOn("#readers_table");
        clickOn("Update");

        waitForFxEvents();

        verify(userService).updateUser(mockUser);
        verify(mockAlertMessage).successMessage("Information updated", "Congrats!");
    }

    @Test
    public void testDeleteReader() {
        User mockUser = new User();
        mockUser.setUserId(1);
        mockUser.setName("John Doe");

        doNothing().when(userService).deleteUser(mockUser);

        readersPaneController.readers.add(mockUser);

        clickOn("#readers_table");
        clickOn("John Doe");
        clickOn("#deleteReader_btn");

        waitForFxEvents();

        verify(userService).deleteUser(mockUser);
    }

    @Test
    public void testReturnBook() {
        Borrowing mockBorrowing = new Borrowing();
        mockBorrowing.setBorId(1);
        Book book = new Book();
        book.setTitle("Book Title");
        book.setAuthor("Author");
        mockBorrowing.setBook(book);
        mockBorrowing.setUser(new User());
        mockBorrowing.setStatus(new BorrowingStatus(1, "Borrowed"));

        when(borrowingService.getAllBorrowingsByUser(1)).thenReturn(Arrays.asList(mockBorrowing));
        when(borrowingService.returnBook(mockBorrowing)).thenReturn("Book returned successfully");

        readersPaneController.borrowings.add(mockBorrowing);

        clickOn("#borBooks_table");
        clickOn("Book Title");
        clickOn("#return_btn");

        waitForFxEvents();

        AlertMessage alert = readersPaneController.alert;
        DialogPane dialogPane = alert.getAlert().getDialogPane();
        Node noButton = dialogPane.lookupButton(ButtonType.CANCEL);

        clickOn(noButton);

        waitForFxEvents();

        verify(mockAlertMessage).successMessage("Book returned successfully", "Congrats");
        verify(borrowingService).returnBook(mockBorrowing);
    }

    @Test
    public void testUpdateList() {
        when(borrowingService.getAllBorrowingsByUser(1)).thenReturn(mockBorrowingsList());

        readersPaneController.updateList(1);

        waitForFxEvents();

        TableView<?> borBooksTable = lookup("#borBooks_table").query();
        assertEquals(2, borBooksTable.getItems().size());
    }

    private List<Borrowing> mockBorrowingsList() {
        List<Borrowing> borrowings = new ArrayList<>();
        Borrowing borrowing1 = new Borrowing();
        borrowing1.setBorId(1);
        Book b1 = new Book();
        b1.setTitle("Book 1");
        b1.setAuthor("Author 1");
        borrowing1.setBook(b1);
        borrowing1.setUser(new User());
        borrowing1.setStatus(new BorrowingStatus(1, "Borrowed"));

        Borrowing borrowing2 = new Borrowing();
        borrowing2.setBorId(2);
        Book b2 = new Book();
        b2.setTitle("Book 2");
        b2.setAuthor("Author 2");
        borrowing2.setBook(b2);
        borrowing2.setUser(new User());
        borrowing2.setStatus(new BorrowingStatus(1, null));

        borrowings.add(borrowing1);
        borrowings.add(borrowing2);
        return borrowings;
    }

}