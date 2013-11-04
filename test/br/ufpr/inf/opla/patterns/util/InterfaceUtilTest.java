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
import br.ufpr.inf.opla.patterns.repositories.ArchitectureRepositoryFlyweight;
import java.util.List;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author giovaniguizzo
 */
public class InterfaceUtilTest {

    private final ArchitectureRepositoryFlyweight architectureRepository;

    public InterfaceUtilTest() {
        this.architectureRepository = ArchitectureRepositoryFlyweight.getInstance();
    }

    /**
     * Test of createInterfaceForSetOfElements method, of class InterfaceUtil.
     */
    @Test
    public void testCreateInterfaceForSetOfElements() {
        String interfaceName = "TesteInterface";
        Architecture architecture = architectureRepository.getArchitecture(ArchitectureRepositoryFlyweight.STRATEGY_MODELS[2]);
        List<Element> participants = architecture.getElements();
        Interface result = InterfaceUtil.createInterfaceForSetOfElements(interfaceName, participants);
        assertEquals("Model::ClassPackage", result.getNamespace());
        List<Method> operations = result.getOperations();
        assertEquals(3, operations.size());
        assertEquals("methodA", operations.get(0).getName());
        //TODO - Édipo - Testar.
        //assertFalse(operations.get(1).getName().equals(operations.get(2).getName()));
        assertEquals("String", operations.get(1).getReturnType());
        assertEquals("Integer", operations.get(2).getReturnType());        
    }
}
