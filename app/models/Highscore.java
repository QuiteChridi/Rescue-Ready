package models;

import java.util.Objects;

public class Highscore {
    private final String name;
    private int score;

    public Highscore(String name, int points){
        this.name = name;
        this.score = points;
    }

    public String getName() {
        return name;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Highscore highscore = (Highscore) o;
        return Objects.equals(name, highscore.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
