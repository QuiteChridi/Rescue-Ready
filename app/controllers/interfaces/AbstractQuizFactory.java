package controllers.interfaces;

import java.util.Map;

/**
 * Interface for the QuizFactory class that is used to get quizzes.
 */
public interface AbstractQuizFactory {
    /**
     * Gets a quiz by its id.
     *
     * @param id
     * @return The quiz with the given id.
     */
    Quiz getQuizById(int id);

    /**
     * Gets a quiz by its name.
     *
     * @return The quiz with the given name.
     */
    Map<Integer, String> getPossibleQuizNames();
}
