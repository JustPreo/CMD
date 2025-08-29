package cmd;

import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import javax.swing.*;

public class Main extends JFrame {

    private final JTextArea consola = new JTextArea();
    private final JScrollPane scroll = new JScrollPane(consola);

    private File rootDir;
    private File actualDir;

    public Main() {

        setTitle("Administrador: Command Prompt");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(920, 560);
        setLocationRelativeTo(null);

        consola.setEditable(true);
        consola.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 16));
        consola.setBackground(Color.BLACK);
        consola.setForeground(new Color(200, 200, 200));
        consola.setLineWrap(true);
        
        apuntarDir();
        Impresiones impresiones = new Impresiones(consola, rootDir);
        impresiones.setActualDir(actualDir);
        Comandos cmd = new Comandos(consola, rootDir, impresiones);

        consola.addKeyListener(new KeyAdapter() {

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    e.consume();
                    String line = impresiones.leerActual();
                    if (line != null) {
                        cmd.hacerComando(line.trim());
                    }
                } else if (e.getKeyCode() == KeyEvent.VK_BACK_SPACE || e.getKeyCode() == KeyEvent.VK_LEFT) {
                    int ingreso = consola.getCaretPosition();
                    int lineaInicio = impresiones.ultimaLinea();
                    if (ingreso <= lineaInicio + impresiones.promptTexto().length()) {
                        e.consume();
                    }
                }
            }

            @Override
            public void keyTyped(KeyEvent e) {
                int ingreso = consola.getCaretPosition();
                int lineaInicio = impresiones.ultimaLinea();
                if (ingreso < lineaInicio + impresiones.promptTexto().length()) {
                    consola.setCaretPosition(consola.getText().length());
                }
            }

        });

        setLayout(new BorderLayout());
        add(scroll, BorderLayout.CENTER);

        impresiones.imprimirBanner();
        impresiones.imprimirPrompt();

    }

    private void apuntarDir() {
        String proyectoDir = System.getProperty("user.dir");
        File predeterminada = new File(proyectoDir);
        this.rootDir = predeterminada;
        this.actualDir = predeterminada;
    }

    public static void main(String[] args) {
        new Main().setVisible(true);
    }

}
