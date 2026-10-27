package service;

import dataaccess.AuthDAO;
import dataaccess.GameDAO;

public class GameService {
    private final AuthDAO authDAO;
    private final GameDAO gameDAO;

    public GameService (AuthDAO authDAO, GameDAO gameDAO) {
        this.authDAO = authDAO;
        this.gameDAO = gameDAO;
    }

//    public ListGamesResult listGames(ListGamesRequest listGamesRequest) {}
//    public CreateGameResult createGame(CreateGameRequest createGameRequest) {}
//    public JoinGameResult joinGame(JoinGameRequest joinGameRequest) {}

}
