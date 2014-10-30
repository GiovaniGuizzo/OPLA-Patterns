/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ufpr.inf.opla.patterns.metricas;

import arquitetura.representation.Architecture;
import arquitetura.representation.Class;
import arquitetura.representation.Concern;
import arquitetura.representation.ConcernHolder;
import arquitetura.representation.Package;
import arquitetura.representation.ParameterMethod;
import arquitetura.representation.Patterns;
import br.ufpr.inf.opla.patterns.designpatterns.Bridge;
import br.ufpr.inf.opla.patterns.models.Scope;
import br.ufpr.inf.opla.patterns.repositories.ArchitectureRepository;
import br.ufpr.inf.opla.patterns.strategies.scopeselection.impl.WholeArchitectureScopeSelection;
import br.ufpr.inf.opla.patterns.util.RelationshipUtil;
import java.util.ArrayList;
import jmetal.core.Solution;
import jmetal.problems.OPLA;
import main.GenerateArchitecture;

/**
 *
 * @author giovani
 */
public class BridgeDissertacao {

    public static void main(String[] args) throws Exception {
        Architecture architecture = new Architecture("Bridge");
        ConcernHolder.INSTANCE.allowedConcerns().add(new Concern("play"));
        ArchitectureRepository.setCurrentArchitecture(architecture);

        OPLA opla = new OPLA("");
        opla.architecture_ = architecture;

        Package aPackage = architecture.createPackage("FooPackage");

        Class client = aPackage.createClass("Client", false);

        Class algA = aPackage.createClass("ClassA", false);
        algA.addConcern("play");
        algA.createMethod("foo", "int", false, new ArrayList<ParameterMethod>());

        Class algB = aPackage.createClass("ClassB", false);
        algB.addConcern("play");
        algB.createMethod("foo", "int", false, new ArrayList<ParameterMethod>());

        RelationshipUtil.createNewDependencyRelationship("usage", client, algA);
        RelationshipUtil.createNewDependencyRelationship("usage", client, algB);

        Solution solution = new Solution(opla);
        opla.evaluate(solution);

        System.out.println("Before: " + solution.getObjective(0) + ", " + solution.getObjective(1));

        WholeArchitectureScopeSelection scopeSelection = new WholeArchitectureScopeSelection();
        Scope scope = scopeSelection.selectScope(architecture, Patterns.BRIDGE);

        Bridge bridge = Bridge.getInstance();
        bridge.verifyPS(scope);

        bridge.apply(scope);

        solution = new Solution(opla);
        opla.evaluate(solution);

        System.out.println("After: " + solution.getObjective(0) + ", " + solution.getObjective(1));

        GenerateArchitecture generateArchitecture = new GenerateArchitecture();
        generateArchitecture.generate(architecture, "Teste");
    }

}
