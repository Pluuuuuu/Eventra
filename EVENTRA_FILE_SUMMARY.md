# Eventra Event Management System - File Summary

This document provides a comprehensive overview of all files in the Eventra application, explaining their purpose, role, and usage within the system.

## Application Structure Overview

The Eventra application follows a standard JavaFX MVC (Model-View-Controller) architecture with the following main components:

### 1. Main Application Files

#### `src/main/java/com/eventra/App.java`
- **Role**: Main application entry point
- **Purpose**: JavaFX Application class that initializes the application
- **Key Functions**:
  - Initializes database connection and schema
  - Loads the initial Welcome screen
  - Sets up the main application window
  - Handles application shutdown and cleanup
- **Usage**: Called by the JVM when starting the application
- **Dependencies**: DatabaseInitializer, Welcome.fxml

#### `src/main/java/com/eventra/Main.java`
- **Role**: Template/example file (not used in production)
- **Purpose**: IDE-generated starter code with basic Java syntax examples
- **Key Functions**: Demonstrates basic Java programming concepts
- **Usage**: Not actively used in the Eventra application
- **Note**: This file can be removed or replaced with proper documentation

### 2. Database Management Files

#### `src/main/java/com/eventra/DatabaseInitializer.java`
- **Role**: Database schema setup and initialization
- **Purpose**: Creates all database tables and initializes with default data
- **Key Functions**:
  - Creates lookup tables (StatusType, RoleType, EventStatusType, etc.)
  - Creates main business tables (UserM, EventM, Attendee, etc.)
  - Inserts initial reference data
  - Creates sample users for testing
  - Validates database structure
- **Usage**: Called by App.java during application startup
- **Dependencies**: Db.java, EventSaveDAO, DatabaseValidator

#### `src/main/java/com/eventra/Db.java`
- **Role**: Database connection manager
- **Purpose**: Manages database connections using HikariCP connection pooling
- **Key Functions**:
  - Initializes connection pool with configuration from properties file
  - Provides pooled database connections
  - Handles connection cleanup on application shutdown
  - Graceful error handling for database unavailability
- **Usage**: Used by all DAO classes and database operations
- **Dependencies**: config.properties, HikariCP library

#### `src/main/java/com/eventra/TestConnection.java`
- **Role**: Database connection testing utility
- **Purpose**: Tests database connectivity and configuration
- **Key Functions**:
  - Validates database connection settings
  - Tests SQL queries
  - Provides debugging information
- **Usage**: Development and troubleshooting tool

#### `src/main/java/com/eventra/DatabaseDebugger.java`
- **Role**: Database debugging and diagnostic tool
- **Purpose**: Provides detailed database information and troubleshooting
- **Key Functions**:
  - Analyzes database structure
  - Validates data integrity
  - Provides debugging output
- **Usage**: Development and maintenance tool

### 3. Model Classes (Data Objects)

#### `src/main/java/com/eventra/model/User.java`
- **Role**: User account data model
- **Purpose**: Represents user accounts with authentication and authorization data
- **Key Functions**:
  - Stores user identification and profile information
  - Manages role-based access control (SuperAdmin, Admin, Staff, Attendee)
  - Tracks account status and login attempts
  - Provides helper methods for name formatting
- **Usage**: Used by UserDAO, controllers, and throughout the application
- **Database Table**: UserM

#### `src/main/java/com/eventra/model/Event.java`
- **Role**: Event data model
- **Purpose**: Represents events in the system
- **Key Functions**:
  - Stores event scheduling and timing information
  - Manages event details and descriptions
  - Tracks attendee capacity and registration
  - Provides helper methods for event status checking
- **Usage**: Used by EventDAO, controllers, and event management features
- **Database Table**: EventM

#### `src/main/java/com/eventra/model/Attendee.java`
- **Role**: Attendee data model
- **Purpose**: Represents event attendees with detailed profile information
- **Key Functions**:
  - Stores attendee-specific information (organization, phone, location, etc.)
  - Links to user accounts
  - Manages attendee types (Regular, VIP)
  - Tracks attendee status and preferences
