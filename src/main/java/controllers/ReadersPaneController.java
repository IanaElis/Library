package controllers;

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
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.converter.IntegerStringConverter;
import javafx.util.converter.LocalDateStringConverter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import services.BorrowingService;
import services.UserService;

import java.io.IOException;
import java.time.LocalDate;

public class ReadersPaneController {

    @FXML
    private AnchorPane readers_form;
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
    private TableColumn<User, Void> button;

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

    private UserService userService;
    private BorrowingService borrowingService;

    ObservableList<Borrowing> borrowings;
    ObservableList<User> readers;
    AlertMessage alert = new AlertMessage();
    private static final Logger logger = LogManager.getLogger(ReadersPaneController.class);
    private User loggedUser;

    public void setLoggedUser(User user) {
        this.loggedUser = user;
    }

    public void setParam(UserService us, BorrowingService brs){
        userService = us;
        borrowingService = brs;
    }

    public void initialize() {
        readers_table.setEditable(true);
        readers_id.setCellValueFactory(new PropertyValueFactory<>("userId"));
        readers_email.setCellValueFactory(new PropertyValueFactory<>("email"));
        readers_name.setCellValueFactory(new PropertyValueFactory<>("name"));
        readers_number.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
        readers_date.setCellValueFactory(new PropertyValueFactory<>("approvalDate"));

        readers_name.setCellFactory(TextFieldTableCell.forTableColumn());
        readers_number.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        readers_date.setCellFactory(TextFieldTableCell.forTableColumn(new LocalDateStringConverter()));

        readers_name.setOnEditCommit(event -> event.getRowValue().setName(event.getNewValue()));
        readers_number.setOnEditCommit(event -> event.getRowValue().setPhoneNumber(event.getNewValue()));
        readers_date.setOnEditCommit(event -> event.getRowValue().setApprovalDate(event.getNewValue()));


        button.setCellFactory(param -> new TableCell<>() {
            private final Button editButton = new Button("Update");
            {
                editButton.setOnAction(event -> {
                    User user = getTableView().getItems().get(getIndex());
                    updateReader(user);
                    logger.info("User {} updated the details of reader {}",
                            loggedUser.getEmail(), user.getEmail());
                });
            }
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(editButton);
                }
            }
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

        readers = FXCollections.observableArrayList();
        readers_table.setItems(readers);

        borrowings = FXCollections.observableArrayList();
        borBooks_table.setItems(borrowings);
    }

    public void showReadersAndBorBooks(){
        readers.setAll(userService.getAllReaders());

        readers_borBooksNum.setCellValueFactory(cellData -> {
            User reader = cellData.getValue();
            int borBooks = borrowingService.getAllBorrowingsByUser(reader.getUserId()).size();
            return new SimpleIntegerProperty(borBooks).asObject();
        });

        readers_table.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null)
                updateList(newValue.getUserId());
        });
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
                    if(result.contains("Book returned successfully")){
                        alert.successMessage(result,"Congrats");
                        logger.info("Book (ISBN: {}) was returned by reader {}. Performed by user {}",
                                borrowing.getBook().getIsbn(), borrowing.getUser().getEmail(), loggedUser.getEmail());
                        updateList(borrowing.getUser().getUserId());
                    }

                    else alert.emptyAlertMessage(result);
                }
                else
                    alert.emptyAlertMessage("The book is already returned");
            }
            else
                alert.emptyAlertMessage("Please select a book to return");
        }
    }

    public void addReaderManually(){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/addReader.fxml"));
            Parent root = loader.load();

            AddUserController controller = loader.getController();
            controller.setParam(userService);
            controller.setParentPane(readers_form);
            controller.setLoggedUser(loggedUser);

            Stage dialogStage = new Stage();
            dialogStage.setScene(new Scene(root));
            dialogStage.showAndWait();
        }catch(IOException e){
            logger.error("Failed to load addReader.fxml", e);
            alert.emptyAlertMessage("Failed to load the Add Reader form");
        }
    }

    public void updateReader(User user){
        userService.updateUser(user);
        alert.successMessage("Information updated", "Congrats!");
        logger.info("User {} update the details of reader {}", loggedUser.getEmail(), user.getEmail());

    }

    public void deleteReader(){
        if(readers_table.getSelectionModel().getSelectedItem() != null) {
            User user = readers_table.getSelectionModel().getSelectedItem();
            userService.deleteUser(user);
            readers.remove(user);
            logger.warn("User {} deleted user {}", loggedUser.getEmail(), user.getEmail());
            readers_table.refresh();
        }
        else alert.emptyAlertMessage("Please select a reader to delete");
    }

}
