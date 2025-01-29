package services;

import dao.*;
import entity.Notification;
import entity.RegisterForm;
import entity.Role;
import entity.User;
import javafx.util.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import util.PasswordUtil;

import java.time.LocalDate;
import java.util.List;

public class UserService {

    private final UserDAO userDAO;
    private final RegisterFormDAO registerFormDAO;
    private final NotificationService notificationService;
    private final RoleDAO roleDAO;
    private final RegisterStatusDAO registerStatusDAO;
    private static final Logger logger = LogManager.getLogger(UserService.class);

    public UserService(UserDAO us, RegisterFormDAO regd, NotificationService ns,
                       RoleDAO rd, RegisterStatusDAO rsd) {
        userDAO = us;
        registerFormDAO = regd;
        notificationService = ns;
        roleDAO = rd;
        registerStatusDAO = rsd;
    }

    public Pair<User, String> authenticateUser(String email, String password) {
        User user = userDAO.getUserByEmail(email);

        if (user == null) {
            return new Pair<>(null, "User does not exist.");
        }

        if (!PasswordUtil.verifyPassword(user.getPassword(), password)) {
            return new Pair<>(null, "Incorrect password.");
        }
        return new Pair<>(user, null);
    }

    public int findRegForm(String email, String password) {
        RegisterForm form = registerFormDAO.getRegisterFormsByEmail(email);
        if (form == null) {
            return 0;
        }
        boolean isPasswordCorrect = PasswordUtil.verifyPassword(form.getPassword(), password);
        if (!isPasswordCorrect) {
            return 0;
        }
        else return form.getStatus().getRegStatId();
    }

    public boolean registerUser(RegisterForm form) {
        if(form == null) throw new IllegalArgumentException("Form cannot be null");
        if (userDAO.getUserByEmail(form.getEmail()) != null) {
            return false;
        }
        boolean registerFormSaved = registerFormDAO.saveOrUpdate(form);

        if (registerFormSaved) {
            Notification ntfc = notificationService.getNotificationById(1);
            notificationService.notifyAdminsOperators(ntfc, form.getEmail());
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

    public boolean approveReader(RegisterForm form) {
        if(registerFormDAO.getRegisterFormsByEmail(form.getEmail()).getStatus().getRegStatId() == 1) {
            form.setStatus(registerStatusDAO.getRegisterStatusById(2));
            registerFormDAO.saveOrUpdate(form);

            User user = new User(form.getEmail(), form.getPassword(), form.getName(),
                    form.getPhoneNumber(), LocalDate.now(), form, roleDAO.getRoleById(1));
            userDAO.saveOrUpdate(user);
            logger.info("User {} created successfully", form.getEmail());
            return true;
        }
        else return false;
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
            logger.warn("The {} doesn't exist", role.toLowerCase());
        }
    }

    public void deleteUser(User user) {
        if(userDAO.getUserByEmail(user.getEmail()) != null) {
            userDAO.delete(user);
        }
        else{
            String role = (user.getRole() != null) ? user.getRole().getName() : "unknown";
            logger.warn("The {} doesn't exist", role.toLowerCase());
        }
    }

    public Role getRole(int id){
        return roleDAO.getRoleById(id);
    }

    public int countAllReaders(){
        return userDAO.countAllReaders();
    }

    public void saveOrUpdate(User user){
        userDAO.saveOrUpdate(user);
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

}
