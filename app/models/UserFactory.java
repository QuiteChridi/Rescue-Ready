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

    public User signUp(String name, String password, String email) {
        return db.withConnection(conn -> {
            User user = null;
            String sql = "INSERT INTO User (name, password, email) VALUES ( ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            stmt.setString(1, name);
            stmt.setString(2, password);
            stmt.setString(3, email);
            stmt.executeUpdate();
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                int id = rs.getInt(0);
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

        private User(int id, String username, String password, String mail) {
            this.id = id;
            this.username = username;
            this.mail = mail;
            this.password = password;
        }

        private User(ResultSet rs) throws SQLException {
            this.id = rs.getInt("iduser");
            this.username = rs.getString("name");
            this.mail = rs.getString("email");
            this.password = rs.getString("password");
        }

        /**
         * Updates the user if it already exists and creates it otherwise. Assumes an
         * autoincrement id column.
         */
        public void save() {
            db.withConnection(conn -> {
                String sql = "UPDATE User SET name = ?, email = ?, password = ? WHERE idUser = ?";
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setString(1, this.username);
                stmt.setString(2, this.mail);
                stmt.setString(3, this.password);
                stmt.setInt(4, this.id);
                stmt.executeUpdate();
                stmt.close();
            });
        }

        /**
         * Delete the user from the database
         */
        public void delete() {
            db.withConnection(conn -> {
                String sql = "DELETE FROM User WHERE UserId = ?";
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setInt(1, this.id);
                stmt.executeUpdate();
                stmt.close();
            });
        }

        public List<User> getFriends() {
            return db.withConnection(conn -> {
                List<User> result = new ArrayList<>();
                String sql = "SELECT * FROM friends, user WHERE id_user1 = ? AND friends.id_user2 = idUser";
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
