package dataaccess;

import model.GameData;

import java.util.Collection;

public interface GameDAO {
    void createGame(String gameName);
    GameData getGame(int gameID);
    Collection<GameData> listGames();
    void updateGame(int gameID);
    void clearGameData();
}
