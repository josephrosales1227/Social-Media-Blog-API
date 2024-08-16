package Service;
import DAO.MessageDAO;
import Model.Message;
import java.util.*;

public class MessageService {
    MessageDAO messageDAO;

    public MessageService() {
        this.messageDAO = new MessageDAO();
    }

    public Message addMessage(Message message) {
        return this.messageDAO.addMessage(message);
    }

    public List<Message> getMessages() {
        return this.messageDAO.getMessages();
    }

    public Message getMessageById(int id) {
        return this.messageDAO.getMessageById(id);
    }

    public Message deleteMessageById(int id) {
        return this.messageDAO.deleteMessageById(id);
    }

    public void updateMessage(int id, String text) {
        this.messageDAO.updateMessage(id, text);
    }

    public List<Message> getMessagesByUser(int id) {
        return this.messageDAO.getMessagesByUser(id);
    }
}
