package controllers.interfaces;

public abstract class Highscore {
    public abstract int getQuizId();

    public abstract int getUserId();

    public abstract int getScore();

    public abstract String getProfilePicPath();

    public abstract String getUserName();

    public abstract String getQuizName();

    public abstract int compareTo(Highscore that);
}
