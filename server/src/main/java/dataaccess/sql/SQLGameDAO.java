package dataaccess.sql;

import chess.ChessGame;
import com.google.gson.Gson;
import dataaccess.*;
import model.*;
import java.sql.*;
import java.util.*;

public class SQLGameDAO implements GameDAO {
    private final Gson gson = new Gson();

    public SQLGameDAO() throws DataAccessException {
        configureDatabase();
    }

    @Override
    public GameData createGame(GameData game) throws DataAccessException {
        int gameID;
        String whiteUser = game.whiteUsername();
        String blackUser = game.blackUsername();
        String gameName = game.gameName();
        ChessGame chessGame = game.chessGame();
        String jsonChessGame = gson.toJson(chessGame);

        String sql = "INSERT INTO game (whiteUser, blackUser, name, game) VALUES (?, ?, ?, ?)";
        try (var conn = DatabaseManager.getConnection();
             var ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, whiteUser);
            ps.setString(2, blackUser);
            ps.setString(3, gameName);
            ps.setString(4, jsonChessGame);
            ps.executeUpdate();

            try (var keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    gameID = keys.getInt(1);
                    return new GameData(gameID, whiteUser, blackUser, gameName, chessGame);
                }
            }

        } catch (SQLException ex) {
            throw new DataAccessException("unable to create game", ex);
        }
        throw new DataAccessException("unable to get generated game ID");
    }

    @Override
    public GameData getGame(int gameID) throws DataAccessException {
        String sql = "SELECT id, whiteUser, blackUser, name, game FROM game WHERE id = ?";
        try (var conn = DatabaseManager.getConnection();
             var ps = conn.prepareStatement(sql)) {
            ps.setInt(1, gameID);
            try (var rs = ps.executeQuery()) {
                if (rs.next()) {
                    return readGame(rs);
                }
            }
        } catch (SQLException ex) {
            throw new DataAccessException("unable to get game", ex);
        }
        return null;
    }

    @Override
    public Collection<GameDataSummary> listGames() throws DataAccessException {
        String sql = "SELECT id, whiteUser, blackUser, name FROM game";
        var games = new ArrayList<GameDataSummary>();
        try (var conn = DatabaseManager.getConnection();
             var ps = conn.prepareStatement(sql);
             var rs = ps.executeQuery()) {
            while (rs.next()) {
                games.add(readGameSummary(rs));
            }
        } catch (SQLException ex) {
            throw new DataAccessException("unable to list games", ex);
        }
        return games;
    }

    @Override
    public void updateGame(int gameID, String username, ChessGame.TeamColor playerColor) throws DataAccessException {
        String sql;
        if (playerColor == ChessGame.TeamColor.WHITE) {
            sql = "UPDATE game SET whiteUser = ? WHERE id = ?";
        } else {
            sql = "UPDATE game SET blackUser = ? WHERE id = ?";
        }
        try (var conn = DatabaseManager.getConnection();
             var ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            ps.setInt(2, gameID);
            if (ps.executeUpdate() == 0) {
                throw new DataAccessException("Game doesn't exist");
            }
        } catch (SQLException ex) {
            throw new DataAccessException("unable to update game", ex);
        }
    }

    @Override
    public void clearGameData() throws DataAccessException {
        String sql = "TRUNCATE game";
        try (var conn = DatabaseManager.getConnection();
             var ps = conn.prepareStatement(sql)) {
            ps.executeUpdate();
        } catch (SQLException ex) {
            throw new DataAccessException("unable to clear game data", ex);
        }
    }

    private GameData readGame(ResultSet rs) throws SQLException {
        return new GameData(
                rs.getInt("id"),
                rs.getString("whiteUser"),
                rs.getString("blackUser"),
                rs.getString("name"),
                gson.fromJson(rs.getString("game"), ChessGame.class));
    }

    private GameDataSummary readGameSummary(ResultSet rs) throws SQLException {
        return new GameDataSummary(
                rs.getInt("id"),
                rs.getString("whiteUser"),
                rs.getString("blackUser"),
                rs.getString("name"));
    }

    private final String[] createStatements = {
            """
            CREATE TABLE IF NOT EXISTS  game (
              `id` int NOT NULL AUTO_INCREMENT,
              `whiteUser` varchar(256) DEFAULT NULL,
              `blackUser` varchar(256) DEFAULT NULL,
              `name` varchar(256) NOT NULL,
              `game` TEXT NOT NULL,
              PRIMARY KEY (`id`)
            )
            """
    };

    private void configureDatabase() throws DataAccessException {
        DatabaseManager.createDatabase();
        try (Connection conn = DatabaseManager.getConnection()) {
            for (String statement : createStatements) {
                try (var preparedStatement = conn.prepareStatement(statement)) {
                    preparedStatement.executeUpdate();
                }
            }
        } catch (SQLException ex) {
            throw new DataAccessException(String.format("Unable to configure database: %s", ex.getMessage()), ex);
        }
    }
}
