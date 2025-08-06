import com.eventra.Db;
import com.eventra.dao.UserDAO;
import com.eventra.dao.AttendeeDAO;
import com.eventra.model.User;
import com.eventra.model.Attendee;
import java.sql.Connection;

public class TestDatabaseConnection {
    public static void main(String[] args) {
        System.out.println("=== Testing Database Connection ===");
        
        try {
            // Test database connection
            Connection conn = Db.get();
            System.out.println("✅ Database connection successful!");
            
            // Test user creation
            System.out.println("\n=== Testing User Creation ===");
            
            // Create a test user
            User testUser = new User();
            testUser.setUsername("testuser123");
            testUser.setFirstName("Test");
            testUser.setLastName("User");
            testUser.setEmail("testuser123@test.com");
            testUser.setPasswordHash(UserDAO.hashPassword("testpass123"));
            testUser.setRoleTypeId(4); // Attendee role
            testUser.setStatusTypeId(1); // Active status
            testUser.setPeriodCanLoginInMinutes(0);
            
            System.out.println("Attempting to create user...");
            boolean userCreated = UserDAO.createUser(testUser);
            System.out.println("User creation result: " + userCreated);
            
            if (userCreated && testUser.getUserId() > 0) {
                System.out.println("User ID: " + testUser.getUserId());
                
                // Test attendee creation
                System.out.println("\n=== Testing Attendee Creation ===");
                Attendee testAttendee = new Attendee();
                testAttendee.setUserId(testUser.getUserId());
                testAttendee.setFirstName("Test");
                testAttendee.setLastName("User");
                testAttendee.setEmail("testuser123@test.com");
                testAttendee.setOrganization("Test Org");
                testAttendee.setType("Regular");
                testAttendee.setPasswordHash(testUser.getPasswordHash());
                testAttendee.setStatusTypeId(1);
                testAttendee.setPeriodCanLoginInMinutes(0);
                
                System.out.println("Attempting to create attendee...");
                boolean attendeeCreated = AttendeeDAO.createAttendee(testAttendee);
                System.out.println("Attendee creation result: " + attendeeCreated);
                
                if (attendeeCreated) {
                    System.out.println("Attendee ID: " + testAttendee.getAttendeeId());
                    System.out.println("✅ Both user and attendee created successfully!");
                } else {
                    System.out.println("❌ Attendee creation failed!");
                }
            } else {
                System.out.println("❌ User creation failed!");
            }
            
        } catch (Exception e) {
            System.err.println("❌ Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
} 