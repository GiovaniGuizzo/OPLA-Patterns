/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ufpr.inf.opla.patterns.util;

import arquitetura.representation.ParameterMethod;
import br.ufpr.inf.opla.patterns.repositories.ArchitectureRepositoryFlyweight;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

/**
 *
 * @author giovaniguizzo
 */
public class ParameterMethodUtilTest {

    private final ArchitectureRepositoryFlyweight architectureRepository;

    public ParameterMethodUtilTest() {
        this.architectureRepository = ArchitectureRepositoryFlyweight.getInstance();
    }

    /**
     * Test of cloneParameter method, of class ParameterMethodUtil.
     */
    @Test
    public void testCloneParameter() {
        ParameterMethod parameterMethod = new ParameterMethod("Teste", "Teste2", "Teste3");
        ParameterMethod clonedParameter = ParameterMethodUtil.cloneParameter(parameterMethod);
        assertTrue(parameterMethod.getName().equals(clonedParameter.getName()));
        assertTrue(parameterMethod.getType().equals(clonedParameter.getType()));
        assertTrue(parameterMethod.getDirection().equals(clonedParameter.getDirection()));
        assertFalse(parameterMethod == clonedParameter);
    }

}
