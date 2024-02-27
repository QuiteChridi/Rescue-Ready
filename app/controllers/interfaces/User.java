package controllers.interfaces;

/**
 * Interface for the User class that is used to create and manage users.
 */
public abstract class User {
    /**
     * Saves the user to the database.
     */
    public abstract void save();

    /**
     * Gets the number of coins the user has.
     *
     * @return The number of coins the user has.
     */
    public abstract int getCoins();

    /**
     * Sets the number of coins the user has.
     *
     * @param coins The number of coins the user has.
     */
    public abstract void setCoins(int coins);

    /**
     * Gets the path to the profile picture of the user.
     *
     * @return The path to the profile picture of the user.
     */
    public abstract String getProfilePicPath();

    /**
     * Sets the path to the profile picture of the user.
     *
     * @param profilePicPath The path to the profile picture of the user.
     */
    public abstract void setProfilePicPath(String profilePicPath);

    /**
     * Gets the id of the user.
     *
     * @return The id of the user.
     */
    public abstract int getId();

    /**
     * Gets the username of the user.
     *
     * @return The username of the user.
     */
    public abstract String getName();

    /**
     * Sets the username of the user.
     *
     * @param username The username of the user.
     */
    public abstract void setName(String username);

    /**
     * Gets the mail of the user.
     *
     * @return The mail of the user.
     */
    public abstract String getMail();

    /**
     * Sets the mail of the user.
     *
     * @param mail The mail of the user.
     */
    public abstract void setMail(String mail);

    /**
     * Gets the password of the user.
     *
     * @return The password of the user.
     */
    public abstract String getPassword();

    /**
     * Sets the password of the user.
     *
     * @param password The password of the user.
     */
    public abstract void setPassword(String password);
}
