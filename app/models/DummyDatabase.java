package models;
import java.util.*;

public class DummyDatabase {
    static List<Highscore> highscores;

    public static List<Highscore> getHighscores(){
        if(highscores == null){
            generateRandomHighscores();
        }
        return highscores;
    }

    public static void updateHighscore(String name, int score){
        if(!isInList(name)) throw new IllegalArgumentException("Name is not in List");

        getHighscore(name).setScore(score);
    }
    public static void addHighscore(String name, int score){
        highscores.add(new Highscore(name, score));
    }

    public static void generateRandomHighscores(){
        highscores = new ArrayList<>();
        highscores.add(new Highscore("Willi",(int) (Math.random() * 100)));
        highscores.add(new Highscore("Hubert",(int) (Math.random() * 100)));
        highscores.add(new Highscore("Sepp",(int) (Math.random() * 100)));
        highscores.add(new Highscore("Fred",(int) (Math.random() * 100)));
        highscores.add(new Highscore("Paul",(int) (Math.random() * 100)));
    }

    public static boolean isInList(String name){
        return highscores.stream()
                .anyMatch(h -> (h.getName()
                        .equals(name)));
    }

    private static Highscore getHighscore(String name){
        return highscores.stream()
                .filter(h -> (h
                        .getName()
                        .equals(name)))
                .findFirst()
                .orElse(null);
    }

    public static int getRank(String name){
        return highscores.indexOf(getHighscore(name));
    }
}
