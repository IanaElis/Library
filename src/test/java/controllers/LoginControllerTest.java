package controllers;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Pair;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testfx.framework.junit.ApplicationTest;
import services.UserService;

import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;
import static org.testfx.api.FxAssert.verifyThat;
import static org.testfx.matcher.control.TextInputControlMatchers.hasText;
import static org.testfx.util.WaitForAsyncUtils.waitForFxEvents;

public class LoginControllerTest extends ApplicationTest {
    private AlertMessage mockAlertMessage;
    @Mock
    private UserService userService;

    @Override
    public void start(Stage stage) throws Exception {
        MockitoAnnotations.openMocks(this);

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/login.fxml"));
        Parent root = loader.load();

        LoginController loginController = loader.getController();
        mockAlertMessage = spy(loginController.alert);
        loginController.alert = mockAlertMessage;

        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @Test
    public void testLoginSuccess() {
        clickOn("#login_email").write("admin@gmail.com");
        clickOn("#login_password").write("111111");

        clickOn("#login_loginBtn");

        waitForFxEvents();

        verify(mockAlertMessage).successMessage("Login successful", "Information");
    }

    @Test
    public void testLoginFailure_pendingApproval() {
        String email = "oscar@gmail.com";
        String password = "123456";

        when(userService.authenticateUser(email, password)).thenReturn(new Pair<>(null, "User not found"));
        when(userService.findRegForm(email, password)).thenReturn(1);

        clickOn("#login_email").write(email);
        clickOn("#login_password").write(password);

        clickOn("#login_loginBtn");

        verify(mockAlertMessage).successMessage("Pending approval", "Information");
    }

    @Test
    public void testLoginFailure_registrationDenied() {
        String email = "ann@gmail.com";
        String password = "123456";

        when(userService.authenticateUser(email, password)).thenReturn(new Pair<>(null, "User not found"));
        when(userService.findRegForm(email, password)).thenReturn(3);

        clickOn("#login_email").write(email);
        clickOn("#login_password").write(password);

        clickOn("#login_loginBtn");

        verify(mockAlertMessage).emptyAlertMessage("Registration denied");
    }

    @Test
    public void testLoginFailure_wrongPassword() {
        String email = "op@mail.bg";
        String password = "wrongpassword";

        when(userService.authenticateUser(email, password)).thenReturn(new Pair<>(null, "Incorrect password."));

        clickOn("#login_email").write(email);
        clickOn("#login_password").write(password);
        clickOn("#login_loginBtn");

        verify(mockAlertMessage).emptyAlertMessage("Incorrect password.");
    }

    @Test
    public void testLoginFailure_userNotExist() {
        String email = "wrong_email";
        String password = "wrongpassword";

        when(userService.authenticateUser(email, password)).thenReturn(new Pair<>(null, "User does not exist."));

        clickOn("#login_email").write(email);
        clickOn("#login_password").write(password);
        clickOn("#login_loginBtn");

        verify(mockAlertMessage).emptyAlertMessage("User does not exist.");
    }

    @Test
    public void testLoginEmptyFields() {
        clickOn("#login_email").write("");
        clickOn("#login_password").write("");


        clickOn("#login_loginBtn");

        verify(mockAlertMessage).emptyAlertMessage("Please fill blank fields");
    }

    @Test
    public void testShowPassword() {
        clickOn("#login_password").write("password123");

        clickOn("#login_checkBx");

        verifyThat("#login_showPass", hasText("password123"));
    }

    @Test
    public void testUserRegistration() {
        clickOn("#login_registerLink");
        clickOn("#register_email").write("test@example.com");
        clickOn("#register_password").write("password123");
        clickOn("#register_name").write("John Doe");
        clickOn("#register_phone_number").write("1234567890");
        clickOn("#register_regBtn");

        verify(mockAlertMessage).
                successMessage("Registration successful! Pending approval","Congratulations!");
    }
}
