package service;

import chess.ChessGame;
import dataaccess.*;
import model.*;
import org.junit.jupiter.api.*;
import service.exception.*;

import java.util.ArrayList;
import java.util.List;

public class ServiceTests {
    private static AuthDAO authDAO;
    private static UserDAO userDAO;
    private static GameDAO gameDAO;

    private static ClearService clearService;
    private static UserService userService;
    private static GameService gameService;

    @BeforeEach
    public void setup() {
        authDAO = new MemoryAuthDAO();
        userDAO = new MemoryUserDAO();
        gameDAO = new MemoryGameDAO();

        clearService = new ClearService(authDAO, gameDAO, userDAO);
        userService = new UserService(authDAO, userDAO);
        gameService = new GameService(authDAO, gameDAO);
    }

    @Test
    @DisplayName("Clear Auth Data")
    public void clearAuthData() throws DataAccessException {
        authDAO.createAuth(new AuthData("f224c571-b257-4c6d-9fa2-9e85a2b8dfa0", "username"));
        clearService.clearAllData();
        Assertions.assertNull(authDAO.getAuth("f224c571-b257-4c6d-9fa2-9e85a2b8dfa0"));
    }

    @Test
    @DisplayName("Clear User Data")
    public void clearUserData() throws DataAccessException {
        userDAO.createUser(new UserData("username", "12345", "test@gmail.com"));
        clearService.clearAllData();
        Assertions.assertNull(userDAO.getUser("username"));
    }

    @Test
    @DisplayName("Clear Game Data")
    public void clearGameData() throws DataAccessException {
        gameDAO.createGame(new GameData(1234, "whiteUsername", "blackUsername", "gameName", new ChessGame()));
        clearService.clearAllData();
        Assertions.assertNull(gameDAO.getGame(1234));
    }

    @Test
    @DisplayName("Register Success")
    public void registerSuccess() throws ServiceException, DataAccessException {
        userService.register(new RegisterRequest("username", "12345", "test@gmail.com"));
        Assertions.assertEquals(new UserData("username", "12345", "test@gmail.com"), userDAO.getUser("username"));
    }

    @Test
    @DisplayName("Register Same Username")
    public void registerSameUsername() throws DataAccessException {
        userDAO.createUser(new UserData("username", "12345", "test@gmail.com"));
        Assertions.assertThrows(AlreadyTakenException.class, () ->
                userService.register(new RegisterRequest("username", "54321", "bob@gmail.com")));
        Assertions.assertEquals(new UserData("username", "12345", "test@gmail.com"), userDAO.getUser("username"));
    }

    @Test
    @DisplayName("Register Missing Args")
    public void registerMissingArgs() throws DataAccessException {
        Assertions.assertThrows(BadRequestException.class, () ->
                userService.register(new RegisterRequest(null, "54321", "bob@gmail.com")));
        Assertions.assertThrows(BadRequestException.class, () ->
                userService.register(new RegisterRequest("username", null, "bob@gmail.com")));
        Assertions.assertThrows(BadRequestException.class, () ->
                userService.register(new RegisterRequest("username", "54321", null)));
        Assertions.assertThrows(BadRequestException.class, () ->
                userService.register(new RegisterRequest("  ", "54321", "bob@gmail.com")));
        Assertions.assertThrows(BadRequestException.class, () ->
                userService.register(new RegisterRequest("username", "  ", "bob@gmail.com")));
        Assertions.assertThrows(BadRequestException.class, () ->
                userService.register(new RegisterRequest("username", "54321", "  ")));
        Assertions.assertNull(userDAO.getUser("username"));
    }

    @Test
    @DisplayName("Register Missing Request")
    public void registerMissingRequest() {
        Assertions.assertThrows(BadRequestException.class, () ->
                userService.register(null));
    }

