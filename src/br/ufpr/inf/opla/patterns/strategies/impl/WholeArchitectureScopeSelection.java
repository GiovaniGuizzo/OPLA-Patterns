package br.ufpr.inf.opla.patterns.strategies.impl;

import arquitetura.representation.Architecture;
import br.ufpr.inf.opla.patterns.models.Scope;
import br.ufpr.inf.opla.patterns.strategies.ScopeSelectionStrategy;

public class WholeArchitectureScopeSelection implements ScopeSelectionStrategy{

    @Override
    public Scope selectScope(Architecture architecture) {
        Scope scope = new Scope();
        scope.getElements().addAll(architecture.getElements());
        return scope;
    }

}