- **Usage**: Used by AttendeeDAO and attendee management features
- **Database Table**: Attendee

#### `src/main/java/com/eventra/model/EventRegistration.java`
- **Role**: Event registration data model
- **Purpose**: Represents the relationship between attendees and events
- **Key Functions**:
  - Links attendees to events
  - Tracks registration status (Pending, Confirmed, Cancelled, Attended)
  - Manages registration timestamps
- **Usage**: Used by EventRegistrationDAO and registration features
- **Database Table**: Registration

#### `src/main/java/com/eventra/model/Session.java`
- **Role**: Event session data model
- **Purpose**: Represents individual sessions within events
- **Key Functions**:
  - Stores session scheduling and details
  - Links sessions to events and presenters
  - Manages session status and room assignments
- **Usage**: Used by SessionDAO and session management features
- **Database Table**: SessionM

#### `src/main/java/com/eventra/model/Presenter.java`
- **Role**: Presenter data model
- **Purpose**: Represents event presenters and speakers
- **Key Functions**:
  - Stores presenter information (bio, contact info, photo)
  - Links presenters to sessions
  - Manages presenter credentials and experience
- **Usage**: Used by PresenterDAO and presenter management features
- **Database Table**: Presenter

### 4. DAO Classes (Data Access Objects)

#### `src/main/java/com/eventra/dao/UserDAO.java`
- **Role**: User data access operations
- **Purpose**: Handles all database operations related to user accounts
- **Key Functions**:
  - User authentication and login
  - User creation and registration
  - User profile updates
  - User search and retrieval
  - Password management
- **Usage**: Used by controllers for user management operations
- **Dependencies**: Db.java, User model

#### `src/main/java/com/eventra/dao/EventDAO.java`
- **Role**: Event data access operations
- **Purpose**: Handles all database operations related to events
- **Key Functions**:
  - Event creation and updates
  - Event search and filtering
  - Event status management
  - Event capacity tracking
- **Usage**: Used by controllers for event management operations
- **Dependencies**: Db.java, Event model

#### `src/main/java/com/eventra/dao/EventSaveDAO.java`
- **Role**: Event saving and management operations
- **Purpose**: Handles complex event operations including sessions and presenters
- **Key Functions**:
  - Complete event creation with sessions
  - Presenter management
  - Duplicate presenter cleanup
  - Event validation
- **Usage**: Used by CreateEventController for comprehensive event creation
- **Dependencies**: Db.java, Event, Session, Presenter models

#### `src/main/java/com/eventra/dao/AttendeeDAO.java`
- **Role**: Attendee data access operations
- **Purpose**: Handles all database operations related to attendees
- **Key Functions**:
  - Attendee profile management
  - Attendee search and filtering
  - Organization management
  - Attendee type management
- **Usage**: Used by controllers for attendee management operations
- **Dependencies**: Db.java, Attendee model

#### `src/main/java/com/eventra/dao/EventRegistrationDAO.java`
- **Role**: Event registration data access operations
- **Purpose**: Handles all database operations related to event registrations
- **Key Functions**:
  - Registration creation and management
  - Registration status updates
  - Registration validation
  - Capacity checking
- **Usage**: Used by controllers for registration operations
- **Dependencies**: Db.java, EventRegistration model

#### `src/main/java/com/eventra/dao/DashboardDAO.java`
- **Role**: Dashboard data access operations
- **Purpose**: Provides data for dashboard displays and analytics
- **Key Functions**:
  - Event statistics and metrics
  - User activity tracking
  - Registration analytics
  - System overview data
- **Usage**: Used by dashboard controllers for displaying statistics
- **Dependencies**: Db.java, various models

#### `src/main/java/com/eventra/dao/SessionDAO.java`
- **Role**: Session data access operations
- **Purpose**: Handles all database operations related to event sessions
- **Key Functions**:
  - Session creation and management
  - Session scheduling
  - Presenter assignments
  - Room assignments
- **Usage**: Used by controllers for session management operations
- **Dependencies**: Db.java, Session model

