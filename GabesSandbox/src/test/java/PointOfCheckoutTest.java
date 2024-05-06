import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PointOfCheckoutTest {

    public PointOfCheckout pointOfCheckout;
    @BeforeEach
    void setUp() {
        pointOfCheckout = new PointOfCheckout();
    }

    /**
     *          Daily charge     Weekday charge     weekend charge      Holiday charge
     * Ladder       $1.99           Yes                 Yes                 No
     * Chainsaw     $1.49           Yes                 No                  Yes
     * Jackhammer   $2.99           Yes                 No                  No
     */

    @Test
    void testCheckoutFailure_test1() {
        Exception exception = Assertions.assertThrows(RuntimeException.class, ()-> pointOfCheckout.performeCheckout("JAKR", 5, 101L, "09/03/15"));
        assertEquals("Discount % must be in the range of 0 - 100", exception.getMessage());
    }

    @Test
    void testCheckoutFailure_test1_negative_discount() {
        Exception exception = Assertions.assertThrows(RuntimeException.class, ()-> pointOfCheckout.performeCheckout("JAKR", 5, -101L, "09/03/15"));
        assertEquals("Discount % must be in the range of 0 - 100", exception.getMessage());
    }

    @Test
    void testCheckoutSuccess_test2() {
        RentalAgreement rentalAgreement = pointOfCheckout.performeCheckout("LADW", 3, 10L, "07/02/20");
        int chargeableDays = rentalAgreement.getChargeDays();

        assertEquals(2, chargeableDays);
        assertEquals(BigDecimal.valueOf(3.98), rentalAgreement.getPreDiscountCharge());
        assertEquals(BigDecimal.valueOf(0.40).setScale(2, RoundingMode.HALF_UP), rentalAgreement.getDiscountAmount());
        assertEquals(BigDecimal.valueOf(3.58), rentalAgreement.getFinalCharge());
    }

    @Test
    void testCheckoutSuccess_test3() {
        RentalAgreement rentalAgreement = pointOfCheckout.performeCheckout("CHNS", 5, 25L, "07/02/15");
        int chargeableDays = rentalAgreement.getChargeDays();

        assertEquals(3, chargeableDays);
        assertEquals(BigDecimal.valueOf(4.47), rentalAgreement.getPreDiscountCharge());
        assertEquals(BigDecimal.valueOf(1.12).setScale(2, RoundingMode.HALF_UP), rentalAgreement.getDiscountAmount());
        assertEquals(BigDecimal.valueOf(3.35), rentalAgreement.getFinalCharge());
    }

    @Test
    void testCheckoutSuccess_test4() {
        RentalAgreement rentalAgreement = pointOfCheckout.performeCheckout("JAKD", 6, 0L, "09/03/15");
        int chargeableDays = rentalAgreement.getChargeDays();

        assertEquals(3, chargeableDays);
        assertEquals(BigDecimal.valueOf(8.97), rentalAgreement.getPreDiscountCharge());
        assertEquals(BigDecimal.valueOf(0).setScale(2, RoundingMode.HALF_UP), rentalAgreement.getDiscountAmount());
        assertEquals(BigDecimal.valueOf(8.97), rentalAgreement.getFinalCharge());
    }

    @Test
    void testCheckoutSuccess_test5() {
        RentalAgreement rentalAgreement = pointOfCheckout.performeCheckout("JAKR", 9, 0L, "07/02/15");
        int chargeableDays = rentalAgreement.getChargeDays();

        assertEquals(5, chargeableDays);
        assertEquals(BigDecimal.valueOf(14.95), rentalAgreement.getPreDiscountCharge());
        assertEquals(BigDecimal.valueOf(0).setScale(2, RoundingMode.HALF_UP), rentalAgreement.getDiscountAmount());
        assertEquals(BigDecimal.valueOf(14.95), rentalAgreement.getFinalCharge());
    }

    @Test
    void testCheckoutSuccess_test6() {
        RentalAgreement rentalAgreement = pointOfCheckout.performeCheckout("JAKR", 4, 50L, "07/02/20");
        int chargeableDays = rentalAgreement.getChargeDays();

        assertEquals(1, chargeableDays);
        assertEquals(BigDecimal.valueOf(2.99), rentalAgreement.getPreDiscountCharge());
        assertEquals(BigDecimal.valueOf(1.50).setScale(2, RoundingMode.HALF_UP), rentalAgreement.getDiscountAmount());
        assertEquals(BigDecimal.valueOf(1.49), rentalAgreement.getFinalCharge());
    }

    /**
     * test added to test overwritten RentalAgreement toString formatting.
     */
    @Test
    void testRentalAgreemenToString() {
        RentalAgreement rentalAgreement = pointOfCheckout.performeCheckout("JAKR", 4000, 5L, "07/02/20");
        System.out.println("\n" + rentalAgreement.toString() + "\n");
    }

}
