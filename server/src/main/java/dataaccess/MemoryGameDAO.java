package dataaccess;

import chess.ChessGame;
import model.GameData;
import model.GameDataSummary;

import java.util.*;

public class MemoryGameDAO implements GameDAO {
    private final Map<Integer, GameData> games = new HashMap<>();
    private int nextID = 1;

    @Override
    public GameData createGame(GameData game) {
        GameData newGame = new GameData(nextID, game.whiteUsername(), game.blackUsername(), game.gameName(), game.chessGame());
        games.put(newGame.gameID(), newGame);
        nextID++;
        return newGame;
    }

    @Override
    public GameData getGame(int gameID) {
        return games.get(gameID);
    }

    @Override
    public Collection<GameDataSummary> listGames() {
        List<GameDataSummary> gameSummaries = new ArrayList<>();
        for (GameData game : games.values()) {
            gameSummaries.add(new GameDataSummary(
                    game.gameID(),
                    game.whiteUsername(),
                    game.blackUsername(),
                    game.gameName()
            ));
        }

        return gameSummaries;
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
        nextID = 1;
    }
}
