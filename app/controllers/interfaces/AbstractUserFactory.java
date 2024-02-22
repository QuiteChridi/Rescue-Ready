package controllers.interfaces;

public interface AbstractUserFactory{
    User getUserById(int id);

    User getUserById(String id);
}
