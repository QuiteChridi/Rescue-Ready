package models;

import controllers.interfaces.Scoreboard;

import java.util.*;

public class DummyScoreboard implements Scoreboard {

    private static List<Highscore> highscores;

    public DummyScoreboard() {
        generateRandomHighscores();
    }

    @Override
    public List<Highscore> getHighscores(){
        highscores.sort(Highscore::compareTo);

        return new ArrayList<>(highscores);
    }

    @Override
    public void updateHighscore(String name, int score){
        if(!isInHighscoreList(name)) throw new IllegalArgumentException("Name is not in List");

        getHighscore(name).setScore(score);
    }

    @Override
    public boolean isInHighscoreList(String name){
        return highscores.stream()
                .anyMatch(h -> (h.getName()
                        .equals(name)));
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
        highscores.sort(Highscore::compareTo);
        return highscores.indexOf(getHighscore(name)) + 1;
    }

    private void generateRandomHighscores(){
        highscores = new ArrayList<>();
        highscores.add(new Highscore("Willi",(int) (Math.random() * 100)));
        highscores.add(new Highscore("Hubert",(int) (Math.random() * 100)));
        highscores.add(new Highscore("Sepp",(int) (Math.random() * 100)));
        highscores.add(new Highscore("Fred",(int) (Math.random() * 100)));
        highscores.add(new Highscore("Paul",(int) (Math.random() * 100)));
    }
}