import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.*;
import javax.swing.table.DefaultTableModel;

public class TestGUI {
    private static JFrame frame;
    private static JTable table;
    private static DefaultTableModel model;

    public static void main(String[] args) {
        // Create the frame
        frame = new JFrame("Simple Finance App");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 400);

        // Create the table model and the table
        model = new DefaultTableModel();
        model.addColumn("Date");
        model.addColumn("Amount");
        model.addColumn("Category");
        model.addColumn("Description");

        table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);

        // Create the input panel for manual entry with a more user-friendly layout
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(4, 2, 5, 5));

        JTextField dateField = new JTextField();
        JTextField amountField = new JTextField();
        JTextField categoryField = new JTextField();
        JTextField descriptionField = new JTextField();

        panel.add(new JLabel("Date:"));
        panel.add(dateField);
        panel.add(new JLabel("Amount:"));
        panel.add(amountField);
        panel.add(new JLabel("Category:"));
        panel.add(categoryField);
        panel.add(new JLabel("Description:"));
        panel.add(descriptionField);

        // Create buttons
        JButton addButton = new JButton("Add Entry");
        JButton importButton = new JButton("Import CSV");
        JButton exportButton = new JButton("Export CSV");

        // Add action listener for "Add Entry" button
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String date = dateField.getText();
                String amountStr = amountField.getText();
                String category = categoryField.getText();
                String description = descriptionField.getText();

                if (!date.isEmpty() && !amountStr.isEmpty() && !category.isEmpty() && !description.isEmpty()) {
                    try {
                        double amount = Double.parseDouble(amountStr);
                        model.addRow(new Object[]{date, amount, category, description});
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(frame, "Amount should be a valid number.");
                    }
                } else {
                    JOptionPane.showMessageDialog(frame, "Please fill all fields.");
                }
            }
        });

        // Add action listener for "Import CSV" button
        importButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setDialogTitle("Choose a CSV File");
                int result = fileChooser.showOpenDialog(frame);

                if (result == JFileChooser.APPROVE_OPTION) {
                    File file = fileChooser.getSelectedFile();
                    importCSV(file);
                }
            }
        });

        // Add action listener for "Export CSV" button
        exportButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setDialogTitle("Save CSV File");
                int result = fileChooser.showSaveDialog(frame);

                if (result == JFileChooser.APPROVE_OPTION) {
                    File file = fileChooser.getSelectedFile();
                    exportCSV(file);
                }
            }
        });

        // Layout setup for buttons
        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
        bottomPanel.add(addButton);
        bottomPanel.add(importButton);
        bottomPanel.add(exportButton);

        // Layout setup for the whole window
        frame.setLayout(new BorderLayout());
        frame.add(scrollPane, BorderLayout.CENTER);  // Table in the center
        frame.add(panel, BorderLayout.NORTH);        // Input fields at the top
        frame.add(bottomPanel, BorderLayout.SOUTH);  // Buttons at the bottom

        frame.setVisible(true);
    }

    private static void importCSV(File file) {
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            // Skip header
            br.readLine();
            while ((line = br.readLine()) != null) {
                String[] fields = line.split(",");
                if (fields.length == 4) {
                    String date = fields[0];
                    double amount = Double.parseDouble(fields[1]);
                    String category = fields[2];
                    String description = fields[3];

                    model.addRow(new Object[]{date, amount, category, description});
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(frame, "Error reading CSV file.");
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(frame, "Error in CSV file format.");
        }
    }

    private static void exportCSV(File file) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            // Write CSV header
            writer.write("Date,Amount,Category,Description\n");

            // Write rows
            for (int i = 0; i < model.getRowCount(); i++) {
                String date = (String) model.getValueAt(i, 0);
                double amount = (Double) model.getValueAt(i, 1);
                String category = (String) model.getValueAt(i, 2);
                String description = (String) model.getValueAt(i, 3);
                writer.write(String.format("%s,%.2f,%s,%s\n", date, amount, category, description));
            }

            JOptionPane.showMessageDialog(frame, "CSV file exported successfully.");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(frame, "Error writing to CSV file.");
        }
    }
}


