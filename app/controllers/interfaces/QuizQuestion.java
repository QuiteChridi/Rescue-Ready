package controllers.interfaces;

import java.util.List;

/**
 * Interface for the QuizQuestion class that is used to create and manage quiz questions.
 */
public abstract class QuizQuestion {
    /**
     * Gets the text of the question.
     *
     * @return The text of the question.
     */
    public abstract String getQuestionText();

    /**
     * Gets the answers to the question.
     *
     * @return A list of answers to the question.
     */
    public abstract List<String> getAnswers();

    /**
     * Gets the correct answer to the question.
     *
     * @return The correct answer to the question.
     */
    public abstract String getCorrectAnswer();

    /**
     * Checks if the answer is correct.
     *
     * @param answer The answer to be checked.
     * @return True if the answer is correct, false otherwise.
     */
    public abstract boolean isCorrectAnswer(String answer);
}
