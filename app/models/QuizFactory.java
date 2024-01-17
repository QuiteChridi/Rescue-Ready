package models;

import controllers.interfaces.Quiz;

import java.util.ArrayList;
import java.util.List;

public class QuizFactory {

    public static Quiz getQuiz(String name){
        return DummyQuiz.getInstance();
    }
    public static List<String> getPossibleQuizes(){
        List<String> possibleQuizes = new ArrayList<>();
        possibleQuizes.add("FirstQuiz");
        possibleQuizes.add("SecondQuiz");
        possibleQuizes.add("ThirdQuiz");

        return possibleQuizes;
    }
}
