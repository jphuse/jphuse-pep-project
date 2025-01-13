package Service;

import java.util.List;

import DAO.AccountDAO;
import DAO.MessageDAO;
import Model.Message;

public class MessageService {

    private AccountDAO accountDAO;
    private MessageDAO messageDAO;

    public MessageService() {
        accountDAO = new AccountDAO();
        messageDAO = new MessageDAO();
    }

        /* 
         *  message_text not empty &&
         *  message_text.length() < 255
         *  posted_by is an actual account
         *      new msg object
         * 
         *  else
         *      null
         */
    public Message createMessage(Message msg) {
        if (!msg.message_text.isBlank() && 
            msg.message_text.length() < 255 &&
            accountDAO.getAccountFromID(msg.posted_by) != null) {

                return messageDAO.createMessage(msg);
        }

        return null;
    }

    public List<Message> getAllMessages() {
        return messageDAO.getAllMessages();
    }

    public Message getMessageFromID(int message_id) {
        return messageDAO.getMessageFromID(message_id);
    }

    public Message deleteMessage(int message_id) {
        return messageDAO.deleteMessage(message_id);
    }

        /*
         *  message_id exists &&
         *  message_text is not blank
         *  message_text.length() <= 255
         *      updated msg object
         * 
         *  else
         *      null
         */
    public Message updateMessage(int message_id, Message msg) {
        if (getMessageFromID(message_id) != null) {
            if (!msg.message_text.isBlank() && msg.message_text.length() <= 255) {
                msg.message_id = message_id;
                    return messageDAO.updateMessage(msg);
            }
        }

        return null;
    }
}