    @Test
    @DisplayName("Register Multiple")
    public void registerMultiple() throws ServiceException, DataAccessException {
        RegisterResult result1 = userService.register(new RegisterRequest("username1", "12345", "test1@gmail.com"));
        Assertions.assertEquals(new UserData("username1", "12345", "test1@gmail.com"), userDAO.getUser("username1"));

        RegisterResult result2 = userService.register(new RegisterRequest("username2", "54321", "test2@gmail.com"));
        Assertions.assertEquals(new UserData("username2", "54321", "test2@gmail.com"), userDAO.getUser("username2"));

        Assertions.assertNotEquals(result1, result2);
        Assertions.assertNotEquals(result1.authToken(), result2.authToken());
    }

    @Test
    @DisplayName("Login Success")
    public void loginSuccess() throws ServiceException, DataAccessException {
        userDAO.createUser(new UserData("username", "12345", "test@gmail.com"));
        LoginResult result = userService.login(new LoginRequest("username", "12345"));
        Assertions.assertNotNull(authDAO.getAuth(result.authToken()));
    }

    @Test
    @DisplayName("Login Missing Request")
    public void loginMissingRequest() {
        Assertions.assertThrows(BadRequestException.class, () ->
                userService.login(null));
    }

    @Test
    @DisplayName("Login Missing Args")
    public void loginMissingArgs() throws DataAccessException {
        userDAO.createUser(new UserData("username", "12345", "test@gmail.com"));
        Assertions.assertThrows(BadRequestException.class, () ->
                userService.login(new LoginRequest(null, "54321")));
        Assertions.assertThrows(BadRequestException.class, () ->
                userService.login(new LoginRequest("username", null)));
        Assertions.assertThrows(BadRequestException.class, () ->
                userService.login(new LoginRequest("  ", "54321")));
        Assertions.assertThrows(BadRequestException.class, () ->
                userService.login(new LoginRequest("username", "  ")));
    }

    @Test
    @DisplayName("Login Unauthorized")
    public void loginUnauthorized() {
        Assertions.assertThrows(UnauthorizedException.class, () ->
              userService.login(new LoginRequest("username", "54321")));
    }

    @Test
    @DisplayName("Login Multiple")
    public void loginMultiple() throws DataAccessException, ServiceException {
        userDAO.createUser(new UserData("username1", "12345", "test1@gmail.com"));
        LoginResult result1 = userService.login(new LoginRequest("username1", "12345"));
        Assertions.assertNotNull(authDAO.getAuth(result1.authToken()));

        userDAO.createUser(new UserData("username2", "54321", "test2@gmail.com"));
        LoginResult result2 = userService.login(new LoginRequest("username2", "54321"));
        Assertions.assertNotNull(authDAO.getAuth(result2.authToken()));

        Assertions.assertNotEquals(result1, result2);
        Assertions.assertNotEquals(result1.authToken(), result2.authToken());
    }

    @Test
    @DisplayName("Logout Success")
    public void logoutSuccess() throws DataAccessException, ServiceException {
        userDAO.createUser(new UserData("username", "12345", "test@gmail.com"));
        authDAO.createAuth(new AuthData("f224c571-b257-4c6d-9fa2-9e85a2b8dfa0", "username"));
        userService.logout("f224c571-b257-4c6d-9fa2-9e85a2b8dfa0");
        Assertions.assertNull(authDAO.getAuth("f224c571-b257-4c6d-9fa2-9e85a2b8dfa0"));
        Assertions.assertNotNull(userDAO.getUser("username"));
    }

    @Test
    @DisplayName("Logout No Auth Token")
    public void logoutNoAuth() throws DataAccessException {
        userDAO.createUser(new UserData("username", "12345", "test@gmail.com"));
        authDAO.createAuth(new AuthData("f224c571-b257-4c6d-9fa2-9e85a2b8dfa0", "username"));
        Assertions.assertThrows(UnauthorizedException.class, () ->
                userService.logout(null));
    }

