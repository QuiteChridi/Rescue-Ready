package controllers.interfaces;

public interface AbstractJokerFactory extends JokerGetter {
    Joker getJokerById (int id);
    int getJokerAmountOfUser(int jokerId, int userId);
    void setJokerAmountOfUser(int jokerId, int userId, int amount);
}
