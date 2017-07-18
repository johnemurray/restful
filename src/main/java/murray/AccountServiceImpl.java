package murray;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.collect.Maps;

import javax.ws.rs.*;
import java.util.Map;

@Path("/")
public class AccountServiceImpl implements AccountService {
    private static final Map<String, Account> accounts = Maps.newHashMap();
    private final FXService fxService;

    static {
        resetAccounts();
    }

    private static void resetAccounts() {
        accounts.clear();
        accounts.put("johnGBP", new AccountImpl(Currency.GBP, 1000));
        accounts.put("johnUSD", new AccountImpl(Currency.USD, 1000));
    }

    @VisibleForTesting
    AccountServiceImpl(FXService fxService, Map<String, Account> newAccountData) {
        this.fxService = fxService;
        accounts.clear();
        accounts.putAll(newAccountData);
    }

    public AccountServiceImpl() {
        this.fxService = new FXServiceImpl();
    }



    @GET
    @Path("/reset")
    @Override
    @Produces("application/json")
    @VisibleForTesting
    public Outcome reset() {
       resetAccounts();
       return new Outcome("OK");

    }

    @GET
    @Path("/getBalance/{accountNumber}")
    @Produces("application/json")
    @Override
    public Outcome getBalance(@PathParam("accountNumber") String accountNumber) {
        final Account account = accounts.get(accountNumber);
        if (account == null) {
            return new Outcome("Account " + accountNumber + " not found");
        }

        return new Outcome(new AccountBalance(account.getCurrency(), account.getBalance()));

    }


    @POST
    @Path("/transfer")
    @Produces("application/json")
    @Consumes("application/json")
    @Override
    public Outcome transfer(TransferRequest transferRequest) {
        if (transferRequest.getSourceAccountNumber().equals(transferRequest.getTargetAccountNumber())) {
            return new Outcome("User Validation error - does not make sense to transfer to source account");
        }

        final Account sourceAccount = accounts.get(transferRequest.getSourceAccountNumber());
        if (sourceAccount == null) {
            return new Outcome("Source account number " + transferRequest.getSourceAccountNumber() + " not found");
        }

        final Account targetAccount = accounts.get(transferRequest.getTargetAccountNumber());
        if (targetAccount == null) {
            return new Outcome("Target account number " + transferRequest.getTargetAccountNumber() + " not found");
        }
        final double amountToWithdraw;
        final double amountToDeposit;
        if (transferRequest.isAmountIsSource()) {
            amountToWithdraw = transferRequest.getAmount();
            amountToDeposit = fxService.convert(sourceAccount.getCurrency(), targetAccount.getCurrency(), transferRequest.getAmount());
        } else {
            amountToDeposit = transferRequest.getAmount();
            amountToWithdraw = fxService.convert(targetAccount.getCurrency(), sourceAccount.getCurrency(), transferRequest.getAmount());
        }
        try {
            final double newAccountBalance = sourceAccount.withdraw(amountToWithdraw);
            targetAccount.deposit(amountToDeposit);
            final AccountBalance accountBalance = new AccountBalance(sourceAccount.getCurrency(), newAccountBalance);
            return new Outcome(accountBalance);
        } catch (InsufficientFundsException e) {
            return new Outcome(e.getMessage());
        }
    }
}
