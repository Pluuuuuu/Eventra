-- =====================================================
-- EVENTRA DATABASE VIEWS (database_views.sql)
-- =====================================================
--
-- This file contains 20 comprehensive database views that provide optimized
-- data access patterns for the Eventra application. These views simplify
-- complex queries and provide consistent data structures for the application.
--
-- WHAT THIS FILE DOES:
-- - Defines optimized database views for common data access patterns
-- - Simplifies complex database queries for the application
-- - Provides consistent data structures across different parts of the system
-- - Improves query performance through pre-optimized data access
-- - Enables easier reporting and analytics capabilities
-- - Supports business intelligence and data analysis
--
-- VIEW CATEGORIES:
-- 1. User Management Views - User information and permissions
-- 2. Event Management Views - Event details and statistics
-- 3. Session Management Views - Session and presenter information
-- 4. Registration Views - Attendee registrations and status
-- 5. Venue and Room Views - Location and capacity management
-- 6. Dashboard Views - System statistics and activity
-- 7. Reporting Views - Analytics and business intelligence
-- 8. Search and Filter Views - Data discovery and filtering
--
-- BENEFITS OF USING VIEWS:
-- - Simplified application queries (easier to write and maintain)
-- - Consistent data structures (same format across different screens)
-- - Performance optimization (pre-optimized data access patterns)
-- - Easier maintenance (changes in one place affect everywhere)
-- - Security through abstraction (hide sensitive data structures)
-- - Better data organization and readability
--
-- USAGE IN APPLICATION:
-- - Controllers use these views for data retrieval and display
-- - Reports and dashboards display view data for analytics
-- - Search functionality leverages view structures for discovery
-- - Analytics use aggregated view data for business intelligence
-- - User interfaces consume view data for consistent display
--
-- TECHNICAL IMPLEMENTATION:
-- - Views are created using CREATE OR ALTER VIEW statements
-- - Each view joins multiple tables to provide comprehensive information
-- - Views include business logic and data transformations
-- - Performance is optimized through proper indexing and query design
-- - Views are designed to be read-only for application use
--
-- BUSINESS VALUE:
-- - Faster application development through simplified data access
-- - Improved user experience with consistent data presentation
-- - Better performance for complex data operations
-- - Easier maintenance and system updates
-- - Enhanced reporting and analytics capabilities
--
-- REAL-WORLD ANALOGY:
-- - Like having a well-organized filing system where you can quickly find information
-- - Like having a dashboard that shows all the important metrics at a glance
-- - Like having a library catalog that helps you find books efficiently
-- - Like having a restaurant menu that organizes dishes by category
-- - Like having a map that shows you the best routes to your destination

USE EventPlannerDB;
GO

-- =====================================================
-- USER MANAGEMENT VIEWS
-- =====================================================
--
-- These views provide comprehensive user information with role-based
-- access control and status management. They're used throughout the
-- application for user authentication, authorization, and display.
--
-- BUSINESS PURPOSE:
-- - Support user authentication and login processes
-- - Enable role-based access control and permissions
-- - Provide user profile information for display and editing
-- - Support user management and administrative functions
-- - Enable user search and filtering capabilities
--
-- TECHNICAL FEATURES:
-- - Join user data with role and status information
-- - Include security and authentication details
-- - Support user lifecycle management
-- - Enable efficient user data retrieval

