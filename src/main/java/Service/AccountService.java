package Service;

import DAO.AccountDAO;
import Model.Account;

public class AccountService {
    
    private AccountDAO accountDAO;

    public AccountService() {
        this.accountDAO = new AccountDAO();
    }

    public Account addAccount(Account account) {
        if (accountDAO.isValidAccountCreation(account))
            return accountDAO.insertAccount(account);

        return null;
    }

    public Account loginAccount(Account account) {
        return accountDAO.loginAccount(account);
    }

    public Account getAccountById(int account_id) {
        return accountDAO.getAccountById(account_id);
    }
    
}
