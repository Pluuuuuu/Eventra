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

-- Insert demo organizers (password: demo123)
INSERT INTO UserM (Username, FirstName, LastName, Email, PasswordHash, RoleTypeID, StatusTypeID, CreatedAt, UpdatedAt) 
VALUES ('organizer1', 'Global', 'Events Co.', 'global@events.com', '$2a$10$7G4gg/2D.5VwHNyXKrRoAODBUyvqZ2mXlHMTi/jKMFxhPRlT54O6e', 2, 1, GETUTCDATE(), GETUTCDATE());

INSERT INTO UserM (Username, FirstName, LastName, Email, PasswordHash, RoleTypeID, StatusTypeID, CreatedAt, UpdatedAt) 
VALUES ('organizer2', 'NYC Music', 'Collective', 'nyc@music.com', '$2a$10$7G4gg/2D.5VwHNyXKrRoAODBUyvqZ2mXlHMTi/jKMFxhPRlT54O6e', 2, 1, GETUTCDATE(), GETUTCDATE());

INSERT INTO UserM (Username, FirstName, LastName, Email, PasswordHash, RoleTypeID, StatusTypeID, CreatedAt, UpdatedAt) 
VALUES ('organizer3', 'Innovate', 'Hub', 'innovate@hub.com', '$2a$10$7G4gg/2D.5VwHNyXKrRoAODBUyvqZ2mXlHMTi/jKMFxhPRlT54O6e', 2, 1, GETUTCDATE(), GETUTCDATE());

INSERT INTO UserM (Username, FirstName, LastName, Email, PasswordHash, RoleTypeID, StatusTypeID, CreatedAt, UpdatedAt) 
VALUES ('organizer4', 'Culinary', 'Delights Inc.', 'culinary@delights.com', '$2a$10$7G4gg/2D.5VwHNyXKrRoAODBUyvqZ2mXlHMTi/jKMFxhPRlT54O6e', 2, 1, GETUTCDATE(), GETUTCDATE());

INSERT INTO UserM (Username, FirstName, LastName, Email, PasswordHash, RoleTypeID, StatusTypeID, CreatedAt, UpdatedAt) 
VALUES ('organizer5', 'Mindful Living', 'Institute', 'mindful@living.com', '$2a$10$7G4gg/2D.5VwHNyXKrRoAODBUyvqZ2mXlHMTi/jKMFxhPRlT54O6e', 2, 1, GETUTCDATE(), GETUTCDATE());

-- Insert demo events
INSERT INTO Event (Title, Description, StartDateTime, EndDateTime, Location, ImageURL, EventStatusTypeID, OrganizerID, MaxAttendees, CurrentAttendees, EventType, CreatedAt, UpdatedAt) 
VALUES ('An Evening of Jazz & Soul', 'Experience the finest jazz and soul music in an intimate setting', '2024-04-20 19:00:00', '2024-04-20 23:00:00', 'The Blue Note Jazz Club, NYC', '/images/jazz-event.jpg', 2, 2, 100, 0, 'Music', GETUTCDATE(), GETUTCDATE());

INSERT INTO Event (Title, Description, StartDateTime, EndDateTime, Location, ImageURL, EventStatusTypeID, OrganizerID, MaxAttendees, CurrentAttendees, EventType, CreatedAt, UpdatedAt) 
VALUES ('Global Tech Innovation Summit 2024', 'Join industry leaders for the most innovative tech conference of the year', '2024-05-10 09:00:00', '2024-05-10 18:00:00', 'Javits Center, New York', '/images/tech-summit.jpg', 2, 3, 500, 0, 'Technology', GETUTCDATE(), GETUTCDATE());

INSERT INTO Event (Title, Description, StartDateTime, EndDateTime, Location, ImageURL, EventStatusTypeID, OrganizerID, MaxAttendees, CurrentAttendees, EventType, CreatedAt, UpdatedAt) 
VALUES ('Annual Tech Summit 2024', 'The premier technology conference featuring cutting-edge innovations and industry leaders', '2024-10-26 09:00:00', '2024-10-28 17:00:00', 'Virtual & San Francisco, CA', '/images/tech-conference.jpg', 2, 3, 1000, 0, 'Technology', GETUTCDATE(), GETUTCDATE());

-- Insert demo presenters
INSERT INTO Presenter (Name, Title, Company, Bio, ImageURL, Email, CreatedAt, UpdatedAt) 
VALUES ('Dr. Anya Sharma', 'Chief AI Ethicist', 'FutureMind Inc.', 'Leading expert in AI ethics and responsible technology development', '/images/presenter1.jpg', 'anya@futuremind.com', GETUTCDATE(), GETUTCDATE());

INSERT INTO Presenter (Name, Title, Company, Bio, ImageURL, Email, CreatedAt, UpdatedAt) 
VALUES ('Michael Chen', 'VP of Blockchain Development', 'CryptoVerse Labs', 'Blockchain pioneer with 15+ years of experience in distributed systems', '/images/presenter2.jpg', 'michael@cryptoverse.com', GETUTCDATE(), GETUTCDATE());

INSERT INTO Presenter (Name, Title, Company, Bio, ImageURL, Email, CreatedAt, UpdatedAt) 
VALUES ('Sarah Lee', 'CEO & Founder', 'EcoSolutions Tech', 'Environmental technology entrepreneur focused on sustainable solutions', '/images/presenter3.jpg', 'sarah@ecosolutions.com', GETUTCDATE(), GETUTCDATE());

-- Insert demo sessions
INSERT INTO Session (EventID, Title, Description, StartTime, EndTime, SessionStatusTypeID, PresenterID, SessionType, MaxCapacity, CreatedAt, UpdatedAt) 
VALUES (3, 'Panel: Sustainable Tech for a Greener Future', 'Discussion on how technology can drive environmental sustainability', '2024-10-26 10:00:00', '2024-10-26 11:30:00', 2, 3, 'Panel', 200, GETUTCDATE(), GETUTCDATE());

INSERT INTO Session (EventID, Title, Description, StartTime, EndTime, SessionStatusTypeID, PresenterID, SessionType, MaxCapacity, CreatedAt, UpdatedAt) 
VALUES (3, 'Deep Dive: Quantum Supremacy and Its Applications', 'Exploring the latest developments in quantum computing', '2024-10-26 14:00:00', '2024-10-26 15:30:00', 2, 1, 'Workshop', 150, GETUTCDATE(), GETUTCDATE());

INSERT INTO Session (EventID, Title, Description, StartTime, EndTime, SessionStatusTypeID, PresenterID, SessionType, MaxCapacity, CreatedAt, UpdatedAt) 
VALUES (3, 'Fireside Chat: The Human Element in Cybersecurity', 'Personal insights on building secure systems', '2024-10-26 15:15:00', '2024-10-26 16:00:00', 2, 2, 'Fireside Chat', 100, GETUTCDATE(), GETUTCDATE()); 