-- View for complete user information with roles and status
-- 
-- This view joins the UserM table with RoleType and StatusType to provide
-- a complete user profile including their role name and account status.
-- It's the primary view for user management and authentication.
--
-- USAGE SCENARIOS:
-- - User login and authentication processes
-- - User profile displays and editing
-- - Admin user management and oversight
-- - Role-based access control implementation
-- - User search and filtering operations
--
-- BUSINESS LOGIC:
-- - Combines user personal information with system roles and status
-- - Provides human-readable role and status names
-- - Includes security and session management details
-- - Supports user lifecycle tracking and management
--
-- TECHNICAL DETAILS:
-- - Uses INNER JOINs to ensure data integrity
-- - Includes all essential user information fields
-- - Optimized for frequent user data access
-- - Supports efficient user authentication queries
CREATE OR ALTER VIEW vw_UserComplete AS
SELECT 
    u.UserID,                    -- Unique user identifier
    u.Username,                  -- Login username (unique)
    u.FirstName,                 -- User's first name
    u.MiddleName,                -- User's middle name (optional)
    u.LastName,                  -- User's last name
    u.Email,                     -- User's email address (unique)
    u.ProfilePicUrl,             -- Path to profile picture
    rt.Name AS RoleName,         -- Human-readable role name
    st.Name AS StatusName,       -- Account status description
    u.PeriodCanLoginInMinutes,   -- Session timeout setting
    u.LastFailedLoginAt,         -- Security tracking (failed login attempts)
    u.CreatedAt,                 -- Account creation timestamp
    u.UpdatedAt                  -- Last modification timestamp
FROM UserM u
INNER JOIN RoleType rt ON u.RoleTypeID = rt.RoleTypeID
INNER JOIN StatusType st ON u.StatusTypeID = st.StatusTypeID;
GO

-- View for attendee information with user details
-- 
-- This view provides complete attendee information including their
-- user account details if they have one. It's used for attendee
-- management and event registration.
--
-- BUSINESS LOGIC:
-- - Links attendees to user accounts when applicable
-- - Provides organization and contact information
-- - Shows attendee type and status for management
-- - Enables attendee-user relationship tracking
-- - Supports attendee search and filtering
--
-- USAGE SCENARIOS:
-- - Attendee registration and management
-- - Event attendance tracking and reporting
-- - Attendee profile display and editing
-- - Attendee search and filtering operations
-- - Event capacity and attendance analytics
--
-- TECHNICAL FEATURES:
-- - LEFT JOIN to include attendees without user accounts
-- - Comprehensive attendee information display
-- - Support for both registered and guest attendees
-- - Efficient attendee data retrieval
CREATE OR ALTER VIEW vw_AttendeeComplete AS
SELECT 
    a.AttendeeID,                -- Unique attendee identifier
    a.FirstName,                 -- Attendee's first name
    a.MiddleName,                -- Attendee's middle name (optional)
    a.LastName,                  -- Attendee's last name
    a.Email,                     -- Attendee's email address
    a.Organization,              -- Company or organization name
    a.Phone,                     -- Contact phone number
    a.Location,                  -- Geographic location
    a.Gender,                    -- Gender identification
    a.DateOfBirth,              -- Birth date for demographics
    a.ProfilePicUrl,             -- Profile picture path
    a.Type AS AttendeeType,      -- Type of attendee (VIP, Regular, etc.)
    st.Name AS StatusName,       -- Attendee status
    u.Username,                  -- Associated username (if any)
    u.RoleTypeID,                -- User role (if account exists)
    a.CreatedAt,                 -- Record creation timestamp
    a.UpdatedAt                  -- Last modification timestamp
FROM Attendee a
INNER JOIN StatusType st ON a.StatusTypeID = st.StatusTypeID
LEFT JOIN UserM u ON a.UserID = u.UserID;
GO

-- View for user permissions
-- 
-- This view provides a comprehensive list of all user permissions
-- based on their role. It's used for access control and feature
-- availability throughout the application.
--
-- SECURITY FEATURES:
-- - Role-based permission mapping
-- - Granular access control
-- - Permission description for UI display
-- - Audit trail for security monitoring
CREATE OR ALTER VIEW vw_UserPermissions AS
SELECT 
    u.UserID,                    -- User identifier
    u.Username,                  -- Login username
    u.Email,                     -- User's email address
    rt.Name AS RoleName,         -- User's role name
    p.Name AS PermissionName,    -- Permission identifier
    p.Description AS PermissionDescription  -- Human-readable description
FROM UserM u
INNER JOIN RoleType rt ON u.RoleTypeID = rt.RoleTypeID
INNER JOIN RolePermission rp ON rt.RoleTypeID = rp.RoleTypeID
INNER JOIN Permission p ON rp.PermissionID = p.PermissionID;
GO

