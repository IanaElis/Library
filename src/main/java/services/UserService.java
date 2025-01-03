package services;

import dao.*;
import entity.Notification;
import entity.RegisterForm;
import entity.RegisterStatus;
import entity.User;
import javafx.util.Pair;

import java.time.LocalDate;
import java.util.List;

public class UserService {

    private UserDAO userDAO;
    private RegisterFormDAO registerFormDAO;
    private NotificationDAO notificationDAO;
    private NotificationService notificationService;
    private RoleDAO roleDAO;
    private RegisterStatusDAO registerStatusDAO;

    public UserService() {
        userDAO = new UserDAO();
        registerFormDAO = new RegisterFormDAO();
        notificationDAO = new NotificationDAO();
        notificationService = new NotificationService();
        roleDAO = new RoleDAO();
        registerStatusDAO = new RegisterStatusDAO();
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
            Notification ntfc = new Notification("A new registration form has been submitted",
                    LocalDate.now(), false);
            ntfc.setAdditionalInfo("User email: " + form.getEmail());
            notificationDAO.saveOrUpdate(ntfc);
            notificationService.notifyAdminsOperators(ntfc);
        }
        return registerFormSaved;
    }

    public boolean addUser(User user){
        if(userDAO.getUserByEmail(user.getEmail()) == null) {
            userDAO.saveOrUpdate(user);
            return true;
        }
        else return false;
    }

    public RegisterForm getRegFormByEmail(String email) {
        return registerFormDAO.getRegisterFormsByEmail(email);
    }

    public void approveReader(RegisterForm form) {
        if(registerFormDAO.getRegisterFormsByEmail(form.getEmail()).getStatus().getRegStatId() != 2) {
            form.setStatus(registerStatusDAO.getRegisterStatusById(2));
            registerFormDAO.saveOrUpdate(form);
            if (userDAO.getUserByEmail(form.getEmail()) == null) {
                User user = new User(form.getEmail(), form.getPassword(), form.getName(),
                        form.getPhoneNumber(), LocalDate.now(), form, roleDAO.getRoleById(1));
                userDAO.saveOrUpdate(user);
            }
        }
    }

    public boolean denyReader(RegisterForm form) {
        if(userDAO.getUserByEmail(form.getEmail()) == null) {
            form.setStatus(registerStatusDAO.getRegisterStatusById(3));
            registerFormDAO.saveOrUpdate(form);
            return true;
        }
        else return false;
    }

    public void updateUser(User user) {
        if(userDAO.getUserById(user.getUserId()) != null) {
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
    public User getUserById(int id){
        return userDAO.getUserById(id);
    }

    public List<User> getAllAdmins(){
        return userDAO.getAllAdmins();
    }

    public List<RegisterForm> getAllRegisterForms() {
        return registerFormDAO.getAllRegisterForms();
    }

    public User getUserByEmail(String email){
        return userDAO.getUserByEmail(email);
    }

}
