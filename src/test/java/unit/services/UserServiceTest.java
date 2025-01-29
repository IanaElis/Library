package unit.services;

import org.junit.Before;
import org.junit.Test;

import dao.*;
import entity.Notification;
import entity.RegisterForm;
import entity.RegisterStatus;
import entity.Role;
import entity.User;
import javafx.util.Pair;
import org.mockito.*;
import services.NotificationService;
import services.UserService;
import util.PasswordUtil;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class UserServiceTest {
    @Mock
    private UserDAO mockUserDAO;
    @Mock
    private RegisterFormDAO mockRegisterFormDAO;
    @Mock
    private NotificationService mockNotificationService;
    @Mock
    private RoleDAO mockRoleDAO;
    @Mock
    private RegisterStatusDAO mockRegisterStatusDAO;
    private UserService userService;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        userService = new UserService(mockUserDAO, mockRegisterFormDAO,
                mockNotificationService, mockRoleDAO, mockRegisterStatusDAO);
    }

    @Test
    public void authenticateUser_validCredentials() {
        User mockUser = new User();
        String storedPassword = "salt:hashedPassword";
        mockUser.setUserId(1);
        mockUser.setEmail("test@example.com");
        mockUser.setPassword(storedPassword);

        when(mockUserDAO.getUserByEmail("test@example.com")).thenReturn(mockUser);
        try (MockedStatic<PasswordUtil> mockedStatic = mockStatic(PasswordUtil.class)) {
            mockedStatic.when(() -> PasswordUtil.verifyPassword(storedPassword, "plaintextPassword"))
                    .thenReturn(true);


            Pair<User, String> result = userService.authenticateUser("test@example.com", "plaintextPassword");

            assertNotNull(result.getKey());
            assertEquals("test@example.com", result.getKey().getEmail());
            assertNull(result.getValue());
        }
    }

    @Test
    public void authenticateUser_invalidPassword() {
        User mockUser = new User();
        String storedPassword = "salt:hashedPassword";
        mockUser.setUserId(1);
        mockUser.setEmail("test@example.com");
        mockUser.setPassword(storedPassword);

        when(mockUserDAO.getUserByEmail("test@example.com")).thenReturn(mockUser);
        try (MockedStatic<PasswordUtil> mockedStatic = mockStatic(PasswordUtil.class)) {
            mockedStatic.when(() -> PasswordUtil.verifyPassword(storedPassword, "plaintextPassword"))
                    .thenReturn(false);

            Pair<User, String> result = userService.authenticateUser("test@example.com", "wrongPassword");

            assertNull(result.getKey());
            assertEquals("Incorrect password.", result.getValue());
        }
    }

    @Test
    public void registerUser_existingUser() {
        RegisterForm mockForm = new RegisterForm();
        mockForm.setEmail("test@example.com");

        when(mockUserDAO.getUserByEmail("test@example.com")).thenReturn(new User());

        boolean result = userService.registerUser(mockForm);

        assertFalse(result);
    }

    @Test
    public void registerUser_newUser() {
        RegisterForm mockForm = new RegisterForm();
        mockForm.setEmail("test@example.com");

        when(mockUserDAO.getUserByEmail("test@example.com")).thenReturn(null);
        when(mockRegisterFormDAO.saveOrUpdate(mockForm)).thenReturn(true);
        Notification mockNotification = new Notification();
        when(mockNotificationService.getNotificationById(1)).thenReturn(mockNotification);

        boolean result = userService.registerUser(mockForm);

        assertTrue(result);
        verify(mockNotificationService).notifyAdminsOperators(mockNotification, "test@example.com");
    }

    @Test
    public void addUser_newUser() {
        User mockUser = new User();
        mockUser.setEmail("newuser@example.com");

        when(mockUserDAO.getUserByEmail("newuser@example.com")).thenReturn(null);

        boolean result = userService.addUser(mockUser);

        assertTrue(result);
        verify(mockUserDAO).saveOrUpdate(mockUser);
    }

    @Test
    public void addUser_existingUser() {
        User mockUser = new User();
        mockUser.setEmail("existinguser@example.com");

        when(mockUserDAO.getUserByEmail("existinguser@example.com")).thenReturn(new User());

        boolean result = userService.addUser(mockUser);

        assertFalse(result);
        verify(mockUserDAO, never()).saveOrUpdate(mockUser);
    }

    @Test
    public void approveReader_validForm() {
        RegisterForm mockForm = new RegisterForm();
        mockForm.setEmail("reader@example.com");
        RegisterStatus mockStatus = new RegisterStatus();
        mockStatus.setRegStatId(1);
        mockForm.setStatus(mockStatus);

        when(mockRegisterFormDAO.getRegisterFormsByEmail("reader@example.com")).thenReturn(mockForm);
        when(mockRegisterStatusDAO.getRegisterStatusById(2)).thenReturn(new RegisterStatus());
        when(mockRoleDAO.getRoleById(1)).thenReturn(new Role());

        boolean result = userService.approveReader(mockForm);

        assertTrue(result);
        verify(mockRegisterFormDAO).saveOrUpdate(mockForm);
        verify(mockUserDAO).saveOrUpdate(any(User.class));
    }

    @Test
    public void denyReader_validForm() {
        RegisterForm mockForm = new RegisterForm();
        mockForm.setEmail("reader@example.com");

        when(mockUserDAO.getUserByEmail("reader@example.com")).thenReturn(null);
        when(mockRegisterStatusDAO.getRegisterStatusById(3)).thenReturn(new RegisterStatus());

        boolean result = userService.denyReader(mockForm);

        assertTrue(result);
        verify(mockRegisterFormDAO).saveOrUpdate(mockForm);
    }


    @Test
    public void deleteUser_existingUser() {
        User mockUser = new User();
        mockUser.setEmail("deleteuser@example.com");

        when(mockUserDAO.getUserByEmail("deleteuser@example.com")).thenReturn(mockUser);

        userService.deleteUser(mockUser);

        verify(mockUserDAO).delete(mockUser);
    }

    @Test
    public void deleteUser_nonExistingUser() {
        User mockUser = new User();
        Role mockRole = new Role();
        mockRole.setName("reader");
        mockUser.setEmail("nonexistent@example.com");
        mockUser.setRole(mockRole);

        when(mockUserDAO.getUserByEmail("nonexistent@example.com")).thenReturn(null);

        userService.deleteUser(mockUser);

        verify(mockUserDAO, never()).delete(mockUser);
    }

}