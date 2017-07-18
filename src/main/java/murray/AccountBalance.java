package murray;


public class AccountBalance {
    private final Currency currency;
    private final double amount;

    public AccountBalance(Currency currency, double amount) {
        this.currency = currency;
        this.amount = amount;
    }

    public Currency getCurrency() {
        return currency;
    }

    public double getAmount() {
        return amount;
    }
}
