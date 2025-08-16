package com.eventra.dao;

import com.eventra.Db;
import java.sql.*;

/**
 * Dashboard DAO (DashboardDAO.java)
 * 
 * This class handles all the database operations related to dashboard metrics
 * and system overview data in the Eventra application. Think of it like a
 * "data analyst" that gathers statistics and metrics to show how the system
 * is performing.
 * 
 * WHAT THIS CLASS DOES:
 * - Counts total events in the system (draft and published)
 * - Tracks total users and their activity status
 * - Monitors event registrations and attendance
 * - Provides system overview metrics and statistics
 * - Supports dashboard displays and reporting
 * 
 * KEY CONCEPTS TO UNDERSTAND:
 * - Dashboard Metrics: Key numbers that show system health and activity
 * - Data Aggregation: Collecting and counting records from different tables
 * - Status Filtering: Only counting records with specific status values
 * - System Monitoring: Tracking how many items exist in each category
 * - Performance Indicators: Numbers that help understand system usage
 * 
 * BUSINESS VALUE:
 * - Provides administrators with system overview and health indicators
 * - Enables monitoring of platform growth and activity levels
 * - Supports decision-making with concrete data and statistics
 * - Helps identify trends in event creation and user engagement
 * - Creates transparency about system usage and performance
 * 
 * REAL-WORLD ANALOGY:
 * - Like a dashboard in a car that shows speed, fuel, and engine status
 * - Like a business report that shows key performance indicators
 * - Like a weather station that tracks various environmental metrics
 * - Like a store inventory system that counts products and sales
 * - Like a website analytics tool that tracks visitor statistics
 */
public class DashboardDAO {
    
    /**
     * getTotalEventsCount() - Count Total Events in System
     * 
     * This method counts how many events exist in the system, including
     * both draft events (being planned) and published events (available
     * to users). Think of it like counting how many events are in the
     * system's calendar.
     * 
     * WHAT IT DOES:
     * - Queries the EventM table for event records
     * - Filters for events with status 1 (Draft) or 2 (Published)
     * - Returns the total count of qualifying events
     * - Handles database errors gracefully
     * 
     * USER EXPERIENCE:
     * - Administrators can see how many events exist in the system
     * - Dashboard displays show current event count
     * - System activity levels are visible at a glance
     * - Growth trends can be monitored over time
     * 
     * TECHNICAL PROCESS:
     * 1. Connect to database
     * 2. Execute COUNT query on EventM table
     * 3. Filter for specific status types (Draft and Published)
     * 4. Return count or 0 if error occurs
     * 
     * BUSINESS VALUE:
     * - Shows platform activity and event creation levels
     * - Helps administrators understand system usage
     * - Supports capacity planning and resource allocation
     * - Enables monitoring of event management activity
     * 
     * @return Total count of draft and published events, or 0 if error occurs
     */
    public static int getTotalEventsCount() {
        // 
        // QUERY FOR EVENT COUNT - Count events with specific statuses
        // 
        // This SQL query counts events that are either in draft status
        // (being planned) or published status (available to users).
        // Think of it like counting how many events are active in the system.
        // 
        // THINK OF IT LIKE:
        // - Counting how many events are in a calendar
        // - Tallying up all the events in a system
        // - Getting a total count of available events
        // 
        String sql = "SELECT COUNT(*) FROM EventM WHERE StatusTypeID IN (1, 2)"; // Draft and Published
        
        try (Connection conn = Db.get();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            // 
            // EXECUTE COUNT QUERY - Get the total number
            // 
            // This executes the query and gets the result. Think of it like
            // running a count operation to see how many items exist.
            // 
            // THINK OF IT LIKE:
            // - Running a tally of all events
            // - Counting items in a list
            // - Getting a total number from a database
            // 
            ResultSet rs = stmt.executeQuery();
            
            // 
            // EXTRACT COUNT RESULT - Get the actual number
            // 
            // If the query returned a result, we extract the count value.
            // Think of it like reading the number from a counter display.
            // 
            // THINK OF IT LIKE:
            // - Reading the result from a calculation
            // - Getting the answer from a count operation
            // - Extracting the total number found
            // 
            if (rs.next()) {
                return rs.getInt(1);
            }
            
        } catch (SQLException e) {
            // 
            // DATABASE ERROR - Handle connection or query problems
            // 
            // If something goes wrong with the database query, we log the error
            // and return 0. This prevents the system from crashing and provides
            // feedback about what went wrong.
            // 
            // THINK OF IT LIKE:
            // - A system error that needs to be reported
            // - A problem that prevents getting the count
            // - An issue that needs to be logged for debugging
            // 
            System.err.println("Error getting total events count: " + e.getMessage());
            e.printStackTrace();
        }
        
        // 
        // RETURN DEFAULT VALUE - Provide fallback when errors occur
        // 
        // If anything goes wrong, we return 0 as a safe default value.
        // This ensures the system continues to work even if there are
        // database problems.
        // 
        // THINK OF IT LIKE:
        // - Providing a safe fallback value
        // - Ensuring the system doesn't crash
        // - Giving a reasonable default when data is unavailable
        // 
        return 0;
    }
    
