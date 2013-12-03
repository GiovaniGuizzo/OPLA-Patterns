/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ufpr.inf.opla.patterns.util;

import arquitetura.exceptions.ClassNotFound;
import arquitetura.exceptions.InterfaceNotFound;
import arquitetura.exceptions.PackageNotFound;
import arquitetura.representation.Architecture;
import arquitetura.representation.Concern;
import arquitetura.representation.Element;
import arquitetura.representation.Interface;
import br.ufpr.inf.opla.patterns.repositories.ArchitectureRepository;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
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

    private final ArchitectureRepository architectureRepository;

    public ElementUtilTest() {
        this.architectureRepository = ArchitectureRepository.getInstance();
    }

    /**
     * Test of isTypeOf method, of class ElementUtil.
     */
    @Test
    public void testIsTypeOf() {
        Architecture architecture = architectureRepository.getArchitecture(ArchitectureRepository.STRATEGY_MODELS[3]);
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
     * Test of getAllSuperInterfaces method, of class ElementUtil.
     */
    @Test
    public void testGetAllSuperInterfaces() {
        Architecture architecture = architectureRepository.getArchitecture(ArchitectureRepository.STRATEGY_MODELS[3]);
        Element child = null;
        try {
            child = architecture.findClassByName("QuickSort").get(0);
        } catch (ClassNotFound ex) {
            Logger.getLogger(ElementUtilTest.class.getName()).log(Level.SEVERE, null, ex);
        }
        assertEquals("QuickSort", child.getName());
        List<Interface> result = ElementUtil.getAllSuperInterfaces(child);
        assertEquals(2, result.size());

        try {
            child = architecture.findInterfaceByName("StrategyInterface");
        } catch (InterfaceNotFound ex) {
            Logger.getLogger(ElementUtilTest.class.getName()).log(Level.SEVERE, null, ex);
        }
        assertEquals("StrategyInterface", child.getName());
        result = ElementUtil.getAllSuperInterfaces(child);
        assertEquals(1, result.size());
    }

    /**
     * Test of getAllExtendedElements method, of class ElementUtil.
     */
    @Test
    public void testGetAllExtendedElements() {
        Architecture architecture = architectureRepository.getArchitecture(ArchitectureRepository.STRATEGY_MODELS[3]);
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

    /**
     * Test of getAllSubElements method, of class ElementUtil.
     */
    @Test
    public void testGetAllSubElements() {
        Architecture architecture = architectureRepository.getArchitecture(ArchitectureRepository.STRATEGY_MODELS[2]);
        Element element = null;
        try {
            element = architecture.findInterfaceByName("CommonStrategy");
        } catch (InterfaceNotFound ex) {
            Logger.getLogger(ElementUtilTest.class.getName()).log(Level.SEVERE, null, ex);
        }
        List<Element> result = ElementUtil.getAllSubElements(element);
        assertEquals(3, result.size());
        List<String> nameList = Arrays.asList(new String[]{"StrategyInterface", "Class1", "Class2"});
        for (Element resultElement : result) {
            assertTrue(nameList.contains(resultElement.getName()));
        }
    }

    /**
     * Test of isClassOrInterface method, of class ElementUtil.
     */
    @Test
    public void testIsClassOrInterface() {
        Architecture architecture = architectureRepository.getArchitecture(ArchitectureRepository.STRATEGY_MODELS[2]);
        Element element = null;
        try {
            element = architecture.findClassByName("Class2").get(0);
        } catch (ClassNotFound ex) {
            Logger.getLogger(ElementUtilTest.class.getName()).log(Level.SEVERE, null, ex);
        }
        Element element2 = null;
        try {
            element2 = architecture.findInterfaceByName("StrategyInterface");
        } catch (InterfaceNotFound ex) {
            Logger.getLogger(ElementUtilTest.class.getName()).log(Level.SEVERE, null, ex);
        }
        Element element3 = null;
        try {
            element3 = architecture.findPackageByName("ClassPackage");
        } catch (PackageNotFound ex) {
            Logger.getLogger(ElementUtilTest.class.getName()).log(Level.SEVERE, null, ex);
        }
        assertTrue(ElementUtil.isClassOrInterface(element));
        assertTrue(ElementUtil.isClassOrInterface(element2));
        assertFalse(ElementUtil.isClassOrInterface(element3));
    }

    /**
     * Test of getCommonConcerns method, of class ElementUtil.
     */
    @Test
    public void testGetCommonConcerns() {
        Architecture architecture = architectureRepository.getArchitecture(ArchitectureRepository.BRIDGE_MODELS[0]);
        Element element = null;
        try {
            element = architecture.findClassByName("QuickSort").get(0);
        } catch (ClassNotFound ex) {
            Logger.getLogger(ElementUtilTest.class.getName()).log(Level.SEVERE, null, ex);
        }
        Element element2 = null;
        try {
            element2 = architecture.findClassByName("BubbleSort").get(0);
        } catch (ClassNotFound ex) {
            Logger.getLogger(ElementUtilTest.class.getName()).log(Level.SEVERE, null, ex);
        }
        List<Element> elementList = Arrays.asList(new Element[]{element, element2});
        Set<Concern> result = ElementUtil.getCommonConcerns(elementList);
        assertEquals(1, result.size());
        assertEquals("[sorting]", result.toString());

        Element element3 = null;
        try {
            element3 = architecture.findClassByName("ShellSort").get(0);
        } catch (ClassNotFound ex) {
            Logger.getLogger(ElementUtilTest.class.getName()).log(Level.SEVERE, null, ex);
        }
        elementList = Arrays.asList(new Element[]{element, element2, element3});
        result = ElementUtil.getCommonConcerns(elementList);
        assertEquals(0, result.size());
    }

    /**
     * Test of getAllConcerns method, of class ElementUtil.
     */
    @Test
    public void testGetAllConcerns_List() {
        Architecture architecture = architectureRepository.getArchitecture(ArchitectureRepository.BRIDGE_MODELS[0]);
        Element element = null;
        try {
            element = architecture.findClassByName("QuickSort").get(0);
        } catch (ClassNotFound ex) {
            Logger.getLogger(ElementUtilTest.class.getName()).log(Level.SEVERE, null, ex);
        }
        Element element2 = null;
        try {
            element2 = architecture.findClassByName("ShellSort").get(0);
        } catch (ClassNotFound ex) {
            Logger.getLogger(ElementUtilTest.class.getName()).log(Level.SEVERE, null, ex);
        }
        List<Element> elementList = Arrays.asList(new Element[]{element, element2});
        Set<Concern> result = ElementUtil.getAllConcerns(elementList);
        assertEquals(2, result.size());
        List<String> concernList = Arrays.asList(new String[]{"Persistence", "sorting"});
        for (Concern concern : result) {
            assertTrue(concernList.contains(concern.getName()));
        }
    }

    /**
     * Test of getAllConcerns method, of class ElementUtil.
     */
    @Test
    public void testGetAllConcerns_Element() {
        Architecture architecture = architectureRepository.getArchitecture(ArchitectureRepository.BRIDGE_MODELS[0]);
        Element element = null;
        try {
            element = architecture.findClassByName("SortAbstractionClass").get(0);
        } catch (ClassNotFound ex) {
            Logger.getLogger(ElementUtilTest.class.getName()).log(Level.SEVERE, null, ex);
        }
        Set<Concern> result = ElementUtil.getAllConcerns(element);
        assertEquals(2, result.size());
        List<String> concernList = Arrays.asList(new String[]{"Persistence", "sorting"});
        for (Concern resultElement : result) {
            assertTrue(concernList.contains(resultElement.getName()));
        }
    }

    /**
     * Test of getCommonConcernsOfAtLeastTwoElements method, of class ElementUtil.
     */
    @Test
    public void testGetCommonConcernsOfAtLeastTwoElements() {
        Architecture architecture = architectureRepository.getArchitecture(ArchitectureRepository.BRIDGE_MODELS[0]);
        Element element = null;
        try {
            element = architecture.findClassByName("QuickSort").get(0);
        } catch (ClassNotFound ex) {
            Logger.getLogger(ElementUtilTest.class.getName()).log(Level.SEVERE, null, ex);
        }
        Element element2 = null;
        try {
            element2 = architecture.findClassByName("ShellSort").get(0);
        } catch (ClassNotFound ex) {
            Logger.getLogger(ElementUtilTest.class.getName()).log(Level.SEVERE, null, ex);
        }
        List<Element> elementList = Arrays.asList(new Element[]{element, element2});
        Set<Concern> result = ElementUtil.getCommonConcernsOfAtLeastTwoElements(elementList);
        assertEquals(0, result.size());

        Element element3 = null;
        try {
            element3 = architecture.findClassByName("BubbleSort").get(0);
        } catch (ClassNotFound ex) {
            Logger.getLogger(ElementUtilTest.class.getName()).log(Level.SEVERE, null, ex);
        }
        elementList = Arrays.asList(new Element[]{element, element2, element3});
        result = ElementUtil.getCommonConcernsOfAtLeastTwoElements(elementList);
        assertEquals(2, result.size());
    }
    
    /**
     * Test of getAllAggregatedElements method, of class ElementUtil.
     */
    @Test
    public void testGetAllAggregatedElements() {
        Architecture architecture = architectureRepository.getArchitecture(ArchitectureRepository.BRIDGE_MODELS[0]);
        Element element = null;
        try {
            element = architecture.findClassByName("SortAbstractionClass").get(0);
        } catch (ClassNotFound ex) {
            Logger.getLogger(ElementUtilTest.class.getName()).log(Level.SEVERE, null, ex);
        }
        Set<Element> result = ElementUtil.getAllAggregatedElements(element);
        assertEquals(2, result.size());
        List<String> nameList = Arrays.asList(new String[]{"SortingImplementationInterface", "PersistenceImplementationInterface"});
        for (Element resultElement : result) {
            assertTrue(nameList.contains(resultElement.getName()));
        }
    }

}
