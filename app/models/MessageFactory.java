package models;

import javax.inject.Inject;
import javax.inject.Singleton;
import controllers.interfaces.AbstractMessageFactory;
import controllers.interfaces.Message;
import play.db.Database;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


@Singleton
public class MessageFactory implements AbstractMessageFactory {
    private Database db;

    @Inject
    MessageFactory(Database db) {
        this.db = db;
    }

    @Override
    public void sendMessage(int senderId, int receiverId, String message) {
        db.withConnection(connection -> {
            PreparedStatement stmt = connection.prepareStatement("INSERT INTO messages (sender_id, receiver_id, message) VALUES (?, ?, ?)");
            stmt.setInt(1, senderId);
            stmt.setInt(2, receiverId);
            stmt.setString(3, message);
            return stmt.executeUpdate();
        });
    }

    @Override
    public List<Message> getMessages(int userId, int conversationPartnerId) {
        return db.withConnection(connection -> {
            List<Message> messagesList = new ArrayList<>();
            PreparedStatement stmt = connection.prepareStatement(
                    "SELECT * FROM messages WHERE (sender_id = ? AND receiver_id = ?) OR (sender_id = ? AND receiver_id = ?)");
            stmt.setInt(1, userId);
            stmt.setInt(2, conversationPartnerId);
            stmt.setInt(3, conversationPartnerId);
            stmt.setInt(4, userId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                MessageImplementation message = new MessageImplementation(rs);
                messagesList.add(message);
            }
            return messagesList;
        });
    }


    public class MessageImplementation extends Message {
        private int id;
        private int senderId;
        private int receiverId;
        private String messageText;
        private Date timestamp;

        private MessageImplementation(int id, int senderId, int receiverId, String messageText, Date timestamp) {
            this.id = id;
            this.senderId = senderId;
            this.receiverId = receiverId;
            this.messageText = messageText;
            this.timestamp = timestamp;
        }

        private MessageImplementation(ResultSet rs) throws SQLException {
            this.id = rs.getInt("id");
            this.senderId = rs.getInt("sender_id");
            this.receiverId = rs.getInt("receiver_id");
            this.messageText = rs.getString("message");
            this.timestamp = rs.getDate("timestamp");
        }


        @Override
        public void sendMessage(int senderId, int receiverId, String message) {
            db.withConnection(connection -> {
                PreparedStatement stmt = connection.prepareStatement("INSERT INTO messages (sender_id, receiver_id, message) VALUES (?, ?, ?)");
                stmt.setInt(1, senderId);
                stmt.setInt(2, receiverId);
                stmt.setString(3, message);
                return stmt.executeUpdate();
            });
        }

        @Override
        public List<MessageImplementation> getMessages(int userId, int conversationPartnerId) {
            return db.withConnection(connection -> {
                List<MessageImplementation> messagesList = new ArrayList<>();
                PreparedStatement stmt = connection.prepareStatement(
                        "SELECT * FROM messages WHERE (sender_id = ? AND receiver_id = ?) OR (sender_id = ? AND receiver_id = ?)");
                stmt.setInt(1, userId);
                stmt.setInt(2, conversationPartnerId);
                stmt.setInt(3, conversationPartnerId);
                stmt.setInt(4, userId);
                ResultSet rs = stmt.executeQuery();

                while (rs.next()) {
                    MessageImplementation message = new MessageImplementation(rs);
                    messagesList.add(message);
                }
                return messagesList;
            });
        }

        @Override
        public int getId() {
            return id;
        }

        @Override
        public int getSenderId() {
            return senderId;
        }

        @Override
        public int getReceiverId() {
            return receiverId;
        }

        @Override
        public String getMessageText() {
            return messageText;
        }

        @Override
        public Date getTimestamp() {
            return timestamp;
        }
    }
}