package models;

import controllers.interfaces.QuizInterface;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class DummyQuizTest {
    private QuizInterface quiz;
    @Before
    public void setUp(){
        quiz = DummyQuizInterface.getInstance();
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