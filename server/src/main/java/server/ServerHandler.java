package server;

import com.google.gson.Gson;
import dataaccess.DataAccessException;
import io.javalin.http.Context;
import model.*;
import service.ClearService;
import service.GameService;
import service.UserService;
import service.exception.ServiceException;

import java.util.Map;


public class ServerHandler {
    private final ClearService clearService;
    private final GameService gameService;
    private final UserService userService;
    private final Gson gson = new Gson();

    public ServerHandler(ClearService clearService, GameService gameService, UserService userService) {
        this.clearService = clearService;
        this.gameService = gameService;
        this.userService = userService;
    }

    public void clear(Context ctx) throws DataAccessException {
        clearService.clearAllData();
        ctx.result("{}");
    }

    public void register(Context ctx) throws DataAccessException, ServiceException {
        RegisterRequest registerRequest = gson.fromJson(ctx.body(), RegisterRequest.class);
        RegisterResult registerResult = userService.register(registerRequest);
        ctx.result(gson.toJson(registerResult));
    }

    public void login(Context ctx) throws DataAccessException, ServiceException {
        LoginRequest loginRequest = gson.fromJson(ctx.body(), LoginRequest.class);
        LoginResult loginResult = userService.login(loginRequest);
        ctx.result(gson.toJson(loginResult));
    }

    public void logout(Context ctx) throws DataAccessException, ServiceException {
        userService.logout(ctx.header("authorization"));
        ctx.result("{}");
    }

    public void listGames(Context ctx) throws DataAccessException, ServiceException {
        ListGamesResult listGamesResult = gameService.listGames(ctx.header("authorization"));
        ctx.result(gson.toJson(listGamesResult));
    }

    public void createGame(Context ctx) throws DataAccessException, ServiceException {
        CreateGameRequest createGameRequest = gson.fromJson(ctx.body(), CreateGameRequest.class);
        CreateGameResult createGameResult = gameService.createGame(createGameRequest, ctx.header("authorization"));
        ctx.result(gson.toJson(createGameResult));
    }

    public void joinGame(Context ctx) throws DataAccessException, ServiceException {
        JoinGameRequest joinGameRequest = gson.fromJson(ctx.body(), JoinGameRequest.class);
        gameService.joinGame(joinGameRequest, ctx.header("authorization"));
        ctx.result("{}");
    }

    public void handleDataAccessException(DataAccessException ex, Context ctx) {
        ctx.status(500);
        ctx.result(errorJson(ex.getMessage()));
    }

    public void handleServiceException(ServiceException ex, Context ctx) {
        ctx.status(ex.getStatusCode());
        ctx.result(errorJson(ex.getMessage()));
    }

    public void handleException(Exception ex, Context ctx) {
        ctx.status(500);
        ctx.result(errorJson(ex.getMessage()));
    }

    private String errorJson(String message) {
        String input = "Error: " + message;
        Map<String, String> output = Map.of("message", input);
        return gson.toJson(output);
    }
}