    /**
     * getTotalUsersCount() - Count Total Active Users in System
     * 
     * This method counts how many active users exist in the system.
     * Only users with status 1 (Active) are counted, excluding
     * inactive, suspended, or deleted accounts. Think of it like
     * counting how many people are currently using the platform.
     * 
     * WHAT IT DOES:
     * - Queries the UserM table for user records
     * - Filters for users with status 1 (Active only)
     * - Returns the total count of active users
     * - Handles database errors gracefully
     * 
     * USER EXPERIENCE:
     * - Administrators can see how many active users exist
     * - Dashboard displays show current user base size
     * - User growth trends can be monitored
     * - System adoption levels are visible
     * 
     * TECHNICAL PROCESS:
     * 1. Connect to database
     * 2. Execute COUNT query on UserM table
     * 3. Filter for active users only (StatusTypeID = 1)
     * 4. Return count or 0 if error occurs
     * 
     * BUSINESS VALUE:
     * - Shows platform adoption and user base size
     * - Helps administrators understand system usage
     * - Supports user growth monitoring and planning
     * - Enables capacity planning for user support
     * 
     * @return Total count of active users, or 0 if error occurs
     */
    public static int getTotalUsersCount() {
        // 
        // QUERY FOR USER COUNT - Count active users only
        // 
        // This SQL query counts users who have active status in the system.
        // Only users with StatusTypeID = 1 are counted, excluding inactive
        // or suspended accounts. Think of it like counting how many people
        // are currently using the platform.
        // 
        // THINK OF IT LIKE:
        // - Counting active members in a club
        // - Tallying current users of a service
        // - Getting the number of active accounts
        // 
        String sql = "SELECT COUNT(*) FROM UserM WHERE StatusTypeID = 1"; // Active users only
        
        try (Connection conn = Db.get();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            // 
            // EXECUTE COUNT QUERY - Get the total number
            // 
            // This executes the query and gets the result. Think of it like
            // running a count operation to see how many active users exist.
            // 
            // THINK OF IT LIKE:
            // - Running a tally of all active users
            // - Counting active members in a system
            // - Getting a total number from a database
            // 
            ResultSet rs = stmt.executeQuery();
            
            // 
            // EXTRACT COUNT RESULT - Get the actual number
            // 
            // If the query returned a result, we extract the count value.
            // Think of it like reading the number from a user counter.
            // 
            // THINK OF IT LIKE:
            // - Reading the result from a calculation
            // - Getting the answer from a count operation
            // - Extracting the total number found
            // 
            if (rs.next()) {
                return rs.getInt(1);
            }
            
        } catch (SQLException e) {
            // 
            // DATABASE ERROR - Handle connection or query problems
            // 
            // If something goes wrong with the database query, we log the error
            // and return 0. This prevents the system from crashing and provides
            // feedback about what went wrong.
            // 
            // THINK OF IT LIKE:
            // - A system error that needs to be reported
            // - A problem that prevents getting the count
            // - An issue that needs to be logged for debugging
            // 
            System.err.println("Error getting total users count: " + e.getMessage());
            e.printStackTrace();
        }
        
        // 
        // RETURN DEFAULT VALUE - Provide fallback when errors occur
        // 
        // If anything goes wrong, we return 0 as a safe default value.
        // This ensures the system continues to work even if there are
        // database problems.
        // 
        // THINK OF IT LIKE:
        // - Providing a safe fallback value
        // - Ensuring the system doesn't crash
        // - Giving a reasonable default when data is unavailable
        // 
        return 0;
    }
    
