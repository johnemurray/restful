package murray;

import com.google.common.collect.Maps;
import org.junit.Before;
import org.junit.Test;

import java.util.Map;

import static murray.Currency.GBP;
import static murray.Currency.USD;
import static org.junit.Assert.*;


public class AccountServiceImplTest {

    private final FXServiceImpl fxService = new FXServiceImpl();
    private AccountService accountService;

    @Before
    public void setUp() throws Exception {
        final Map<String, Account> accounts = Maps.newHashMap();
        accounts.put("johnGBP", new AccountImpl(GBP, 1000));
        accounts.put("johnEUR", new AccountImpl(Currency.EUR, 1000));
        accounts.put("johnUSD", new AccountImpl(USD, 1000));
        accounts.put("ninaGBP", new AccountImpl(GBP, 1000));
        accountService = new AccountServiceImpl(fxService, accounts);
    }

    @Test
    public void testGetBalance() {
        final Outcome outcome = accountService.getBalance("johnGBP");
        assertTrue(outcome.isSuccess());
        assertNull(outcome.getMessage());
        final AccountBalance accountBalance = outcome.getAccountBalance();
        assertEquals(1000, accountBalance.getAmount(), 0.001);
        assertEquals(GBP, accountBalance.getCurrency());
    }


    @Test
    public void testGetBalance_accountNotFound() {
        final Outcome outcome = accountService.getBalance("johnZZZ");
        assertFalse(outcome.isSuccess());
        assertNull(outcome.getAccountBalance());
        assertEquals("Account johnZZZ not found", outcome.getMessage());
    }

    @Test
    public void testTransferGBPUSDtrue() {
        final AccountBalance newSourceAccountBalance = accountService.transfer(new TransferRequest("johnGBP", "johnUSD", 100, true)).getAccountBalance();
        assertEquals(900, newSourceAccountBalance.getAmount(), 0.001);
        final Outcome outcome = accountService.getBalance("johnUSD");
        final AccountBalance accountBalance = outcome.getAccountBalance();
        assertTrue(outcome.isSuccess());
        assertEquals(1000 + fxService.convert(GBP, USD, 100), accountBalance.getAmount(), 0.001);
    }


    @Test
    public void testTransferGBPUSDtrue_notEnoughMoneyInAccount() throws Exception {
        final Outcome outcome = accountService.transfer(new TransferRequest("johnGBP", "johnUSD", 1001, true));
        assertFalse(outcome.isSuccess());
        assertNull(outcome.getAccountBalance());
    }

    @Test
    public void testTransferGBPUSDfalse() throws Exception {
        final Outcome outcome = accountService.transfer(new TransferRequest("johnGBP", "johnUSD", 100, false));
        assertTrue(outcome.isSuccess());
        assertNull(outcome.getMessage());
        final AccountBalance newSourceAccountBalance = outcome.getAccountBalance();
        assertEquals(1000 - fxService.convert(GBP, USD, 100), newSourceAccountBalance.getAmount(), 0.001);
        final Outcome outcome2 = accountService.getBalance("johnUSD");
        assertEquals(1100, outcome2.getAccountBalance().getAmount(), 0.001);
    }


    @Test
    public void testTransferGBPUSDtrue_justEnoughMoneyInAccount() throws Exception {
        final AccountBalance newSourceAccountBalance = accountService.transfer(new TransferRequest("johnGBP", "johnUSD", 1000, true)).getAccountBalance();
        assertEquals(0, newSourceAccountBalance.getAmount(), 0.001);
        final AccountBalance outcome2 = accountService.getBalance("johnUSD").getAccountBalance();
        assertEquals(1000 + fxService.convert(GBP, USD, 1000), outcome2.getAmount(), 0.001);
    }

    @Test
    public void testTransferGBPUSDfalse_notEnoughMoneyInAccount() throws Exception {
        final Outcome outcome = accountService.transfer(new TransferRequest("johnGBP", "johnUSD", 1000, false));
        assertNull(outcome.getAccountBalance());
        assertFalse(outcome.isSuccess());
    }


    @Test
    public void testTransferGBPGBPtrue() throws Exception {
        final AccountBalance newSourceAccountBalance = accountService.transfer(new TransferRequest("johnGBP", "ninaGBP", 100, true)).getAccountBalance();
        assertEquals(900, newSourceAccountBalance.getAmount(), 0.001);
        final    AccountBalance outcome2 = accountService.getBalance("ninaGBP").getAccountBalance();
        assertEquals(1100, outcome2.getAmount(), 0.001);
    }


    @Test
    public void testTransferToSameAccountNotAllowed() throws Exception {
        Outcome outcome = accountService.transfer(new TransferRequest("johnGBP", "johnGBP", 1000, false));
        assertFalse(outcome.isSuccess());
    }


    @Test
    public void testSourceAccountNotFound() throws Exception {
        assertFalse(accountService.transfer(new TransferRequest("johnZZZ", "johnGBP", 1000, false)).isSuccess());
    }


    @Test
    public void testTargetAccountNotFound() throws Exception {
        accountService.transfer(new TransferRequest("johnGBP", "johnZZZ", 1000, false));
    }
}