import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.util.Locale;

public class RentalAgreement {
    private int rentalDayCount;
    private BigDecimal discountPercent;
    private LocalDate checkoutDate;
    private LocalDate dueDate;
    public RentalTool rentalTool;
    private int chargeDays;
    private BigDecimal preDiscountCharge;
    private BigDecimal discountAmount;
    private BigDecimal finalCharge;

    public RentalAgreement (RentalTool rentalTool, int rentalDayCount, Long discountPercent, LocalDate checkoutDate) {
        this.setRentalDayCount(rentalDayCount);
        this.setDiscountPercent(BigDecimal.valueOf(discountPercent));
        this.rentalTool = rentalTool;
        this.checkoutDate = checkoutDate;
        this.dueDate = checkoutDate.plusDays(rentalDayCount);

        computeAndSetChargeDays(checkoutDate, dueDate);
        computeAndSetRentalCost();
    }

    public BigDecimal getPreDiscountCharge() {
        return preDiscountCharge;
    }

    public BigDecimal getDiscountAmount() {
        return discountAmount;
    }

    public BigDecimal getFinalCharge() {
        return finalCharge;
    }

    public int getChargeDays() {
        return chargeDays;
    }

    private void setRentalDayCount(int rentalDayCount) {
        if (rentalDayCount < 1) {
            throw new RuntimeException("Rental day count cannot be less that 1");
        }
        this.rentalDayCount = rentalDayCount;
    }

    private void setDiscountPercent(BigDecimal discountPercent) {
        if (discountPercent.compareTo(BigDecimal.ZERO) < 0 || discountPercent.compareTo(BigDecimal.valueOf(100)) > 0) {
            throw new RuntimeException("Discount % must be in the range of 0 - 100");
        }
        this.discountPercent = discountPercent;
    }

    /**
     * compute and set chargeable rental days.
     */
    private void computeAndSetChargeDays(LocalDate checkoutDate, LocalDate dueDate) {

        LocalDate chargeDay = checkoutDate.plusDays(1); //charging starts day after checkout date.
        LocalDate laborDay = computeLaborDayDate(checkoutDate);
        LocalDate julyFourthObserved = computeObservedJulyFourth(checkoutDate);

        /**
         * the year the tool is checked out must be kept track of to account for
         * scenario where during the course of the rental period a new year is
         * entered, resulting in needing to re-calculate the labor day and
         * observable July 4th holidays for that year sense they may fall on
         * different days.
         */
        int yearOfCheckout = checkoutDate.getYear();

        int chargeableDays = 0;

        /**
         * compute chargeable days.
         */
        while (!chargeDay.isAfter(dueDate)) {

            /**
             * reset holidays if new year is entered
             */
            if (chargeDay.getYear() != yearOfCheckout) {
                laborDay = computeLaborDayDate(chargeDay);
                julyFourthObserved = computeObservedJulyFourth(chargeDay);
            }

            int dayOfWeek = chargeDay.getDayOfWeek().getValue();

            /**
             * if charge day falls on weekend and tool is weekend chargeable, add charge day.
             */
            if (rentalTool.isWeekendCharge() && (dayOfWeek == 6 || dayOfWeek == 7)) {
                chargeableDays++;

                /**
                 * if charge day lands on the weekday, check if holidays are chargeable.
                 * if holiday chargeable, then charge for day.
                 * if not holiday chargeable then charge only if charge day is not holiday.
                 */
            } else if (rentalTool.isWeekdayCharge() && (dayOfWeek >= 1 && dayOfWeek <= 5)) {
                if (rentalTool.isHolidayCharge()) {
                    chargeableDays++;
                } else if (!chargeDay.equals(laborDay) && !chargeDay.equals(julyFourthObserved)){
                    chargeableDays++;
                }
            }
            chargeDay = chargeDay.plusDays(1);
        }
        chargeDays = chargeableDays;
    }

    /**
     * compute labor day for the year of parameter's date.
     * @param date
     * @return date of Labor Day
     */
    private LocalDate computeLaborDayDate(LocalDate date) {
        LocalDate dayCheck = LocalDate.of(date.getYear(), 9, 1);
        LocalDate laborDay = null;

        /**
         * starting at first day of September, find the first Monday.
         * Will be able to always find the first Monday within the
         * first 7 days of the Month, so only iterate a max of 7 days.
         */
        for (int i = 0; i < 7; i++) {
            if (dayCheck.getDayOfWeek().toString().equalsIgnoreCase("MONDAY")) {
                laborDay = dayCheck;
                break;
            } else {
                dayCheck = dayCheck.plusDays(1);
            }
        }
        return laborDay;
    }

    /**
     * compute weekday in which 4th of July is observed.
     * If for the year of the parameter date the 4th is on a Saturday, then
     * Friday will be the observable holiday. If the 4th is a Sunday, then
     * Monday will be the observable holiday.
     * @param date
     * @return date of weekday observable holiday for July 4th.
     */
    private LocalDate computeObservedJulyFourth(LocalDate date) {
        LocalDate dayCheck = LocalDate.of(date.getYear(), 7, 4);
        String dayOfWeek = dayCheck.getDayOfWeek().toString().toUpperCase();
        LocalDate fourthOfJulyObserved = dayOfWeek.equals("SATURDAY") ? dayCheck.minusDays(1) : dayOfWeek.equals("SUNDAY") ? dayCheck.plusDays(1) : dayCheck;

        return fourthOfJulyObserved;
    }

    /**
     * calculate rental costs, and apply discount if applicable.
     */
    private void computeAndSetRentalCost() {
        preDiscountCharge = BigDecimal.valueOf(chargeDays).multiply(BigDecimal.valueOf(rentalTool.getDailyCharge()));
        discountAmount = preDiscountCharge.multiply(discountPercent).divide(BigDecimal.valueOf(100)).setScale(2, RoundingMode.HALF_UP);
        finalCharge = preDiscountCharge.subtract(discountAmount);

    }

    @Override
    public String toString() {
        Locale locale = new Locale("en", "US");
        NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(locale);
        return
                "Tool code: " + rentalTool.getToolCode() + "\n" +
                "Tool type: " + rentalTool.getToolType() + "\n" +
                "Tool brand: " + rentalTool.getBrand() + "\n" +
                "Rental days: " + rentalDayCount + "\n" +
                "Check out date: " + checkoutDate + "\n" +
                "Due date: " + dueDate + "\n" +
                "Daily rental charge: " + currencyFormatter.format(rentalTool.getDailyCharge()) + "\n" +
                "Charge days: " + chargeDays + "\n" +
                "Pre-discount charge: " + currencyFormatter.format(preDiscountCharge) + "\n" +
                "Discount percent: " + discountPercent + "%" + "\n" +
                "Discount amount: " + currencyFormatter.format(discountAmount) + "\n" +
                "Final charge: " + currencyFormatter.format(finalCharge) + "\n";
    }



}
