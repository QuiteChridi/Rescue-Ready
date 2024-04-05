package models;

import controllers.interfaces.*;
import play.db.Database;
import play.db.Databases;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Factory for creating and retrieving users from the database.
 * Implements the AbstractUserFactory, FriendManager and AccountManager interfaces.
 * It is a singleton class, meaning that only one instance of it will be created.
 * It is responsible for creating User objects and getting users from the database.
 */
@Singleton
public class UserFactory implements AbstractUserFactory, FriendManager, AccountManager {
    private Database db;

    /**
     * Constructor for UserFactory
     *
     * @param db the database
     */
    @Inject
    UserFactory(Database db) {
        this.db = db;
    }
    /**
     * Authenticates a user with the given credentials
     *
     * @param username username from user input
     * @param password password from user input
     * @return Found user or null if user not found
     */
    @Override
    public UserImplementation authenticate(String username, String password) {
        return db.withConnection(conn -> {
            UserImplementation user = null;
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM user WHERE name = ? AND password = ?");
            stmt.setString(1, username);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                user = new UserImplementation(rs);
            }
            stmt.close();
            return user;
        });
    }

    /**
     * Creates a user in the database with the given credentials
     *
     * @param name     username
     * @param password password
     * @param email    email
     * @return User if created, else null
     */
    @Override
    public UserImplementation createUser(String name, String password, String email) {
        return db.withConnection(conn -> {
            UserImplementation user = null;
            String sql = "INSERT INTO user (name, password, email) VALUES ( ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            stmt.setString(1, name);
            stmt.setString(2, password);
            stmt.setString(3, email);
            stmt.executeUpdate();
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                int id = rs.getInt(1);
                user = new UserImplementation(id, name, password, email);
            }
            stmt.close();
            return user;
        });
    }

    /**
     * Checks if a user is a friend of another user
     *
     * @param userId      id of user
     * @param otherUserId id of other user
     * @return true if user is friend of other user, else false
     */
    @Override
    public boolean isFriend(int userId, int otherUserId) {
        return db.withConnection(conn -> {
            String sql = "SELECT * FROM friends WHERE friends.id_user_1 = ? AND friends.id_user_2 = ? ";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, userId);
            stmt.setInt(2, otherUserId);
            ResultSet rs = stmt.executeQuery();
            boolean isFriend = rs.next();
            stmt.close();
            return isFriend;
        });
    }

    /**
     * Retrieves a user from database with given ID
     *
     * @param id id of user to find
     * @return User if found, else null
     */
    @Override
    public UserImplementation getUserById(int id) {
        return db.withConnection(conn -> {
            UserImplementation user = null;
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM user WHERE idUser = ?");
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                user = new UserImplementation(rs);
            }
            stmt.close();
            return user;
        });
    }

    /**
     * Polymorphism method for getUserById(int)
     *
     * @param id String of id of user to find
     * @return User if found, else null
     */
    @Override
    public UserImplementation getUserById(String id) {
        return getUserById(Integer.parseInt(id));
    }

    /**
     * Retrieves all users from the database that match the search query
     *
     * @param searchQuery the search query to match users with
     * @return List of all users that match the search query
     */
    @Override
    public List<User> searchUsersByName(String searchQuery) {
        return db.withConnection(conn -> {
            List<User> users = new ArrayList<>();
            String sql = "SELECT * FROM user WHERE name LIKE ?";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, "%" + searchQuery + "%");
                ResultSet rs = stmt.executeQuery();
                while (rs.next()) {
                    UserImplementation user = new UserImplementation(rs);
                    users.add(user);
                }
                stmt.close();
                return users;
            } catch (SQLException e) {
                e.printStackTrace();
                return null;
            }
        });
    }

    /**
     * Adds a friend to the database
     *
     * @param userId   id of user
     * @param friendId id of friend
     * @return true if friend was added successfully, else false
     */
    @Override
    public boolean addFriend(int userId, int friendId) {
        return db.withConnection(conn -> {
            String sql = "INSERT INTO friends (id_user_1, id_user_2) VALUES (?, ?)";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, userId);
                stmt.setInt(2, friendId);
                stmt.executeUpdate();
                return true;
            } catch (SQLException e) {
                e.printStackTrace();
                return false;
            }
        });
    }

    /**
     * Removes a friend from the database
     *
     * @param userId   id of user
     * @param friendId id of friend
     * @return true if friend was removed successfully, else false
     */
    @Override
    public boolean removeFriend(int userId, int friendId) {
        return db.withConnection(conn -> {
            String sql = "DELETE FROM friends WHERE id_user_1 = ? AND id_user_2 = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, userId);
                stmt.setInt(2, friendId);
                stmt.executeUpdate();
                return true;
            } catch (SQLException e) {
                e.printStackTrace();
                return false;
            }
        });
    }

    /**
     * Delete the user from the database
     *
     * @param userId id of user to delete
     */
    @Override
    public void deleteUser(int userId) {
        db.withConnection(conn -> {
            String sql = "DELETE FROM user WHERE UserId = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, userId);
            stmt.executeUpdate();
            stmt.close();
        });
    }

    /**
     * Returns a list of all users in the database
     *
     * @return List of all users in the database
     */
    @Override
    public List<User> getAllUsers() {
        return db.withConnection(conn -> {
            List<User> result = new ArrayList<>();
            String sql = "SELECT * FROM user";
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                UserImplementation user = new UserImplementation(rs);
                result.add(user);
            }
            stmt.close();
            return result;
        });
    }

    /**
     * Returns a list of all friends of a user
     *
     * @param userId id of user
     * @return List of all friends of the user
     */
    @Override
    public List<User> getFriends(int userId) {
        return db.withConnection(conn -> {
            List<User> result = new ArrayList<>();
            String sql = "SELECT user.* FROM friends JOIN user ON friends.id_user_2 = user.idUser WHERE friends.id_user_1 = ?;";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                UserImplementation user = new UserImplementation(rs);
                result.add(user);
            }
            stmt.close();
            return result;
        });
    }

    /**
     * Returns a list of all users that are not friends of a user
     *
     * @param userId id of user
     * @return List of all users that are not friends of the user
     */
    @Override
    public List<User> getNotFriends(int userId) {
        List<User> allUsers = getAllUsers();
        List<User> friends = getFriends(userId);

        List<User> notFriends = allUsers.stream()
                .filter(user -> !friends.contains(user))
                .collect(Collectors.toList());

        return notFriends;
    }

    /**
     * Implementation of the User interface for creating User objects
     * It is a static class, meaning that it can only be used by the UserFactory class.
     */
    public class UserImplementation extends User {
        private static final String DEFAULT_PROFILE_PIC_PATH = "images/profilePics/profilePic.png";
        private final int id;
        private String username;
        private String password;
        private String mail;
        private int coins;
        private List<Integer> jokers = new ArrayList<>();
        private String profilePicPath;

        /**
         * Constructor for UserImplementation
         *
         * @param id       id of user
         * @param username username of user
         * @param password password of user
         * @param mail     email of user
         */
        private UserImplementation(int id, String username, String password, String mail) {
            this.id = id;
            this.username = username;
            this.mail = mail;
            this.password = password;
            this.coins = 0;
            this.profilePicPath = DEFAULT_PROFILE_PIC_PATH;
        }

        /**
         * Constructor for UserImplementation
         *
         * @param rs ResultSet from database
         * @throws SQLException
         */
        private UserImplementation(ResultSet rs) throws SQLException {
            this.id = rs.getInt("idUser");
            this.username = rs.getString("name");
            this.mail = rs.getString("email");
            this.password = rs.getString("password");
            this.coins = rs.getInt("coins");
            this.profilePicPath = rs.getString("profile_pic_path");
        }

        /**
         * Updates the user if it already exists and creates it otherwise. Assumes an
         * autoincrement id column.
         */
        @Override
        public void save() {
            db.withConnection(conn -> {
                String sql = "UPDATE user SET name = ?, email = ?, password = ?, coins = ?, profile_pic_path = ? WHERE idUser = ?";
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setString(1, this.username);
                stmt.setString(2, this.mail);
                stmt.setString(3, this.password);
                stmt.setInt(4, this.coins);
                stmt.setString(5, profilePicPath);
                stmt.setInt(6, this.id);

                stmt.executeUpdate();
                stmt.close();
            });
        }

        /**
         * Deletes the user from the database
         */
        @Override
        public int getCoins() {
            return coins;
        }

        /**
         * Sets the amount of coins the user has
         *
         * @param coins amount of coins
         */
        @Override
        public void setCoins(int coins) {
            this.coins = coins;
        }

        /**
         * Returns the profile picture path of the user
         *
         * @return profile picture path
         */
        @Override
        public String getProfilePicPath() {
            return profilePicPath;
        }

        /**
         * Sets the profile picture path of the user
         *
         * @param profilePicPath profile picture path
         */
        @Override
        public void setProfilePicPath(String profilePicPath) {
            this.profilePicPath = profilePicPath;
        }

        /**
         * Returns the id of the user
         *
         * @return id of user
         */
        @Override
        public int getId() {
            return id;
        }

        /**
         * Returns the username of the user
         *
         * @return username of user
         */
        @Override
        public String getName() {
            return username;
        }

        /**
         * Sets the username of the user
         *
         * @param username username of user
         */
        @Override
        public void setName(String username) {
            this.username = username;
        }

        /**
         * Returns the email of the user
         *
         * @return email of user
         */
        @Override
        public String getMail() {
            return mail;
        }

        /**
         * Sets the email of the user
         *
         * @param mail email of user
         */
        @Override
        public void setMail(String mail) {
            this.mail = mail;
        }

        /**
         * Returns the password of the user
         *
         * @return password of user
         */
        @Override
        public String getPassword() {
            return password;
        }

        /**
         * Sets the password of the user
         *
         * @param password password of user
         */
        @Override
        public void setPassword(String password) {
            this.password = password;
        }

        /**
         * Returns the list of jokers of the user
         *
         * @return list of jokers
         */
        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }

            if (!(obj instanceof User)) {
                return false;
            }

            User userToCompare = (User) obj;
            return this.id == userToCompare.getId();
        }
    }
}
