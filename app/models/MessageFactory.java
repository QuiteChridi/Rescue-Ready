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

/**
 * MessageFactory is a class that implements the AbstractMessageFactory interface.
 * It is responsible for creating Message objects and getting messages from the database.
 * It is a singleton class, meaning that only one instance of it will be created.
 */
@Singleton
public class MessageFactory implements AbstractMessageFactory {
    private Database db;

    /**
     * Constructor for MessageFactory.
     *
     * @param db The database to be used.
     */
    @Inject
    MessageFactory(Database db) {
        this.db = db;
    }

    /**
     * Method to send a message. The message will be sent from the sender to the receiver.
     * The message will be stored in the database.
     *
     * @param senderId   The id of the sender.
     * @param receiverId The id of the receiver.
     * @param message    The message to be sent.
     */
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

    /**
     * Method to get the messages between two users. The messages will be retrieved from the database.
     * The messages will be returned as a list of Message objects.
     *
     * @param userId                The id of the user.
     * @param conversationPartnerId The id of the conversation partner.
     * @return A list of messages.
     */
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

    /**
     * A static class that implements the Message interface.
     * It is used to create Message objects.
     */
    public class MessageImplementation extends Message {
        private int id;
        private int senderId;
        private int receiverId;
        private String messageText;
        private Date timestamp;

        /**
         * Constructor for MessageImplementation. This constructor is private and can only be used by the MessageFactory class.
         *
         * @param id          The id of the message.
         * @param senderId    The id of the sender.
         * @param receiverId  The id of the receiver.
         * @param messageText The message text.
         * @param timestamp   The timestamp of the message.
         */
        private MessageImplementation(int id, int senderId, int receiverId, String messageText, Date timestamp) {
            this.id = id;
            this.senderId = senderId;
            this.receiverId = receiverId;
            this.messageText = messageText;
            this.timestamp = timestamp;
        }

        /**
         * Constructor for MessageImplementation.
         *
         * @param rs The result set from the database.
         * @throws SQLException If an SQL exception occurs.
         */
        private MessageImplementation(ResultSet rs) throws SQLException {
            this.id = rs.getInt("id");
            this.senderId = rs.getInt("sender_id");
            this.receiverId = rs.getInt("receiver_id");
            this.messageText = rs.getString("message");
            this.timestamp = rs.getDate("timestamp");
        }

        /**
         * Method to send a message. The message will be sent from the sender to the receiver.
         * The message will be stored in the database.
         *
         * @param senderId   The id of the sender.
         * @param receiverId The id of the receiver.
         * @param message    The message to be sent.
         */
        @Override
        public void sendMessage(int senderId, int receiverId, String message) {
        }

        /**
         * Method to get the messages between two users. The messages will be retrieved from the database.
         * The messages will be returned as a list of Message objects.
         *
         * @param userId                The id of the user.
         * @param conversationPartnerId The id of the conversation partner.
         * @return A list of messages.
         */
        @Override
        public List<MessageImplementation> getMessages(int userId, int conversationPartnerId) {
            return null;
        }

        /**
         * Method to get the id of the message.
         *
         * @return The id of the message.
         */
        @Override
        public int getId() {
            return id;
        }

        /**
         * Method to get the id of the sender.
         *
         * @return The id of the sender.
         */
        @Override
        public int getSenderId() {
            return senderId;
        }

        /**
         * Method to get the id of the receiver.
         *
         * @return The id of the receiver.
         */
        @Override
        public int getReceiverId() {
            return receiverId;
        }

        /**
         * Method to get the message text.
         *
         * @return The message text.
         */
        @Override
        public String getMessageText() {
            return messageText;
        }

        /**
         * Method to get the timestamp of the message.
         *
         * @return The timestamp of the message.
         */
        @Override
        public Date getTimestamp() {
            return timestamp;
        }
    }
}