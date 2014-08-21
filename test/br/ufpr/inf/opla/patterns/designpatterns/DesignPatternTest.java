/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ufpr.inf.opla.patterns.designpatterns;

import arquitetura.representation.Architecture;
import arquitetura.representation.Class;
import arquitetura.representation.Interface;
import br.ufpr.inf.opla.patterns.repositories.ArchitectureRepository;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

/**
 *
 * @author giovaniguizzo
 */
public class DesignPatternTest {

    private final Adapter adapter;
    private final Bridge bridge;
    private final Strategy strategy;

    public DesignPatternTest() {
        adapter = Adapter.getInstance();
        bridge = Bridge.getInstance();
        strategy = Strategy.getInstance();
    }

    @Test
    public void testAddStereotype() {
        String model = ArchitectureRepository.STRATEGY_MODELS[4];
        Architecture architecture = ArchitectureRepository.getArchitecture(model);

        Interface target = architecture.findInterfaceByName("SomeInterface");
        assertEquals("SomeInterface", target.getName());
        Class newClass = architecture.createClass("TesteDeClass", false);
        
        assertTrue(adapter.addStereotype(target));
        assertTrue(adapter.addStereotype(newClass));
        assertEquals(1, target.getPatternsOperations().getAllPatterns().size());
        assertEquals(1, newClass.getPatternsOperations().getAllPatterns().size());
        
        assertTrue(strategy.addStereotype(target));
        assertTrue(strategy.addStereotype(newClass));
        assertEquals(2, target.getPatternsOperations().getAllPatterns().size());
        assertEquals(2, newClass.getPatternsOperations().getAllPatterns().size());
        
        assertTrue(bridge.addStereotype(target));
        assertTrue(bridge.addStereotype(newClass));
        assertEquals(3, target.getPatternsOperations().getAllPatterns().size());
        assertEquals(3, newClass.getPatternsOperations().getAllPatterns().size());
    }

}
