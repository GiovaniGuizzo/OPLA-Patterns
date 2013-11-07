package br.ufpr.inf.opla.patterns.repositories;

import arquitetura.builders.ArchitectureBuilder;
import arquitetura.representation.Architecture;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ArchitectureRepositoryFlyweight {

    public static final String[] STRATEGY_MODELS = new String[]{
        "test/br/ufpr/inf/opla/patterns/resources/strategy/Verify.uml",
        "test/br/ufpr/inf/opla/patterns/resources/strategy/Verify2.uml",
        "test/br/ufpr/inf/opla/patterns/resources/strategy/Verify3.uml",
        "test/br/ufpr/inf/opla/patterns/resources/strategy/Apply1.uml"
    };

    public static final String[] OTHER_MODELS = new String[]{
        "test/br/ufpr/inf/opla/patterns/resources/other/Model1.uml",
        "test/br/ufpr/inf/opla/patterns/resources/other/Model2.uml",
        "test/br/ufpr/inf/opla/patterns/resources/other/Model3.uml"
    };

    public static final String[] OUTPUT = new String[]{
        "Output1",
        "Output2"
    };

    private static final ArchitectureRepositoryFlyweight INSTANCE = new ArchitectureRepositoryFlyweight();
    private final HashMap<String, Architecture> models;
    private final ArchitectureBuilder architectureBuilder;

    private ArchitectureRepositoryFlyweight() {
        this.models = new HashMap<>();
        this.architectureBuilder = new ArchitectureBuilder();
    }

    public static ArchitectureRepositoryFlyweight getInstance() {
        return INSTANCE;
    }

    public Architecture getArchitecture(String path) {
        Architecture architecture = models.get(path);
        if (architecture == null) {
            try {
                architecture = architectureBuilder.create(path);
                models.put(path, architecture);
            } catch (Exception ex) {
                Logger.getLogger(ArchitectureRepositoryFlyweight.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return architecture;
    }

    public void clearArchitecture(String path) {
        models.remove(path);
    }

}
