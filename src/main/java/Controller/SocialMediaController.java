package Controller;

import io.javalin.Javalin;
import io.javalin.http.Context;
import Service.AccountService;
import Service.MessageService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import Model.Account;
import Model.Message;
import java.util.*;


public class SocialMediaController {
    AccountService accountService;
    MessageService messageService;

    public Javalin startAPI() {
        accountService = new AccountService();
        messageService = new MessageService();
        Javalin app = Javalin.create();
        app.post("/register", this::userRegistrationHandler);
        app.post("/login", this::loginHandler);
        app.post("/messages", this::createMessageHandler);
        app.get("/messages", this::retrieveMessagesHandler);
        app.get("/messages/{message_id}", this::retrieveMessageByIdHandler);
        app.delete("/messages/{message_id}", this::deleteMessageByIdHandler);
        app.patch("/messages/{message_id}", this::updateMessageByIdHandler);
        app.get("/accounts/{account_id}/messages", this::retrieveMessagesByUserHandler);
        return app;
    }

    private void userRegistrationHandler(Context context) throws JsonProcessingException{
        ObjectMapper mapper = new ObjectMapper();
        Account account = mapper.readValue(context.body(), Account.class);
        if(!validAccount(account)) {
            context.status(400);
        } else {
            Account addedAccount = accountService.addAccount(account);
            if(addedAccount == null) {
                context.status(400);
            } else {
                context.json(mapper.writeValueAsString(addedAccount));
            }
        }
    }

    private boolean validAccount(Account account) {
        String username = account.getUsername();
        String password = account.getPassword();
        if(username.length() == 0 || password.length() < 4) {
            return false;
        }
        
        return !accountService.accountExists(account);
    }

     private void loginHandler(Context context) throws JsonProcessingException{
        ObjectMapper mapper = new ObjectMapper();
        Account account = mapper.readValue(context.body(), Account.class);
        Account loggedAccount = accountService.loginAccount(account);
        if(loggedAccount == null) {
            context.status(401);
        } else {
            context.json(mapper.writeValueAsString(loggedAccount));
        }
     }

      private void createMessageHandler(Context context) throws JsonProcessingException{
        ObjectMapper mapper = new ObjectMapper();
        Message message = mapper.readValue(context.body(), Message.class);
        if(!validMessage(message)) {
            context.status(400);
        } else {
            Message addedMessage = messageService.addMessage(message);
            if(addedMessage == null) {
                context.status(401);
            } else {
                context.json(mapper.writeValueAsString(addedMessage));
            }
        }
      }

      private boolean validMessage(Message message) {
        if(message.getMessage_text().length() == 0 || message.getMessage_text().length() > 255) {
            return false;
        }
        return accountService.accountExists(message.getPosted_by());
      }

       private void retrieveMessagesHandler(Context context) throws JsonProcessingException{
            ObjectMapper mapper = new ObjectMapper();
            List<Message> list = messageService.getMessages();
            context.json(mapper.writeValueAsString(list));
       }

        private void retrieveMessageByIdHandler(Context context) throws JsonProcessingException{
            ObjectMapper mapper = new ObjectMapper();
            int message_id = Integer.parseInt(context.pathParam("message_id"));
            Message message = messageService.getMessageById(message_id);
            if(message != null) {
                context.json(mapper.writeValueAsString(message));
            }
        }

         private void deleteMessageByIdHandler(Context context) throws JsonProcessingException{
            ObjectMapper mapper = new ObjectMapper();
            int message_id = Integer.parseInt(context.pathParam("message_id"));
            Message message = messageService.deleteMessageById(message_id);
            if(message != null) {
                context.json(mapper.writeValueAsString(message));
            }
         }

          private void updateMessageByIdHandler(Context context) throws JsonProcessingException{
            ObjectMapper mapper = new ObjectMapper();
            int message_id = Integer.parseInt(context.pathParam("message_id"));
            String body = context.body();
            String new_message_text = body.substring(18, body.length() - 3);
            Message message = messageService.getMessageById(message_id);
            if(message != null && new_message_text.length() > 0 && new_message_text.length() < 255) {
                messageService.updateMessage(message_id, new_message_text);
                Message updatedMessage = messageService.getMessageById(message_id);
                context.json(mapper.writeValueAsString(updatedMessage));

            } else {
                context.status(400);
            }
          }


           private void retrieveMessagesByUserHandler(Context context) throws JsonProcessingException{
                ObjectMapper mapper = new ObjectMapper();
                int account_id = Integer.parseInt(context.pathParam("account_id"));
                List<Message> list = messageService.getMessagesByUser(account_id);
                context.json(mapper.writeValueAsString(list));
           }
}