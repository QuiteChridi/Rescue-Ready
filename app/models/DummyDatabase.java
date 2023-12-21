package models;

import java.util.*;

public class DummyDatabase implements Database {
    static List<Highscore> highscores;

    @Override
    public List<Highscore> getHighscores(){
        if(highscores == null){
            generateRandomHighscores();
        }
        return highscores;
    }

    @Override
    public void updateHighscore(String name, int score){
        if(!isInList(name)) throw new IllegalArgumentException("Name is not in List");

        getHighscore(name).setScore(score);
    }

    @Override
    public boolean isInList(String name){
        return highscores.stream()
                .anyMatch(h -> (h.getName()
                        .equals(name)));
    }

    @Override
    public List<QuizQuestion> getQuizQuestions() {
        return generateDummyQuiz();
    }

    @Override
    public void addHighscore(String name, int score){
        highscores.add(new Highscore(name, score));
    }

    @Override
    public Highscore getHighscore(String name){
        return highscores.stream()
                .filter(h -> (h
                        .getName()
                        .equals(name)))
                .findFirst()
                .orElse(null);
    }

    @Override
    public int getRank(String name){
        return highscores.indexOf(getHighscore(name));
    }

    private void generateRandomHighscores(){
        highscores = new ArrayList<>();
        highscores.add(new Highscore("Willi",(int) (Math.random() * 100)));
        highscores.add(new Highscore("Hubert",(int) (Math.random() * 100)));
        highscores.add(new Highscore("Sepp",(int) (Math.random() * 100)));
        highscores.add(new Highscore("Fred",(int) (Math.random() * 100)));
        highscores.add(new Highscore("Paul",(int) (Math.random() * 100)));
    }

    private void generateDummyQuiz(){
        List<QuizQuestion> quiz = new ArrayList<>();
        List<String> answers = new ArrayList<>();
        answers.add("Den Kopf nach hinten neigen");
        answers.add("Den Kopf nach vorne neigen");
        answers.add("Kopf in neutraler Position halten");
        answers.add("Wasser trinken, um das Blut zu stoppen");

        new QuizQuestion("Was ist die richtige Vorgehensweise bei einem Nasenbluten?",List.of(), "Mit kaltem Wasser abspülen");
    }
}
