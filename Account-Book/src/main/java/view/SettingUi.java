package view;

import controller.Impl.UserControllerImpl;
import javax.swing.*;

/**
 * Interface defining the contract for application settings UI components.
 * This interface handles financial data directory management and user profile editing.
 *
 * @author Boliang Chen
 * @version 1.0.0
 * @since v1.0.0
 */
public interface SettingUi {

    /**
     * Displays the settings window with directory selection and user profile fields.
     * The window includes:
     * - Financial data directory selection
     * - User password change field
     * - Gender selection
     * - Age selection
     */
    void SettingWindow();

    /**
     * Creates and configures the main file path selection panel.
     * @return JPanel containing directory path display and selection controls
     */
    JPanel createFilePathPanel();

    /**
     * Creates and configures the user profile settings panel.
     * Includes fields for:
     * - Password change (optional)
     * - Gender selection (radio buttons)
     * - Age selection (dropdown)
     * @return JPanel containing all user profile editing components
     */
    JPanel createUserSettingPanel();

    /**
     * Creates the panel containing the save button for applying settings.
     * @return JPanel with properly aligned save button
     */
    JPanel createSaveButtonPanel();

    /**
     * Initializes and configures all button action listeners including:
     * - Directory chooser button
     * - Settings save button
     */
    void setupButtonListeners();
}
