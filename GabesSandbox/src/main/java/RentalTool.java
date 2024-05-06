
public class RentalTool {

    private String toolCode;
    private String toolType;
    private String brand;
    private Double dailyCharge;
    private boolean weekdayCharge;
    private boolean weekendCharge;
    private boolean holidayCharge;

    public RentalTool(String toolCode, String toolType,
                      String brand, Double dailyCharge,
                      boolean weekdayCharge, boolean weekendCharge,
                      boolean holidayCharge) {
        this.toolCode = toolCode;
        this.toolType = toolType;
        this.brand = brand;
        this.dailyCharge = dailyCharge;
        this.weekendCharge = weekendCharge;
        this.weekdayCharge = weekdayCharge;
        this.holidayCharge = holidayCharge;
    }

    public boolean isWeekdayCharge() {
        return weekdayCharge;
    }

    public boolean isWeekendCharge() {
        return weekendCharge;
    }

    public boolean isHolidayCharge() {
        return holidayCharge;
    }

    public String getToolCode() {
        return toolCode;
    }

    public String getToolType() {
        return toolType;
    }

    public String getBrand() {
        return brand;
    }

    public Double getDailyCharge() {
        return dailyCharge;
    }
}
