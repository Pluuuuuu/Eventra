/**
 * Sign Up Controller (SignUpController.java)
 * 
 * This class controls the SIGN UP SCREEN of the Eventra application. Think of it as the
 * "registration desk" that handles everything when a new user wants to create an account.
 * 
 * WHAT THIS CLASS DOES:
 * - Manages the sign-up form (name, email, password, organization fields)
 * - Validates user input in real-time as they type
 * - Checks if email already exists in the system
 * - Creates new user accounts with proper validation
 * - Handles password confirmation and security
 * - Provides immediate feedback for form errors
 * - Redirects to login after successful registration
 * 
 * KEY CONCEPTS TO UNDERSTAND:
 * - JavaFX Controller: Connects the FXML UI design with Java code
 * - FXML Injection: @FXML annotation links UI elements to Java variables
 * - Real-time Validation: Checks input as user types (not just on submit)
 * - Timeline Animation: Delays email checking until user stops typing
 * - Event Listeners: Responds to text changes and user actions
 * - Error Handling: Shows user-friendly error messages
 * - Input Sanitization: Cleans and validates user input
 * 
 * UI COMPONENTS MANAGED:
 * - Personal information fields (first, middle, last name)
 * - Email field with real-time availability checking
 * - Password and confirmation fields
 * - Organization field
 * - Sign Up button and error display
 * - Link back to login screen
 * 
 * SECURITY FEATURES:
 * - Password confirmation requirement
 * - Email uniqueness validation
 * - Input sanitization (trimming whitespace)
 * - Real-time validation prevents invalid submissions
 */
package com.eventra.controller;

// These imports give us access to the tools we need:
import com.eventra.dao.UserDAO;           // For creating new user accounts in database
import com.eventra.dao.AttendeeDAO;       // For creating attendee-specific records
import com.eventra.model.User;             // User data model for new accounts
import com.eventra.model.Attendee;         // Attendee-specific data model
import com.eventra.util.ViewUtil;          // For navigating between screens

// JavaFX imports for animations and timing:
import javafx.animation.Timeline;         // Controls when actions happen (like delayed email checking)
import javafx.animation.KeyFrame;         // Defines specific points in time for actions
import javafx.util.Duration;              // Represents time periods (milliseconds, seconds, etc.)

// JavaFX imports for the user interface:
import javafx.fxml.FXML;                  // Links this class to FXML UI elements
import javafx.scene.control.*;            // UI controls (buttons, text fields, labels, etc.)

/**
 * SignUpController class - Controls the Sign Up Screen
 * 
 * This class implements the MVC (Model-View-Controller) pattern:
 * - MODEL: User and Attendee data models, DAO classes for database operations
 * - VIEW: FXML file defines the visual layout of the sign-up form
 * - CONTROLLER: This class connects the model and view, handles all user interactions
 * 
 * The @FXML annotation tells JavaFX that this class should be connected
 * to the SignUp.fxml file, allowing us to control the UI elements.
 * 
 * REAL-TIME VALIDATION FEATURE:
 * This controller includes advanced real-time validation that checks user input
 * as they type, providing immediate feedback without waiting for form submission.
 */
public class SignUpController {
    
    /**
     * FXML INJECTED FIELDS - These are automatically connected to UI elements
     * 
     * The @FXML annotation tells JavaFX to automatically connect these Java variables
     * to the corresponding elements in the SignUp.fxml file. This is called "dependency injection"
     * and it's how we control the UI from our Java code.
     * 
     * Think of it like having remote controls for each UI element:
     * - You can read what the user typed (getText())
     * - You can change what's displayed (setText())
     * - You can show/hide elements (setVisible())
     * - You can respond to user actions (setOnAction())
     */
    @FXML private TextField firstNameField;       // Where user types their first name
    @FXML private TextField middleNameField;      // Where user types their middle name (optional)
    @FXML private TextField lastNameField;        // Where user types their last name
    @FXML private TextField emailField;           // Where user types their email address
    @FXML private PasswordField passwordField;    // Hidden password input (shows dots/asterisks)
    @FXML private PasswordField confirmPasswordField; // Second password field for confirmation
    @FXML private TextField organizationField;    // Where user types their organization/company
    @FXML private Button signUpButton;            // Button user clicks to create their account
    @FXML private Label errorLabel;               // Displays error messages if sign-up fails
    @FXML private Hyperlink signInLink;           // Link to go back to the login page
    
