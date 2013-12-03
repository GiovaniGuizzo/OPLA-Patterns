/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ufpr.inf.opla.patterns.util;

import arquitetura.representation.Architecture;
import arquitetura.representation.Interface;
import br.ufpr.inf.opla.patterns.designpatterns.Strategy;
import br.ufpr.inf.opla.patterns.models.AlgorithmFamily;
import br.ufpr.inf.opla.patterns.models.Scope;
import br.ufpr.inf.opla.patterns.models.ps.PSPLA;
import br.ufpr.inf.opla.patterns.models.ps.impl.PSPLAStrategy;
import br.ufpr.inf.opla.patterns.models.ps.impl.PSStrategy;
import br.ufpr.inf.opla.patterns.repositories.ArchitectureRepository;
import br.ufpr.inf.opla.patterns.strategies.impl.WholeArchitectureScopeSelection;
import java.util.List;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

/**
 *
 * @author giovaniguizzo
 */
public class StrategyUtilTest {

    private final ArchitectureRepository architectureRepository;
    private final WholeArchitectureScopeSelection wholeArchitectureScopeSelection;
    private final Strategy strategy;

    public StrategyUtilTest() {
        this.architectureRepository = ArchitectureRepository.getInstance();
        this.wholeArchitectureScopeSelection = new WholeArchitectureScopeSelection();
        this.strategy = Strategy.getInstance();
    }

    /**
     * Test of getStrategyInterfaceFromAlgorithmFamily method, of class AlgorithmFamilyUtil.
     */
    @Test
    public void testGetStrategyInterfaceFromAlgorithmFamily() {
        Architecture architecture = architectureRepository.getArchitecture(ArchitectureRepository.STRATEGY_MODELS[2]);
        Scope scope = wholeArchitectureScopeSelection.selectScope(architecture);
        List<AlgorithmFamily> familiesFromScope = AlgorithmFamilyUtil.getFamiliesFromScope(scope);
        Interface aInterface = StrategyUtil.getStrategyInterfaceFromAlgorithmFamily(familiesFromScope.get(0));
        assertEquals("StrategyInterface", aInterface.getName());
    }

    /**
     * Test of createStrategyInterfaceForAlgorithmFamily method, of class AlgorithmFamilyUtil.
     */
    @Test
    public void testCreateStrategyInterfaceForAlgorithmFamily() {
        Architecture architecture = architectureRepository.getArchitecture(ArchitectureRepository.STRATEGY_MODELS[2]);
        Scope scope = wholeArchitectureScopeSelection.selectScope(architecture);
        List<AlgorithmFamily> familiesFromScope = AlgorithmFamilyUtil.getFamiliesFromScope(scope);
        AlgorithmFamily family = familiesFromScope.get(0);
        assertEquals("Class", family.getName());
        Interface aInterface = StrategyUtil.createStrategyInterfaceForAlgorithmFamily(family);
        assertEquals("ClassStrategy", aInterface.getName());
        assertEquals(3, aInterface.getOperations().size());
    }

    /**
     * Test of areTheAlgorithmFamilyAndContextsPartOfAVariability method, of class AlgorithmFamilyUtil.
     */
    @Test
    public void testAreTheAlgorithmFamilyAndContextsPartOfAVariability() {
        Scope scope = wholeArchitectureScopeSelection.selectScope(architectureRepository.getArchitecture(ArchitectureRepository.STRATEGY_MODELS[0]));

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

        PSPLAStrategy psPLAStrategy = (PSPLAStrategy) psPLAList.get(0);
        boolean result = StrategyUtil.areTheAlgorithmFamilyAndContextsPartOfAVariability(psPLAStrategy.getAlgorithmFamily(), psPLAStrategy.getContexts());
        assertTrue(result);

        scope = wholeArchitectureScopeSelection.selectScope(architectureRepository.getArchitecture(ArchitectureRepository.STRATEGY_MODELS[1]));
        assertEquals(7, scope.getElements().size());

        verifyPSPLA = strategy.verifyPSPLA(scope);
        assertFalse(verifyPSPLA);

        PSStrategy psStrategy = (PSStrategy) scope.getPSs(strategy).get(0);
        AlgorithmFamily psAlgorithmFamily = psStrategy.getAlgorithmFamily();
        assertEquals("sort", psAlgorithmFamily.getName());
        assertEquals(4, psAlgorithmFamily.getParticipants().size());
        result = StrategyUtil.areTheAlgorithmFamilyAndContextsPartOfAVariability(psAlgorithmFamily, psStrategy.getContexts());
        assertFalse(result);
    }
}
