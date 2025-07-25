//#!/usr/bin/env kotlin
//
//-- 1. Create the database if it doesn’t already exist
//        USE EventPlannerDB;
//GO
//
//
//-- 3. Lookup tables
//        CREATE TABLE StatusType (
//    StatusTypeID    TINYINT       PRIMARY KEY,
//    Name            NVARCHAR(20)  NOT NULL UNIQUE
//);
//
//CREATE TABLE RoleType (
//    RoleTypeID      TINYINT       PRIMARY KEY,
//    Name            NVARCHAR(20)  NOT NULL UNIQUE
//);
//
//CREATE TABLE EventStatusType (
//    EventStatusTypeID TINYINT     PRIMARY KEY,
//    Name              NVARCHAR(20) NOT NULL UNIQUE
//);
//
//CREATE TABLE SessionStatusType (
//    SessionStatusTypeID TINYINT   PRIMARY KEY,
//    Name                NVARCHAR(20) NOT NULL UNIQUE
//);
//
//CREATE TABLE RegistrationStatusType (
//    RegistrationStatusTypeID TINYINT PRIMARY KEY,
//    Name                     NVARCHAR(20) NOT NULL UNIQUE
//);
//
//
//-- 4. Core entities
//
//        CREATE TABLE Attendee (
//    AttendeeID               INT           IDENTITY(1,1) PRIMARY KEY,
//    FirstName                NVARCHAR(100) NOT NULL,
//    MiddleName               NVARCHAR(100),
//    LastName                 NVARCHAR(100) NOT NULL,
//    Email                    NVARCHAR(255) NOT NULL UNIQUE,
//    Organization             NVARCHAR(255),
//    Phone                    NVARCHAR(50),
//    Location                 NVARCHAR(255),
//    Gender                   NVARCHAR(20),
//    DateOfBirth              DATE,
//    ProfilePicUrl            NVARCHAR(500),
//    Type                     NVARCHAR(50)  NOT NULL,
//    PasswordHash             NVARCHAR(255) NOT NULL,
//    StatusTypeID             TINYINT       NOT NULL DEFAULT 1,
//    PeriodCanLoginInMinutes  TINYINT       NOT NULL DEFAULT 0,
//    LastFailedLoginAt        DATETIME2,
//    CreatedAt                DATETIME2     NOT NULL DEFAULT SYSUTCDATETIME(),
//    UpdatedAt                DATETIME2     NOT NULL DEFAULT SYSUTCDATETIME(),
//    CONSTRAINT FK_Attendee_StatusType
//            FOREIGN KEY (StatusTypeID)
//            REFERENCES StatusType(StatusTypeID)
//            ON DELETE NO ACTION
//            ON UPDATE CASCADE
//);
//
//CREATE TABLE UserM (
//    UserID                   INT           IDENTITY(1,1) PRIMARY KEY,
//    Username                 NVARCHAR(100) NOT NULL UNIQUE,
//    FirstName                NVARCHAR(100) NOT NULL,
//    MiddleName               NVARCHAR(100),
//    LastName                 NVARCHAR(100) NOT NULL,
//    Email                    NVARCHAR(255) NOT NULL UNIQUE,
//    PasswordHash             NVARCHAR(255) NOT NULL,
//    ProfilePicUrl            NVARCHAR(500),
//    RoleTypeID               TINYINT       NOT NULL DEFAULT 2,
//    StatusTypeID             TINYINT       NOT NULL DEFAULT 1,
//    PeriodCanLoginInMinutes  TINYINT       NOT NULL DEFAULT 0,
//    LastFailedLoginAt        DATETIME2,
//    CreatedAt                DATETIME2     NOT NULL DEFAULT SYSUTCDATETIME(),
//    UpdatedAt                DATETIME2     NOT NULL DEFAULT SYSUTCDATETIME(),
//    CONSTRAINT FK_UserM_RoleType
//            FOREIGN KEY (RoleTypeID)
//            REFERENCES RoleType(RoleTypeID)
//            ON DELETE NO ACTION
//            ON UPDATE CASCADE,
//    CONSTRAINT FK_UserM_StatusType
//            FOREIGN KEY (StatusTypeID)
//            REFERENCES StatusType(StatusTypeID)
//            ON DELETE NO ACTION
//            ON UPDATE CASCADE
//);
//
//CREATE TABLE Venue (
//    VenueID   INT           IDENTITY(1,1) PRIMARY KEY,
//    Name      NVARCHAR(255) NOT NULL,
//    Address   NVARCHAR(500),
//    Capacity  INT,
//    Rooms     INT,
//    MapLink   NVARCHAR(500),
//    CreatedAt DATETIME2     NOT NULL DEFAULT SYSUTCDATETIME(),
//    UpdatedAt DATETIME2     NOT NULL DEFAULT SYSUTCDATETIME()
//);
//
//CREATE TABLE Room (
//    RoomID    INT           IDENTITY(1,1) PRIMARY KEY,
//    VenueID   INT           NOT NULL,
//    Name      NVARCHAR(100) NOT NULL,
//    Capacity  INT,
//    CreatedAt DATETIME2     NOT NULL DEFAULT SYSUTCDATETIME(),
//    UpdatedAt DATETIME2     NOT NULL DEFAULT SYSUTCDATETIME(),
//    CONSTRAINT FK_Room_Venue
//            FOREIGN KEY (VenueID)
//            REFERENCES Venue(VenueID)
//            ON DELETE CASCADE
//            ON UPDATE NO ACTION
//);
//
//CREATE TABLE EventM (
//    EventID         INT           IDENTITY(1,1) PRIMARY KEY,
//    Title           NVARCHAR(255) NOT NULL,
//    Description     NVARCHAR(MAX),
//    StartDate       DATETIME2     NOT NULL,
//    EndDate         DATETIME2     NOT NULL,
//    VenueID         INT           NOT NULL,
//    CreatedByUserID INT           NOT NULL,
//    StatusTypeID    TINYINT       NOT NULL DEFAULT 1,
//    CreatedAt       DATETIME2     NOT NULL DEFAULT SYSUTCDATETIME(),
//    UpdatedAt       DATETIME2     NOT NULL DEFAULT SYSUTCDATETIME(),
//    CONSTRAINT CK_EventM_Dates
//            CHECK (EndDate >= StartDate),
//    CONSTRAINT FK_EventM_Venue
//            FOREIGN KEY (VenueID)
//            REFERENCES Venue(VenueID)
//            ON DELETE NO ACTION
//            ON UPDATE NO ACTION,
//    CONSTRAINT FK_EventM_CreatedBy
//            FOREIGN KEY (CreatedByUserID)
//            REFERENCES UserM(UserID)
//            ON DELETE NO ACTION
//            ON UPDATE NO ACTION,
//    CONSTRAINT FK_EventM_StatusType
//            FOREIGN KEY (StatusTypeID)
//            REFERENCES EventStatusType(EventStatusTypeID)
//            ON DELETE NO ACTION
//            ON UPDATE CASCADE
//);
//
//CREATE TABLE Presenter (
//    PresenterID     INT           IDENTITY(1,1) PRIMARY KEY,
//    FirstName       NVARCHAR(100) NOT NULL,
//    MiddleName      NVARCHAR(100),
//    LastName        NVARCHAR(100) NOT NULL,
//    Bio             NVARCHAR(MAX),
//    Email           NVARCHAR(255) UNIQUE,
//    PhotoUrl        NVARCHAR(500),
//    ContactInfo     NVARCHAR(255),
//    CreatedByUserID INT           NOT NULL,
//    CreatedAt       DATETIME2     NOT NULL DEFAULT SYSUTCDATETIME(),
//    UpdatedAt       DATETIME2     NOT NULL DEFAULT SYSUTCDATETIME(),
//    CONSTRAINT FK_Presenter_CreatedBy
//            FOREIGN KEY (CreatedByUserID)
//            REFERENCES UserM(UserID)
//            ON DELETE NO ACTION
//            ON UPDATE NO ACTION
//);
//
//CREATE TABLE SessionM (
//    SessionID        INT           IDENTITY(1,1) PRIMARY KEY,
//    EventID          INT           NOT NULL,
//    CreatedByUserID  INT           NOT NULL,
//    Track            NVARCHAR(100) NOT NULL,
//    Role             NVARCHAR(50),
//    Title            NVARCHAR(255) NOT NULL,
//    Abstract         NVARCHAR(MAX),
//    StartAt          DATETIME2     NOT NULL,
//    EndAt            DATETIME2     NOT NULL,
//    RoomID           INT,
//    StatusTypeID     TINYINT       NOT NULL DEFAULT 1,
//    CreatedAt        DATETIME2     NOT NULL DEFAULT SYSUTCDATETIME(),
//    UpdatedAt        DATETIME2     NOT NULL DEFAULT SYSUTCDATETIME(),
//    CONSTRAINT CK_SessionM_Times
//            CHECK (EndAt > StartAt),
//    CONSTRAINT FK_SessionM_Event
//            FOREIGN KEY (EventID)
//            REFERENCES EventM(EventID)
//            ON DELETE CASCADE
//            ON UPDATE NO ACTION,
//    CONSTRAINT FK_SessionM_CreatedBy
//            FOREIGN KEY (CreatedByUserID)
//            REFERENCES UserM(UserID)
//            ON DELETE NO ACTION
//            ON UPDATE NO ACTION,
//    CONSTRAINT FK_SessionM_Room
//            FOREIGN KEY (RoomID)
//            REFERENCES Room(RoomID)
//            ON DELETE SET NULL
//            ON UPDATE NO ACTION,
//    CONSTRAINT FK_SessionM_StatusType
//            FOREIGN KEY (StatusTypeID)
//            REFERENCES SessionStatusType(SessionStatusTypeID)
//            ON DELETE NO ACTION
//            ON UPDATE CASCADE
//);
//
//CREATE TABLE SessionPresenter (
//    SessionID   INT NOT NULL,
//    PresenterID INT NOT NULL,
//    PRIMARY KEY (SessionID, PresenterID),
//CONSTRAINT FK_SP_Session
//        FOREIGN KEY (SessionID)
//REFERENCES SessionM(SessionID)
//ON DELETE CASCADE
//ON UPDATE NO ACTION,
//CONSTRAINT FK_SP_Presenter
//        FOREIGN KEY (PresenterID)
//REFERENCES Presenter(PresenterID)
//ON DELETE CASCADE
//ON UPDATE NO ACTION
//);
//
//CREATE TABLE Registration (
//    RegistrationID            INT           IDENTITY(1,1) PRIMARY KEY,
//    AttendeeID                INT           NOT NULL,
//    EventID                   INT           NOT NULL,
//    RegistrationStatusTypeID  TINYINT       NOT NULL DEFAULT 1,
//    CreatedAt                 DATETIME2     NOT NULL DEFAULT SYSUTCDATETIME(),
//    UpdatedAt                 DATETIME2     NOT NULL DEFAULT SYSUTCDATETIME(),
//    CONSTRAINT FK_Reg_Attendee
//            FOREIGN KEY (AttendeeID)
//            REFERENCES Attendee(AttendeeID)
//            ON DELETE CASCADE
//            ON UPDATE NO ACTION,
//    CONSTRAINT FK_Reg_Event
//            FOREIGN KEY (EventID)
//            REFERENCES EventM(EventID)
//            ON DELETE CASCADE
//            ON UPDATE NO ACTION,
//    CONSTRAINT FK_Reg_StatusType
//            FOREIGN KEY (RegistrationStatusTypeID)
//            REFERENCES RegistrationStatusType(RegistrationStatusTypeID)
//            ON DELETE NO ACTION
//            ON UPDATE CASCADE
//);
//
//CREATE TABLE Permission (
//    PermissionID  INT           IDENTITY(1,1) PRIMARY KEY,
//    Name          NVARCHAR(100) NOT NULL UNIQUE,
//    Description   NVARCHAR(255)
//);
//
//CREATE TABLE RolePermission (
//    RoleTypeID    TINYINT       NOT NULL,
//    PermissionID  INT           NOT NULL,
//    PRIMARY KEY (RoleTypeID, PermissionID),
//CONSTRAINT FK_RP_RoleType
//        FOREIGN KEY (RoleTypeID)
//REFERENCES RoleType(RoleTypeID)
//ON DELETE CASCADE
//ON UPDATE CASCADE,
//CONSTRAINT FK_RP_Permission
//        FOREIGN KEY (PermissionID)
//REFERENCES Permission(PermissionID)
//ON DELETE CASCADE
//ON UPDATE CASCADE
//);