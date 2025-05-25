package controller;

import controller.Impl.AccountBookControllerImpl;

import java.io.IOException;
import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Core account book control interface defining fundamental accounting management operations
 */
public interface AccountBookController {

    /* Basic data model definitions */
    record Record(Date date, double amount, String category, String description) {}
    record DateRange(Date startDate, Date endDate) {}

    /**
     * Initializes date format configuration (timezone, strict mode)
     */
    void initializeDateFormat();

    /**
     * Executes core date range search operation
     *
     * @param startInput Start date input (accepts non-standard formats)
     * @param endInput End date input (accepts non-standard formats)
     * @return Date-grouped records in descending order
     * @throws ParseException When date parsing fails
     * @throws IOException When data file read operation fails
     * @throws IllegalArgumentException When invalid date logic is detected
     */
    Map<Date, List<AccountBookControllerImpl.Record>> searchRecords(String startInput, String endInput)
            throws ParseException, IOException, IllegalArgumentException;

    /**
     * Formats Date object to standardized string
     * @param date Date object to format
     * @return Date string conforming to YYYY-MM-DD specification
     */
    String formatDate(Date date);
}