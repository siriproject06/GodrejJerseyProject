package com.godrejjersey;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class AnalysisFrame extends JFrame {

    private JTextArea analysisArea;

    public AnalysisFrame() {
        setTitle("Liquidity Ratio Analysis");
        setSize(650, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        analysisArea = new JTextArea();
        analysisArea.setEditable(false);
        analysisArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
        analysisArea.setLineWrap(true);
        analysisArea.setWrapStyleWord(true);

        add(new JScrollPane(analysisArea));

        loadAnalysis();
    }

    private void loadAnalysis() {
        String sql = """
                SELECT financial_year, current_ratio,
                       quick_ratio, cash_ratio
                FROM liquidity_data
                ORDER BY financial_year
                """;

        StringBuilder result = new StringBuilder();

        result.append("LIQUIDITY RATIO ANALYSIS\n");
        result.append("========================\n\n");

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            boolean hasRecords = false;

            while (resultSet.next()) {
                hasRecords = true;

                String year =
                        resultSet.getString("financial_year");

                double currentRatio =
                        resultSet.getDouble("current_ratio");

                double quickRatio =
                        resultSet.getDouble("quick_ratio");

                double cashRatio =
                        resultSet.getDouble("cash_ratio");

                result.append("Financial Year: ")
                        .append(year)
                        .append("\n");

                result.append(String.format(
                        "Current Ratio: %.2f%n",
                        currentRatio
                ));

                result.append(String.format(
                        "Quick Ratio: %.2f%n",
                        quickRatio
                ));

                result.append(String.format(
                        "Cash Ratio: %.2f%n",
                        cashRatio
                ));

                result.append("\nInterpretation:\n");

                if (currentRatio >= 2) {
                    result.append(
                            "- Current ratio indicates a strong ability " +
                            "to meet short-term obligations.\n"
                    );
                } else if (currentRatio >= 1) {
                    result.append(
                            "- Current ratio indicates that current " +
                            "assets can cover current liabilities.\n"
                    );
                } else {
                    result.append(
                            "- Current ratio indicates possible short-term " +
                            "liquidity difficulty.\n"
                    );
                }

                if (quickRatio >= 1) {
                    result.append(
                            "- Quick ratio indicates satisfactory " +
                            "immediate liquidity.\n"
                    );
                } else {
                    result.append(
                            "- Quick ratio is below 1, indicating dependence " +
                            "on inventory or other current assets.\n"
                    );
                }

                if (cashRatio >= 0.5) {
                    result.append(
                            "- Cash ratio indicates a relatively strong " +
                            "cash position.\n"
                    );
                } else {
                    result.append(
                            "- Cash ratio indicates that cash alone may " +
                            "not cover all current liabilities.\n"
                    );
                }

                result.append("\n------------------------\n\n");
            }

            if (!hasRecords) {
                result.append(
                        "No financial records available.\n" +
                        "Please enter data first."
                );
            }

            analysisArea.setText(result.toString());

        } catch (Exception ex) {
            analysisArea.setText(
                    "Error loading analysis:\n" + ex.getMessage()
            );
        }
    }
}