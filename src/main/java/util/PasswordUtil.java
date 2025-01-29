package util;
import org.apache.commons.lang3.RandomStringUtils;
import java.security.SecureRandom;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.util.Base64;

public class PasswordUtil {
    public static String generatePassword(int length) {
        return RandomStringUtils.random(length, true, true);
    }

    private static final int ITERATIONS = 1000;
    private static final int KEY_LENGTH = 64;

    public static String hashPassword(String password) {
        try {
            byte[] salt = generateSalt();

            PBEKeySpec spec = new PBEKeySpec(password.toCharArray(), salt, ITERATIONS, KEY_LENGTH);
            SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
            byte[] hash = skf.generateSecret(spec).getEncoded();

            // Return salt + hash in a combined format
            String saltBase64 = Base64.getEncoder().encodeToString(salt);
            String hashBase64 = Base64.getEncoder().encodeToString(hash);

            return saltBase64 + ":" + hashBase64;

        } catch (Exception e) {
            throw new RuntimeException("Error while hashing password", e);
        }
    }

    public static boolean verifyPassword(String storedHash, String password) {
        try {
            String[] parts = storedHash.split(":");
            String saltBase64 = parts[0];
            String storedPasswordHash = parts[1];

            byte[] salt = Base64.getDecoder().decode(saltBase64);

            PBEKeySpec spec = new PBEKeySpec(password.toCharArray(), salt, ITERATIONS, KEY_LENGTH);
            SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
            byte[] hash = skf.generateSecret(spec).getEncoded();

            String hashBase64 = Base64.getEncoder().encodeToString(hash);
            return hashBase64.equals(storedPasswordHash);

        } catch (Exception e) {
            throw new RuntimeException("Error while verifying password", e);
        }
    }

    private static byte[] generateSalt() {
        SecureRandom sr = new SecureRandom();
        byte[] salt = new byte[16];
        sr.nextBytes(salt);
        return salt;
    }
}
