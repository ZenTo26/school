package	AdminForm.OperationMethod;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import AdminForm.AdminGUI;
public class AddGUI extends JFrame {
    private JTextField firstNameField;
    private JTextField lastNameField;
    private JTextField dobField;
    private JTextField addressField;
    private JTextField sexField;
    private JTextField phoneNumberField;
    private JButton addButton;
    private Connection connection;
    private AdminGUI adminGUI;

    public AddGUI(Connection connection, AdminGUI adminGUI) {
        this.connection = connection;
        this.adminGUI = adminGUI;

        setTitle("Add Information");
        setSize(400, 300);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        initComponents();
        addComponents();

        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String firstName = firstNameField.getText();
                String lastName = lastNameField.getText();
                String dob = dobField.getText();
                String address = addressField.getText();
                String sex = sexField.getText();
                String phoneNumber = phoneNumberField.getText();

                if (addInformation(firstName, lastName, dob, address, sex, phoneNumber)) {
                    JOptionPane.showMessageDialog(AddGUI.this, "Information added successfully!");
                    dispose(); // Close the AddGUI window
                    adminGUI.setVisible(true); // Show the AdminGUI
                } else {
                    JOptionPane.showMessageDialog(AddGUI.this, "Failed to add information!");
                }
            }
        });
        setExtendedState(JFrame.MAXIMIZED_BOTH); // Set to full screen
        setVisible(true); // Make the frame visible
    }

    private void initComponents() {
        firstNameField = new JTextField(20);
        lastNameField = new JTextField(20);
        dobField = new JTextField(20);
        addressField = new JTextField(20);
        sexField = new JTextField(20);
        phoneNumberField = new JTextField(20);
        addButton = new JButton("Add");

        Font labelFont = new Font("Arial", Font.BOLD, 14);
        Font fieldFont = new Font("Arial", Font.PLAIN, 14);

        firstNameField.setFont(fieldFont);
        lastNameField.setFont(fieldFont);
        dobField.setFont(fieldFont);
        addressField.setFont(fieldFont);
        sexField.setFont(fieldFont);
        phoneNumberField.setFont(fieldFont);

        addButton.setFont(new Font("Arial", Font.BOLD, 14));
        addButton.setBackground(new Color(0, 153, 51));
        addButton.setForeground(Color.WHITE);
    }

    private void addComponents() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(new JLabel("First Name:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        panel.add(firstNameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(new JLabel("Last Name:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        panel.add(lastNameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(new JLabel("Date of Birth (YYYY-MM-DD):"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
        panel.add(dobField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        panel.add(new JLabel("Address:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 3;
        panel.add(addressField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        panel.add(new JLabel("Sex:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 4;
        panel.add(sexField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 5;
        panel.add(new JLabel("Phone Number:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 5;
        panel.add(phoneNumberField, gbc);

        gbc.gridx = 1;
        gbc.gridy = 6;
        panel.add(addButton, gbc);

        add(panel);
    }

    private boolean addInformation(String firstName, String lastName, String dob, String address, String sex, String phoneNumber) {
        String query = "INSERT INTO Students (FirstName, LastName, DateOfBirth, Address, PhoneNumber, Sex) VALUES (?, ?, ?, ?, ?, ?)";
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, firstName);
            statement.setString(2, lastName);
            statement.setString(3, dob);
            statement.setString(4, address);
            statement.setString(5, phoneNumber);
            statement.setString(6, sex);
            int rowsAffected = statement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
