import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.*;

class Student {
    String id;
    String name;
    String className;
    ArrayList<Subject> subjects;
    
    public Student(String id, String name, String className) {
        this.id = id;
        this.name = name;
        this.className = className;
        this.subjects = new ArrayList<>();
    }
    
    public void addSubject(String subjectName, double marks) {
        subjects.add(new Subject(subjectName, marks));
    }
    
    public double calculateAverage() {
        if (subjects.isEmpty()) return 0;
        double total = 0;
        for (Subject s : subjects) {
            total += s.marks;
        }
        return total / subjects.size();
    }
    
    public String getGrade() {
        double avg = calculateAverage();
        if (avg >= 90) return "A+";
        else if (avg >= 80) return "A";
        else if (avg >= 70) return "B";
        else if (avg >= 60) return "C";
        else if (avg >= 50) return "D";
        else return "F";
    }
}

class Subject {
    String name;
    double marks;
    
    public Subject(String name, double marks) {
        this.name = name;
        this.marks = marks;
    }
}

public class StudentGradeManagementSystem extends JFrame {
    private ArrayList<Student> students = new ArrayList<>();
    private JTable studentTable;
    private DefaultTableModel tableModel;
    
    public StudentGradeManagementSystem() {
        setTitle("Student Grade Management System");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        initComponents();
    }
    
