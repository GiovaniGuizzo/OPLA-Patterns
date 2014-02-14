package br.ufpr.inf.opla.patterns.designpatterns;

import arquitetura.representation.Architecture;
import arquitetura.representation.Element;
import arquitetura.representation.Interface;
import arquitetura.representation.Variant;
import arquitetura.representation.relationship.RealizationRelationship;
import arquitetura.representation.relationship.Relationship;
import arquitetura.representation.relationship.UsageRelationship;
import br.ufpr.inf.opla.patterns.models.Scope;
import br.ufpr.inf.opla.patterns.models.ps.PS;
import br.ufpr.inf.opla.patterns.models.ps.PSPLA;
import br.ufpr.inf.opla.patterns.models.ps.impl.PSPLAStrategy;
import br.ufpr.inf.opla.patterns.models.ps.impl.PSStrategy;
import br.ufpr.inf.opla.patterns.repositories.ArchitectureRepository;
import br.ufpr.inf.opla.patterns.strategies.ScopeSelectionStrategy;
import br.ufpr.inf.opla.patterns.strategies.impl.WholeArchitectureScopeSelection;
import br.ufpr.inf.opla.patterns.util.ElementUtil;
import br.ufpr.inf.opla.patterns.util.MethodUtil;
import br.ufpr.inf.opla.patterns.util.RelationshipUtil;
import java.util.List;
import main.GenerateArchitecture;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

public class StrategyTest {

    private final Strategy strategy;
    private final ScopeSelectionStrategy scopeSelectionStrategy;

    public StrategyTest() {
        this.strategy = Strategy.getInstance();
        this.scopeSelectionStrategy = new WholeArchitectureScopeSelection();
    }

    /**
     * Verifies if there are some PSs in the scope. There should be 3 PSs, with 4 members each (1 context and 3 algorithms).
     */
    @Test
    public void verifyPSTest() {
        Scope scope = scopeSelectionStrategy.selectScope(ArchitectureRepository.getArchitecture(ArchitectureRepository.STRATEGY_MODELS[0]));

        assertEquals(7, scope.getElements().size());

        boolean verifyPS = strategy.verifyPS(scope);
        assertTrue(verifyPS);

        List<PS> psList = scope.getPSs(strategy);
        assertEquals(3, psList.size());

        for (PS ps : psList) {
            PSStrategy psStrategy = (PSStrategy) ps;
            assertEquals(4, psStrategy.getParticipants().size());
            assertEquals(3, psStrategy.getAlgorithmFamily().getParticipants().size());
            assertEquals(1, psStrategy.getContexts().size());
        }
    }

    /**
     * Verifies if the Algorithm Family "sort" is the best one.
     */
    @Test
    public void verifyPSTest2() {
        Scope scope = scopeSelectionStrategy.selectScope(ArchitectureRepository.getArchitecture(ArchitectureRepository.STRATEGY_MODELS[1]));

        assertTrue(strategy.verifyPS(scope));

        List<PS> psList = scope.getPSs(strategy);

        PSStrategy pSStrategy = (PSStrategy) psList.get(0);
        assertEquals("sort", pSStrategy.getAlgorithmFamily().getName());
        assertEquals(4, pSStrategy.getAlgorithmFamily().getParticipants().size());
        assertEquals(2, pSStrategy.getContexts().size());
    }

    /**
     * Verifies if there is some PSs-PLA in the scope. There should be 3 PSs-PLA, with 4 members each (1 context and 3 algorithms).
     */
    @Test
    public void verifyPSPLATest() {
        Scope scope = scopeSelectionStrategy.selectScope(ArchitectureRepository.getArchitecture(ArchitectureRepository.STRATEGY_MODELS[0]));

        assertEquals(7, scope.getElements().size());

        boolean verifyPSPLA = strategy.verifyPSPLA(scope);
        assertTrue(verifyPSPLA);

        List<PSPLA> psPLAList = scope.getPSsPLA(strategy);
        assertEquals(3, psPLAList.size());

        for (PSPLA psPLA : psPLAList) {
            PSPLAStrategy psPLAStrategy = (PSPLAStrategy) psPLA;
            assertEquals(4, psPLAStrategy.getParticipants().size());
            assertEquals(3, psPLAStrategy.getAlgorithmFamily().getParticipants().size());
            assertEquals(1, psPLAStrategy.getContexts().size());
        }
    }

    /**
     * Verifies if there is no PSs-PLA in the scope.
     */
    @Test
    public void verifyPSPLATest2() {
        Scope scope = scopeSelectionStrategy.selectScope(ArchitectureRepository.getArchitecture(ArchitectureRepository.STRATEGY_MODELS[1]));

        assertEquals(7, scope.getElements().size());

        boolean verifyPSPLA = strategy.verifyPSPLA(scope);
        assertFalse(verifyPSPLA);
    }

