# Eventra Database - Entity Relationship Diagram (ERD)

## Database Overview
**Database Name:** EventPlannerDB  
**Purpose:** Event management system for planning, organizing, and managing events with user roles, registrations, and session management.

---

## Entity Relationship Diagram

```mermaid
erDiagram
    %% Core User Management
    UserM {
        int UserID PK
        nvarchar Username UK
        nvarchar FirstName
        nvarchar MiddleName
        nvarchar LastName
        nvarchar Email UK
        nvarchar PasswordHash
        nvarchar ProfilePicUrl
        tinyint RoleTypeID FK
        tinyint StatusTypeID FK
        tinyint PeriodCanLoginInMinutes
        datetime2 LastFailedLoginAt
        datetime2 CreatedAt
        datetime2 UpdatedAt
    }

    RoleType {
        tinyint RoleTypeID PK
        nvarchar Name UK
    }

    StatusType {
        tinyint StatusTypeID PK
        nvarchar Name UK
    }

    Permission {
        int PermissionID PK
        nvarchar Name UK
        nvarchar Description
    }

    RolePermission {
        tinyint RoleTypeID PK,FK
        int PermissionID PK,FK
    }

    %% Attendee Management
    Attendee {
        int AttendeeID PK
        nvarchar FirstName
        nvarchar MiddleName
        nvarchar LastName
        nvarchar Email UK
        nvarchar Organization
        nvarchar Phone
        nvarchar Location
        nvarchar Gender
        date DateOfBirth
        nvarchar ProfilePicUrl
        nvarchar Type
        nvarchar PasswordHash
        tinyint StatusTypeID FK
        tinyint PeriodCanLoginInMinutes
        datetime2 LastFailedLoginAt
        datetime2 CreatedAt
        datetime2 UpdatedAt
        int UserID FK
    }

    %% Event Management
    EventM {
        int EventID PK
        nvarchar Title
        nvarchar Description
        datetime2 StartDate
        datetime2 EndDate
        int VenueID FK
        int CreatedByUserID FK
        tinyint StatusTypeID FK
        datetime2 CreatedAt
        datetime2 UpdatedAt
    }

    EventStatusType {
        tinyint EventStatusTypeID PK
        nvarchar Name UK
    }

    %% Venue and Room Management
    Venue {
        int VenueID PK
        nvarchar Name
        nvarchar Address
        int Capacity
        int Rooms
        nvarchar MapLink
        datetime2 CreatedAt
        datetime2 UpdatedAt
    }

    Room {
        int RoomID PK
        int VenueID FK
        nvarchar Name
        int Capacity
        datetime2 CreatedAt
        datetime2 UpdatedAt
    }

    %% Registration System
    Registration {
        int RegistrationID PK
        int AttendeeID FK
        int EventID FK
        tinyint RegistrationStatusTypeID FK
        datetime2 CreatedAt
        datetime2 UpdatedAt
    }

    RegistrationStatusType {
        tinyint RegistrationStatusTypeID PK
        nvarchar Name UK
    }

    %% Session Management
    SessionM {
        int SessionID PK
        int EventID FK
        int CreatedByUserID FK
        nvarchar Track
        nvarchar Role
        nvarchar Title
        nvarchar Abstract
        datetime2 StartAt
        datetime2 EndAt
        int RoomID FK
        tinyint StatusTypeID FK
        datetime2 CreatedAt
        datetime2 UpdatedAt
    }

    SessionStatusType {
        tinyint SessionStatusTypeID PK
        nvarchar Name UK
    }

    %% Presenter Management
    Presenter {
        int PresenterID PK
        nvarchar FirstName
        nvarchar MiddleName
        nvarchar LastName
        nvarchar Bio
        nvarchar Email UK
        nvarchar PhotoUrl
        nvarchar ContactInfo
        int CreatedByUserID FK
        datetime2 CreatedAt
        datetime2 UpdatedAt
    }

    SessionPresenter {
        int SessionID PK,FK
        int PresenterID PK,FK
    }

    %% Relationships
    UserM ||--o{ Attendee : "has"
    UserM ||--o{ EventM : "creates"
    UserM ||--o{ SessionM : "creates"
    UserM ||--o{ Presenter : "creates"
    
    RoleType ||--o{ UserM : "has"
    StatusType ||--o{ UserM : "has"
    StatusType ||--o{ Attendee : "has"
    
    RoleType ||--o{ RolePermission : "has"
    Permission ||--o{ RolePermission : "assigned_to"
    
    EventStatusType ||--o{ EventM : "has"
    Venue ||--o{ EventM : "hosts"
    Venue ||--o{ Room : "contains"
    
    EventM ||--o{ SessionM : "contains"
    Room ||--o{ SessionM : "hosts"
    SessionStatusType ||--o{ SessionM : "has"
    
    SessionM ||--o{ SessionPresenter : "has"
    Presenter ||--o{ SessionPresenter : "presents"
    
    Attendee ||--o{ Registration : "registers"
    EventM ||--o{ Registration : "receives"
    RegistrationStatusType ||--o{ Registration : "has"

```

