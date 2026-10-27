package server;

import io.javalin.*;
import dataaccess.*;
import service.*;

public class Server {

    private final Javalin javalin;

    public Server() {
        AuthDAO authDAO = new MemoryAuthDAO();
        GameDAO gameDAO = new MemoryGameDAO();
        UserDAO userDAO = new MemoryUserDAO();

        ClearService clearService = new ClearService(authDAO, gameDAO, userDAO);
        GameService gameService = new GameService(authDAO, gameDAO);
        UserService userService = new UserService(authDAO, userDAO);

        ServerHandler serverHandler = new ServerHandler(clearService, gameService, userService);

        javalin = Javalin.create(config -> config.staticFiles.add("web"))
                .delete("/db", serverHandler::clear)
                .post("/user", serverHandler::register);
    }

    public int run(int desiredPort) {
        javalin.start(desiredPort);
        return javalin.port();
    }

    public void stop() {
        javalin.stop();
    }
}
