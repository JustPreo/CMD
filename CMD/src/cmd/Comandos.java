package cmd;

import javax.swing.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class Comandos {

    private final JTextArea console;
    private final File rootDir;
    private File currentDir = null;
    boolean modoEscritura = false;

    private final Impresiones impresiones;

    public Comandos(JTextArea console, File rootDir, Impresiones impresiones) {
        this.console = console;
        this.rootDir = rootDir;
        this.currentDir = rootDir;
        this.impresiones = impresiones;
    }

    private void println(String text) {//Para ponerlo en la consola de ferchooooo
        console.append(text + "\n");
    }

    private void printPrompt() {
        impresiones.imprimirPrompt();
    }

    private String[] dividirArgs(String linea) {
        ArrayList<String> list = new ArrayList<>();
        boolean comillas = false;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < linea.length(); i++) {
            char c = linea.charAt(i);
            if (c == '"') {
                comillas = !comillas;
                continue;
            }
            if (!comillas && Character.isWhitespace(c)) {
                if (sb.length() > 0) {
                    list.add(sb.toString());
                    sb.setLength(0);
                }
            } else {
                sb.append(c);
            }
        }
        if (sb.length() > 0) {
            list.add(sb.toString());
        }
        if (list.isEmpty()) {
            return new String[]{""};
        }
        return list.toArray(new String[0]);
    }

    public void hacerComando(String linea) {
        println("\n");
        if (modoEscritura) {
            if (linea.equals(":wq")) {
                try {
                    Files.write(currentDir.toPath(),
                            console.getText().getBytes(StandardCharsets.UTF_8));
                    println("Archivo guardado: " + currentDir.getName());
                } catch (IOException e) {
                    println("Error al guardar: " + e.getMessage());
                }
                modoEscritura = false;
                printPrompt();
                return;
            } else {
                console.append(linea + "\n");
                return;
            }
        }
        if (linea.isEmpty()) {
            printPrompt();
            return;
        }

        String[] args = dividirArgs(linea);
        String cmd = args[0].toLowerCase();

        try {
            switch (cmd) {
                case "help":
                case "?"://EXTRA POR SI NOS DA TIEMPO

                    break;
                case "dir"://DIRECTORIO ACTUAL
                    cmdDir();

                    break;
                case "date"://FECHA ACTUAL
                    date();

                    break;
                case "time"://MIRAR TIEMPO ACTUAL
                    time();

                    break;
                case "mkdir"://CREAR CARPETA
                    mkdir(args);

                    break;
                case "mfile"://CREAR ARCHIVO
                    fileCrear(args);

                    break;
                case "rm"://BORARR (rm -rf en el CD)
                    Rm(args);

                    break;
                case "cd"://ELEGIR LA RUTA
                    cD(args);

                    break;
                case "<...>"://VOLVER UNA CARPETA ATRAS
                    volver();

                    break;
                case "wr"://ESCRIBIR
                    Wr(args);

                    break;
                case "rd"://LEER
                    Rd(args);
                    break;
                case "exit":
                    System.exit(0);
                    break;
                default:
                    println("'" + args[0] + "' no existe tal comando");
            }
        } catch (Exception ex) {
            println("Error: " + ex.getMessage());
        }

        printPrompt();
    }

    private void cmdDir()//Directorio
    {
        File[] lista = currentDir.listFiles();
        if (lista == null) {
            println("Directorio vacio");
            return;
        }

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        println(" Directorio de " + currentDir.getAbsolutePath());
        println(" ");
        for (File f : lista) {
            String fecha = df.format(new Date(f.lastModified()));
            String tipo = (f.isDirectory() ? "<DIR>" : "    ");
            println(String.format(Locale.ROOT, "%s %5s %s", fecha, tipo, f.getName()));
        }
        println("");
    }

    private void date() {
        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
        println(f.format(new Date()));
    }

    private void time() {
        SimpleDateFormat f = new SimpleDateFormat("HH:mm");
        println(f.format(new Date()));
    }

    private void mkdir(String[] args) throws IOException {
        if (args.length > 2) {
            println("Debe usar: mkdir <nombre>");
            return;
        }
        File archivo = new File(currentDir, args[1]);

        if (archivo.exists()) {
            println("Ya existe la carpeta");
            return;
        }

        if (archivo.mkdirs())//Revisa si se crea o no
        {
            println("Carpeta creada: " + archivo.getName());
        } else {
            println("No se pudo crear la carpeta");
        }

    }

    private void fileCrear(String[] args) throws IOException {
        if (args.length > 2) {
            println("Debe usar: mfile <nombre.txt>");
            return;
        }

        File archivo = new File(currentDir, args[1]);
        if (archivo.exists())//Revisar si existe
        {
            println("archivo ya existe");
            return;
        }

        if (archivo.createNewFile()) {
            println("Archivo creado: " + archivo.getName());
        } else {
            println("No se pudo crear el archivo");
        }
    }

    private boolean RmR(File f) {
        if (f.isDirectory()) {
            File[] children = f.listFiles();
            if (children != null) {
                for (File child : children) {
                    if (!RmR(child)) {
                        return false;
                    }
                }
            }
        }
        return f.delete();
    }

    private void Rm(String[] args) throws IOException {
        if (args.length < 2) {
            println("Debe usar: rm <nombre>");
            return;
        }

        File archivo = new File(currentDir, args[1]);
        if (!archivo.exists()) {
            println("No existe: " + archivo.getName());
        }
        if (RmR(archivo)) {
            println("Se elimino:" + archivo.getName());
        } else {
            println("No se pudo eliminar");
        }

    }

    private void volver() throws IOException {

        if (currentDir.equals(rootDir)) {
            println("Ya esta en la raiz");
            return;
        }
        currentDir = currentDir.getCanonicalFile().getParentFile();
        impresiones.setActualDir(currentDir);
    }

    private void cD(String[] args) throws IOException {
        if (args.length < 2) {
            println("Debe usar: cd <carpeta>");
            return;
        }
        if ("..".equals(args[1])) {
            volver();//Por si escribe ..
            return;
        }
        File dir = new File(currentDir, args[1]);
        if (!dir.exists() || !dir.isDirectory()) {
            println("Ruta invalida");
            return;
        }
        currentDir = dir;
        impresiones.setActualDir(currentDir);

    }

    private void Wr(String[] args) throws IOException {
    if (args.length < 2) {
        println("Uso: wr <archivo>");
        return;
    }

    File file = new File(currentDir, args[1]);
    if (!file.exists() || !file.isFile()) {
        println("El archivo no existe.");
        return;
    }
    JTextArea area = new JTextArea(15, 50);
    area.setFont(new java.awt.Font("Monospaced", java.awt.Font.PLAIN, 14));
    JScrollPane sp = new JScrollPane(area);
    String textoExistente = new String(Files.readAllBytes(file.toPath()), StandardCharsets.UTF_8);
    area.setText(textoExistente);

    int result = JOptionPane.showConfirmDialog(
            null,
            sp,
            "Escribir en " + file.getName(),
            JOptionPane.OK_CANCEL_OPTION,
            JOptionPane.PLAIN_MESSAGE
    );

    if (result == JOptionPane.OK_OPTION) {
        Files.write(file.toPath(), area.getText().getBytes(StandardCharsets.UTF_8));
        println("Archivo guardado: " + file.getName());
    } else {
        println("Edicion cancelada");
    }
}


    private void Rd(String[] args) throws IOException {
        if (args.length < 2) {
            println("Uso: Rd <archivo>");
            return;
        }

        File file = new File(currentDir, args[1]);

        if (!file.exists() || !file.isFile()) {
            println("Archivo no existe");
            return;
        }
        String texto = new String(Files.readAllBytes(file.toPath()), StandardCharsets.UTF_8);
        println("");
        println("=== " + file.getName() + " ===");
        println((texto.isEmpty() ? "(Vacio)" : texto));
        println("====== FIN ======");
    }

}
