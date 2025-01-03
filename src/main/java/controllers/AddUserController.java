package controllers;

import dao.RoleDAO;
import entity.User;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import org.apache.logging.log4j.Logger;
import services.UserService;
import util.LoggerUtil;
import util.PasswordUtil;

import java.time.LocalDate;

public class AddUserController {
    @FXML
    private TextField email;
    @FXML
    private TextField name;
    @FXML
    private TextField number;
    @FXML
    private TextField password;

    private AnchorPane rootPane;
    private AlertMessage alert = new AlertMessage();
    private RoleDAO roleDAO = new RoleDAO();
    private UserService userService = new UserService();
    private static final Logger logger = LoggerUtil.getLogger();
    private User loggedUser;

    public void setLoggedUser(User user) {
        this.loggedUser = user;
    }

    public void setParentPane(AnchorPane pane) {
        this.rootPane = pane;
    }

    public void addUser() {
        if(rootPane!=null) {
            String email = this.email.getText();
            String name = this.name.getText();
            String number = this.number.getText();
            String password = this.password.getText();

            if(email == null || email.isBlank() || name == null || name.isBlank() ||
                    number == null || number.isBlank() || password == null || password.isBlank()) {
                alert.emptyAlertMessage("Please fill in all the fields");
            }
            else {
                int parsedNumber = 0;
                try {
                    parsedNumber = Integer.parseInt(number);
                } catch (NumberFormatException e) {
                    alert.emptyAlertMessage("Invalid phone number format");
                }

                User newUser = new User(email, password, name, parsedNumber,
                        LocalDate.now(),null,null);
                String role = "";
                if (rootPane.getId() != null && rootPane.getId().contains("readers")) {
                    newUser.setRole(roleDAO.getRoleById(1));
                    role = "reader";
                } else if (rootPane.getId() != null && rootPane.getId().contains("operators")) {
                    newUser.setRole(roleDAO.getRoleById(2));
                    role = "operator";
                }

                try {
                    if (userService.addUser(newUser)) {
                        alert.successMessage("The " + role + " is created", "Success");
                        logger.info("User {} added new {} {}",
                                loggedUser.getEmail(), role, newUser.getEmail());
                    }
                    else alert.emptyAlertMessage("The email is already in use");
                }
                catch (Exception e) {
                    alert.emptyAlertMessage("An error occurred while creating the user: " + e.getMessage());
                    logger.error("An error {} occurred while creating new user {}", e, newUser.getEmail());
                }
            }
        }
    }

    public void generatePass() {
        String pass = PasswordUtil.generatePassword(6);
        this.password.setText(pass);
    }
}