    /**
     * getTotalRegistrationsCount() - Count Total Event Registrations
     * 
     * This method counts how many event registrations exist in the system.
     * Only registrations with status 1 (Pending) or 2 (Confirmed) are
     * counted, excluding cancelled or rejected registrations. Think of
     * it like counting how many people have signed up for events.
     * 
     * WHAT IT DOES:
     * - Queries the Registration table for registration records
     * - Filters for registrations with status 1 (Pending) or 2 (Confirmed)
     * - Returns the total count of active registrations
     * - Handles database errors gracefully
     * 
     * USER EXPERIENCE:
     * - Administrators can see how many registrations exist
     * - Dashboard displays show current registration activity
     * - Event popularity can be monitored
     * - System engagement levels are visible
     * 
     * TECHNICAL PROCESS:
     * 1. Connect to database
     * 2. Execute COUNT query on Registration table
     * 3. Filter for pending and confirmed registrations
     * 4. Return count or 0 if error occurs
     * 
     * BUSINESS VALUE:
     * - Shows event engagement and registration activity
     * - Helps administrators understand user participation
     * - Supports event planning and capacity management
     * - Enables monitoring of platform engagement levels
     * 
     * @return Total count of pending and confirmed registrations, or 0 if error occurs
     */
    public static int getTotalRegistrationsCount() {
        // 
        // QUERY FOR REGISTRATION COUNT - Count active registrations
        // 
        // This SQL query counts registrations that are either pending
        // (waiting for confirmation) or confirmed (approved). Think of
        // it like counting how many people have signed up for events.
        // 
        // THINK OF IT LIKE:
        // - Counting confirmed event attendees
        // - Tallying up event registrations
        // - Getting the number of people signed up for events
        // 
        String sql = "SELECT COUNT(*) FROM Registration WHERE RegistrationStatusTypeID IN (1, 2)"; // Pending and Confirmed
        
        try (Connection conn = Db.get();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            // 
            // EXECUTE COUNT QUERY - Get the total number
            // 
            // This executes the query and gets the result. Think of it like
            // running a count operation to see how many registrations exist.
            // 
            // THINK OF IT LIKE:
            // - Running a tally of all registrations
            // - Counting event sign-ups in a system
            // - Getting a total number from a database
            // 
            ResultSet rs = stmt.executeQuery();
            
            // 
            // EXTRACT COUNT RESULT - Get the actual number
            // 
            // If the query returned a result, we extract the count value.
            // Think of it like reading the number from a registration counter.
            // 
            // THINK OF IT LIKE:
            // - Reading the result from a calculation
            // - Getting the answer from a count operation
            // - Extracting the total number found
            // 
            if (rs.next()) {
                return rs.getInt(1);
            }
            
        } catch (SQLException e) {
            // 
            // DATABASE ERROR - Handle connection or query problems
            // 
            // If something goes wrong with the database query, we log the error
            // and return 0. This prevents the system from crashing and provides
            // feedback about what went wrong.
            // 
            // THINK OF IT LIKE:
            // - A system error that needs to be reported
            // - A problem that prevents getting the count
            // - An issue that needs to be logged for debugging
            // 
            System.err.println("Error getting total registrations count: " + e.getMessage());
            e.printStackTrace();
        }
        
        // 
        // RETURN DEFAULT VALUE - Provide fallback when errors occur
        // 
        // If anything goes wrong, we return 0 as a safe default value.
        // This ensures the system continues to work even if there are
        // database problems.
        // 
        // THINK OF IT LIKE:
        // - Providing a safe fallback value
        // - Ensuring the system doesn't crash
        // - Giving a reasonable default when data is unavailable
        // 
        return 0;
    }
    
    /**
     * getTotalAttendeesCount() - Count Total Active Attendees
     * 
     * This method counts how many active attendees exist in the system.
     * Only attendees with status 1 (Active) are counted, excluding
     * inactive or suspended attendee accounts. Think of it like
     * counting how many people are registered to attend events.
     * 
     * WHAT IT DOES:
     * - Queries the Attendee table for attendee records
     * - Filters for attendees with status 1 (Active only)
     * - Returns the total count of active attendees
     * - Handles database errors gracefully
     * 
     * USER EXPERIENCE:
     * - Administrators can see how many active attendees exist
     * - Dashboard displays show current attendee base size
     * - Attendee growth trends can be monitored
     * - Event participation levels are visible
     * 
     * TECHNICAL PROCESS:
     * 1. Connect to database
     * 2. Execute COUNT query on Attendee table
     * 3. Filter for active attendees only (StatusTypeID = 1)
     * 4. Return count or 0 if error occurs
     * 
     * BUSINESS VALUE:
     * - Shows event participation and attendee base size
     * - Helps administrators understand event engagement
     * - Supports attendee growth monitoring and planning
     * - Enables capacity planning for event management
     * 
     * @return Total count of active attendees, or 0 if error occurs
     */
    public static int getTotalAttendeesCount() {
        // 
        // QUERY FOR ATTENDEE COUNT - Count active attendees only
        // 
        // This SQL query counts attendees who have active status in the system.
        // Only attendees with StatusTypeID = 1 are counted, excluding inactive
        // or suspended accounts. Think of it like counting how many people
        // are currently registered to attend events.
        // 
        // THINK OF IT LIKE:
        // - Counting active event participants
        // - Tallying current attendees in a system
        // - Getting the number of active attendee accounts
        // 
        String sql = "SELECT COUNT(*) FROM Attendee WHERE StatusTypeID = 1"; // Active attendees only
        
        try (Connection conn = Db.get();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            // 
            // EXECUTE COUNT QUERY - Get the total number
            // 
            // This executes the query and gets the result. Think of it like
            // running a count operation to see how many active attendees exist.
            // 
            // THINK OF IT LIKE:
            // - Running a tally of all active attendees
            // - Counting active participants in a system
            // - Getting a total number from a database
            // 
            ResultSet rs = stmt.executeQuery();
            
            // 
            // EXTRACT COUNT RESULT - Get the actual number
            // 
            // If the query returned a result, we extract the count value.
            // Think of it like reading the number from an attendee counter.
            // 
            // THINK OF IT LIKE:
            // - Reading the result from a calculation
            // - Getting the answer from a count operation
            // - Extracting the total number found
            // 
            if (rs.next()) {
                return rs.getInt(1);
            }
            
        } catch (SQLException e) {
            // 
            // DATABASE ERROR - Handle connection or query problems
            // 
            // If something goes wrong with the database query, we log the error
            // and return 0. This prevents the system from crashing and provides
            // feedback about what went wrong.
            // 
            // THINK OF IT LIKE:
            // - A system error that needs to be reported
            // - A problem that prevents getting the count
            // - An issue that needs to be logged for debugging
            // 
            System.err.println("Error getting total attendees count: " + e.getMessage());
            e.printStackTrace();
        }
        
        // 
        // RETURN DEFAULT VALUE - Provide fallback when errors occur
        // 
        // If anything goes wrong, we return 0 as a safe default value.
        // This ensures the system continues to work even if there are
        // database problems.
        // 
        // THINK OF IT LIKE:
        // - Providing a safe fallback value
        // - Ensuring the system doesn't crash
        // - Giving a reasonable default when data is unavailable
        // 
        return 0;
    }
    
