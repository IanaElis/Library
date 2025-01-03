package controllers;

import dao.UserDAO;
import entity.User;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import org.apache.logging.log4j.Logger;
import util.LoggerUtil;

public class ProfilePaneController {

    @FXML
    private TextField email;
    @FXML
    private TextField name;
    @FXML
    private TextField number;
    private AlertMessage alert = new AlertMessage();
    private UserDAO userDAO = new UserDAO();
    private static final Logger logger = LoggerUtil.getLogger();
    private User loggedUser;

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
                userDAO.saveOrUpdate(loggedUser);
                logger.info("User {} changed personal information", loggedUser.getEmail());
            }
            else alert.emptyAlertMessage("Please fill in missing information");
        } catch (Exception e) {
            alert.emptyAlertMessage("Error occurred");
        }
    }
}