#### `src/main/java/com/eventra/dao/PresenterDAO.java`
- **Role**: Presenter data access operations
- **Purpose**: Handles all database operations related to presenters
- **Key Functions**:
  - Presenter creation and management
  - Presenter search and filtering
  - Bio and contact information management
- **Usage**: Used by controllers for presenter management operations
- **Dependencies**: Db.java, Presenter model

#### `src/main/java/com/eventra/dao/DatabaseValidator.java`
- **Role**: Database validation and integrity checking
- **Purpose**: Ensures database structure and data integrity
- **Key Functions**:
  - Table structure validation
  - Foreign key relationship checking
  - Data consistency verification
  - Schema validation
- **Usage**: Called by DatabaseInitializer and for maintenance operations
- **Dependencies**: Db.java

### 5. Controller Classes (UI Logic)

#### `src/main/java/com/eventra/controller/LoginController.java`
- **Role**: Login screen controller
- **Purpose**: Manages user authentication and login process
- **Key Functions**:
  - User authentication with email and password
  - Password visibility toggle
  - Error handling and user feedback
  - Navigation to appropriate dashboard based on user role
- **Usage**: Controls Login.fxml view
- **Dependencies**: UserDAO, SessionManager, ViewUtil

#### `src/main/java/com/eventra/controller/SignUpController.java`
- **Role**: User registration controller
- **Purpose**: Manages new user account creation
- **Key Functions**:
  - User registration form handling
  - Input validation
  - Password confirmation
  - Account creation
- **Usage**: Controls SignUp.fxml view
- **Dependencies**: UserDAO, ViewUtil

#### `src/main/java/com/eventra/controller/DashboardController.java`
- **Role**: Main dashboard controller
- **Purpose**: Manages the main application dashboard
- **Key Functions**:
  - Dashboard initialization
  - User information display
  - Navigation to different sections
  - Logout functionality
- **Usage**: Controls Dashboard.fxml view
- **Dependencies**: SessionManager, ViewUtil

#### `src/main/java/com/eventra/controller/AttendeeEventsController.java`
- **Role**: Attendee events view controller
- **Purpose**: Manages the display and interaction with events for attendees
- **Key Functions**:
  - Event listing and filtering
  - Event registration
  - Event details display
  - Search functionality
- **Usage**: Controls AttendeeEvents.fxml view
- **Dependencies**: EventDAO, EventRegistrationDAO

#### `src/main/java/com/eventra/controller/EventDetailsController.java`
- **Role**: Event details controller
- **Purpose**: Manages the display of detailed event information
- **Key Functions**:
  - Event information display
  - Session information
  - Presenter details
  - Registration functionality
- **Usage**: Controls EventDetails.fxml view
- **Dependencies**: EventDAO, SessionDAO, PresenterDAO

#### `src/main/java/com/eventra/controller/CreateEventController.java`
- **Role**: Event creation controller
- **Purpose**: Manages the event creation process
- **Key Functions**:
  - Event form handling
  - Session creation
  - Presenter management
  - Event validation and saving
- **Usage**: Controls CreateEvent.fxml view
- **Dependencies**: EventSaveDAO, PresenterDAO

#### `src/main/java/com/eventra/controller/AttendeeProfileController.java`
- **Role**: Attendee profile controller
- **Purpose**: Manages attendee profile information
- **Key Functions**:
  - Profile information display
  - Profile editing
  - Registration history
  - Preferences management
- **Usage**: Controls AttendeeProfile.fxml view
- **Dependencies**: AttendeeDAO, EventRegistrationDAO

#### `src/main/java/com/eventra/controller/MyScheduleController.java`
- **Role**: Personal schedule controller
- **Purpose**: Manages the user's personal event schedule
- **Key Functions**:
  - Registered events display
  - Schedule management
  - Event reminders
  - Schedule export
- **Usage**: Controls MySchedule.fxml view
- **Dependencies**: EventRegistrationDAO, EventDAO

