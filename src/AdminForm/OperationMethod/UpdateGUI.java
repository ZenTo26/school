package AdminForm.OperationMethod;

import school.db.database;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class UpdateGUI extends JFrame {
    private JTextField textField;
    private JButton submitButton;

    public UpdateGUI() {
        setTitle("Update Student");
        setSize(400, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Panel for input
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new BoxLayout(inputPanel, BoxLayout.Y_AXIS));
        inputPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel label = new JLabel("Enter Student ID:");
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        label.setFont(new Font("Arial", Font.BOLD, 16));

        textField = new JTextField(20);
        textField.setMaximumSize(new Dimension(Integer.MAX_VALUE, textField.getPreferredSize().height));
        textField.setAlignmentX(Component.CENTER_ALIGNMENT);

        inputPanel.add(label);
        inputPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        inputPanel.add(textField);

        // Panel for button
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(0, 20, 20, 20));

        submitButton = new JButton("Submit");
        submitButton.setFont(new Font("Arial", Font.BOLD, 14));
        submitButton.setBackground(new Color(30, 144, 255));
        submitButton.setForeground(Color.WHITE);
        submitButton.setFocusPainted(false);
        submitButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String studentID = textField.getText();
                if (validateStudentID(studentID)) {
                    dispose(); // Close the current window
                    int studentIDInt = Integer.parseInt(studentID); // Convert to int
                    new EditGUI(studentIDInt).setVisible(true); // Pass the int value to EditGUI
                } else {
                    JOptionPane.showMessageDialog(null, "Invalid student ID");
                }
            }
        });

        buttonPanel.add(submitButton);

        add(inputPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        setLocationRelativeTo(null); // Center the window
    }

    private boolean validateStudentID(String studentID) {
        try (Connection connection = new database().connectionDatabase();
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM Students WHERE StudentID = ?")) {
            statement.setString(1, studentID);
            ResultSet resultSet = statement.executeQuery();
            return resultSet.next(); // If there's a matching record, return true
        } catch (SQLException e) {
            e.printStackTrace();
            return false; // Return false in case of any error
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new UpdateGUI().setVisible(true));
    }
}
