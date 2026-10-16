
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import javax.swing.text.AbstractDocument;
import javax.swing.text.DocumentFilter;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;

public class StudentResultApp {

    static final Color NAVY = new Color(25, 42, 86);
    static final Color BLUE = new Color(55, 105, 220);
    static final Color BACKGROUND = new Color(245, 247, 252);
    static final Color GREEN = new Color(22, 145, 95);
    static final Color RED = new Color(220, 65, 75);
    static final Color TEXT = new Color(45, 55, 75);

    JFrame frame;
    CardLayout cardLayout;
    JPanel cards;

    JTextField nameField;
    JTextField[] markFields = new JTextField[7];

    JLabel resultName;
    JLabel gradeLabel;
    JLabel totalLabel;
    JLabel averageLabel;
    JLabel overallLabel;
    JLabel highestLabel;
    JLabel[] subjectResults = new JLabel[7];

    String[] subjects = {
        "Subject 1",
        "Subject 2",
        "Subject 3",
        "Subject 4",
        "Subject 5",
        "Subject 6",
        "Subject 7"
    };

    // Example credits supplied for this project. Verify these against the official syllabus.
    double[] credits = {3, 4, 4, 3, 4, 3, 2};

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new StudentResultApp().createApp());
    }

    void createApp() {
        frame = new JFrame("Student Result Dashboard");
        frame.setSize(760, 850);
        frame.setMinimumSize(new Dimension(650, 700));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);

        cardLayout = new CardLayout();
        cards = new JPanel(cardLayout);

        cards.add(createInputPage(), "INPUT");
        cards.add(createResultPage(), "RESULT");

        frame.setContentPane(cards);
        cardLayout.show(cards, "INPUT");
        frame.setVisible(true);

        nameField.requestFocusInWindow();
    }

    // INPUT PAGE
    JPanel createInputPage() {
        JPanel page = new JPanel(new BorderLayout());
        page.setBackground(BACKGROUND);

        JPanel header = new JPanel();
        header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));
        header.setBackground(NAVY);
        header.setBorder(new EmptyBorder(22, 30, 22, 30));

        JLabel title = new JLabel("STUDENT RESULT");
        title.setFont(new Font("SansSerif", Font.BOLD, 28));
        title.setForeground(Color.WHITE);

        JLabel subtitle = new JLabel("ACADEMIC PERFORMANCE DASHBOARD");
        subtitle.setFont(new Font("SansSerif", Font.PLAIN, 12));
        subtitle.setForeground(new Color(205, 216, 245));

        header.add(title);
        header.add(Box.createVerticalStrut(6));
        header.add(subtitle);
        page.add(header, BorderLayout.NORTH);

        JPanel form = new JPanel();
        form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));
        form.setBackground(BACKGROUND);
        form.setBorder(new EmptyBorder(22, 30, 25, 30));

        JLabel studentTitle = sectionTitle("STUDENT INFORMATION");
        form.add(studentTitle);
        form.add(Box.createVerticalStrut(10));

        JPanel nameCard = new JPanel(new BorderLayout(15, 0));
        nameCard.setBackground(Color.WHITE);
        nameCard.setBorder(new EmptyBorder(12, 15, 12, 15));
        nameCard.setMaximumSize(new Dimension(Integer.MAX_VALUE, 65));

        JLabel nameLabel = new JLabel("Student Name");
        nameLabel.setFont(new Font("SansSerif", Font.BOLD, 14));

        nameField = new JTextField();

((AbstractDocument) nameField.getDocument())
        .setDocumentFilter(new UppercaseFilter());

