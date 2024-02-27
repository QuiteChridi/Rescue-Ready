package controllers.interfaces;

import java.util.List;

/**
 * Interface for the MessageFactory class that is used to send and receive messages.
 */
public interface AbstractMessageFactory {
    /**
     * Sends a message from one user to another.
     *
     * @param senderId   The id of the user sending the message.
     * @param receiverId The id of the user receiving the message.
     * @param message    The message to be sent.
     */
    void sendMessage(int senderId, int receiverId, String message);

    /**
     * Gets all the messages between two users.
     *
     * @param userId                The id of the user.
     * @param conversationPartnerId The id of the user the user is having a conversation with.
     * @return A list of all the messages between the two users.
     */
    List<Message> getMessages(int userId, int conversationPartnerId);
}
