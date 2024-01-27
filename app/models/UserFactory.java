package models;

import play.db.Database;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Singleton
public class UserFactory {
    private Database db;
    private static final String DEFAULT_PROFILE_PIC_PATH = "images/profilePic.png";
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
    public User authenticate(String username, String password) {
        return db.withConnection(conn -> {
            User user = null;
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM user WHERE name = ? AND password = ?");
            stmt.setString(1, username);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                user = new User(rs);
            }
            stmt.close();
            return user;
        });
    }

    public User createUserInUsers (String name, String password, String email) {
        return db.withConnection(conn -> {
            User user = null;
            String sql = "INSERT INTO user (name, password, email) VALUES ( ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            stmt.setString(1, name);
            stmt.setString(2, password);
            stmt.setString(3, email);
            stmt.executeUpdate();
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                int id = rs.getInt(1);
                user = new User(id, name, password, email);
            }
            stmt.close();
            return user;
        });
    }


    /**
     * Retrieves a user from database with given ID
     *
     * @param id id of user to find
     * @return User if found, else null
     */
    public User getUserById(int id) {
        return db.withConnection(conn -> {
            User user = null;
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM user WHERE iduser = ?");
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                user = new User(rs);
            }
            stmt.close();
            return user;
        });
    }

    /**
     * Polymorphism method for getUserById(int)
     *
     * @param id String of id
     * @return User if found, else null
     */
    public User getUserById(String id) {
        return getUserById(Integer.parseInt(id));
    }

    public List<User> getAllUsers() {
        return db.withConnection(conn -> {
            List<User> users = new ArrayList<>();
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM user");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                User user = new User(rs);
                users.add(user);
            }
            stmt.close();
            return users;
        });
    }



    public class User {
        private int id;
        private String username;
        private String password;
        private String mail;
        private int coins;
        private int fiftyFiftyJoker;
        private int doublePointsJoker;
        private int pauseJoker;
        private String profilePicPath;


        private User(int id, String username, String password, String mail) {
            this.id = id;
            this.username = username;
            this.mail = mail;
            this.password = password;
            this.coins = 0;
            this.fiftyFiftyJoker = 0;
            this.doublePointsJoker = 0;
            this.pauseJoker = 0;
            this.profilePicPath = DEFAULT_PROFILE_PIC_PATH;
        }

        private User(ResultSet rs) throws SQLException {
            this.id = rs.getInt("iduser");
            this.username = rs.getString("name");
            this.mail = rs.getString("email");
            this.password = rs.getString("password");
            this.coins = rs.getInt("coins");
            this.fiftyFiftyJoker = rs.getInt("fifty_fifty_joker");
            this.doublePointsJoker = rs.getInt("double_points_joker");
            this.pauseJoker = rs.getInt("pause_joker");
            this.profilePicPath = rs.getString("profile_pic_path");
        }

        /**
         * Updates the user if it already exists and creates it otherwise. Assumes an
         * autoincrement id column.
         */
        public void save() {
            db.withConnection(conn -> {
                String sql = "UPDATE user SET name = ?, email = ?, password = ?, coins = ?, fifty_fifty_joker = ?, double_points_joker = ?, pause_joker = ?, profile_pic_path = ? WHERE idUser = ?";
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setString(1, this.username);
                stmt.setString(2, this.mail);
                stmt.setString(3, this.password);
                stmt.setInt(4, this.coins);
                stmt.setInt(5, this.fiftyFiftyJoker);
                stmt.setInt(6, this.doublePointsJoker);
                stmt.setInt(7, this.pauseJoker);
                stmt.setString(8, this.profilePicPath);
                stmt.setInt(9, this.id);

                stmt.executeUpdate();
                stmt.close();
            });
        }

        /**
         * Delete the user from the database
         */
        public void delete() {
            db.withConnection(conn -> {
                String sql = "DELETE FROM user WHERE UserId = ?";
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setInt(1, this.id);
                stmt.executeUpdate();
                stmt.close();
            });
        }

        public List<User> getFriends() {
            return db.withConnection(conn -> {
                List<User> result = new ArrayList<>();
                String sql = "SELECT user.* FROM friends JOIN user ON friends.id_user_2 = user.iduser WHERE friends.id_user_1 = ?;";
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setInt(1, this.id);
                ResultSet rs = stmt.executeQuery();
                while (rs.next()) {
                    User user = new User(rs);
                    result.add(user);
                }
                stmt.close();
                return result;
            });
        }

        public int getCoins() {
            return coins;
        }

        public void setCoins(int coins) {
            this.coins = coins;
            save();
        }

        public int getFiftyFiftyJoker() {
            return fiftyFiftyJoker;
        }

        public void setFiftyFiftyJoker(int fiftyFiftyJoker) {
            this.fiftyFiftyJoker = fiftyFiftyJoker;
            save();
        }

        public int getDoublePointsJoker() {
            return doublePointsJoker;
        }

        public void setDoublePointsJoker(int doublePointsJoker) {
            this.doublePointsJoker = doublePointsJoker;
            save();
        }

        public int getPauseJoker() {
            return pauseJoker;
        }

        public void setPauseJoker(int pauseJoker) {
            this.pauseJoker = pauseJoker;
            save();
        }

        public String getProfilePicPath() {
            return profilePicPath;
        }

        public void setProfilePicPath(String profilePicPath) {
            this.profilePicPath = profilePicPath;
        }

        public int getId() {
            return id;
        }


        public String getName() {
            return username;
        }

        public void setName(String username) {
            this.username = username;
            this.save();
        }

        public String getMail() {
            return mail;
        }

        public void setMail(String mail) {
            this.mail = mail;
            save();
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
            this.save();
        }

    }

}
