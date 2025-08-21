# Eventra Application - DAOs, Controllers, and FXML Files Explanation

## 1. DAO (Data Access Object) Classes

### Overview
DAOs handle all database operations, providing clean separation between business logic and data access. They encapsulate SQL queries and database-specific code.

### Key DAO Classes

#### UserDAO.java
**Purpose**: User authentication and account management
**Key Methods**:
- `authenticateUser()` - Login with BCrypt password verification
- `createUser()` - New user registration
- `getUserByEmail()` - User lookup
- `updateUser()` - Profile updates

**Security Features**:
- BCrypt password hashing
- Failed login tracking
- Automatic password upgrade from plain text

#### EventDAO.java
**Purpose**: Event management operations
**Key Methods**:
- `createEvent()` - New event creation
- `getAllEvents()` - Event listing
- `updateEvent()` - Event modifications
- `getEventsByStatus()` - Status filtering

#### EventSaveDAO.java
**Purpose**: Complex event operations with sessions
**Key Methods**:
- `saveEventWithSessions()` - Complete event creation
- `cleanupDuplicatePresenters()` - Data integrity
- `validateEventData()` - Business rule validation

#### AttendeeDAO.java
**Purpose**: Attendee profile management
**Key Methods**:
- `createAttendee()` - Profile creation
- `updateAttendee()` - Profile updates
- `getAttendeesByOrganization()` - Organization filtering

#### EventRegistrationDAO.java
**Purpose**: Event registration and attendance
**Key Methods**:
- `registerForEvent()` - Registration with capacity checking
- `updateRegistrationStatus()` - Status management
- `getRegistrationsByEvent()` - Registration listing

#### DashboardDAO.java
**Purpose**: Analytics and dashboard data
**Key Methods**:
- `getEventStatistics()` - Event metrics
- `getUserActivityStats()` - User analytics
- `getRegistrationAnalytics()` - Registration trends

### DAO Design Patterns

```java
// Connection Management
try (Connection conn = Db.get();
     PreparedStatement stmt = conn.prepareStatement(sql)) {
    // Database operations
}

// Result Set Mapping
private static User mapResultSetToUser(ResultSet rs) throws SQLException {
    User user = new User();
    user.setUserId(rs.getInt("UserID"));
    user.setEmail(rs.getString("Email"));
    return user;
}
```

## 2. Controller Classes

### Overview
Controllers manage UI logic, handle user interactions, and coordinate between FXML views and business logic.

### Key Controller Classes

#### LoginController.java
**Purpose**: User authentication interface
**Key Features**:
- Email/password authentication
- Password visibility toggle
- Real-time error clearing
- Role-based navigation

```java
@FXML
private void handleSignIn() {
    String email = emailField.getText().trim();
    String password = passwordField.getText();
    
    Optional<User> user = UserDAO.authenticateUser(email, password);
    if (user.isPresent()) {
        SessionManager.setCurrentUser(user.get());
        navigateToDashboard(user.get().getRoleTypeId());
    } else {
        showError("Invalid email or password");
    }
}
```

#### SignUpController.java
**Purpose**: User registration interface
**Key Features**:
- Real-time email validation
- Password confirmation
- Form validation
- Automatic attendee profile creation

```java
// Real-time email checking
emailField.textProperty().addListener((observable, oldValue, newValue) -> {
    if (emailCheckTimeline != null) {
        emailCheckTimeline.stop();
    }
    emailCheckTimeline = new Timeline(new KeyFrame(Duration.millis(500), e -> checkEmailExists()));
    emailCheckTimeline.play();
});
```

#### DashboardController.java
**Purpose**: Main application dashboard
**Key Features**:
- User information display
- Navigation menu management
- Logout functionality
- Role-based access control

#### AttendeeEventsController.java
**Purpose**: Event browsing and registration
**Key Features**:
- Event listing with filtering
- Search functionality
- Registration process
- Event details navigation

#### CreateEventController.java
**Purpose**: Event creation interface
**Key Features**:
- Multi-step event creation
- Session management
- Presenter assignment
- Form validation

### Controller Design Patterns

