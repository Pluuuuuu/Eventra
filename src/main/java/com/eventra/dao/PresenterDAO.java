package com.eventra.dao;

import com.eventra.Db;
import com.eventra.model.Presenter;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Presenter Data Access Object (PresenterDAO.java)
 * 
 * This class handles all database operations related to presenters and speakers
 * in the Eventra system. Think of it like a "librarian" who manages a collection
 * of presenter information, helping you find, retrieve, and organize speaker data.
 * 
 * WHAT THIS CLASS DOES:
 * - Finds presenters who are speaking at specific events
 * - Retrieves detailed information about individual presenters
 * - Converts database records into Presenter objects
 * - Manages the connection between presenters and events
 * 
 * KEY CONCEPTS TO UNDERSTAND:
 * - Data Access Object (DAO): A class that handles database operations
 * - Database Queries: SQL commands that fetch data from the database
 * - Result Set Mapping: Converting database results into Java objects
 * - Connection Management: Safely opening and closing database connections
 * 
 * BUSINESS VALUE:
 * - Enables event organizers to see who's speaking at their events
 * - Helps attendees learn about speakers before events
 * - Manages presenter information for event marketing
 * - Provides data for speaker directories and profiles
 * 
 * REAL-WORLD ANALOGY:
 * - Like a conference organizer who knows all the speakers and their details
 * - Like a talent agent who manages information about performers
 * - Like a library catalog that helps you find books by author
 * - Like a contact manager that stores information about people
 */
public class PresenterDAO {
    
