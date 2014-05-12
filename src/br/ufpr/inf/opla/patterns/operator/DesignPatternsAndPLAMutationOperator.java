package br.ufpr.inf.opla.patterns.operator;

import br.ufpr.inf.opla.patterns.strategies.DesignPatternSelectionStrategy;
import br.ufpr.inf.opla.patterns.strategies.ScopeSelectionStrategy;
import java.util.HashMap;
import jmetal.core.Solution;
import jmetal.operators.mutation.PLAFeatureMutation;
import jmetal.util.PseudoRandom;

public class DesignPatternsAndPLAMutationOperator extends DesignPatternsMutationOperator {

    private final PLAFeatureMutation pLAFeatureMutation;

    public DesignPatternsAndPLAMutationOperator(HashMap<String, Object> parameters, ScopeSelectionStrategy scopeSelectionStrategy, DesignPatternSelectionStrategy designPatternSelectionStrategy) {
        super(parameters, scopeSelectionStrategy, designPatternSelectionStrategy);
        pLAFeatureMutation = new PLAFeatureMutation(parameters);
    }

    @Override
    protected void hookMutation(Solution solution, Double probability) throws Exception {
        int random = PseudoRandom.randInt(0, 6);
        if (random == 0) {
            super.hookMutation(solution, probability);
        } else {
            pLAFeatureMutation.execute(solution);
        }
    }

}
