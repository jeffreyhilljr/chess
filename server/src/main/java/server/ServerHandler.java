package server;

import dataaccess.DataAccessException;
import io.javalin.http.Context;
import service.ClearService;
import service.GameService;
import service.UserService;


public class ServerHandler {
    private final ClearService clearService;
    private final GameService gameService;
    private final UserService userService;

    public ServerHandler(ClearService clearService, GameService gameService, UserService userService) {
        this.clearService = clearService;
        this.gameService = gameService;
        this.userService = userService;
    }

    public void clear(Context ctx) throws DataAccessException {
        clearService.clearAllData();
        ctx.result("{}");
    }

}
