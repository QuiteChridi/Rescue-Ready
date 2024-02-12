package controllers.interfaces;

import models.UserFactory;

import java.util.List;

public abstract class User {
    public abstract void save();

    public abstract List<UserFactory.UserImplementation> getAllUsers();

    public abstract boolean addFriend(int userId, int friendId);

    public abstract boolean removeFriend(int userId, int friendId);

    public abstract void delete();

    public abstract List<UserFactory.UserImplementation> getFriends();

    public abstract int getCoins();

    public abstract void setCoins(int coins);

    public abstract int getFiftyFiftyJoker();

    public abstract void setFiftyFiftyJoker(int fiftyFiftyJoker);

    public abstract int getDoublePointsJoker();

    public abstract void setDoublePointsJoker(int doublePointsJoker);

    public abstract int getPauseJoker();

    public abstract void setPauseJoker(int pauseJoker);

    public abstract String getProfilePicPath();

    public abstract void setProfilePicPath(String profilePicPath);

    public abstract int getId();

    public abstract String getName();

    public abstract void setName(String username);

    public abstract String getMail();

    public abstract void setMail(String mail);

    public abstract String getPassword();

    public abstract void setPassword(String password);
}