```java
// FXML Injection
@FXML private TextField emailField;
@FXML private Button submitButton;

// Event Handling
@FXML
private void handleButtonClick() {
    // Handle user interaction
}

// Validation
private boolean validateInput(String email, String password) {
    if (email.isEmpty() || password.isEmpty()) {
        showError("Please fill in all fields");
        return false;
    }
    return true;
}
```

## 3. FXML (JavaFX Markup Language) Files

### Overview
FXML files define user interface layout declaratively, separating UI design from application logic.

### Key FXML Files

#### Login.fxml
**Purpose**: Login screen interface
**Structure**:
```xml
<HBox xmlns="http://javafx.com/javafx"
      xmlns:fx="http://javafx.com/fxml"
      fx:controller="com.eventra.controller.LoginController"
      alignment="CENTER" spacing="60">

    <!-- Logo Section -->
    <VBox alignment="CENTER" styleClass="logo-section">
        <ImageView fx:id="logoImage" fitHeight="300" fitWidth="300"/>
    </VBox>

    <!-- Login Form -->
    <VBox alignment="CENTER" spacing="25" styleClass="login-card">
        <Text text="Welcome Back" styleClass="login-title"/>
        <TextField fx:id="emailField" promptText="Email"/>
        <PasswordField fx:id="passwordField" promptText="Password"/>
        <Button fx:id="signInButton" text="Sign In"/>
        <Hyperlink fx:id="signUpLink" text="Don't have an account? Sign Up"/>
    </VBox>
</HBox>
```

**Components**:
- Logo display
- Email/password fields
- Password visibility toggle
- Navigation links

#### SignUp.fxml
**Purpose**: User registration form
**Structure**:
```xml
<VBox spacing="20" styleClass="signup-form-container">
    <!-- Name Fields -->
    <HBox spacing="15">
        <TextField fx:id="firstNameField" promptText="First Name"/>
        <TextField fx:id="middleNameField" promptText="Middle Name"/>
    </HBox>
    
    <!-- Contact Information -->
    <TextField fx:id="emailField" promptText="Email"/>
    <TextField fx:id="lastNameField" promptText="Last Name"/>
    
    <!-- Password Fields -->
    <HBox spacing="15">
        <PasswordField fx:id="passwordField" promptText="Password"/>
        <PasswordField fx:id="confirmPasswordField" promptText="Confirm Password"/>
    </HBox>
    
    <!-- Organization -->
    <TextField fx:id="organizationField" promptText="Organization"/>
    
    <!-- Error Display -->
    <Label fx:id="errorLabel" styleClass="error-message" visible="false"/>
    
    <!-- Submit Button -->
    <Button fx:id="signUpButton" text="Create Account"/>
</VBox>
```

**Features**:
- Responsive layout
- Real-time validation
- Error messaging
- Password confirmation

#### Dashboard.fxml
**Purpose**: Main application dashboard
**Structure**:
```xml
<VBox styleClass="dashboard-container">
    <!-- Header -->
    <HBox styleClass="dashboard-header">
        <VBox>
            <Text text="Welcome, " styleClass="welcome-text"/>
            <Text fx:id="userNameText" styleClass="user-name"/>
        </VBox>
        <Button fx:id="logoutButton" text="Logout"/>
    </HBox>

    <!-- Navigation Menu -->
    <HBox spacing="20" styleClass="navigation-menu">
        <Button fx:id="eventsButton" text="Events" onAction="#handleEvents"/>
        <Button fx:id="profileButton" text="Profile" onAction="#handleProfile"/>
        <Button fx:id="scheduleButton" text="My Schedule" onAction="#handleSchedule"/>
    </HBox>

    <!-- Content Area -->
    <VBox fx:id="contentArea" styleClass="content-area"/>
</VBox>
```

**Features**:
- User information display
- Navigation menu
- Dynamic content area
- Logout functionality

