package integrational;

import dao.*;
import entity.User;
import services.NotificationService;
import services.UserService;

import javafx.util.Pair;
import org.junit.Test;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;

import static org.junit.Assert.*;


public class UserAuthenticationIntegrationTest {
    private UserService userService;
    private UserDAO userDAO;

    @Before
    public void setup() {
        userDAO = new UserDAO();
        userService = new UserService(userDAO, new RegisterFormDAO(),
                new NotificationService(new UserNotificationDAO(), new NotificationDAO(), userDAO),
                new RoleDAO(), new RegisterStatusDAO());
    }

    @Test
    public void testAuthenticateUserWithCorrectCredentials() {
        User testUser = new User("test@example.com", "password123", "John Doe", 1234567890, LocalDate.now(), null, null);
        userDAO.saveOrUpdate(testUser);

        Pair<User, String> result = userService.authenticateUser("test@example.com", "password123");

        assertNotNull(result.getKey());
        assertNull(result.getValue());
    }

    @Test
    public void testAuthenticateUserWithIncorrectPassword() {
        User testUser = new User("test@example.com", "password123", "John Doe", 1234567890, LocalDate.now(), null, null);
        userDAO.saveOrUpdate(testUser);

        Pair<User, String> result = userService.authenticateUser("test@example.com", "wrongpassword");

        assertNull(result.getKey());
        assertEquals("Incorrect password.", result.getValue());
    }
}

