# Eventra - Complete Project Documentation
## Comprehensive Analysis & Technical Specifications

---

## Table of Contents
1. [Project Overview](#project-overview)
2. [Technology Stack & Architecture](#technology-stack--architecture)
3. [Database Design & Schema](#database-design--schema)
4. [User Interface & Experience](#user-interface--experience)
5. [User Role System](#user-role-system)
6. [Core Functionality](#core-functionality)
7. [Security Features](#security-features)
8. [Development & Deployment](#development--deployment)
9. [Code Quality & Architecture](#code-quality--architecture)
10. [Testing & Quality Assurance](#testing--quality-assurance)
11. [Documentation & Support](#documentation--support)
12. [Performance & Scalability](#performance--scalability)
13. [Azure SQL Database Integration](#azure-sql-database-integration)
14. [Future Enhancement Potential](#future-enhancement-potential)
15. [Project Structure & Files](#project-structure--files)
16. [Technical Implementation Details](#technical-implementation-details)

---

## Project Overview

Eventra is a comprehensive **JavaFX-based event management application** designed for planning, organizing, and managing events with sophisticated user role management, event registration, and session coordination. It's built using modern Java technologies and follows a clean architecture pattern.

### Key Features
- **Multi-role user management** (SuperAdmin, Admin, Staff, Attendee)
- **Comprehensive event management** with venue and room planning
- **Session and presenter management** for complex event structures
- **Advanced registration system** with status tracking
- **Role-based access control** with granular permissions
- **Modern JavaFX UI** with responsive design
- **Azure SQL Database integration** for production deployment

---

## Technology Stack & Architecture

### Core Technologies
- **Java 21** - Latest LTS version for optimal performance
- **JavaFX 21.0.2** - Modern UI framework for desktop applications
- **Maven** - Dependency management and build automation
- **HikariCP 5.0.1** - High-performance database connection pooling
- **BCrypt** - Secure password hashing with salt

### Database Support
- **Azure SQL Database** - Production database (Microsoft SQL Server)
- **H2 Database** - Embedded development database
- **SQLite** - Lightweight alternative database option

### Architecture Pattern
- **MVC (Model-View-Controller)** architecture
- **DAO (Data Access Object)** pattern for database operations
- **Session Management** for user authentication
- **Role-Based Access Control (RBAC)** system

---

## Database Design & Schema

### Core Entities
The system uses a sophisticated relational database with the following main entities:

#### 1. User Management
- **UserM** - Main user table with authentication and role information
- **RoleType** - User roles (SuperAdmin, Admin, Staff, Attendee)
- **StatusType** - User account status management
- **Permission** & **RolePermission** - Granular access control

#### 2. Event Management
- **EventM** - Core event information and metadata
- **EventStatusType** - Event lifecycle management
- **Venue** & **Room** - Physical location management

#### 3. Session & Presenter Management
- **SessionM** - Individual sessions within events
- **Presenter** - Speaker and presenter information
- **SessionPresenter** - Many-to-many session-presenter relationships

#### 4. Registration System
- **Registration** - Event attendance tracking
- **RegistrationStatusType** - Registration state management
- **Attendee** - Extended user information for event participants

### Database Views
The system includes **20+ comprehensive database views** for:
- User management and permissions
- Event statistics and reporting
- Session and presenter information
- Registration analytics
- Dashboard metrics
- Search and filtering operations

---

## User Interface & Experience

### FXML-Based UI Design
- **24 FXML screens** covering all application functionality
- **Responsive layouts** with modern design principles
- **Consistent styling** using comprehensive CSS framework
- **Accessibility features** with proper labeling and navigation

### Key Screens
1. **Welcome** - Application entry point
2. **Login/SignUp** - User authentication
3. **Role-Specific Dashboards** - Tailored user experiences
4. **Event Management** - Creation, editing, and management
5. **Profile Management** - User information and settings
6. **Registration System** - Event attendance management

### Visual Design System
- **Color Scheme**: Purple-based theme (#4b3a8c, #9d7abf)
- **Typography**: Segoe UI with consistent sizing hierarchy
- **Components**: Modern cards, buttons, and form elements
- **Animations**: Hover effects, transitions, and micro-interactions
- **Responsive Design**: Adapts to different screen sizes

---

## User Role System

### Four-Tier Role Hierarchy
1. **SuperAdmin** - System-wide administration and user management
2. **Admin** - Event and venue management
3. **Staff** - Event operations and attendee management
4. **Attendee** - Event registration and participation

### Permission-Based Access Control
- **Granular permissions** for each role
- **Dynamic UI adaptation** based on user permissions
- **Secure navigation** between role-appropriate screens
- **Audit trails** for user actions

---

## Core Functionality

### Event Management
- **Event Creation**: Comprehensive event setup with venue, dates, and details
- **Event Publishing**: Workflow from draft to published status
- **Venue Management**: Capacity planning and room allocation
- **Session Planning**: Detailed session scheduling with presenters

### Registration System
- **Attendee Registration**: Event sign-up and management
- **Status Tracking**: Pending, confirmed, and cancelled registrations
- **Capacity Management**: Venue and room capacity constraints
- **Registration Analytics**: Reporting and statistics

### User Management
- **Profile Management**: User information and picture management
- **Authentication**: Secure login with password hashing
- **Session Management**: Persistent user sessions
- **Password Security**: BCrypt hashing with salt

---

## Security Features

### Authentication & Authorization
- **BCrypt password hashing** with salt generation
- **Session-based authentication** with timeout management
- **Role-based access control** for all system features
- **Input validation** and SQL injection prevention

### Data Protection
- **Connection pooling** with HikariCP for performance
- **Prepared statements** for all database queries
- **Transaction management** for data integrity
- **Audit logging** for user actions

---

## Development & Deployment

### Build System
- **Maven-based** project structure
- **JavaFX Maven Plugin** for application packaging
- **Multiple build scripts** for different deployment scenarios
- **Dependency management** with version control

### Development Tools
- **Multiple batch files** for different Java versions
- **Database connection testing** utilities
- **FXML screenshot generation** tools for documentation
- **Hot-reload capabilities** for development

### Deployment Options
- **Standalone JAR** with embedded JavaFX
- **Custom launcher** with jlink optimization
- **Multiple Java version support** (Java 11, 21)
- **Cross-platform compatibility** (Windows focus with PowerShell scripts)

---

## Code Quality & Architecture

### Design Patterns
- **MVC Architecture**: Clear separation of concerns
- **DAO Pattern**: Abstracted data access layer
- **Factory Pattern**: Object creation management
- **Observer Pattern**: Event-driven UI updates

### Code Organization
- **Package structure**: Logical grouping by functionality
- **Controller classes**: One per FXML screen
- **Model classes**: Clean data objects with validation
- **Utility classes**: Reusable helper functions

### Error Handling
- **Comprehensive exception handling** throughout the stack
- **User-friendly error messages** with fallback options
- **Logging and debugging** capabilities
- **Graceful degradation** when services are unavailable

---

## Testing & Quality Assurance

### Testing Infrastructure
- **Unit test framework** with JUnit
- **Database connection testing** utilities
- **UI screenshot generation** for visual testing
- **Integration testing** capabilities

### Quality Measures
- **Code documentation** with comprehensive comments
- **Consistent coding standards** across the project
- **Error logging** and debugging information
- **Performance optimization** with connection pooling

---

## Documentation & Support

### Comprehensive Documentation
- **Database ERD** with detailed relationship diagrams
- **Feature documentation** for each major component
- **Navigation guides** for different user roles
- **Technical specifications** and implementation details

### User Support
- **Role-specific navigation guides**
- **Feature documentation** for administrators
- **Screenshot documentation** of all UI screens
- **Database view explanations** for reporting

---

## Performance & Scalability

### Optimization Features
- **Database connection pooling** for efficient resource usage
- **Prepared statement caching** for query optimization
- **Efficient data loading** with pagination support
- **Memory management** with proper resource cleanup

### Scalability Considerations
- **Modular architecture** for easy feature additions
- **Database view optimization** for complex queries
- **Connection pool configuration** for load handling
- **Efficient data structures** for large datasets

---

## Azure SQL Database Integration

### Database Configuration
The project includes Microsoft SQL Server JDBC driver for Azure SQL Database integration:
```xml
<dependency>
    <groupId>com.microsoft.sqlserver</groupId>
    <artifactId>mssql-jdbc</artifactId>
    <version>10.2.0.jre11</version>
</dependency>
```

### Azure-Specific Features
- **Fully managed service** - No server maintenance required
- **Built-in security** - Azure Active Directory integration
- **Automatic backups** and point-in-time restore
- **Geo-replication** capabilities
- **Scalability** - Easy to scale up/down based on demand

### Security Enhancements
- **BCrypt password hashing** for secure authentication
- **Connection pooling** with HikariCP for performance
- **Prepared statements** to prevent SQL injection
- **Role-based access control** for user permissions

### Azure SQL Database Benefits
- **Query optimization** with Azure's intelligent query processing
- **Automatic tuning** recommendations
- **Built-in monitoring** and performance insights
- **Connection pooling** with HikariCP implementation

---

## Future Enhancement Potential

### Technical Improvements
- **Cloud deployment** capabilities
- **Mobile application** development
- **API development** for third-party integrations
- **Real-time notifications** and updates

### Feature Additions
- **Advanced reporting** and analytics
- **Multi-language support** for international events
- **Payment processing** integration
- **Social media** integration and sharing

---

## Project Structure & Files

### Directory Organization
```
Eventra/
├── src/
│   ├── main/
│   │   ├── java/com/eventra/
│   │   │   ├── controller/     # 24 controller classes
│   │   │   ├── dao/           # Data access objects
│   │   │   ├── model/         # Data models
│   │   │   └── util/          # Utility classes
│   │   ├── resources/
│   │   │   ├── fxml/          # 24 FXML UI files
│   │   │   ├── css/           # Comprehensive styling
│   │   │   └── images/        # UI assets and logos
│   │   └── config.properties  # Database configuration
├── pom.xml                    # Maven configuration
├── database_views.sql         # 20+ database views
├── Eventra_ERD.md            # Database schema documentation
└── Multiple build scripts     # Deployment automation
```

### Key Files
- **24 FXML screens** for complete UI coverage
- **24 Java controllers** for business logic
- **Comprehensive CSS** with 1800+ lines of styling
- **Database views** for optimized queries
- **Build automation** scripts for different environments

---

## Technical Implementation Details

### Database Connection Management
```java
// HikariCP configuration for Azure SQL Database
HikariConfig cfg = new HikariConfig();
cfg.setJdbcUrl(p.getProperty("db.url"));
cfg.setConnectionTimeout(60000);        // 60 seconds
cfg.setMaximumPoolSize(5);
cfg.setMinimumIdle(1);
cfg.setIdleTimeout(300000);            // 5 minutes
cfg.setMaxLifetime(1800000);           // 30 minutes
```

### Password Security
```java
// BCrypt hashing with salt
public static String hashPassword(String password) {
    return BCrypt.hashpw(password, BCrypt.gensalt());
}

// Secure password verification
boolean passwordMatches = BCrypt.checkpw(password, storedHash);
```

### Role-Based Navigation
```java
// Dynamic navigation based on user role
if (user.getRoleTypeId() == 1) {
    ViewUtil.switchTo("SuperAdminDashboard", window);
} else if (user.getRoleTypeId() == 2) {
    ViewUtil.switchTo("AdminDashboard", window);
} else if (user.getRoleTypeId() == 4) {
    ViewUtil.switchTo("AttendeeEvents", window);
}
```

### Session Management
```java
// User session management
SessionManager.setCurrentUser(user);
User currentUser = SessionManager.getCurrentUser();
```

---

## Conclusion

Eventra represents a **production-ready, enterprise-grade event management system** with sophisticated architecture, comprehensive functionality, and excellent user experience design. The integration with Azure SQL Database makes it a cloud-ready application with all the benefits of Microsoft's managed database service.

### Key Strengths
- **Modern Java 21** technology stack
- **Professional-grade architecture** with clean separation of concerns
- **Comprehensive security** with BCrypt and role-based access control
- **Scalable database design** with optimized views and queries
- **Professional UI/UX** with modern JavaFX design
- **Azure cloud integration** for enterprise deployment
- **Extensive documentation** and testing infrastructure

### Business Value
- **Reduced operational overhead** through automated event management
- **Improved user experience** with role-specific interfaces
- **Enhanced security** through comprehensive access control
- **Scalable architecture** for growing event management needs
- **Cloud-ready deployment** with Azure integration

This project demonstrates **enterprise-level software development practices** and provides a solid foundation for professional event management applications in both corporate and educational environments.

---

*Document generated from comprehensive analysis of the Eventra project codebase and documentation.*
