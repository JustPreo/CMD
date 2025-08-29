package cmd;

import java.io.File;
import java.io.IOException;
import javax.swing.*;

public class Impresiones {

    private final JTextArea consola;
    private final File rootDir;
    private File actualDir;

    public Impresiones(JTextArea consola, File rootDir) {
        this.consola = consola;
        this.rootDir = rootDir;
        this.actualDir = rootDir;
    }

    public File getActualDir() {
        return actualDir;
    }

    public void setActualDir(File dir) {
        this.actualDir = dir;
    }

    public void imprimirln(String s) {
        consola.setText(consola.getText() + s + "\n");
        consola.setCaretPosition(consola.getDocument().getLength());
    }

    public void imprimir(String s) {
        consola.setText(consola.getText() + s);
        consola.setCaretPosition(consola.getDocument().getLength());
    }

    public void imprimirBanner() {
        imprimirln("Microsoft Windows [Version 1.0]");
        imprimirln("(c) Fernando & Aaron Industries. Todos los derechos reservados.\n");
    }

    public String promptTexto() {
        String rel = rootRelativePath(actualDir);
        return "C:" + rel + "> ";
    }

    public void imprimirPrompt() {
        imprimir(promptTexto());
    }

    private String rootRelativePath(File f) {
        try {
            String root = rootDir.getCanonicalPath();
            String path = f.getCanonicalPath();
            String relacion = path.equals(root) ? "\\" : path.substring(root.length()).replace('/', '\\');
            if (!relacion.startsWith("\\")) {
                relacion = "\\" + relacion;
            }
            return relacion;
        } catch (IOException e) {
            return "\\";
        }
    }

    public int ultimaLinea() {
        String texto = consola.getText();
        return texto.lastIndexOf('\n') + 1;
    }

    public String leerActual() {
        int inicio = ultimaLinea();
        String recuadro = consola.getText().substring(inicio);
        if (!recuadro.startsWith(promptTexto())) {
            consola.setCaretPosition(consola.getText().length());
            return null;
        }
        return recuadro.substring(promptTexto().length());
    }
}
