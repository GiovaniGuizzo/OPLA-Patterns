/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ufpr.inf.opla.patterns.util;

import arquitetura.representation.Architecture;
import arquitetura.representation.Concern;
import arquitetura.representation.Element;
import arquitetura.representation.Method;
import br.ufpr.inf.opla.patterns.repositories.ArchitectureRepository;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

/**
 *
 * @author giovaniguizzo
 */
public class MethodUtilTest {

    public MethodUtilTest() {
    }

    /**
     * Test of getMethodsFromElement method, of class MethodUtil.
     */
    @Test
    public void testGetMethodsFromElement() {
        Architecture architecture = ArchitectureRepository.getArchitecture(ArchitectureRepository.OTHER_MODELS[1]);
        Element element = null;
        element = architecture.findClassByName("Class2").get(0);
        assertEquals("Class2", element.getName());
        Set<Method> result = MethodUtil.getMethodsFromElement(element);
        assertEquals(4, result.size());
    }

    /**
     * Test of getAllMethodsFromSetOfElements method, of class MethodUtil.
     */
    @Test
    public void testGetAllMethodsFromSetOfElements() {
        Architecture architecture = ArchitectureRepository.getArchitecture(ArchitectureRepository.OTHER_MODELS[1]);
        List<Method> result = MethodUtil.getAllMethodsFromSetOfElements(architecture.getElements());
        assertEquals(5, result.size());
    }

    /**
     * Test of createMethodsFromSetOfElementsByConcern method, of class MethodUtil.
     */
    @Test
    public void testCreateMethodsFromSetOfElements() {
        String model = ArchitectureRepository.OTHER_MODELS[1];
        Architecture architecture = ArchitectureRepository.getArchitecture(model);
        List<Method> result = MethodUtil.createMethodsFromSetOfElements(architecture.getElements());
        assertEquals(5, result.size());
        for (Method method : result) {
            if ("Operation2".equals(method.getName()) && "String".equals(method.getReturnType())) {
                assertEquals(3, method.getParameters().size());
                break;
            }
        }
    }

    /**
     * Test of cloneMethod method, of class MethodUtil.
     */
    @Test
    public void testCloneMethod() {
        Architecture architecture = ArchitectureRepository.getArchitecture(ArchitectureRepository.OTHER_MODELS[1]);
        Element element = null;
        element = architecture.findClassByName("Class2").get(0);
        assertEquals("Class2", element.getName());
        List<Method> methods = new ArrayList<>(MethodUtil.getMethodsFromElement(element));
        assertEquals(4, methods.size());
        Method method = null;
        for (int i = 0; i < methods.size(); i++) {
            method = methods.get(i);
            if ("Operation2".equals(method.getName()) && "String".equals(method.getReturnType())) {
                break;
            }
        }
        Method result = MethodUtil.cloneMethod(method);
        assertNotSame(method.getId(), result.getId());
        assertEquals(method.getName(), result.getName());
        assertEquals(method.getNamespace(), result.getNamespace());
        assertEquals(method.getOwnConcerns(), result.getOwnConcerns());
        assertEquals(method.getParameters(), result.getParameters());
        assertEquals(method.getReturnType(), result.getReturnType());
    }

    /**
     * Test of cloneMethods method, of class MethodUtil.
     */
    @Test
    public void testCloneMethods() {
        Architecture architecture = ArchitectureRepository.getArchitecture(ArchitectureRepository.OTHER_MODELS[1]);
        List<Method> methods = new ArrayList<>(MethodUtil.getAllMethodsFromSetOfElements(architecture.getElements()));
        assertEquals(5, methods.size());
        Set<Method> result = MethodUtil.cloneMethods(new HashSet(methods));
        Iterator<Method> iterator = result.iterator();
        Iterator<Method> methodsIterator = methods.iterator();
        while (iterator.hasNext()) {
            Method resultMethod = iterator.next();
            Method method = methodsIterator.next();
            assertNotSame(method.getId(), resultMethod.getId());
            assertEquals(method.getName(), resultMethod.getName());
            assertEquals(method.getNamespace(), resultMethod.getNamespace());
            assertEquals(method.getOwnConcerns(), resultMethod.getOwnConcerns());
            assertEquals(method.getParameters(), resultMethod.getParameters());
            assertEquals(method.getReturnType(), resultMethod.getReturnType());
        }
    }