    @Test
    @DisplayName("Logout Auth Token Doesn't Match")
    public void logoutAuthDoesntMatch() throws DataAccessException {
        userDAO.createUser(new UserData("username", "12345", "test@gmail.com"));
        authDAO.createAuth(new AuthData("f224c571-b257-4c6d-9fa2-9e85a2b8dfa0", "username"));
        Assertions.assertThrows(UnauthorizedException.class, () ->
                userService.logout("g224c571-b257-4c6d-9fa2-9e85a2b8dfa0"));
    }

    @Test
    @DisplayName("List Games Success")
    public void listGamesSuccess() throws DataAccessException, ServiceException {
        userDAO.createUser(new UserData("username", "12345", "test@gmail.com"));
        authDAO.createAuth(new AuthData("f224c571-b257-4c6d-9fa2-9e85a2b8dfa0", "username"));
        ChessGame game = new ChessGame();
        gameDAO.createGame(new GameData(0, "whiteUser", "blackUser", "game", game));
        ListGamesResult result = gameService.listGames("f224c571-b257-4c6d-9fa2-9e85a2b8dfa0");
        Assertions.assertEquals(new ArrayList<>(List.of(new GameDataSummary(1, "whiteUser", "blackUser", "game"))), result.games());
    }

    @Test
    @DisplayName("List Games Multiple")
    public void listGamesMultiple() throws DataAccessException, ServiceException {
        userDAO.createUser(new UserData("username", "12345", "test@gmail.com"));
        authDAO.createAuth(new AuthData("f224c571-b257-4c6d-9fa2-9e85a2b8dfa0", "username"));

        ChessGame game1 = new ChessGame();
        gameDAO.createGame(new GameData(0, "whiteUser1", "blackUser1", "game1", game1));
        ChessGame game2 = new ChessGame();
        gameDAO.createGame(new GameData(0, "whiteUser2", "blackUser2", "game2", game2));

        ListGamesResult result = gameService.listGames("f224c571-b257-4c6d-9fa2-9e85a2b8dfa0");
        Assertions.assertEquals(new ArrayList<>(List.of(
                new GameDataSummary(1, "whiteUser1", "blackUser1", "game1"),
                new GameDataSummary(2, "whiteUser2", "blackUser2", "game2"))), result.games());
    }

    @Test
    @DisplayName("List Games No Auth Token")
    public void listGamesNoAuth() throws DataAccessException {
        userDAO.createUser(new UserData("username", "12345", "test@gmail.com"));
        authDAO.createAuth(new AuthData("f224c571-b257-4c6d-9fa2-9e85a2b8dfa0", "username"));
        Assertions.assertThrows(UnauthorizedException.class, () ->
                gameService.listGames(null));
    }

    @Test
    @DisplayName("List Games Auth Token Doesn't Match")
    public void listGamesAuthDoesntMatch() throws DataAccessException {
        userDAO.createUser(new UserData("username", "12345", "test@gmail.com"));
        authDAO.createAuth(new AuthData("f224c571-b257-4c6d-9fa2-9e85a2b8dfa0", "username"));
        Assertions.assertThrows(UnauthorizedException.class, () ->
                gameService.listGames("g224c571-b257-4c6d-9fa2-9e85a2b8dfa0"));
    }

    @Test
    @DisplayName("Create Games Success")
    public void createGamesSuccess() throws DataAccessException, ServiceException {
        userDAO.createUser(new UserData("username", "12345", "test@gmail.com"));
        authDAO.createAuth(new AuthData("f224c571-b257-4c6d-9fa2-9e85a2b8dfa0", "username"));
        CreateGameResult result = gameService.createGame(new CreateGameRequest("game"), "f224c571-b257-4c6d-9fa2-9e85a2b8dfa0");
        Assertions.assertEquals(1, result.gameID());
        Assertions.assertNotNull(gameDAO.getGame(1));
    }

