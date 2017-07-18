package murray;

import org.junit.Test;

import static murray.Currency.*;
import static org.junit.Assert.*;


public class FXServiceImplTest {

    private final FXServiceImpl fxService = new FXServiceImpl();

    @Test
    public void testEURGBP() {
        assertEquals(88.47, fxService.convert(EUR, GBP, 100), 0.01);
    }

    @Test
    public void testGBPEUR() {
        assertEquals(113.03, fxService.convert(GBP, EUR, 100), 0.01);
    }

    @Test
    public void testEURUSD() {
        assertEquals(115.27, fxService.convert(EUR, USD, 100), 0.01);
    }

    @Test
    public void testUSDEUR() {
        assertEquals(86.75, fxService.convert(USD, EUR, 100), 0.01);
    }

    @Test
    public void testGBPUSD() {
        assertEquals(130.29, fxService.convert(GBP, USD, 100), 0.01);
    }

    @Test
    public void testUSDGBP() {
        assertEquals(130.29, fxService.convert(USD, GBP, 100), 0.01);
    }
}