styleInput(nameField);

        nameCard.add(nameLabel, BorderLayout.WEST);
        nameCard.add(nameField, BorderLayout.CENTER);
        form.add(nameCard);

        form.add(Box.createVerticalStrut(22));
        form.add(sectionTitle("ENTER SUBJECT MARKS"));
        form.add(Box.createVerticalStrut(12));

        JPanel subjectGrid = new JPanel(new GridLayout(0, 2, 12, 12));
        subjectGrid.setOpaque(false);

        for (int i = 0; i < subjects.length; i++) {
            markFields[i] = new JTextField();
            styleInput(markFields[i]);

            JPanel subjectCard = new JPanel(new BorderLayout(0, 8));
            subjectCard.setBackground(Color.WHITE);
            subjectCard.setBorder(new EmptyBorder(12, 14, 12, 14));

            JLabel subjectLabel = new JLabel(subjects[i]);
            subjectLabel.setFont(new Font("SansSerif", Font.BOLD, 13));
            subjectLabel.setForeground(TEXT);

            subjectCard.add(subjectLabel, BorderLayout.NORTH);
            subjectCard.add(markFields[i], BorderLayout.CENTER);

            subjectGrid.add(subjectCard);
        }

        form.add(subjectGrid);
        form.add(Box.createVerticalStrut(20));

        JButton calculateButton = new JButton("CALCULATE RESULT  →");
        styleButton(calculateButton, BLUE);
        calculateButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        calculateButton.setMaximumSize(new Dimension(300, 45));
        calculateButton.setPreferredSize(new Dimension(300, 45));
        calculateButton.addActionListener(e -> calculateResult());

        form.add(calculateButton);
        form.add(Box.createVerticalStrut(10));

        JLabel note = new JLabel("Enter marks from 0 to 100");
        note.setFont(new Font("SansSerif", Font.PLAIN, 12));
        note.setForeground(Color.GRAY);
        note.setAlignmentX(Component.CENTER_ALIGNMENT);
        form.add(note);

        JScrollPane scroll = new JScrollPane(form);
        scroll.setBorder(null);
        scroll.getViewport().setBackground(BACKGROUND);
        scroll.getVerticalScrollBar().setUnitIncrement(16);

        page.add(scroll, BorderLayout.CENTER);

        // Enter key moves to the next box
        nameField.addActionListener(e -> markFields[0].requestFocusInWindow());

        for (int i = 0; i < markFields.length - 1; i++) {
            final int next = i + 1;
            markFields[i].addActionListener(
                e -> markFields[next].requestFocusInWindow()
            );
        }

        markFields[6].addActionListener(e -> calculateButton.doClick());

        return page;
    }

    // RESULT PAGE
    JPanel createResultPage() {
        JPanel page = new JPanel(new BorderLayout());
        page.setBackground(BACKGROUND);

        JPanel header = new JPanel();
        header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));
        header.setBackground(NAVY);
        header.setBorder(new EmptyBorder(20, 30, 20, 30));

        JLabel title = new JLabel("YOUR RESULT");
        title.setFont(new Font("SansSerif", Font.BOLD, 27));
        title.setForeground(Color.WHITE);

        JLabel subtitle = new JLabel("ACADEMIC PERFORMANCE SUMMARY");
        subtitle.setForeground(new Color(205, 216, 245));

        header.add(title);
        header.add(Box.createVerticalStrut(5));
        header.add(subtitle);
        page.add(header, BorderLayout.NORTH);

        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBackground(BACKGROUND);
        content.setBorder(new EmptyBorder(20, 30, 20, 30));

        resultName = new JLabel();
        resultName.setFont(new Font("SansSerif", Font.BOLD, 23));
        resultName.setForeground(NAVY);
        resultName.setAlignmentX(Component.CENTER_ALIGNMENT);
        content.add(resultName);

        content.add(Box.createVerticalStrut(15));

        JPanel gradeCard = new JPanel();
        gradeCard.setLayout(new BoxLayout(gradeCard, BoxLayout.Y_AXIS));
        gradeCard.setBackground(Color.WHITE);
        gradeCard.setBorder(new EmptyBorder(12, 15, 12, 15));
        gradeCard.setMaximumSize(new Dimension(Integer.MAX_VALUE, 90));

        JLabel gradeTitle = new JLabel("SEMESTER GPA");
        gradeTitle.setForeground(Color.GRAY);
        gradeTitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        gradeLabel = new JLabel();
        gradeLabel.setFont(new Font("SansSerif", Font.BOLD, 28));
        gradeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        gradeCard.add(gradeTitle);
        gradeCard.add(Box.createVerticalStrut(5));
        gradeCard.add(gradeLabel);
        content.add(gradeCard);

        content.add(Box.createVerticalStrut(12));

        JPanel stats = new JPanel(new GridLayout(1, 2, 12, 0));
        stats.setOpaque(false);

        totalLabel = new JLabel();
        averageLabel = new JLabel();

        stats.add(statCard("TOTAL MARKS", totalLabel));
        stats.add(statCard("AVERAGE", averageLabel));
        content.add(stats);

        content.add(Box.createVerticalStrut(16));
        content.add(sectionTitle("SUBJECT-WISE RESULTS"));
        content.add(Box.createVerticalStrut(10));

        JPanel resultsGrid = new JPanel(new GridLayout(0, 2, 10, 10));
        resultsGrid.setOpaque(false);

        for (int i = 0; i < subjects.length; i++) {
            subjectResults[i] = new JLabel();

            JPanel resultCard = new JPanel(new BorderLayout(0, 8));
            resultCard.setBackground(Color.WHITE);
            resultCard.setBorder(new EmptyBorder(10, 12, 10, 12));

            JLabel subject = new JLabel(subjects[i]);
            subject.setFont(new Font("SansSerif", Font.BOLD, 12));

            resultCard.add(subject, BorderLayout.NORTH);
            resultCard.add(subjectResults[i], BorderLayout.CENTER);

            resultsGrid.add(resultCard);
        }

        content.add(resultsGrid);
        content.add(Box.createVerticalStrut(14));

        overallLabel = new JLabel();
        overallLabel.setFont(new Font("SansSerif", Font.BOLD, 17));
        overallLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        content.add(overallLabel);

        content.add(Box.createVerticalStrut(10));

        highestLabel = new JLabel();
        highestLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        highestLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        content.add(highestLabel);

        content.add(Box.createVerticalStrut(8));
        JLabel cgpaNote = new JLabel("CGPA requires results and credits from all semesters.");
        cgpaNote.setFont(new Font("SansSerif", Font.ITALIC, 12));
        cgpaNote.setForeground(Color.GRAY);
        cgpaNote.setAlignmentX(Component.CENTER_ALIGNMENT);
        content.add(cgpaNote);

        content.add(Box.createVerticalStrut(16));

        JButton backButton = new JButton("← BACK TO INPUT");
        styleButton(backButton, NAVY);
        backButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        backButton.setMaximumSize(new Dimension(240, 43));
        backButton.addActionListener(e -> {
            cardLayout.show(cards, "INPUT");
            nameField.requestFocusInWindow();
        });

        content.add(backButton);

        JScrollPane scroll = new JScrollPane(content);
        scroll.setBorder(null);
        scroll.getVerticalScrollBar().setUnitIncrement(16);

        page.add(scroll, BorderLayout.CENTER);
        return page;
    }

    JLabel sectionTitle(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("SansSerif", Font.BOLD, 13));
        label.setForeground(NAVY);
        return label;
    }

    JPanel statCard(String title, JLabel value) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(Color.WHITE);
        card.setBorder(new EmptyBorder(12, 10, 12, 10));

        JLabel heading = new JLabel(title);
        heading.setForeground(Color.GRAY);
        heading.setAlignmentX(Component.CENTER_ALIGNMENT);

        value.setFont(new Font("SansSerif", Font.BOLD, 22));
        value.setForeground(BLUE);
        value.setAlignmentX(Component.CENTER_ALIGNMENT);

        card.add(heading);
        card.add(Box.createVerticalStrut(7));
        card.add(value);
        return card;
    }

    void styleInput(JTextField field) {
        field.setFont(new Font("SansSerif", Font.PLAIN, 15));
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 226, 240)),
            new EmptyBorder(8, 10, 8, 10)
        ));
    }

    void styleButton(JButton button, Color color) {
        button.setFont(new Font("SansSerif", Font.BOLD, 13));
        button.setForeground(Color.WHITE);
        button.setBackground(color);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setOpaque(true);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    // CALCULATE R2025 grades and credit-weighted semester GPA
    void calculateResult() {
        String name = nameField.getText().trim();

        if (name.isEmpty()) {
            showError("Please enter the student name.", nameField);
            return;
        }

        int[] marks = new int[7];
        for (int i = 0; i < markFields.length; i++) {
            try {
                marks[i] = Integer.parseInt(markFields[i].getText().trim());
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(
                    frame,
                    "Enter a valid whole-number mark for " + subjects[i] + ".",
                    "Invalid Input",
                    JOptionPane.ERROR_MESSAGE
                );
                markFields[i].requestFocusInWindow();
                return;
            }

            if (marks[i] < 0 || marks[i] > 100) {
                JOptionPane.showMessageDialog(
                    frame,
                    subjects[i] + " mark must be between 0 and 100.",
                    "Invalid Mark",
                    JOptionPane.ERROR_MESSAGE
                );
                markFields[i].requestFocusInWindow();
                return;
            }
        }

        int total = 0;
        int highest = marks[0];
        String highestSubject = subjects[0];
        boolean passed = true;
        double weightedGradePoints = 0.0;
        double totalCredits = 0.0;

        for (int i = 0; i < marks.length; i++) {
            int mark = marks[i];
            total += mark;
            if (mark < 50) passed = false;
            if (mark > highest) {
                highest = mark;
                highestSubject = subjects[i];
            }

            String letterGrade = getLetterGrade(mark);
            double gradePoint = getGradePoint(mark);
            weightedGradePoints += credits[i] * gradePoint;
            totalCredits += credits[i];

            String status = mark >= 50 ? "PASS" : "FAIL";
            String detail = String.format(
                "%s  |  GP: %s  |  %d/100  |  %s credits  |  %s",
                letterGrade,
                gradePoint == 6.5 ? "6.5" : String.format("%.0f", gradePoint),
                mark,
                credits[i] == Math.floor(credits[i]) ? String.format("%.0f", credits[i]) : credits[i],
                status
            );
            subjectResults[i].setText(detail);
            subjectResults[i].setForeground(mark >= 50 ? GREEN : RED);
        }

        double average = total / 7.0;
        double semesterGpa = weightedGradePoints / totalCredits;

        resultName.setText(name);
        gradeLabel.setText(String.format("%.2f / 10.00", semesterGpa));
        gradeLabel.setForeground(passed ? GREEN : RED);
        totalLabel.setText(total + " / 700");
        averageLabel.setText(String.format("%.2f%%", average));
        overallLabel.setText(passed ? "✓ ALL SUBJECTS CLEARED" : "✕ REAPPEARANCE REQUIRED");
        overallLabel.setForeground(passed ? GREEN : RED);
        highestLabel.setText("★ Highest Mark: " + highestSubject + " — " + highest);

        cardLayout.show(cards, "RESULT");
    }

    // R2025 Regulation grade bands supplied by the user.
    double getGradePoint(int mark) {
        if (mark >= 91) return 10.0; // S
        if (mark >= 81) return 9.0;  // A+
        if (mark >= 71) return 8.0;  // A
        if (mark >= 66) return 7.0;  // B+
        if (mark >= 61) return 6.5;  // B
        if (mark >= 56) return 6.0;  // C+
        if (mark >= 50) return 5.0;  // C
        return 0.0;                  // U
    }

    String getLetterGrade(int mark) {
        if (mark >= 91) return "S";
        if (mark >= 81) return "A+";
        if (mark >= 71) return "A";
        if (mark >= 66) return "B+";
        if (mark >= 61) return "B";
        if (mark >= 56) return "C+";
        if (mark >= 50) return "C";
        return "U";
    }

    void showError(String message, JTextField field) {
        JOptionPane.showMessageDialog(
            frame,
            message,
            "Missing Information",
            JOptionPane.WARNING_MESSAGE
        );
        field.requestFocusInWindow();
    }
    static class UppercaseFilter extends DocumentFilter {
    @Override
    public void insertString(
            FilterBypass fb, int offset, String text,
            AttributeSet attr) throws BadLocationException {
        if (text != null) {
            super.insertString(fb, offset, text.toUpperCase(), attr);
        }
    }

    @Override
    public void replace(
            FilterBypass fb, int offset, int length,
            String text, AttributeSet attrs)
            throws BadLocationException {
        if (text != null) {
            text = text.toUpperCase();
        }
        super.replace(fb, offset, length, text, attrs);
    }
}
}
