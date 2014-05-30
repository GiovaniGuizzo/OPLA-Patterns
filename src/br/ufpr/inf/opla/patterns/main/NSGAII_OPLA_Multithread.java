/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ufpr.inf.opla.patterns.main;

import br.ufpr.inf.opla.patterns.repositories.ArchitectureRepository;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author giovaniguizzo
 */
public class NSGAII_OPLA_Multithread {

    private static int MAX_THREADS = 4;
    private static int RUNNING_THREADS = 0;
    private static List<Thread> ACTIVE_THREADS;

    private static final String[] PLAS = {
        ArchitectureRepository.MICROWAVE_OVEN_SOFTWARE,
        ArchitectureRepository.AGM
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

    private static void initialize() {
        ACTIVE_THREADS = new ArrayList<>();

        new Thread(new Runnable() {

            @Override
            public void run() {
                Scanner scanner = new Scanner(System.in);
                while (true) {
                    String line = scanner.nextLine();
                    switch (line) {
                        case "+":
                            incrementMaxThreads();
                            break;
                        case "-":
                            decrementMaxThreads();
                            break;
                        case "print":
                            System.out.println("There are " + getActiveThreadsSize() + " threads active now. They are:");
                            for (Thread thread : getActiveThreads()) {
                                System.out.println(thread.getName());
                            }
                            break;
                        case "exit":
                            System.out.println("Are you sure? (true or false):");
                            boolean sure = scanner.nextBoolean();
                            if (sure) {
                                killAllThreads();
                                System.exit(0);
                            }
                            break;
                        default:
                            System.out.println("Command not valid. Please, select one of the following:");
                            System.out.println("\t\"+\" - Increments the max number of threads;");
                            System.out.println("\t\"-\" - Decrements the max number of threads;");
                            System.out.println("\t\"print\" - Prints the current running threads;");
                            System.out.println("\t\"exit\" - Exits program and kills all running threads;");
                    }
                }
            }

        }).start();
    }

    public static void main(String[] args) throws InterruptedException {
        initialize();

        for (final int maxEvaluations : MAX_EVALUATIONS) {
            for (final int populationSize : POPULATION_SIZE) {
                for (final double mutationProbability : MUTATION_PROBABILITY) {
                    for (final String mutationOperator : MUTATION_OPERATORS) {
                        for (final String pla : PLAS) {
                            final String context = getContext(pla, mutationOperator, String.valueOf(populationSize), String.valueOf(maxEvaluations), String.valueOf(mutationProbability));
                            while (RUNNING_THREADS >= MAX_THREADS) {
                                Thread.sleep(1000);
                            }
                            incrementRunningThreads();
                            new Thread(new Runnable() {

                                private Process process = null;

                                @Override
                                public void run() {
                                    try {
                                        System.out.println("Initializing: " + context);
                                        ProcessBuilder builder = new ProcessBuilder("java", "-jar", "dist/OPLA-Patterns.jar",
                                                "" + populationSize,
                                                "" + maxEvaluations,
                                                "" + mutationProbability,
                                                pla,
                                                context,
                                                mutationOperator,
                                                "false");
                                        final File destination = new File("experiment/" + NSGAII_OPLA.getPlaName(pla) + "/" + context + "/SYSTEM_OUTPUT.txt");
                                        {
                                            final File parentFile = destination.getParentFile();
                                            if (!parentFile.exists()) {
                                                parentFile.mkdirs();
                                            }
                                        }
                                        destination.createNewFile();
                                        builder.redirectOutput(destination);
                                        builder.redirectError(destination);
                                        process = builder.start();
                                        int exitValue = process.waitFor();
                                        System.out.println("Ending: " + context + " with exit value " + exitValue);
                                    } catch (IOException | InterruptedException ex) {
                                        Logger.getLogger(NSGAII_OPLA_Multithread.class.getName()).log(Level.SEVERE, null, ex);
                                    } finally {
                                        if (process != null) {
                                            process.destroy();
                                        }
                                        decrementRunningThreads();
                                    }
                                }
                            }, context).start();
                        }
                    }
                }
            }
        }

    }

    public static synchronized void decrementRunningThreads() {
        RUNNING_THREADS--;
    }

    public static synchronized void incrementRunningThreads() {
        RUNNING_THREADS++;
    }

    public static synchronized void decrementMaxThreads() {
        MAX_THREADS--;
    }

    public static synchronized void incrementMaxThreads() {
        MAX_THREADS++;
    }

    private static synchronized void killAllThreads() {
        RUNNING_THREADS = Integer.MAX_VALUE;
        for (Thread thread : ACTIVE_THREADS) {
            thread.stop();
        }
    }

    private static synchronized void addThread(Thread thread) {
        ACTIVE_THREADS.add(thread);
    }

    private static synchronized void removeThread(Thread thread) {
        ACTIVE_THREADS.remove(thread);
    }

    private static synchronized int getActiveThreadsSize() {
        return ACTIVE_THREADS.size();
    }

    private static synchronized List<Thread> getActiveThreads() {
        return new ArrayList<>(ACTIVE_THREADS);
    }

    public static String getContext(String pla, String mutationOperator, String populationSize, String maxEvaluations, String mutationProbability) {
        return NSGAII_OPLA.getPlaName(pla) + "_" + mutationOperator + "_" + populationSize + "_" + maxEvaluations + "_" + mutationProbability;
    }

}
