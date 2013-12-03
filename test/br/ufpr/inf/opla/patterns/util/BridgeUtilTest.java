/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ufpr.inf.opla.patterns.util;

import arquitetura.representation.Element;
import br.ufpr.inf.opla.patterns.designpatterns.Bridge;
import br.ufpr.inf.opla.patterns.models.AlgorithmFamily;
import br.ufpr.inf.opla.patterns.models.Scope;
import br.ufpr.inf.opla.patterns.models.ps.PS;
import br.ufpr.inf.opla.patterns.models.ps.impl.PSBridge;
import br.ufpr.inf.opla.patterns.repositories.ArchitectureRepository;
import br.ufpr.inf.opla.patterns.strategies.impl.WholeArchitectureScopeSelection;
import java.util.Arrays;
import java.util.List;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

/**
 *
 * @author giovaniguizzo
 */
public class BridgeUtilTest {

    private final ArchitectureRepository architectureRepository;
    private final WholeArchitectureScopeSelection wholeArchitectureScopeSelection;
    private final Bridge bridge;

    public BridgeUtilTest() {
        this.architectureRepository = ArchitectureRepository.getInstance();
        this.wholeArchitectureScopeSelection = new WholeArchitectureScopeSelection();
        this.bridge = Bridge.getInstance();
    }

    /**
     * Test of getImplementationInterfaces method, of class BridgeUtil.
     */
    @Test
    public void testGetImplementationInterfaces() {
        // TODO review the generated test code and remove the default call to fail.
    }

    /**
     * Test of getAbstractionClasses method, of class BridgeUtil.
     */
    @Test
    public void testGetAbstractionClasses() {
        Scope scope = wholeArchitectureScopeSelection.selectScope(architectureRepository.getArchitecture(ArchitectureRepository.BRIDGE_MODELS[0]));

        assertTrue(bridge.verifyPS(scope));
        List<PS> pSs = scope.getPSs(bridge);
        List<String> nameList = Arrays.asList(new String[]{"Sort", "ort", "sort", "persist"});
        List<String> typeList = Arrays.asList(new String[]{AlgorithmFamily.METHOD, AlgorithmFamily.SUFFIX});
        for (PS ps : pSs) {
            PSBridge psBridge = (PSBridge) ps;
            AlgorithmFamily algorithmFamily = psBridge.getAlgorithmFamily();
            if (nameList.contains(algorithmFamily.getName()) && typeList.contains(algorithmFamily.getType())) {
                List<Element> result = BridgeUtil.getAbstractionClasses(scope, algorithmFamily);
                assertEquals(2, result.size());
            }
        }
    }

}
