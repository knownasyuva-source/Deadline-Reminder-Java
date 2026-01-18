import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import com.deadlinereminder.model.*;

public class DatabaseHandler implements DeadlineOperations {
    private static final String PROPERTIES_FILE = "database.properties";

    public DatabaseHandler() throws SQLException {
        try (Connection c = getConnection()) {
            createTableIfNotExists(c);
        }
    }

    private Connection getConnection() throws SQLException {
        String url = "jdbc:mysql://localhost:3306/deadlineDB?serverTimezone=UTC";
        String user = "root";
        String pass = "Manoj@0001";

        try (java.io.InputStream input = new java.io.FileInputStream(PROPERTIES_FILE)) {
            java.util.Properties prop = new java.util.Properties();
            prop.load(input);

            url = prop.getProperty("db.url", url);
            user = prop.getProperty("db.user", user);
            pass = prop.getProperty("db.password", pass);
        } catch (java.io.IOException ex) {
            System.out.println("Could not load " + PROPERTIES_FILE + " file provided, using defaults.");
        }

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            /* driver missing */ }
        return DriverManager.getConnection(url, user, pass);
    }

    private void createTableIfNotExists(Connection c) throws SQLException {
        String sql = "CREATE TABLE IF NOT EXISTS deadlines ("
                + "id INT AUTO_INCREMENT PRIMARY KEY,"
                + "title VARCHAR(100),"
                + "type VARCHAR(20),"
                + "dueDate DATE,"
                + "subject VARCHAR(50),"
                + "teamMembers VARCHAR(100)"
                + ");";
        try (Statement st = c.createStatement()) {
            st.execute(sql);
        }
    }

    @Override
    public void add(Deadline d) throws Exception {
        String sql = "INSERT INTO deadlines (title,type,dueDate,subject,teamMembers) VALUES (?,?,?,?,?)";
        try (Connection c = getConnection();
                PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, d.getTitle());
            ps.setString(2, d.getType());
            ps.setDate(3, d.getDueDate());
            ps.setString(4, d.getSubject());
            ps.setString(5, d.getTeamMembers());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next())
                    d.setId(rs.getInt(1));
            }
        } catch (SQLException e) {
            throw new Exception("Add failed", e);
        }
    }

    @Override
    public java.util.List<Deadline> getAll() throws Exception {
        List<Deadline> out = new ArrayList<>();/*  */
        String sql = "SELECT id,title,type,dueDate,subject,teamMembers FROM deadlines ORDER BY dueDate";
        try (Connection c = getConnection();
                PreparedStatement ps = c.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                String type = rs.getString("type");
                Deadline d = "Project".equals(type) ? new Project() : new Assignment();
                d.setId(rs.getInt("id"));
                d.setTitle(rs.getString("title"));
                d.setType(type);
                d.setDueDate(rs.getDate("dueDate"));
                d.setSubject(rs.getString("subject"));
                d.setTeamMembers(rs.getString("teamMembers"));
                out.add(d);
            }
        } catch (SQLException e) {
            throw new Exception("Fetch failed", e);
        }
        return out;
    }

    @Override
    public void update(Deadline d) throws Exception {
        String sql = "UPDATE deadlines SET title=?, type=?, dueDate=?, subject=?, teamMembers=? WHERE id=?";
        try (Connection c = getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, d.getTitle());
            ps.setString(2, d.getType());
            ps.setDate(3, d.getDueDate());
            ps.setString(4, d.getSubject());
            ps.setString(5, d.getTeamMembers());
            ps.setInt(6, d.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new Exception("Update failed", e);
        }
    }

    @Override
    public void delete(int id) throws Exception {
        String sql = "DELETE FROM deadlines WHERE id=?";
        try (Connection c = getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new Exception("Delete failed", e);
        }
    }
}