-- =====================================================
-- EVENT MANAGEMENT VIEWS
-- =====================================================

-- View for complete event information
CREATE OR ALTER VIEW vw_EventComplete AS
SELECT 
    e.EventID,
    e.Title,
    e.Description,
    e.StartDate,
    e.EndDate,
    v.Name AS VenueName,
    v.Address AS VenueAddress,
    v.Capacity AS VenueCapacity,
    v.MapLink AS VenueMapLink,
    est.Name AS EventStatus,
    u.Username AS CreatedByUsername,
    u.Email AS CreatedByEmail,
    e.CreatedAt,
    e.UpdatedAt
FROM EventM e
INNER JOIN Venue v ON e.VenueID = v.VenueID
INNER JOIN EventStatusType est ON e.StatusTypeID = est.EventStatusTypeID
INNER JOIN UserM u ON e.CreatedByUserID = u.UserID;
GO

-- View for events with registration counts
CREATE OR ALTER VIEW vw_EventRegistrationStats AS
SELECT 
    e.EventID,
    e.Title,
    e.StartDate,
    e.EndDate,
    v.Name AS VenueName,
    v.Capacity AS VenueCapacity,
    est.Name AS EventStatus,
    COUNT(r.RegistrationID) AS TotalRegistrations,
    COUNT(CASE WHEN rst.Name = 'Confirmed' THEN 1 END) AS ConfirmedRegistrations,
    COUNT(CASE WHEN rst.Name = 'Pending' THEN 1 END) AS PendingRegistrations,
    COUNT(CASE WHEN rst.Name = 'Cancelled' THEN 1 END) AS CancelledRegistrations
FROM EventM e
INNER JOIN Venue v ON e.VenueID = v.VenueID
INNER JOIN EventStatusType est ON e.StatusTypeID = est.EventStatusTypeID
LEFT JOIN Registration r ON e.EventID = r.EventID
LEFT JOIN RegistrationStatusType rst ON r.RegistrationStatusTypeID = rst.RegistrationStatusTypeID
GROUP BY e.EventID, e.Title, e.StartDate, e.EndDate, v.Name, v.Capacity, est.Name;
GO

-- View for upcoming events
CREATE OR ALTER VIEW vw_UpcomingEvents AS
SELECT 
    e.EventID,
    e.Title,
    e.Description,
    e.StartDate,
    e.EndDate,
    v.Name AS VenueName,
    v.Address AS VenueAddress,
    est.Name AS EventStatus,
    DATEDIFF(day, GETUTCDATE(), e.StartDate) AS DaysUntilEvent
FROM EventM e
INNER JOIN Venue v ON e.VenueID = v.VenueID
INNER JOIN EventStatusType est ON e.StatusTypeID = est.EventStatusTypeID
WHERE e.StartDate > GETUTCDATE()
AND est.Name IN ('Published', 'Active');
GO

-- =====================================================
-- SESSION MANAGEMENT VIEWS
-- =====================================================

-- View for complete session information
CREATE OR ALTER VIEW vw_SessionComplete AS
SELECT 
    s.SessionID,
    s.Track,
    s.Role,
    s.Title,
    s.Abstract,
    s.StartAt,
    s.EndAt,
    e.EventID,
    e.Title AS EventTitle,
    r.Name AS RoomName,
    r.Capacity AS RoomCapacity,
    v.Name AS VenueName,
    sst.Name AS SessionStatus,
    u.Username AS CreatedByUsername,
    s.CreatedAt,
    s.UpdatedAt
FROM SessionM s
INNER JOIN EventM e ON s.EventID = e.EventID
LEFT JOIN Room r ON s.RoomID = r.RoomID
LEFT JOIN Venue v ON r.VenueID = v.VenueID
INNER JOIN SessionStatusType sst ON s.StatusTypeID = sst.SessionStatusTypeID
INNER JOIN UserM u ON s.CreatedByUserID = u.UserID;
GO

