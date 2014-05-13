/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ufpr.inf.opla.patterns.util;

import arquitetura.exceptions.ClassNotFound;
import arquitetura.exceptions.PackageNotFound;
import arquitetura.helpers.UtilResources;
import arquitetura.representation.Architecture;
import arquitetura.representation.Class;
import arquitetura.representation.Concern;
import arquitetura.representation.ConcernHolder;
import arquitetura.representation.Element;
import arquitetura.representation.Interface;
import arquitetura.representation.Method;
import arquitetura.representation.Package;
import br.ufpr.inf.opla.patterns.repositories.ArchitectureRepository;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
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

    public ElementUtilTest() {
    }

    /**
     * Test of isTypeOf method, of class ElementUtil.
     */
    @Test
    public void testIsTypeOf() {
        Architecture architecture = ArchitectureRepository.getArchitecture(ArchitectureRepository.STRATEGY_MODELS[3]);
        Element child = null;
        child = architecture.findClassByName("QuickSort").get(0);
        assertEquals("QuickSort", child.getName());
        Element parent = architecture.getElements().get(4);
        assertEquals("InterfaceDeTeste", parent.getName());
        assertTrue(ElementUtil.isTypeOf(child, parent));
        assertFalse(ElementUtil.isTypeOf(parent, child));
        assertFalse(ElementUtil.isTypeOf(child, child));
        child = architecture.findInterfaceByName("StrategyInterface");
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
        Architecture architecture = ArchitectureRepository.getArchitecture(ArchitectureRepository.STRATEGY_MODELS[3]);
        Element child = null;
        child = architecture.findClassByName("QuickSort").get(0);
        assertEquals("QuickSort", child.getName());
        List<Interface> result = ElementUtil.getAllSuperInterfaces(child);
        assertEquals(2, result.size());
        child = architecture.findInterfaceByName("StrategyInterface");
        assertEquals("StrategyInterface", child.getName());
        result = ElementUtil.getAllSuperInterfaces(child);
        assertEquals(1, result.size());
    }

    /**
     * Test of getAllExtendedElements method, of class ElementUtil.
     */
    @Test
    public void testGetAllExtendedElements() {
        Architecture architecture = ArchitectureRepository.getArchitecture(ArchitectureRepository.STRATEGY_MODELS[3]);
        Element child = null;
        child = architecture.findClassByName("QuickSort").get(0);
        assertEquals("QuickSort", child.getName());
        List<Element> result = ElementUtil.getAllExtendedElements(child);
        assertEquals(0, result.size());
        child = architecture.findInterfaceByName("StrategyInterface");
        assertEquals("StrategyInterface", child.getName());
        result = ElementUtil.getAllExtendedElements(child);
        assertEquals(1, result.size());
    }

    /**
     * Test of getAllSubElements method, of class ElementUtil.
     */
    @Test
    public void testGetAllSubElements() {
        Architecture architecture = ArchitectureRepository.getArchitecture(ArchitectureRepository.STRATEGY_MODELS[2]);
        Element element = null;
        element = architecture.findInterfaceByName("CommonStrategy");
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
        Architecture architecture = ArchitectureRepository.getArchitecture(ArchitectureRepository.STRATEGY_MODELS[2]);
        Element element = null;
        element = architecture.findClassByName("Class2").get(0);
        Element element2 = null;
        element2 = architecture.findInterfaceByName("StrategyInterface");
        Element element3 = null;
        element3 = architecture.findPackageByName("ClassPackage");
        assertTrue(ElementUtil.isClassOrInterface(element));
        assertTrue(ElementUtil.isClassOrInterface(element2));
        assertFalse(ElementUtil.isClassOrInterface(element3));
    }

    /**
     * Test of getOwnAndMethodsCommonConcerns method, of class ElementUtil.
     */
    @Test
    public void testGetOwnAndMethodsCommonConcerns() {
        Architecture architecture = ArchitectureRepository.getArchitecture(ArchitectureRepository.BRIDGE_MODELS[1]);
        Element element = null;
        element = architecture.findClassByName("QuickSort").get(0);
        Element element2 = null;
        element2 = architecture.findClassByName("BubbleSort").get(0);
        List<Element> elementList = Arrays.asList(new Element[]{element, element2});
        Set<Concern> result = ElementUtil.getOwnAndMethodsCommonConcerns(elementList);
        assertEquals(1, result.size());
        assertEquals("[bowling]", result.toString());

        Element element3 = null;
        element3 = architecture.findClassByName("ShellSort").get(0);
        elementList = Arrays.asList(new Element[]{element, element2, element3});
        result = ElementUtil.getOwnAndMethodsCommonConcerns(elementList);
        assertEquals(0, result.size());
    }

    /**
     * Test of getOwnAndMethodsConcerns method, of class ElementUtil.
     */
    @Test
    public void testGetOwnAndMethodsConcerns_List() {
        Architecture architecture = ArchitectureRepository.getArchitecture(ArchitectureRepository.BRIDGE_MODELS[1]);
        Element element = null;
        element = architecture.findClassByName("QuickSort").get(0);
        Element element2 = null;
        element2 = architecture.findClassByName("ShellSort").get(0);
        List<Element> elementList = Arrays.asList(new Element[]{element, element2});
        Set<Concern> result = ElementUtil.getOwnAndMethodsConcerns(elementList);
        assertEquals(2, result.size());
        List<String> concernList = Arrays.asList(new String[]{"bowling", "brickles"});
        for (Concern concern : result) {
            assertTrue(concernList.contains(concern.getName()));
        }
    }

    /**
     * Test of getOwnAndMethodsConcerns method, of class ElementUtil.
     */
    @Test
    public void testGetOwnAndMethodsConcerns_Element() {
        Architecture architecture = ArchitectureRepository.getArchitecture(ArchitectureRepository.BRIDGE_MODELS[1]);
        Element element = null;
        element = architecture.findClassByName("SortAbstractionClass").get(0);
        Set<Concern> result = ElementUtil.getOwnAndMethodsConcerns(element);
        assertEquals(2, result.size());
        List<String> concernList = Arrays.asList(new String[]{"bowling", "brickles"});
        for (Concern resultElement : result) {
            assertTrue(concernList.contains(resultElement.getName()));
        }
    }

    /**
     * Test of getOwnAndMethodsCommonConcernsOfAtLeastTwoElements method, of class ElementUtil.
     */
    @Test
    public void testGetOwnAndMethodsCommonConcernsOfAtLeastTwoElements() {
        Architecture architecture = ArchitectureRepository.getArchitecture(ArchitectureRepository.BRIDGE_MODELS[1]);
        Element element = null;
        element = architecture.findClassByName("QuickSort").get(0);
        Element element2 = null;
        element2 = architecture.findClassByName("ShellSort").get(0);
        List<Element> elementList = Arrays.asList(new Element[]{element, element2});
        Set<Concern> result = ElementUtil.getOwnAndMethodsCommonConcernsOfAtLeastTwoElements(elementList);
        assertEquals(0, result.size());

        Element element3 = null;
        element3 = architecture.findClassByName("BubbleSort").get(0);
        elementList = Arrays.asList(new Element[]{element, element2, element3});
        result = ElementUtil.getOwnAndMethodsCommonConcernsOfAtLeastTwoElements(elementList);
        assertEquals(2, result.size());
    }

    /**
     * Test of getAllAggregatedElements method, of class ElementUtil.
     */
    @Test
    public void testGetAllAggregatedElements() {
        Architecture architecture = ArchitectureRepository.getArchitecture(ArchitectureRepository.BRIDGE_MODELS[1]);
        Element element = null;
        element = architecture.findClassByName("SortAbstractionClass").get(0);
        Set<Element> result = ElementUtil.getAllAggregatedElements(element);
        assertEquals(2, result.size());
        List<String> nameList = Arrays.asList(new String[]{"BowlingImplementationInterface", "BricklesImplementationInterface"});
        for (Element resultElement : result) {
            assertTrue(nameList.contains(resultElement.getName()));
        }
    }

    /**
     * Test of getAllCommonSuperInterfaces method, of class InterfaceUtil.
     */
    @Test
    public void testGetAllCommonSuperInterfaces() {
        Architecture architecture = ArchitectureRepository.getArchitecture(ArchitectureRepository.STRATEGY_MODELS[2]);
        Element element = null;
        element = architecture.findClassByName("Class1").get(0);
        Element element2 = null;
        element2 = architecture.findClassByName("Class2").get(0);
        Element element3 = null;
        element3 = architecture.findInterfaceByName("StrategyInterface");
        List<Element> elementList = Arrays.asList(new Element[]{element, element2, element3});
        List<Interface> commonSuperInterfacesOfASetOfElements = ElementUtil.getAllCommonSuperInterfaces(elementList);
        assertEquals(1, commonSuperInterfacesOfASetOfElements.size());
        assertEquals("CommonStrategy", commonSuperInterfacesOfASetOfElements.get(0).getName());
    }

    /**
     * Test of getAllCommonInterfaces method, of class InterfaceUtil.
     */
    @Test
    public void testGetAllCommonInterfaces() {
        Architecture architecture = ArchitectureRepository.getArchitecture(ArchitectureRepository.STRATEGY_MODELS[2]);
        Element element = null;
        element = architecture.findClassByName("Class1").get(0);
        Element element2 = null;
        element2 = architecture.findClassByName("Class2").get(0);
        Element element3 = null;
        element3 = architecture.findInterfaceByName("StrategyInterface");
        List<Element> elementList = Arrays.asList(new Element[]{element, element2, element3});
        List<Interface> commonInterfaces = ElementUtil.getAllCommonInterfaces(elementList);
        assertEquals(2, commonInterfaces.size());
        List<String> nameList = Arrays.asList(new String[]{"StrategyInterface", "CommonStrategy"});
        for (Element resultElement : commonInterfaces) {
            assertTrue(nameList.contains(resultElement.getName()));
        }
    }

    /**
     * Test of groupElementsByConcern method, of class ElementUtil.
     */
    @Test
    public void testGroupElementsByConcern() {
        String model = ArchitectureRepository.OTHER_MODELS[3];
        Architecture architecture = ArchitectureRepository.getArchitecture(model);
        HashMap<Concern, List<Element>> result = ElementUtil.groupElementsByConcern(architecture.getElements());
        assertEquals(4, result.size());
        assertEquals(2, result.get(null).size());
        assertEquals(2, result.get(ConcernHolder.INSTANCE.getConcernByName("bowling")).size());
        assertEquals(1, result.get(ConcernHolder.INSTANCE.getConcernByName("collision")).size());
        assertEquals(1, result.get(ConcernHolder.INSTANCE.getConcernByName("brickles")).size());
    }

    /**
     * Test of getElementsWithNoOwnConcernsAndWithAtLeastOneMethodWithNoConcerns method, of class ElementUtil.
     */
    @Test
    public void testGetElementsWithNoOwnConcernsAndWithAtLeastOneMethodWithNoConcerns() {
        String model = ArchitectureRepository.OTHER_MODELS[3];
        Architecture architecture = ArchitectureRepository.getArchitecture(model);
        ArrayList<Element> result = ElementUtil.getElementsWithNoOwnConcernsAndWithAtLeastOneMethodWithNoConcerns(architecture.getElements());
        assertEquals(2, result.size());
    }

    /**
     * Test of getNameSpace method, of class ElementUtil.
     */
    @Test
    public void testGetNameSpace() {
        String model = ArchitectureRepository.STRATEGY_MODELS[2];
        Architecture architecture = ArchitectureRepository.getArchitecture(model);
        List<Element> elements = architecture.getElements();
        String result = ElementUtil.getNameSpace(elements);
        assertEquals("Model::ClassPackage", result);
    }

    /**
     * Test of implementInterface method, of class ElementUtil.
     */
    @Test
    public void testImplementInterface() {
        String model = ArchitectureRepository.STRATEGY_MODELS[2];
        Architecture architecture = ArchitectureRepository.getArchitecture(model);
        try {
            Package aPackage = architecture.findPackageByName("ClassPackage");
            List<Element> elements = new ArrayList<>(architecture.getElements());
            elements.remove(aPackage);

            Interface anInterface = architecture.createInterface("VaiFoiDeuInterface");
            Method createOperation = anInterface.createOperation("foiDeu");

            List<Element> adapterList = new ArrayList<>();
            List<Element> adapteeList = new ArrayList<>();
            ElementUtil.implementInterface(elements, anInterface, adapterList, adapteeList);

            assertTrue(adapterList.size() == 1 || adapterList.size() == 2);
            assertEquals(4, adapteeList.size());

            Class aClass = architecture.findClassByName("CommonStrategyAdapter").get(0);
            assertTrue(aClass.getAllMethods().contains(createOperation));
        } catch (PackageNotFound | ClassNotFound ex) {
            Logger.getLogger(ElementUtilTest.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(ElementUtilTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Test
    public void testFindPackage() {
        String model = ArchitectureRepository.STRATEGY_MODELS[2];
        Architecture architecture = ArchitectureRepository.getArchitecture(model);

        Package aPackage = architecture.createPackage("PacoteDeTeste");
        Interface anInterface = aPackage.createInterface("InterfaceDeTeste");

        String packageName = UtilResources.extractPackageName(anInterface.getNamespace());
        assertEquals("PacoteDeTeste", packageName);
    }

    @Test
    public void testGetChainOfRelatedElementsWithSameConcern() {
        String model = ArchitectureRepository.OTHER_MODELS[7];
        Architecture architecture = ArchitectureRepository.getArchitecture(model);

        Element klass1 = architecture.findClassByName("Class1").get(0);
        Element klass2 = architecture.findClassByName("Class2").get(0);
        Element klass3 = architecture.findClassByName("Class3").get(0);
        Element klass4 = architecture.findClassByName("Class4").get(0);
        Element klass5 = architecture.findClassByName("Class5").get(0);
        Element klass6 = architecture.findClassByName("Class6").get(0);
        Element klass7 = architecture.findClassByName("Class7").get(0);
        Element klass8 = architecture.findClassByName("Class8").get(0);
        Element klass9 = architecture.findInterfaceByName("Class9");

        ArrayList classes = new ArrayList();
        classes.add(klass1);
        classes.add(klass2);
        classes.add(klass3);
        classes.add(klass4);
        classes.add(klass5);
        classes.add(klass6);
        classes.add(klass7);
        classes.add(klass8);
        classes.add(klass9);

        List<Element> chain = ElementUtil.getChainOfRelatedElementsWithSameConcern(classes, new Concern("action"));

        assertEquals(7, chain.size());
        assertFalse(chain.contains(klass1));
        assertTrue(chain.contains(klass2));
        assertTrue(chain.contains(klass3));
        assertTrue(chain.contains(klass4));
        assertTrue(chain.contains(klass5));
        assertTrue(chain.contains(klass6));
        assertFalse(chain.contains(klass7));
        assertTrue(chain.contains(klass8));
        assertTrue(chain.contains(klass9));
    }

    @Test
    public void testGetAppliedDesignPatterns() {
        Architecture architecture = ArchitectureRepository.getArchitecture(ArchitectureRepository.OTHER_MODELS[8]);
        arquitetura.representation.Class klass1 = architecture.findClassByName("Class1").get(0);
        arquitetura.representation.Class klass2 = architecture.findClassByName("Class2").get(0);
        arquitetura.representation.Class klass3 = architecture.findClassByName("Class3").get(0);

        assertEquals(2, ElementUtil.getAppliedDesignPatterns(klass1).size());
        assertEquals(1, ElementUtil.getAppliedDesignPatterns(klass2).size());
        assertEquals(0, ElementUtil.getAppliedDesignPatterns(klass3).size());
    }

}
