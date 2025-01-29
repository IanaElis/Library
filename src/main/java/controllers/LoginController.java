package controllers;

import dao.RegisterStatusDAO;
import entity.RegisterForm;
import entity.User;
import javafx.event.ActionEvent;

import java.io.IOException;
import java.time.LocalDate;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import services.*;
import util.PasswordUtil;

public class LoginController  {
    @FXML
    private CheckBox login_checkBx;
    @FXML
    private TextField login_email;
    @FXML
    private AnchorPane login_form;
    @FXML
    private PasswordField login_password;
    @FXML
    private Hyperlink login_registerLink;
    @FXML
    private TextField login_showPass;

    @FXML
    private CheckBox register_checkBx;
    @FXML
    private TextField register_email;
    @FXML
    private AnchorPane register_form;
    @FXML
    private PasswordField register_password;
    @FXML
    private Hyperlink register_loginLink;
    @FXML
    private TextField register_name;
    @FXML
    private TextField register_phone_number;
    @FXML
    private TextField register_showPass;

    AlertMessage alert = new AlertMessage();
    private final UserService userService = ServicesDAOs.getUserService();
    private final BookService bookService = ServicesDAOs.getBookService();
    private final NotificationService notificationService = ServicesDAOs.getNotificationService();
    private static final Logger logger = LogManager.getLogger(LoginController.class);

    public void switchForm(ActionEvent event) {
        if(event.getSource() == login_registerLink) {
            login_form.setVisible(false);
            register_form.setVisible(true);
        }else if(event.getSource() == register_loginLink) {
            register_form.setVisible(false);
            login_form.setVisible(true);
        }
    }

    public void showPassword() {
        togglePasswordField(register_checkBx, register_password, register_showPass);
        togglePasswordField(login_checkBx, login_password, login_showPass);
    }

    private void togglePasswordField(CheckBox checkBox, PasswordField password, TextField showPass) {
        if (checkBox.isSelected()) {
            showPass.setText(password.getText());
            showPass.setVisible(true);
            password.setVisible(false);
        } else {
            password.setText(showPass.getText());
            password.setVisible(true);
            showPass.setVisible(false);
        }
    }

    public void matchPasswords(){
        if(!register_showPass.isVisible()) {
            if(!register_showPass.getText().equals(register_password.getText())) {
                register_showPass.setText(register_password.getText());
            }
        }
        else{
            if(!register_showPass.getText().equals(register_password.getText())) {
                register_password.setText(register_showPass.getText());
            }
        }
    }

    public void handleLogin(ActionEvent event) throws IOException {
        String email = login_email.getText();
        String password = login_password.getText();

        if(email.isEmpty() || password.isEmpty()) {
            alert.emptyAlertMessage("Please fill blank fields");
        }
        else {
            Pair<User,String> result = userService.authenticateUser(email, password);
            User user = result.getKey();
            if (user == null) {
                int regStatId = userService.findRegForm(email, password);
                if(regStatId == 1) {
                    alert.successMessage("Pending approval", "Information");
                    return;
                }
                else if(regStatId == 3) {
                    alert.emptyAlertMessage("Registration denied");
                    return;
                }
                else {
                    alert.emptyAlertMessage(result.getValue());
                    logger.warn("Failed login attempt with message {}", result.getValue());
                    return;
                }
            }

            navigateToDashboard(event, user);
        }
    }

    public void navigateToDashboard(ActionEvent event, User user) throws IOException {
        String fxmlFile = null;
        if (user.getRole().getRoleID() == 1) {
            //reader
            //approved, reader dashboard
            fxmlFile = "/fxml/readerAccount.fxml";
        }
        if (user.getRole().getRoleID() == 2) {
            //operator
            fxmlFile = "/fxml/operatorAccount.fxml";
        }
        if (user.getRole().getRoleID() == 3) {
            //admin
            fxmlFile = "/fxml/AdminAccount.fxml";
        }

        if(fxmlFile == null){
            alert.emptyAlertMessage("Unexpected role id");
            logger.error("The role id value not found in the db. Not able to load dashboard");
            return;
        }

        alert.successMessage("Login successful", "Information");

        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
        Parent root = loader.load();

        if(user.getRole().getRoleID() == 1){
            ReaderController reader = loader.getController();
            reader.getDefaultPane();
            reader.setLoggedUser(user);
        }
        if(user.getRole().getRoleID() == 2){
            OperatorController operator = loader.getController();
            operator.setLoggedUser(user);
            operator.getDefaultPane();
        }
        if(user.getRole().getRoleID() == 3) {
            AdminController admin = loader.getController();
            admin.setLoggedUser(user);
            admin.getDefaultPane();
        }
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
        logger.info("User {} logged in successfully", user.getEmail());
        NotificationsPaneController controller = new NotificationsPaneController();
        controller.setParam(userService, bookService, notificationService);
        controller.showUnreadNotificationsAlert(user.getUserId());
    }

    public void handleRegister() {
        String email = register_email.getText();
        String password = register_password.getText();
        String name = register_name.getText();
        int phoneNumber;
        try {
            phoneNumber = Integer.parseInt(register_phone_number.getText());
        } catch (NumberFormatException e) {
            alert.emptyAlertMessage("Invalid phone number format");
            return;
        }

        if (email.isEmpty() || password.isEmpty() || name.isEmpty()) {
            alert.emptyAlertMessage("Please fill blank fields");
        } else if (password.length() < 6) {
            alert.emptyAlertMessage("Password should be at least 6 characters");
        } else {
            matchPasswords();

            String hashedPass = PasswordUtil.hashPassword(password);

            RegisterForm form = new RegisterForm(email, hashedPass, name, phoneNumber,
                    new RegisterStatusDAO().getRegisterStatusById(1), LocalDate.now());
            if (userService.registerUser(form)) {
                alert.successMessage("Registration successful! Pending" +
                        " approval", "Congratulations!");
                logger.info("Registration form with ID {} created successfully", form.getRegFormId());
                logger.info("Notification for new registration form submitted sent.");
                switchForm(new ActionEvent(register_loginLink, null));
            }
            else{
                alert.emptyAlertMessage("Registration failed. " +
                        "Email might already be registered");
                logger.warn("Registration denied due to email already existing");
            }
        }
    }
}
