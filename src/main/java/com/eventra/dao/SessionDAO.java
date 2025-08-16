package com.eventra.dao;

import com.eventra.Db;
import com.eventra.model.Session;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Session Data Access Object (SessionDAO.java)
 * 
 * This class handles all database operations related to event sessions
 * in the Eventra system. Think of it like a "schedule manager" who keeps
 * track of all the activities, presentations, and meetings that happen
 * during events.
 * 
 * WHAT THIS CLASS DOES:
 * - Finds all sessions for a specific event
 * - Retrieves detailed information about individual sessions
 * - Converts database records into Session objects
 * - Manages the connection between sessions and presenters
 * - Handles session scheduling and timing information
 * 
 * KEY CONCEPTS TO UNDERSTAND:
 * - Data Access Object (DAO): A class that handles database operations
 * - Database Queries: SQL commands that fetch data from the database
 * - Result Set Mapping: Converting database results into Java objects
 * - Session Management: Organizing activities within events
 * - Time Management: Handling start and end times for sessions
 * 
 * BUSINESS VALUE:
 * - Enables event organizers to see their complete event schedule
 * - Helps attendees plan which sessions to attend
 * - Manages session timing and presenter assignments
 * - Provides data for event programs and agendas
 * - Supports session capacity and registration management
 * 
 * REAL-WORLD ANALOGY:
 * - Like a conference program that lists all sessions and times
 * - Like a school schedule that shows all classes and periods
 * - Like a meeting agenda that outlines all activities
 * - Like a festival lineup that shows all performances
 */
public class SessionDAO {
    
    /**
     * getSessionsByEventId(int eventId) - Find All Sessions for an Event
     * 
     * This method finds all the sessions that are scheduled for a specific event.
     * It's like looking up a complete conference program to see all the
     * presentations, workshops, and activities that will happen.
     * 
     * WHAT IT DOES:
     * - Searches the database for all sessions linked to a specific event
     * - Only shows active sessions (StatusTypeID = 1, not cancelled)
     * - Returns sessions sorted chronologically by start time
     * - Automatically converts database records to Session objects
     * - Includes complete session details like timing and presenters
     * 
     * USER EXPERIENCE:
     * - Event organizers can see their complete event schedule
     * - Attendees can browse all available sessions before registering
     * - Event marketing teams can create detailed event programs
     * - Event planning teams can manage session logistics
     * 
     * TECHNICAL PROCESS:
     * 1. Connects to the database safely
     * 2. Runs a SQL query to find all sessions for the event
     * 3. Converts each database result into a Session object
     * 4. Returns a chronologically sorted list of all sessions
     * 
     * @param eventId The ID of the event to find sessions for
     * @return A list of Session objects sorted by start time, or empty list if none found
     */
    public List<Session> getSessionsByEventId(int eventId) {
        // 
        // CREATE RESULT CONTAINER - Prepare to store found sessions
        // 
        // This creates an empty list that we'll fill with session information
        // as we find it in the database.
        // 
        // THINK OF IT LIKE:
        // - Getting an empty calendar to fill with appointments
        // - Preparing a schedule template to populate with activities
        // - Creating a list to write down all scheduled events
        // 
        List<Session> sessions = new ArrayList<>();
        
        // 
        // SQL QUERY - Find all sessions for the specific event
        // 
        // This SQL query finds all sessions that belong to the specified event.
        // It filters for active sessions only and orders them by start time.
        // 
        // QUERY EXPLANATION:
        // - SELECT *: Get all session information
        // - FROM SessionM: Search in the main sessions table
        // - WHERE EventID = ?: Only for the specific event we're looking for
        // - AND StatusTypeID = 1: Only active sessions (not cancelled or completed)
        // - ORDER BY StartAt ASC: Sort chronologically by start time
        // 
        String sql = "SELECT * FROM SessionM WHERE EventID = ? AND StatusTypeID = 1 ORDER BY StartAt ASC";
        
        // 
        // DATABASE OPERATION - Execute the query and process results
        // 
        // This section safely connects to the database, runs the query,
        // and processes each result to create Session objects.
        // 
        // THINK OF IT LIKE:
        // - Opening a conference program to see all scheduled sessions
        // - Looking through the schedule one session at a time
         // - Taking notes on each scheduled activity
         // 
        try (Connection conn = Db.get();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            // 
            // SET QUERY PARAMETER - Specify which event to search for
            // 
            // This replaces the "?" placeholder in the SQL query with the
            // actual event ID we want to find sessions for.
            // 
            // THINK OF IT LIKE:
            // - Filling in a search form with a specific event name
            // - Replacing a placeholder with the actual value
            // - Specifying which conference you want the schedule for
            // 
            stmt.setInt(1, eventId);
            
            // 
            // EXECUTE QUERY - Run the search and get results
            // 
            // This runs the SQL query and returns a result set containing
            // all the session information that matches our search.
            // 
            // THINK OF IT LIKE:
            // - Submitting a search and getting back all scheduled sessions
            // - Running a search engine query and seeing the results
            // - Looking up information and getting a complete schedule
            // 
            ResultSet rs = stmt.executeQuery();
            
            // 
            // PROCESS RESULTS - Convert each database record to a Session object
            // 
            // This loop goes through each result from the database and
            // converts it into a Session object that the application can use.
            // 
            // THINK OF IT LIKE:
            // - Going through a conference program one session at a time
            // - Reading through a schedule and creating calendar entries
            // - Processing each scheduled activity to extract useful information
            // 
            while (rs.next()) {
                // 
                // CONVERT DATABASE RECORD TO SESSION OBJECT
                // 
                // This helper method takes the raw database data and converts
                // it into a properly formatted Session object.
                // 
                // THINK OF IT LIKE:
                // - Converting a schedule entry into a calendar appointment
                // - Taking raw data and organizing it into a structured format
                // - Transforming database information into user-friendly objects
                // 
                Session session = mapResultSetToSession(rs);
                
                // 
                // ADD TO RESULTS LIST - Collect all sessions found
                // 
                // This adds each session we found to our results list
                // so we can return all of them together.
                // 
                // THINK OF IT LIKE:
                // - Adding appointments to a calendar as you find them
                // - Collecting schedule entries in a planner
                // - Building a complete program as you discover sessions
                // 
                sessions.add(session);
            }
        } catch (SQLException e) {
            // 
            // ERROR HANDLING - Log and report any database problems
            // 
            // If something goes wrong with the database operation, we log
            // the error so developers can fix it, but don't crash the application.
            // 
            // THINK OF IT LIKE:
            // - A car's check engine light that alerts you to problems
            // - A website that shows an error message instead of crashing
            // - A system that gracefully handles problems and reports them
            // 
            System.err.println("Error fetching sessions by event ID: " + e.getMessage());
            e.printStackTrace();
        }
        
        // 
        // RETURN RESULTS - Send back all sessions found
        // 
        // This returns the complete list of sessions, whether we found
        // many, few, or none at all.
        // 
        // THINK OF IT LIKE:
        // - Handing over a completed event schedule
        // - Returning a filled calendar with all appointments
        // - Delivering a complete conference program
        // 
        return sessions;
    }
    
