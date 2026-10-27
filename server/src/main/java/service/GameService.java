package service;

import chess.ChessGame;
import dataaccess.*;
import model.*;
import service.exception.*;

public class GameService {
    private final AuthDAO authDAO;
    private final GameDAO gameDAO;

    public GameService (AuthDAO authDAO, GameDAO gameDAO) {
        this.authDAO = authDAO;
        this.gameDAO = gameDAO;
    }

    public ListGamesResult listGames(String authToken) throws DataAccessException, ServiceException {
        if (authToken == null) {
            throw new UnauthorizedException();
        }

        AuthData authData = authDAO.getAuth(authToken);

        if (authData == null) {
            throw new UnauthorizedException();
        }

        return new ListGamesResult(gameDAO.listGames());
    }


    public CreateGameResult createGame(CreateGameRequest createGameRequest, String authToken) throws DataAccessException, ServiceException {
        if (authToken == null) {
            throw new UnauthorizedException();
        }

        AuthData authData = authDAO.getAuth(authToken);

        if (authData == null) {
            throw new UnauthorizedException();
        }

        String gameName = createGameRequest.gameName();

        GameData game = new GameData(0, null, null, gameName, new ChessGame());
        GameData newGame = gameDAO.createGame(game);

        return new CreateGameResult(newGame.gameID());
    }


    public void joinGame(JoinGameRequest joinGameRequest, String authToken) {
//        if (authToken == null) {
//            throw new UnauthorizedException();
//        }
//
//        AuthData authData = authDAO.getAuth(authToken);
//
//        if (authData == null) {
//            throw new UnauthorizedException();
//        }
    }
}
