// Test account creation process
// Run this from IntelliJ to debug the account creation issue

import com.eventra.dao.UserDAO;
import com.eventra.dao.AttendeeDAO;
import com.eventra.model.User;
import com.eventra.model.Attendee;

public class TestAccountCreation {
    public static void main(String[] args) {
        System.out.println("=== Testing Account Creation Process ===");
        
        try {
            // Test data
            String username = "testuser123";
            String firstName = "Test";
            String lastName = "User";
            String email = "testuser123@example.com";
            String password = "password123";
            String organization = "Test Org";
            
            System.out.println("1. Checking if username exists...");
            boolean usernameExists = UserDAO.usernameExists(username);
            System.out.println("   Username exists: " + usernameExists);
            
            System.out.println("2. Checking if email exists...");
            boolean userEmailExists = UserDAO.emailExists(email);
            boolean attendeeEmailExists = AttendeeDAO.emailExists(email);
            System.out.println("   Email exists in UserM: " + userEmailExists);
            System.out.println("   Email exists in Attendee: " + attendeeEmailExists);
            
            if (userEmailExists || attendeeEmailExists) {
                System.out.println("   Email already exists, skipping test");
                return;
            }
            
            System.out.println("3. Creating User record...");
            String hashedPassword = UserDAO.hashPassword(password);
            User newUser = new User(username, firstName, lastName, email, hashedPassword);
            newUser.setRoleTypeId(4); // Attendee role
            
            boolean userCreated = UserDAO.createUser(newUser);
            System.out.println("   User created: " + userCreated);
            System.out.println("   User ID: " + newUser.getUserId());
            
            if (userCreated && newUser.getUserId() > 0) {
                System.out.println("4. Creating Attendee record...");
                Attendee newAttendee = new Attendee(firstName, lastName, email, organization, hashedPassword, "Regular");
                newAttendee.setUserId(newUser.getUserId());
                
                boolean attendeeCreated = AttendeeDAO.createAttendee(newAttendee);
                System.out.println("   Attendee created: " + attendeeCreated);
                System.out.println("   Attendee ID: " + newAttendee.getAttendeeId());
                
                if (attendeeCreated) {
                    System.out.println("5. SUCCESS: Both records created successfully!");
                } else {
                    System.out.println("5. FAILED: Attendee creation failed");
                }
            } else {
                System.out.println("4. FAILED: User creation failed");
            }
            
        } catch (Exception e) {
            System.err.println("Error during test: " + e.getMessage());
            e.printStackTrace();
        }
    }
} 