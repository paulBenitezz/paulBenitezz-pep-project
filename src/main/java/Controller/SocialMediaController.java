package Controller;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import Model.Account;
import Model.Message;
import Service.AccountService;
import Service.MessageService;
import io.javalin.Javalin;
import io.javalin.http.Context;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller. The endpoints you will need can be
 * found in readme.md as well as the test cases. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
public class SocialMediaController {
    private AccountService accountService;
    private MessageService messageService;

    public SocialMediaController() {
        this.accountService = new AccountService();
        this.messageService = new MessageService();
    }

    /**
     * In order for the test cases to work, you will need to write the endpoints in the startAPI() method, as the test
     * suite must receive a Javalin object from this method.
     * @return a Javalin app object which defines the behavior of the Javalin controller.
     */
    public Javalin startAPI() {
        Javalin app = Javalin.create();
        app.post("/register", this::postAccountHandler);
        app.post("/login", this::postLoginHandler);
        app.post("/messages", this::postMessageHandler);
        app.get("/messages", this::getMessagesHandler);
        app.get("/messages/{message_id}", this::getMessagesByIdHandler);
        app.delete("messages/{message_id}", this::deleteMessageByIdHandler);
        app.patch("messages/{message_id}", this::updateMessageByIdHandler);
        app.get("accounts/{account_id}/messages", this::getMessagesByAccountHandler);
        return app;
    }

    /**
     * This is an example handler for an example endpoint.
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     */
    private void postAccountHandler(Context ctx) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Account account = mapper.readValue(ctx.body(), Account.class);
        Account newAccount = accountService.addAccount(account);
        if (newAccount != null) {
            ctx.json(mapper.writeValueAsString(newAccount));
        } else {
            ctx.status(400);
        }
    }

    private void postLoginHandler(Context ctx) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Account account = mapper.readValue(ctx.body(), Account.class);
        Account foundAccount = accountService.loginAccount(account);
        if (foundAccount != null) {
            ctx.json(mapper.writeValueAsString(foundAccount));
        } else {
            ctx.status(401);
        }
    }

    private void postMessageHandler(Context ctx) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Message message = mapper.readValue(ctx.body(), Message.class);
        Message newMessage = messageService.createMessage(message);
        if (newMessage != null) {
            ctx.json(mapper.writeValueAsString(newMessage));
        } else {
            ctx.status(400);
        }
    }

    private void getMessagesHandler(Context ctx) throws JsonProcessingException {
        ctx.json(messageService.getAllMessages());
    }

    private void getMessagesByIdHandler(Context ctx) throws JsonProcessingException {
        int messageId = Integer.parseInt(ctx.pathParam("message_id")); 
        Message message = messageService.getMessageById(messageId); 

        if (message != null) {
            ctx.json(message);
        } 

    }

    private void deleteMessageByIdHandler(Context ctx) throws JsonProcessingException {
        int messageId = Integer.parseInt(ctx.pathParam("message_id")); 
        
        Message existingMessage = messageService.getMessageById(messageId);
        if (existingMessage != null) {
            Message deletedMessage = messageService.deleteMessageById(messageId);
            ctx.json(deletedMessage); // Return the deleted message
        } 
    }

    private void updateMessageByIdHandler(Context ctx) throws JsonProcessingException {
        int message_id = Integer.parseInt(ctx.pathParam("message_id"));
        ObjectMapper mapper = new ObjectMapper();
        Message message = mapper.readValue(ctx.body(), Message.class);
        
        String new_messageText = message.getMessage_text();

        Message existingMessage = messageService.getMessageById(message_id);
    
        Message updatedMessage = messageService.updateMessageById(message_id, existingMessage, new_messageText);
    
        if (updatedMessage != null) {
            ctx.json(updatedMessage);
        } else {
            ctx.status(400); 
        }
    }

    private void getMessagesByAccountHandler(Context ctx) throws JsonProcessingException {
        List<Message> messages = new ArrayList<>();
        try {
            int account_id = Integer.parseInt(ctx.pathParam("account_id"));
            Account account = accountService.getAccountById(account_id);
            
            if (account == null) {
                ctx.json(messages);
                
            }
    
           messages = messageService.getMessagesByAccount(account);
    
            ctx.json(messages);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}