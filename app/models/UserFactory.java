package models;

import controllers.interfaces.*;
import play.db.Database;
import scala.Int;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Factory for creating and retrieving users from the database
 */
@Singleton
public class UserFactory implements AbstractUserFactory, FriendManager, AccountManager {
    private Database db;
    //private HighscoreFactory scores;

    /**
     * Constructor for UserFactory
     * @param db the database
     */
    @Inject
    UserFactory(Database db) {
        this.db = db;
    }

    /**
     * Authenticates a user with the given credentials
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
     * Creates a user in the database
     * @param name username
     * @param password password
     * @param email email
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
     * Retrieves a user from database with given ID
     * @param id id of user to find
     * @return User if found, else null
     */
    @Override
    public UserImplementation getUserById(int id) {
        return db.withConnection(conn -> {
            UserImplementation user = null;
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM user WHERE iduser = ?");
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
     * @param id String of id
     * @return User if found, else null
     */
    @Override
    public UserImplementation getUserById(String id) {
        return getUserById(Integer.parseInt(id));
    }

    /**
     * Retrieves all users from the database that match the search query
     * @param searchQuery the search query
     * @return List of all users that match the search query
     */
    @Override
    public List<User> searchUsersByName(String searchQuery) {
        return db.withConnection(conn -> {
            List<User> users = new ArrayList<>();
            String sql = "SELECT * FROM user WHERE name LIKE ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, "%" + searchQuery + "%");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                UserImplementation user = new UserImplementation(rs);
                users.add(user);
            }
            stmt.close();
            return users;
        });
    }

    /**
     * Adds a friend to the database
     * @param userId id of user
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

    @Override
    public List<User> getFriends(int userId) {
        return db.withConnection(conn -> {
            List<User> result = new ArrayList<>();
            String sql = "SELECT user.* FROM friends JOIN user ON friends.id_user_2 = user.iduser WHERE friends.id_user_1 = ?;";
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






    public class UserImplementation extends User {
        private static final String DEFAULT_PROFILE_PIC_PATH = "images/profilePics/profilePic.png";

        private final int id;
        private String username;
        private String password;
        private String mail;
        private int coins;
        private List<Integer> jokers = new ArrayList<>();
        private String profilePicPath;

        private UserImplementation(int id, String username, String password, String mail) {
            this.id = id;
            this.username = username;
            this.mail = mail;
            this.password = password;
            this.coins = 0;
            this.profilePicPath = DEFAULT_PROFILE_PIC_PATH;
        }

        private UserImplementation(ResultSet rs) throws SQLException {
            this.id = rs.getInt("iduser");
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

        @Override
        public int getCoins() {
            return coins;
        }

        @Override
        public void setCoins(int coins) {
            this.coins = coins;
        }

        @Override
        public String getProfilePicPath() {
            return profilePicPath;
        }

        @Override
        public void setProfilePicPath(String profilePicPath) {
            this.profilePicPath = profilePicPath;
        }

        @Override
        public int getId() {
            return id;
        }

        @Override
        public String getName() {
            return username;
        }

        @Override
        public void setName(String username) {
            this.username = username;
        }

        @Override
        public String getMail() {
            return mail;
        }

        @Override
        public void setMail(String mail) {
            this.mail = mail;
        }

        @Override
        public String getPassword() {
            return password;
        }

        @Override
        public void setPassword(String password) {
            this.password = password;
        }
    }
}
