/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ufpr.inf.opla.patterns.indicadores;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import jmetal.core.SolutionSet;

/**
 *
 * @author giovaniguizzo
 */
public class Hypervolume {

    public static void clearFile(String path) {
        File file = new File(path);
        if (file.exists()) {
            file.delete();
        }
    }

    public static void printFormatedHypervolumeFile(SolutionSet allSolutions, String path) throws IOException {
        File file = new File(path);
        file.getParentFile().mkdirs();
        try (FileWriter fileWriter = new FileWriter(file, true)) {
            for (int i = 0; i < allSolutions.size(); i++) {
                fileWriter.write(allSolutions.get(i).toString());
                fileWriter.write("\n");
            }
            fileWriter.write("\n");
        }
    }

    public static void printReferencePoint(SolutionSet allSolutions, String path, int objectives) throws IOException {
        File file = new File(path);
        file.getParentFile().mkdirs();
        double[] max = new double[objectives];
        for (int i = 0; i < max.length; i++) {
            max[i] = Double.MIN_VALUE;
            for (int j = 0; j < allSolutions.size(); j++) {
                double solutionValue = allSolutions.get(j).getObjective(i);
                if (solutionValue > max[i]) {
                    max[i] = solutionValue;
                }
            }
        }
        try (FileWriter fileWriter = new FileWriter(file)) {
            for (double d : max) {
                fileWriter.write(Double.toString(d + 0.1D) + " ");
            }
        }
    }

}
