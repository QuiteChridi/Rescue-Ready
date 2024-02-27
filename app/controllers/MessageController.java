package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import controllers.interfaces.AbstractMessageFactory;
import controllers.interfaces.Message;
import models.MessageFactory;
import play.libs.Json;
import play.mvc.*;

import com.google.inject.Inject;

import java.util.List;

/**
 * This controller contains an action to handle HTTP requests
 * to the application's message page.
 * It extends the Controller class provided by Play.
 */
public class MessageController extends Controller {

    /**
     * The messageFactory is used to manage the messages.
     */
    private final AbstractMessageFactory messageFactory;

    /**
     * Constructor for the MessageController
     *
     * @param messages the MessageFactory
     * @Inject is used to inject the MessageFactory
     */
    @Inject
    public MessageController(MessageFactory messages) {
        this.messageFactory = messages;
    }

    /**
     * Constructs a new MessageController
     *
     * @param messages the MessageFactory
     */
    public MessageController(AbstractMessageFactory messages) {
        this.messageFactory = messages;
    }

    /**
     * Sends a message to the receiver. The message is taken from the request body. The sender is taken from the session.
     * The receiver is taken from the request parameter. The message is sent to the receiver.
     *
     * @param request    the request containing the message
     * @param receiverId the id of the receiver
     * @return a Result indicating the success of the operation
     */
    public Result sendMessage(Http.Request request, int receiverId) {
        JsonNode json = request.body().asJson();
        if (json == null) {
            return badRequest("Expecting JSON data");
        }
        try {
            int senderId = Integer.parseInt(request.session().get("userID").orElseThrow(() -> new IllegalStateException("User ID not found in session")));
            String messageText = json.get("content").asText();
            messageFactory.sendMessage(senderId, receiverId, messageText);
            return ok("Nachricht gesendet");
        } catch (Exception e) {
            return internalServerError("Fehler beim Senden der Nachricht: " + e.getMessage());
        }
    }

    /**
     * Gets the messages between the user and the conversation partner. The conversation partner is taken from the request parameter.
     *
     * @param request               the request containing the conversation partner
     * @param conversationPartnerId the id of the conversation partner
     * @return a Result containing the messages
     */
    public Result getMessages(Http.Request request, int conversationPartnerId) {
        int userId = Integer.parseInt(request.session().get("userID").orElseThrow(() -> new IllegalStateException("User ID not found in session")));
        List<controllers.interfaces.Message> messages = messageFactory.getMessages(userId, conversationPartnerId);
        return ok(Json.toJson(messages));
    }
}