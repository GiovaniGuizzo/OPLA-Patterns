/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ufpr.inf.opla.patterns.designpatterns;

import arquitetura.exceptions.ClassNotFound;
import arquitetura.representation.Architecture;
import arquitetura.representation.Class;
import br.ufpr.inf.opla.patterns.models.Scope;
import br.ufpr.inf.opla.patterns.models.ps.PS;
import br.ufpr.inf.opla.patterns.models.ps.PSPLA;
import br.ufpr.inf.opla.patterns.models.ps.impl.PSBridge;
import br.ufpr.inf.opla.patterns.models.ps.impl.PSPLABridge;
import br.ufpr.inf.opla.patterns.repositories.ArchitectureRepository;
import br.ufpr.inf.opla.patterns.strategies.ScopeSelectionStrategy;
import br.ufpr.inf.opla.patterns.strategies.impl.WholeArchitectureScopeSelection;
import br.ufpr.inf.opla.patterns.util.ElementUtil;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import main.GenerateArchitecture;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

/**
 *
 * @author giovaniguizzo
 */
public class BridgeTest {

    private final Bridge bridge;
    private final ScopeSelectionStrategy scopeSelectionStrategy;
    private final ArchitectureRepository architectureRepository;

    public BridgeTest() {
        this.bridge = Bridge.getInstance();
        this.architectureRepository = ArchitectureRepository.getInstance();
        this.scopeSelectionStrategy = new WholeArchitectureScopeSelection();
    }

    /**
     * Test of verifyPS method, of class Bridge.
     */
    @Test
    public void testVerifyPS() {
        String model = ArchitectureRepository.STRATEGY_MODELS[0];
        Architecture architecture = architectureRepository.getArchitecture(model);
        Scope scope = scopeSelectionStrategy.selectScope(architecture);

        assertEquals(7, scope.getElements().size());

        boolean verifyPS = bridge.verifyPS(scope);
        assertTrue(verifyPS);

        List<PS> psList = scope.getPSs(bridge);
        assertEquals(3, psList.size());

        for (PS ps : psList) {
            PSBridge psSBridge = (PSBridge) ps;
            assertEquals(4, psSBridge.getParticipants().size());
            assertEquals(3, psSBridge.getAlgorithmFamily().getParticipants().size());
            assertEquals(1, psSBridge.getContexts().size());
        }
    }

    @Test
    public void testVerifyPS2() {
        String model = ArchitectureRepository.STRATEGY_MODELS[1];
        Architecture architecture = architectureRepository.getArchitecture(model);
        Scope scope = scopeSelectionStrategy.selectScope(architecture);

        assertEquals(7, scope.getElements().size());

        boolean verifyPS = bridge.verifyPS(scope);
        assertTrue(verifyPS);
    }

    @Test
    public void testVerifyPS3() {
        String model = ArchitectureRepository.STRATEGY_MODELS[2];
        Architecture architecture = architectureRepository.getArchitecture(model);
        Scope scope = scopeSelectionStrategy.selectScope(architecture);

        assertEquals(5, scope.getElements().size());

        boolean verifyPS = bridge.verifyPS(scope);
        assertTrue(verifyPS);
    }

    /**
     * Test of verifyPSPLA method, of class Bridge.
     */
    @Test
    public void testVerifyPSPLA() {
        String model = ArchitectureRepository.STRATEGY_MODELS[0];
        Architecture architecture = architectureRepository.getArchitecture(model);
        Scope scope = scopeSelectionStrategy.selectScope(architecture);

        assertEquals(7, scope.getElements().size());

        boolean verifyPSPLA = bridge.verifyPSPLA(scope);
        assertTrue(verifyPSPLA);

        List<PSPLA> psPLAList = scope.getPSsPLA(bridge);
        assertEquals(3, psPLAList.size());

        for (PSPLA psPLA : psPLAList) {
            PSPLABridge psPLABridge = (PSPLABridge) psPLA;
            assertEquals(4, psPLABridge.getParticipants().size());
            assertEquals(3, psPLABridge.getAlgorithmFamily().getParticipants().size());
            assertEquals(1, psPLABridge.getContexts().size());
        }
    }

    /**
     * Test of apply method, of class Bridge.
     */
    @Test
    public void testApply() {
        try {
            String model = ArchitectureRepository.BRIDGE_MODELS[0];
            Architecture architecture = architectureRepository.getArchitecture(model);
            Scope scope = scopeSelectionStrategy.selectScope(architecture);
            
            assertEquals(3, scope.getElements().size());
            
            boolean verifyPSPLA = bridge.verifyPSPLA(scope);
            assertTrue(verifyPSPLA);
            
            assertTrue(bridge.apply(scope));
            
            Class abstractClass = architecture.findClassByName("BrickleAbstraction").get(0);
            assertEquals(5, abstractClass.getRelationships().size());
            assertEquals(3, abstractClass.getAllConcerns().size());
            assertEquals(1, ElementUtil.getAllSubElements(abstractClass).size());
            
            Class stationary = architecture.findClassByName("StationaryBrickle").get(0);
            assertEquals(2, stationary.getRelationships().size());
            
            Class movable = architecture.findClassByName("MovableBrickle").get(0);
            assertEquals(2, movable.getRelationships().size());
            
            GenerateArchitecture generateArchitecture = new GenerateArchitecture();
            generateArchitecture.generate(architecture, ArchitectureRepository.OUTPUT[6]);
        } catch (ClassNotFound ex) {
            Logger.getLogger(BridgeTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
