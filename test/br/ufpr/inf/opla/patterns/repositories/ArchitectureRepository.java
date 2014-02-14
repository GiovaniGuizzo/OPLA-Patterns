package br.ufpr.inf.opla.patterns.repositories;

import arquitetura.builders.ArchitectureBuilder;
import arquitetura.representation.Architecture;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ArchitectureRepository {

    public static final String[] STRATEGY_MODELS = new String[]{
        "test/br/ufpr/inf/opla/patterns/resources/strategy/Verify.uml",
        "test/br/ufpr/inf/opla/patterns/resources/strategy/Verify2.uml",
        "test/br/ufpr/inf/opla/patterns/resources/strategy/Verify3.uml",
        "test/br/ufpr/inf/opla/patterns/resources/strategy/Apply1.uml",
        "test/br/ufpr/inf/opla/patterns/resources/strategy/Apply2.uml"
    };

    public static final String[] BRIDGE_MODELS = new String[]{
        "test/br/ufpr/inf/opla/patterns/resources/bridge/Verify.uml",
        "test/br/ufpr/inf/opla/patterns/resources/bridge/Apply.uml"
    };

    public static final String[] OTHER_MODELS = new String[]{
        "test/br/ufpr/inf/opla/patterns/resources/other/Model1.uml",
        "test/br/ufpr/inf/opla/patterns/resources/other/Model2.uml",
        "test/br/ufpr/inf/opla/patterns/resources/other/Model3.uml",
        "test/br/ufpr/inf/opla/patterns/resources/other/Model4.uml",
        "test/br/ufpr/inf/opla/patterns/resources/other/Model5.uml",
        "test/br/ufpr/inf/opla/patterns/resources/other/Model6.uml"
    };

    public static final String[] OUTPUT = new String[]{
        "Output1",
        "Output2",
        "Output3",
        "Output4",
        "Output5",
        "Output6",
        "Output7",
        "Output8",
        "Output9",
        "Output10"
    };

    private static final ArchitectureRepository INSTANCE = new ArchitectureRepository();

    public static ArchitectureRepository getInstance() {
        return INSTANCE;
    }
    private final ArchitectureBuilder architectureBuilder;

    private ArchitectureRepository() {
        this.architectureBuilder = new ArchitectureBuilder();
    }

    public Architecture getArchitecture(String path) {
        try {
            return architectureBuilder.create(path);
        } catch (Exception ex) {
            Logger.getLogger(ArchitectureRepository.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

}
