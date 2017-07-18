package murray;


public class TransferRequest {


    private String sourceAccountNumber;
    private String targetAccountNumber;
    private double amount;
    private boolean amountIsSource;

    public TransferRequest() {

    }

    public TransferRequest(String sourceAccountNumber, String targetAccountNumber, double amount, boolean amountIsSource) {
        this.sourceAccountNumber = sourceAccountNumber;
        this.targetAccountNumber = targetAccountNumber;
        this.amount = amount;
        this.amountIsSource = amountIsSource;
    }

    public String getSourceAccountNumber() {
        return sourceAccountNumber;
    }


    public String getTargetAccountNumber() {
        return targetAccountNumber;
    }

    public double getAmount() {
        return amount;
    }

    public boolean isAmountIsSource() {
        return amountIsSource;
    }

    public void setSourceAccountNumber(String sourceAccountNumber) {
        this.sourceAccountNumber = sourceAccountNumber;
    }

    public void setTargetAccountNumber(String targetAccountNumber) {
        this.targetAccountNumber = targetAccountNumber;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public void setAmountIsSource(boolean amountIsSource) {
        this.amountIsSource = amountIsSource;
    }
}
