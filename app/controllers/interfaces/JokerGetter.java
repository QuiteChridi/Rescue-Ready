package controllers.interfaces;

import java.util.List;

/**
 * Interface for getting all jokers of a user
 */
public interface JokerGetter {
    /**
     * Get all jokers of a user
     * @param userId the user id
     * @return a list of jokers
     */
    List<Joker> getAllJokers(int userId);
}
