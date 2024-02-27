package controllers.interfaces;

/**
 * Interface for the AccountManager class.
 * This class is responsible for authenticating users and creating/deleting users.
 */
public interface AccountManager {
    /**
     * Authenticates a user with the given username and password.
     *
     * @param username The username of the user.
     * @param password The password of the user.
     * @return The user object if the user is authenticated, null otherwise.
     */
    User authenticate(String username, String password);

    /**
     * Creates a new user with the given name, password, and email.
     *
     * @param name     The name of the user.
     * @param password The password of the user.
     * @param email    The email of the user.
     * @return The user object if the user is created, null otherwise.
     */
    User createUser(String name, String password, String email);

    /**
     * Deletes the user with the given user ID.
     *
     * @param userId The ID of the user to delete.
     */
    void deleteUser(int userId);
}
