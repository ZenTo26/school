package LoginForm;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import school.db.database;
import AdminForm.AdminGUI;

public class LoginGUI extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private Connection connection;

    public LoginGUI(Connection connection) {
        this.connection = connection;

        setTitle("Login");
        setSize(400, 250);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        initComponents();
        addComponents();
        addActionListeners();
    }

    private void initComponents() {
        usernameField = new JTextField(20);
        passwordField = new JPasswordField(20);
        loginButton = new JButton("Login");

        // Set fonts and colors for components
        Font font = new Font("Arial", Font.PLAIN, 14);
        usernameField.setFont(font);
        passwordField.setFont(font);
        loginButton.setFont(new Font("Arial", Font.BOLD, 14));

        loginButton.setBackground(new Color(30, 144, 255));
        loginButton.setForeground(Color.WHITE);
    }

    private void addComponents() {
        JPanel panel = new JPanel();
        panel.setBackground(Color.LIGHT_GRAY);

        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setFont(new Font("Arial", Font.BOLD, 14));
        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setFont(new Font("Arial", Font.BOLD, 14));

        GroupLayout layout = new GroupLayout(panel);
        panel.setLayout(layout);
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);

        layout.setHorizontalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.CENTER)
                .addGroup(layout.createSequentialGroup()
                    .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(usernameLabel)
                        .addComponent(passwordLabel))
                    .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(usernameField)
                        .addComponent(passwordField)))
                .addComponent(loginButton)
        );

        layout.setVerticalGroup(
            layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(usernameLabel)
                    .addComponent(usernameField))
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(passwordLabel)
                    .addComponent(passwordField))
                .addGap(20)
                .addComponent(loginButton)
        );

        add(panel);
    }

    private void addActionListeners() {
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                String password = new String(passwordField.getPassword());

                if (authenticate(username, password)) {
                    JOptionPane.showMessageDialog(LoginGUI.this, "Login successful!");
                    openAdminGUI(); // Navigate to AdminGUI after successful login
                } else {
                    JOptionPane.showMessageDialog(LoginGUI.this, "Invalid username or password!");
                }
            }
        });
    }

    private boolean authenticate(String username, String password) {
        String query = "SELECT * FROM account WHERE username = ? AND password = ?";
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, username);
            statement.setString(2, password);
            ResultSet resultSet = statement.executeQuery();
            return resultSet.next(); // If a row is returned, credentials are valid
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private void openAdminGUI() {
        // Create an instance of AdminGUI and make it visible
        AdminGUI adminGUI = new AdminGUI();
        adminGUI.setVisible(true);
        dispose(); // Close the LoginGUI
    }

    public static void main(String[] args) {
        database db = new database();
        Connection connection = db.connectionDatabase();

        if (connection != null) {
            SwingUtilities.invokeLater(() -> {
                new LoginGUI(connection).setVisible(true);
            });
        } else {
            System.err.println("Failed to connect to the database.");
        }
    }
}
