package com.godrejjersey;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;

public class DataEntryFrame extends JFrame {

    private JTextField yearField;
    private JTextField currentAssetsField;
    private JTextField currentLiabilitiesField;
    private JTextField inventoryField;
    private JTextField cashEquivalentsField;

    public DataEntryFrame() {
        setTitle("Enter Financial Data");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(6, 2, 10, 10));
        panel.setBorder(
                BorderFactory.createEmptyBorder(20, 20, 20, 20)
        );

        panel.add(new JLabel("Financial Year:"));
        yearField = new JTextField();
        panel.add(yearField);

        panel.add(new JLabel("Current Assets:"));
        currentAssetsField = new JTextField();
        panel.add(currentAssetsField);

        panel.add(new JLabel("Current Liabilities:"));
        currentLiabilitiesField = new JTextField();
        panel.add(currentLiabilitiesField);

        panel.add(new JLabel("Inventory:"));
        inventoryField = new JTextField();
        panel.add(inventoryField);

        panel.add(new JLabel("Cash Equivalents:"));
        cashEquivalentsField = new JTextField();
        panel.add(cashEquivalentsField);

        JButton saveButton = new JButton("Calculate and Save");
        panel.add(new JLabel());
        panel.add(saveButton);

        add(panel);

        saveButton.addActionListener(e -> calculateAndSave());
    }

    private void calculateAndSave() {
        try {
            String financialYear = yearField.getText();

            double currentAssets =
                    Double.parseDouble(currentAssetsField.getText());

            double currentLiabilities =
                    Double.parseDouble(currentLiabilitiesField.getText());

            double inventory =
                    Double.parseDouble(inventoryField.getText());

            double cashEquivalents =
                    Double.parseDouble(cashEquivalentsField.getText());

            if (financialYear.isEmpty()) {
                JOptionPane.showMessageDialog(
                        this,
                        "Please enter the financial year."
                );
                return;
            }

            if (currentLiabilities == 0) {
                JOptionPane.showMessageDialog(
                        this,
                        "Current liabilities cannot be zero."
                );
                return;
            }

            double currentRatio =
                    currentAssets / currentLiabilities;

            double quickRatio =
                    (currentAssets - inventory) / currentLiabilities;

            double cashRatio =
                    cashEquivalents / currentLiabilities;

            String sql = """
                    INSERT INTO liquidity_data
                    (
                        financial_year,
                        current_assets,
                        current_liabilities,
                        inventory,
                        cash_equivalents,
                        current_ratio,
                        quick_ratio,
                        cash_ratio
                    )
                    VALUES (?, ?, ?, ?, ?, ?, ?, ?)
                    """;

            try (Connection connection = DBConnection.getConnection();
                 PreparedStatement statement =
                         connection.prepareStatement(sql)) {

                statement.setString(1, financialYear);
                statement.setDouble(2, currentAssets);
                statement.setDouble(3, currentLiabilities);
                statement.setDouble(4, inventory);
                statement.setDouble(5, cashEquivalents);
                statement.setDouble(6, currentRatio);
                statement.setDouble(7, quickRatio);
                statement.setDouble(8, cashRatio);

                statement.executeUpdate();

                JOptionPane.showMessageDialog(
                        this,
                        String.format(
                                "Data saved successfully!\n\n" +
                                "Current Ratio: %.2f\n" +
                                "Quick Ratio: %.2f\n" +
                                "Cash Ratio: %.2f",
                                currentRatio,
                                quickRatio,
                                cashRatio
                        )
                );

                clearFields();
            }

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(
                    this,
                    "Please enter valid numbers for financial values."
            );
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(
                    this,
                    "Error saving data:\n" + ex.getMessage(),
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void clearFields() {
        yearField.setText("");
        currentAssetsField.setText("");
        currentLiabilitiesField.setText("");
        inventoryField.setText("");
        cashEquivalentsField.setText("");
    }
}