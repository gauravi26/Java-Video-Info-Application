package com.example.swing;
import java.io.File;
import javax.swing.JOptionPane;
public class checkjre {
    public static void main(String[] args) {
        String jreFolderPath = "C:\\Users\\hp\\sts-4.17.2.RELEASE\\plugins\\org.eclipse.justj.openjdk.hotspot.jre.full.win32.x86_64_17.0.6.v20230125-1136";
        boolean isJREInstalled = isJavaInstalled(jreFolderPath);

        if (isJREInstalled) {
            JOptionPane.showMessageDialog(null, "Java is installed at: " + jreFolderPath);
        } else {
            JOptionPane.showMessageDialog(null, "Java is not installed at: " + jreFolderPath);
        }
    }

    private static boolean isJavaInstalled(String jreFolderPath) {
        File jreFolder = new File(jreFolderPath);

        // Check if the specified JRE folder exists
        System.out.println(jreFolder);  // Move this line above the return statement
        return jreFolder.exists() && jreFolder.isDirectory();
    }
}
