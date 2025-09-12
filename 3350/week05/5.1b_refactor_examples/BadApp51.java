import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.List;
import java.util.ArrayList;

public class BadApp51 extends JFrame {
    private JTextField nameField;
    private JTextArea displayArea;
    private JButton saveButton, loadButton, calcButton, analyzeButton;
    private List<String> names;

    public BadApp51() {
        names = new ArrayList<>();

        setTitle("Bad App Example");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 300);
        setLayout(new BorderLayout());

        JPanel topPanel = new JPanel();
        nameField = new JTextField(20);
        topPanel.add(nameField);

        saveButton = new JButton("Save");
        loadButton = new JButton("Load");
        calcButton = new JButton("Calc Lengths");
        analyzeButton = new JButton("Analyze Names");

        topPanel.add(saveButton);
        topPanel.add(loadButton);
        topPanel.add(calcButton);
        topPanel.add(analyzeButton);

        add(topPanel, BorderLayout.NORTH);

        displayArea = new JTextArea();
        add(new JScrollPane(displayArea), BorderLayout.CENTER);

        saveButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String name = nameField.getText();
                names.add(name);
                try (BufferedWriter writer = new BufferedWriter(new FileWriter("names.txt"))) {
                    for (String n : names) {
                        writer.write(n);
                        writer.newLine();
                    }
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                displayArea.setText("Saved " + names.size() + " names.");
            }
        });

        loadButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                names.clear();
                try (BufferedReader reader = new BufferedReader(new FileReader("names.txt"))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        names.add(line);
                    }
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                displayArea.setText(String.join("\n", names));
            }
        });

        calcButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                StringBuilder sb = new StringBuilder();
                for (String name : names) {
                    sb.append(name).append(" -> ").append(name.length()).append(" chars\n");
                }
                displayArea.setText(sb.toString());
            }
        });

        analyzeButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int totalLength = 0;
                int shortest = Integer.MAX_VALUE;
                int longest = 0;
                for (String name : names) {
                    int len = name.length();
                    totalLength += len;
                    if (len < shortest) shortest = len;
                    if (len > longest) longest = len;
                }
                double average = names.isEmpty() ? 0 : (double) totalLength / names.size();
                StringBuilder sb = new StringBuilder();
                sb.append("Average Length: ").append(String.format("%.2f", average)).append("\n");
                sb.append("Shortest Name Length: ").append(shortest).append("\n");
                sb.append("Longest Name Length: ").append(longest).append("\n");
                displayArea.setText(sb.toString());
            }
        });

        setVisible(true);
    }

    public static void main(String[] args) {
        new BadApp51();
    }
}
