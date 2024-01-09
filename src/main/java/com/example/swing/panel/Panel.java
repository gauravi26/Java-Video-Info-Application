package com.example.swing.panel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import javax.swing.*;
import java.awt.FlowLayout;



public class Panel extends JFrame {
    private JButton browse;
    private JPanel Extract;
    private JTextField path;
    private JButton info;
    private JButton browseAgainButton;
    private JButton exitButton;
    private Process batchProcess;
    private JLabel Header;
    private JLabel SubHeading;

    // // Add a JLabel for the heading inside the constructor
    //        JLabel headingLabel = new JLabel("Extract Video File Name and Duration");
    //        headingLabel.setFont(new Font("Arial", Font.BOLD, 16)); // Adjust font size and style as needed
    public Panel() {
        this.info = new JButton("Info");
        this.browse = new JButton("Browse");
        this.path = new JTextField(20);
        this.Extract = new JPanel();
        // Use FlowLayout instead of BoxLayout
        this.Extract.setLayout(new FlowLayout());

        browseAgainButton = new JButton("Browse Again");
        this.browse.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            int result = fileChooser.showOpenDialog(null);
            if (result == JFileChooser.APPROVE_OPTION) {
                File selectedFolder = fileChooser.getSelectedFile();
                this.path.setText(selectedFolder.getAbsolutePath());
            }
        });

        this.info.addActionListener(e -> {
            String folderPath = this.path.getText();
            if (!folderPath.isEmpty()) {
                try {
                    String command = "cmd";
                    String argument = "/c videocalculator.bat"; // Replace with the actual name of your batch file
                    ProcessBuilder processBuilder = new ProcessBuilder(command, argument);
                    processBuilder.environment().put("folder_path", folderPath);
                    this.batchProcess = processBuilder.start();
                    OutputStream outputStream = this.batchProcess.getOutputStream();
                    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream));
                    writer.write(folderPath);
                    writer.newLine();
                    writer.flush();
                    int exitCode = this.batchProcess.waitFor();
                    System.out.println("Batch file exited with code: " + exitCode);
                    writer.close();
                    outputStream.close();
                    Object message = exitCode == 0 ? "Batch file executed successfully!" :
                            "Batch file exited with an error. Exit code: " + exitCode;
                    JOptionPane.showMessageDialog(null, message, "Batch File Result", JOptionPane.INFORMATION_MESSAGE);
                } catch (IOException | InterruptedException ex) {
                    ex.printStackTrace();
                }
            }
        });
        this.Extract.add(this.Header);
        this.Extract.add(SubHeading);
        this.Extract.add(this.browse);
        this.Extract.add(this.path);
        this.Extract.add(this.info);
        this.setContentPane(this.Extract);
        browseAgainButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Clear the text in the path JTextField
                Panel.this.path.setText("");

            }
        });

        this.Extract.add(browseAgainButton);


    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Panel");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Create an instance of Panel
        Panel panel = new Panel();

        // Set the content pane of the frame to the panel's content pane
        frame.setContentPane(panel.getContentPane());

        // Pack the frame to fit its components
        frame.pack();

        // Set the size you desire (e.g., 600x400 pixels)
        frame.setSize(600, 400);

        // Center the frame on the screen
        frame.setLocationRelativeTo(null);

        // Make the frame visible
        frame.setVisible(true);
    }

    public JComponent $$$getRootComponent$$$() {
        return this.Extract;
    }
}
