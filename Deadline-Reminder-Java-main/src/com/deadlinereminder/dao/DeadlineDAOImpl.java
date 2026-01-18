package com.deadlinereminder.dao;

import com.deadlinereminder.DBUtil;
import com.deadlinereminder.model.Deadline;
import com.deadlinereminder.model.Assignment;
import com.deadlinereminder.model.Project;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DeadlineDAOImpl implements DeadlineDAO {
    public DeadlineDAOImpl() throws DataAccessException {
        try (Connection conn = DBUtil.getConnection()) {
            createTableIfNotExists(conn);
        } catch (SQLException e) {
            throw new DataAccessException("Failed to initialize database", e);
        }
    }

    private void createTableIfNotExists(Connection conn) throws SQLException {
        String sql = "CREATE TABLE IF NOT EXISTS deadlines ("
                + "id INT AUTO_INCREMENT PRIMARY KEY,"
                + "title VARCHAR(100),"
                + "type VARCHAR(20),"
                + "dueDate DATE,"
                + "subject VARCHAR(50),"
                + "teamMembers VARCHAR(100)"
                + ");";
        try (Statement st = conn.createStatement()) {
            st.execute(sql);
        }
    }

    @Override
    public void add(Deadline d) throws DataAccessException {
        String sql = "INSERT INTO deadlines (title,type,dueDate,subject,teamMembers) VALUES (?,?,?,?,?)";
        try (Connection conn = DBUtil.getConnection(); PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, d.getTitle());
            ps.setString(2, d.getType());
            ps.setDate(3, d.getDueDate());
            ps.setString(4, d.getSubject());
            ps.setString(5, d.getTeamMembers());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) d.setId(rs.getInt(1));
            }
        } catch (SQLException e) {
            throw new DataAccessException("Error adding deadline", e);
        }
    }

    @Override
    public List<Deadline> getAll() throws DataAccessException {
        List<Deadline> list = new ArrayList<>();
        String sql = "SELECT id,title,type,dueDate,subject,teamMembers FROM deadlines ORDER BY dueDate";
        try (Connection conn = DBUtil.getConnection(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                String type = rs.getString("type");
                Deadline d = "Project".equals(type) ? new Project() : new Assignment();
                d.setId(rs.getInt("id"));
                d.setTitle(rs.getString("title"));
                d.setType(type);
                d.setDueDate(rs.getDate("dueDate"));
                d.setSubject(rs.getString("subject"));
                d.setTeamMembers(rs.getString("teamMembers"));
                list.add(d);
            }
        } catch (SQLException e) {
            throw new DataAccessException("Error fetching deadlines", e);
        }
        return list;
    }

    @Override
    public void update(Deadline d) throws DataAccessException {
        String sql = "UPDATE deadlines SET title=?, type=?, dueDate=?, subject=?, teamMembers=? WHERE id=?";
        try (Connection conn = DBUtil.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, d.getTitle());
            ps.setString(2, d.getType());
            ps.setDate(3, d.getDueDate());
            ps.setString(4, d.getSubject());
            ps.setString(5, d.getTeamMembers());
            ps.setInt(6, d.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("Error updating deadline", e);
        }
    }

    @Override
    public void delete(int id) throws DataAccessException {
        String sql = "DELETE FROM deadlines WHERE id=?";
        try (Connection conn = DBUtil.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("Error deleting deadline", e);
        }
    }
}
