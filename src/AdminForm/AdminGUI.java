package AdminForm;

import school.db.database;
import AdminForm.OperationMethod.AddGUI;
import AdminForm.OperationMethod.UpdateGUI;
import AdminForm.OperationMethod.DeleteGUI;
import javax.swing.*;
import javax.swing.event.DocumentListener;
import javax.swing.event.DocumentEvent;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.Vector;

public class AdminGUI extends JFrame implements ActionListener {
    private JButton addButton, updateButton, deleteButton;
    private JTable table;
    private DefaultTableModel tableModel;
    private Connection connection;
    private JCheckBox currentlyStudyingCheckbox;
    private JTextField searchField;

    public AdminGUI() {
        setTitle("Admin GUI");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        initComponents();
        addComponents();
        addActionListeners();

        connectToDatabase();
        displayStudents(false);
    }

    private void initComponents() {
        addButton = new JButton("Add");
        updateButton = new JButton("Update");
        deleteButton = new JButton("Delete");
        currentlyStudyingCheckbox = new JCheckBox("Currently Studying");
        searchField = new JTextField(20);

        // Set fonts and colors for components
        Font font = new Font("Arial", Font.PLAIN, 14);
        addButton.setFont(font);
        updateButton.setFont(font);
        deleteButton.setFont(font);
        currentlyStudyingCheckbox.setFont(font);
        searchField.setFont(font);

        addButton.setBackground(new Color(60, 179, 113));
        addButton.setForeground(Color.WHITE);
        updateButton.setBackground(new Color(30, 144, 255));
        updateButton.setForeground(Color.WHITE);
        deleteButton.setBackground(new Color(220, 20, 60));
        deleteButton.setForeground(Color.WHITE);
    }

    private void addComponents() {
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.LINE_AXIS));
        buttonPanel.add(addButton);
        buttonPanel.add(Box.createRigidArea(new Dimension(10, 0)));
        buttonPanel.add(updateButton);
        buttonPanel.add(Box.createRigidArea(new Dimension(10, 0)));
        buttonPanel.add(deleteButton);
        buttonPanel.add(Box.createRigidArea(new Dimension(10, 0)));
        buttonPanel.add(currentlyStudyingCheckbox);

        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchPanel.add(new JLabel("Search:"));
        searchPanel.add(searchField);

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(searchPanel, BorderLayout.NORTH);
        topPanel.add(buttonPanel, BorderLayout.CENTER);
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        tableModel = new DefaultTableModel();
        table = new JTable(tableModel);
        table.setFillsViewportHeight(true);

        JScrollPane scrollPane = new JScrollPane(table);

        setLayout(new BorderLayout());
        add(topPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
    }

    private void addActionListeners() {
        addButton.addActionListener(this);
        updateButton.addActionListener(this);
        deleteButton.addActionListener(this);
        currentlyStudyingCheckbox.addActionListener(this);

        searchField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                performSearch();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                performSearch();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                performSearch();
            }
        });
    }

    private void connectToDatabase() {
        database db = new database();
        connection = db.connectionDatabase();
    }

    private void displayStudents(boolean onlyCurrentlyStudying) {
        try {
            Statement stmt = connection.createStatement();
            String query = "SELECT s.StudentID, CONCAT(s.FirstName, ' ', s.LastName) AS FullName, s.DateOfBirth, " +
                    "s.Address, s.PhoneNumber, s.Sex, " +
                    "AVG(sc.Score) AS AverageScore, " +
                    "AVG(g.GPA) AS AverageGPA " +
                    "FROM Students s " +
                    "LEFT JOIN Enrollments e ON s.StudentID = e.StudentID " +
                    "LEFT JOIN Subjects sb ON e.SubjectID = sb.SubjectID " +
                    "LEFT JOIN Scores sc ON e.EnrollmentID = sc.EnrollmentID " +
                    "LEFT JOIN GPA g ON s.StudentID = g.StudentID AND e.Year = g.Year AND e.Semester = g.Semester ";

            if (onlyCurrentlyStudying) {
                query += "WHERE e.Year IS NOT NULL ";
            }

            query += "GROUP BY s.StudentID, s.FirstName, s.LastName, s.DateOfBirth, " +
                    "s.Address, s.PhoneNumber, s.Sex";

            ResultSet rs = stmt.executeQuery(query);
            ResultSetMetaData metaData = rs.getMetaData();

            tableModel.setRowCount(0);

            int columnCount = metaData.getColumnCount();
            Vector<String> columnNames = new Vector<>();
            for (int i = 1; i <= columnCount; i++) {
                columnNames.add(metaData.getColumnName(i));
            }
            tableModel.setColumnIdentifiers(columnNames);

            while (rs.next()) {
                Vector<Object> rowData = new Vector<>();
                for (int i = 1; i <= columnCount; i++) {
                    rowData.add(rs.getObject(i));
                }
                tableModel.addRow(rowData);
            }

            rs.close();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void performSearch() {
        String searchText = searchField.getText();
        searchStudents(searchText);
    }

    private void searchStudents(String searchText) {
        try {
            String query = "SELECT s.StudentID, CONCAT(s.FirstName, ' ', s.LastName) AS FullName, s.DateOfBirth, " +
                    "s.Address, s.PhoneNumber, s.Sex, " +
                    "e.Year, e.Semester, " +
                    "sb.SubjectName, sc.Score, " +
                    "g.GPA " +
                    "FROM Students s " +
                    "LEFT JOIN Enrollments e ON s.StudentID = e.StudentID " +
                    "LEFT JOIN Subjects sb ON e.SubjectID = sb.SubjectID " +
                    "LEFT JOIN Scores sc ON e.EnrollmentID = sc.EnrollmentID " +
                    "LEFT JOIN GPA g ON s.StudentID = g.StudentID AND e.Year = g.Year AND e.Semester = g.Semester " +
                    "WHERE " +
                    "s.StudentID LIKE ?";
            PreparedStatement pstmt = connection.prepareStatement(query);
            String searchPattern = "%" + searchText + "%";
            pstmt.setString(1, searchPattern);
            ResultSet rs = pstmt.executeQuery();
            ResultSetMetaData metaData = rs.getMetaData();

            tableModel.setRowCount(0);

            int columnCount = metaData.getColumnCount();
            Vector<String> columnNames = new Vector<>();
            for (int i = 1; i <= columnCount; i++) {
                columnNames.add(metaData.getColumnName(i));
            }
            tableModel.setColumnIdentifiers(columnNames);

            while (rs.next()) {
                Vector<Object> rowData = new Vector<>();
                for (int i = 1; i <= columnCount; i++) {
                    rowData.add(rs.getObject(i));
                }
                tableModel.addRow(rowData);
            }

            rs.close();
            pstmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {	
        if (e.getSource() == addButton) {
            AddGUI addGUI = new AddGUI(connection, this);
            addGUI.setVisible(true);
        } else if (e.getSource() == updateButton) {
            UpdateGUI updateGUI = new UpdateGUI();
            updateGUI.setVisible(true);
        } else if (e.getSource() == deleteButton) {
            DeleteGUI deleteGUI = new DeleteGUI(this);
            deleteGUI.setVisible(true);
            dispose();
        } else if (e.getSource() == currentlyStudyingCheckbox) {
            boolean onlyCurrentlyStudying = currentlyStudyingCheckbox.isSelected();
            displayStudents(onlyCurrentlyStudying);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new AdminGUI().setVisible(true));
    }
}
