import java.util.HashMap;
import java.util.Map;

public class DatabaseRepresentation {
    public Map<String, RentalTool> rentalToolData;
    /**
     *  This data will most likely be stored in a database.
     *  An H2 database instance can be used here as well.
     *
     *  populate database representation with tool rental information.
     */
    public DatabaseRepresentation() {
        this.rentalToolData = new HashMap<>();
        rentalToolData.put("CHNS", new RentalTool("CHNS", "Chainsaw", "Stihl", 1.49, true, false, true));
        rentalToolData.put("LADW", new RentalTool("LADW", "Ladder", "Werner", 1.99, true, true, false));
        rentalToolData.put("JAKD", new RentalTool("JAKD", "Jackhammer", "DeWalt", 2.99, true, false, false));
        rentalToolData.put("JAKR", new RentalTool("JAKR", "Jackhammer", "Ridgid", 2.99, true, false, false));
    }

}
