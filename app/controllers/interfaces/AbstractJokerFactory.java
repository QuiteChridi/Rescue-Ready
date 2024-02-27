package controllers.interfaces;

import java.util.List;

/**
 * Interface for JokerFactory class that provides methods to get jokers and their amount of user.
 */
public interface AbstractJokerFactory extends JokerGetter {
    /**
     * Get joker by id.
     *
     * @param id     Id of joker.
     * @param userId Id of user.
     * @return Joker.
     */
    Joker getJokerById(int id, int userId);

    /**
     * Get jokers of user.
     *
     * @param jokerId Id of joker.
     * @param userId  Id of user.
     * @return List of jokers.
     */
    int getJokerAmountOfUser(int jokerId, int userId);

    /**
     * Set joker amount of user.
     *
     * @param jokerId Id of joker.
     * @param userId  Id of user.
     * @param amount  Amount of joker.
     */
    void setJokerAmountOfUser(int jokerId, int userId, int amount);
}
