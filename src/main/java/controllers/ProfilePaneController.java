package controllers;

import entity.User;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import services.UserService;

public class ProfilePaneController {

    @FXML
    private TextField email;
    @FXML
    private TextField name;
    @FXML
    private TextField number;
    private final AlertMessage alert = new AlertMessage();
    private UserService userService;
    private static final Logger logger = LogManager.getLogger(ProfilePaneController.class);
    private User loggedUser;

    public void setParam(UserService us){
        userService = us;
    }

    public void setLoggedUser(User user) {
        this.loggedUser = user;
    }

    public void saveChanges(){
        try {
            String email = this.email.getText();
            String name = this.name.getText();
            String number = this.number.getText();

            if(email != null && name != null && number!=null && !email.isBlank() && !name.isBlank()
            && !number.isBlank()) {
                int parsedNumber = 0;
                try {
                    parsedNumber = Integer.parseInt(number);
                } catch (NumberFormatException e) {
                    alert.emptyAlertMessage("Invalid phone number format");
                }
                loggedUser.setEmail(email);
                loggedUser.setName(name);
                loggedUser.setPhoneNumber(parsedNumber);
                userService.saveOrUpdate(loggedUser);
                logger.info("User {} changed personal information", loggedUser.getEmail());
            }
            else alert.emptyAlertMessage("Please fill in missing information");
        } catch (Exception e) {
            alert.emptyAlertMessage("Error occurred");
        }
    }
}
