package controllers;

import entity.Book;
import entity.Borrowing;
import entity.User;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Pair;
import javafx.util.converter.IntegerStringConverter;
import services.BookService;
import services.BorrowingService;
import services.UserService;

import java.io.IOException;
import java.time.LocalDate;

public class AdminController {
    @FXML
    private AnchorPane books_form;
    @FXML
    private AnchorPane dashboard_form;
    @FXML
    private AnchorPane notifications_form;
    @FXML
    private AnchorPane operators_form;
    @FXML
    private AnchorPane profile_form;

    @FXML
    private AnchorPane reports_form;
    @FXML
    private AnchorPane readers_form;
    @FXML
    private Button books_btn;
    @FXML
    private Button dashboard_btn;
    @FXML
    private Button exit_btn;
    @FXML
    private Button notifications_btn;
    @FXML
    private Button operators_btn;
    @FXML
    private Button profile_btn;
    @FXML
    private Button readers_btn;
    @FXML
    private Button reports_btn;

    @FXML
    private Label username_label;

    @FXML
    private TableView<User> operators_table;
    @FXML
    private TableColumn<User, LocalDate> operators_col_date;
    @FXML
    private TableColumn<User, String> operators_col_email;
    @FXML
    private TableColumn<User, Integer> operators_col_id;
    @FXML
    private TableColumn<User, String> operators_col_name;
    @FXML
    private TableColumn<User, Integer> operators_col_number;

    @FXML
    private TableView<Book> books_table;
    @FXML
    private TableColumn<Book, String> genre;
    @FXML
    private TableColumn<Book, Integer> isbn;
    @FXML
    private TableColumn<Book, String> author;
    @FXML
    private TableColumn<Book, Integer> av_q;
    @FXML
    private TableColumn<Book, String > publisher;
    @FXML
    private TableColumn<Book, String> status;
    @FXML
    private TableColumn<Book, String> title;
    @FXML
    private TableColumn<Book, Integer> total_q;
    @FXML
    private TableColumn<Book, LocalDate> year;

    @FXML
    private TableView<User> readers_table;
    @FXML
    private TableColumn<User, Integer> readers_borBooksNum;
    @FXML
    private TableColumn<User, LocalDate> readers_date;
    @FXML
    private TableColumn<User, String> readers_email;
    @FXML
    private TableColumn<User, Integer> readers_id;
    @FXML
    private TableColumn<User, String> readers_name;
    @FXML
    private TableColumn<User, Integer> readers_number;

    @FXML
    private TableView<Borrowing> borBooks_table;
    @FXML
    private TableColumn<Borrowing, LocalDate> borBooks_actualDate;
    @FXML
    private TableColumn<Borrowing, String> borBooks_author;
    @FXML
    private TableColumn<Borrowing, String> borBooks_borStatus;
    @FXML
    private TableColumn<Borrowing, LocalDate> borBooks_dateOfB;
    @FXML
    private TableColumn<Borrowing, LocalDate> borBooks_expDateOfRet;
    @FXML
    private TableColumn<Borrowing, Integer> borBooks_id;
    @FXML
    private TableColumn<Borrowing, String> borBooks_title;

    @FXML
    private Button return_btn;
    @FXML
    private Button updateBook_btn;
    @FXML
    private Button archiveBook_btn;
    @FXML
    private Button delete_btn;
    @FXML
    private Button issueBook_btn;


    private User loggedUser;
    private UserService userService;
    private BookService bookService;
    private BorrowingService borrowingService;
    private ObservableList<Borrowing> borrowings = FXCollections.observableArrayList();
    private AlertMessage alert = new AlertMessage();

    public AdminController() {
        userService = new UserService();
        bookService = new BookService();
        borrowingService = new BorrowingService();
    }

    public void setLoggedUser(User user) {
        this.loggedUser = user;
        showName();
    }

    public void switchForm(ActionEvent event) {
        if(event.getSource() == dashboard_btn) {
            setFormsInvisible();
            dashboard_form.setVisible(true);
        }
        else if(event.getSource() == operators_btn) {
            setFormsInvisible();
            operators_form.setVisible(true);
            showOperators();
        }
        else if(event.getSource() == readers_btn) {
            setFormsInvisible();
            readers_form.setVisible(true);
            showReadersAndBorBooks();
        }
        else if(event.getSource() == books_btn) {
            setFormsInvisible();
            books_form.setVisible(true);
            showBooks();
        }
        else if(event.getSource() == notifications_btn) {
            setFormsInvisible();
            notifications_form.setVisible(true);
        }
        else if(event.getSource() == reports_btn) {
            setFormsInvisible();
            reports_form.setVisible(true);
        }
        else if(event.getSource() == profile_btn) {
            setFormsInvisible();
            profile_form.setVisible(true);
        }
        else if(event.getSource() == exit_btn) {
            setFormsInvisible();
        }
    }

