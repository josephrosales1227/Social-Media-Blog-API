package Service;
import Model.Account;
import DAO.AccountDAO;

public class AccountService {
    AccountDAO accountDAO;

    public AccountService() {
        accountDAO = new AccountDAO();
    }

    public Account addAccount(Account acc) {
        return this.accountDAO.insertAccount(acc);
    }

    public boolean accountExists(Account acc) {
        return this.accountDAO.accountExists(acc);
    }

    public boolean accountExists(int id) {
        return this.accountDAO.accountExists(id);
    }

    public Account loginAccount(Account acc) {
        return this.accountDAO.loginAccount(acc);
    }
}