---

## Table Descriptions

### Core User Management
- **UserM**: Main user table storing all system users (admins, organizers, attendees)
- **RoleType**: Defines user roles (SuperAdmin, Admin, Staff, Attendee)
- **StatusType**: User status (Active, Inactive, Suspended, etc.)
- **Permission**: System permissions for role-based access control
- **RolePermission**: Many-to-many relationship between roles and permissions

### Attendee Management
- **Attendee**: Extended user information for event attendees
- Links to UserM through UserID foreign key

### Event Management
- **EventM**: Main events table with all event details
- **EventStatusType**: Event status (Draft, Published, Cancelled, etc.)
- **Venue**: Event venues with capacity and location information
- **Room**: Individual rooms within venues

### Registration System
- **Registration**: Links attendees to events they've registered for
- **RegistrationStatusType**: Registration status (Pending, Confirmed, Cancelled, etc.)

### Session Management
- **SessionM**: Individual sessions within events
- **SessionStatusType**: Session status (Scheduled, In Progress, Completed, etc.)
- **SessionPresenter**: Many-to-many relationship between sessions and presenters

### Presenter Management
- **Presenter**: Information about event presenters/speakers
- **SessionPresenter**: Links presenters to specific sessions

---

## Key Relationships

### One-to-Many Relationships
- **UserM → Attendee**: One user can have one attendee record
- **UserM → EventM**: One user can create many events
- **UserM → SessionM**: One user can create many sessions
- **UserM → Presenter**: One user can create many presenters
- **Venue → Room**: One venue can have many rooms
- **EventM → SessionM**: One event can have many sessions
- **EventM → Registration**: One event can have many registrations

### Many-to-Many Relationships
- **SessionM ↔ Presenter**: Through SessionPresenter junction table
- **RoleType ↔ Permission**: Through RolePermission junction table

### Foreign Key Constraints
- All relationships maintain referential integrity
- Cascade delete/update rules applied where appropriate
- Check constraints ensure data validity (e.g., EndDate >= StartDate)

---

## Data Flow

1. **User Registration**: Users are created in UserM, then attendee records are created in Attendee
2. **Event Creation**: Users with appropriate roles can create events in EventM
3. **Session Planning**: Events can have multiple sessions with presenters
4. **Registration**: Attendees can register for events through the Registration table
5. **Role-Based Access**: Permissions are managed through RoleType and Permission tables

---

## Database Constraints

### Primary Keys
- All tables have auto-incrementing primary keys
- Composite primary keys for junction tables

### Unique Constraints
- Username and Email in UserM
- Email in Attendee and Presenter
- Name fields in type tables

### Check Constraints
- EventM: EndDate >= StartDate
- SessionM: EndAt > StartAt

### Foreign Key Constraints
- All relationships properly defined with appropriate cascade rules
- Referential integrity maintained across all tables 