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

        if (createGameRequest == null) {
            throw new BadRequestException();
        }

        String gameName = createGameRequest.gameName();

        if (gameName == null || gameName.isBlank()) {
            throw new BadRequestException();
        }

        GameData game = new GameData(0, null, null, gameName, new ChessGame());
        GameData newGame = gameDAO.createGame(game);

        return new CreateGameResult(newGame.gameID());
    }


    public void joinGame(JoinGameRequest joinGameRequest, String authToken) throws DataAccessException, ServiceException {
        if (authToken == null) {
            throw new UnauthorizedException();
        }

        AuthData authData = authDAO.getAuth(authToken);

        if (authData == null) {
            throw new UnauthorizedException();
        }

        ChessGame.TeamColor playerColor = joinGameRequest.playerColor();
        int gameID = joinGameRequest.gameID();

        GameData game = gameDAO.getGame(gameID);

        if (game == null) {
            throw new BadRequestException();
        }

        if (playerColor == ChessGame.TeamColor.WHITE) {
            if (game.whiteUsername() != null) {
                throw new AlreadyTakenException();
            }
        } else if (playerColor == ChessGame.TeamColor.BLACK) {
            if (game.blackUsername() != null) {
                throw new AlreadyTakenException();
            }
        } else {
            throw new BadRequestException();
        }

        gameDAO.updateGame(gameID, authData.username(), playerColor);
    }
}
