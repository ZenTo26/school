package SearchForm;
import school.db.database;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import LoginForm.LoginGUI;
public class SearchForm {
    private JFrame frame;
    private JTextField studentIdField;
    private JPanel resultPanel;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                SearchForm window = new SearchForm();
                window.frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public SearchForm() {
        initialize();
    }

    private void initialize() {
        frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setTitle("Student Search");
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setLayout(new BorderLayout());

        JPanel searchPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(10, 10, 10, 10);
        searchPanel.add(new JLabel("Student ID:"), gbc);

        studentIdField = new JTextField(20);
        gbc.gridx++;
        searchPanel.add(studentIdField, gbc);

        JButton btnSearch = new JButton("Search");
        btnSearch.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                searchStudent();
            }
        });
        gbc.gridx++;
        searchPanel.add(btnSearch, gbc);

        JButton btnLogin = new JButton("Login");
        btnLogin.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                openLoginGUI();
            }
        });
        gbc.gridx++;
        searchPanel.add(btnLogin, gbc);

        frame.add(searchPanel, BorderLayout.NORTH);

        resultPanel = new JPanel();
        resultPanel.setLayout(new BoxLayout(resultPanel, BoxLayout.Y_AXIS));
        JScrollPane scrollPane = new JScrollPane(resultPanel);
        frame.add(scrollPane, BorderLayout.CENTER);
    }

    private void searchStudent() {
        int studentId = Integer.parseInt(studentIdField.getText());
        database db = new database();
        try (Connection connection = db.connectionDatabase()) {
            String studentQuery = "SELECT * FROM Students WHERE StudentID = ?";
            PreparedStatement studentStatement = connection.prepareStatement(studentQuery);
            studentStatement.setInt(1, studentId);
            ResultSet studentResultSet = studentStatement.executeQuery();

            if (studentResultSet.next()) {
                String[] columnNames = {"Attribute", "Value"};
                Object[][] data = {
                        {"Student ID", studentResultSet.getInt("StudentID")},
                        {"First Name", studentResultSet.getString("FirstName")},
                        {"Last Name", studentResultSet.getString("LastName")},
                        {"Date of Birth", studentResultSet.getDate("DateOfBirth")},
                        {"Address", studentResultSet.getString("Address")},
                        {"Phone Number", studentResultSet.getString("PhoneNumber")},
                        {"Sex", studentResultSet.getString("Sex")},
                        {"Age", studentResultSet.getInt("Age")}
                };

                DefaultTableModel model = new DefaultTableModel(data, columnNames);
                JTable table = new JTable(model);

                String gpaQuery = "SELECT Year, Semester, GPA FROM GPA WHERE StudentID = ?";
                PreparedStatement gpaStatement = connection.prepareStatement(gpaQuery);
                gpaStatement.setInt(1, studentId);
                ResultSet gpaResultSet = gpaStatement.executeQuery();

                Object[] gpaColumnNames = {"Year", "Semester", "GPA"};
                DefaultTableModel gpaModel = new DefaultTableModel(gpaColumnNames, 0);

                while (gpaResultSet.next()) {
                    Object[] rowData = {
                            gpaResultSet.getInt("Year"),
                            gpaResultSet.getInt("Semester"),
                            gpaResultSet.getBigDecimal("GPA")
                    };
                    gpaModel.addRow(rowData);
                }

                JTable gpaTable = new JTable(gpaModel);

                // Align content in "Year", "Semester", and "GPA" columns to center
                DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
                centerRenderer.setHorizontalAlignment(JLabel.CENTER);
                gpaTable.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
                gpaTable.getColumnModel().getColumn(1).setCellRenderer(centerRenderer);
                gpaTable.getColumnModel().getColumn(2).setCellRenderer(centerRenderer);

                resultPanel.removeAll();
                resultPanel.add(new JScrollPane(table));
                resultPanel.add(new JScrollPane(gpaTable));
                frame.revalidate();
            } else {
                resultPanel.removeAll();
                resultPanel.add(new JLabel("Student not found."));
                frame.revalidate();
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            resultPanel.removeAll();
            resultPanel.add(new JLabel("Error fetching student details."));
            frame.revalidate();
        }
    }
    private void openLoginGUI() {
        database db = new database();
        Connection connection = db.connectionDatabase();

        if (connection != null) {
            LoginGUI loginGUI = new LoginGUI(connection);
            loginGUI.setVisible(true);
            frame.dispose(); 
        } else {
            JOptionPane.showMessageDialog(frame, "Failed to connect to the database.");
        }
    }
}
