import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.List;
import java.util.ArrayList;

class OrganizedAppView extends JFrame {
	private JTextField nameField;
    private JTextArea displayArea;
    private JButton saveButton, loadButton, calcButton, analyzeButton;
    private JPanel topPanel;
    private AppController appController;

    public OrganizedAppView(NamesManager nameManager) {
        this.appController = new AppController(nameManager);
        createContainer();
        createTopPanel();
        assembleApp();
        showApp();

    }

    private void createContainer() {
        setTitle("Bad App Example");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 800);
        setLayout(new BorderLayout());
    }

    private void createTopPanel() {
        topPanel = new JPanel();
        nameField = new JTextField(20);
        topPanel.add(nameField);

        saveButton = appController.setUpSaveButton(nameField);
        loadButton = appController.setUpLoadButton();
        calcButton = appController.setUpCalcButton();
        analyzeButton = appController.setUpAnalyzeButton();

        topPanel.add(saveButton);
        topPanel.add(loadButton);
        topPanel.add(calcButton);
        topPanel.add(analyzeButton);
    }

    private void assembleApp() {
        add(topPanel, BorderLayout.NORTH);
        add(new JScrollPane(appController.getDisplayArea()), BorderLayout.CENTER);
    }

    private void showApp() {
        setVisible(true);
    }
}