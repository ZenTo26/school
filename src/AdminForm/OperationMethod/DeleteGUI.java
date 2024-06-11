package AdminForm.OperationMethod;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import AdminForm.AdminGUI;
import school.db.database;

public class DeleteGUI extends JFrame implements ActionListener {
    private JTextField idField;
    private JButton deleteButton;
    private database db;
    private JFrame parentFrame;

    public DeleteGUI(JFrame parentFrame) {
        this.parentFrame = parentFrame;
        setTitle("Delete Student");
        setSize(300, 150);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(parentFrame);

        JPanel panel = new JPanel();
        add(panel);
        placeComponents(panel);

        db = new database();
    }
    private void placeComponents(JPanel panel) {
        panel.setLayout(null);

        JLabel idLabel = new JLabel("Student ID:");
        idLabel.setBounds(10, 20, 80, 25);
        panel.add(idLabel);

        idField = new JTextField(20);
        idField.setBounds(100, 20, 160, 25);
        panel.add(idField);

        deleteButton = new JButton("Delete");
        deleteButton.setBounds(100, 50, 80, 25);
        deleteButton.addActionListener(this);
        panel.add(deleteButton);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == deleteButton) {
            String id = idField.getText();
            if (!id.isEmpty()) {
                if (deleteStudent(id)) {
                    JOptionPane.showMessageDialog(this, "Student deleted successfully.");
                    idField.setText("");
                    parentFrame.setVisible(true); // Show the parent frame (AdminGUI)
                    dispose(); // Close the current window (DeleteGUI)
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to delete student.");
                }
            } else {
                JOptionPane.showMessageDialog(this, "Please enter a valid ID.");
            }
        }
    }

    private boolean deleteStudent(String id) {
        try (Connection connection = db.connectionDatabase();
             PreparedStatement deleteGPAStatement = connection.prepareStatement("DELETE FROM GPA WHERE StudentID = ?");
             PreparedStatement deleteScoreStatement = connection.prepareStatement("DELETE FROM Scores WHERE EnrollmentID IN (SELECT EnrollmentID FROM Enrollments WHERE StudentID = ?)");
             PreparedStatement deleteEnrollmentStatement = connection.prepareStatement("DELETE FROM Enrollments WHERE StudentID = ?");
             PreparedStatement deleteStudentStatement = connection.prepareStatement("DELETE FROM Students WHERE StudentID = ?")) {

            if (connection != null) {
                // Start transaction
                connection.setAutoCommit(false);

                // Delete student's GPA records
                deleteGPAStatement.setString(1, id);
                deleteGPAStatement.executeUpdate();

                // Delete student's score records
                deleteScoreStatement.setString(1, id);
                deleteScoreStatement.executeUpdate();

                // Delete student's enrollments
                deleteEnrollmentStatement.setString(1, id);
                deleteEnrollmentStatement.executeUpdate();

                // Delete student
                deleteStudentStatement.setString(1, id);
                int rowsDeleted = deleteStudentStatement.executeUpdate();

                // Commit transaction
                connection.commit();

                return rowsDeleted > 0;
            } else {
                JOptionPane.showMessageDialog(this, "Failed to connect to the database.");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error deleting student from database: " + ex.getMessage());
        }
        return false;
    }
}
