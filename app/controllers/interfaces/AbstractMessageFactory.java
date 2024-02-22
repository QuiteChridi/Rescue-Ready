package controllers.interfaces;

import java.util.List;

public interface AbstractMessageFactory {
    void sendMessage(int senderId, int receiverId, String message);
    List<Message> getMessages(int userId, int conversationPartnerId);}
