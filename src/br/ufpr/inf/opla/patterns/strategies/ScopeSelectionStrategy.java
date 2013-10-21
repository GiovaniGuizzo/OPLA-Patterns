package br.ufpr.inf.opla.patterns.strategies;

import br.ufpr.inf.opla.patterns.models.Scope;
import arquitetura.representation.Architecture;

public interface ScopeSelectionStrategy {
    
    public Scope selectScope(Architecture architecture);

}