package controllers.interfaces;

/**
 * AbstractUserFactory interface is used to create User objects without specifying the type of object to be created.
 */
public interface AbstractUserFactory {
    /**
     * Get user by id from the database.
     *
     * @param id
     * @return User object with the given id.
     */
    User getUserById(int id);

    /**
     * Get user by id from the database.
     *
     * @param id
     * @return User object with the given id.
     */
    User getUserById(String id);
}