#### `src/main/java/com/eventra/controller/AdminDashboardController.java`
- **Role**: Admin dashboard controller
- **Purpose**: Manages the admin-specific dashboard
- **Key Functions**:
  - Admin statistics display
  - Event management navigation
  - User management navigation
  - System overview
- **Usage**: Controls AdminDashboard.fxml view
- **Dependencies**: DashboardDAO

#### `src/main/java/com/eventra/controller/AdminManageEventsController.java`
- **Role**: Admin event management controller
- **Purpose**: Manages event administration and oversight
- **Key Functions**:
  - Event listing and management
  - Event status updates
  - Event editing
  - Event deletion
- **Usage**: Controls AdminManageEvents.fxml view
- **Dependencies**: EventDAO

#### `src/main/java/com/eventra/controller/SuperAdminDashboardController.java`
- **Role**: Super admin dashboard controller
- **Purpose**: Manages the super admin dashboard with highest privileges
- **Key Functions**:
  - System-wide statistics
  - User management
  - System settings
  - Global administration
- **Usage**: Controls SuperAdminDashboard.fxml view
- **Dependencies**: DashboardDAO

### 6. Utility Classes

#### `src/main/java/com/eventra/util/ViewUtil.java`
- **Role**: View management utility
- **Purpose**: Provides common view navigation and management functions
- **Key Functions**:
  - FXML loading and scene management
  - View transitions
  - Error handling for view operations
- **Usage**: Used by all controllers for view navigation
- **Dependencies**: JavaFX FXML loader

#### `src/main/java/com/eventra/util/SessionManager.java`
- **Role**: User session management
- **Purpose**: Manages user sessions and authentication state
- **Key Functions**:
  - User session creation and management
  - Authentication state tracking
  - Session validation
  - Logout functionality
- **Usage**: Used by controllers for session management
- **Dependencies**: User model

#### `src/main/java/com/eventra/util/RouteManager.java`
- **Role**: Application routing utility
- **Purpose**: Manages navigation between different application views
- **Key Functions**:
  - Route definitions
  - Navigation logic
  - View state management
- **Usage**: Used by controllers for navigation
- **Dependencies**: ViewUtil

#### `src/main/java/com/eventra/util/PasswordHashGenerator.java`
- **Role**: Password security utility
- **Purpose**: Handles password hashing and security
- **Key Functions**:
  - Password hashing using BCrypt
  - Password validation
  - Security utilities
- **Usage**: Used by UserDAO for password management
- **Dependencies**: BCrypt library

### 7. FXML View Files

#### `src/main/resources/fxml/Login.fxml`
- **Role**: Login screen user interface
- **Purpose**: Provides the login form and user interface
- **Usage**: Loaded by LoginController
- **Components**: Email field, password field, login button, signup link

#### `src/main/resources/fxml/SignUp.fxml`
- **Role**: User registration screen
- **Purpose**: Provides the registration form interface
- **Usage**: Loaded by SignUpController
- **Components**: Registration form fields, validation, submit button

#### `src/main/resources/fxml/Dashboard.fxml`
- **Role**: Main application dashboard
- **Purpose**: Central navigation hub for the application
- **Usage**: Loaded by DashboardController
- **Components**: Navigation menu, user info, quick actions

#### `src/main/resources/fxml/AttendeeEvents.fxml`
- **Role**: Events listing for attendees
- **Purpose**: Displays available events for attendees
- **Usage**: Loaded by AttendeeEventsController
- **Components**: Event cards, search, filters, registration buttons

#### `src/main/resources/fxml/EventDetails.fxml`
- **Role**: Detailed event information display
- **Purpose**: Shows comprehensive event information
- **Usage**: Loaded by EventDetailsController
- **Components**: Event details, sessions, presenters, registration

#### `src/main/resources/fxml/CreateEvent.fxml`
- **Role**: Event creation form
- **Purpose**: Provides interface for creating new events
- **Usage**: Loaded by CreateEventController
- **Components**: Event form, session management, presenter assignment

#### `src/main/resources/fxml/AttendeeProfile.fxml`
- **Role**: Attendee profile management
- **Purpose**: Displays and manages attendee profile information
- **Usage**: Loaded by AttendeeProfileController
- **Components**: Profile information, edit form, registration history