    /**
     * getRegistrationsThisMonth() - Count Registrations for Current Month
     * 
     * This method counts how many event registrations were created in the
     * current month. Only registrations with status 1 (Pending) or 2 (Confirmed)
     * are counted, excluding cancelled or rejected registrations. Think of
     * it like counting how many people signed up for events in the last 30 days.
     * 
     * WHAT IT DOES:
     * - Queries the Registration table for registration records
     * - Filters for registrations created in the current month
     * - Returns the total count of registrations for the current month
     * - Handles database errors gracefully
     * 
     * USER EXPERIENCE:
     * - Administrators can see how many registrations were created in the current month
     * - Dashboard displays show current month's registration activity
     * - Monthly registration trends can be monitored
     * - System engagement levels are visible
     * 
     * TECHNICAL PROCESS:
     * 1. Connect to database
     * 2. Execute COUNT query on Registration table
     * 3. Filter for registrations created in the current month
     * 4. Return count or 0 if error occurs
     * 
     * BUSINESS VALUE:
     * - Shows monthly registration activity and trends
     * - Helps administrators understand user engagement over time
     * - Supports monthly planning and capacity management
     * - Enables monitoring of platform engagement levels
     * 
     * @return Total count of registrations for the current month, or 0 if error occurs
     */
    public static int getRegistrationsThisMonth() {
        // 
        // QUERY FOR REGISTRATION COUNT - Count registrations for current month
        // 
        // This SQL query counts registrations that were created in the current month.
        // Think of it like counting how many people signed up for events in the last 30 days.
        // 
        // THINK OF IT LIKE:
        // - Counting registrations from the last 30 days
        // - Tallying up registrations for the current month
        // - Getting the number of registrations in the last month
        // 
        String sql = "SELECT COUNT(*) FROM Registration " +
                    "WHERE MONTH(CreatedAt) = MONTH(CURRENT_DATE) " +
                    "AND YEAR(CreatedAt) = YEAR(CURRENT_DATE) " +
                    "AND RegistrationStatusTypeID IN (1, 2)"; // Pending and Confirmed
        
        try (Connection conn = Db.get();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            // 
            // EXECUTE COUNT QUERY - Get the total number
            // 
            // This executes the query and gets the result. Think of it like
            // running a count operation to see how many registrations exist.
            // 
            // THINK OF IT LIKE:
            // - Running a tally of all registrations
            // - Counting event sign-ups in a system
            // - Getting a total number from a database
            // 
            ResultSet rs = stmt.executeQuery();
            
            // 
            // EXTRACT COUNT RESULT - Get the actual number
            // 
            // If the query returned a result, we extract the count value.
            // Think of it like reading the number from a registration counter.
            // 
            // THINK OF IT LIKE:
            // - Reading the result from a calculation
            // - Getting the answer from a count operation
            // - Extracting the total number found
            // 
            if (rs.next()) {
                return rs.getInt(1);
            }
            
        } catch (SQLException e) {
            // 
            // DATABASE ERROR - Handle connection or query problems
            // 
            // If something goes wrong with the database query, we log the error
            // and return 0. This prevents the system from crashing and provides
            // feedback about what went wrong.
            // 
            // THINK OF IT LIKE:
            // - A system error that needs to be reported
            // - A problem that prevents getting the count
            // - An issue that needs to be logged for debugging
            // 
            System.err.println("Error getting registrations this month: " + e.getMessage());
            e.printStackTrace();
        }
        
        // 
        // RETURN DEFAULT VALUE - Provide fallback when errors occur
        // 
        // If anything goes wrong, we return 0 as a safe default value.
        // This ensures the system continues to work even if there are
        // database problems.
        // 
        // THINK OF IT LIKE:
        // - Providing a safe fallback value
        // - Ensuring the system doesn't crash
        // - Giving a reasonable default when data is unavailable
        // 
        return 0;
    }
    
