/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ufpr.inf.opla.patterns.util;

import arquitetura.representation.Architecture;
import arquitetura.representation.Element;
import arquitetura.representation.Interface;
import arquitetura.representation.Method;
import br.ufpr.inf.opla.patterns.repositories.ArchitectureRepository;
import java.util.ArrayList;
import java.util.List;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import org.junit.Test;

/**
 *
 * @author giovaniguizzo
 */
public class InterfaceUtilTest {

    public InterfaceUtilTest() {
    }

    /**
     * Test of createInterfaceForSetOfElements method, of class InterfaceUtil.
     */
    @Test
    public void testCreateInterfaceForSetOfElements() {
        String interfaceName = "TesteInterface";
        Architecture architecture = ArchitectureRepository.getArchitecture(ArchitectureRepository.STRATEGY_MODELS[2]);
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
}
