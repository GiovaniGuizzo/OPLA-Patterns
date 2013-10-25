package br.ufpr.inf.opla.patterns.strategies;

import arquitetura.representation.Architecture;
import br.ufpr.inf.opla.patterns.models.Scope;

public interface ScopeSelectionStrategy {
    
    public Scope selectScope(Architecture architecture);

}