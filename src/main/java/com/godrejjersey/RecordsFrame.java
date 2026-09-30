package com.godrejjersey;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class RecordsFrame extends JFrame {

    private JTable table;
    private DefaultTableModel tableModel;

    public RecordsFrame() {
        setTitle("Financial Records");
        setSize(900, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        String[] columns = {
                "ID",
                "Financial Year",
                "Current Assets",
                "Current Liabilities",
                "Inventory",
                "Cash Equivalents",
                "Current Ratio",
                "Quick Ratio",
                "Cash Ratio"
        };

        tableModel = new DefaultTableModel(columns, 0);
        table = new JTable(tableModel);

        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        loadRecords();
    }

    private void loadRecords() {
        String sql = """
                SELECT id, financial_year, current_assets,
                       current_liabilities, inventory,
                       cash_equivalents, current_ratio,
                       quick_ratio, cash_ratio
                FROM liquidity_data
                ORDER BY id DESC
                """;

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                Object[] row = {
                        resultSet.getInt("id"),
                        resultSet.getString("financial_year"),
                        resultSet.getDouble("current_assets"),
                        resultSet.getDouble("current_liabilities"),
                        resultSet.getDouble("inventory"),
                        resultSet.getDouble("cash_equivalents"),
                        resultSet.getDouble("current_ratio"),
                        resultSet.getDouble("quick_ratio"),
                        resultSet.getDouble("cash_ratio")
                };

                tableModel.addRow(row);
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(
                    this,
                    "Error loading records:\n" + ex.getMessage(),
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }
}