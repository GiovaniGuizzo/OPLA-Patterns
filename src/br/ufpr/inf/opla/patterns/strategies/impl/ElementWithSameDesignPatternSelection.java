package br.ufpr.inf.opla.patterns.strategies.impl;

import arquitetura.representation.Architecture;
import arquitetura.representation.Element;
import arquitetura.representation.Patterns;
import br.ufpr.inf.opla.patterns.models.Scope;
import br.ufpr.inf.opla.patterns.strategies.ScopeSelectionStrategy;
import br.ufpr.inf.opla.patterns.strategies.defaultstrategy.RandomScopeSelection;
import br.ufpr.inf.opla.patterns.util.ElementUtil;
import java.util.Set;

public class ElementWithSameDesignPatternSelection implements ScopeSelectionStrategy {

    private final ScopeSelectionStrategy randomScopeSelection;

    public ElementWithSameDesignPatternSelection() {
        randomScopeSelection = new RandomScopeSelection();
    }

    @Override
    public Scope selectScope(Architecture architecture, Patterns designPattern) {
        Scope scope = randomScopeSelection.selectScope(architecture, designPattern);
        for (int i = 0; i < scope.getElements().size(); i++) {
            Element element = scope.getElements().get(i);
            Set<String> appliedDesignPatterns = ElementUtil.getAppliedDesignPatterns(element);
            if ((appliedDesignPatterns.size() == 1 && !appliedDesignPatterns.contains(designPattern.getName()))
                    || (appliedDesignPatterns.size() == 2 && !appliedDesignPatterns.contains(designPattern.getName()) && !appliedDesignPatterns.contains(Patterns.ADAPTER.getName()))
                    || (appliedDesignPatterns.size() > 2)) {
                scope.getElements().remove(i);
                i--;
            }
        }
        return scope;
    }

}
