package br.ufpr.inf.opla.patterns.operator;

import arquitetura.representation.Architecture;
import br.ufpr.inf.opla.patterns.models.DesignPattern;
import br.ufpr.inf.opla.patterns.models.Scope;
import br.ufpr.inf.opla.patterns.strategies.DesignPatternSelectionStrategy;
import br.ufpr.inf.opla.patterns.strategies.ScopeSelectionStrategy;
import br.ufpr.inf.opla.patterns.strategies.defaultstrategy.RandomDesignPatternSelection;
import br.ufpr.inf.opla.patterns.strategies.defaultstrategy.RandomScopeSelection;

public class PatternsMutationOperator {
    
    public Architecture mutateArchitecture(Architecture architecture){
        RandomScopeSelection rss = new RandomScopeSelection();
        RandomDesignPatternSelection rdps = new RandomDesignPatternSelection();
        return mutateArchitecture(architecture, rss, rdps);
    }
    
    public Architecture mutateArchitecture(Architecture architecture, ScopeSelectionStrategy scopeSelectionStartegy, DesignPatternSelectionStrategy designPatternSelectionStrategy){
        DesignPattern designPattern = designPatternSelectionStrategy.selectDesignPattern();
        Scope scope = scopeSelectionStartegy.selectScope(architecture);
        if(designPattern.randomlyVerifyAsPSOrPSPLA(scope)){
            designPattern.apply(scope);
        }
        return architecture;
    }
    
}