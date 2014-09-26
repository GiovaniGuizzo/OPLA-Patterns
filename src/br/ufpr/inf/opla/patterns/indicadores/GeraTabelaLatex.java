/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ufpr.inf.opla.patterns.indicadores;

import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import jmetal.core.Solution;
import jmetal.core.SolutionSet;
import jmetal.qualityIndicator.util.MetricsUtil;

/**
 *
 * @author giovaniguizzo
 */
public class GeraTabelaLatex {

    public static void main(String[] args) {
        MetricsUtil mu = new MetricsUtil();
        DecimalFormat formatter = new DecimalFormat("#.###");

        HashMap<String, String[]> experiments = new HashMap();

        experiments.put("agm", new String[]{
            "agm_PLAMutation_200_30000_0.9",
            "agm_DesignPatternsMutationOperator_50_30000_0.9",
            "agm_DesignPatternsAndPLAMutationOperator_200_30000_0.9"
        });

        experiments.put("MicrowaveOvenSoftware", new String[]{
            "MicrowaveOvenSoftware_PLAMutation_50_3000_0.9",
            "MicrowaveOvenSoftware_DesignPatternsMutationOperator_50_300000_0.9",
            "MicrowaveOvenSoftware_DesignPatternsAndPLAMutationOperator_50_30000_0.9"
        });

        experiments.put("MobileMedia", new String[]{
            "MobileMedia_PLAMutation_50_30000_0.9",
            "MobileMedia_DesignPatternsMutationOperator_50_30000_0.9",
            "MobileMedia_DesignPatternsAndPLAMutationOperator_50_30000_0.9"
        });

        experiments.put("BeT", new String[]{
            "BeT_PLAMutation_50_30000_0.9",
            "BeT_DesignPatternsMutationOperator_100_30000_0.9",
            "BeT_DesignPatternsAndPLAMutationOperator_200_30000_0.9"
        });

        String[] plas = new String[]{"AGM", "MM", "BET", "MOS"};

        escreveTabelao(experiments, plas, mu, formatter);
        escreveTabelaED(experiments, plas, mu, formatter);
    }

