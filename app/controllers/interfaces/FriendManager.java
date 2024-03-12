package controllers.interfaces;

import java.util.List;

/**
 * Interface for FriendManager class that provides methods to add and remove friends, search for users, get all users, get friends of a user and get users that are not friends of a user
 */
public interface FriendManager {
    /**
     * Adds a friend to the database and returns true if the friend was added successfully, false otherwise
     *
     * @param userId   the id of the user
     * @param friendId the id of the friend
     * @return true if the friend was added successfully, false otherwise
     */
    boolean addFriend(int userId, int friendId);

    /**
     * Removes a friend from the database and returns true if the friend was removed successfully, false otherwise
     *
     * @param userId   the id of the user
     * @param friendId the id of the friend
     * @return true if the friend was removed successfully, false otherwise
     */
    boolean removeFriend(int userId, int friendId);

    /**
     * Searches for users by their name and returns a list of users
     *
     * @param searchQuery the name of the user
     * @return a list of users
     */
    List<User> searchUsersByName(String searchQuery);

    /**
     * Gets all users from the database and returns them as a list
     *
     * @return a list of users
     */
    List<User> getAllUsers();

    /**
     * Gets the friends of a user from the database and returns them as a list
     *
     * @param userId the id of the user
     * @return a list of friends
     */
    List<User> getFriends(int userId);

    /**
     * Gets the users that are not friends of a user from the database and returns them as a list
     *
     * @param userId the id of the user
     * @return a list of users that are not friends of the user
     */
    List<User> getNotFriends(int userId);

    /**
     * Checks if a user is a friend of another user and returns true if the user is a friend, false otherwise
     *
     * @param userId      the id of the user
     * @param otherUserId the id of the other user
     * @return true if the user is a friend, false otherwise
     */
    boolean isFriend(int userId, int otherUserId);

    /**
     * Gets a user by their id from the database and returns the user
     *
     * @param id the id of the user
     * @return the user
     */
    User getUserById(int id);
}