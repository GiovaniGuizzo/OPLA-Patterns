/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ufpr.inf.opla.patterns.util;

import arquitetura.representation.Architecture;
import arquitetura.representation.Class;
import arquitetura.representation.Concern;
import arquitetura.representation.Element;
import arquitetura.representation.Interface;
import arquitetura.representation.Patterns;
import br.ufpr.inf.opla.patterns.designpatterns.Bridge;
import br.ufpr.inf.opla.patterns.models.AlgorithmFamily;
import br.ufpr.inf.opla.patterns.models.Scope;
import br.ufpr.inf.opla.patterns.models.ps.PS;
import br.ufpr.inf.opla.patterns.models.ps.impl.PSBridge;
import br.ufpr.inf.opla.patterns.repositories.ArchitectureRepository;
import br.ufpr.inf.opla.patterns.strategies.scopeselection.impl.WholeArchitectureScopeSelection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import main.GenerateArchitecture;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

/**
 *
 * @author giovaniguizzo
 */
public class BridgeUtilTest {

    private final WholeArchitectureScopeSelection wholeArchitectureScopeSelection;
    private final Bridge bridge;

    public BridgeUtilTest() {
        this.wholeArchitectureScopeSelection = new WholeArchitectureScopeSelection();
        this.bridge = Bridge.getInstance();
    }

    /**
     * Test of getImplementationInterfaces method, of class BridgeUtil.
     */
    @Test
    public void testGetImplementationInterfaces() {
        Scope scope = wholeArchitectureScopeSelection.selectScope(ArchitectureRepository.getArchitecture(ArchitectureRepository.BRIDGE_MODELS[1]), Patterns.BRIDGE);

        assertTrue(bridge.verifyPS(scope));
        List<PS> pSs = scope.getPSs(bridge);
        boolean foi = false;
        for (PS ps : pSs) {
            PSBridge psBridge = (PSBridge) ps;
            AlgorithmFamily algorithmFamily = psBridge.getAlgorithmFamily();
            if (algorithmFamily.getName().equals("Sort") && algorithmFamily.getType().equals(AlgorithmFamily.SUFFIX)) {
                HashMap<Concern, List<Interface>> implementationInterfaces = BridgeUtil.getImplementationInterfaces(algorithmFamily.getParticipants());
                for (List<Interface> interfaces : implementationInterfaces.values()) {
                    assertEquals(1, interfaces.size());
                    foi = true;
                }
                break;
            }
        }
        assertTrue(foi);
    }

    /**
     * Test of getAbstractionClasses method, of class BridgeUtil.
     */
    @Test
    public void testGetAbstractionClasses() {
        Scope scope = wholeArchitectureScopeSelection.selectScope(ArchitectureRepository.getArchitecture(ArchitectureRepository.BRIDGE_MODELS[1]), Patterns.BRIDGE);

        assertTrue(bridge.verifyPS(scope));
        List<PS> pSs = scope.getPSs(bridge);
        List<String> nameList = Arrays.asList(new String[]{"Sort", "ort", "sort", "persist"});
        List<String> typeList = Arrays.asList(new String[]{AlgorithmFamily.METHOD, AlgorithmFamily.SUFFIX});
        for (PS ps : pSs) {
            PSBridge psBridge = (PSBridge) ps;
            AlgorithmFamily algorithmFamily = psBridge.getAlgorithmFamily();
            if (nameList.contains(algorithmFamily.getName()) && typeList.contains(algorithmFamily.getType())) {
                List<Element> result = BridgeUtil.getAbstractionClasses(algorithmFamily);
                assertEquals(2, result.size());
            }
        }
    }

