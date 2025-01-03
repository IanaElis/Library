package util;
import org.apache.commons.lang3.RandomStringUtils;

public class PasswordUtil {
    public static String generatePassword(int length) {
        return RandomStringUtils.random(length, true, true);
    }
}