    /**
     * Test of mergeMethodsToNewOne method, of class MethodUtil.
     */
    @Test
    public void testMergeMethodsToNewOne() {
        Architecture architecture = ArchitectureRepository.getArchitecture(ArchitectureRepository.OTHER_MODELS[1]);
        Element element = null;
        element = architecture.findClassByName("Class2").get(0);
        Element element2 = null;
        element2 = architecture.findClassByName("Class1").get(0);
        assertEquals("Class2", element.getName());
        assertEquals("Class1", element2.getName());
        List<Method> methods = new ArrayList<>(MethodUtil.getMethodsFromElement(element));
        List<Method> methods2 = new ArrayList<>(MethodUtil.getMethodsFromElement(element2));
        assertEquals(4, methods.size());
        assertEquals(2, methods2.size());
        Method method = null;
        for (int i = 0; i < methods.size(); i++) {
            method = methods.get(i);
            if ("Operation2".equals(method.getName()) && "String".equals(method.getReturnType())) {
                break;
            }
        }
        Method method2 = null;
        for (int i = 0; i < methods2.size(); i++) {
            method2 = methods2.get(i);
            if ("Operation2".equals(method2.getName()) && "String".equals(method2.getReturnType())) {
                break;
            }
        }
        Method result = MethodUtil.mergeMethodsToNewOne(method, method2);
        assertNotSame(method.getId(), result.getId());
        assertNotSame(method2.getId(), result.getId());
        assertEquals("Operation2", result.getName());
        assertEquals(method.getNamespace(), result.getNamespace());
        assertEquals(0, result.getOwnConcerns().size());
        assertEquals(3, result.getParameters().size());
        assertEquals("String", result.getReturnType());
    }

    /**
     * Test of mergeMethodsToMethodA method, of class MethodUtil.
     */
    @Test
    public void testMergeMethodsToMethodA() {
        Architecture architecture = ArchitectureRepository.getArchitecture(ArchitectureRepository.OTHER_MODELS[1]);
        Element element = null;
        element = architecture.findClassByName("Class2").get(0);
        Element element2 = null;
        element2 = architecture.findClassByName("Class1").get(0);
        assertEquals("Class2", element.getName());
        assertEquals("Class1", element2.getName());
        List<Method> methods = new ArrayList<>(MethodUtil.getMethodsFromElement(element));
        List<Method> methods2 = new ArrayList<>(MethodUtil.getMethodsFromElement(element2));
        assertEquals(4, methods.size());
        assertEquals(2, methods2.size());
        Method method = null;
        for (int i = 0; i < methods.size(); i++) {
            method = methods.get(i);
            if ("Operation2".equals(method.getName()) && "String".equals(method.getReturnType())) {
                break;
            }
        }
        Method method2 = null;
        for (int i = 0; i < methods2.size(); i++) {
            method2 = methods2.get(i);
            if ("Operation2".equals(method2.getName()) && "String".equals(method2.getReturnType())) {
                break;
            }
        }
        MethodUtil.mergeMethodsToMethodA(method, method2);
        assertNotSame(method2.getId(), method.getId());
        assertEquals("Operation2", method.getName());
        assertEquals(0, method.getOwnConcerns().size());
        assertEquals(3, method.getParameters().size());
        assertEquals("String", method.getReturnType());
    }

    /**
     * Test of getAllMethodsFromElement method, of class MethodUtil.
     */
    @Test
    public void testGetAllMethodsFromElement() {
        String model = ArchitectureRepository.STRATEGY_MODELS[3];
        Architecture architecture = ArchitectureRepository.getArchitecture(model);
        Element element = null;
        element = architecture.findClassByName("QuickSort").get(0);
        assertEquals("QuickSort", element.getName());
        assertEquals(2, ElementUtil.getRelationships(element).size());
        List<Method> allMethodsFromElement = MethodUtil.getAllMethodsFromElement(element);
        assertEquals(1, allMethodsFromElement.size());
    }

