package br.ufpr.inf.opla.patterns.defaultstrategy;

import br.ufpr.inf.opla.patterns.models.DesignPattern;
import br.ufpr.inf.opla.patterns.strategies.DesignPatternSelectionStrategy;
import java.util.Random;

public class RandomDesignPatternSelection implements DesignPatternSelectionStrategy {

    @Override
    public DesignPattern selectDesignPattern() {
        int index = new Random().nextInt(DesignPattern.FEASIBLE.length);
        return DesignPattern.FEASIBLE[index];
    }

}