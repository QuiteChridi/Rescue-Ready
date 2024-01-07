package models;

import org.junit.Test;

import static org.junit.Assert.*;

public class DummyQuizTest {
    @Test
    public void getNextQuizQuestionShouldReturnAQuestionAsLongAsThereIsAny(){
        var quiz = new DummyQuiz();
        quiz.nextQuestion();

        while(quiz.hasNextQuestion()){
            assertNotNull(quiz.getCurrentQuestion());
            quiz.nextQuestion();
        }
        quiz.nextQuestion();
        assertNull(quiz.getCurrentQuestion());
    }

    @Test
    public void isCorrectShouldMatchgetCorrectAnswer(){
        var quiz = new DummyQuiz();
        quiz.nextQuestion();
        assertTrue(quiz.isCorrectAnswer(quiz.getCorrectAnswer()));
    }
    @Test
    public void isCorrectShouldReturnFalseOnIncorrectAnswer(){
        var quiz = new DummyQuiz();
        quiz.nextQuestion();
        assertFalse(quiz.isCorrectAnswer(quiz.getCorrectAnswer() + "x"));
    }
}