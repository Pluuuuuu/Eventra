-- =====================================================
-- Eventra Database Views
-- Useful views for common queries and reporting
-- =====================================================

USE EventPlannerDB;
GO

-- =====================================================
-- USER MANAGEMENT VIEWS
-- =====================================================

-- View for complete user information with roles and status
CREATE OR ALTER VIEW vw_UserComplete AS
SELECT 
    u.UserID,
    u.Username,
    u.FirstName,
    u.MiddleName,
    u.LastName,
    u.Email,
    u.ProfilePicUrl,
    rt.Name AS RoleName,
    st.Name AS StatusName,
    u.PeriodCanLoginInMinutes,
    u.LastFailedLoginAt,
    u.CreatedAt,
    u.UpdatedAt
FROM UserM u
INNER JOIN RoleType rt ON u.RoleTypeID = rt.RoleTypeID
INNER JOIN StatusType st ON u.StatusTypeID = st.StatusTypeID;
GO

-- View for attendee information with user details
CREATE OR ALTER VIEW vw_AttendeeComplete AS
SELECT 
    a.AttendeeID,
    a.FirstName,
    a.MiddleName,
    a.LastName,
    a.Email,
    a.Organization,
    a.Phone,
    a.Location,
    a.Gender,
    a.DateOfBirth,
    a.ProfilePicUrl,
    a.Type AS AttendeeType,
    st.Name AS StatusName,
    u.Username,
    u.RoleTypeID,
    a.CreatedAt,
    a.UpdatedAt
FROM Attendee a
INNER JOIN StatusType st ON a.StatusTypeID = st.StatusTypeID
LEFT JOIN UserM u ON a.UserID = u.UserID;
GO

-- View for user permissions
CREATE OR ALTER VIEW vw_UserPermissions AS
SELECT 
    u.UserID,
    u.Username,
    u.Email,
    rt.Name AS RoleName,
    p.Name AS PermissionName,
    p.Description AS PermissionDescription
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