    /**
     * getActiveEventsCount() - Count Active Events (Published)
     * 
     * This method counts how many events are currently published and available
     * to users. Only events with status 2 (Published) are counted, excluding
     * draft or cancelled events. Think of it like counting how many events
     * are currently open for registration.
     * 
     * WHAT IT DOES:
     * - Queries the EventM table for event records
     * - Filters for events with status 2 (Published only)
     * - Returns the total count of published events
     * - Handles database errors gracefully
     * 
     * USER EXPERIENCE:
     * - Administrators can see how many active events exist
     * - Dashboard displays show current open event count
     * - Event availability can be monitored
     * - System engagement levels are visible
     * 
     * TECHNICAL PROCESS:
     * 1. Connect to database
     * 2. Execute COUNT query on EventM table
     * 3. Filter for published events only (StatusTypeID = 2)
     * 4. Return count or 0 if error occurs
     * 
     * BUSINESS VALUE:
     * - Shows platform event availability and engagement
     * - Helps administrators understand system usage
     * - Supports event availability monitoring
     * - Enables monitoring of platform engagement levels
     * 
     * @return Total count of published events, or 0 if error occurs
     */
    public static int getActiveEventsCount() {
        // 
        // QUERY FOR ACTIVE EVENT COUNT - Count published events only
        // 
        // This SQL query counts events that are currently published and available
        // to users. Only events with StatusTypeID = 2 are counted, excluding
        // draft or cancelled events. Think of it like counting how many events
        // are currently open for registration.
        // 
        // THINK OF IT LIKE:
        // - Counting how many events are currently open for registration
        // - Tallying up all the events that are published
        // - Getting the number of events that are available
        // 
        String sql = "SELECT COUNT(*) FROM EventM WHERE StatusTypeID = 2"; // Published events
        
        try (Connection conn = Db.get();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            // 
            // EXECUTE COUNT QUERY - Get the total number
            // 
            // This executes the query and gets the result. Think of it like
            // running a count operation to see how many published events exist.
            // 
            // THINK OF IT LIKE:
            // - Running a tally of all published events
            // - Counting events that are available
            // - Getting a total number from a database
            // 
            ResultSet rs = stmt.executeQuery();
            
            // 
            // EXTRACT COUNT RESULT - Get the actual number
            // 
            // If the query returned a result, we extract the count value.
            // Think of it like reading the number from an event counter.
            // 
            // THINK OF IT LIKE:
            // - Reading the result from a calculation
            // - Getting the answer from a count operation
            // - Extracting the total number found
            // 
            if (rs.next()) {
                return rs.getInt(1);
            }
            
        } catch (SQLException e) {
            // 
            // DATABASE ERROR - Handle connection or query problems
            // 
            // If something goes wrong with the database query, we log the error
            // and return 0. This prevents the system from crashing and provides
            // feedback about what went wrong.
            // 
            // THINK OF IT LIKE:
            // - A system error that needs to be reported
            // - A problem that prevents getting the count
            // - An issue that needs to be logged for debugging
            // 
            System.err.println("Error getting active events count: " + e.getMessage());
            e.printStackTrace();
        }
        
        // 
        // RETURN DEFAULT VALUE - Provide fallback when errors occur
        // 
        // If anything goes wrong, we return 0 as a safe default value.
        // This ensures the system continues to work even if there are
        // database problems.
        // 
        // THINK OF IT LIKE:
        // - Providing a safe fallback value
        // - Ensuring the system doesn't crash
        // - Giving a reasonable default when data is unavailable
        // 
        return 0;
    }
    
    /**
     * getDashboardMetrics() - Get Comprehensive System Overview Metrics
     * 
     * This method gathers all the key metrics and statistics from the system
     * to provide a comprehensive overview of its current state. It combines
     * data from different tables to give a holistic view.
     * 
     * WHAT IT DOES:
     * - Calls getActiveEventsCount() to get the total number of published events
     * - Calls getTotalUsersCount() to get the total number of active users
     * - Calls getRegistrationsThisMonth() to get the total number of registrations for the current month
     * - Calls getTotalAttendeesCount() to get the total number of active attendees
     * - Combines all these metrics into a single DashboardMetrics object
     * - Handles database errors gracefully
     * 
     * USER EXPERIENCE:
     * - Administrators can see a complete picture of the system's health
     * - Dashboard displays show all major metrics in one place
     * - System overview is available at a glance
     * - Growth trends and activity levels are visible
     * 
     * TECHNICAL PROCESS:
     * 1. Connect to database
     * 2. Call getActiveEventsCount()
     * 3. Call getTotalUsersCount()
     * 4. Call getRegistrationsThisMonth()
     * 5. Call getTotalAttendeesCount()
     * 6. Combine results into a DashboardMetrics object
     * 7. Return the DashboardMetrics object
     * 
     * BUSINESS VALUE:
     * - Provides a single source of truth for system health
     * - Enables administrators to monitor all aspects of the system
     * - Supports decision-making with concrete data
     * - Helps identify trends and patterns in system usage
     * - Creates transparency about system performance
     * 
     * @return A DashboardMetrics object containing all system overview metrics
     */
    public static DashboardMetrics getDashboardMetrics() {
        DashboardMetrics metrics = new DashboardMetrics();
        
        try {
            metrics.totalEvents = getActiveEventsCount();
            metrics.totalUsers = getTotalUsersCount();
            metrics.totalRegistrations = getRegistrationsThisMonth();
            metrics.totalAttendees = getTotalAttendeesCount();
            
            System.out.println("Dashboard metrics loaded: Events=" + metrics.totalEvents + 
                             ", Users=" + metrics.totalUsers + 
                             ", Registrations=" + metrics.totalRegistrations + 
                             ", Attendees=" + metrics.totalAttendees);
            
        } catch (Exception e) {
            System.err.println("Error loading dashboard metrics: " + e.getMessage());
            e.printStackTrace();
        }
        
        return metrics;
    }
    
