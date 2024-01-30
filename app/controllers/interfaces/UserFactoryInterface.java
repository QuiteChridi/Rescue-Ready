package controllers.interfaces;

import models.UserFactory;

import java.util.List;

public interface UserFactoryInterface {
    UserFactory.User authenticate(String username, String password);

    UserFactory.User createUserInUsers(String name, String password, String email);

    UserFactory.User getUserById(int id);

    UserFactory.User getUserById(String id);

    List<UserFactory.User> getAllUsers();
}