    /**
     * Tests the Strategy application.
     */
    @Test
    public void applyTest() {
        String model = ArchitectureRepository.STRATEGY_MODELS[3];
        Architecture architecture = ArchitectureRepository.getArchitecture(model);
        Scope scope = scopeSelectionStrategy.selectScope(architecture);
        boolean verifyPS = strategy.verifyPS(scope);
        assertTrue(verifyPS);
        boolean apply = strategy.apply(scope);
        assertTrue(apply);
        Element element = null;
        element = architecture.findClassByName("Class1").get(0);
        assertEquals("Class1", element.getName());
        assertEquals(1, ElementUtil.getRelationships(element).size());

        Interface strategyInterface = null;
        strategyInterface = architecture.findInterfaceByName("InterfaceDeTeste");
        assertEquals("InterfaceDeTeste", strategyInterface.getName());

        Relationship usage = ElementUtil.getRelationships(element).get(0);
        Element usedElementFromRelationship = RelationshipUtil.getUsedElementFromRelationship(usage);
        assertEquals(strategyInterface, usedElementFromRelationship);

        arquitetura.representation.Class element2 = null;
        element2 = architecture.findClassByName("BubbleSort").get(0);
        assertEquals(1, element2.getAllMethods().size());

        arquitetura.representation.Class element3 = null;
        element3 = architecture.findClassByName("QuickSort").get(0);
        assertEquals(2, element3.getAllMethods().size());

        GenerateArchitecture generateArchitecture = new GenerateArchitecture();
        generateArchitecture.generate(architecture, ArchitectureRepository.OUTPUT[0]);
    }

    @Test
    public void applyTest2() {
        String model = ArchitectureRepository.STRATEGY_MODELS[0];
        Architecture architecture = ArchitectureRepository.getArchitecture(model);
        Scope scope = scopeSelectionStrategy.selectScope(architecture);
        boolean verifyPS = strategy.verifyPS(scope);
        assertTrue(verifyPS);
        boolean apply = strategy.apply(scope);
        assertTrue(apply);

        Element element = null;
        element = architecture.findClassByName("ContextClass").get(0);
        assertEquals("ContextClass", element.getName());
        assertEquals(1, ElementUtil.getRelationships(element).size());

        Element strategyInterface = null;
        strategyInterface = architecture.findInterfaceByName("SortStrategy");
        assertEquals("SortStrategy", strategyInterface.getName());
        assertEquals(1, MethodUtil.getAllMethodsFromElement(strategyInterface).size());
        assertEquals("model::PackageSort", strategyInterface.getNamespace());

        Relationship usage = ElementUtil.getRelationships(element).get(0);
        Element usedElementFromRelationship = RelationshipUtil.getUsedElementFromRelationship(usage);
        assertEquals(strategyInterface, usedElementFromRelationship);
        assertEquals(1, strategyInterface.getOwnConcerns().size());
        assertNotNull(strategyInterface.getVariationPoint());
        assertNull(element.getVariationPoint());
        assertTrue(strategyInterface.getVariationPoint().getVariationPointElement().equals(strategyInterface));
        for (Variant variant : strategyInterface.getVariationPoint().getVariants()) {
            assertTrue(variant.getVariationPoints().contains(strategyInterface.getVariationPoint()));
        }
        assertEquals(1, strategyInterface.getOwnConcerns().size());

        GenerateArchitecture generateArchitecture = new GenerateArchitecture();
        generateArchitecture.generate(architecture, ArchitectureRepository.OUTPUT[1]);
    }

    @Test
    public void applyTest3() {
        String model = ArchitectureRepository.STRATEGY_MODELS[4];
        Architecture architecture = ArchitectureRepository.getArchitecture(model);
        Scope scope = scopeSelectionStrategy.selectScope(architecture);
        boolean verifyPS = strategy.verifyPS(scope);
        assertTrue(verifyPS);
        boolean apply = strategy.apply(scope);
        assertTrue(apply);

        Interface strategyInterface = null;
        strategyInterface = architecture.findInterfaceByName("SortStrategy");
        assertEquals("SortStrategy", strategyInterface.getName());
        assertEquals(3, MethodUtil.getAllMethodsFromElement(strategyInterface).size());
        assertEquals(2, strategyInterface.getOwnConcerns().size());

        arquitetura.representation.Class adapterClass = null;
        adapterClass = architecture.findClassByName("Sort2Adapter").get(0);
        assertEquals("Sort2Adapter", adapterClass.getName());
        assertEquals(3, MethodUtil.getAllMethodsFromElement(adapterClass).size());
        assertEquals(2, adapterClass.getOwnConcerns().size());

        Interface adaptee = null;
        adaptee = architecture.findInterfaceByName("Sort2");
        assertEquals("Sort2", adaptee.getName());
        assertEquals(1, MethodUtil.getAllMethodsFromElement(adaptee).size());
        assertEquals(1, adaptee.getOwnConcerns().size());

        RealizationRelationship realization;
        UsageRelationship usage;
        if (ElementUtil.getRelationships(adapterClass).get(0) instanceof RealizationRelationship) {
            realization = (RealizationRelationship) ElementUtil.getRelationships(adapterClass).get(0);
            usage = (UsageRelationship) ElementUtil.getRelationships(adapterClass).get(1);
        } else {
            realization = (RealizationRelationship) ElementUtil.getRelationships(adapterClass).get(1);
            usage = (UsageRelationship) ElementUtil.getRelationships(adapterClass).get(0);
        }
        assertEquals(strategyInterface, realization.getSupplier());

        assertEquals(adaptee, usage.getSupplier());

        GenerateArchitecture generateArchitecture = new GenerateArchitecture();
        generateArchitecture.generate(architecture, ArchitectureRepository.OUTPUT[2]);
    }
}
