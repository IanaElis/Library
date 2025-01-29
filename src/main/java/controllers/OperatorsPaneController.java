package controllers;

import entity.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
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
import services.UserService;

import java.io.IOException;
import java.time.LocalDate;

public class OperatorsPaneController {

    @FXML
    private AnchorPane operators_form;
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

    private UserService userService;
    private final AlertMessage alert = new AlertMessage();
    private ObservableList<User> operators;
    private static final Logger logger = LogManager.getLogger(OperatorsPaneController.class);
    private User loggedUser;

    public void setParam(UserService us){
        userService = us;
    }

    public void setLoggedUser(User user) {
        this.loggedUser = user;
    }

    public void initialize(){
        operators_table.setEditable(true);

        operators_col_id.setCellValueFactory(new PropertyValueFactory<>("userId"));
        operators_col_email.setCellValueFactory(new PropertyValueFactory<>("email"));
        operators_col_name.setCellValueFactory(new PropertyValueFactory<>("name"));
        operators_col_number.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
        operators_col_date.setCellValueFactory(new PropertyValueFactory<>("approvalDate"));

        operators_col_name.setCellFactory(TextFieldTableCell.forTableColumn());
        operators_col_number.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        operators_col_date.setCellFactory(TextFieldTableCell.forTableColumn(new LocalDateStringConverter()));

        operators_col_name.setOnEditCommit(event -> event.getRowValue().setName(event.getNewValue()));
        operators_col_number.setOnEditCommit(event -> event.getRowValue().setPhoneNumber(event.getNewValue()));
        operators_col_date.setOnEditCommit(event -> event.getRowValue().setApprovalDate(event.getNewValue()));

        operators = FXCollections.observableArrayList();
        operators_table.setItems(operators);
    }

    public void showOperators(){
        operators.setAll(userService.getAllOperators());
    }

    public void addOperator() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/addOperator.fxml"));
        Parent root = loader.load();

        AddUserController controller = loader.getController();
        controller.setParam(userService);
        controller.setParentPane(operators_form);
        controller.setLoggedUser(loggedUser);

        Stage dialogStage = new Stage();
        dialogStage.setScene(new Scene(root));
        dialogStage.showAndWait();
    }

    public void updateOperator(){
        for(User u: operators_table.getItems()){
            userService.updateUser(u);
        }
        alert.successMessage("Table updated", "Congrats!");
        logger.info("User {} updated the operators' table", loggedUser.getEmail());
    }

    public void deleteOperator(){
        if(operators_table.getSelectionModel().getSelectedItem() != null) {
            User user = operators_table.getSelectionModel().getSelectedItem();
            userService.deleteUser(user);
            logger.warn("User {} deleted user {}", loggedUser.getEmail(), user.getEmail());
            operators_table.refresh();
        }
        else alert.emptyAlertMessage("Please select an operator to delete");
    }
}
