package models;

import controllers.interfaces.Quiz;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class DummyQuizTest {
    private Quiz quiz;
    @Before
    public void setUp(){
        quiz = DummyQuiz.getInstance();
        quiz.nextQuestion();
    }
    @Test
    public void getNextQuizQuestionShouldReturnAQuestionAsLongAsThereIsAny(){
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
        assertTrue(quiz.isCorrectAnswer(quiz.getCorrectAnswer()));
    }
    @Test
    public void isCorrectShouldReturnFalseOnIncorrectAnswer(){
        assertFalse(quiz.isCorrectAnswer(quiz.getCorrectAnswer() + "x"));
    }
}