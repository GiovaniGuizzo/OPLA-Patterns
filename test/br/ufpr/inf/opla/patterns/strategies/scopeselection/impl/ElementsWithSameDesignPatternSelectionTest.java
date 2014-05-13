package br.ufpr.inf.opla.patterns.strategies.scopeselection.impl;

import arquitetura.representation.Architecture;
import arquitetura.representation.Patterns;
import br.ufpr.inf.opla.patterns.models.Scope;
import br.ufpr.inf.opla.patterns.repositories.ArchitectureRepository;
import br.ufpr.inf.opla.patterns.strategies.scopeselection.ScopeSelectionStrategy;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

public class ElementsWithSameDesignPatternSelectionTest {

    public ElementsWithSameDesignPatternSelectionTest() {
    }

    @Test
    public void testSelectScope() {
        Architecture architecture = ArchitectureRepository.getArchitecture(ArchitectureRepository.OTHER_MODELS[8]);
        ScopeSelectionStrategy strategy = new ElementsWithSameDesignPatternSelection(new WholeArchitectureScopeSelection());
        Scope scope = strategy.selectScope(architecture, Patterns.BRIDGE);

        arquitetura.representation.Class klass1 = architecture.findClassByName("Class1").get(0);
        arquitetura.representation.Class klass2 = architecture.findClassByName("Class2").get(0);
        arquitetura.representation.Class klass3 = architecture.findClassByName("Class3").get(0);
        arquitetura.representation.Class klass4 = architecture.findClassByName("Class4").get(0);

        assertEquals(3, scope.getElements().size());
        assertTrue(scope.getElements().contains(klass1));
        assertTrue(scope.getElements().contains(klass2));
        assertTrue(scope.getElements().contains(klass3));
        assertFalse(scope.getElements().contains(klass4));
    }

}
