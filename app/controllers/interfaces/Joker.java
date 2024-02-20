package controllers.interfaces;

public abstract class Joker {

    public abstract int getId();

    public abstract String getName();

    public abstract void setName(String jokerName);

    public abstract String getDescription();

    public abstract void setDescription(String jokerDescription);

    public abstract int getPrice();

    public abstract void setPrice(int jokerPrice);
}
