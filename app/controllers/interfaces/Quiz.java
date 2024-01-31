package controllers.interfaces;

public abstract class Quiz {
    public abstract QuizQuestion getCurrentQuestion();

    public abstract void nextQuestion();

    public abstract boolean hasNextQuestion();

    public abstract boolean isCorrectAnswer(String answer);

    public abstract String getCorrectAnswer();

    public abstract int getId();

    public abstract String getName();
}
