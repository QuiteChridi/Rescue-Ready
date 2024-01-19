package models;

import java.util.*;

public class DummyFriendList {

    static List<UserFactory.User> userList;

    public static List<UserFactory.User> getUserList() {
        generateUserList();
        return userList;
    }

    public static void generateUserList() {
        userList = new ArrayList<>();

        //userList.add(new User(1, "Alice", "alice", "alice@alice.de"));
        //userList.add(new User(2, "Bob", "bob", "bob@bob.de"));
        //userList.add(new User(3, "Charlie", "charlie", "charlie@charlie.de"));
    }
}
