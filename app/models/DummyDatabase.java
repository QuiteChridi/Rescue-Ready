package models;

import java.util.HashMap;
import java.util.Map;

public class DummyDatabase {
    static Map<String, Integer> highscores;

    public static Map<String, Integer> getHighscores(){
        if(highscores == null){
            generateRandomHighscores();
        }
        return highscores;
    }

    public static void updateHighscore(String name, int highscore){
        highscores.put(name, highscore);
    }
    public static void generateRandomHighscores(){
        highscores = new HashMap<>();
        highscores.put("Willi", (int) (Math.random() * 100));
        highscores.put("Hubert", (int) (Math.random() * 100));
        highscores.put("Sepp", (int) (Math.random() * 100));
        highscores.put("Fred", (int) (Math.random() * 100));
        highscores.put("Paul", (int) (Math.random() * 100));
    }
}