    /**
     * mapResultSetToSession(ResultSet rs) - Convert Database Data to Session Object
     * 
     * This private helper method takes raw database results and converts them
     * into a properly formatted Session object. It's like a translator that
     * converts database language into Java language.
     * 
     * WHAT IT DOES:
     * - Takes database query results and converts them to Session objects
     * - Maps database field names to Session object properties
     * - Converts database timestamps to Java date/time objects
     * - Looks up presenter information for each session
     * - Sets default values for missing information
     * - Handles null values gracefully
     * 
     * USER EXPERIENCE:
     * - Ensures session information is consistently formatted
     * - Provides complete session details including presenters
     * - Converts technical database data into user-friendly objects
     * - Handles incomplete data gracefully
     * 
     * TECHNICAL PROCESS:
     * 1. Creates a new empty Session object
     * 2. Extracts data from the database result set
     * 3. Maps database fields to Session properties
     * 4. Looks up additional information like presenters
     * 5. Sets default values for missing information
     * 6. Returns the complete Session object
     * 
     * @param rs The database result set containing session information
     * @return A fully populated Session object
     * @throws SQLException If there's an error reading the database data
     */
    private Session mapResultSetToSession(ResultSet rs) throws SQLException {
        // 
        // CREATE EMPTY SESSION OBJECT - Start with a blank session
        // 
        // This creates a new, empty Session object that we'll fill
        // with information from the database.
        // 
        // THINK OF IT LIKE:
        // - Getting a blank calendar entry to fill out
        // - Creating an empty schedule slot to populate with information
        // - Starting with a blank canvas to paint a session on
        // 
        Session session = new Session();
        
        // 
        // SET BASIC IDENTIFICATION - Assign unique session and event IDs
        // 
        // This sets the unique identifiers that make this session
        // distinct and link it to the correct event.
        // 
        // THINK OF IT LIKE:
        // - Assigning a session number to a conference activity
        // - Giving a unique ID to a scheduled meeting
        // - Creating a reference number for a calendar appointment
        // 
        session.setSessionId(rs.getInt("SessionID"));
        session.setEventId(rs.getInt("EventID"));
        
        // 
        // SET SESSION CONTENT - Title and description information
        // 
        // This sets the basic information about what the session is about
        // and what attendees can expect to learn or experience.
        // 
        // THINK OF IT LIKE:
        // - Filling in the title and description of a calendar appointment
        // - Writing the name and summary of a conference session
        // - Creating the header and details of a scheduled activity
        // 
        session.setTitle(rs.getString("Title"));
        session.setDescription(rs.getString("Abstract"));
        
        // 
        // MAP TIME FIELDS - Convert database timestamps to Java date/time objects
        // 
        // This section converts the database timestamp fields (StartAt/EndAt)
        // to Java LocalDateTime objects that the Session model can use.
        // 
        // THINK OF IT LIKE:
        // - Converting between different time formats
        // - Translating database time information to Java time information
        // - Ensuring time data is in the right format for the application
        // 
        session.setStartTime(rs.getTimestamp("StartAt").toLocalDateTime());
        session.setEndTime(rs.getTimestamp("EndAt").toLocalDateTime());
        
        // 
        // SET SESSION STATUS - Current state of the session
        // 
        // This sets whether the session is active, cancelled, completed,
        // or in another state.
        // 
        // THINK OF IT LIKE:
        // - Marking a meeting as confirmed, pending, or cancelled
        // - Setting the status of a scheduled activity
        // - Indicating whether a session is ready to go
        // 
        session.setSessionStatusTypeId(rs.getInt("StatusTypeID"));
        
        // 
        // LOOK UP PRESENTER - Find who is leading this session
        // 
        // This calls a helper method to find the presenter ID for this
        // session by looking in the SessionPresenter relationship table.
        // 
        // THINK OF IT LIKE:
        // - Looking up who is assigned to lead a meeting
        // - Finding the speaker for a conference session
        // - Identifying the instructor for a workshop
        // 
        session.setPresenterId(getPresenterIdForSession(rs.getInt("SessionID")));
        
        // 
        // SET SESSION TYPE AND CAPACITY - Categorize and limit the session
        // 
        // This sets what kind of session this is (workshop, keynote, panel)
        // and how many people can attend.
        // 
        // THINK OF IT LIKE:
        // - Categorizing a meeting as a workshop, presentation, or discussion
        // - Setting a limit on how many people can join a session
        // - Defining the format and capacity of an activity
        // 
        session.setSessionType(rs.getString("Track"));
        session.setMaxCapacity(0); // Default unlimited capacity
        
        // 
        // HANDLE TIMESTAMPS - Convert database timestamps to Java date/time objects
        // 
        // This section safely converts database timestamp fields to Java
        // LocalDateTime objects, handling cases where timestamps might be missing.
        // 
        // THINK OF IT LIKE:
        // - Converting between different time formats
        // - Translating database time information to Java time information
        // - Ensuring time data is in the right format for the application
        // 
        
        // 
        // SET CREATION TIMESTAMP - When the session was first planned
        // 
        // This sets when the session record was originally created in the system.
        // If no timestamp exists, we use the current time as a default.
        // 
        // THINK OF IT LIKE:
        // - Recording when a meeting was first scheduled
        // - Stamping a creation date on a calendar entry
        // - Marking when something was first planned in a system
        // 
        if (rs.getTimestamp("CreatedAt") != null) {
            session.setCreatedAt(rs.getTimestamp("CreatedAt").toLocalDateTime());
        } else {
            session.setCreatedAt(java.time.LocalDateTime.now());
        }
        
        // 
        // SET UPDATE TIMESTAMP - When the session was last modified
        // 
        // This sets when the session information was last changed.
        // If no timestamp exists, we use the current time as a default.
        // 
        // THINK OF IT LIKE:
        // - Recording when a meeting schedule was last updated
        // - Stamping a "last modified" date on a calendar entry
        // - Marking when something was last changed in a system
        // 
        if (rs.getTimestamp("UpdatedAt") != null) {
            session.setUpdatedAt(rs.getTimestamp("UpdatedAt").toLocalDateTime());
        } else {
            session.setUpdatedAt(java.time.LocalDateTime.now());
        }
        
        // 
        // RETURN COMPLETE SESSION OBJECT - Send back the fully populated session
        // 
        // This returns the Session object with all its fields properly
        // set with information from the database.
        // 
        // THINK OF IT LIKE:
        // - Handing over a completed calendar entry
        // - Delivering a finished session schedule
        // - Returning a complete record with all information filled in
        // 
        return session;
    }
    
