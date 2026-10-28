package service;

import chess.ChessGame;
import dataaccess.*;
import model.AuthData;
import model.GameData;
import model.RegisterRequest;
import model.UserData;
import org.junit.jupiter.api.*;
import service.exception.*;

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
    public void registerSameUsername() throws ServiceException, DataAccessException {
        userDAO.createUser(new UserData("username", "12345", "test@gmail.com"));
        Assertions.assertThrows(AlreadyTakenException.class, () ->
                userService.register(new RegisterRequest("username", "54321", "bob@gmail.com")));
        Assertions.assertEquals(new UserData("username", "12345", "test@gmail.com"), userDAO.getUser("username"));
    }

    @Test
    @DisplayName("Register Missing Args")
    public void registerMissingArgs() throws ServiceException, DataAccessException {
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

}


