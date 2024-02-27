package controllers.interfaces;

/**
 * Abstract class for Highscore object to be used in the HighscoreFactory class.
 */
public abstract class Highscore {
    /**
     * Returns the id of the quiz.
     *
     * @return the id of the quiz.
     */
    public abstract int getQuizId();

    /**
     * Returns the id of the user.
     *
     * @return the id of the user.
     */
    public abstract int getUserId();

    /**
     * Returns the score of the user.
     *
     * @return the score of the user.
     */
    public abstract int getScore();

    /**
     * Returns the path to the profile picture of the user.
     *
     * @return the path to the profile picture of the user.
     */
    public abstract String getProfilePicPath();

    /**
     * Returns the name of the user.
     *
     * @return the name of the user.
     */
    public abstract String getUserName();

    /**
     * Returns the name of the quiz.
     *
     * @return the name of the quiz.
     */
    public abstract String getQuizName();

    /**
     * Compares this Highscore object with another Highscore object.
     *
     * @param that the Highscore object to compare with.
     * @return a negative integer, zero, or a positive integer as this Highscore object is less than, equal to, or greater than the specified Highscore object.
     */
    public abstract int compareTo(Highscore that);
}
