package dataaccess;

import chess.ChessGame;
import model.GameData;

import java.util.Collection;

public interface GameDAO {
    void createGame(GameData game);
    GameData getGame(int gameID);
    Collection<GameData> listGames();
    void updateGame(int gameID, String username, ChessGame.TeamColor playerColor) throws DataAccessException;
    void clearGameData();
}
