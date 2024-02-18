package controllers.interfaces;

import java.util.List;

public interface AbstractUserFactory{
    User getUserById(int id);

    User getUserById(String id);
}
