package models;

import org.junit.Test;

import static org.junit.Assert.*;

public class DummyQuizTest {
    @Test
    public void getNextQuizQuestionShouldReturnAQuestionAsLongAsThereIsAny(){
        var quiz = DummyQuiz.getInstance();
        quiz.nextQuestion();

        while(quiz.hasNextQuestion()){
            assertNotNull(quiz.getCurrentQuestion());
            quiz.nextQuestion();
        }
        quiz.nextQuestion();
        assertNull(quiz.getCurrentQuestion());
        quiz.resetQuiz();
    }

    @Test
    public void isCorrectShouldMatchgetCorrectAnswer(){
        var quiz = DummyQuiz.getInstance();
        quiz.nextQuestion();
        assertTrue(quiz.isCorrectAnswer(quiz.getCorrectAnswer()));
    }
    @Test
    public void isCorrectShouldReturnFalseOnIncorrectAnswer(){
        var quiz = DummyQuiz.getInstance();
        quiz.nextQuestion();
        assertFalse(quiz.isCorrectAnswer(quiz.getCorrectAnswer() + "x"));
    }
}