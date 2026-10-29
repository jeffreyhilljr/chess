package dataaccess.sql;

import chess.ChessGame;
import com.google.gson.Gson;
import dataaccess.*;
import model.*;
import org.junit.jupiter.api.*;
import org.mindrot.jbcrypt.BCrypt;
import service.ClearService;
import java.sql.SQLException;
import java.util.Collection;

public class SqlDAOTests {
    private static AuthDAO authDAO;
    private static UserDAO userDAO;
    private static GameDAO gameDAO;

    @BeforeEach
    public void setup() throws DataAccessException {
        authDAO = new SQLAuthDAO();
        gameDAO = new SQLGameDAO();
        userDAO = new SQLUserDAO();

        ClearService clearService = new ClearService(authDAO, gameDAO, userDAO);
        clearService.clearAllData();
    }

    @Test
    @DisplayName("Create User Success")
    public void createUserSuccess() throws DataAccessException {
        userDAO.createUser(new UserData("testUser", "12345", "test@gmail.com"));
        String sql = "SELECT username, password, email FROM user WHERE username = ?";
        try (var conn = DatabaseManager.getConnection();
             var ps = conn.prepareStatement(sql)) {
            ps.setString(1, "testUser");
            try (var rs = ps.executeQuery()) {
                if (rs.next()) {
                    Assertions.assertEquals("testUser", rs.getString("username"));
                    Assertions.assertTrue(BCrypt.checkpw("12345", rs.getString("password")));
                    Assertions.assertEquals("test@gmail.com", rs.getString("email"));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    @DisplayName("Create User Same Username")
    public void createUserSameUsername() throws DataAccessException {
        userDAO.createUser(new UserData("testUser", "12345", "test@gmail.com"));
        Assertions.assertThrows(DataAccessException.class, () ->
                userDAO.createUser(new UserData("testUser", "54321", "test2@gmail.com")));
        String sql = "SELECT username, password, email FROM user WHERE username = ?";
        try (var conn = DatabaseManager.getConnection();
             var ps = conn.prepareStatement(sql)) {
            ps.setString(1, "testUser");
            try (var rs = ps.executeQuery()) {
                if (rs.next()) {
                    Assertions.assertEquals("testUser", rs.getString("username"));
                    Assertions.assertTrue(BCrypt.checkpw("12345", rs.getString("password")));
                    Assertions.assertEquals("test@gmail.com", rs.getString("email"));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    @DisplayName("Get User Success")
    public void getUserSuccess() throws DataAccessException {
        userDAO.createUser(new UserData("testUser", "12345", "test@gmail.com"));
        UserData user = userDAO.getUser("testUser");
        Assertions.assertEquals("testUser", user.username());
        Assertions.assertTrue(BCrypt.checkpw("12345", user.password()));
        Assertions.assertEquals("test@gmail.com", user.email());
    }

    @Test
    @DisplayName("Get User Null")
    public void getUserNull() throws DataAccessException {
        UserData user = userDAO.getUser("testUser");
        Assertions.assertNull(user);
    }

    @Test
    @DisplayName("Get User Throws Exception")
    public void getUserThrowsException() throws DataAccessException, SQLException {
        String sql = "DROP TABLE user";
        try (var conn = DatabaseManager.getConnection();
             var ps = conn.prepareStatement(sql)) {
           ps.executeUpdate();
        }
        Assertions.assertThrows(DataAccessException.class, () -> userDAO.getUser("testUser"));
    }

    @Test
    @DisplayName("Clear User Data Success")
    public void clearUserDataSuccess() throws DataAccessException {
        userDAO.createUser(new UserData("testUser", "12345", "test@gmail.com"));
        userDAO.clearUserData();
        UserData user = userDAO.getUser("testUser");
        Assertions.assertNull(user);
    }

    @Test
    @DisplayName("Clear User Data Throws Exception")
    public void clearUserDataThrowsException() throws DataAccessException, SQLException {
        userDAO.createUser(new UserData("testUser", "12345", "test@gmail.com"));
        String sql = "DROP TABLE user";
        try (var conn = DatabaseManager.getConnection();
             var ps = conn.prepareStatement(sql)) {
            ps.executeUpdate();
        }
        Assertions.assertThrows(DataAccessException.class, () -> userDAO.clearUserData());
    }

    @Test
    @DisplayName("Create Game Success")
    public void createGameSuccess() throws DataAccessException {
        ChessGame chessGame = new ChessGame();
        Gson gson = new Gson();
        gameDAO.createGame(new GameData(0, null, null, "gameName", chessGame));
        String sql = "SELECT id, whiteUser, blackUser, name, game FROM game WHERE id = ?";
        try (var conn = DatabaseManager.getConnection();
             var ps = conn.prepareStatement(sql)) {
            ps.setInt(1, 1);
            try (var rs = ps.executeQuery()) {
                if (rs.next()) {
                    Assertions.assertNull(rs.getString("whiteUser"));
                    Assertions.assertNull(rs.getString("blackUser"));
                    Assertions.assertEquals("gameName", rs.getString("name"));
                    Assertions.assertEquals(gson.toJson(chessGame), rs.getString("game"));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    @DisplayName("Create Game Null Name")
    public void createGameNullName() {
        ChessGame chessGame = new ChessGame();
        Assertions.assertThrows(DataAccessException.class, () ->
                gameDAO.createGame(new GameData(0, null, null, null, chessGame)));
    }

    @Test
    @DisplayName("Get Game Success")
    public void getGameSuccess() throws DataAccessException {
        ChessGame chessGame = new ChessGame();
        gameDAO.createGame(gameDAO.createGame(new GameData(0, "user1", null, "gameName", chessGame)));
        GameData user = gameDAO.getGame(1);
        Assertions.assertEquals("user1", user.whiteUsername());
        Assertions.assertNull(user.blackUsername());
        Assertions.assertEquals("gameName", user.gameName());
        Assertions.assertEquals(chessGame, user.chessGame());
    }

    @Test
    @DisplayName("Get Game Null")
    public void getGameNull() throws DataAccessException {
        GameData user = gameDAO.getGame(1);
        Assertions.assertNull(user);
    }

    @Test
    @DisplayName("Get Game Throws Exception")
    public void getGameThrowsException() throws DataAccessException, SQLException {
        String sql = "DROP TABLE game";
        try (var conn = DatabaseManager.getConnection();
             var ps = conn.prepareStatement(sql)) {
            ps.executeUpdate();
        }
        Assertions.assertThrows(DataAccessException.class, () -> gameDAO.getGame(1));
    }

    @Test
    @DisplayName("List Games Success")
    public void listGamesSuccess() throws DataAccessException {
        ChessGame chessGame1 = new ChessGame();
        gameDAO.createGame(new GameData(0, "user1", "user2", "gameName1", chessGame1));
        ChessGame chessGame2 = new ChessGame();
        gameDAO.createGame(new GameData(0, "user3", "user4", "gameName2", chessGame2));
        Collection<GameDataSummary> games = gameDAO.listGames();
        Assertions.assertTrue(games.contains(new GameDataSummary(1, "user1", "user2", "gameName1")));
        Assertions.assertTrue(games.contains(new GameDataSummary(2, "user3", "user4", "gameName2")));
    }

    @Test
    @DisplayName("List Games Throws Exception")
    public void listGamesThrowsException() throws DataAccessException, SQLException {
        String sql = "DROP TABLE game";
        try (var conn = DatabaseManager.getConnection();
             var ps = conn.prepareStatement(sql)) {
            ps.executeUpdate();
        }
        Assertions.assertThrows(DataAccessException.class, () -> gameDAO.listGames());
    }

    @Test
    @DisplayName("Update Game Success White")
    public void updateGameSuccessWhite() throws DataAccessException {
        ChessGame chessGame = new ChessGame();
        gameDAO.createGame(gameDAO.createGame(new GameData(0, null, null, "gameName", chessGame)));
        gameDAO.updateGame(1, "user1", ChessGame.TeamColor.WHITE);
        GameData user = gameDAO.getGame(1);
        Assertions.assertEquals("user1", user.whiteUsername());
        Assertions.assertNull(user.blackUsername());
        Assertions.assertEquals("gameName", user.gameName());
        Assertions.assertEquals(chessGame, user.chessGame());
    }

    @Test
    @DisplayName("Update Game Success Black")
    public void updateGameSuccessBlack() throws DataAccessException {
        ChessGame chessGame = new ChessGame();
        gameDAO.createGame(gameDAO.createGame(new GameData(0, null, null, "gameName", chessGame)));
        gameDAO.updateGame(1, "user1", ChessGame.TeamColor.BLACK);
        GameData user = gameDAO.getGame(1);
        Assertions.assertEquals("user1", user.blackUsername());
        Assertions.assertNull(user.whiteUsername());
        Assertions.assertEquals("gameName", user.gameName());
        Assertions.assertEquals(chessGame, user.chessGame());
    }

    @Test
    @DisplayName("Update Game Throws Exception")
    public void updateGameThrowsException() throws DataAccessException, SQLException {
        String sql = "DROP TABLE game";
        try (var conn = DatabaseManager.getConnection();
             var ps = conn.prepareStatement(sql)) {
            ps.executeUpdate();
        }
        Assertions.assertThrows(DataAccessException.class, () ->
                gameDAO.updateGame(1, "user", ChessGame.TeamColor.WHITE));
    }

    @Test
    @DisplayName("Update Nonexisting Game")
    public void updateNonexistingGame() {
        Assertions.assertThrows(DataAccessException.class, () ->
                gameDAO.updateGame(1, "user", ChessGame.TeamColor.WHITE));
    }

    @Test
    @DisplayName("Clear Game Data Success")
    public void clearGameDataSuccess() throws DataAccessException {
        ChessGame chessGame = new ChessGame();
        gameDAO.createGame(gameDAO.createGame(new GameData(0, null, null, "gameName", chessGame)));
        gameDAO.clearGameData();
        GameData game = gameDAO.getGame(1);
        Assertions.assertNull(game);
    }

    @Test
    @DisplayName("Clear Game Data Throws Exception")
    public void clearGameDataThrowsException() throws DataAccessException, SQLException {
        ChessGame chessGame = new ChessGame();
        gameDAO.createGame(gameDAO.createGame(new GameData(0, null, null, "gameName", chessGame)));
        String sql = "DROP TABLE game";
        try (var conn = DatabaseManager.getConnection();
             var ps = conn.prepareStatement(sql)) {
            ps.executeUpdate();
        }
        Assertions.assertThrows(DataAccessException.class, () -> gameDAO.clearGameData());
    }

    @Test
    @DisplayName("Create Auth Success")
    public void createAuthSuccess() throws DataAccessException {
        authDAO.createAuth(new AuthData("a1b2c3d4e5f6g7h8i9j0", "user"));
        String sql = "SELECT authToken, username FROM auth WHERE authToken = ?";
        try (var conn = DatabaseManager.getConnection();
             var ps = conn.prepareStatement(sql)) {
            ps.setString(1, "a1b2c3d4e5f6g7h8i9j0");
            try (var rs = ps.executeQuery()) {
                if (rs.next()) {
                    Assertions.assertEquals("a1b2c3d4e5f6g7h8i9j0", rs.getString("authToken"));
                    Assertions.assertEquals("user", rs.getString("username"));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    @DisplayName("Create Auth Missing Token")
    public void createAuthMissingToken() {
        Assertions.assertThrows(DataAccessException.class, () -> authDAO.createAuth(new AuthData(null, "user")));
    }

    @Test
    @DisplayName("Get Auth Success")
    public void getAuthSuccess() throws DataAccessException {
        authDAO.createAuth(new AuthData("a1b2c3d4e5f6g7h8i9j0", "user"));
        AuthData auth = authDAO.getAuth("a1b2c3d4e5f6g7h8i9j0");
        Assertions.assertEquals("a1b2c3d4e5f6g7h8i9j0", auth.authToken());
        Assertions.assertEquals("user", auth.username());
    }

    @Test
    @DisplayName("Get Auth Null")
    public void getAuthNull() throws DataAccessException {
        Assertions.assertNull(authDAO.getAuth("a1b2c3d4e5f6g7h8i9j0"));
    }

    @Test
    @DisplayName("Get Auth Throws Exception")
    public void getAuthThrowsException() throws DataAccessException, SQLException {
        String sql = "DROP TABLE auth";
        try (var conn = DatabaseManager.getConnection();
             var ps = conn.prepareStatement(sql)) {
            ps.executeUpdate();
        }
        Assertions.assertThrows(DataAccessException.class, () -> authDAO.getAuth("a1b2c3d4e5f6g7h8i9j0"));
    }

    @Test
    @DisplayName("Delete Auth Success")
    public void deleteAuthSuccess() throws DataAccessException {
        authDAO.createAuth(new AuthData("a1b2c3d4e5f6g7h8i9j0", "user"));
        authDAO.deleteAuth("a1b2c3d4e5f6g7h8i9j0");
        Assertions.assertNull(authDAO.getAuth("a1b2c3d4e5f6g7h8i9j0"));
    }

    @Test
    @DisplayName("Delete Auth Throws Exception")
    public void deleteAuthThrowsException() throws DataAccessException, SQLException {
        String sql = "DROP TABLE auth";
        try (var conn = DatabaseManager.getConnection();
             var ps = conn.prepareStatement(sql)) {
            ps.executeUpdate();
        }
        Assertions.assertThrows(DataAccessException.class, () -> authDAO.deleteAuth("a1b2c3d4e5f6g7h8i9j0"));
    }

    @Test
    @DisplayName("Clear Auth Data Success")
    public void clearAuthDataSuccess() throws DataAccessException {
        authDAO.createAuth(new AuthData("a1b2c3d4e5f6g7h8i9j0", "user"));
        authDAO.clearAuthData();
        Assertions.assertNull(authDAO.getAuth("a1b2c3d4e5f6g7h8i9j0"));
    }

    @Test
    @DisplayName("Clear Auth Data Throws Exception")
    public void clearAuthDataThrowsException() throws DataAccessException, SQLException {
        authDAO.createAuth(new AuthData("a1b2c3d4e5f6g7h8i9j0", "user"));
        String sql = "DROP TABLE auth";
        try (var conn = DatabaseManager.getConnection();
             var ps = conn.prepareStatement(sql)) {
            ps.executeUpdate();
        }
        Assertions.assertThrows(DataAccessException.class, () -> authDAO.clearAuthData());
    }
}
