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
import arquitetura.representation.Method;
import br.ufpr.inf.opla.patterns.repositories.ArchitectureRepository;
import java.util.ArrayList;
import java.util.Arrays;
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
public class InterfaceUtilTest {

    private final ArchitectureRepository architectureRepository;

    public InterfaceUtilTest() {
        this.architectureRepository = ArchitectureRepository.getInstance();
    }
    
    /**
     * Test of createInterfaceForSetOfElements method, of class InterfaceUtil.
     */
    @Test
    public void testCreateInterfaceForSetOfElements() {
        String interfaceName = "TesteInterface";
        Architecture architecture = architectureRepository.getArchitecture(ArchitectureRepository.STRATEGY_MODELS[2]);
        List<Element> participants = architecture.getElements();
        Interface result = InterfaceUtil.createInterfaceForSetOfElements(interfaceName, participants);
        assertEquals("Model::ClassPackage", result.getNamespace());
        List<Method> operations = new ArrayList<>(result.getOperations());
        assertEquals(3, operations.size());
        Method tempMethodA = operations.get(0);
        Method tempMethodB = operations.get(1);
        Method tempMethodC = operations.get(2);
        assertEquals("methodA", tempMethodA.getName());
        assertFalse(tempMethodA.getName().equals(tempMethodB.getName()));
        assertFalse(tempMethodB.getName().equals(tempMethodC.getName()));
        assertEquals("Integer", tempMethodB.getReturnType());
        assertEquals("String", tempMethodC.getReturnType());
    }

    /**
     * Test of getCommonSuperInterfaces method, of class InterfaceUtil.
     */
    @Test
    public void testGetCommonSuperInterfaces() {
        Architecture architecture = architectureRepository.getArchitecture(ArchitectureRepository.STRATEGY_MODELS[2]);
        Element element = null;
        try {
            element = architecture.findClassByName("Class1").get(0);
        } catch (ClassNotFound ex) {
            Logger.getLogger(ElementUtilTest.class.getName()).log(Level.SEVERE, null, ex);
        }
        Element element2 = null;
        try {
            element2 = architecture.findClassByName("Class2").get(0);
        } catch (ClassNotFound ex) {
            Logger.getLogger(ElementUtilTest.class.getName()).log(Level.SEVERE, null, ex);
        }
        Element element3 = null;
        try {
            element3 = architecture.findInterfaceByName("StrategyInterface");
        } catch (InterfaceNotFound ex) {
            Logger.getLogger(InterfaceUtilTest.class.getName()).log(Level.SEVERE, null, ex);
        }
        List<Element> elementList = Arrays.asList(new Element[]{element, element2, element3});
        List<Interface> commonSuperInterfacesOfASetOfElements = InterfaceUtil.getCommonSuperInterfaces(elementList);
        assertEquals(1, commonSuperInterfacesOfASetOfElements.size());
        assertEquals("CommonStrategy", commonSuperInterfacesOfASetOfElements.get(0).getName());
    }

    /**
     * Test of getCommonInterfaces method, of class InterfaceUtil.
     */
    @Test
    public void testGetCommonInterfaces() {
        Architecture architecture = architectureRepository.getArchitecture(ArchitectureRepository.STRATEGY_MODELS[2]);
        Element element = null;
        try {
            element = architecture.findClassByName("Class1").get(0);
        } catch (ClassNotFound ex) {
            Logger.getLogger(ElementUtilTest.class.getName()).log(Level.SEVERE, null, ex);
        }
        Element element2 = null;
        try {
            element2 = architecture.findClassByName("Class2").get(0);
        } catch (ClassNotFound ex) {
            Logger.getLogger(ElementUtilTest.class.getName()).log(Level.SEVERE, null, ex);
        }
        Element element3 = null;
        try {
            element3 = architecture.findInterfaceByName("StrategyInterface");
        } catch (InterfaceNotFound ex) {
            Logger.getLogger(InterfaceUtilTest.class.getName()).log(Level.SEVERE, null, ex);
        }
        List<Element> elementList = Arrays.asList(new Element[]{element, element2, element3});
        List<Interface> commonInterfaces = InterfaceUtil.getCommonInterfaces(elementList);
        assertEquals(2, commonInterfaces.size());
        List<String> nameList = Arrays.asList(new String[]{"StrategyInterface", "CommonStrategy"});
        for (Element resultElement : commonInterfaces) {
            assertTrue(nameList.contains(resultElement.getName()));
        }
    }
}
