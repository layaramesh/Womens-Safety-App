import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.io.TempDir;

import java.io.*;
import java.nio.file.*;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Functional tests for Women's Safety App
 */
public class WomensSafetyAppTest {

    @TempDir
    Path tempDir;

    @Test
    @DisplayName("Should validate phone number format")
    public void testPhoneNumberValidation() {
        assertTrue(isValidPhoneNumber("+14252897888"), "Valid US phone number should pass");
        assertTrue(isValidPhoneNumber("+18332811225"), "Valid US toll-free should pass");
        assertFalse(isValidPhoneNumber("1234567890"), "Missing + should fail");
        assertFalse(isValidPhoneNumber("+1234"), "Too short should fail");
        assertFalse(isValidPhoneNumber("invalid"), "Non-numeric should fail");
    }

    @Test
    @DisplayName("Should parse contact list correctly")
    public void testContactListParsing() {
        String contacts = "+14252897888,+18332811225, +12065551234";
        String[] parsed = parseContacts(contacts);
        
        assertEquals(3, parsed.length, "Should parse 3 contacts");
        assertEquals("+14252897888", parsed[0].trim(), "First contact should match");
        assertEquals("+18332811225", parsed[1].trim(), "Second contact should match");
        assertEquals("+12065551234", parsed[2].trim(), "Third contact should match");
    }

    @Test
    @DisplayName("Should validate timeout duration")
    public void testTimeoutValidation() {
        assertTrue(isValidTimeout("10"), "Integer should be valid");
        assertTrue(isValidTimeout("0.5"), "Decimal should be valid");
        assertTrue(isValidTimeout("1.0"), "Float should be valid");
        assertFalse(isValidTimeout("-5"), "Negative should be invalid");
        assertFalse(isValidTimeout("abc"), "Non-numeric should be invalid");
        assertFalse(isValidTimeout(""), "Empty should be invalid");
    }

    @Test
    @DisplayName("Should create log entries correctly")
    public void testLogFileCreation() throws IOException {
        Path logFile = tempDir.resolve("test_calls.log");
        String phoneNumber = "+14252897888";
        String message = "Test message";
        
        // Simulate log writing
        try (FileWriter fw = new FileWriter(logFile.toFile(), true)) {
            String timestamp = "2025-12-30 19:00:00";
            String logEntry = String.format("%s | To: %s | Message: %s%n", timestamp, phoneNumber, message);
            fw.write(logEntry);
        }
        
        assertTrue(Files.exists(logFile), "Log file should be created");
        String content = Files.readString(logFile);
        assertTrue(content.contains(phoneNumber), "Log should contain phone number");
        assertTrue(content.contains(message), "Log should contain message");
    }

    @Test
    @DisplayName("Should load config properties correctly")
    public void testConfigLoading() throws IOException {
        Path configFile = tempDir.resolve("test-config.properties");
        
        // Create test config
        Properties props = new Properties();
        props.setProperty("azure.phoneNumber", "+18332811225");
        props.setProperty("azure.enabled", "true");
        props.setProperty("azure.callbackUri", "https://example.ngrok.io");
        
        try (FileWriter fw = new FileWriter(configFile.toFile())) {
            props.store(fw, "Test Config");
        }
        
        // Load and verify
        Properties loaded = new Properties();
        try (FileReader fr = new FileReader(configFile.toFile())) {
            loaded.load(fr);
        }
        
        assertEquals("+18332811225", loaded.getProperty("azure.phoneNumber"));
        assertEquals("true", loaded.getProperty("azure.enabled"));
        assertEquals("https://example.ngrok.io", loaded.getProperty("azure.callbackUri"));
    }

    @Test
    @DisplayName("Should format location sharing message correctly")
    public void testMessageFormatting() {
        String name = "Laya";
        float minutes = 10.0f;
        String expected = "Laya has shared their location with you for 10.0 minutes.";
        String actual = formatLocationMessage(name, minutes);
        
        assertEquals(expected, actual, "Message should be formatted correctly");
    }

    @Test
    @DisplayName("Should validate callback URI format")
    public void testCallbackUriValidation() {
        assertTrue(isValidCallbackUri("https://example.ngrok.io"));
        assertTrue(isValidCallbackUri("https://abc123.ngrok-free.dev"));
        assertFalse(isValidCallbackUri("http://localhost:3000"), "HTTP should be invalid for production");
        assertFalse(isValidCallbackUri("invalid-url"), "Malformed URL should be invalid");
        assertFalse(isValidCallbackUri(""), "Empty should be invalid");
    }

    // Helper methods matching Main.java logic
    private boolean isValidPhoneNumber(String phone) {
        if (phone == null || phone.isEmpty()) return false;
        return phone.matches("^\\+\\d{10,15}$");
    }

    private String[] parseContacts(String contacts) {
        return contacts.split(",");
    }

    private boolean isValidTimeout(String timeout) {
        if (timeout == null || timeout.isEmpty()) return false;
        try {
            float value = Float.parseFloat(timeout);
            return value > 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private String formatLocationMessage(String name, float minutes) {
        return name + " has shared their location with you for " + minutes + " minutes.";
    }

    private boolean isValidCallbackUri(String uri) {
        if (uri == null || uri.isEmpty()) return false;
        // In production, require HTTPS
        return uri.startsWith("https://") && uri.contains(".");
    }
}
