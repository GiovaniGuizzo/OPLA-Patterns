/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ufpr.inf.opla.patterns.designpatterns;

import arquitetura.representation.Architecture;
import arquitetura.representation.Class;
import arquitetura.representation.Element;
import arquitetura.representation.Interface;
import arquitetura.representation.Patterns;
import arquitetura.representation.relationship.DependencyRelationship;
import arquitetura.representation.relationship.Relationship;
import arquitetura.representation.relationship.UsageRelationship;
import br.ufpr.inf.opla.patterns.models.Scope;
import br.ufpr.inf.opla.patterns.models.ps.impl.PSMediator;
import br.ufpr.inf.opla.patterns.repositories.ArchitectureRepository;
import br.ufpr.inf.opla.patterns.strategies.scopeselection.ScopeSelectionStrategy;
import br.ufpr.inf.opla.patterns.strategies.scopeselection.impl.WholeArchitectureScopeSelection;
import br.ufpr.inf.opla.patterns.util.ElementUtil;
import br.ufpr.inf.opla.patterns.util.RelationshipUtil;
import java.util.ArrayList;
import java.util.List;
import main.GenerateArchitecture;
import org.junit.Assert;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

/**
 *
 * @author giovaniguizzo
 */
public class MediatorTest {

    private final Mediator mediator;
    private final ScopeSelectionStrategy scopeSelectionStrategy;

    public MediatorTest() {
        this.mediator = Mediator.getInstance();
        this.scopeSelectionStrategy = new WholeArchitectureScopeSelection();
    }

    @Test
    public void testVerifyPS() {
        String model = ArchitectureRepository.OTHER_MODELS[7];
        Architecture architecture = ArchitectureRepository.getArchitecture(model);

        Scope scope = scopeSelectionStrategy.selectScope(architecture, Patterns.MEDIATOR);

        Assert.assertTrue(mediator.verifyPS(scope));
        Assert.assertEquals(7, scope.getPSs().get(0).getParticipants().size());
        Assert.assertEquals("action", ((PSMediator) scope.getPSs().get(0)).getConcern().getName());
    }

    @Test
    public void testVerifyPS2() {
        String model = ArchitectureRepository.OTHER_MODELS[7];
        Architecture architecture = ArchitectureRepository.getArchitecture(model);

        Class klass2 = architecture.findClassByName("Class2").get(0);
        Class klass4 = architecture.findClassByName("Class4").get(0);
        Class klass6 = architecture.findClassByName("Class6").get(0);
        Class klass8 = architecture.findClassByName("Class8").get(0);
        Interface interface9 = architecture.findInterfaceByName("Class9");

        Scope scope = new Scope();
        scope.getElements().add(interface9);
        scope.getElements().add(klass8);
        scope.getElements().add(klass6);
        scope.getElements().add(klass4);
        Assert.assertFalse(mediator.verifyPS(scope));

        scope.getElements().add(klass2);
        Assert.assertTrue(mediator.verifyPS(scope));

    }

    @Test
    public void testApply() {
        String model = ArchitectureRepository.OTHER_MODELS[7];
        Architecture architecture = ArchitectureRepository.getArchitecture(model);

        Scope scope = scopeSelectionStrategy.selectScope(architecture, Patterns.MEDIATOR);

        Assert.assertTrue(mediator.verifyPS(scope));

        Assert.assertTrue(mediator.apply(scope));

        Class klass1 = architecture.findClassByName("Class1").get(0);
        Class klass2 = architecture.findClassByName("Class2").get(0);
        Class klass3 = architecture.findClassByName("Class3").get(0);
        Class klass4 = architecture.findClassByName("Class4").get(0);
        Class klass5 = architecture.findClassByName("Class5").get(0);
        Class klass6 = architecture.findClassByName("Class6").get(0);
        Class klass7 = architecture.findClassByName("Class7").get(0);
        Class klass8 = architecture.findClassByName("Class8").get(0);
        Interface interface9 = architecture.findInterfaceByName("Class9");
        Class adapter = architecture.findClassByName("Class9Adapter").get(0);

        Interface colleagueInterface = architecture.findInterfaceByName("ActionColleague");
        Interface mediatorInterface = architecture.findInterfaceByName("ActionMediator");
        Class mediatorClass = architecture.findClassByName("ActionMediatorImpl").get(0);
        Class eventOfInterest = architecture.findClassByName("EventOfInterest").get(0);

        assertFalse(ElementUtil.isTypeOf(klass1, colleagueInterface));
        assertTrue(ElementUtil.isTypeOf(klass2, colleagueInterface));
        assertTrue(ElementUtil.isTypeOf(klass3, colleagueInterface));
        assertTrue(ElementUtil.isTypeOf(klass4, colleagueInterface));
        assertTrue(ElementUtil.isTypeOf(klass5, colleagueInterface));
        assertFalse(ElementUtil.isTypeOf(klass6, colleagueInterface));
        assertFalse(ElementUtil.isTypeOf(klass7, colleagueInterface));
        assertTrue(ElementUtil.isTypeOf(klass8, colleagueInterface));
        assertFalse(ElementUtil.isTypeOf(interface9, colleagueInterface));
        assertTrue(ElementUtil.isTypeOf(adapter, colleagueInterface));

        List<Element> usedElements = new ArrayList<>();
        usedElements.add(colleagueInterface);
        usedElements.add(mediatorInterface);

        Assert.assertTrue(usedElements.contains(RelationshipUtil.getClientElementFromRelationship(ElementUtil.getRelationships(eventOfInterest).get(0))));
        Assert.assertTrue(usedElements.contains(RelationshipUtil.getClientElementFromRelationship(ElementUtil.getRelationships(eventOfInterest).get(1))));

        List<Element> shouldBeUsed = new ArrayList<>();
        shouldBeUsed.add(klass2);
        shouldBeUsed.add(klass3);
        shouldBeUsed.add(klass4);
        shouldBeUsed.add(klass5);
        shouldBeUsed.add(klass8);
        shouldBeUsed.add(adapter);

        for (Relationship relationship : ElementUtil.getRelationships(mediatorClass)) {
            if (relationship instanceof UsageRelationship || relationship instanceof DependencyRelationship) {
                Assert.assertTrue(shouldBeUsed.contains(RelationshipUtil.getUsedElementFromRelationship(relationship)));
            }
        }

        List<Element> shouldNotBeUsed = new ArrayList<>();
        shouldNotBeUsed.add(klass1);
        shouldNotBeUsed.add(klass6);
        shouldNotBeUsed.add(klass7);
        shouldNotBeUsed.add(interface9);

        for (Relationship relationship : ElementUtil.getRelationships(mediatorClass)) {
            if (relationship instanceof UsageRelationship || relationship instanceof DependencyRelationship) {
                Assert.assertTrue(!shouldNotBeUsed.contains(RelationshipUtil.getUsedElementFromRelationship(relationship)));
            }
        }

        GenerateArchitecture generateArchitecture = new GenerateArchitecture();
        generateArchitecture.generate(architecture, ArchitectureRepository.OUTPUT[8]);
    }

    @Test
    public void testApply2() {
        String model = ArchitectureRepository.MEDIATOR_MODELS[0];
        Architecture architecture = ArchitectureRepository.getArchitecture(model);

        Scope scope = scopeSelectionStrategy.selectScope(architecture, Patterns.MEDIATOR);

        Assert.assertTrue(mediator.verifyPS(scope));

        Assert.assertTrue(mediator.apply(scope));

        GenerateArchitecture generateArchitecture = new GenerateArchitecture();
        generateArchitecture.generate(architecture, ArchitectureRepository.OUTPUT[9]);
    }

}
