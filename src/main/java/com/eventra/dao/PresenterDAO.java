package com.eventra.dao;

import com.eventra.Db;
import com.eventra.model.Presenter;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PresenterDAO {
    
    public List<Presenter> getPresentersByEventId(int eventId) {
        List<Presenter> presenters = new ArrayList<>();
        String sql = "SELECT DISTINCT p.* FROM Presenter p " +
                    "INNER JOIN SessionPresenter sp ON p.PresenterID = sp.PresenterID " +
                    "INNER JOIN SessionM s ON sp.SessionID = s.SessionID " +
                    "WHERE s.EventID = ? AND s.StatusTypeID = 1 " +
                    "ORDER BY p.FirstName ASC";
        
        try (Connection conn = Db.get();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, eventId);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                Presenter presenter = mapResultSetToPresenter(rs);
                presenters.add(presenter);
            }
        } catch (SQLException e) {
            System.err.println("Error fetching presenters by event ID: " + e.getMessage());
            e.printStackTrace();
        }
        return presenters;
    }
    
    public Presenter getPresenterById(int presenterId) {
        String sql = "SELECT * FROM Presenter WHERE PresenterID = ?";
        
        try (Connection conn = Db.get();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, presenterId);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return mapResultSetToPresenter(rs);
            }
        } catch (SQLException e) {
            System.err.println("Error fetching presenter by ID: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }
    
    private Presenter mapResultSetToPresenter(ResultSet rs) throws SQLException {
        Presenter presenter = new Presenter();
        presenter.setPresenterId(rs.getInt("PresenterID"));
        
        // Combine FirstName and LastName into Name
        String firstName = rs.getString("FirstName");
        String lastName = rs.getString("LastName");
        String fullName = (firstName != null ? firstName : "") + " " + (lastName != null ? lastName : "");
        presenter.setName(fullName.trim());
        
        presenter.setTitle("Speaker"); // Default title
        presenter.setCompany(rs.getString("Bio")); // Use Bio as company for now
        presenter.setBio(rs.getString("Bio"));
        presenter.setImageUrl(null);
        presenter.setEmail(rs.getString("Email"));
        presenter.setLinkedinUrl(null);
        presenter.setTwitterUrl(null);
        
        // Set timestamps if they exist
        if (rs.getTimestamp("CreatedAt") != null) {
            presenter.setCreatedAt(rs.getTimestamp("CreatedAt").toLocalDateTime());
        } else {
            presenter.setCreatedAt(java.time.LocalDateTime.now());
        }
        
        if (rs.getTimestamp("UpdatedAt") != null) {
            presenter.setUpdatedAt(rs.getTimestamp("UpdatedAt").toLocalDateTime());
        } else {
            presenter.setUpdatedAt(java.time.LocalDateTime.now());
        }
        
        return presenter;
    }
} 