package br.ufpr.inf.opla.patterns.models;

import br.ufpr.inf.opla.patterns.designpatterns.Bridge;
import br.ufpr.inf.opla.patterns.designpatterns.Facade;
import br.ufpr.inf.opla.patterns.designpatterns.Mediator;
import br.ufpr.inf.opla.patterns.designpatterns.Strategy;
import java.util.Random;

public abstract class DesignPattern {

    public static final DesignPattern[] FEASIBLE = new DesignPattern[]{
        Strategy.getInstance(),
        Bridge.getInstance(),
        Facade.getInstance(),
        Mediator.getInstance()
    };

    private final String name;
    private final String category;
    private final Random random;

    public DesignPattern(String name, String category) {
        this.random = new Random();
        this.name = name;
        this.category = category;
    }

    public String getName() {
        return name;
    }

    public String getCategory() {
        return category;
    }

    public boolean verifyAsPSOrPSPLA(Scope scope) {
        double PLAProbability = random.nextDouble();
        if (random.nextDouble() < PLAProbability) {
            return verifyPSPLA(scope);
        } else {
            return verifyPS(scope);
        }
    }

    protected abstract boolean verifyPS(Scope scope);

    protected abstract boolean verifyPSPLA(Scope scope);

    public abstract boolean apply(Scope scope);

}
