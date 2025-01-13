package Service;

import java.util.List;

import DAO.AccountDAO;
import DAO.MessageDAO;
import Model.Account;
import Model.Message;

public class AccountService {

    private AccountDAO accountDAO;
    private MessageDAO messageDAO;

    public AccountService() {
        accountDAO = new AccountDAO();
        messageDAO = new MessageDAO();
    }

    public Account getAccountFromUsername(String username) {
        return accountDAO.getAccountFromUsername(username);
    }

        /*
         *  username length not blank &&
         *  password length >= 4 &&
         *  username does not exist
         *      new account object
         * 
         *  else
         *      null
         */
    public Account registerAccount(Account acc) {
        if (!acc.getUsername().isBlank() && acc.getPassword().length() >= 4) {
            if (getAccountFromUsername(acc.getUsername()) == null) {
                return accountDAO.registerAccount(acc);
            }
        }
        
        return null;
    }

    public Account getAccountFromID(int account_id) {
        return accountDAO.getAccountFromID(account_id);
    }

    public List<Message> getAccountMessages(int account_id) {
        return accountDAO.getAccountMessages(account_id);
    }
    
}
