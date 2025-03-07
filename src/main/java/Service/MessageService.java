package Service;

import java.util.List;

import DAO.MessageDAO;
import Model.Account;
import Model.Message;

public class MessageService {
    private MessageDAO messageDAO;

    public MessageService() {
        this.messageDAO = new MessageDAO();
    }

    public Message createMessage(Message message) {
        if (message.getMessage_text() == "" || message.getMessage_text().length() >= 255)
            return null;
        return messageDAO.createMessage(message);
    }

    public List<Message> getAllMessages() {
        return messageDAO.getAllMessages();
    }

    public Message getMessageById(int message_id) {
        return messageDAO.getMessageById(message_id);
    }

    public Message deleteMessageById(int message_id) {
        Message messageToDelete = messageDAO.getMessageById(message_id);

        if (messageToDelete != null) {
            messageDAO.deleteMessageById(message_id);
            return messageToDelete; 
        }
        return null;
    }

    public Message updateMessageById(int message_id, Message message, String new_message) {
        Message existingMessage = messageDAO.getMessageById(message_id);
    
        if (existingMessage == null || new_message == null || new_message == "" || new_message.length() > 255) {
            return null; 
        }
    
        messageDAO.updateMessageById(message_id, new_message);
    
        return messageDAO.getMessageById(message_id);
    }    

    public List<Message> getMessagesByAccount(Account account) {
        return messageDAO.getMessagesByAccount(account);

    }

}
