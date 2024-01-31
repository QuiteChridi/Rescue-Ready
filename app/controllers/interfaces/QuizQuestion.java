package controllers.interfaces;

import java.util.List;

public abstract class QuizQuestion {
    public abstract String getQuestionText();

    public abstract List<String> getAnswers();

    public abstract String getCorrectAnswer();

    public abstract boolean isCorrectAnswer(String answer);
}
