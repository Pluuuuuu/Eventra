/**
 * Dashboard Controller (DashboardController.java)
 * 
 * This class controls the MAIN DASHBOARD SCREEN of the Eventra application. Think of it as the
 * "control center" that shows different content based on the user's role and provides navigation
 * to various parts of the system.
 * 
 * WHAT THIS CLASS DOES:
 * - Displays personalized welcome message and user information
 * - Shows different content based on user role (SuperAdmin, Admin, Staff, Attendee)
 * - Loads and displays user profile pictures and logos
 * - Provides role-specific navigation buttons and functionality
 * - Handles user session management and authentication
 * - Redirects users to appropriate specialized dashboards
 * 
 * KEY CONCEPTS TO UNDERSTAND:
 * - JavaFX Controller: Connects the FXML UI design with Java code
 * - FXML Injection: @FXML annotation links UI elements to Java variables
 * - Role-Based Access Control: Different users see different content based on their permissions
 * - Session Management: Tracks who is currently logged in
 * - Dynamic UI Generation: Creates buttons and content based on user role
 * - Navigation Control: Routes users to appropriate screens based on their role
 * 
 * UI COMPONENTS MANAGED:
 * - Welcome text with user's name
 * - User email and role display
 * - Profile picture and logo images
 * - Dynamic content area that changes based on user role
 * - Role-specific navigation buttons
 * 
 * ROLE-BASED FUNCTIONALITY:
 * - SuperAdmin (1): Redirected to specialized SuperAdmin dashboard
 * - Admin (2): Can manage events and view reports
 * - Staff (3): Can manage attendees and event operations
 * - Attendee (4): Can view events and manage their schedule
 */
package com.eventra.controller;

// These imports give us access to the tools we need:
import com.eventra.util.SessionManager;          // For managing user sessions and getting current user
import com.eventra.util.ViewUtil;                 // For navigating between different screens
import com.eventra.model.User;                    // User data model for current logged-in user

// JavaFX imports for the user interface:
import javafx.fxml.FXML;                          // Links this class to FXML UI elements
import javafx.scene.text.Text;                    // For displaying text (welcome message, user info)
import javafx.scene.control.Button;               // For creating clickable buttons
import javafx.scene.layout.VBox;                  // Vertical layout container for organizing content
import javafx.scene.image.ImageView;              // For displaying images (logo, profile picture)
import javafx.scene.image.Image;                  // For loading image files

// Java standard library imports for file operations:
import java.io.File;                              // Represents files on the computer
import java.io.FileInputStream;                   // For reading file contents
import java.io.FileNotFoundException;              // For handling missing files gracefully

/**
 * DashboardController class - Controls the Main Dashboard Screen
 * 
 * This class implements the MVC (Model-View-Controller) pattern:
 * - MODEL: User data from SessionManager, User model class
 * - VIEW: FXML file defines the visual layout of the dashboard
 * - CONTROLLER: This class connects the model and view, handles role-based logic
 * 
 * The @FXML annotation tells JavaFX that this class should be connected
 * to the Dashboard.fxml file, allowing us to control the UI elements.
 * 
 * ROLE-BASED DESIGN:
 * This controller is a perfect example of "role-based access control" where
 * different users see completely different interfaces based on their permissions.
 * This makes the app secure and user-friendly at the same time.
 */
public class DashboardController {
    
    /**
     * FXML INJECTED FIELDS - These are automatically connected to UI elements
     * 
     * The @FXML annotation tells JavaFX to automatically connect these Java variables
     * to the corresponding elements in the Dashboard.fxml file. This is called "dependency injection"
     * and it's how we control the UI from our Java code.
     * 
     * Think of it like having remote controls for each UI element:
     * - You can read what's displayed (getText())
     * - You can change what's displayed (setText())
     * - You can show/hide elements (setVisible())
     * - You can add/remove child elements (getChildren().add())
     */
    @FXML private Text welcomeText;               // Displays personalized welcome message (e.g., "Hello, John!")
    @FXML private Text emailText;                 // Shows the user's email address
    @FXML private Text roleText;                  // Displays the user's role (e.g., "Role: Admin")
    @FXML private VBox roleSpecificContent;       // Container that holds different content based on user role
    @FXML private ImageView logoImageView;        // Displays the Eventra logo
    @FXML private ImageView profileImageView;     // Shows the user's profile picture
    
