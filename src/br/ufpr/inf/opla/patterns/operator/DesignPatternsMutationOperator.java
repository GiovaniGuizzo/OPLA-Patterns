package br.ufpr.inf.opla.patterns.operator;

import arquitetura.representation.Architecture;
import br.ufpr.inf.opla.patterns.models.DesignPattern;
import br.ufpr.inf.opla.patterns.models.Scope;
import br.ufpr.inf.opla.patterns.strategies.DesignPatternSelectionStrategy;
import br.ufpr.inf.opla.patterns.strategies.ScopeSelectionStrategy;
import br.ufpr.inf.opla.patterns.strategies.defaultstrategy.RandomDesignPatternSelection;
import br.ufpr.inf.opla.patterns.strategies.defaultstrategy.RandomScopeSelection;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.log4j.Priority;

public class DesignPatternsMutationOperator {

    private static final Logger LOGGER = LogManager.getLogger(DesignPatternsMutationOperator.class);

    public Architecture mutateArchitecture(Architecture architecture) {
        RandomScopeSelection rss = new RandomScopeSelection();
        RandomDesignPatternSelection rdps = new RandomDesignPatternSelection();
        return mutateArchitecture(architecture, rss, rdps);
    }

    public Architecture mutateArchitecture(Architecture architecture, ScopeSelectionStrategy scopeSelectionStartegy, DesignPatternSelectionStrategy designPatternSelectionStrategy) {
        DesignPattern designPattern = designPatternSelectionStrategy.selectDesignPattern();
        Scope scope = scopeSelectionStartegy.selectScope(architecture);
        if (designPattern.randomlyVerifyAsPSOrPSPLA(scope)) {
            if (designPattern.apply(scope)) {
                LOGGER.log(Priority.INFO,
                        "Design pattern " + designPattern.getName() + " applied to scope " + scope.getElements().toArray().toString() + " successfully!");
            }
        }
        return architecture;
    }

}
