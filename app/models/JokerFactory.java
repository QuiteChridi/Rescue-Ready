package models;


import controllers.interfaces.AbstractJokerFactory;
import controllers.interfaces.Joker;
import play.db.Database;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@Singleton
public class JokerFactory implements AbstractJokerFactory {
    private final Database db;

    @Inject
    JokerFactory(Database db) {
        this.db = db;
    }


    @Override
    public JokerImplementation getJokerById(int id) {
        return db.withConnection(conn -> {
            JokerImplementation joker = null;
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM joker WHERE idjoker = ?");
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                joker = new JokerImplementation(rs);
            }
            stmt.close();
            return joker;
        });
    }

    @Override
    public void setJokersOfUser(int jokerId, int userId, int amount) {
        db.withConnection(conn -> {
            PreparedStatement stmt = conn.prepareStatement("UPDATE joker_of_users SET amount = ? WHERE user_id = ? AND joker_id = ?");
            stmt.setInt(1, amount);
            stmt.setInt(2, userId);
            stmt.setInt(3, jokerId);

            stmt.executeUpdate();
            stmt.close();
        });
    }

    public class JokerImplementation extends Joker {

        private final int jokerId;

        private String jokerName;

        private String jokerDescription;

        private int jokerPrice;

        private JokerImplementation(ResultSet rs) throws SQLException {
            this.jokerId = rs.getInt("idJoker");
            this.jokerName = rs.getString("name");
            this.jokerDescription = rs.getString("description");
            this.jokerPrice = rs.getInt("price");
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
    }
}