    /**
     * Test of getAllMethodsFromSetOfElementsByConcern method, of class MethodUtil.
     */
    @Test
    public void testGetAllMethodsFromSetOfElementsByConcern() {
        Architecture architecture = ArchitectureRepository.getArchitecture(ArchitectureRepository.OTHER_MODELS[3]);
        ArrayList<Element> arrayList = new ArrayList<>(architecture.getElements());
        List<Method> allMethodsFromSetOfElementsByConcern = MethodUtil.getAllMethodsFromSetOfElementsByConcern(arrayList, new Concern("bowling"));
        assertEquals(1, allMethodsFromSetOfElementsByConcern.size());
        allMethodsFromSetOfElementsByConcern = MethodUtil.getAllMethodsFromSetOfElementsByConcern(arrayList, new Concern("collision"));
        assertEquals(2, allMethodsFromSetOfElementsByConcern.size());
    }

    /**
     * Test of createMethodsFromSetOfElementsByConcern method, of class MethodUtil.
     */
    @Test
    public void testCreateMethodsFromSetOfElementsByConcern() {
        String model = ArchitectureRepository.OTHER_MODELS[3];
        Architecture architecture = ArchitectureRepository.getArchitecture(model);

        List<Method> result = MethodUtil.createMethodsFromSetOfElementsByConcern(architecture.getElements(), new Concern("bowling"));
        assertEquals(1, result.size());
        assertEquals(1, result.get(0).getParameters().size());
        assertEquals(1, result.get(0).getAllConcerns().size());

        result = MethodUtil.createMethodsFromSetOfElementsByConcern(architecture.getElements(), new Concern("collision"));
        assertEquals(2, result.size());
    }

    @Test
    public void testGetAllMethodsFromElementByConcern() {
        String model = ArchitectureRepository.OTHER_MODELS[3];
        Architecture architecture = ArchitectureRepository.getArchitecture(model);

        Element element = architecture.findClassByName("Class3").get(0);

        List<Method> methods = MethodUtil.getAllMethodsFromElementByConcern(element, null);
        assertEquals(1, methods.size());
        assertEquals("nothing", methods.get(0).getName());
        assertEquals("", methods.get(0).getReturnType());

        methods = MethodUtil.getAllMethodsFromElementByConcern(element, new Concern("brickles"));
        assertEquals(1, methods.size());
        assertEquals("nothing", methods.get(0).getName());
        assertEquals("Integer", methods.get(0).getReturnType());

        element = architecture.findClassByName("Class2").get(0);

        methods = MethodUtil.getAllMethodsFromElementByConcern(element, new Concern("collision"));
        assertEquals(2, methods.size());
    }

    @Test
    public void testGetAllCommonMethodsFromSetOfElements() {
        String model = ArchitectureRepository.OTHER_MODELS[3];
        Architecture architecture = ArchitectureRepository.getArchitecture(model);

        Element klass1 = architecture.findClassByName("Class1").get(0);
        Element klass2 = architecture.findClassByName("Class2").get(0);
        Element klass3 = architecture.findClassByName("Class3").get(0);
        Element klass4 = architecture.findClassByName("Class4").get(0);

        List<Method> methods = MethodUtil.getAllCommonMethodsFromSetOfElements(architecture.getElements());
        assertTrue(methods.isEmpty());

        List<Element> elements = new ArrayList<>();
        elements.add(klass4);
        elements.add(klass3);

        methods = MethodUtil.getAllCommonMethodsFromSetOfElements(elements);
        assertEquals(1, methods.size());

        elements = new ArrayList<>();
        elements.add(klass1);
        elements.add(klass2);

        methods = MethodUtil.getAllCommonMethodsFromSetOfElements(elements);
        assertEquals(1, methods.size());
    }

    @Test
    public void testGetAllCommonMethodsFromSetOfElementsByConcern() {
        String model = ArchitectureRepository.OTHER_MODELS[3];
        Architecture architecture = ArchitectureRepository.getArchitecture(model);

        Element klass1 = architecture.findClassByName("Class1").get(0);
        Element klass2 = architecture.findClassByName("Class2").get(0);
        Element klass3 = architecture.findClassByName("Class3").get(0);
        Element klass4 = architecture.findClassByName("Class4").get(0);

        List<Element> elements = new ArrayList<>();
        elements.add(klass4);
        elements.add(klass3);

        List<Method> methods = MethodUtil.getAllCommonMethodsFromSetOfElementsByConcern(elements, null);
        assertEquals(1, methods.size());

        elements = new ArrayList<>();
        elements.add(klass1);
        elements.add(klass2);

        methods = MethodUtil.getAllCommonMethodsFromSetOfElementsByConcern(elements, new Concern("bowling"));
        assertEquals(1, methods.size());
    }
}
