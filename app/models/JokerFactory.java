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

@Singleton
public class JokerFactory implements AbstractJokerFactory {
    private final Database db;

    @Inject
    JokerFactory(Database db) {
        this.db = db;
    }


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

    @Override
    public List<Integer> getAllJokerAmountsOfUser(int userId) {
        return db.withConnection(conn -> {
            List<Integer> amounts = new ArrayList<>();
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM joker_of_users WHERE user_id = ?");
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            while(amounts.size() < 3){
                while (rs.next()) {
                    amounts.add(rs.getInt("amount"));
                }
                amounts.add(0);
            }
            stmt.close();
            return amounts;
        });
    }

    public class JokerImplementation extends Joker {

        private final int jokerId;

        private String jokerName;

        private String jokerDescription;

        private int jokerPrice;

        private int jokerAmount;

        private JokerImplementation(ResultSet rs, int userId) throws SQLException {
            this.jokerId = rs.getInt("idJoker");
            this.jokerName = rs.getString("name");
            this.jokerDescription = rs.getString("description");
            this.jokerPrice = rs.getInt("price");
            this.jokerAmount = getJokerAmountOfUser(jokerId, userId);
        }

        @Override
        public int getId() {
            return jokerId;
        }
        @Override
        public String getName() {
            return jokerName;
        }
        @Override
        public String getDescription() {
            return jokerDescription;
        }
        @Override
        public int getPrice() {
            return jokerPrice;
        }

        public int getAmount() {
            return jokerAmount;
        }
    }
}
