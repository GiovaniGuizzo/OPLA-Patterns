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
import br.ufpr.inf.opla.patterns.designpatterns.Mediator;
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
public class MediatorDissertacao {

    public static void main(String[] args) throws Exception {
        Architecture architecture = new Architecture("Mediator");
        ConcernHolder.INSTANCE.allowedConcerns().add(new Concern("play"));
        ArchitectureRepository.setCurrentArchitecture(architecture);

        OPLA opla = new OPLA("");
        opla.architecture_ = architecture;

        Package aPackage = architecture.createPackage("FooPackage");

        Class algA = aPackage.createClass("ClassA", false);
        algA.addConcern("play");
        algA.createMethod("foo", "int", false, new ArrayList<ParameterMethod>());

        Class algB = aPackage.createClass("ClassB", false);
        algB.addConcern("play");
        algB.createMethod("foo", "int", false, new ArrayList<ParameterMethod>());

        Class algC = aPackage.createClass("ClassC", false);
        algC.addConcern("play");
        algC.createMethod("foo", "int", false, new ArrayList<ParameterMethod>());

        Class algD = aPackage.createClass("ClassD", false);
        algD.addConcern("play");
        algD.createMethod("foo", "int", false, new ArrayList<ParameterMethod>());

        RelationshipUtil.createNewDependencyRelationship("usage", algA, algB);
        RelationshipUtil.createNewDependencyRelationship("usage", algA, algD);

        RelationshipUtil.createNewDependencyRelationship("usage", algB, algC);

        RelationshipUtil.createNewDependencyRelationship("usage", algC, algD);
        RelationshipUtil.createNewDependencyRelationship("usage", algC, algA);

        RelationshipUtil.createNewDependencyRelationship("usage", algD, algB);

        Solution solution = new Solution(opla);
        opla.evaluate(solution);

        System.out.println("Before: " + solution.getObjective(0) + ", " + solution.getObjective(1));

        WholeArchitectureScopeSelection scopeSelection = new WholeArchitectureScopeSelection();
        Scope scope = scopeSelection.selectScope(architecture, Patterns.MEDIATOR);

        Mediator mediator = Mediator.getInstance();
        mediator.verifyPS(scope);

        mediator.apply(scope);

        Class eventOfInterest = architecture.findClassByName("EventOfInterest").get(0);
        eventOfInterest.setNamespace("model::EventOfInterest");
        architecture.moveElementToPackage(eventOfInterest, aPackage);

        solution = new Solution(opla);
        opla.evaluate(solution);

        System.out.println("After: " + solution.getObjective(0) + ", " + solution.getObjective(1));

        GenerateArchitecture generateArchitecture = new GenerateArchitecture();
        generateArchitecture.generate(architecture, "Teste");
    }

}