    /**
     * initialize() method - Sets up the dashboard when it first loads
     * 
     * This method is automatically called by JavaFX when the dashboard screen is displayed.
     * Think of it as the "setup crew" that prepares everything before the user sees the screen.
     * 
     * WHAT IT DOES:
     * 1. Loads the Eventra logo and displays it
     * 2. Gets the currently logged-in user from the session manager
     * 3. If user is logged in: displays personalized information and role-specific content
     * 4. If no user is logged in: redirects to the login screen for security
     * 5. Sets up the entire dashboard based on user permissions
     * 
     * SECURITY CHECK:
     * The first thing this method does is check if someone is actually logged in.
     * If no user is found, it immediately redirects to login - this prevents
     * unauthorized access to the dashboard.
     * 
     * PERSONALIZATION:
     * Once we know who the user is, we customize everything:
     * - Welcome message with their first name
     * - Display their email and role
     * - Show their profile picture
     * - Create role-specific navigation buttons
     * 
     * @FXML annotation means this method is automatically called by JavaFX
     */
    @FXML
    public void initialize() {
        // STEP 1: Load and display the Eventra logo
        loadLogo();
        
        // STEP 2: Get the currently logged-in user from session management
        User currentUser = SessionManager.getCurrentUser();
        
        if (currentUser != null) {
            // STEP 3A: User is logged in - display personalized dashboard
            // Display personalized welcome message with user's first name
            welcomeText.setText("Hello, " + currentUser.getFirstName() + "!");
            // Show user's email address
            emailText.setText("Email: " + currentUser.getEmail());
            
            // STEP 3B: Convert role number to readable role name and display it
            String roleName = getRoleName(currentUser.getRoleTypeId());
            roleText.setText("Role: " + roleName);
            
            // STEP 3C: Load and display the user's profile picture
            loadProfilePicture(currentUser);
            
            // STEP 3D: Create and show content specific to the user's role
            showRoleSpecificContent(currentUser.getRoleTypeId());
        } else {
            // STEP 3E: No user logged in - security redirect
            // This prevents unauthorized access to the dashboard
            ViewUtil.switchTo("Login", welcomeText.getScene().getWindow());
        }
    }
    
    /**
     * showRoleSpecificContent() method - Creates different content based on user role
     * 
     * This method is the heart of the role-based access control system. It determines
     * what buttons and functionality each user sees based on their permission level.
     * 
     * HOW IT WORKS:
     * 1. Clears any existing content from the dashboard
     * 2. Uses a switch statement to check the user's role ID
     * 3. Calls the appropriate method to create role-specific content
     * 4. Each role gets completely different navigation options
     * 
     * ROLE ID MAPPING:
     * - 1 = SuperAdmin: Gets redirected to specialized dashboard
     * - 2 = Admin: Can manage events and view reports
     * - 3 = Staff: Can manage attendees and event operations
     * - 4 = Attendee: Can view events and manage their schedule
     * - Default: Shows basic content for unknown roles
     * 
     * SECURITY BENEFIT:
     * Users can only see and access features they're authorized to use.
     * This prevents confusion and maintains system security.
     * 
     * @param roleTypeId - The numeric role identifier (1-4)
     */
    private void showRoleSpecificContent(int roleTypeId) {
        // Clear existing content to prepare for new role-specific content
        roleSpecificContent.getChildren().clear();
        
        // Use switch statement to handle different user roles
        switch (roleTypeId) {
            case 1: // SuperAdmin - highest level of access
                // SuperAdmins get redirected to a specialized dashboard with full system control
                navigateToSuperAdminDashboard();
                return; // Return early since we're navigating away
            case 2: // Admin - can manage events and view reports
                createAdminContent();
                break;
            case 3: // Staff - can help with event operations and attendee management
                createStaffContent();
                break;
            case 4: // Attendee - regular users who can register for events
                createAttendeeContent();
                break;
            default: // Unknown role - show basic content
                createDefaultContent();
        }
    }
    
