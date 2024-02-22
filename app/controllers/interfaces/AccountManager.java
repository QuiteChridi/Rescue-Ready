package controllers.interfaces;

public interface AccountManager {
    User authenticate(String username, String password);

    User createUser(String name, String password, String email);

    void deleteUser(int userId);
}
