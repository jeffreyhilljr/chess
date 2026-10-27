package dataaccess;

import chess.ChessGame;
import model.GameData;
import model.GameDataSummary;

import java.util.Collection;

public interface GameDAO {
    GameData createGame(GameData game) throws DataAccessException;
    GameData getGame(int gameID) throws DataAccessException;
    Collection<GameDataSummary> listGames() throws DataAccessException;
    void updateGame(int gameID, String username, ChessGame.TeamColor playerColor) throws DataAccessException;
    void clearGameData() throws DataAccessException;
}
