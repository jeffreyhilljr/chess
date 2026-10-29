package dataaccess.sql;

import dataaccess.*;
import model.UserData;
import org.mindrot.jbcrypt.BCrypt;
import java.sql.*;

public class SQLUserDAO implements UserDAO {

    public SQLUserDAO() throws DataAccessException {
        String[] createStatements = {
                """
            CREATE TABLE IF NOT EXISTS user (
              `username` varchar(256) NOT NULL,
              `password` varchar(256) NOT NULL,
              `email` varchar(256) NOT NULL,
              PRIMARY KEY (`username`)
            )
            """
        };
        DatabaseManager.configureDatabase(createStatements);
    }

    @Override
    public void createUser(UserData user) throws DataAccessException {
        String username = user.username();
        String password = user.password();
        String email = user.email();
        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
        String sql = "INSERT INTO user (username, password, email) VALUES (?, ?, ?)";
        try (var conn = DatabaseManager.getConnection();
             var ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            ps.setString(2, hashedPassword);
            ps.setString(3, email);
            ps.executeUpdate();
        } catch (SQLException ex) {
            throw new DataAccessException("unable to create user", ex);
        }
    }

    @Override
    public UserData getUser(String username) throws DataAccessException {
        String sql = "SELECT username, password, email FROM user WHERE username = ?";
        try (var conn = DatabaseManager.getConnection();
             var ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            try (var rs = ps.executeQuery()) {
                if (rs.next()) {
                    return readUser(rs);
                }
            }
        } catch (SQLException ex) {
            throw new DataAccessException("unable to get user", ex);
        }
        return null;
    }

    @Override
    public void clearUserData() throws DataAccessException {
        String sql = "TRUNCATE user";
        try (var conn = DatabaseManager.getConnection();
             var ps = conn.prepareStatement(sql)) {
            ps.executeUpdate();
        } catch (SQLException ex) {
            throw new DataAccessException("unable to clear user data", ex);
        }
    }

    private UserData readUser(ResultSet rs) throws SQLException {
        return new UserData(
                rs.getString("username"),
                rs.getString("password"),
                rs.getString("email"));
    }
}
