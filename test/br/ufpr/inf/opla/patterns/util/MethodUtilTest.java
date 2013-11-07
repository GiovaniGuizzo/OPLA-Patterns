/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ufpr.inf.opla.patterns.util;

import arquitetura.representation.Architecture;
import arquitetura.representation.Element;
import arquitetura.representation.Method;
import br.ufpr.inf.opla.patterns.repositories.ArchitectureRepositoryFlyweight;
import java.util.List;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author giovaniguizzo
 */
public class MethodUtilTest {

    private final ArchitectureRepositoryFlyweight architectureRepository;

    public MethodUtilTest() {
        this.architectureRepository = ArchitectureRepositoryFlyweight.getInstance();
    }

    /**
     * Test of getMethodsFromElement method, of class MethodUtil.
     */
    @Test
    public void testGetMethodsFromElement() {
        Architecture architecture = architectureRepository.getArchitecture(ArchitectureRepositoryFlyweight.OTHER_MODELS[1]);
        Element element = architecture.getElements().get(1);
        assertEquals("Class2", element.getName());
        List<Method> result = MethodUtil.getMethodsFromElement(element);
        assertEquals(4, result.size());
    }

    /**
     * Test of getMethodsFromSetOfElements method, of class MethodUtil.
     */
    @Test
    public void testGetMethodsFromSetOfElements() {
        Architecture architecture = architectureRepository.getArchitecture(ArchitectureRepositoryFlyweight.OTHER_MODELS[1]);
        List<Method> result = MethodUtil.getMethodsFromSetOfElements(architecture.getElements());
        assertEquals(5, result.size());
    }

    /**
     * Test of createMethodsFromSetOfElements method, of class MethodUtil.
     */
    @Test
    public void testCreateMethodsFromSetOfElements() {
        String model = ArchitectureRepositoryFlyweight.OTHER_MODELS[1];
        architectureRepository.clearArchitecture(model);
        Architecture architecture = architectureRepository.getArchitecture(model);
        List<Method> result = MethodUtil.createMethodsFromSetOfElements(architecture.getElements());
        assertEquals(5, result.size());
        Method method = result.get(1);
        assertEquals("Operation2", method.getName());
        assertEquals("String", method.getReturnType());
        assertEquals(3, method.getParameters().size());
    }

    /**
     * Test of cloneMethod method, of class MethodUtil.
     */
    @Test
    public void testCloneMethod() {
        Architecture architecture = architectureRepository.getArchitecture(ArchitectureRepositoryFlyweight.OTHER_MODELS[1]);
        Element element = architecture.getElements().get(1);
        assertEquals("Class2", element.getName());
        List<Method> methods = MethodUtil.getMethodsFromElement(element);
        assertEquals(4, methods.size());
        Method method = methods.get(3);
        assertEquals("Operation2", method.getName());
        assertEquals("String", method.getReturnType());
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
        Architecture architecture = architectureRepository.getArchitecture(ArchitectureRepositoryFlyweight.OTHER_MODELS[1]);
        List<Method> methods = MethodUtil.getMethodsFromSetOfElements(architecture.getElements());
        assertEquals(5, methods.size());
        List<Method> result = MethodUtil.cloneMethods(methods);
        for (int i = 0; i < result.size(); i++) {
            Method resultMethod = result.get(i);
            Method method = methods.get(i);
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
        Architecture architecture = architectureRepository.getArchitecture(ArchitectureRepositoryFlyweight.OTHER_MODELS[1]);
        Element element = architecture.getElements().get(1);
        Element element2 = architecture.getElements().get(0);
        assertEquals("Class2", element.getName());
        assertEquals("Class1", element2.getName());
        List<Method> methods = MethodUtil.getMethodsFromElement(element);
        List<Method> methods2 = MethodUtil.getMethodsFromElement(element2);
        assertEquals(4, methods.size());
        assertEquals(2, methods2.size());
        Method method = methods.get(3);
        assertEquals("Operation2", method.getName());
        assertEquals("String", method.getReturnType());
        Method method2 = methods2.get(1);
        assertEquals("Operation2", method2.getName());
        assertEquals("String", method2.getReturnType());
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
        Architecture architecture = architectureRepository.getArchitecture(ArchitectureRepositoryFlyweight.OTHER_MODELS[1]);
        Element element = architecture.getElements().get(1);
        Element element2 = architecture.getElements().get(0);
        assertEquals("Class2", element.getName());
        assertEquals("Class1", element2.getName());
        List<Method> methods = MethodUtil.getMethodsFromElement(element);
        List<Method> methods2 = MethodUtil.getMethodsFromElement(element2);
        assertEquals(4, methods.size());
        assertEquals(2, methods2.size());
        Method method = methods.get(3);
        assertEquals("Operation2", method.getName());
        assertEquals("String", method.getReturnType());
        Method method2 = methods2.get(1);
        assertEquals("Operation2", method2.getName());
        assertEquals("String", method2.getReturnType());
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
        String model = ArchitectureRepositoryFlyweight.STRATEGY_MODELS[3];
        Architecture architecture = architectureRepository.getArchitecture(model);
        Element element = architecture.getElements().get(0);
        assertEquals("QuickSort", element.getName());
        assertEquals(2, element.getRelationships().size());
        List<Method> allMethodsFromElement = MethodUtil.getAllMethodsFromElement(element);
        assertEquals(2, allMethodsFromElement.size());
    }
}
