package service;

import dataaccess.*;
import model.*;
import service.exception.*;

import java.util.UUID;

public class UserService {
    private final AuthDAO authDAO;
    private final UserDAO userDAO;

    public UserService (AuthDAO authDAO, UserDAO userDAO) {
        this.authDAO = authDAO;
        this.userDAO = userDAO;
    }

    public RegisterResult register(RegisterRequest registerRequest) throws DataAccessException, ServiceException {
        if (registerRequest == null) {
            throw new BadRequestException();
        }

        String username = registerRequest.username();
        String password = registerRequest.password();
        String email = registerRequest.email();

        if (isMissing(username) || isMissing(password) || isMissing(email)) {
            throw new BadRequestException();
        }

        if (userDAO.getUser(username) != null) {
            throw new AlreadyTakenException();
        }

        UserData user = new UserData(username, password, email);
        AuthData auth = new AuthData(UUID.randomUUID().toString() , username);

        userDAO.createUser(user);
        authDAO.createAuth(auth);

        return new RegisterResult(username, auth.authToken());
    }


    public LoginResult login(LoginRequest loginRequest) throws DataAccessException, ServiceException {
        if (loginRequest == null) {
            throw new BadRequestException();
        }

        String username = loginRequest.username();
        String password = loginRequest.password();

        if (isMissing(username) || isMissing(password)) {
            throw new BadRequestException();
        }

        UserData user = userDAO.getUser(username);

        if (user == null) {
            throw new UnauthorizedException();
        }

        AuthData auth = new AuthData(UUID.randomUUID().toString() , username);
        authDAO.createAuth(auth);

        return new LoginResult(username, auth.authToken());
    }


    public void logout(String authToken) throws DataAccessException, ServiceException {
        if (authToken == null) {
            throw new UnauthorizedException();
        }

        AuthData authData = authDAO.getAuth(authToken);

        if (authData == null) {
            throw new UnauthorizedException();
        }

        authDAO.deleteAuth(authToken);
    }

    private boolean isMissing(String input) {
        return input == null || input.isBlank();
    }

}
