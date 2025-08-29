/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cmd;

import javax.swing.*;
import java.awt.*;
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

    public Comandos(JTextArea console, File rootDir) {
        this.console = console;
        this.rootDir = rootDir;
        this.currentDir = rootDir;
    }

    private void println(String text) {//Para ponerlo en la consola de ferchooooo
        console.append(text + "\n");
    }

    private void printPrompt() {
        console.append(currentDir.getAbsolutePath() + "> ");
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
        println(linea);

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

                    break;
                case "date"://FECHA ACTUAL

                    break;
                case "time"://MIRAR TIEMPO ACTUAL

                    break;
                case "mkdir"://CREAR CARPETA

                    break;
                case "mfile"://CREAR ARCHIVO

                    break;
                case "rm"://BORARR (rm -rf en el CD)

                    break;
                case "cd"://ELEGIR LA RUTA

                    break;
                case "<...>"://VOLVER UNA CARPETA ATRAS

                    break;
                case "wr"://ESCRIBIR

                    break;
                case "rd"://LEER

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
    if (lista == null)
    {
    println("Directorio vacio");
    return;
    }
    
    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    println(" Directorio de "+currentDir.getAbsolutePath());
    println(" ");
    for (File f:lista)
    {
    String fecha = df.format(new Date(f.lastModified()));
    String tipo = (f.isDirectory() ? "<DIR>":"    ");
    println(String.format(Locale.ROOT,"%s %5s %s",fecha,tipo,f.getName() ));
    }
    println("");
    }
    
    private void date()
    {
    SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
    println(f.format(new Date()));
    }
    
    private void time()
    {
    SimpleDateFormat f = new SimpleDateFormat("HH:mm");
    println(f.format(new Date()));
    }
    
    private void mkdir(String[] args) throws IOException
    {
    if (args.length>2)
    {
    println("Debe usar: mkdir <nombre>");
    return;
    }
    File archivo = new File(currentDir, args[1]);
    
    if (archivo.exists())
    {
    println("Ya existe la carpeta");
    return;
    }
    
    if (archivo.mkdirs())
    {
    println("Carpeta creada: "+ archivo.getName()); 
    }
    else
    {
    println("No se pudo crear la carpeta");
    }
    
    }

}