    public void setFormsInvisible(){
        dashboard_form.setVisible(false);
        operators_form.setVisible(false);
        readers_form.setVisible(false);
        books_form.setVisible(false);
        notifications_form.setVisible(false);
        reports_form.setVisible(false);
        profile_form.setVisible(false);
    }

    public void showName(){
        if(loggedUser != null) username_label.setText("Welcome, " + loggedUser.getName());
    }

    public void showOperators(){
        ObservableList<User> operators = FXCollections.observableArrayList(userService.getAllOperators());
        operators_table.setItems(operators);

        operators_col_id.setCellValueFactory(new PropertyValueFactory<>("userId"));
        operators_col_email.setCellValueFactory(new PropertyValueFactory<>("email"));
        operators_col_name.setCellValueFactory(new PropertyValueFactory<>("name"));
        operators_col_number.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
        operators_col_date.setCellValueFactory(new PropertyValueFactory<>("approvalDate"));
    }

    public void showBooks(){
        ObservableList<Book> books = FXCollections.observableArrayList(bookService.getAllBooks());
        books_table.setItems(books);
        books_table.setEditable(true);
//        books_table.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        isbn.setCellValueFactory(new PropertyValueFactory<>("isbn"));
        title.setCellValueFactory(new PropertyValueFactory<>("title"));
        author.setCellValueFactory(new PropertyValueFactory<>("author"));
        genre.setCellValueFactory(new PropertyValueFactory<>("genre"));
        year.setCellValueFactory(new PropertyValueFactory<>("year"));
        status.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getStatus().getStatus()));
        total_q.setCellValueFactory(new PropertyValueFactory<>("totalQuantity"));
        av_q.setCellValueFactory(new PropertyValueFactory<>("availableQuantity"));
        publisher.setCellValueFactory(new PropertyValueFactory<>("publisher"));

        total_q.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        total_q.setOnEditCommit(event -> {
            Book book = event.getRowValue();
            book.setTotalQuantity(event.getNewValue());
        });

        av_q.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        av_q.setOnEditCommit(event -> {
            Book book = event.getRowValue();
            book.setAvailableQuantity(event.getNewValue());
        });
    }

    public void updateBook() {
        for (Book book : books_table.getItems()) {
            bookService.updateBook(book);
        }
        alert.successMessage("Book updated", "Congrats!");
    }

    public void showReadersAndBorBooks(){
        ObservableList<User> readers = FXCollections.observableArrayList(userService.getAllUsers());
        System.out.println(readers.size());
        readers_table.setItems(readers);

        readers_table.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null)
                updateList(newValue.getUserId());
        });

        readers_id.setCellValueFactory(new PropertyValueFactory<>("userId"));
        readers_email.setCellValueFactory(new PropertyValueFactory<>("email"));
        readers_name.setCellValueFactory(new PropertyValueFactory<>("name"));
        readers_number.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
        readers_date.setCellValueFactory(new PropertyValueFactory<>("approvalDate"));
        readers_borBooksNum.setCellValueFactory(cellData -> {
            User reader = cellData.getValue();
            int borBooks = borrowingService.getAllBorrowingsByUser(reader.getUserId()).size();
            return new SimpleIntegerProperty(borBooks).asObject();
        });

        borBooks_id.setCellValueFactory(new PropertyValueFactory<>("borId"));
        borBooks_title.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getBook().getTitle()));
        borBooks_author.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getBook().getAuthor()));
        borBooks_dateOfB.setCellValueFactory(new PropertyValueFactory<>("borrowingDate"));
        borBooks_expDateOfRet.setCellValueFactory(new PropertyValueFactory<>("expectedReturnDate"));
        borBooks_actualDate.setCellValueFactory(new PropertyValueFactory<>("actualReturnDate"));
        borBooks_borStatus.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getStatus().getStatus()));
    }

    public void updateList(int userId){
        borrowings.clear();
        borrowings.setAll(borrowingService.getAllBorrowingsByUser(userId));
        borBooks_table.setItems(borrowings);
    }

    public void returnBook(ActionEvent event){
        if(event.getSource() == return_btn) {
            if (borBooks_table.getSelectionModel().getSelectedItem() != null) {
                Borrowing borrowing = borBooks_table.getSelectionModel().getSelectedItem();
                if(borrowing.getStatus().getBorStatId() != 2){
                    borrowing.setDamaged(alert.confirmationMessageYesNo("Is the book damaged?"));

                    String result = borrowingService.returnBook(borrowing);
                    if(result.contains("Book returned successfully"))
                        alert.successMessage(result,"Congrats");
                    else alert.emptyAlertMessage(result);
                }
                else
                    alert.emptyAlertMessage("The book is already returned");
            }
            else
                alert.emptyAlertMessage("Please select a book to return");
        }
    }

    public void archiveBook(ActionEvent event) {
        if(event.getSource() == archiveBook_btn){
            if(books_table.getSelectionModel().getSelectedItems() != null) {
                    Book book = books_table.getSelectionModel().getSelectedItem();
                    checkProcess(book);
            }
            else alert.emptyAlertMessage("Please select a book to archive");
        }
    }

    public void checkProcess(Book book){
        String s = bookService.archiveBook(book);
        System.out.println(s);
        if(!s.isEmpty()){
            if(s.equals("The book must be 40+ years old to be archived")){
                if(alert.confirmationMessage("The book must be 40+ years old to be archived. " +
                        "Do you want to archive this book?")){
                    bookService.archiveYoungBook(book);
                    alert.successMessage("The book is archived", "Success");
                }
            }
            else alert.emptyAlertMessage(s);
        }
        else alert.successMessage("The book is archived", "Success");
    }

    public void deleteBook(ActionEvent event) {
        if(event.getSource() == delete_btn){
            if(books_table.getSelectionModel().getSelectedItem() != null){
                bookService.deleteBook(books_table.getSelectionModel().getSelectedItem());
            }
            else alert.emptyAlertMessage("Please select a book to delete");
        }
    }

    public void issueBook(ActionEvent event) throws IOException {
        if(event.getSource() == issueBook_btn){
            if(books_table.getSelectionModel().getSelectedItem() != null){
                Book book = books_table.getSelectionModel().getSelectedItem();
                String s;
                int bookStatus = book.getStatus().getId();
                Pair<Integer,Integer> info = openIssueBookDialog();
                if(info.getKey() != 0){
                    if(info.getValue() == bookStatus || (bookStatus == 2 && info.getKey() == 1)){
                        s = borrowingService.issueBook(book.getId(), info.getKey());
                        if(s.contains("Book issued to "))
                            alert.successMessage(s, "Success");
                        else
                            alert.emptyAlertMessage(s);
                    }
                    else
                        alert.emptyAlertMessage("The book is archived and cannot be taken out");
                }
                else
                    alert.emptyAlertMessage("No value entered or dialog was cancelled");
            }
            else alert.emptyAlertMessage("Please select a book to issue");
        }
    }

    public Pair<Integer,Integer> openIssueBookDialog() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/issueBook.fxml"));
        Parent root = loader.load();
        IssueBookController issueBookController = loader.getController();
        Stage dialogStage = new Stage();
        dialogStage.setScene(new Scene(root));
        dialogStage.showAndWait();

        return issueBookController.getInfoToIssue();
    }

    public void addBook() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/addBook.fxml"));
        Parent root = loader.load();
        Stage dialogStage = new Stage();
        dialogStage.setScene(new Scene(root));
        dialogStage.showAndWait();
    }

    public void filterBookTable(){

    }

    public void addOperator() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/addReader.fxml"));
        Parent root = loader.load();
        Stage dialogStage = new Stage();
        dialogStage.setScene(new Scene(root));
        dialogStage.showAndWait();
    }
    public void updateOperator(){
        userService.updateUser(operators_table.getSelectionModel().getSelectedItem());
        //make table editable
    }
    public void deleteOperator(){
        userService.deleteUser(operators_table.getSelectionModel().getSelectedItem());
    }

    public void addReaderManually() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/addReader.fxml"));
        Parent root = loader.load();
        Stage dialogStage = new Stage();
        dialogStage.setScene(new Scene(root));
        dialogStage.showAndWait();
    }
    public void updateReader(){
        userService.updateUser(readers_table.getSelectionModel().getSelectedItem());
        //make table editable
    }
    public void deleteReader(){
        userService.deleteUser(readers_table.getSelectionModel().getSelectedItem());
    }

    public void makeReports(ActionEvent event) {
        //if press b1 - forms created report
        //b2 - books report
        //b3 - users report
        //b4 - user rating
    }

}