    /**
     * getPresenterIdForSession(int sessionId) - Find Presenter for a Session
     * 
     * This private helper method looks up which presenter is assigned to
     * lead a specific session. It's like checking a meeting schedule to
     * see who is responsible for leading each activity.
     * 
     * WHAT IT DOES:
     * - Searches the SessionPresenter relationship table
     * - Finds the presenter ID linked to a specific session
     * - Returns the presenter ID if found, or 0 if none assigned
     * - Handles database errors gracefully
     * 
     * USER EXPERIENCE:
     * - Ensures each session shows who is leading it
     * - Provides complete session information including presenter details
     * - Helps attendees know who they'll be learning from
     * - Supports event planning and presenter management
     * 
     * TECHNICAL PROCESS:
     * 1. Connects to the database safely
     * 2. Runs a simple SQL query to find the presenter
     * 3. Returns the presenter ID or 0 if none found
     * 4. Handles any database errors gracefully
     * 
     * @param sessionId The ID of the session to find the presenter for
     * @return The presenter ID if found, or 0 if no presenter is assigned
     */
    private int getPresenterIdForSession(int sessionId) {
        // 
        // SQL QUERY - Find presenter assigned to the specific session
        // 
        // This is a simple query that looks in the SessionPresenter table
        // to find which presenter is assigned to lead this session.
        // 
        // THINK OF IT LIKE:
        // - Looking up who is assigned to lead a specific meeting
        // - Finding the speaker for a particular conference session
        // - Checking who is responsible for a scheduled activity
        // 
        String sql = "SELECT PresenterID FROM SessionPresenter WHERE SessionID = ?";
        
        // 
        // DATABASE OPERATION - Execute the query and get the result
        // 
        // This section safely connects to the database, runs the query,
        // and processes the result to find the presenter ID.
        // 
        // THINK OF IT LIKE:
        // - Looking up an assignment in a meeting schedule
        // - Searching for who is responsible for a specific task
        // - Finding the person assigned to lead an activity
        // 
        try (Connection conn = Db.get();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            // 
            // SET QUERY PARAMETER - Specify which session to search for
            // 
            // This replaces the "?" placeholder with the actual session ID
            // we want to find the presenter for.
            // 
            // THINK OF IT LIKE:
            // - Filling in a search form with a specific session number
            // - Replacing a placeholder with the actual value
            // - Specifying exactly which session you want presenter information for
            // 
            stmt.setInt(1, sessionId);
            
            // 
            // EXECUTE QUERY - Run the search and get the result
            // 
            // This runs the SQL query and returns a result set containing
            // the presenter ID if one is assigned to this session.
            // 
            // THINK OF IT LIKE:
            // - Submitting a search and getting back the assigned person
            // - Looking up an assignment and finding who is responsible
            // - Searching a database and getting the person assigned
            // 
            ResultSet rs = stmt.executeQuery();
            
            // 
            // CHECK FOR RESULTS - See if we found a presenter assignment
            // 
            // This checks if the database returned any results for our search.
            // If we found a presenter assignment, we return their ID.
            // 
            // THINK OF IT LIKE:
            // - Checking if your search returned any results
            // - Seeing if you found who is assigned to the session
            // - Verifying that your lookup was successful
            // 
            if (rs.next()) {
                // 
                // RETURN PRESENTER ID - Send back the assigned presenter
                // 
                // This returns the ID of the presenter who is assigned
                // to lead this session.
                // 
                // THINK OF IT LIKE:
                // - Returning the name of who is leading a meeting
                // - Sending back the speaker assigned to a session
                // - Delivering the person responsible for an activity
                // 
                return rs.getInt("PresenterID");
            }
        } catch (SQLException e) {
            // 
            // ERROR HANDLING - Log and report any database problems
            // 
            // If something goes wrong with the database operation, we log
            // the error so developers can fix it, but don't crash the application.
            // 
            // THINK OF IT LIKE:
            // - A car's check engine light that alerts you to problems
            // - A website that shows an error message instead of crashing
            // - A system that gracefully handles problems and reports them
            // 
            System.err.println("Error getting presenter for session: " + e.getMessage());
        }
        
        // 
        // RETURN DEFAULT VALUE - Indicate no presenter is assigned
        // 
        // If we didn't find a presenter assignment for this session,
        // we return 0 to indicate that no presenter is currently assigned.
        // 
        // THINK OF IT LIKE:
        // - Saying "sorry, no one is assigned to lead this session yet"
        // - Indicating that your search didn't find any assignments
        // - Returning a default value when no assignment exists
        // 
        return 0; // No presenter found
    }
} 