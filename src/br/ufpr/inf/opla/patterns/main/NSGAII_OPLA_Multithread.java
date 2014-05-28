/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ufpr.inf.opla.patterns.main;

import br.ufpr.inf.opla.patterns.repositories.ArchitectureRepository;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author giovaniguizzo
 */
public class NSGAII_OPLA_Multithread {

    private static final int MAX_THREADS = 2;
    private static int RUNNING_THREADS = 0;

    private static final String[] PLAS = {
        ArchitectureRepository.AGM,
        ArchitectureRepository.MICROWAVE_OVEN_SOFTWARE
    };

    private static final String[] MUTATION_OPERATORS = {
        "DesignPatternsMutationOperator",
        "DesignPatternsAndPLAMutationOperator",
        "PLAMutation"
    };

    private static final int[] POPULATION_SIZE = {
        50,
        100,
        200
    };

    private static final int[] MAX_EVALUATIONS = {
        3000,
        30000,
        300000
    };

    private static final double[] MUTATION_PROBABILITY = {
        0.1,
        0.5,
        0.9
    };

    public static void main(String[] args) throws InterruptedException {
        for (final String pla : PLAS) {
            for (final String mutationOperator : MUTATION_OPERATORS) {
                for (final int populationSize : POPULATION_SIZE) {
                    for (final int maxEvaluations : MAX_EVALUATIONS) {
                        for (final double mutationProbability : MUTATION_PROBABILITY) {
                            final String context = getContext(pla, mutationOperator, String.valueOf(populationSize), String.valueOf(maxEvaluations), String.valueOf(mutationProbability));
                            while (RUNNING_THREADS >= MAX_THREADS) {
                                Thread.sleep(1000);
                            }
                            RUNNING_THREADS++;
                            new Thread(new Runnable() {

                                @Override
                                public void run() {
                                    try {
                                        Runtime runtime = Runtime.getRuntime();
                                        Process exec = runtime.exec("java -jar dist/OPLA-Patterns.jar "
                                                + populationSize + " "
                                                + maxEvaluations + " "
                                                + mutationProbability + " "
                                                + pla + " "
                                                + context + " "
                                                + mutationOperator + " false");
                                        exec.waitFor();
                                    } catch (IOException | InterruptedException ex) {
                                        Logger.getLogger(NSGAII_OPLA_Multithread.class.getName()).log(Level.SEVERE, null, ex);
                                    }
                                    RUNNING_THREADS--;
                                }
                            }).start();
                        }
                    }
                }
            }
        }

    }

    public static String getContext(String pla, String mutationOperator, String populationSize, String maxEvaluations, String mutationProbability) {
        return NSGAII_OPLA.getPlaName(pla) + "_" + mutationOperator + "_" + populationSize + "_" + maxEvaluations + "_" + mutationProbability;
    }

}
