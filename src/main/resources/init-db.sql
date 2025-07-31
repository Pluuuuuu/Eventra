-- Initialize lookup tables for Eventra application

-- Status Type lookup table
INSERT INTO StatusType (StatusTypeID, Name) VALUES 
(1, 'Active'),
(2, 'Inactive'),
(3, 'Suspended'),
(4, 'Deleted');

-- Role Type lookup table
INSERT INTO RoleType (RoleTypeID, Name) VALUES 
(1, 'SuperAdmin'),
(2, 'Admin'),
(3, 'Staff'),
(4, 'Attendees');

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
VALUES ('testuser', 'Test', 'User', 'test@eventra.com', '$2a$10$TToB9tKSXwz5Ub9mOByFMekz.ygcmX2ZMgiDmpP.QigvnhUtjVXrK', 2, 1, GETUTCDATE(), GETUTCDATE());

-- Insert demo attendees (password: demo123)
INSERT INTO UserM (Username, FirstName, LastName, Email, PasswordHash, RoleTypeID, StatusTypeID, CreatedAt, UpdatedAt) 
VALUES ('attendee1', 'John', 'Doe', 'john.doe@example.com', '$2a$10$7G4gg/2D.5VwHNyXKrRoAODBUyvqZ2mXlHMTi/jKMFxhPRlT54O6e', 4, 1, GETUTCDATE(), GETUTCDATE());

INSERT INTO UserM (Username, FirstName, LastName, Email, PasswordHash, RoleTypeID, StatusTypeID, CreatedAt, UpdatedAt) 
VALUES ('attendee2', 'Jane', 'Smith', 'jane.smith@example.com', '$2a$10$7G4gg/2D.5VwHNyXKrRoAODBUyvqZ2mXlHMTi/jKMFxhPRlT54O6e', 4, 1, GETUTCDATE(), GETUTCDATE()); 