    private static void escreveTabelao(HashMap<String, String[]> experiments, String[] plas, MetricsUtil mu, DecimalFormat formatter) {
        try (FileWriter tabelao = new FileWriter("experiment/TABELAO.txt")) {
            for (int i = 0; i < experiments.size(); i++) {
                Map.Entry<String, String[]> entry = (Map.Entry<String, String[]>) experiments.entrySet().toArray()[i];
                String pla = entry.getKey();
                String[] contexts = entry.getValue();

                tabelao.append("\\begin{table}[htb]\n"
                        + "\t\\scriptsize\n"
                        + "\t\\renewcommand{\\arraystretch}{1.3}\n"
                        + "\t\\centering\n"
                        + "\t\\caption{Soluções encontradas para a ALP " + plas[i] + "}\n"
                        + "\t\\label{TabelaFitness" + plas[i] + "}\n"
                        + "\\newcolumntype{C}{>{\\centering\\arraybackslash}X}\n"
                        + "\t\\begin{tabularx}{\\textwidth}{| c | C | C | C | C |}\n"
                        + "\t\t\\toprule");
                tabelao.append("\t\t\\multirow{2}{*}{\\textbf{ALP}} & \\multirow{2}{*}{\\textbf{PFtrue}} ");
                tabelao.append("& \\multicolumn{3}{c|}{\\textbf{PFknown}} ");
                tabelao.append("\\\\\n");

                tabelao.append("\t\t\\cline{3-5}\n");
                tabelao.append("\t\t& ");
                tabelao.append("& \\textbf{PLAM} ");
                tabelao.append("& \\textbf{DPM} ");
                tabelao.append("& \\textbf{PLADPM} ");
                tabelao.append("\\\\\n");
                tabelao.append("\t\t\\midrule\n");

                String directoryPath = "experiment/" + pla + "/";

                SolutionSet truePareto = removeDominadas(mu.readNonDominatedSolutionSet(directoryPath + "FUN_All_" + pla + ".txt"));

                tabelao.append("\t\t" + plas[i] + " & ");

                int aux = 0;
                for (Iterator<Solution> iterator = truePareto.iterator(); iterator.hasNext();) {
                    Solution solution = iterator.next();
                    tabelao.append("(");
                    for (int j = 0; j < 2; j++) {
                        double objective = solution.getObjective(j);
                        tabelao.append(formatter.format(objective) + "");
                        if (j != 1) {
                            tabelao.append(",");
                        }
                    }
                    tabelao.append(")");
                    if (aux == 1) {
                        aux = 0;
                        tabelao.append(" ");
                    } else {
                        aux++;
                    }
                }

                for (String context : contexts) {
                    SolutionSet knownPareto = mu.readNonDominatedSolutionSet(directoryPath + context + "/FUN_ALL_" + pla + ".txt");
                    tabelao.append("& ");

                    aux = 0;
                    for (Iterator<Solution> iterator = knownPareto.iterator(); iterator.hasNext();) {
                        Solution solution = iterator.next();
                        tabelao.append("(");
                        for (int j = 0; j < 2; j++) {
                            double objective = solution.getObjective(j);
                            tabelao.append(formatter.format(objective) + "");
                            if (j != 1) {
                                tabelao.append(",");
                            }
                        }
                        tabelao.append(")");
                        if (aux == 1) {
                            aux = 0;
                            tabelao.append(" ");
                        } else {
                            aux++;
                        }
                    }
                }

                tabelao.append("\n\t\t\\\\\\bottomrule\n");
                tabelao.append("\t\\end{tabularx}\n"
                        + "\\end{table}\n\n");

            }
        } catch (IOException ex) {
            Logger.getLogger(GeraTabelaLatex.class.getName()).log(Level.SEVERE, null, ex);
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
                    j -= 1;
                } else if (dominado) {
                    result.remove(i);
                    j = i;
                }
            }
        }

        return result;
    }

    private static void escreveTabelaED(HashMap<String, String[]> experiments, String[] plas, MetricsUtil mu, DecimalFormat formatter) {
        try (FileWriter tabela = new FileWriter("experiment/TABELA_ED.txt")) {

            tabela.append("\\begin{table}[htb]\n"
                    + "\t\\fontsize{10pt}{12pt}\\selectfont\n"
                    + "\t\\renewcommand{\\arraystretch}{1.3}\n"
                    + "\t\\centering\n"
                    + "\t\\caption{Soluções de maior e menor ED por experimento}\n"
                    + "\t\\label{TabelaED}\n"
                    + "\t\\newcolumntype{C}{>{\\centering\\arraybackslash}X}\n"
                    + "\t\\begin{tabularx}{\\textwidth}{| c | C | C | C | C | C | C |}\n"
                    + "\t\t\\toprule\n"
                    + "\t\t\\multirow{2}{*}{\\textbf{ALP}} & \\multicolumn{2}{c|}{\\textbf{PLAM}} & \\multicolumn{2}{c|}{\\textbf{DPM}} & \\multicolumn{2}{c|}{\\textbf{PLAMDPM}}\\\\\n"
                    + "\t\t\\cline{2-7}\n"
                    + "\t\t& \\textbf{-ED} & \\textbf{+ED} & \\textbf{-ED} & \\textbf{+ED} & \\textbf{-ED} & \\textbf{+ED} \\\\\n"
                    + "\t\t\\midrule\n");

            for (int i = 0; i < experiments.size(); i++) {
                Map.Entry<String, String[]> entry = (Map.Entry<String, String[]>) experiments.entrySet().toArray()[i];
                String pla = entry.getKey();
                String[] contexts = entry.getValue();

                tabela.append("\t\t" + plas[i]);

                String directoryPath = "experiment/" + pla + "/";
                SolutionSet ss = mu.readNonDominatedSolutionSet(directoryPath + "FUN_All_N_" + pla + ".txt");
                ss = removeDominadas(ss);
//        ss.printObjectivesToFile(directoryPath + "FUN_All_N_" + pla + ".txt");

                double[] min = mu.getMinimumValues(ss.writeObjectivesToMatrix(), 2);
                for (String contexto : contexts) {
                    double menorDistancia = Double.MAX_VALUE;
                    int menorIndex = -1;
                    double maiorDistancia = Double.MIN_VALUE;
                    int maiorIndex = -1;

//                    double[][] front = new double[quantidadeSolucoes][numObjetivos];
//                    for(solucoes)for(objetivos)solucoes[solucaoI][numObjJ] = valor do banco;
                    double[][] front = mu.readFront(directoryPath + contexto + "/" + "FUN_All_N_" + pla + ".txt");
                    for (int j = 0; j < front.length; j++) {
                        double distanciaEuclidiana = mu.distance(min, front[j]);
                        if (distanciaEuclidiana < menorDistancia) {
                            menorDistancia = distanciaEuclidiana;
                            menorIndex = j;
                        }

                        if (distanciaEuclidiana > maiorDistancia) {
                            maiorDistancia = distanciaEuclidiana;
                            maiorIndex = j;
                        }
                    }

                    double[][] frontOriginal = mu.readFront(directoryPath + contexto + "/" + "FUN_All_" + pla + ".txt");
                    double[] maiorSolucao = frontOriginal[maiorIndex];
                    double[] menorSolucao = frontOriginal[menorIndex];

                    tabela.append(" & " + formatter.format(menorDistancia) + " (");
                    for (int j = 0; j < menorSolucao.length; j++) {
                        double objetivo = menorSolucao[j];
                        tabela.append(formatter.format(objetivo));
                        if (j != menorSolucao.length - 1) {
                            tabela.append(",");
                        }
                    }
                    tabela.append(")");

                    tabela.append(" & " + formatter.format(maiorDistancia) + " (");
                    for (int j = 0; j < maiorSolucao.length; j++) {
                        double objetivo = maiorSolucao[j];
                        tabela.append(formatter.format(objetivo));
                        if (j != maiorSolucao.length - 1) {
                            tabela.append(",");
                        }
                    }
                    tabela.append(")");
                }

                if (i != experiments.size() - 1) {
                    tabela.append(" \\\\\\hline\n");
                } else {
                    tabela.append(" \\\\\n");
                }

            }
            tabela.append("\t\t\\bottomrule\n"
                    + "\t\\end{tabularx}\n"
                    + "\\end{table}");
        } catch (IOException ex) {
            Logger.getLogger(GeraTabelaLatex.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
