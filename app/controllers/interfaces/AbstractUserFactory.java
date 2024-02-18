package controllers.interfaces;

import models.UserFactory;

import java.util.List;

public interface AbstractUserFactory{
    User getUserById(int id);

    User getUserById(String id);

    List<User> getAllUsers();

    List<User> getFriends(int userId);
}
