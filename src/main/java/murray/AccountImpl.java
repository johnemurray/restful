package murray;

import com.google.common.util.concurrent.AtomicDouble;


public class AccountImpl implements Account {

    private final Currency currency;
    private AtomicDouble balance;

    public AccountImpl(Currency currency, double balance) {
        this.currency = currency;
        this.balance = new AtomicDouble(balance);
    }
    @Override
    public Currency getCurrency() {
        return currency;
    }

    @Override
    public double withdraw(final double amount) throws InsufficientFundsException {
        final double newBalance = balance.addAndGet(-amount);
        if (newBalance < 0) {
            balance.addAndGet(amount);
            throw new InsufficientFundsException();

        }
        return newBalance;

    }

    @Override
    public double deposit(final double amount) {
        if (amount > 0) {
            return balance.addAndGet(amount);
        } else {
            throw new IllegalArgumentException("Cannot deposit a non-negative amount [requested=" + amount + "]");
        }
    }

    @Override
    public double getBalance() {
        return balance.get();
    }
}