#### `src/main/resources/fxml/MySchedule.fxml`
- **Role**: Personal schedule display
- **Purpose**: Shows user's registered events and schedule
- **Usage**: Loaded by MyScheduleController
- **Components**: Schedule view, event details, reminders

#### `src/main/resources/fxml/AdminDashboard.fxml`
- **Role**: Admin dashboard interface
- **Purpose**: Provides admin-specific dashboard functionality
- **Usage**: Loaded by AdminDashboardController
- **Components**: Admin statistics, management tools, navigation

#### `src/main/resources/fxml/SuperAdminDashboard.fxml`
- **Role**: Super admin dashboard interface
- **Purpose**: Provides super admin dashboard with highest privileges
- **Usage**: Loaded by SuperAdminDashboardController
- **Components**: System-wide statistics, user management, system settings

### 8. Configuration Files

#### `src/main/resources/config.properties`
- **Role**: Application configuration
- **Purpose**: Stores database connection and application settings
- **Usage**: Loaded by Db.java for database configuration
- **Contents**: Database URL, username, password, connection settings

#### `src/main/resources/config.properties.template`
- **Role**: Configuration template
- **Purpose**: Provides template for configuration setup
- **Usage**: Reference for setting up config.properties
- **Contents**: Example configuration values

#### `src/main/resources/init-db.sql`
- **Role**: Database initialization script
- **Purpose**: SQL script for database setup
- **Usage**: Reference for manual database setup
- **Contents**: Table creation and initial data insertion

### 9. Build and Deployment Files

#### `pom.xml`
- **Role**: Maven project configuration
- **Purpose**: Defines project dependencies, build process, and configuration
- **Usage**: Used by Maven for building the project
- **Contents**: Dependencies, plugins, build configuration

#### `mvnw` and `mvnw.cmd`
- **Role**: Maven wrapper scripts
- **Purpose**: Allows building without installing Maven locally
- **Usage**: Build scripts for different operating systems

#### Various `.bat` files
- **Role**: Build and run scripts
- **Purpose**: Provide convenient ways to build and run the application
- **Usage**: Development and deployment scripts
- **Examples**: build.bat, run.bat, run-maven.bat

### 10. Documentation Files

#### `Eventra_ERD.md`
- **Role**: Database entity relationship diagram documentation
- **Purpose**: Documents database structure and relationships
- **Usage**: Reference for database design and maintenance

#### `PROFILE_FEATURES_README.md`
- **Role**: Profile features documentation
- **Purpose**: Documents user profile functionality
- **Usage**: Reference for profile feature development

#### `ATTENDEE_NAVIGATION_GUIDE.md`
- **Role**: Attendee navigation documentation
- **Purpose**: Guides users through attendee features
- **Usage**: User documentation and training

#### `FXML_SCREENSHOTTER_README.md`
- **Role**: FXML screenshot utility documentation
- **Purpose**: Documents the screenshot utility tool
- **Usage**: Development tool documentation

## File Relationships and Dependencies

### Core Dependencies
1. **App.java** → DatabaseInitializer → Db.java → config.properties
2. **Controllers** → DAOs → Db.java → Database
3. **FXML Files** → Controllers → Models → DAOs
4. **Models** → Database Tables (via DAOs)

### Data Flow
1. **User Input** → FXML → Controller → DAO → Database
2. **Database** → DAO → Model → Controller → FXML → User Display

### Security Flow
1. **Login** → LoginController → UserDAO → Database → SessionManager
2. **Authorization** → SessionManager → Controllers → Role-based Access

## Summary

The Eventra application is a comprehensive event management system with a well-structured architecture. The files are organized into logical layers:

- **Presentation Layer**: FXML files and Controllers
- **Business Logic Layer**: Models and DAOs
- **Data Access Layer**: Database utilities and connection management
- **Configuration Layer**: Properties files and build configuration

Each file has a specific role and responsibility, contributing to the overall functionality of the event management system. The modular design allows for easy maintenance, testing, and future enhancements.