    private void initComponents() {
        // Main Panel
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        // Title
        JLabel titleLabel = new JLabel("Student Grade Management System", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(new Color(0, 102, 204));
        mainPanel.add(titleLabel, BorderLayout.NORTH);
        
        // Table
        String[] columns = {"Student ID", "Name", "Class", "Average", "Grade"};
        tableModel = new DefaultTableModel(columns, 0);
        studentTable = new JTable(tableModel);
        studentTable.setRowHeight(25);
        studentTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        JScrollPane scrollPane = new JScrollPane(studentTable);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        
        // Button Panel
        JPanel buttonPanel = new JPanel(new GridLayout(2, 3, 10, 10));
        
        JButton addStudentBtn = createButton("Add Student", new Color(34, 139, 34));
        JButton addGradesBtn = createButton("Add Grades", new Color(30, 144, 255));
        JButton viewReportBtn = createButton("View Report", new Color(255, 140, 0));
        JButton searchBtn = createButton("Search Student", new Color(138, 43, 226));
        JButton deleteBtn = createButton("Delete Student", new Color(220, 20, 60));
        JButton exitBtn = createButton("Exit", new Color(105, 105, 105));
        
        addStudentBtn.addActionListener(e -> addStudent());
        addGradesBtn.addActionListener(e -> addGrades());
        viewReportBtn.addActionListener(e -> viewReport());
        searchBtn.addActionListener(e -> searchStudent());
        deleteBtn.addActionListener(e -> deleteStudent());
        exitBtn.addActionListener(e -> System.exit(0));
        
        buttonPanel.add(addStudentBtn);
        buttonPanel.add(addGradesBtn);
        buttonPanel.add(viewReportBtn);
        buttonPanel.add(searchBtn);
        buttonPanel.add(deleteBtn);
        buttonPanel.add(exitBtn);
        
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        add(mainPanel);
    }
    
    private JButton createButton(String text, Color color) {
        JButton button = new JButton(text);
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Arial", Font.BOLD, 12));
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
    }
    
    private void addStudent() {
        JTextField idField = new JTextField(15);
        JTextField nameField = new JTextField(15);
        JTextField classField = new JTextField(15);
        
        JPanel panel = new JPanel(new GridLayout(3, 2, 10, 10));
        panel.add(new JLabel("Student ID:"));
        panel.add(idField);
        panel.add(new JLabel("Name:"));
        panel.add(nameField);
        panel.add(new JLabel("Class:"));
        panel.add(classField);
        
        int result = JOptionPane.showConfirmDialog(this, panel, "Add Student", 
            JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        
        if (result == JOptionPane.OK_OPTION) {
            String id = idField.getText().trim();
            String name = nameField.getText().trim();
            String className = classField.getText().trim();
            
            if (id.isEmpty() || name.isEmpty() || className.isEmpty()) {
                JOptionPane.showMessageDialog(this, "All fields are required!", 
                    "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            for (Student s : students) {
                if (s.id.equals(id)) {
                    JOptionPane.showMessageDialog(this, "Student ID already exists!", 
                        "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }
            
            Student student = new Student(id, name, className);
            students.add(student);
            refreshTable();
            JOptionPane.showMessageDialog(this, "Student added successfully!", 
                "Success", JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    private void addGrades() {
        String studentId = JOptionPane.showInputDialog(this, "Enter Student ID:");
        if (studentId == null || studentId.trim().isEmpty()) return;
        
        Student student = findStudent(studentId.trim());
        if (student == null) {
            JOptionPane.showMessageDialog(this, "Student not found!", 
                "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        String numStr = JOptionPane.showInputDialog(this, "Enter number of subjects:");
        if (numStr == null) return;
        
        try {
            int numSubjects = Integer.parseInt(numStr);
            
            for (int i = 0; i < numSubjects; i++) {
                JTextField subjectField = new JTextField(15);
                JTextField marksField = new JTextField(15);
                
                JPanel panel = new JPanel(new GridLayout(2, 2, 10, 10));
                panel.add(new JLabel("Subject Name:"));
                panel.add(subjectField);
                panel.add(new JLabel("Marks (0-100):"));
                panel.add(marksField);
                
                int result = JOptionPane.showConfirmDialog(this, panel, 
                    "Add Subject " + (i + 1), JOptionPane.OK_CANCEL_OPTION);
                
                if (result == JOptionPane.OK_OPTION) {
                    String subject = subjectField.getText().trim();
                    double marks = Double.parseDouble(marksField.getText().trim());
                    
                    if (marks < 0 || marks > 100) {
                        JOptionPane.showMessageDialog(this, 
                            "Marks must be between 0-100!", "Error", 
                            JOptionPane.ERROR_MESSAGE);
                        i--;
                        continue;
                    }
                    
                    student.addSubject(subject, marks);
                }
            }
            
            refreshTable();
            JOptionPane.showMessageDialog(this, "Grades added successfully!", 
                "Success", JOptionPane.INFORMATION_MESSAGE);
                
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid input!", 
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void viewReport() {
        int selectedRow = studentTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a student from the table!", 
                "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        String studentId = (String) tableModel.getValueAt(selectedRow, 0);
        Student student = findStudent(studentId);
        
        if (student == null) return;
        
        StringBuilder report = new StringBuilder();
        report.append("========== REPORT CARD ==========\n\n");
        report.append("Student ID: ").append(student.id).append("\n");
        report.append("Name: ").append(student.name).append("\n");
        report.append("Class: ").append(student.className).append("\n\n");
        report.append("Subjects:\n");
        report.append("--------------------------------\n");
        
        for (Subject s : student.subjects) {
            report.append(String.format("%-20s: %.2f\n", s.name, s.marks));
        }
        
        report.append("--------------------------------\n");
        report.append(String.format("Average: %.2f\n", student.calculateAverage()));
        report.append("Grade: ").append(student.getGrade()).append("\n");
        report.append("=================================");
        
        JTextArea textArea = new JTextArea(report.toString());
        textArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        textArea.setEditable(false);
        
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(400, 300));
        
        JOptionPane.showMessageDialog(this, scrollPane, "Report Card", 
            JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void searchStudent() {
        String studentId = JOptionPane.showInputDialog(this, "Enter Student ID:");
        if (studentId == null || studentId.trim().isEmpty()) return;
        
        Student student = findStudent(studentId.trim());
        if (student == null) {
            JOptionPane.showMessageDialog(this, "Student not found!", 
                "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        String info = String.format(
            "Student ID: %s\nName: %s\nClass: %s\nAverage: %.2f\nGrade: %s",
            student.id, student.name, student.className, 
            student.calculateAverage(), student.getGrade()
        );
        
        JOptionPane.showMessageDialog(this, info, "Student Information", 
            JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void deleteStudent() {
        int selectedRow = studentTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a student from the table!", 
                "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        int confirm = JOptionPane.showConfirmDialog(this, 
            "Are you sure you want to delete this student?", 
            "Confirm Delete", JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            String studentId = (String) tableModel.getValueAt(selectedRow, 0);
            students.removeIf(s -> s.id.equals(studentId));
            refreshTable();
            JOptionPane.showMessageDialog(this, "Student deleted successfully!", 
                "Success", JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    private void refreshTable() {
        tableModel.setRowCount(0);
        for (Student s : students) {
            Object[] row = {
                s.id,
                s.name,
                s.className,
                String.format("%.2f", s.calculateAverage()),
                s.getGrade()
            };
            tableModel.addRow(row);
        }
    }
    
    private Student findStudent(String id) {
        for (Student s : students) {
            if (s.id.equals(id)) return s;
        }
        return null;
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new StudentGradeManagementSystem().setVisible(true);
        });
    }
}