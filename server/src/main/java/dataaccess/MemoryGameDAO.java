package dataaccess;

import chess.ChessGame;
import model.GameData;

import java.util.*;

public class MemoryGameDAO implements GameDAO {
    private final Map<Integer, GameData> games = new HashMap<>();

    @Override
    public void createGame(GameData game) {
        games.put(game.gameID(), game);
    }

    @Override
    public GameData getGame(int gameID) {
        return games.get(gameID);
    }

    @Override
    public Collection<GameData> listGames() {
        return new ArrayList<>(games.values());
    }

    @Override
    public void updateGame(int gameID, String username, ChessGame.TeamColor playerColor) throws DataAccessException {
        GameData game = getGame(gameID);

        if (game == null) {
            throw new DataAccessException("Game doesn't exist");
        }

        String whiteUsername = game.whiteUsername();
        String blackUsername = game.blackUsername();
        String gameName = game.gameName();
        ChessGame chessGame = game.chessGame();

        if (playerColor == ChessGame.TeamColor.WHITE) {
            whiteUsername = username;
        } else {
            blackUsername = username;
        }

        GameData newGame = new GameData(gameID, whiteUsername, blackUsername, gameName, chessGame);
        games.put(gameID, newGame);
    }

    @Override
    public void clearGameData() {
        games.clear();
    }
}
