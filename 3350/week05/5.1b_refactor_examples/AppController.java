import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.List;
import java.util.ArrayList;

class AppController {
	private NamesManager namesManager;
	private JTextArea displayArea;

	public AppController(NamesManager namesManager) {
		this.namesManager = namesManager;
		displayArea = new JTextArea();
	}

	public JTextArea getDisplayArea() {
		return displayArea;
	}

	public JButton setUpSaveButton(JTextField nameField) {
		JButton saveButton = new JButton("Save");

		saveButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String name = nameField.getText();
                namesManager.saveName(name);
                displayArea.setText("Saved " + namesManager.numNames() + " names.");
            }
        });

        return saveButton;
	}

	public JButton setUpLoadButton() {
		JButton loadButton = new JButton("Load");
		loadButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
				displayArea.setText(namesManager.getStringOfNames());
            }
        });
		return loadButton;
	}

	public JButton setUpCalcButton() {

        JButton calcButton = new JButton("Calc Lengths");

        calcButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                displayArea.setText(namesManager.getNamesWithLengthCounts());
            }
        });
        return calcButton;
	}

	public JButton setUpAnalyzeButton() {
		JButton analyzeButton = new JButton("Analyze");
		analyzeButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                
                displayArea.setText(namesManager.getNamesStats());
            }
        });
        return analyzeButton;
		
	}
}