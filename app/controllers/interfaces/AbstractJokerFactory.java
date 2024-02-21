package controllers.interfaces;

import java.util.List;

public interface AbstractJokerFactory extends JokerGetter {
    Joker getJokerById (int id, int userId);
    int getJokerAmountOfUser(int jokerId, int userId);
    void setJokerAmountOfUser(int jokerId, int userId, int amount);
    List<Joker> getAllJokers(int userId);
}
