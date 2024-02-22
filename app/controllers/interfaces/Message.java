package controllers.interfaces;

import models.MessageFactory;
import java.util.Date;
import java.util.List;

public abstract class Message {
    public abstract void sendMessage(int senderId, int receiverId, String message);
    public abstract List<MessageFactory.MessageImplementation> getMessages(int userId, int conversationPartnerId);
    public abstract int getId();
    public abstract int getSenderId();
    public abstract int getReceiverId();
    public abstract String getMessageText();
    public abstract Date getTimestamp();

}
