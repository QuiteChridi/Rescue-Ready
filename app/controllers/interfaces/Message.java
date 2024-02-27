package controllers.interfaces;

import models.MessageFactory;

import java.util.Date;
import java.util.List;

/**
 * Interface for the Message class that is used to send and receive messages between users.
 */
public abstract class Message {
    /**
     * Sends a message from the sender to the receiver.
     *
     * @param senderId   The id of the sender.
     * @param receiverId The id of the receiver.
     * @param message    The message to be sent.
     */
    public abstract void sendMessage(int senderId, int receiverId, String message);

    /**
     * Gets all the messages between two users.
     *
     * @param userId                The id of the user.
     * @param conversationPartnerId The id of the conversation partner.
     * @return A list of messages between the two users.
     */
    public abstract List<MessageFactory.MessageImplementation> getMessages(int userId, int conversationPartnerId);

    /**
     * Gets the id of the sender.
     *
     * @return The id of the sender.
     */
    public abstract int getSenderId();

    /**
     * Gets the id of the receiver.
     *
     * @return The id of the receiver.
     */
    public abstract int getReceiverId();

    /**
     * Gets the text of the message.
     *
     * @return The text of the message.
     */
    public abstract String getMessageText();

    /**
     * Gets the timestamp of the message.
     *
     * @return The timestamp of the message.
     */
    public abstract Date getTimestamp();

}