    // ADMIN-SPECIFIC METRICS (based on CreatedByUserID)
    
    /**
     * getMyEventsCount(int adminUserId) - Count Events Created by Specific Admin
     * 
     * This method counts how many events were created by a specific administrator
     * user. Only events with status 1 (Draft) or 2 (Published) are counted,
     * excluding cancelled or rejected events. Think of it like counting how
     * many events an administrator has planned or published.
     * 
     * WHAT IT DOES:
     * - Queries the EventM table for event records
     * - Filters for events created by the specific admin user
     * - Returns the total count of events created by the admin
     * - Handles database errors gracefully
     * 
     * USER EXPERIENCE:
     * - Administrators can see how many events they have created
     * - Dashboard displays show current event count for the admin
     * - Admin activity levels are visible
     * - System usage by admins is transparent
     * 
     * TECHNICAL PROCESS:
     * 1. Connect to database
     * 2. Execute COUNT query on EventM table
     * 3. Filter for events created by the specific admin user
     * 4. Return count or 0 if error occurs
     * 
     * BUSINESS VALUE:
     * - Shows admin event creation activity
     * - Helps administrators understand their own activity
     * - Supports admin growth monitoring and planning
     * - Enables capacity planning for admin resources
     * 
     * @param adminUserId The ID of the administrator user
     * @return Total count of events created by the admin, or 0 if error occurs
     */
    public static int getMyEventsCount(int adminUserId) {
        // 
        // QUERY FOR MY EVENTS COUNT - Count events created by specific admin
        // 
        // This SQL query counts events that were created by a specific administrator
        // user. Only events with status 1 (Draft) or 2 (Published) are counted,
        // excluding cancelled or rejected events. Think of it like counting how
        // many events an administrator has planned or published.
        // 
        // THINK OF IT LIKE:
        // - Counting how many events an administrator has created
        // - Tallying up all the events planned or published by an admin
        // - Getting the number of events created by a specific user
        // 
        String sql = "SELECT COUNT(*) FROM EventM WHERE CreatedByUserID = ? AND StatusTypeID IN (1, 2)"; // Draft and Published
        
        try (Connection conn = Db.get();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            // 
            // SET PARAMETER - Pass the admin user ID
            // 
            // This sets the parameter for the admin user ID in the query.
            // Think of it like passing the ID of the user whose events we are counting.
            // 
            // THINK OF IT LIKE:
            // - Passing the ID of the user we are interested in
            // - Setting the ID for the query
            // - Passing the user's identifier
            // 
            stmt.setInt(1, adminUserId);
            ResultSet rs = stmt.executeQuery();
            
            // 
            // EXTRACT COUNT RESULT - Get the actual number
            // 
            // If the query returned a result, we extract the count value.
            // Think of it like reading the number from an event counter.
            // 
            // THINK OF IT LIKE:
            // - Reading the result from a calculation
            // - Getting the answer from a count operation
            // - Extracting the total number found
            // 
            if (rs.next()) {
                return rs.getInt(1);
            }
            
        } catch (SQLException e) {
            // 
            // DATABASE ERROR - Handle connection or query problems
            // 
            // If something goes wrong with the database query, we log the error
            // and return 0. This prevents the system from crashing and provides
            // feedback about what went wrong.
            // 
            // THINK OF IT LIKE:
            // - A system error that needs to be reported
            // - A problem that prevents getting the count
            // - An issue that needs to be logged for debugging
            // 
            System.err.println("Error getting my events count for admin " + adminUserId + ": " + e.getMessage());
            e.printStackTrace();
        }
        
        // 
        // RETURN DEFAULT VALUE - Provide fallback when errors occur
        // 
        // If anything goes wrong, we return 0 as a safe default value.
        // This ensures the system continues to work even if there are
        // database problems.
        // 
        // THINK OF IT LIKE:
        // - Providing a safe fallback value
        // - Ensuring the system doesn't crash
        // - Giving a reasonable default when data is unavailable
        // 
        return 0;
    }
    