    private void navigateToSuperAdminDashboard() {
        System.out.println("Navigating to SuperAdmin Dashboard");
        ViewUtil.switchTo("SuperAdminDashboard", welcomeText.getScene().getWindow());
    }
    
    private void createSuperAdminContent() {
        // This method is now only kept for backward compatibility
        // SuperAdmins are redirected to the new dashboard view
        Button dashboardBtn = new Button("Go to Dashboard");
        dashboardBtn.setOnAction(e -> navigateToSuperAdminDashboard());
        dashboardBtn.getStyleClass().add("primary-button");
        
        roleSpecificContent.getChildren().add(dashboardBtn);
    }
    
    /**
     * createAdminContent() method - Creates navigation buttons for Admin users
     * 
     * Admin users have elevated permissions compared to regular attendees.
     * They can manage events and view system reports, but don't have full
     * system administration access like SuperAdmins.
     * 
     * WHAT ADMINS CAN DO:
     * - Manage Events: Create, edit, delete, and organize events
     * - View Reports: See attendance statistics and event performance data
     * - Moderate Content: Approve or reject event submissions
     * - Manage Venues: Set up and configure event locations
     * 
     * BUTTON DESIGN:
     * - Primary Button: "Manage Events" - main function, gets prominent styling
     * - Secondary Button: "View Reports" - supporting function, gets subtle styling
     * - Both buttons are added to the roleSpecificContent container
     * 
     * SECURITY:
     * Only users with roleTypeId = 2 (Admin) will see these buttons.
     * Regular attendees cannot access these administrative functions.
     */
    private void createAdminContent() {
        // Create the main "Manage Events" button - primary function for admins
        Button manageEventsBtn = new Button("Manage Events");
        manageEventsBtn.setOnAction(e -> handleManageEvents()); // Handle button clicks
        manageEventsBtn.getStyleClass().add("primary-button"); // Apply primary button styling
        
        // Create the "View Reports" button - secondary function for admins
        Button reportsBtn = new Button("View Reports");
        reportsBtn.setOnAction(e -> handleViewReports()); // Handle button clicks
        reportsBtn.getStyleClass().add("secondary-button"); // Apply secondary button styling
        
        // Add both buttons to the dashboard content area
        roleSpecificContent.getChildren().addAll(manageEventsBtn, reportsBtn);
    }
    
    private void createStaffContent() {
        Button eventOperationsBtn = new Button("Event Operations");
        eventOperationsBtn.setOnAction(e -> handleEventOperations());
        eventOperationsBtn.getStyleClass().add("primary-button");
        
        Button attendeeManagementBtn = new Button("Attendee Management");
        attendeeManagementBtn.setOnAction(e -> handleAttendeeManagement());
        attendeeManagementBtn.getStyleClass().add("secondary-button");
        
        roleSpecificContent.getChildren().addAll(eventOperationsBtn, attendeeManagementBtn);
    }
    
    private void createAttendeeContent() {
        Button viewEventsBtn = new Button("View Events");
        viewEventsBtn.setOnAction(e -> handleViewEvents());
        viewEventsBtn.getStyleClass().add("primary-button");
        
        Button myProfileBtn = new Button("My Profile");
        myProfileBtn.setOnAction(e -> handleMyProfile());
        myProfileBtn.getStyleClass().add("secondary-button");
        
        roleSpecificContent.getChildren().addAll(viewEventsBtn, myProfileBtn);
    }
    
    private void createDefaultContent() {
        Button viewEventsBtn = new Button("View Events");
        viewEventsBtn.setOnAction(e -> handleViewEvents());
        viewEventsBtn.getStyleClass().add("primary-button");
        
        roleSpecificContent.getChildren().add(viewEventsBtn);
    }
    
