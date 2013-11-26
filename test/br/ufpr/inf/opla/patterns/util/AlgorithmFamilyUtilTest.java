/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ufpr.inf.opla.patterns.util;

import arquitetura.representation.Architecture;
import arquitetura.representation.Interface;
import br.ufpr.inf.opla.patterns.models.AlgorithmFamily;
import br.ufpr.inf.opla.patterns.models.Scope;
import br.ufpr.inf.opla.patterns.repositories.ArchitectureRepository;
import br.ufpr.inf.opla.patterns.strategies.impl.WholeArchitectureScopeSelection;
import java.util.List;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 *
 * @author giovaniguizzo
 */
public class AlgorithmFamilyUtilTest {

    private final ArchitectureRepository architectureRepository;
    private final WholeArchitectureScopeSelection wholeArchitectureScopeSelection;

    public AlgorithmFamilyUtilTest() {
        this.architectureRepository = ArchitectureRepository.getInstance();
        wholeArchitectureScopeSelection = new WholeArchitectureScopeSelection();
    }

    /**
     * Test of getFamiliesFromScope method, of class AlgorithmFamilyUtil.
     */
    @Test
    public void testGetFamiliesFromScope() {
        Architecture architecture = architectureRepository.getArchitecture(ArchitectureRepository.STRATEGY_MODELS[0]);
        Scope scope = wholeArchitectureScopeSelection.selectScope(architecture);
        List<AlgorithmFamily> familiesFromScope = AlgorithmFamilyUtil.getFamiliesFromScope(scope);
        assertEquals(3, familiesFromScope.size());
    }

    /**
     * Test of getStrategyInterfaceFromAlgorithmFamily method, of class AlgorithmFamilyUtil.
     */
    @Test
    public void testGetStrategyInterfaceFromAlgorithmFamily() {
        Architecture architecture = architectureRepository.getArchitecture(ArchitectureRepository.STRATEGY_MODELS[2]);
        Scope scope = wholeArchitectureScopeSelection.selectScope(architecture);
        List<AlgorithmFamily> familiesFromScope = AlgorithmFamilyUtil.getFamiliesFromScope(scope);
        Interface aInterface = AlgorithmFamilyUtil.getStrategyInterfaceFromAlgorithmFamily(familiesFromScope.get(0));
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
        Interface aInterface = AlgorithmFamilyUtil.createStrategyInterfaceForAlgorithmFamily(family);
        assertEquals("ClassStrategy", aInterface.getName());
        assertEquals(3, aInterface.getOperations().size());
    }

}
