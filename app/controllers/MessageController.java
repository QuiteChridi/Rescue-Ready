package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import controllers.interfaces.AbstractMessageFactory;
import controllers.interfaces.Message;
import models.MessageFactory;
import play.libs.Json;
import play.mvc.*;


import com.google.inject.Inject;
import java.util.List;

public class MessageController extends Controller {
    private final AbstractMessageFactory messageFactory;

    @Inject
    public MessageController(MessageFactory messages) {
        this.messageFactory = messages;
    }
    public MessageController(AbstractMessageFactory messages) {
        this.messageFactory = messages;
    }

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
    public Result getMessages(Http.Request request, int conversationPartnerId) {
        int userId = Integer.parseInt(request.session().get("userID").orElseThrow(() -> new IllegalStateException("User ID not found in session")));
        List<controllers.interfaces.Message> messages = messageFactory.getMessages(userId, conversationPartnerId);
        return ok(Json.toJson(messages));
    }
}