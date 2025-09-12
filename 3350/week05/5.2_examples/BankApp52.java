import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class BankApp52 extends JFrame {
    private JTextField nameField = new JTextField(20);
    private JTextField accountField = new JTextField(20);
    private JTextArea outputArea = new JTextArea(10, 30);

    public BankApp52() {
        setTitle("Bank Application");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel inputPanel = new JPanel();
        inputPanel.add(new JLabel("Customer Name:"));
        inputPanel.add(nameField);
        inputPanel.add(new JLabel("Account Number:"));
        inputPanel.add(accountField);

        JButton addButton = new JButton("Add Customer");
        addButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String name = nameField.getText();
                String acc = accountField.getText();
                if (name.isEmpty() || acc.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Please fill in all fields.");
                    return;
                }
                outputArea.append("Customer Added: " + name + " (Account: " + acc + ")\n");
                nameField.setText("");
                accountField.setText("");
            }
        });

        JButton searchButton = new JButton("Search Customer");
        searchButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String name = nameField.getText();
                String acc = accountField.getText();
                if (name.isEmpty() || acc.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Please fill in all fields.");
                    return;
                }
                outputArea.append("Searching for: " + name + " (Account: " + acc + ")\n");
                nameField.setText("");
                accountField.setText("");
            }
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(addButton);
        buttonPanel.add(searchButton);

        add(inputPanel, BorderLayout.NORTH);
        add(buttonPanel, BorderLayout.CENTER);
        add(new JScrollPane(outputArea), BorderLayout.SOUTH);

        pack();
        setVisible(true);
    }

    
    public static void main(String[] args) {
        new BankApp52();
    }
}
