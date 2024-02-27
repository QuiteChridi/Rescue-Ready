package controllers.interfaces;

/**
 * Abstract class for Joker object to be used in the JokerFactory class.
 */
public abstract class Joker {

    /**
     * Returns the id of the joker.
     *
     * @return the id of the joker.
     */
    public abstract int getId();

    /**
     * Returns the name of the joker.
     *
     * @return the name of the joker.
     */
    public abstract String getName();

    /**
     * Returns the description of the joker.
     *
     * @return the description of the joker.
     */
    public abstract String getDescription();

    /**
     * Returns the price of the joker.
     *
     * @return the price of the joker.
     */
    public abstract int getPrice();

    /**
     * Returns the path to the picture of the joker.
     *
     * @return the path to the picture of the joker.
     */
    public abstract String getJokerPicPath();

    /**
     * Returns the amount of the joker.
     *
     * @return the amount of the joker.
     */
    public abstract int getAmount();
}
