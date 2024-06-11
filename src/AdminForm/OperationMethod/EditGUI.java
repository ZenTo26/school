package AdminForm.OperationMethod;

import school.db.database;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EditGUI extends JFrame {
    private JTextField firstNameField;
    private JTextField lastNameField;
    private JTextField dateOfBirthField;
    private JTextField addressField;
    private JTextField phoneNumberField;
    private JComboBox<String> sexComboBox;
    private JComboBox<Integer> yearComboBox;
    private JComboBox<Integer> semesterComboBox;
    private JTextField scoreField;
    private JComboBox<String> subjectComboBox;
    private JButton updateButton;

    private int studentID;

    public EditGUI(int studentID) {
        this.studentID = studentID;

        setTitle("Edit Student Information");
        setSize(500, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel formPanel = new JPanel();
        formPanel.setLayout(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10);

        JLabel firstNameLabel = new JLabel("First Name:");
        firstNameLabel.setFont(new Font("Arial", Font.BOLD, 14));
        firstNameField = new JTextField(20);

        JLabel lastNameLabel = new JLabel("Last Name:");
        lastNameLabel.setFont(new Font("Arial", Font.BOLD, 14));
        lastNameField = new JTextField(20);

        JLabel dateOfBirthLabel = new JLabel("Date of Birth (YYYY-MM-DD):");
        dateOfBirthLabel.setFont(new Font("Arial", Font.BOLD, 14));
        dateOfBirthField = new JTextField(10);

        JLabel addressLabel = new JLabel("Address:");
        addressLabel.setFont(new Font("Arial", Font.BOLD, 14));
        addressField = new JTextField(50);

        JLabel phoneNumberLabel = new JLabel("Phone Number:");
        phoneNumberLabel.setFont(new Font("Arial", Font.BOLD, 14));
        phoneNumberField = new JTextField(15);

        JLabel sexLabel = new JLabel("Sex:");
        sexLabel.setFont(new Font("Arial", Font.BOLD, 14));
        sexComboBox = new JComboBox<>(new String[]{"M", "F"});

        JLabel yearLabel = new JLabel("Enrollment Year:");
        yearLabel.setFont(new Font("Arial", Font.BOLD, 14));
        yearComboBox = new JComboBox<>(new Integer[]{1, 2, 3, 4});

        JLabel semesterLabel = new JLabel("Enrollment Semester:");
        semesterLabel.setFont(new Font("Arial", Font.BOLD, 14));
        semesterComboBox = new JComboBox<>(new Integer[]{1, 2});

        JLabel subjectLabel = new JLabel("Subject:");
        subjectLabel.setFont(new Font("Arial", Font.BOLD, 14));
        subjectComboBox = new JComboBox<>();

        JLabel scoreLabel = new JLabel("Score:");
        scoreLabel.setFont(new Font("Arial", Font.BOLD, 14));
        scoreField = new JTextField(5);

        updateButton = new JButton("Update");
        updateButton.setFont(new Font("Arial", Font.BOLD, 14));
        updateButton.setBackground(new Color(30, 144, 255));
        updateButton.setForeground(Color.WHITE);
        updateButton.setFocusPainted(false);

        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Update student information in the database
                updateStudentInfo();
            }
        });

        // Adding components to the form panel
        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(firstNameLabel, gbc);
        gbc.gridx = 1;
        formPanel.add(firstNameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(lastNameLabel, gbc);
        gbc.gridx = 1;
        formPanel.add(lastNameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        formPanel.add(dateOfBirthLabel, gbc);
        gbc.gridx = 1;
        formPanel.add(dateOfBirthField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        formPanel.add(addressLabel, gbc);
        gbc.gridx = 1;
        formPanel.add(addressField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        formPanel.add(phoneNumberLabel, gbc);
        gbc.gridx = 1;
        formPanel.add(phoneNumberField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 5;
        formPanel.add(sexLabel, gbc);
        gbc.gridx = 1;
        formPanel.add(sexComboBox, gbc);

        gbc.gridx = 0;
        gbc.gridy = 6;
        formPanel.add(yearLabel, gbc);
        gbc.gridx = 1;
        formPanel.add(yearComboBox, gbc);

        gbc.gridx = 0;
        gbc.gridy = 7;
        formPanel.add(semesterLabel, gbc);
        gbc.gridx = 1;
        formPanel.add(semesterComboBox, gbc);

        gbc.gridx = 0;
        gbc.gridy = 8;
        formPanel.add(subjectLabel, gbc);
        gbc.gridx = 1;
        formPanel.add(subjectComboBox, gbc);

        gbc.gridx = 0;
        gbc.gridy = 9;
        formPanel.add(scoreLabel, gbc);
        gbc.gridx = 1;
        formPanel.add(scoreField, gbc);

        gbc.gridx = 1;
        gbc.gridy = 10;
        formPanel.add(updateButton, gbc);

        add(formPanel, BorderLayout.CENTER);

        fetchStudentData(); // Fetch student data from the database and populate fields
        fetchSubjects(); // Fetch subjects for the combo box

        setLocationRelativeTo(null); // Center the window
    }

    private void fetchStudentData() {
        try (Connection connection = new database().connectionDatabase();
             PreparedStatement statement = connection.prepareStatement(
                     "SELECT FirstName, LastName, DateOfBirth, Address, PhoneNumber, Sex " +
                             "FROM Students WHERE StudentID = ?")) {
            statement.setInt(1, studentID);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                firstNameField.setText(resultSet.getString("FirstName"));
                lastNameField.setText(resultSet.getString("LastName"));
                dateOfBirthField.setText(resultSet.getString("DateOfBirth"));
                addressField.setText(resultSet.getString("Address"));
                phoneNumberField.setText(resultSet.getString("PhoneNumber"));
                sexComboBox.setSelectedItem(resultSet.getString("Sex"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error fetching student data.");
        }
    }

    private void fetchSubjects() {
        List<String> subjects = new ArrayList<>();
        try (Connection connection = new database().connectionDatabase();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT SubjectName FROM Subjects")) {
            while (resultSet.next()) {
                subjects.add(resultSet.getString("SubjectName"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error fetching subjects.");
        }
        subjectComboBox.setModel(new DefaultComboBoxModel<>(subjects.toArray(new String[0])));
    }

    private void updateStudentInfo() {
        // Get input values
        String firstName = firstNameField.getText();
        String lastName = lastNameField.getText();
        String dateOfBirth = dateOfBirthField.getText();
        String address = addressField.getText();
        String phoneNumber = phoneNumberField.getText();
        String sex = (String) sexComboBox.getSelectedItem();
        int year = (int) yearComboBox.getSelectedItem();
        int semester = (int) semesterComboBox.getSelectedItem();
        String subject = (String) subjectComboBox.getSelectedItem();
        double score = Double.parseDouble(scoreField.getText());

        try (Connection connection = new database().connectionDatabase()) {
            // Update student information
            try (PreparedStatement statement = connection.prepareStatement(
                         "UPDATE Students SET FirstName = ?, LastName = ?, DateOfBirth = ?, " +
                                 "Address = ?, PhoneNumber = ?, Sex = ? WHERE StudentID = ?")) {
                statement.setString(1, firstName);
                statement.setString(2, lastName);
                statement.setString(3, dateOfBirth);
                statement.setString(4, address);
                statement.setString(5, phoneNumber);
                statement.setString(6, sex);
                statement.setInt(7, studentID);
                statement.executeUpdate();
            }

            // Update score
            int enrollmentID = getEnrollmentID(connection, studentID, year, semester);
            if (enrollmentID != -1) {
                try (PreparedStatement statement = connection.prepareStatement(
                             "UPDATE Scores SET Score = ? WHERE EnrollmentID = ?")) {
                    statement.setDouble(1, score);
                    statement.setInt(2, enrollmentID);
                    statement.executeUpdate();
                }

                // Recalculate GPA
                recalculateGPA(connection, studentID, year, semester);
            } else {
                JOptionPane.showMessageDialog(null, "Enrollment record not found for the specified year and semester.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error updating student information.");
        }
    }

    private int getEnrollmentID(Connection connection, int studentID, int year, int semester) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(
                     "SELECT EnrollmentID FROM Enrollments WHERE StudentID = ? AND Year = ? AND Semester = ?")) {
            statement.setInt(1, studentID);
            statement.setInt(2, year);
            statement.setInt(3, semester);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt("EnrollmentID");
            }
        }
        return -1;
    }

    private void recalculateGPA(Connection connection, int studentID, int year, int semester) throws SQLException {
        double totalAverageScore = 0.0;
        int totalEnrollments = 0;

        try (PreparedStatement statement = connection.prepareStatement(
                "SELECT AVG(Score) AS AverageScore FROM Scores " +
                        "JOIN Enrollments ON Scores.EnrollmentID = Enrollments.EnrollmentID " +
                        "WHERE Enrollments.StudentID = ? AND Enrollments.Year = ? AND Enrollments.Semester = ? " +
                        "GROUP BY Enrollments.EnrollmentID")) {
            statement.setInt(1, studentID);
            statement.setInt(2, year);
            statement.setInt(3, semester);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                double averageScore = resultSet.getDouble("AverageScore");
                totalAverageScore += averageScore;
                totalEnrollments++;
            }
        }

        // Calculate the GPA
        double gpa = totalEnrollments > 0 ? totalAverageScore / totalEnrollments : 0.0;

        // Update GPA in the database
        try (PreparedStatement statement = connection.prepareStatement(
                "UPDATE GPA SET GPA = ? WHERE StudentID = ? AND Year = ? AND Semester = ?")) {
            statement.setDouble(1, gpa);
            statement.setInt(2, studentID);
            statement.setInt(3, year);
            statement.setInt(4, semester);
            statement.executeUpdate();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new EditGUI(1).setVisible(true));
    }
}
