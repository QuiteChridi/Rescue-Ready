package controllers.interfaces;

public abstract class User {
    public abstract void save();

    public abstract int getCoins();

    public abstract void setCoins(int coins);

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
