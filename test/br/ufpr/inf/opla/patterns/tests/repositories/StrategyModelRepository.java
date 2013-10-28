package br.ufpr.inf.opla.patterns.tests.repositories;

import arquitetura.builders.ArchitectureBuilder;
import arquitetura.representation.Architecture;
import java.util.logging.Level;
import java.util.logging.Logger;

public class StrategyModelRepository {

    private static final StrategyModelRepository INSTANCE = new StrategyModelRepository();
    private String dir;
    private Architecture model1;
    private Architecture model2;
    private Architecture model3;

    private StrategyModelRepository() {
        try {
            this.dir = "test/br/ufpr/inf/opla/patterns/resources/strategy/";
            this.model1 = new ArchitectureBuilder().create(dir + "Verify.uml");
            this.model2 = new ArchitectureBuilder().create(dir + "Verify2.uml");
            this.model3 = new ArchitectureBuilder().create(dir + "Verify3.uml");
        } catch (Exception ex) {
            Logger.getLogger(StrategyModelRepository.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static StrategyModelRepository getInstance() {
        return INSTANCE;
    }

    public Architecture getModel1() {
        return model1;
    }

    public Architecture getModel2() {
        return model2;
    }

    public Architecture getModel3() {
        return model3;
    }

}
