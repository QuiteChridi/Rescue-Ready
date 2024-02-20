package controllers.interfaces;

public interface AbstractJokerFactory {
    Joker getJokerById (int id);
    public void setJokersOfUser(int jokerId, int userId, int amount);
}
