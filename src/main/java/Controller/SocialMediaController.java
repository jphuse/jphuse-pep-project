package Controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import Model.Account;
import Model.Message;
import Service.AccountService;
import Service.MessageService;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.javalin.Javalin;
import io.javalin.http.Context;

public class SocialMediaController {
    private static final Logger LOG = LoggerFactory.getLogger(SocialMediaController.class);

    private AccountService accountService;
    private MessageService messageService;

    public SocialMediaController() {
        accountService = new AccountService();
        messageService = new MessageService();
    }

    public Javalin startAPI() {
        Javalin app = Javalin.create();
        app.post("/register", this::register);
        app.post("/login", this::login);
        app.post("/messages", this::createMessage);
        app.get("/messages", this::getAllMessages);
        app.get("/messages/{message_id}", this::getMessage);
        app.delete("/messages/{message_id}", this::deleteMessage);
        app.patch("/messages/{message_id}", this::updateMessage);
        app.get("/accounts/{account_id}/messages", this::getAccountMessages);

        return app;
    }

    private void register(Context context) {
        ObjectMapper om = new ObjectMapper();
        Account acc = null;
        try {
            acc = om.readValue(context.body(), Account.class);
        } catch (JsonProcessingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        /*
         *  username length not blank &&
         *  password length >= 4 &&
         *  username does not exist
         * 
         *      Respond with JSON of new account (with account_id generated)
         *      Response 200
         * 
         *  else
         *      status 400
         */
        Account result = accountService.registerAccount(acc);
        if (result != null) {
            context.json(result).status(200);
        } else {
            context.status(400);
        }
    }

    private void login(Context context) {
        ObjectMapper om = new ObjectMapper();
        Account acc = null;
        try {
            acc = om.readValue(context.body(), Account.class);
        } catch (JsonProcessingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        /* 
         *  username exists &&
         *  password matches account password
         *      Respond with JSON of logged in account (with account_id)
         *      Response 200
         * 
         *  else
         *      status 401
         */

        Account match = accountService.getAccountFromUsername(acc.getUsername());
        if (match != null && acc.password.equals(match.password)) {
            context.json(match).status(200);
        } else {
            context.status(401);
        }
    }

    private void createMessage(Context context) {
        ObjectMapper om = new ObjectMapper();
        Message msg = null;
        try {
            msg = om.readValue(context.body(), Message.class);
        } catch (JsonProcessingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        /* 
         *  message_text not blank &&
         *  message_text.length() < 255
         *  posted_by is an actual account
         *      Respond with JSON of new message (with message_id generated)
         *      Response 200
         * 
         *  else
         *      Response 400
         */

        Message result = messageService.createMessage(msg);
        if (result != null) {
                context.json(result).status(200);
        } else {
            context.status(400);
        }
    }

    private void getAllMessages(Context context) {
        // getAllMessages() returns all msgs in the database or
        // an empty list if there are none
        List<Message> result = messageService.getAllMessages();
        
        if (result != null) {
            context.json(result).status(200);
        } else {
            context.status(200);
        }
    }

    private void getMessage(Context context) {
        // getMessageFromID() returns the message or
        // null if otherwise
        Message result = messageService.getMessageFromID(Integer.parseInt(context.pathParam("message_id")));
        
        if (result != null) {
            context.json(result).status(200);
        } else {
            context.status(200);
        }
    }

    private void deleteMessage(Context context) {
        // deleteMessage() returns the deleted message if it existed or
        // null if otherwise
        Message result = messageService.deleteMessage(Integer.parseInt(context.pathParam("message_id")));

        if (result != null) {
            context.json(result).status(200);
        } else {
            context.status(200);
        }
    }

    private void updateMessage(Context context) {
        int message_id = Integer.parseInt(context.pathParam("message_id"));

        ObjectMapper om = new ObjectMapper();
        Message msg = null;
        try {
            msg = om.readValue(context.body(), Message.class);
        } catch (JsonProcessingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        /*
         *  message_id exists &&
         *  message_text is not blank
         *  message_text.length() <= 255
         *      Respond with JSON of updated message (with message_id)
         *      Response 200
         * 
         *  else
         *      Response 400
         */
        
        Message result = messageService.updateMessage(message_id, msg);

        if (result != null) {
            context.json(result).status(200);
        } else {
            context.status(400);
        }
    }

    private void getAccountMessages(Context context) {
        // getAccountMessages() returns all msgs from the account or
        // an empty list if there are none
        List<Message> result = accountService.getAccountMessages(Integer.parseInt(context.pathParam("account_id")));

        if (result != null) {
            context.json(result).status(200);
        } else {
            context.status(200);
        }
    }
}