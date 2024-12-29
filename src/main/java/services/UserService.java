package services;

import dao.RegisterFormDAO;
import dao.RegisterStatusDAO;
import dao.UserDAO;
import entity.RegisterForm;
import entity.User;
import javafx.util.Pair;

import java.util.List;

public class UserService {

    private UserDAO userDAO;
    private RegisterFormDAO registerFormDAO;

    public UserService() {
        userDAO = new UserDAO();
        registerFormDAO = new RegisterFormDAO();
    }

    public Pair<User, String> authenticateUser(String email, String password) {
        if (email == null || password == null || email.isEmpty() || password.isEmpty()) {
            throw new IllegalArgumentException("Email or password cannot be null");
        }
        User user = userDAO.getUserByEmail(email);

        if (user == null) {
            return new Pair<>(null, "User does not exist.");
        }

        if (!user.getPassword().equals(password)) {
            return new Pair<>(null, "Incorrect password.");
        }
        return new Pair<>(user, null);
    }

    public int findRegForm(String email, String password) {
        if (email == null || password == null || email.isEmpty() || password.isEmpty()) {
            throw new IllegalArgumentException("Email or password cannot be null");
        }
        RegisterForm form = registerFormDAO.getRegisterFormsByEmailPass(email, password);
        if (form == null) {
            return 0;
        }
        return form.getStatus().getRegStatId();
    }

    public boolean registerUser(RegisterForm form) {
        if(form == null) throw new IllegalArgumentException("Form cannot be null");
        if (userDAO.getUserByEmail(form.getEmail()) != null) {
            return false;
        }
        boolean registerFormSaved = registerFormDAO.saveOrUpdate(form);

        if (registerFormSaved) {
            //NOTIFICATIONS
          /*  Notification notification = new Notification();
            notification.setSender("Administrator");
            notification.setTheme("New Registration Request");
            notification.setMessage("A new registration form has been submitted for " + form.getName());
            notificationService.notifyAdmins(notification);
           */
        }
        return registerFormSaved;
    }

    public void addUser(User user){
        if(userDAO.getUserByEmail(user.getEmail()) == null) {
            userDAO.saveOrUpdate(user);
        }
        else{
            System.out.println("The email is already in use");
        }
    }
    public void updateUser(User user) {
        if(userDAO.getUserByEmail(user.getEmail()) != null) {
            userDAO.saveOrUpdate(user);
        }
        else{
            String role = userDAO.getUserByEmail(user.getEmail()).getRole().getName();
            System.out.println("The " + role.toLowerCase() + " doesn't exist");
        }
    }
    public void deleteUser(User user) {
        if(userDAO.getUserByEmail(user.getEmail()) != null) {
            userDAO.delete(user);
        }
        else{
            String role = userDAO.getUserByEmail(user.getEmail()).getRole().getName();
                System.out.println("The " + role.toLowerCase() + " doesn't exist");
        }
    }

    public List<User> getAllOperators() {
        return userDAO.getAllOperators();
    }

    public List<User> getAllReaders(){
        return userDAO.getAllReaders();
    }

    public List<User> getAllUsers(){
        return userDAO.getAllUsers();
    }
}