    @Test
    @DisplayName("Create Game No Auth Token")
    public void createGameNoAuth() throws DataAccessException {
        userDAO.createUser(new UserData("username", "12345", "test@gmail.com"));
        authDAO.createAuth(new AuthData("f224c571-b257-4c6d-9fa2-9e85a2b8dfa0", "username"));
        Assertions.assertThrows(UnauthorizedException.class, () ->
                gameService.createGame(new CreateGameRequest("name"), null));
    }

    @Test
    @DisplayName("Create Game Auth Token Doesn't Match")
    public void createGameAuthDoesntMatch() throws DataAccessException {
        userDAO.createUser(new UserData("username", "12345", "test@gmail.com"));
        authDAO.createAuth(new AuthData("f224c571-b257-4c6d-9fa2-9e85a2b8dfa0", "username"));
        Assertions.assertThrows(UnauthorizedException.class, () ->
                gameService.createGame(new CreateGameRequest("name"), "g224c571-b257-4c6d-9fa2-9e85a2b8dfa0"));
    }

    @Test
    @DisplayName("Create Game Missing Request")
    public void createGameMissingRequest() throws DataAccessException {
        userDAO.createUser(new UserData("username", "12345", "test@gmail.com"));
        authDAO.createAuth(new AuthData("f224c571-b257-4c6d-9fa2-9e85a2b8dfa0", "username"));
        Assertions.assertThrows(BadRequestException.class, () ->
                gameService.createGame(null, "f224c571-b257-4c6d-9fa2-9e85a2b8dfa0"));
    }

    @Test
    @DisplayName("Create Game Missing Args")
    public void createGameMissingArgs() throws DataAccessException {
        userDAO.createUser(new UserData("username", "12345", "test@gmail.com"));
        authDAO.createAuth(new AuthData("f224c571-b257-4c6d-9fa2-9e85a2b8dfa0", "username"));
        Assertions.assertThrows(BadRequestException.class, () ->
                gameService.createGame(new CreateGameRequest(null), "f224c571-b257-4c6d-9fa2-9e85a2b8dfa0"));
        Assertions.assertThrows(BadRequestException.class, () ->
                gameService.createGame(new CreateGameRequest("  "), "f224c571-b257-4c6d-9fa2-9e85a2b8dfa0"));
    }

    @Test
    @DisplayName("Join Game Success White")
    public void joinGameSuccessWhite() throws DataAccessException, ServiceException {
        userDAO.createUser(new UserData("username", "12345", "test@gmail.com"));
        authDAO.createAuth(new AuthData("f224c571-b257-4c6d-9fa2-9e85a2b8dfa0", "username"));
        ChessGame game = new ChessGame();
        gameDAO.createGame(new GameData(0, null, null, "game", game));
        gameService.joinGame(new JoinGameRequest(ChessGame.TeamColor.WHITE, 1), "f224c571-b257-4c6d-9fa2-9e85a2b8dfa0");
        Assertions.assertEquals("username", gameDAO.getGame(1).whiteUsername());
        Assertions.assertNull(gameDAO.getGame(1).blackUsername());
    }

    @Test
    @DisplayName("Join Game Success Black")
    public void joinGameSuccessBlack() throws DataAccessException, ServiceException {
        userDAO.createUser(new UserData("username", "12345", "test@gmail.com"));
        authDAO.createAuth(new AuthData("f224c571-b257-4c6d-9fa2-9e85a2b8dfa0", "username"));
        ChessGame game = new ChessGame();
        gameDAO.createGame(new GameData(0, null, null, "game", game));
        gameService.joinGame(new JoinGameRequest(ChessGame.TeamColor.BLACK, 1), "f224c571-b257-4c6d-9fa2-9e85a2b8dfa0");
        Assertions.assertEquals("username", gameDAO.getGame(1).blackUsername());
        Assertions.assertNull(gameDAO.getGame(1).whiteUsername());
    }

