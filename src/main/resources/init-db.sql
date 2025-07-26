-- Initialize lookup tables for Eventra application

-- Status Type lookup table
INSERT INTO StatusType (StatusTypeID, Name) VALUES 
(1, 'Active'),
(2, 'Inactive'),
(3, 'Suspended'),
(4, 'Deleted');

-- Role Type lookup table
INSERT INTO RoleType (RoleTypeID, Name) VALUES 
(1, 'Administrator'),
(2, 'User'),
(3, 'Event Manager'),
(4, 'Presenter');

-- Event Status Type lookup table
INSERT INTO EventStatusType (EventStatusTypeID, Name) VALUES 
(1, 'Draft'),
(2, 'Published'),
(3, 'Cancelled'),
(4, 'Completed');

-- Session Status Type lookup table
INSERT INTO SessionStatusType (SessionStatusTypeID, Name) VALUES 
(1, 'Draft'),
(2, 'Confirmed'),
(3, 'Cancelled'),
(4, 'Completed');

-- Registration Status Type lookup table
INSERT INTO RegistrationStatusType (RegistrationStatusTypeID, Name) VALUES 
(1, 'Pending'),
(2, 'Confirmed'),
(3, 'Cancelled'),
(4, 'Attended');

-- Insert a test user (password: test123)
INSERT INTO UserM (Username, FirstName, LastName, Email, PasswordHash, RoleTypeID, StatusTypeID, CreatedAt, UpdatedAt) 
VALUES ('testuser', 'Test', 'User', 'test@eventra.com', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', 2, 1, GETUTCDATE(), GETUTCDATE()); 