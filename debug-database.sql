-- Debug script to check Azure SQL database content
-- Run this in your Azure SQL database to see what data exists

-- Check if tables exist
SELECT TABLE_NAME 
FROM INFORMATION_SCHEMA.TABLES 
WHERE TABLE_TYPE = 'BASE TABLE' 
AND TABLE_NAME IN ('UserM', 'EventM', 'Venue', 'Registration', 'Attendee');

-- Check UserM table content
SELECT 'UserM Table:' as TableName;
SELECT UserID, Username, Email, RoleTypeID, StatusTypeID 
FROM UserM 
ORDER BY UserID;

-- Check EventM table content
SELECT 'EventM Table:' as TableName;
SELECT EventID, Title, StartDate, EndDate, StatusTypeID, CreatedByUserID 
FROM EventM 
ORDER BY EventID;

-- Check Venue table content
SELECT 'Venue Table:' as TableName;
SELECT VenueID, Name, Capacity 
FROM Venue 
ORDER BY VenueID;

-- Check Registration table content
SELECT 'Registration Table:' as TableName;
SELECT RegistrationID, AttendeeID, EventID, RegistrationStatusTypeID 
FROM Registration 
ORDER BY RegistrationID;

-- Check Attendee table content
SELECT 'Attendee Table:' as TableName;
SELECT AttendeeID, FirstName, LastName, Email, Type 
FROM Attendee 
ORDER BY AttendeeID;

-- Check if matcha123 user exists
SELECT 'Looking for matcha123 user:' as Search;
SELECT UserID, Username, Email, RoleTypeID 
FROM UserM 
WHERE Email = 'matcha123@gmail.com' OR Username = 'matcha123';

-- Check events with venue info
SELECT 'Events with Venue Info:' as TableName;
SELECT e.EventID, e.Title, e.StartDate, e.EndDate, e.StatusTypeID,
       v.Name as VenueName, v.Capacity as VenueCapacity
FROM EventM e
LEFT JOIN Venue v ON e.VenueID = v.VenueID
ORDER BY e.EventID; 