    /**
     * getRegistrationsForMyEvents(int adminUserId) - Count Registrations for Events Created by Specific Admin
     * 
     * This method counts how many event registrations were created for events
     * that were created by a specific administrator user. Only registrations
     * with status 1 (Pending) or 2 (Confirmed) are counted, excluding cancelled
     * or rejected registrations. Think of it like counting how many people
     * signed up for events that an administrator has created.
     * 
     * WHAT IT DOES:
     * - Queries the Registration table for registration records
     * - Joins with the EventM table to filter for events created by the specific admin
     * - Filters for registrations created for events created by the specific admin
     * - Returns the total count of registrations for the specific admin's events
     * - Handles database errors gracefully
     * 
     * USER EXPERIENCE:
     * - Administrators can see how many registrations they have received for their events
     * - Dashboard displays show current registration activity for the admin's events
     * - Admin event engagement can be monitored
     * - System usage by admins is transparent
     * 
     * TECHNICAL PROCESS:
     * 1. Connect to database
     * 2. Execute COUNT query on Registration table
     * 3. Join with EventM table to filter for events created by the specific admin
     * 4. Filter for registrations created for the specific admin's events
     * 5. Return count or 0 if error occurs
     * 
     * BUSINESS VALUE:
     * - Shows admin event registration activity
     * - Helps administrators understand their own event engagement
     * - Supports admin growth monitoring and planning
     * - Enables capacity planning for admin resources
     * 
     * @param adminUserId The ID of the administrator user
     * @return Total count of registrations for the specific admin's events, or 0 if error occurs
     */
    public static int getRegistrationsForMyEvents(int adminUserId) {
        // 
        // QUERY FOR REGISTRATIONS FOR MY EVENTS - Count registrations for events created by specific admin
        // 
        // This SQL query counts registrations that were created for events
        // that were created by a specific administrator user. Only registrations
        // with status 1 (Pending) or 2 (Confirmed) are counted, excluding cancelled
        // or rejected registrations. Think of it like counting how many people
        // signed up for events that an administrator has created.
        // 
        // THINK OF IT LIKE:
        // - Counting registrations for events an administrator has created
        // - Tallying up registrations for the specific admin's events
        // - Getting the number of registrations for a specific user's events
        // 
        String sql = "SELECT COUNT(*) FROM Registration r " +
                    "INNER JOIN EventM e ON r.EventID = e.EventID " +
                    "WHERE e.CreatedByUserID = ? AND r.RegistrationStatusTypeID IN (1, 2)"; // Pending and Confirmed
        
        try (Connection conn = Db.get();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            // 
            // SET PARAMETER - Pass the admin user ID
            // 
            // This sets the parameter for the admin user ID in the query.
            // Think of it like passing the ID of the user whose registrations we are counting.
            // 
            // THINK OF IT LIKE:
            // - Passing the ID of the user we are interested in
            // - Setting the ID for the query
            // - Passing the user's identifier
            // 
            stmt.setInt(1, adminUserId);
            ResultSet rs = stmt.executeQuery();
            
            // 
            // EXTRACT COUNT RESULT - Get the actual number
            // 
            // If the query returned a result, we extract the count value.
            // Think of it like reading the number from a registration counter.
            // 
            // THINK OF IT LIKE:
            // - Reading the result from a calculation
            // - Getting the answer from a count operation
            // - Extracting the total number found
            // 
            if (rs.next()) {
                return rs.getInt(1);
            }
            
        } catch (SQLException e) {
            // 
            // DATABASE ERROR - Handle connection or query problems
            // 
            // If something goes wrong with the database query, we log the error
            // and return 0. This prevents the system from crashing and provides
            // feedback about what went wrong.
            // 
            // THINK OF IT LIKE:
            // - A system error that needs to be reported
            // - A problem that prevents getting the count
            // - An issue that needs to be logged for debugging
            // 
            System.err.println("Error getting registrations for admin " + adminUserId + " events: " + e.getMessage());
            e.printStackTrace();
        }
        
        // 
        // RETURN DEFAULT VALUE - Provide fallback when errors occur
        // 
        // If anything goes wrong, we return 0 as a safe default value.
        // This ensures the system continues to work even if there are
        // database problems.
        // 
        // THINK OF IT LIKE:
        // - Providing a safe fallback value
        // - Ensuring the system doesn't crash
        // - Giving a reasonable default when data is unavailable
        // 
        return 0;
    }
    
