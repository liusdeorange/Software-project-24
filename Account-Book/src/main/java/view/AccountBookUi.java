package view;

/**
 * Account book user interface defining core UI behaviors
 */
public interface AccountBookUi {

    /**
     * Creates main application window with control panel and result display area
     */
    void AccountBookWindow();

    /**
     * Retrieves formatted date string from date selection components
     * @param isStart Date type flag (true for start date, false for end date)
     * @return Standardized date string (format: yyyy-MM-dd)
     */
    String getSelectedDate(boolean isStart);

    /**
     * Initializes date formatting utilities (provides unified date format support for controllers)
     */
    void initializeDateFormat();
}
