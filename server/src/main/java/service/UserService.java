package service;

import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.UserDAO;
import model.AuthData;
import model.RegisterRequest;
import model.RegisterResult;
import model.UserData;
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


//    public LoginResult login(LoginRequest loginRequest) {}
//    public void logout() {}

    private boolean isMissing(String input) {
        return input == null || input.isBlank();
    }

}