#### AttendeeEvents.fxml
**Purpose**: Events listing interface
**Structure**:
```xml
<VBox styleClass="events-container">
    <!-- Search and Filter Bar -->
    <HBox spacing="15" styleClass="search-filter-bar">
        <TextField fx:id="searchField" promptText="Search events..."/>
        <ComboBox fx:id="categoryFilter" promptText="Category"/>
        <ComboBox fx:id="dateFilter" promptText="Date"/>
    </HBox>

    <!-- Events Grid -->
    <ScrollPane fitToWidth="true" styleClass="events-scroll-pane">
        <VBox fx:id="eventsContainer" spacing="20" styleClass="events-grid">
            <!-- Event cards dynamically added here -->
        </VBox>
    </ScrollPane>
</VBox>
```

**Features**:
- Search functionality
- Category filtering
- Date filtering
- Responsive grid layout

#### CreateEvent.fxml
**Purpose**: Event creation form
**Structure**:
```xml
<ScrollPane fitToWidth="true" styleClass="create-event-container">
    <VBox spacing="30">
        <!-- Event Information -->
        <VBox spacing="15" styleClass="form-section">
            <Text text="Event Information" styleClass="section-title"/>
            <TextField fx:id="eventTitleField" promptText="Event Title"/>
            <TextArea fx:id="eventDescriptionField" promptText="Event Description"/>
            <HBox spacing="15">
                <DatePicker fx:id="startDatePicker" promptText="Start Date"/>
                <DatePicker fx:id="endDatePicker" promptText="End Date"/>
            </HBox>
        </VBox>

        <!-- Sessions Management -->
        <VBox spacing="15" styleClass="form-section">
            <Text text="Sessions" styleClass="section-title"/>
            <VBox fx:id="sessionsContainer" spacing="10">
                <!-- Session forms dynamically added here -->
            </VBox>
            <Button fx:id="addSessionButton" text="Add Session"/>
        </VBox>

        <!-- Submit Buttons -->
        <HBox spacing="15" styleClass="button-bar">
            <Button fx:id="saveDraftButton" text="Save as Draft"/>
            <Button fx:id="publishButton" text="Publish Event"/>
        </HBox>
    </VBox>
</ScrollPane>
```

**Features**:
- Multi-section layout
- Dynamic session management
- Date/time pickers
- Draft/publish options

### FXML Design Patterns

```xml
<!-- Controller Binding -->
fx:controller="com.eventra.controller.LoginController"

<!-- Component Referencing -->
<TextField fx:id="emailField"/>

<!-- Event Handling -->
<Button onAction="#handleSignIn" text="Sign In"/>

<!-- CSS Styling -->
styleClass="modern-input"
stylesheets="@../css/styles.css"

<!-- Layout Management -->
<VBox spacing="20" alignment="CENTER">
    <HBox spacing="15" HBox.hgrow="ALWAYS">
        <!-- Responsive components -->
    </HBox>
</VBox>
```

## 4. Integration and Data Flow

### User Registration Flow
```
SignUp.fxml → SignUpController → UserDAO + AttendeeDAO → Database
```

### User Login Flow
```
Login.fxml → LoginController → UserDAO → Database → SessionManager → Dashboard
```

### Event Creation Flow
```
CreateEvent.fxml → CreateEventController → EventSaveDAO → Database → Event Listing
```

### Key Integration Points

1. **Data Consistency**: DAOs ensure integrity across related tables
2. **Error Handling**: Controllers catch exceptions and show user-friendly messages
3. **Security**: Prepared statements, input validation, secure fields
4. **Performance**: Connection pooling, lazy loading, efficient layouts

## 5. Best Practices

### DAO Best Practices
- Use prepared statements for security
- Handle connections with try-with-resources
- Map results to objects for clean separation
- Implement proper error handling
- Use transactions for data consistency

### Controller Best Practices
- Keep controllers thin and focused
- Validate all user input
- Handle exceptions gracefully
- Use FXML injection effectively
- Implement responsive event handling

### FXML Best Practices
- Use CSS for styling separation
- Implement responsive design
- Use meaningful component IDs
- Organize layout logically
- Support accessibility features

## Summary

The Eventra application uses a well-structured MVC architecture:

- **DAOs**: Handle data persistence with security and performance
- **Controllers**: Manage UI logic and user interactions
- **FXML**: Define responsive and accessible user interfaces

This separation provides maintainability, testability, scalability, security, and excellent user experience.