    // Role-specific action handlers
    private void handleManageUsers() {
        System.out.println("Manage Users clicked - SuperAdmin functionality");
        ViewUtil.switchTo("SuperAdminManageUsers", welcomeText.getScene().getWindow());
    }
    
    private void handleSystemSettings() {
        System.out.println("System Settings clicked - SuperAdmin functionality");
        ViewUtil.switchTo("SuperAdminSystemSettings", welcomeText.getScene().getWindow());
    }
    
    private void handleManageEvents() {
        System.out.println("Manage Events clicked - Admin functionality");
        ViewUtil.switchTo("AdminManageEvents", welcomeText.getScene().getWindow());
    }
    
    private void handleViewReports() {
        System.out.println("View Reports clicked - Admin functionality");
        ViewUtil.switchTo("AdminReports", welcomeText.getScene().getWindow());
    }
    
    private void handleEventOperations() {
        System.out.println("Event Operations clicked - Staff functionality");
        ViewUtil.switchTo("StaffEventOperations", welcomeText.getScene().getWindow());
    }
    
    private void handleAttendeeManagement() {
        System.out.println("Attendee Management clicked - Staff functionality");
        ViewUtil.switchTo("StaffAttendeeManagement", welcomeText.getScene().getWindow());
    }
    
    @FXML
    private void handleViewEvents() {
        System.out.println("View Events clicked - Attendee functionality");
        ViewUtil.switchTo("AttendeeEvents", welcomeText.getScene().getWindow());
    }
    
    private void handleMyProfile() {
        System.out.println("My Profile clicked - Attendee functionality");
        ViewUtil.switchTo("AttendeeProfile", welcomeText.getScene().getWindow());
    }
    
    @FXML
    private void handleLogout() {
        // Clear session
        SessionManager.logout();
        
        // Navigate back to login
        ViewUtil.switchTo("Login", welcomeText.getScene().getWindow());
    }
    
    private String getRoleName(int roleTypeId) {
        switch (roleTypeId) {
            case 1:
                return "Super Administrator";
            case 2:
                return "Administrator";
            case 3:
                return "Staff";
            case 4:
                return "Attendee";
            default:
                return "Unknown";
        }
    }
    
    private void loadLogo() {
        try {
            // Load logo from resources
            String logoPath = getClass().getResource("/images/logo.png.png").toExternalForm();
            Image logoImage = new Image(logoPath);
            logoImageView.setImage(logoImage);
        } catch (Exception e) {
            System.err.println("Error loading logo: " + e.getMessage());
        }
    }
    
    private void loadProfilePicture(User user) {
        try {
            if (user.getProfilePicUrl() != null && !user.getProfilePicUrl().trim().isEmpty()) {
                // Load user's profile picture
                File profilePicFile = new File(user.getProfilePicUrl());
                if (profilePicFile.exists()) {
                    Image profileImage = new Image(new FileInputStream(profilePicFile));
                    profileImageView.setImage(profileImage);
                } else {
                    loadDefaultProfilePicture();
                }
            } else {
                loadDefaultProfilePicture();
            }
        } catch (FileNotFoundException e) {
            System.err.println("Profile picture file not found: " + e.getMessage());
            loadDefaultProfilePicture();
        } catch (Exception e) {
            System.err.println("Error loading profile picture: " + e.getMessage());
            loadDefaultProfilePicture();
        }
    }
    
    private void loadDefaultProfilePicture() {
        try {
            // Load default profile picture from resources
            String defaultImagePath = getClass().getResource("/images/logo.png.png").toExternalForm();
            Image defaultImage = new Image(defaultImagePath);
            profileImageView.setImage(defaultImage);
        } catch (Exception e) {
            System.err.println("Error loading default profile picture: " + e.getMessage());
        }
    }
    
    @FXML
    private void handleProfilePictureClick() {
        // Navigate to user info page
        ViewUtil.switchTo("UserInfo", profileImageView.getScene().getWindow());
    }
} 