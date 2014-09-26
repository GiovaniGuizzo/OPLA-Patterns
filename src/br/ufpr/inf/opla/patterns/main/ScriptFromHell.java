/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ufpr.inf.opla.patterns.main;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

/**
 *
 * @author giovaniguizzo
 */
public class ScriptFromHell {

    public static void main(String[] args) {
        try {
            File experiment = new File("./experiment");
            recursivelyMove(experiment);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private static void recursivelyMove(File parent) throws IOException {
        for (File child : parent.listFiles()) {
            if (child.isFile()) {
                if (child.getName().startsWith("output")
                        && (child.getName().endsWith(".uml") || child.getName().endsWith(".di") || child.getName().endsWith(".notation"))) {
                    Path source = Paths.get(child.getPath());
                    Path destination = source.resolveSibling("output").resolve(child.getName().replace("output", ""));
                    System.out.println("Arquivo movido: " + destination.toString());
                    Files.move(source, destination, StandardCopyOption.REPLACE_EXISTING);
                }
            } else if (child.isDirectory() && !child.getName().startsWith(".")) {
                recursivelyMove(child);
            }
        }
    }

}