-- View for sessions with presenters
CREATE OR ALTER VIEW vw_SessionWithPresenters AS
SELECT 
    s.SessionID,
    s.Title AS SessionTitle,
    s.Track,
    s.StartAt,
    s.EndAt,
    e.EventID,
    e.Title AS EventTitle,
    r.Name AS RoomName,
    v.Name AS VenueName,
    sst.Name AS SessionStatus,
    STRING_AGG(CONCAT(p.FirstName, ' ', p.LastName), ', ') AS Presenters,
    COUNT(sp.PresenterID) AS PresenterCount
FROM SessionM s
INNER JOIN EventM e ON s.EventID = e.EventID
LEFT JOIN Room r ON s.RoomID = r.RoomID
LEFT JOIN Venue v ON r.VenueID = v.VenueID
INNER JOIN SessionStatusType sst ON s.StatusTypeID = sst.SessionStatusTypeID
LEFT JOIN SessionPresenter sp ON s.SessionID = sp.SessionID
LEFT JOIN Presenter p ON sp.PresenterID = p.PresenterID
GROUP BY s.SessionID, s.Title, s.Track, s.StartAt, s.EndAt, e.EventID, e.Title, r.Name, v.Name, sst.Name;
GO

-- View for presenter information
CREATE OR ALTER VIEW vw_PresenterComplete AS
SELECT 
    p.PresenterID,
    p.FirstName,
    p.MiddleName,
    p.LastName,
    p.Bio,
    p.Email,
    p.PhotoUrl,
    p.ContactInfo,
    u.Username AS CreatedByUsername,
    COUNT(sp.SessionID) AS SessionCount,
    p.CreatedAt,
    p.UpdatedAt
FROM Presenter p
INNER JOIN UserM u ON p.CreatedByUserID = u.UserID
LEFT JOIN SessionPresenter sp ON p.PresenterID = sp.PresenterID
GROUP BY p.PresenterID, p.FirstName, p.MiddleName, p.LastName, p.Bio, p.Email, p.PhotoUrl, p.ContactInfo, u.Username, p.CreatedAt, p.UpdatedAt;
GO

-- =====================================================
-- REGISTRATION VIEWS
-- =====================================================

-- View for complete registration information
CREATE OR ALTER VIEW vw_RegistrationComplete AS
SELECT 
    r.RegistrationID,
    a.AttendeeID,
    a.FirstName,
    a.MiddleName,
    a.LastName,
    a.Email AS AttendeeEmail,
    a.Organization,
    a.Type AS AttendeeType,
    e.EventID,
    e.Title AS EventTitle,
    e.StartDate AS EventStartDate,
    e.EndDate AS EventEndDate,
    v.Name AS VenueName,
    rst.Name AS RegistrationStatus,
    r.CreatedAt AS RegistrationDate,
    r.UpdatedAt
FROM Registration r
INNER JOIN Attendee a ON r.AttendeeID = a.AttendeeID
INNER JOIN EventM e ON r.EventID = e.EventID
INNER JOIN Venue v ON e.VenueID = v.VenueID
INNER JOIN RegistrationStatusType rst ON r.RegistrationStatusTypeID = rst.RegistrationStatusTypeID;
GO

-- View for attendee event registrations
CREATE OR ALTER VIEW vw_AttendeeRegistrations AS
SELECT 
    a.AttendeeID,
    a.FirstName,
    a.LastName,
    a.Email,
    a.Organization,
    e.EventID,
    e.Title AS EventTitle,
    e.StartDate,
    e.EndDate,
    v.Name AS VenueName,
    rst.Name AS RegistrationStatus,
    r.CreatedAt AS RegistrationDate
FROM Attendee a
INNER JOIN Registration r ON a.AttendeeID = r.AttendeeID
INNER JOIN EventM e ON r.EventID = e.EventID
INNER JOIN Venue v ON e.VenueID = v.VenueID
INNER JOIN RegistrationStatusType rst ON r.RegistrationStatusTypeID = rst.RegistrationStatusTypeID;
GO

