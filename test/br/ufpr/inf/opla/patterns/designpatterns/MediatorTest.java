/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ufpr.inf.opla.patterns.designpatterns;

import arquitetura.representation.Architecture;
import arquitetura.representation.Element;
import br.ufpr.inf.opla.patterns.models.Scope;
import br.ufpr.inf.opla.patterns.repositories.ArchitectureRepository;
import br.ufpr.inf.opla.patterns.strategies.ScopeSelectionStrategy;
import br.ufpr.inf.opla.patterns.strategies.impl.WholeArchitectureScopeSelection;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 * @author giovaniguizzo
 */
public class MediatorTest {

    private final Mediator mediator;
    private final ScopeSelectionStrategy scopeSelectionStrategy;

    public MediatorTest() {
        this.mediator = Mediator.getInstance();
        this.scopeSelectionStrategy = new WholeArchitectureScopeSelection();
    }

    @Test
    public void testVerifyPS() {
        String model = ArchitectureRepository.OTHER_MODELS[7];
        Architecture architecture = ArchitectureRepository.getArchitecture(model);

        Element klass2 = architecture.findClassByName("Class2").get(0);
        Scope scope = new Scope();
        scope.getElements().add(klass2);

        Assert.assertTrue(mediator.verifyPS(scope));
    }

    @Test
    public void testApply() {
    }

}
