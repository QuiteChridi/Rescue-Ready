package models;


import controllers.interfaces.AbstractJokerFactory;
import controllers.interfaces.Joker;
import play.db.Database;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * JokerFactory is a class that implements the AbstractJokerFactory interface.
 * It is responsible for creating Joker objects and getting jokers from the database.
 * It is a singleton class, meaning that only one instance of it will be created.
 */
@Singleton
public class JokerFactory implements AbstractJokerFactory {
    private final Database db;

    /**
     * Constructor for JokerFactory.
     *
     * @param db The database to be used.
     */
    @Inject
    JokerFactory(Database db) {
        this.db = db;
    }

    /**
     * Method to get all jokers.
     *
     * @param userId The id of the user.
     * @return A list of jokers.
     */
    @Override
    public List<Joker> getAllJokers(int userId) {
        return db.withConnection(conn -> {
            List<Joker> result = new ArrayList<>();
            String sql = "SELECT * FROM joker";
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                JokerImplementation joker = new JokerImplementation(rs, userId);
                result.add(joker);
            }
            stmt.close();
            return result;
        });
    }

    /**
     * Method to get a joker by its id.
     *
     * @param id     The id of the joker.
     * @param userId The id of the user.
     * @return The joker.
     */
    @Override
    public JokerImplementation getJokerById(int id, int userId) {
        return db.withConnection(conn -> {
            JokerImplementation joker = null;
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM joker WHERE idjoker = ?");
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                joker = new JokerImplementation(rs, userId);
            }
            stmt.close();
            return joker;
        });
    }

    /**
     * Method to set the amount of a joker for a user.
     *
     * @param jokerId The id of the joker.
     * @param userId  The id of the user.
     * @param amount  The amount of the joker.
     */
    @Override
    public void setJokerAmountOfUser(int jokerId, int userId, int amount) {
        db.withConnection(conn -> {
            PreparedStatement stmt = conn.prepareStatement("REPLACE INTO joker_of_users VALUES (?,?,?)");
            stmt.setInt(1, userId);
            stmt.setInt(2, jokerId);
            stmt.setInt(3, amount);

            stmt.executeUpdate();
            stmt.close();
        });
    }

    /**
     * Method to get the amount of a joker for a user.
     *
     * @param jokerId The id of the joker.
     * @param userId  The id of the user.
     * @return The amount of the joker.
     */
    @Override
    public int getJokerAmountOfUser(int jokerId, int userId) {
        return db.withConnection(conn -> {
            int amount = 0;
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM joker_of_users WHERE user_id = ? AND joker_id = ?");
            stmt.setInt(1, userId);
            stmt.setInt(2, jokerId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                amount = rs.getInt("amount");
            }
            stmt.close();
            return amount;
        });
    }

    /**
     * The joker implementation class that extends the Joker class. It is used to create Joker objects.
     * It is a static class, meaning that it can only be used by the JokerFactory class.
     */
    public class JokerImplementation extends Joker {

        private final int jokerId;

        private String jokerName;

        private String jokerDescription;

        private int jokerPrice;

        private int jokerAmount;

        private String jokerPicPath;

        /**
         * Constructor for JokerImplementation.
         *
         * @param rs     The result set of the joker.
         * @param userId The id of the user.
         * @throws SQLException If an error occurs while accessing the database.
         */
        private JokerImplementation(ResultSet rs, int userId) throws SQLException {
            this.jokerId = rs.getInt("idJoker");
            this.jokerName = rs.getString("name");
            this.jokerDescription = rs.getString("description");
            this.jokerPrice = rs.getInt("price");
            this.jokerPicPath = rs.getString("joker_pic_path");
            this.jokerAmount = getJokerAmountOfUser(jokerId, userId);
        }

        /**
         * Method to get the id of the joker.
         *
         * @return The id of the joker.
         */
        @Override
        public int getId() {
            return jokerId;
        }

        /**
         * Method to get the name of the joker.
         *
         * @return The name of the joker.
         */
        @Override
        public String getName() {
            return jokerName;
        }

        /**
         * Method to get the description of the joker.
         *
         * @return The description of the joker.
         */
        @Override
        public String getDescription() {
            return jokerDescription;
        }

        /**
         * Method to get the price of the joker.
         *
         * @return The price of the joker.
         */
        @Override
        public int getPrice() {
            return jokerPrice;
        }

        /**
         * Method to get the path to the picture of the joker.
         *
         * @return The path to the picture of the joker.
         */
        @Override
        public String getJokerPicPath() {
            return jokerPicPath;
        }

        /**
         * Method to get the amount of the joker.
         *
         * @return The amount of the joker.
         */
        @Override
        public int getAmount() {
            return jokerAmount;
        }
    }
}
