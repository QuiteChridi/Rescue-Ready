package models;

import java.util.*;

public class DummyFriendList {

    static List<User> userList;

    public static List<User> getUserList() {
        generateUserList();
        return userList;
    }

    public static void generateUserList() {
        userList = new ArrayList<>();
        userList.add(new User(1, "Alice"));
        userList.add(new User(2, "Bob"));
        userList.add(new User(3, "Charlie"));
        userList.add(new User(4, "David"));
    }
}
