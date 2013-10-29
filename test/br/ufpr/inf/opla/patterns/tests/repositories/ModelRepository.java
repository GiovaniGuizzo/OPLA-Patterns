package br.ufpr.inf.opla.patterns.tests.repositories;

import arquitetura.builders.ArchitectureBuilder;
import arquitetura.representation.Architecture;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ModelRepository {

    private final List<Architecture> modelList = new ArrayList<>();

    public void loadModel(String modelPath) {
        try {
            modelList.add(new ArchitectureBuilder().create(modelPath));
        } catch (Exception ex) {
            Logger.getLogger(ModelRepository.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public Architecture getModel(int number) {
        return modelList.get(number);
    }

}
