package cmd;

import java.awt.*;
import java.io.File;
import javax.swing.*;

public class Main extends JFrame{
    
    private final JTextArea console = new JTextArea();
    private final JScrollPane scroll = new JScrollPane(console);

    private File rootDir;
    private File currentDir;

    public Main() {
        setTitle("Administrador: Command Prompt");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(920, 560);
        setLocationRelativeTo(null);
        
        console.setEditable(true);
        console.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 16));
        console.setBackground(Color.BLACK);
        console.setForeground(new Color(200, 200, 200));
        console.setLineWrap(true);
        apuntarDir();

        setLayout(new BorderLayout());
        add(scroll, BorderLayout.CENTER);
    }
    
    private void apuntarDir() {
        String projectDir = System.getProperty("user.dir");
        File sandbox = new File(projectDir);
        this.rootDir = sandbox;
        this.currentDir = sandbox;
    }
    
    public static void main(String[] args) {
        new Main().setVisible(true);
    }
    
}