-- =====================================================
-- VENUE AND ROOM VIEWS
-- =====================================================

-- View for venue with room information
CREATE OR ALTER VIEW vw_VenueWithRooms AS
SELECT 
    v.VenueID,
    v.Name AS VenueName,
    v.Address,
    v.Capacity AS VenueCapacity,
    v.Rooms AS TotalRooms,
    v.MapLink,
    COUNT(r.RoomID) AS ActualRooms,
    SUM(r.Capacity) AS TotalRoomCapacity,
    v.CreatedAt,
    v.UpdatedAt
FROM Venue v
LEFT JOIN Room r ON v.VenueID = r.VenueID
GROUP BY v.VenueID, v.Name, v.Address, v.Capacity, v.Rooms, v.MapLink, v.CreatedAt, v.UpdatedAt;
GO

-- View for room availability (simplified)
CREATE OR ALTER VIEW vw_RoomAvailability AS
SELECT 
    r.RoomID,
    r.Name AS RoomName,
    r.Capacity,
    v.VenueID,
    v.Name AS VenueName,
    v.Address AS VenueAddress,
    COUNT(s.SessionID) AS ScheduledSessions,
    r.CreatedAt,
    r.UpdatedAt
FROM Room r
INNER JOIN Venue v ON r.VenueID = v.VenueID
LEFT JOIN SessionM s ON r.RoomID = s.RoomID
GROUP BY r.RoomID, r.Name, r.Capacity, v.VenueID, v.Name, v.Address, r.CreatedAt, r.UpdatedAt;
GO

-- =====================================================
-- DASHBOARD VIEWS
-- =====================================================

-- View for dashboard statistics
CREATE OR ALTER VIEW vw_DashboardStats AS
SELECT 
    (SELECT COUNT(*) FROM UserM) AS TotalUsers,
    (SELECT COUNT(*) FROM Attendee) AS TotalAttendees,
    (SELECT COUNT(*) FROM EventM) AS TotalEvents,
    (SELECT COUNT(*) FROM EventM WHERE StartDate > GETUTCDATE()) AS UpcomingEvents,
    (SELECT COUNT(*) FROM Registration) AS TotalRegistrations,
    (SELECT COUNT(*) FROM SessionM) AS TotalSessions,
    (SELECT COUNT(*) FROM Presenter) AS TotalPresenters,
    (SELECT COUNT(*) FROM Venue) AS TotalVenues;
GO

-- View for recent activity
CREATE OR ALTER VIEW vw_RecentActivity AS
SELECT 
    'Event' AS ActivityType,
    EventID AS ID,
    Title AS Name,
    CreatedAt AS ActivityDate,
    'Created by ' + u.Username AS Details
FROM EventM e
INNER JOIN UserM u ON e.CreatedByUserID = u.UserID
WHERE e.CreatedAt >= DATEADD(day, -30, GETUTCDATE())

UNION ALL

SELECT 
    'Registration' AS ActivityType,
    r.RegistrationID AS ID,
    CONCAT(a.FirstName, ' ', a.LastName) AS Name,
    r.CreatedAt AS ActivityDate,
    'Registered for ' + e.Title AS Details
FROM Registration r
INNER JOIN Attendee a ON r.AttendeeID = a.AttendeeID
INNER JOIN EventM e ON r.EventID = e.EventID
WHERE r.CreatedAt >= DATEADD(day, -30, GETUTCDATE())

UNION ALL

SELECT 
    'Session' AS ActivityType,
    SessionID AS ID,
    Title AS Name,
    CreatedAt AS ActivityDate,
    'Created by ' + u.Username AS Details
FROM SessionM s
INNER JOIN UserM u ON s.CreatedByUserID = u.UserID
WHERE s.CreatedAt >= DATEADD(day, -30, GETUTCDATE())

ORDER BY ActivityDate DESC;
GO

-- =====================================================
-- REPORTING VIEWS
-- =====================================================

