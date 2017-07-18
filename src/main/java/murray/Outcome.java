package murray;

public class Outcome {

    private AccountBalance accountBalance;
    private String message;
    private boolean success;
    public Outcome(AccountBalance accountBalance) {
        this.accountBalance = accountBalance;
        this.success = true;
    }

    public Outcome(String errorMessage) {
        this.message = errorMessage;
        this.success = false;
    }



    public Outcome() {
        //for json
    }

    public AccountBalance getAccountBalance() {
        return accountBalance;
    }

    public void setAccountBalance(AccountBalance accountBalance) {
        this.accountBalance = accountBalance;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }


}
