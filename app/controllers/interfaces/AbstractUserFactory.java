package controllers.interfaces;

import models.UserFactory;

import java.util.List;

public interface AbstractUserFactory{
    User getUserById(int id);

    User getUserById(String id);
}
