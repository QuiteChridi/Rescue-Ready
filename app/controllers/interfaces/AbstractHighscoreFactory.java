package controllers.interfaces;

import java.util.List;
import java.util.Map;

/**
 * Interface for HighscoreFactory class that provides methods to add highscores to the database and get highscores from the database
 */
public interface AbstractHighscoreFactory {
    /**
     * Adds a highscore to the database and returns true if the highscore was added successfully, false otherwise
     *
     * @param quizId the id of the quiz
     * @return true if the highscore was added successfully, false otherwise
     */
    List<Highscore> getHighscoresOfQuiz(int quizId);

    /**
     * Gets the highscores of a user from the database and returns them as a list
     *
     * @param userId the id of the user
     * @return a list of highscores
     */
    List<Highscore> getHighscoresOfUser(int userId);

    /**
     * Gets possible quiz names from the database and returns them as a map
     *
     * @return a map of possible quiz names with their ids as keys and names as values
     */
    Map<Integer, String> getPossibleQuizNames();

    /**
     * Gets the highscore of a user and a quiz from the database and returns it
     *
     * @param userId the id of the user
     * @param quizId the id of the quiz
     * @return the highscore of the user and the quiz
     */
    Highscore getHighscoreOfUserAndQuiz(int userId, int quizId);

    /**
     * Creates a highscore in the database
     *
     * @param userId the id of the user
     * @param quizID the id of the quiz
     * @param score  the score of the user
     */
    void createHighscore(int userId, int quizID, int score);
}