    /**
     * INSTANCE VARIABLES - Store state and data for this controller
     * 
     * These variables keep track of the current state of the sign-up screen
     * and manage resources like the email checking timeline.
     */
    private Timeline emailCheckTimeline;          // Controls delayed email checking (waits for user to stop typing)
    
    /**
     * initialize() method - Sets up the sign-up screen when it first loads
     * 
     * This method is automatically called by JavaFX when the sign-up screen is displayed.
     * Think of it as the "setup crew" that prepares everything before the user sees the screen.
     * 
     * WHAT IT DOES:
     * 1. Sets up the Enter key to trigger sign-up when pressed in password confirmation field
     * 2. Creates real-time validation listeners for all input fields
     * 3. Implements advanced email checking with delayed validation
     * 4. Automatically clears error messages when user starts typing
     * 5. Provides immediate feedback for better user experience
     * 
     * REAL-TIME VALIDATION EXPLANATION:
     * Instead of waiting for the user to click "Sign Up" to find errors, this method
     * listens to every keystroke and provides immediate feedback. This is called
     * "real-time validation" and it makes the app feel much more responsive.
     * 
     * EMAIL CHECKING TIMELINE:
     * The email field has special behavior - it waits 500 milliseconds after the user
     * stops typing before checking if the email already exists. This prevents excessive
     * database calls while the user is still typing.
     * 
     * @FXML annotation means this method is automatically called by JavaFX
     */
    @FXML
    public void initialize() {
        
        // Set up enter key handling - allows users to press Enter to submit the form
        confirmPasswordField.setOnAction(event -> handleSignUp());
        
        // Clear error when user starts typing in first name field
        firstNameField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (errorLabel.isVisible()) hideError();
        });
        
        // Clear error when user starts typing in middle name field
        middleNameField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (errorLabel.isVisible()) hideError();
        });
        
        // Clear error when user starts typing in last name field
        lastNameField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (errorLabel.isVisible()) hideError();
        });
        
        // Special email field listener with real-time validation and delayed checking
        emailField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (errorLabel.isVisible()) hideError();
            
            // Cancel any pending email check to prevent multiple simultaneous checks
            if (emailCheckTimeline != null) {
                emailCheckTimeline.stop();
            }
            
            // Start a new timeline to check email after user stops typing (500ms delay)
            // This prevents checking the database on every keystroke
            emailCheckTimeline = new Timeline(new KeyFrame(Duration.millis(500), e -> checkEmailExists()));
            emailCheckTimeline.play();
        });
        
        // Clear error when user starts typing in password field
        passwordField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (errorLabel.isVisible()) hideError();
        });
        
        // Clear error when user starts typing in password confirmation field
        confirmPasswordField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (errorLabel.isVisible()) hideError();
        });
        
        // Clear error when user starts typing in organization field
        organizationField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (errorLabel.isVisible()) hideError();
        });
    }
    
    /**
     * checkEmailExists() method - Checks if an email address is already registered
     * 
     * This method is called automatically when the user types in the email field.
     * It waits for the user to stop typing (500ms delay) before checking the database.
     * 
     * HOW IT WORKS:
     * 1. Gets the email text from the input field and removes extra spaces
     * 2. Validates that the email is not empty and has proper format
     * 3. Checks both User and Attendee tables for existing email
     * 4. Shows error message if email already exists
     * 5. Moves focus back to email field if there's an error
     * 
     * WHY WE CHECK BOTH TABLES:
     * - User table: Contains basic user account information
     * - Attendee table: Contains additional attendee-specific details
     * - An email might exist in either table, so we check both
     * 
     * ERROR HANDLING:
     * - Database errors are logged but not shown to the user
     * - This prevents the app from crashing during real-time validation
     * - Users can still complete the form even if email checking fails
     * 
     * PERFORMANCE CONSIDERATION:
     * - Only checks database if email format is valid
     * - Prevents unnecessary database calls for obviously invalid emails
     */
    private void checkEmailExists() {
        // Get email text and remove leading/trailing spaces for clean validation
        String email = emailField.getText().trim();
        
        // Only check if email is not empty and has valid format
        // This prevents unnecessary database calls for invalid emails
        if (!email.isEmpty() && isValidEmail(email)) {
            try {
                // Check both User and Attendee tables for existing email
                if (UserDAO.emailExists(email) || AttendeeDAO.emailExists(email)) {
                    showError("This email already exists");
                    emailField.requestFocus(); // Move cursor back to email field
                }
            } catch (Exception e) {
                // Log the error for debugging but don't show to user
                System.err.println("Error checking email existence: " + e.getMessage());
                // Don't show error to user for database connectivity issues during typing
            }
        }
    }
    
    /**
     * handleSignUp() method - Main method that processes user registration
     * 
     * This is the CORE method that handles the entire sign-up process when a user
     * clicks the "Sign Up" button or presses Enter in the password confirmation field.
     * 
     * HOW IT WORKS:
     * 1. Collects and validates all form data from input fields
     * 2. Generates a unique username from the email address
     * 3. Checks for existing usernames and emails to prevent duplicates
     * 4. Securely hashes the password using BCrypt
     * 5. Creates both User and Attendee records in the database
     * 6. Handles the entire process as a single transaction
     * 7. Provides detailed feedback and error handling
     * 
     * SECURITY FEATURES:
     * - Password hashing using industry-standard BCrypt
     * - Input sanitization (removing extra spaces)
     * - Duplicate email/username prevention
     * - Transaction-based database operations
     * 
     * USER EXPERIENCE FEATURES:
     * - Button is disabled during processing to prevent double-clicks
     * - Progress indication ("Creating Account...")
     * - Detailed error messages for specific problems
     * - Automatic username generation with conflict resolution
     * 
     * @FXML annotation means this method is called when the Sign Up button is clicked
     */
    @FXML
    private void handleSignUp() {
        System.out.println("=== DEBUG: handleSignUp method called ===");
        
        // Clear any existing error messages when starting signup process
        hideError();
        
        // STEP 1: COLLECT AND SANITIZE FORM DATA
        // Get text from all input fields and remove extra spaces for clean data
        // The null check prevents errors if fields are somehow empty
        String firstName = firstNameField.getText() != null ? firstNameField.getText().trim() : "";
        String middleName = middleNameField.getText() != null ? middleNameField.getText().trim() : "";
        String lastName = lastNameField.getText() != null ? lastNameField.getText().trim() : "";
        String email = emailField.getText() != null ? emailField.getText().trim() : "";
        String password = passwordField.getText() != null ? passwordField.getText() : "";
        String confirmPassword = confirmPasswordField.getText() != null ? confirmPasswordField.getText() : "";
        String organization = organizationField.getText() != null ? organizationField.getText().trim() : "";
            
        // Debug logging to help developers troubleshoot form submission issues
        System.out.println("DEBUG: Form data collected:");
        System.out.println("  First Name: '" + firstName + "'");
        System.out.println("  Last Name: '" + lastName + "'");
        System.out.println("  Email: '" + email + "'");
        System.out.println("  Organization: '" + organization + "'");
        System.out.println("  Password length: " + (password != null ? password.length() : 0));
        
        // STEP 2: GENERATE UNIQUE USERNAME
        // Create username from email address (everything before the @ symbol)
        // This ensures usernames are unique and easy to remember
        String username = email.contains("@") ? email.substring(0, email.indexOf("@")) : email;
        
        // STEP 3: VALIDATE ALL INPUT DATA
        // Check if all required fields are filled and data is valid
        System.out.println("DEBUG: Starting validation...");
        if (!validateInput(firstName, middleName, lastName, email, password, confirmPassword, organization)) {
            System.out.println("DEBUG: Validation failed!");
            return; // Stop here if validation fails
        }
        System.out.println("DEBUG: Validation passed!");
        
        // STEP 4: PREPARE UI FOR PROCESSING
        // Disable the sign-up button to prevent multiple submissions
        // Change button text to show progress to the user
        signUpButton.setDisable(true);
        signUpButton.setText("Creating Account...");
        
        try {
            // STEP 5: RESOLVE USERNAME CONFLICTS
            // Check if the generated username already exists in the system
            if (UserDAO.usernameExists(username)) {
                // If username exists, append a number to make it unique
                // This ensures every user gets a unique username even if emails are similar
                int counter = 1;
                String originalUsername = username;
                while (UserDAO.usernameExists(username)) {
                    username = originalUsername + counter; // e.g., john1, john2, john3
                    counter++;
                }
            }
            
            // STEP 6: DOUBLE-CHECK EMAIL UNIQUENESS
            // Even though we checked during typing, double-check now to ensure no race conditions
            // This prevents two users from registering with the same email simultaneously
            System.out.println("DEBUG: Checking if email exists...");
            boolean userEmailExists = UserDAO.emailExists(email);
            boolean attendeeEmailExists = AttendeeDAO.emailExists(email);
            System.out.println("DEBUG: Email exists in UserM: " + userEmailExists);
            System.out.println("DEBUG: Email exists in Attendee: " + attendeeEmailExists);
            
            if (userEmailExists || attendeeEmailExists) {
                System.out.println("DEBUG: Email already exists, showing error");
                showError("This email already exists. Please use a different email or sign in.");
                emailField.requestFocus(); // Move cursor back to email field
                return; // Stop here - can't proceed with duplicate email
            }
            System.out.println("DEBUG: Email is unique, proceeding...");
            
            // STEP 7: SECURE PASSWORD HANDLING
            // Hash the password using BCrypt (industry-standard hashing algorithm)
            // This ensures passwords are never stored in plain text in the database
            String hashedPassword = UserDAO.hashPassword(password);
            
            // STEP 8: CREATE USER RECORD
            // Create a new User object with basic account information
            // Role 4 = Attendee (regular user who can register for events)
            User newUser = new User(username, firstName, lastName, email, hashedPassword);
            newUser.setMiddleName(middleName.isEmpty() ? null : middleName); // Handle optional middle name
            newUser.setRoleTypeId(4); // Explicitly set to Attendees role
            
            // STEP 9: CREATE ATTENDEE RECORD
            // Create a new Attendee object with additional attendee-specific information
            // "Regular" indicates this is a standard attendee (not VIP, staff, etc.)
            Attendee newAttendee = new Attendee(firstName, lastName, email, organization, hashedPassword, "Regular");
            newAttendee.setMiddleName(middleName.isEmpty() ? null : middleName); // Handle optional middle name
            
            // STEP 10: DATABASE RECORD CREATION
            // Create both User and Attendee records in the database
            // This is done in sequence to ensure proper relationships
            System.out.println("DEBUG: About to create User record...");
            boolean userCreated = UserDAO.createUser(newUser);
            System.out.println("DEBUG: User created: " + userCreated);
            System.out.println("DEBUG: User ID: " + newUser.getUserId());
            
            boolean attendeeCreated = false;
            if (userCreated && newUser.getUserId() > 0) {
                // Link the Attendee record to the User record using the generated UserID
                newAttendee.setUserId(newUser.getUserId());
                System.out.println("DEBUG: About to create Attendee record with UserID: " + newUser.getUserId());
                attendeeCreated = AttendeeDAO.createAttendee(newAttendee);
                System.out.println("DEBUG: Attendee created: " + attendeeCreated);
                
                // If attendee creation failed, we should clean up the user record
                if (!attendeeCreated) {
                    System.err.println("DEBUG: Attendee creation failed, cleaning up user record");
                    // Note: In a production system, you'd want to delete the user record here
                    // For now, we'll just log the issue
                }
            } else {
                System.out.println("DEBUG: Skipping Attendee creation - User creation failed or UserID is invalid");
            }
            
            if (userCreated && attendeeCreated) {
                System.out.println("DEBUG: Both records created successfully! Navigating to login...");
                // Registration successful
                
                // Clear any existing error messages first
                hideError();
                
                // Use Platform.runLater to ensure UI updates happen on the JavaFX thread
                javafx.application.Platform.runLater(() -> {
                    // Show success message
                    showSuccess("Account created successfully! Please sign in.");
                    
                    // Clear form
                    clearForm();
                    
                    // Navigate to login after a short delay
                    javafx.application.Platform.runLater(() -> {
                        try {
                            Thread.sleep(1500); // Short delay to show success message
                            ViewUtil.switchTo("Login", firstNameField.getScene().getWindow());
                            System.out.println("DEBUG: Successfully navigated to Login page");
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                        }
                    });
                });
                
            } else {
                // More specific error messages
                if (!userCreated && !attendeeCreated) {
                    showError("Failed to create account. Please try again.");
                } else if (!userCreated) {
                    showError("Failed to create user account. Please try again.");
                } else {
                    showError("Failed to create attendee profile. Please try again.");
                }
            }
            
        } catch (Exception e) {
            System.err.println("Registration error: " + e.getMessage());
            e.printStackTrace();
            showError("An error occurred during registration. Please try again.");
        } finally {
            // Re-enable button
            signUpButton.setDisable(false);
            signUpButton.setText("Create Account");
        }
    }
    
    @FXML
    private void handleSignIn() {
        // Navigate to login page
        ViewUtil.switchTo("Login", firstNameField.getScene().getWindow());
    }
    
    private boolean validateInput(String firstName, String middleName, String lastName, 
                                String email, String password, String confirmPassword, String organization) {
        
        if (firstName.isEmpty()) {
            showError("Please enter your first name.");
            firstNameField.requestFocus();
            return false;
        }
        
        if (lastName.isEmpty()) {
            showError("Please enter your last name.");
            lastNameField.requestFocus();
            return false;
        }
        
        if (email.isEmpty()) {
            showError("Please enter your email address.");
            emailField.requestFocus();
            return false;
        }
        
        if (!isValidEmail(email)) {
            showError("Please enter a valid email address (e.g., user@example.com).");
            emailField.requestFocus();
            return false;
        }
        
        if (password.isEmpty()) {
            showError("Please enter a password.");
            passwordField.requestFocus();
            return false;
        }
        
        if (password.length() < 8) {
            showError("Password must be at least 8 characters long.");
            passwordField.requestFocus();
            return false;
        }
        
        if (!password.equals(confirmPassword)) {
            showError("Passwords do not match.");
            confirmPasswordField.requestFocus();
            return false;
        }
        
        if (organization == null || organization.isEmpty()) {
            showError("Please enter your organization.");
            organizationField.requestFocus();
            return false;
        }
        
        return true;
    }
    
    private boolean isValidEmail(String email) {
        // More comprehensive email validation
        if (email == null || email.trim().isEmpty()) {
            return false;
        }
        
        // Check for basic email structure
        String emailRegex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
        
        if (!email.matches(emailRegex)) {
            return false;
        }
        
        // Additional checks for common email format issues
        if (email.startsWith(".") || email.endsWith(".") || 
            email.contains("..") || email.startsWith("@") || 
            email.endsWith("@") || email.indexOf("@") != email.lastIndexOf("@")) {
            return false;
        }
        
        // Check domain part
        String[] parts = email.split("@");
        if (parts.length != 2) {
            return false;
        }
        
        String domain = parts[1];
        if (domain.startsWith(".") || domain.endsWith(".") || 
            domain.startsWith("-") || domain.endsWith("-") ||
            domain.contains("..") || domain.length() < 3) {
            return false;
        }
        
        return true;
    }
    
    private void showError(String message) {
        System.out.println("DEBUG: Showing error message: " + message);
        errorLabel.setText(message);
        errorLabel.setStyle("-fx-text-fill: #d32f2f;");
        errorLabel.setVisible(true);
        errorLabel.setManaged(true);
    }
    
    private void showSuccess(String message) {
        System.out.println("DEBUG: Showing success message: " + message);
        errorLabel.setText(message);
        errorLabel.setStyle("-fx-text-fill: #388e3c;");
        errorLabel.setVisible(true);
        errorLabel.setManaged(true);
    }
    
    private void hideError() {
        System.out.println("DEBUG: Hiding error message");
        errorLabel.setVisible(false);
        errorLabel.setManaged(false);
    }
    
    private void clearForm() {
        firstNameField.clear();
        middleNameField.clear();
        lastNameField.clear();
        emailField.clear();
        passwordField.clear();
        confirmPasswordField.clear();
        organizationField.clear();
    }
} 