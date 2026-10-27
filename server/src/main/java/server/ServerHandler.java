package server;

import com.google.gson.Gson;
import dataaccess.DataAccessException;
import io.javalin.http.Context;
import model.RegisterRequest;
import model.RegisterResult;
import service.ClearService;
import service.GameService;
import service.UserService;
import service.exception.ServiceException;


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

}
