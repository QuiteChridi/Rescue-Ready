package controllers.interfaces;

import java.util.List;

public interface AbstractUserFactory {
    User authenticate(String username, String password);

    User createUserInUsers(String name, String password, String email);

    User getUserById(int id);

    User getUserById(String id);

    List<User> getAllUsers();
}
