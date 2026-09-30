package com.godrejjersey;

import javax.swing.*;
import java.awt.*;

public class DashboardFrame extends JFrame {

    public DashboardFrame() {
        setTitle("Godrej Jersey - Dashboard");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JLabel headingLabel = new JLabel(
                "Godrej Jersey Liquidity Ratio Analysis",
                SwingConstants.CENTER
        );
        headingLabel.setFont(new Font("Arial", Font.BOLD, 20));

        JButton dataEntryButton = new JButton("Enter Financial Data");
        JButton recordsButton = new JButton("View Records");
        JButton analysisButton = new JButton("View Ratio Analysis");
        JButton exitButton = new JButton("Exit");

        JPanel buttonPanel = new JPanel(new GridLayout(4, 1, 10, 10));
        buttonPanel.setBorder(
                BorderFactory.createEmptyBorder(30, 100, 30, 100)
        );

        buttonPanel.add(dataEntryButton);
        buttonPanel.add(recordsButton);
        buttonPanel.add(analysisButton);
        buttonPanel.add(exitButton);

        setLayout(new BorderLayout());
        add(headingLabel, BorderLayout.NORTH);
        add(buttonPanel, BorderLayout.CENTER);

        dataEntryButton.addActionListener(e ->
                new DataEntryFrame().setVisible(true)
        );

        recordsButton.addActionListener(e ->
                new RecordsFrame().setVisible(true)
        );

        analysisButton.addActionListener(e ->
                new AnalysisFrame().setVisible(true)
        );

        exitButton.addActionListener(e ->
                System.exit(0)
        );
    }
}