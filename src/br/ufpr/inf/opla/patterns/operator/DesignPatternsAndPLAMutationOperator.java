package br.ufpr.inf.opla.patterns.operator;

import arquitetura.exceptions.ClassNotFound;
import arquitetura.exceptions.ConcernNotFoundException;
import arquitetura.exceptions.NotFoundException;
import arquitetura.exceptions.PackageNotFound;
import arquitetura.representation.Architecture;
import arquitetura.representation.Interface;
import br.ufpr.inf.opla.patterns.designpatterns.DesignPattern;
import br.ufpr.inf.opla.patterns.models.Scope;
import br.ufpr.inf.opla.patterns.repositories.ArchitectureRepository;
import br.ufpr.inf.opla.patterns.strategies.DesignPatternSelectionStrategy;
import br.ufpr.inf.opla.patterns.strategies.ScopeSelectionStrategy;
import br.ufpr.inf.opla.patterns.strategies.defaultstrategy.RandomDesignPatternSelection;
import br.ufpr.inf.opla.patterns.strategies.defaultstrategy.RandomScopeSelection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import jmetal.core.Solution;
import jmetal.operators.mutation.Mutation;
import jmetal.operators.mutation.PLAFeatureMutation;
import jmetal.problems.OPLA;
import jmetal.util.Configuration;
import jmetal.util.JMException;
import jmetal.util.PseudoRandom;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.log4j.Priority;

public class DesignPatternsAndPLAMutationOperator extends Mutation {

    private static final Logger LOGGER = LogManager.getLogger(DesignPatternsAndPLAMutationOperator.class);
    private final PLAFeatureMutation pLAFeatureMutation;

    public DesignPatternsAndPLAMutationOperator(HashMap<String, Object> parameters) {
        super(parameters);
        pLAFeatureMutation = new PLAFeatureMutation(parameters);
    }

    public Architecture mutateArchitecture(Architecture architecture) {
        RandomScopeSelection rss = new RandomScopeSelection();
        RandomDesignPatternSelection rdps = new RandomDesignPatternSelection();
        return mutateArchitecture(architecture, rss, rdps);
    }

    public Architecture mutateArchitecture(Architecture architecture, ScopeSelectionStrategy scopeSelectionStartegy, DesignPatternSelectionStrategy designPatternSelectionStrategy) {
        ArchitectureRepository.setCurrentArchitecture(architecture);
        DesignPattern designPattern = designPatternSelectionStrategy.selectDesignPattern();
        Scope scope = scopeSelectionStartegy.selectScope(architecture);
        if (designPattern.randomlyVerifyAsPSOrPSPLA(scope)) {
            if (designPattern.apply(scope)) {
                LOGGER.log(Priority.INFO,
                        "Design Pattern " + designPattern.getName() + " applied to scope " + scope.getElements().toString() + " successfully!");
            }
        }
        return architecture;
    }

    @Override
    public Object execute(Object o) throws JMException, CloneNotSupportedException, ClassNotFound, PackageNotFound, NotFoundException, ConcernNotFoundException {
        Solution solution = (Solution) o;
        Double probability = (Double) getParameter("probability");

        if (probability == null) {
            Configuration.logger_.severe("FeatureMutation.execute: probability not specified");
            java.lang.Class<String> cls = java.lang.String.class;
            String name = cls.getName();
            throw new JMException("Exception in " + name + ".execute()");
        }

        try {
            int random = PseudoRandom.randInt(0, 6);
            if (random == 0) {
                if (solution.getDecisionVariables()[0].getVariableType() == java.lang.Class.forName(Architecture.ARCHITECTURE_TYPE)) {
                    if (PseudoRandom.randDouble() < probability) {
                        Architecture arch = ((Architecture) solution.getDecisionVariables()[0]);
                        this.mutateArchitecture(arch);
                    }
                }
                if (!this.isValidSolution(((Architecture) solution.getDecisionVariables()[0]))) {
                    Architecture clone = ((Architecture) solution.getDecisionVariables()[0]).deepClone();
                    solution.getDecisionVariables()[0] = clone;
                    OPLA.contDiscardedSolutions_++;
                    LOGGER.log(Priority.INFO, "Invalid Solution. Reverting Modifications.");
                }
            } else {
                pLAFeatureMutation.execute(o);
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (Exception ex) {
            java.util.logging.Logger.getLogger(DesignPatternsAndPLAMutationOperator.class.getName()).log(Level.SEVERE, null, ex);
        }

        return solution;
    }

    private boolean isValidSolution(Architecture solution) {
        boolean isValid = true;
        List<Interface> allInterfaces = new ArrayList<>(solution.getAllInterfaces());
        if (!allInterfaces.isEmpty()) {
            for (Interface itf : allInterfaces) {
                if ((itf.getImplementors().isEmpty()) && (itf.getDependents().isEmpty()) && (!itf.getOperations().isEmpty())) {
                    return false;
                }
            }
        }
        return isValid;
    }

}
