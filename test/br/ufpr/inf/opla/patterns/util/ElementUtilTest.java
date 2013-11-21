/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ufpr.inf.opla.patterns.util;

import arquitetura.exceptions.ClassNotFound;
import arquitetura.exceptions.InterfaceNotFound;
import arquitetura.representation.Architecture;
import arquitetura.representation.Element;
import arquitetura.representation.Interface;
import br.ufpr.inf.opla.patterns.repositories.ArchitectureRepositoryFlyweight;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

/**
 *
 * @author giovaniguizzo
 */
public class ElementUtilTest {

    private final ArchitectureRepositoryFlyweight architectureRepository;

    public ElementUtilTest() {
        this.architectureRepository = ArchitectureRepositoryFlyweight.getInstance();
    }

    /**
     * Test of isTypeOf method, of class ElementUtil.
     */
    @Test
    public void testIsTypeOf() {
        Architecture architecture = architectureRepository.getArchitecture(ArchitectureRepositoryFlyweight.STRATEGY_MODELS[3]);
        Element child = null;
        try {
            child = architecture.findClassByName("QuickSort").get(0);
        } catch (ClassNotFound ex) {
            Logger.getLogger(ElementUtilTest.class.getName()).log(Level.SEVERE, null, ex);
        }
        assertEquals("QuickSort", child.getName());
        Element parent = architecture.getElements().get(4);
        assertEquals("InterfaceDeTeste", parent.getName());
        assertTrue(ElementUtil.isTypeOf(child, parent));
        assertFalse(ElementUtil.isTypeOf(parent, child));
        assertFalse(ElementUtil.isTypeOf(child, child));
        try {
            child = architecture.findInterfaceByName("StrategyInterface");
        } catch (InterfaceNotFound ex) {
            Logger.getLogger(ElementUtilTest.class.getName()).log(Level.SEVERE, null, ex);
        }
        assertEquals("StrategyInterface", child.getName());
        assertTrue(ElementUtil.isTypeOf(child, parent));
        assertFalse(ElementUtil.isTypeOf(parent, child));
        assertFalse(ElementUtil.isTypeOf(child, child));
    }

    /**
     * Test of getAllImplementedInterfaces method, of class ElementUtil.
     */
    @Test
    public void testGetAllImplementedInterfaces() {
        Architecture architecture = architectureRepository.getArchitecture(ArchitectureRepositoryFlyweight.STRATEGY_MODELS[3]);
        Element child = null;
        try {
            child = architecture.findClassByName("QuickSort").get(0);
        } catch (ClassNotFound ex) {
            Logger.getLogger(ElementUtilTest.class.getName()).log(Level.SEVERE, null, ex);
        }
        assertEquals("QuickSort", child.getName());
        List<Interface> result = ElementUtil.getAllImplementedInterfaces(child);
        assertEquals(2, result.size());
        
        try {
            child = architecture.findInterfaceByName("StrategyInterface");
        } catch (InterfaceNotFound ex) {
            Logger.getLogger(ElementUtilTest.class.getName()).log(Level.SEVERE, null, ex);
        }
        assertEquals("StrategyInterface", child.getName());
        result = ElementUtil.getAllImplementedInterfaces(child);
        assertEquals(0, result.size());
    }

    /**
     * Test of getAllExtendedElements method, of class ElementUtil.
     */
    @Test
    public void testGetAllExtendedElements() {
        Architecture architecture = architectureRepository.getArchitecture(ArchitectureRepositoryFlyweight.STRATEGY_MODELS[3]);
        Element child = null;
        try {
            child = architecture.findClassByName("QuickSort").get(0);
        } catch (ClassNotFound ex) {
            Logger.getLogger(ElementUtilTest.class.getName()).log(Level.SEVERE, null, ex);
        }
        assertEquals("QuickSort", child.getName());
        List<Element> result = ElementUtil.getAllExtendedElements(child);
        assertEquals(0, result.size());
        
        try {
            child = architecture.findInterfaceByName("StrategyInterface");
        } catch (InterfaceNotFound ex) {
            Logger.getLogger(ElementUtilTest.class.getName()).log(Level.SEVERE, null, ex);
        }
        assertEquals("StrategyInterface", child.getName());
        result = ElementUtil.getAllExtendedElements(child);
        assertEquals(1, result.size());
    }

}