    /**
     * getPresentersByEventId(int eventId) - Find All Speakers for an Event
     * 
     * This method finds all the presenters who are speaking at a specific event.
     * It's like looking up all the speakers in a conference program to see
     * who will be presenting.
     * 
     * WHAT IT DOES:
     * - Searches the database for presenters linked to a specific event
     * - Only shows presenters from active sessions (StatusTypeID = 1)
     * - Returns presenters sorted alphabetically by first name
     * - Automatically converts database records to Presenter objects
     * 
     * USER EXPERIENCE:
     * - Event organizers can see all speakers for their event
     * - Attendees can browse who's speaking before registering
     * - Event marketing teams can create speaker lists
     * - Event programs can show complete speaker rosters
     * 
     * TECHNICAL PROCESS:
     * 1. Connects to the database safely
     * 2. Runs a SQL query to find presenters for the event
     * 3. Converts each database result into a Presenter object
     * 4. Returns a list of all presenters found
     * 
     * @param eventId The ID of the event to find presenters for
     * @return A list of Presenter objects, or empty list if none found
     */
    public List<Presenter> getPresentersByEventId(int eventId) {
        // 
        // CREATE RESULT CONTAINER - Prepare to store found presenters
        // 
        // This creates an empty list that we'll fill with presenter information
        // as we find it in the database.
        // 
        // THINK OF IT LIKE:
        // - Getting an empty folder to collect documents
        // - Preparing a shopping cart to hold items
        // - Creating a list to write down names
        // 
        List<Presenter> presenters = new ArrayList<>();
        
        // 
        // SQL QUERY - Find presenters linked to the specific event
        // 
        // This SQL query finds all presenters who are speaking at the specified event.
        // It joins multiple tables to connect presenters to events through sessions.
        // 
        // QUERY EXPLANATION:
        // - SELECT DISTINCT p.*: Get all presenter information, avoid duplicates
        // - FROM Presenter p: Start with the presenter table
        // - INNER JOIN SessionPresenter sp: Connect presenters to sessions
        // - INNER JOIN SessionM s: Connect sessions to events
        // - WHERE s.EventID = ?: Only for the specific event we're looking for
        // - AND s.StatusTypeID = 1: Only active sessions (not cancelled)
        // - ORDER BY p.FirstName ASC: Sort alphabetically by first name
        // 
        String sql = "SELECT DISTINCT p.* FROM Presenter p " +
                    "INNER JOIN SessionPresenter sp ON p.PresenterID = sp.PresenterID " +
                    "INNER JOIN SessionM s ON sp.SessionID = s.SessionID " +
                    "WHERE s.EventID = ? AND s.StatusTypeID = 1 " +
                    "ORDER BY p.FirstName ASC";
        
        // 
        // DATABASE OPERATION - Execute the query and process results
        // 
        // This section safely connects to the database, runs the query,
        // and processes each result to create Presenter objects.
        // 
        // THINK OF IT LIKE:
        // - Opening a library catalog to search for books
        // - Looking through the results one by one
         // - Taking notes on each item found
         // 
        try (Connection conn = Db.get();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            // 
            // SET QUERY PARAMETER - Specify which event to search for
            // 
            // This replaces the "?" placeholder in the SQL query with the
            // actual event ID we want to find presenters for.
            // 
            // THINK OF IT LIKE:
            // - Filling in a blank on a form with specific information
            // - Replacing a placeholder with the actual value
            // - Specifying which conference you want speaker information for
            // 
            stmt.setInt(1, eventId);
            
            // 
            // EXECUTE QUERY - Run the search and get results
            // 
            // This runs the SQL query and returns a result set containing
            // all the presenter information that matches our search.
            // 
            // THINK OF IT LIKE:
            // - Submitting a search form and getting back results
            // - Running a search engine query and seeing the results
            // - Looking up information in a database and getting matches
            // 
            ResultSet rs = stmt.executeQuery();
            
            // 
            // PROCESS RESULTS - Convert each database record to a Presenter object
            // 
            // This loop goes through each result from the database and
            // converts it into a Presenter object that the application can use.
            // 
            // THINK OF IT LIKE:
            // - Going through a stack of business cards one by one
            // - Reading through a list of names and creating contact entries
            // - Processing each search result to extract useful information
            // 
            while (rs.next()) {
                // 
                // CONVERT DATABASE RECORD TO PRESENTER OBJECT
                // 
                // This helper method takes the raw database data and converts
                // it into a properly formatted Presenter object.
                // 
                // THINK OF IT LIKE:
                // - Converting a form submission into a contact record
                // - Taking raw data and organizing it into a structured format
                // - Transforming database information into user-friendly objects
                // 
                Presenter presenter = mapResultSetToPresenter(rs);
                
                // 
                // ADD TO RESULTS LIST - Collect all presenters found
                // 
                // This adds each presenter we found to our results list
                // so we can return all of them together.
                // 
                // THINK OF IT LIKE:
                // - Adding items to a shopping cart as you find them
                // - Collecting business cards in a folder
                // - Building a list of contacts as you discover them
                // 
                presenters.add(presenter);
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
            System.err.println("Error fetching presenters by event ID: " + e.getMessage());
            e.printStackTrace();
        }
        
        // 
        // RETURN RESULTS - Send back all presenters found
        // 
        // This returns the complete list of presenters, whether we found
        // many, few, or none at all.
        // 
        // THINK OF IT LIKE:
        // - Handing over a completed list of contacts
        // - Returning a filled shopping cart
        // - Delivering a completed report
        // 
        return presenters;
    }
    
    /**
     * getPresenterById(int presenterId) - Find a Specific Presenter
     * 
     * This method finds detailed information about one specific presenter
     * using their unique ID number. It's like looking up a specific person
     * in a contact directory.
     * 
     * WHAT IT DOES:
     * - Searches the database for a presenter with the specified ID
     * - Returns complete presenter information if found
     * - Returns null if no presenter exists with that ID
     * - Automatically converts database record to Presenter object
     * 
     * USER EXPERIENCE:
     * - Event organizers can get details about a specific speaker
     * - Attendees can view complete speaker profiles
     * - System can display presenter information on event pages
     * - Applications can look up presenter details by reference
     * 
     * TECHNICAL PROCESS:
     * 1. Connects to the database safely
     * 2. Runs a simple SQL query to find the specific presenter
     * 3. Converts the database result into a Presenter object
     * 4. Returns the presenter or null if not found
     * 
     * @param presenterId The unique ID of the presenter to find
     * @return A Presenter object if found, or null if not found
     */
    public Presenter getPresenterById(int presenterId) {
        // 
        // SQL QUERY - Find presenter by unique ID
        // 
        // This is a simple query that looks for a presenter with
        // the exact ID number we're searching for.
        // 
        // THINK OF IT LIKE:
        // - Looking up a person by their employee ID number
        // - Finding a book by its catalog number
        // - Searching for a contact by their unique reference number
        // 
        String sql = "SELECT * FROM Presenter WHERE PresenterID = ?";
        
        // 
        // DATABASE OPERATION - Execute the query and get results
        // 
        // This section safely connects to the database, runs the query,
        // and processes the result to create a Presenter object.
        // 
        // THINK OF IT LIKE:
        // - Looking up a specific entry in a phone book
        // - Searching for a particular item in a catalog
        // - Finding a specific record in a filing system
        // 
        try (Connection conn = Db.get();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            // 
            // SET QUERY PARAMETER - Specify which presenter to find
            // 
            // This replaces the "?" placeholder with the actual presenter ID
            // we want to look up.
            // 
            // THINK OF IT LIKE:
            // - Filling in a search form with a specific ID number
            // - Replacing a placeholder with the actual value
            // - Specifying exactly which person you want to find
            // 
            stmt.setInt(1, presenterId);
            
            // 
            // EXECUTE QUERY - Run the search and get the result
            // 
            // This runs the SQL query and returns a result set containing
            // the presenter information if found.
            // 
            // THINK OF IT LIKE:
            // - Submitting a search and getting back one result
            // - Looking up an entry and finding the information
            // - Searching a database and getting a match
            // 
            ResultSet rs = stmt.executeQuery();
            
            // 
            // CHECK FOR RESULTS - See if we found the presenter
            // 
            // This checks if the database returned any results for our search.
            // If we found a presenter, we convert it to an object and return it.
            // 
            // THINK OF IT LIKE:
            // - Checking if your search returned any results
            // - Seeing if you found the person you were looking for
            // - Verifying that your lookup was successful
            // 
            if (rs.next()) {
                // 
                // CONVERT DATABASE RECORD TO PRESENTER OBJECT
                // 
                // This helper method takes the raw database data and converts
                // it into a properly formatted Presenter object.
                // 
                // THINK OF IT LIKE:
                // - Converting a form submission into a contact record
                // - Taking raw data and organizing it into a structured format
                // - Transforming database information into user-friendly objects
                // 
                return mapResultSetToPresenter(rs);
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
            System.err.println("Error fetching presenter by ID: " + e.getMessage());
            e.printStackTrace();
        }
        
        // 
        // RETURN NULL IF NOT FOUND - Indicate no presenter was found
        // 
        // If we didn't find a presenter with the specified ID, we return
        // null to indicate that no such presenter exists.
        // 
        // THINK OF IT LIKE:
        // - Saying "sorry, that person doesn't exist in our system"
        // - Indicating that your search didn't find any matches
        // - Returning an empty result when nothing is found
        // 
        return null;
    }
    
    /**
     * mapResultSetToPresenter(ResultSet rs) - Convert Database Data to Presenter Object
     * 
     * This private helper method takes raw database results and converts them
     * into a properly formatted Presenter object. It's like a translator that
     * converts database language into Java language.
     * 
     * WHAT IT DOES:
     * - Takes database query results and converts them to Presenter objects
     * - Combines first and last names into a single full name
     * - Sets default values for missing information
     * - Converts database timestamps to Java date/time objects
     * - Handles null values gracefully
     * 
     * USER EXPERIENCE:
     * - Ensures presenter information is consistently formatted
     * - Provides default values when information is missing
     * - Converts technical database data into user-friendly objects
     * - Handles incomplete data gracefully
     * 
     * TECHNICAL PROCESS:
     * 1. Creates a new empty Presenter object
     * 2. Extracts data from the database result set
     * 3. Combines and formats the data appropriately
     * 4. Sets default values for missing information
     * 5. Returns the complete Presenter object
     * 
     * @param rs The database result set containing presenter information
     * @return A fully populated Presenter object
     * @throws SQLException If there's an error reading the database data
     */
    private Presenter mapResultSetToPresenter(ResultSet rs) throws SQLException {
        // 
        // CREATE EMPTY PRESENTER OBJECT - Start with a blank presenter
        // 
        // This creates a new, empty Presenter object that we'll fill
        // with information from the database.
        // 
        // THINK OF IT LIKE:
        // - Getting a blank contact form to fill out
        // - Creating an empty profile to populate with information
        // - Starting with a blank canvas to paint on
        // 
        Presenter presenter = new Presenter();
        
        // 
        // SET BASIC IDENTIFICATION - Assign the unique presenter ID
        // 
        // This sets the unique identifier that makes this presenter
        // distinct from all others in the system.
        // 
        // THINK OF IT LIKE:
        // - Assigning a serial number to a product
        // - Giving a unique ID to a new employee
        // - Creating a reference number for a contact
        // 
        presenter.setPresenterId(rs.getInt("PresenterID"));
        
        // 
        // COMBINE NAME FIELDS - Create a full name from first and last names
        // 
        // This section takes the separate first and last name fields from
        // the database and combines them into a single, full name for display.
        // 
        // THINK OF IT LIKE:
        // - Taking two separate name tags and combining them into one
        // - Merging first and last names into a complete name
        // - Creating a display name from separate name components
        // 
        String firstName = rs.getString("FirstName");
        String lastName = rs.getString("LastName");
        
        // 
        // SAFE NAME COMBINATION - Handle missing or null names gracefully
        // 
        // This safely combines the names even if one or both are missing,
        // preventing errors and ensuring we always have a valid name string.
        // 
        // THINK OF IT LIKE:
        // - Carefully combining ingredients even if some are missing
        // - Building a complete picture from available pieces
        // - Creating a readable name even with incomplete information
        // 
        String fullName = (firstName != null ? firstName : "") + " " + (lastName != null ? lastName : "");
        presenter.setName(fullName.trim());
        
        // 
        // SET DEFAULT VALUES - Provide sensible defaults for missing information
        // 
        // This sets default values for fields that might not have data
        // in the database, ensuring the presenter object is complete.
        // 
        // THINK OF IT LIKE:
        // - Filling in missing information with reasonable defaults
        // - Providing placeholder text when data is missing
        // - Ensuring a form is complete even with missing fields
        // 
        presenter.setTitle("Speaker"); // Default title
        presenter.setCompany(rs.getString("Bio")); // Use Bio as company for now
        presenter.setBio(rs.getString("Bio"));
        presenter.setImageUrl(null);
        presenter.setEmail(rs.getString("Email"));
        presenter.setLinkedinUrl(null);
        presenter.setTwitterUrl(null);
        
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
        // SET CREATION TIMESTAMP - When the presenter was first added
        // 
        // This sets when the presenter record was originally created in the system.
        // If no timestamp exists, we use the current time as a default.
        // 
        // THINK OF IT LIKE:
        // - Recording when a contact was first added to your address book
        // - Stamping a creation date on a document
        // - Marking when something was first entered into a system
        // 
        if (rs.getTimestamp("CreatedAt") != null) {
            presenter.setCreatedAt(rs.getTimestamp("CreatedAt").toLocalDateTime());
        } else {
            presenter.setCreatedAt(java.time.LocalDateTime.now());
        }
        
        // 
        // SET UPDATE TIMESTAMP - When the presenter was last modified
        // 
        // This sets when the presenter information was last changed.
        // If no timestamp exists, we use the current time as a default.
        // 
        // THINK OF IT LIKE:
        // - Recording when a contact's information was last updated
        // - Stamping a "last modified" date on a document
        // - Marking when something was last changed in a system
        // 
        if (rs.getTimestamp("UpdatedAt") != null) {
            presenter.setUpdatedAt(rs.getTimestamp("UpdatedAt").toLocalDateTime());
        } else {
            presenter.setUpdatedAt(java.time.LocalDateTime.now());
        }
        
        // 
        // RETURN COMPLETE PRESENTER OBJECT - Send back the fully populated presenter
        // 
        // This returns the Presenter object with all its fields properly
        // set with information from the database.
        // 
        // THINK OF IT LIKE:
        // - Handing over a completed contact form
        // - Delivering a finished profile
        // - Returning a complete record with all information filled in
        // 
        return presenter;
    }
} 