import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class PointOfCheckout {

    /**
     * This will most likely be an entry point of a POST endpoint.
     * The method parameters would then be required request parameters,
     * which will enforce required method parameters being provided.
     */
    public RentalAgreement performeCheckout(String toolCode, int rentalDayCount, Long discountPercent, String checkoutDate) {

        /**
         * initialize database representation and fetch tool rental information.
         * This is a mock java only representation of what would probably be done
         * through a DAO object fetching rental data from a database for the given
         * tool code.
         */
        DatabaseRepresentation database = new DatabaseRepresentation();
        RentalTool rentalTool = database.rentalToolData.get(toolCode);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yy");
        LocalDate dateOfCheckout = LocalDate.parse(checkoutDate, formatter);

        return new RentalAgreement(rentalTool, rentalDayCount, discountPercent, dateOfCheckout);

    }
}
