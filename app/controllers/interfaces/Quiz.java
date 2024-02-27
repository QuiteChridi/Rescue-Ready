package controllers.interfaces;

/**
 * Interface for the Quiz class that is used to create and manage quizzes.
 */
public abstract class Quiz {
    /**
     * Gets the current question of the quiz.
     *
     * @return The current question of the quiz.
     */
    public abstract QuizQuestion getCurrentQuestion();

    /**
     * Gets the next question of the quiz.
     */
    public abstract void nextQuestion();

    /**
     * Checks if there is a next question in the quiz.
     *
     * @return True if there is a next question, false otherwise.
     */
    public abstract boolean hasNextQuestion();

    /**
     * Checks if the answer is correct.
     *
     * @param answer The answer to be checked.
     * @return True if the answer is correct, false otherwise.
     */
    public abstract boolean isCorrectAnswer(String answer);

    /**
     * Gets the correct answer to the current question.
     *
     * @return The correct answer to the current question.
     */
    public abstract String getCorrectAnswer();

    /**
     * Gets the id of the quiz.
     *
     * @return The id of the quiz.
     */
    public abstract int getId();

    /**
     * Gets the name of the quiz.
     *
     * @return The name of the quiz.
     */
    public abstract String getName();
}