    /**
     * Test of createAbstractionClasses method, of class BridgeUtil.
     */
    @Test
    public void testCreateAbstractionClasses() {
        final Architecture architecture = ArchitectureRepository.getArchitecture(ArchitectureRepository.BRIDGE_MODELS[0]);
        Scope scope = wholeArchitectureScopeSelection.selectScope(architecture, Patterns.BRIDGE);

        assertTrue(bridge.verifyPS(scope));
        List<PS> pSs = scope.getPSs(bridge);
        PSBridge ps = (PSBridge) pSs.get(0);

        List<Element> result = BridgeUtil.createAbstractionClasses(ps.getAlgorithmFamily());
        assertEquals(2, result.size());
        final Class abstractClass = (arquitetura.representation.Class) result.get(0);
        assertEquals(5, abstractClass.getAllMethods().size());
        assertEquals(3, abstractClass.getAllConcerns().size());

        GenerateArchitecture generateArchitecture = new GenerateArchitecture();
        generateArchitecture.generate(architecture, ArchitectureRepository.OUTPUT[3]);
    }

    /**
     * Test of createImplementationInterface method, of class BridgeUtil.
     */
    @Test
    public void testCreateImplementationInterface() {
        final Architecture architecture = ArchitectureRepository.getArchitecture(ArchitectureRepository.BRIDGE_MODELS[0]);
        Scope scope = wholeArchitectureScopeSelection.selectScope(architecture, Patterns.BRIDGE);

        assertTrue(bridge.verifyPS(scope));
        List<PS> pSs = scope.getPSs(bridge);
        PSBridge ps = (PSBridge) pSs.get(0);
        AlgorithmFamily algorithmFamily = ps.getAlgorithmFamily();

        HashMap<Concern, List<Element>> groupedElements = ElementUtil.groupElementsByConcern(algorithmFamily.getParticipants());
        List<Interface> interfaces = new ArrayList<>();
        for (Map.Entry<Concern, List<Element>> entry : groupedElements.entrySet()) {
            interfaces.add(BridgeUtil.createImplementationInterface(entry.getKey(), entry.getValue()));
        }

        assertEquals(3, interfaces.size());

        GenerateArchitecture generateArchitecture = new GenerateArchitecture();
        generateArchitecture.generate(architecture, ArchitectureRepository.OUTPUT[4]);
    }

    /**
     * Test of aggregateAbstractionWithImplementation method, of class BridgeUtil.
     */
    @Test
    public void testAggregateAbstractionWithImplementation() {
        final Architecture architecture = ArchitectureRepository.getArchitecture(ArchitectureRepository.BRIDGE_MODELS[0]);
        Scope scope = wholeArchitectureScopeSelection.selectScope(architecture, Patterns.BRIDGE);

        assertTrue(bridge.verifyPS(scope));
        List<PS> pSs = scope.getPSs(bridge);
        PSBridge ps = (PSBridge) pSs.get(0);
        AlgorithmFamily algorithmFamily = ps.getAlgorithmFamily();

        List<Element> result = BridgeUtil.createAbstractionClasses(algorithmFamily);
        assertEquals(2, result.size());
        final Class abstractClass = (arquitetura.representation.Class) result.get(0);
        assertEquals(5, abstractClass.getAllMethods().size());
        assertEquals(3, abstractClass.getAllConcerns().size());

        HashMap<Concern, List<Element>> groupedElements = ElementUtil.groupElementsByConcern(algorithmFamily.getParticipants());
        List<Interface> interfaces = new ArrayList<>();
        for (Map.Entry<Concern, List<Element>> entry : groupedElements.entrySet()) {
            Interface anInterface = BridgeUtil.createImplementationInterface(entry.getKey(), entry.getValue());
            interfaces.add(anInterface);
            BridgeUtil.aggregateAbstractionWithImplementation(abstractClass, anInterface);
        }
        assertEquals(3, interfaces.size());

        assertEquals(4, abstractClass.getRelationships().size());
        for (Interface anInterface : interfaces) {
            assertEquals(1, anInterface.getRelationships().size());
        }

        GenerateArchitecture generateArchitecture = new GenerateArchitecture();
        generateArchitecture.generate(architecture, ArchitectureRepository.OUTPUT[5]);
    }

}