    @Test
    @DisplayName("Join Game No Auth Token")
    public void joinGameNoAuth() throws DataAccessException {
        userDAO.createUser(new UserData("username", "12345", "test@gmail.com"));
        authDAO.createAuth(new AuthData("f224c571-b257-4c6d-9fa2-9e85a2b8dfa0", "username"));
        Assertions.assertThrows(UnauthorizedException.class, () ->
                gameService.joinGame(new JoinGameRequest(ChessGame.TeamColor.WHITE, 1), null));
    }

    @Test
    @DisplayName("Join Game Auth Token Doesn't Match")
    public void joinGameAuthDoesntMatch() throws DataAccessException {
        userDAO.createUser(new UserData("username", "12345", "test@gmail.com"));
        authDAO.createAuth(new AuthData("f224c571-b257-4c6d-9fa2-9e85a2b8dfa0", "username"));
        Assertions.assertThrows(UnauthorizedException.class, () ->
                gameService.joinGame(new JoinGameRequest(ChessGame.TeamColor.WHITE, 1), "g224c571-b257-4c6d-9fa2-9e85a2b8dfa0"));
    }

    @Test
    @DisplayName("Join Game Doesn't Exist")
    public void joinGameDoesntExist() throws DataAccessException {
        userDAO.createUser(new UserData("username", "12345", "test@gmail.com"));
        authDAO.createAuth(new AuthData("f224c571-b257-4c6d-9fa2-9e85a2b8dfa0", "username"));
        Assertions.assertThrows(BadRequestException.class, () ->
                gameService.joinGame(new JoinGameRequest(ChessGame.TeamColor.WHITE, 1), "f224c571-b257-4c6d-9fa2-9e85a2b8dfa0"));
    }

    @Test
    @DisplayName("Join Game Missing Args")
    public void joinGameMissingArgs() throws DataAccessException {
        userDAO.createUser(new UserData("username", "12345", "test@gmail.com"));
        authDAO.createAuth(new AuthData("f224c571-b257-4c6d-9fa2-9e85a2b8dfa0", "username"));
        Assertions.assertThrows(BadRequestException.class, () ->
                gameService.joinGame(new JoinGameRequest(null, 1), "f224c571-b257-4c6d-9fa2-9e85a2b8dfa0"));
    }

    @Test
    @DisplayName("Join Game User Taken White")
    public void joinGameUserTakenWhite() throws DataAccessException {
        userDAO.createUser(new UserData("username", "12345", "test@gmail.com"));
        authDAO.createAuth(new AuthData("f224c571-b257-4c6d-9fa2-9e85a2b8dfa0", "username"));
        userDAO.createUser(new UserData("whiteUser", "54321", "white@gmail.com"));
        ChessGame game = new ChessGame();
        gameDAO.createGame(new GameData(0, "whiteUser", null, "game", game));
        Assertions.assertThrows(AlreadyTakenException.class, () ->
                gameService.joinGame(new JoinGameRequest(ChessGame.TeamColor.WHITE, 1), "f224c571-b257-4c6d-9fa2-9e85a2b8dfa0"));
        Assertions.assertEquals(new GameData(1, "whiteUser", null, "game", game), gameDAO.getGame(1));
    }

    @Test
    @DisplayName("Join Game User Taken Black")
    public void joinGameUserTakenBlack() throws DataAccessException {
        userDAO.createUser(new UserData("username", "12345", "test@gmail.com"));
        authDAO.createAuth(new AuthData("f224c571-b257-4c6d-9fa2-9e85a2b8dfa0", "username"));
        userDAO.createUser(new UserData("blackUser", "54321", "black@gmail.com"));
        ChessGame game = new ChessGame();
        gameDAO.createGame(new GameData(0, null, "blackUser", "game", game));
        Assertions.assertThrows(AlreadyTakenException.class, () ->
                gameService.joinGame(new JoinGameRequest(ChessGame.TeamColor.BLACK, 1), "f224c571-b257-4c6d-9fa2-9e85a2b8dfa0"));
        Assertions.assertEquals(new GameData(1, null, "blackUser", "game", game), gameDAO.getGame(1));
    }
}


