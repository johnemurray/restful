package murray;

import static murray.Currency.*;


public class FXServiceImpl implements FXService {

    private static final double EURGBP = 0.8847;
    private static final double EURUSD = 1.1527;
    private static final double GBPUSD = 1.3029;

    @Override
    public double convert(Currency from, Currency to, double amount) {

        if (amount == 0) {
            return 0;
        } else if (from == to) {
            return amount;
        } else if (from == EUR) {
            if (to == GBP) {
                return amount * EURGBP;
            } else if (to == USD) {
                return amount * EURUSD;
            }
        } else if (from == GBP) {
            if (to == EUR) {
                return amount / EURGBP;
            } else if (to == USD) {
                return amount * GBPUSD;
            }
        } else if (from == USD) {
            if (to == EUR) {
                return amount / EURUSD;
            } else if (to == GBP) {
                return amount * GBPUSD;
            }
        }
        throw new IllegalArgumentException("Could not convert [from=" + from + ", to=" + to + "]");


    }
}