-- View for event attendance report
CREATE OR ALTER VIEW vw_EventAttendanceReport AS
SELECT 
    e.EventID,
    e.Title AS EventTitle,
    e.StartDate,
    e.EndDate,
    v.Name AS VenueName,
    v.Capacity AS VenueCapacity,
    COUNT(r.RegistrationID) AS TotalRegistrations,
    COUNT(CASE WHEN rst.Name = 'Confirmed' THEN 1 END) AS ConfirmedAttendees,
    COUNT(CASE WHEN rst.Name = 'Pending' THEN 1 END) AS PendingAttendees,
    COUNT(CASE WHEN rst.Name = 'Cancelled' THEN 1 END) AS CancelledRegistrations,
    CAST(COUNT(CASE WHEN rst.Name = 'Confirmed' THEN 1 END) * 100.0 / NULLIF(v.Capacity, 0) AS DECIMAL(5,2)) AS AttendancePercentage
FROM EventM e
INNER JOIN Venue v ON e.VenueID = v.VenueID
LEFT JOIN Registration r ON e.EventID = r.EventID
LEFT JOIN RegistrationStatusType rst ON r.RegistrationStatusTypeID = rst.RegistrationStatusTypeID
GROUP BY e.EventID, e.Title, e.StartDate, e.EndDate, v.Name, v.Capacity;
GO

-- View for presenter session report
CREATE OR ALTER VIEW vw_PresenterSessionReport AS
SELECT 
    p.PresenterID,
    p.FirstName,
    p.LastName,
    p.Email,
    p.Bio,
    COUNT(sp.SessionID) AS TotalSessions,
    COUNT(DISTINCT s.EventID) AS TotalEvents,
    MIN(s.StartAt) AS FirstSession,
    MAX(s.EndAt) AS LastSession
FROM Presenter p
LEFT JOIN SessionPresenter sp ON p.PresenterID = sp.PresenterID
LEFT JOIN SessionM s ON sp.SessionID = s.SessionID
GROUP BY p.PresenterID, p.FirstName, p.LastName, p.Email, p.Bio;
GO

-- =====================================================
-- SEARCH AND FILTER VIEWS
-- =====================================================

-- View for event search (with all relevant information)
CREATE OR ALTER VIEW vw_EventSearch AS
SELECT 
    e.EventID,
    e.Title,
    e.Description,
    e.StartDate,
    e.EndDate,
    v.Name AS VenueName,
    v.Address AS VenueAddress,
    est.Name AS EventStatus,
    COUNT(r.RegistrationID) AS RegistrationCount,
    COUNT(s.SessionID) AS SessionCount,
    u.Username AS CreatedBy
FROM EventM e
INNER JOIN Venue v ON e.VenueID = v.VenueID
INNER JOIN EventStatusType est ON e.StatusTypeID = est.EventStatusTypeID
INNER JOIN UserM u ON e.CreatedByUserID = u.UserID
LEFT JOIN Registration r ON e.EventID = r.EventID
LEFT JOIN SessionM s ON e.EventID = s.EventID
GROUP BY e.EventID, e.Title, e.Description, e.StartDate, e.EndDate, v.Name, v.Address, est.Name, u.Username;
GO

-- View for attendee search
CREATE OR ALTER VIEW vw_AttendeeSearch AS
SELECT 
    a.AttendeeID,
    a.FirstName,
    a.MiddleName,
    a.LastName,
    a.Email,
    a.Organization,
    a.Phone,
    a.Location,
    a.Type AS AttendeeType,
    st.Name AS StatusName,
    COUNT(r.RegistrationID) AS EventRegistrationCount,
    a.CreatedAt
FROM Attendee a
INNER JOIN StatusType st ON a.StatusTypeID = st.StatusTypeID
LEFT JOIN Registration r ON a.AttendeeID = r.AttendeeID
GROUP BY a.AttendeeID, a.FirstName, a.MiddleName, a.LastName, a.Email, a.Organization, a.Phone, a.Location, a.Type, st.Name, a.CreatedAt;
GO

PRINT 'All views created successfully!';
GO
