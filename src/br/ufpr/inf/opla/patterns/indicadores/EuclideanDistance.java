package br.ufpr.inf.opla.patterns.indicadores;

import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import jmetal.core.SolutionSet;
import jmetal.qualityIndicator.util.MetricsUtil;
import jmetal.util.JMException;

public class EuclideanDistance {

    //  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --
    public static void main(String[] args) throws FileNotFoundException, IOException, JMException, ClassNotFoundException {
        String[] plas = {
            "MicrowaveOvenSoftware",
//            "ServiceAndSupportSystem"
        };

        String[] contextos = {
            "PLAMutation",
            "PLAMutationWithPatterns",
//            "OnlyPatternsMutation"
        };
        for (String pla : plas) {
            MetricsUtil mu = new MetricsUtil();

            try (FileWriter funAll = new FileWriter("experiment/" + pla + "/" + "FUN_All_" + pla + ".txt")) {
                for (String contexto : contextos) {
                    double[][] front = mu.readFront("experiment/" + pla + "/" + contexto + "/" + "FUN_ALL_" + pla + ".txt");
                    for (double[] solucao : front) {
                        funAll.write(solucao[0] + " " + solucao[1] + "\n");
                    }
                }
            }

            SolutionSet ss = mu.readNonDominatedSolutionSet("experiment/" + pla + "/" + "FUN_All_" + pla + ".txt");
            ss = removeDominadas(ss);
            ss.printObjectivesToFile("experiment/" + pla + "/" + "FUN_All_" + pla + ".txt");

            double[] min = mu.getMinimumValues(mu.readFront("experiment/" + pla + "/" + "FUN_All_" + pla + ".txt"), 2);
            try (FileWriter todosEds = new FileWriter("experiment/" + pla + "/" + "ALL_ED_" + pla + ".txt")) {
                try (FileWriter menoresEds = new FileWriter("experiment/" + pla + "/" + "MIN_ED_" + pla + ".txt")) {
                    todosEds.write("--- " + min[0] + " " + min[1] + " ---" + "\n");
                    for (String contexto : contextos) {
                        List<Integer> melhoresSolucoesPorContexto = new ArrayList<>();
                        double menorDistancia = Double.MAX_VALUE;

                        double[][] front = mu.readFront("experiment/" + pla + "/" + contexto + "/" + "FUN_ALL_" + pla + ".txt");
                        for (int i = 0; i < front.length; i++) {
                            double distanciaEuclidiana = mu.distance(min, front[i]);
                            todosEds.write(distanciaEuclidiana + "\n");
                            if (distanciaEuclidiana < menorDistancia) {
                                menorDistancia = distanciaEuclidiana;
                                melhoresSolucoesPorContexto.clear();
                            }
                            if (distanciaEuclidiana == menorDistancia) {
                                melhoresSolucoesPorContexto.add(i);
                            }
                        }

                        for (Integer melhorSolucao : melhoresSolucoesPorContexto) {
                            menoresEds.write(contexto + ": " + melhorSolucao + " - ED: " + menorDistancia + "\n");
                        }
                    }
                }
            }
        }
    }

    public static SolutionSet removeDominadas(SolutionSet result) {
        boolean dominador, dominado;
        double valor1 = 0;
        double valor2 = 0;

        for (int i = 0; i < (result.size() - 1); i++) {
            for (int j = (i + 1); j < result.size(); j++) {
                dominador = true;
                dominado = true;

                for (int k = 0; k < result.get(i).numberOfObjectives(); k++) {
                    valor1 = result.get(i).getObjective(k);
                    valor2 = result.get(j).getObjective(k);

                    if (valor1 > valor2 || dominador == false) {
                        dominador = false;
                    } else if (valor1 <= valor2) {
                        dominador = true;
                    }

                    if (valor2 > valor1 || dominado == false) {
                        dominado = false;
                    } else if (valor2 < valor1) {
                        dominado = true;
                    }
                }

                if (dominador) {
                    result.remove(j);
                    j = j - 1;
                } else if (dominado) {
                    result.remove(i);
                    j = i;
                }
            }
        }

        return result;
    }

}
