package oldModels;

import controllers.interfaces.ScoreboardInterface;
import models.HighscoreFactory;

import java.util.*;


public class DummyScoreboard implements ScoreboardInterface {
    private static DummyScoreboard INSTANCE;
    private List<HighscoreFactory.Highscore> highscores;

    private DummyScoreboard() {
        generateRandomHighscores();
    }

    public synchronized static DummyScoreboard getInstance(){
        if(INSTANCE == null){
            INSTANCE = new DummyScoreboard();
        }
        return INSTANCE;
    }

    public List<HighscoreFactory.Highscore> getHighscores(){
        highscores.sort(HighscoreFactory.Highscore::compareTo);

        return new ArrayList<>(highscores);
    }

    public void updateHighscore(String name, int score){
        if(!isInHighscoreList(name)) throw new IllegalArgumentException("Name is not in List");

        getHighscore(name).setScore(score);
    }


    public boolean isInHighscoreList(String name){
        return highscores.stream()
                .anyMatch(h -> (h.getUserName()
                        .equals(name)));
    }

    public HighscoreFactory.Highscore getHighscore(String name){
        return highscores.stream()
                .filter(h -> (h
                        .getUserName()
                        .equals(name)))
                .findFirst()
                .orElse(null);
    }

    public int getRank(String name){
        highscores.sort(HighscoreFactory.Highscore::compareTo);
        return highscores.indexOf(getHighscore(name)) + 1;
    }

    @Override
    public List<HighscoreFactory.Highscore> getHighscoresOfQuiz(int quizId) {
        return null;
    }

    @Override
    public boolean isInHighscoreList(String userName, String quizName) {
        return false;
    }

    private void generateRandomHighscores(){
        highscores = new ArrayList<>();
        highscores.add(new HighscoreFactory.Highscore("Willi",1, (int) (Math.random() * 100)));
        highscores.add(new HighscoreFactory.Highscore("Hubert",1, (int) (Math.random() * 100)));
        highscores.add(new HighscoreFactory.Highscore("Sepp",1, (int) (Math.random() * 100)));
        highscores.add(new HighscoreFactory.Highscore("Fred",1, (int) (Math.random() * 100)));
        highscores.add(new HighscoreFactory.Highscore("Paul",1, (int) (Math.random() * 100)));
    }

}