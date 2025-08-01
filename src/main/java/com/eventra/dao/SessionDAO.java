package com.eventra.dao;

import com.eventra.Db;
import com.eventra.model.Session;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SessionDAO {
    
    public List<Session> getSessionsByEventId(int eventId) {
        List<Session> sessions = new ArrayList<>();
        String sql = "SELECT * FROM Session WHERE EventID = ? AND SessionStatusTypeID = 2 ORDER BY StartTime ASC";
        
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
        session.setDescription(rs.getString("Description"));
        session.setStartTime(rs.getTimestamp("StartTime").toLocalDateTime());
        session.setEndTime(rs.getTimestamp("EndTime").toLocalDateTime());
        session.setSessionStatusTypeId(rs.getInt("SessionStatusTypeID"));
        session.setPresenterId(rs.getInt("PresenterID"));
        session.setSessionType(rs.getString("SessionType"));
        session.setMaxCapacity(rs.getInt("MaxCapacity"));
        session.setCreatedAt(rs.getTimestamp("CreatedAt").toLocalDateTime());
        session.setUpdatedAt(rs.getTimestamp("UpdatedAt").toLocalDateTime());
        return session;
    }
} 