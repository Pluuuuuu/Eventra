package com.eventra.dao;

import com.eventra.Db;
import com.eventra.model.Session;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SessionDAO {
    
    public List<Session> getSessionsByEventId(int eventId) {
        List<Session> sessions = new ArrayList<>();
        String sql = "SELECT * FROM SessionM WHERE EventID = ? AND StatusTypeID = 1 ORDER BY StartAt ASC";
        
        try (Connection conn = Db.get();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, eventId);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                Session session = mapResultSetToSession(rs);
                sessions.add(session);
            }
        } catch (SQLException e) {
            System.err.println("Error fetching sessions by event ID: " + e.getMessage());
            e.printStackTrace();
        }
        return sessions;
    }
    
    private Session mapResultSetToSession(ResultSet rs) throws SQLException {
        Session session = new Session();
        session.setSessionId(rs.getInt("SessionID"));
        session.setEventId(rs.getInt("EventID"));
        session.setTitle(rs.getString("Title"));
        session.setDescription(rs.getString("Abstract"));
        
        // Map StartAt/EndAt to StartTime/EndTime
        session.setStartTime(rs.getTimestamp("StartAt").toLocalDateTime());
        session.setEndTime(rs.getTimestamp("EndAt").toLocalDateTime());
        
        session.setSessionStatusTypeId(rs.getInt("StatusTypeID"));
        
        // Get presenter ID from SessionPresenter table
        session.setPresenterId(getPresenterIdForSession(rs.getInt("SessionID")));
        
        session.setSessionType(rs.getString("Track"));
        session.setMaxCapacity(0); // Default unlimited
        
        // Set timestamps
        if (rs.getTimestamp("CreatedAt") != null) {
            session.setCreatedAt(rs.getTimestamp("CreatedAt").toLocalDateTime());
        } else {
            session.setCreatedAt(java.time.LocalDateTime.now());
        }
        
        if (rs.getTimestamp("UpdatedAt") != null) {
            session.setUpdatedAt(rs.getTimestamp("UpdatedAt").toLocalDateTime());
        } else {
            session.setUpdatedAt(java.time.LocalDateTime.now());
        }
        
        return session;
    }
    
    private int getPresenterIdForSession(int sessionId) {
        String sql = "SELECT PresenterID FROM SessionPresenter WHERE SessionID = ?";
        
        try (Connection conn = Db.get();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, sessionId);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt("PresenterID");
            }
        } catch (SQLException e) {
            System.err.println("Error getting presenter for session: " + e.getMessage());
        }
        return 0; // No presenter found
    }
} 