    /**
     * getActiveEventsForAdmin(int adminUserId) - Count Active Events Created by Specific Admin
     * 
     * This method counts how many events were created by a specific administrator
     * user and are currently published. Only events with status 2 (Published)
     * are counted, excluding draft or cancelled events. Think of it like
     * counting how many events an administrator has published.
     * 
     * WHAT IT DOES:
     * - Queries the EventM table for event records
     * - Filters for events created by the specific admin user
     * - Filters for published events only (StatusTypeID = 2)
     * - Returns the total count of published events created by the admin
     * - Handles database errors gracefully
     * 
     * USER EXPERIENCE:
     * - Administrators can see how many events they have published
     * - Dashboard displays show current published event count for the admin
     * - Admin event activity can be monitored
     * - System usage by admins is transparent
     * 
     * TECHNICAL PROCESS:
     * 1. Connect to database
     * 2. Execute COUNT query on EventM table
     * 3. Filter for events created by the specific admin user
     * 4. Filter for published events only (StatusTypeID = 2)
     * 5. Return count or 0 if error occurs
     * 
     * BUSINESS VALUE:
     * - Shows admin event publication activity
     * - Helps administrators understand their own event engagement
     * - Supports admin growth monitoring and planning
     * - Enables capacity planning for admin resources
     * 
     * @param adminUserId The ID of the administrator user
     * @return Total count of published events created by the admin, or 0 if error occurs
     */
    public static int getActiveEventsForAdmin(int adminUserId) {
        // 
        // QUERY FOR ACTIVE EVENTS FOR ADMIN - Count published events created by specific admin
        // 
        // This SQL query counts events that were created by a specific administrator
        // user and are currently published. Only events with status 2 (Published)
        // are counted, excluding draft or cancelled events. Think of it like
        // counting how many events an administrator has published.
        // 
        // THINK OF IT LIKE:
        // - Counting how many events an administrator has published
        // - Tallying up all the events that are published and created by an admin
        // - Getting the number of events that are published and created by a specific user
        // 
        String sql = "SELECT COUNT(*) FROM EventM WHERE CreatedByUserID = ? AND StatusTypeID = 2"; // Published events only
        
        try (Connection conn = Db.get();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            // 
            // SET PARAMETER - Pass the admin user ID
            // 
            // This sets the parameter for the admin user ID in the query.
            // Think of it like passing the ID of the user whose published events we are counting.
            // 
            // THINK OF IT LIKE:
            // - Passing the ID of the user we are interested in
            // - Setting the ID for the query
            // - Passing the user's identifier
            // 
            stmt.setInt(1, adminUserId);
            ResultSet rs = stmt.executeQuery();
            
            // 
            // EXTRACT COUNT RESULT - Get the actual number
            // 
            // If the query returned a result, we extract the count value.
            // Think of it like reading the number from an event counter.
            // 
            // THINK OF IT LIKE:
            // - Reading the result from a calculation
            // - Getting the answer from a count operation
            // - Extracting the total number found
            // 
            if (rs.next()) {
                return rs.getInt(1);
            }
            
        } catch (SQLException e) {
            // 
            // DATABASE ERROR - Handle connection or query problems
            // 
            // If something goes wrong with the database query, we log the error
            // and return 0. This prevents the system from crashing and provides
            // feedback about what went wrong.
            // 
            // THINK OF IT LIKE:
            // - A system error that needs to be reported
            // - A problem that prevents getting the count
            // - An issue that needs to be logged for debugging
            // 
            System.err.println("Error getting active events for admin " + adminUserId + ": " + e.getMessage());
            e.printStackTrace();
        }
        
        // 
        // RETURN DEFAULT VALUE - Provide fallback when errors occur
        // 
        // If anything goes wrong, we return 0 as a safe default value.
        // This ensures the system continues to work even if there are
        // database problems.
        // 
        // THINK OF IT LIKE:
        // - Providing a safe fallback value
        // - Ensuring the system doesn't crash
        // - Giving a reasonable default when data is unavailable
        // 
        return 0;
    }
    
    /**
     * getAdminDashboardMetrics(int adminUserId) - Get Comprehensive Admin-Specific Dashboard Metrics
     * 
     * This method gathers all the key metrics and statistics for a specific
     * administrator user to provide a comprehensive overview of their activity
     * and the system usage under their control. It combines data from
     * different tables to give a holistic view.
     * 
     * WHAT IT DOES:
     * - Calls getMyEventsCount() to get the total number of events created by the specific admin
     * - Calls getRegistrationsForMyEvents() to get the total number of registrations for the specific admin's events
     * - Calls getActiveEventsForAdmin() to get the total number of published events created by the specific admin
     * - Combines all these metrics into a single AdminDashboardMetrics object
     * - Handles database errors gracefully
     * 
     * USER EXPERIENCE:
     * - Administrators can see a complete picture of their own activity and system usage
     * - Dashboard displays show all major metrics for the specific admin
     * - System overview is available at a glance
     * - Growth trends and activity levels are visible
     * 
     * TECHNICAL PROCESS:
     * 1. Connect to database
     * 2. Call getMyEventsCount()
     * 3. Call getRegistrationsForMyEvents()
     * 4. Call getActiveEventsForAdmin()
     * 5. Combine results into a AdminDashboardMetrics object
     * 6. Return the AdminDashboardMetrics object
     * 
     * BUSINESS VALUE:
     * - Provides a single source of truth for admin activity and system usage
     * - Enables administrators to monitor all aspects of their own system
     * - Supports decision-making with concrete data
     * - Helps identify trends and patterns in admin activity
     * - Creates transparency about system usage by admins
     * 
     * @param adminUserId The ID of the administrator user
     * @return An AdminDashboardMetrics object containing all admin-specific metrics
     */
    public static AdminDashboardMetrics getAdminDashboardMetrics(int adminUserId) {
        AdminDashboardMetrics metrics = new AdminDashboardMetrics();
        
        try {
            metrics.myEvents = getMyEventsCount(adminUserId);
            metrics.totalRegistrations = getRegistrationsForMyEvents(adminUserId);
            metrics.activeEvents = getActiveEventsForAdmin(adminUserId);
            
            System.out.println("Admin dashboard metrics loaded for user " + adminUserId + ": " + 
                             "MyEvents=" + metrics.myEvents + 
                             ", Registrations=" + metrics.totalRegistrations + 
                             ", ActiveEvents=" + metrics.activeEvents);
            
        } catch (Exception e) {
            System.err.println("Error loading admin dashboard metrics for user " + adminUserId + ": " + e.getMessage());
            e.printStackTrace();
        }
        
        return metrics;
    }

    /**
     * Inner class to hold dashboard metrics
     */
    public static class DashboardMetrics {
        public int totalEvents = 0;
        public int totalUsers = 0;
        public int totalRegistrations = 0;
        public int totalAttendees = 0;
    }
    
    /**
     * Inner class to hold admin-specific dashboard metrics
     */
    public static class AdminDashboardMetrics {
        public int myEvents = 0;
        public int totalRegistrations = 0;
        public int activeEvents = 0;
    }
}