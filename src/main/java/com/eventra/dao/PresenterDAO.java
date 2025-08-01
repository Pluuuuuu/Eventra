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
                    "INNER JOIN Session s ON p.PresenterID = s.PresenterID " +
                    "WHERE s.EventID = ? AND s.SessionStatusTypeID = 2 " +
                    "ORDER BY p.Name ASC";
        
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
        presenter.setName(rs.getString("Name"));
        presenter.setTitle(rs.getString("Title"));
        presenter.setCompany(rs.getString("Company"));
        presenter.setBio(rs.getString("Bio"));
        presenter.setImageUrl(rs.getString("ImageURL"));
        presenter.setEmail(rs.getString("Email"));
        presenter.setLinkedinUrl(rs.getString("LinkedinURL"));
        presenter.setTwitterUrl(rs.getString("TwitterURL"));
        presenter.setCreatedAt(rs.getTimestamp("CreatedAt").toLocalDateTime());
        presenter.setUpdatedAt(rs.getTimestamp("UpdatedAt").toLocalDateTime());
        return presenter;
    }
} 