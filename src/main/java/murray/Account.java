package murray;


public interface Account {


    double withdraw(double amount) throws  InsufficientFundsException;

    double deposit(double amount) ;

    double getBalance();

    Currency getCurrency();
}
