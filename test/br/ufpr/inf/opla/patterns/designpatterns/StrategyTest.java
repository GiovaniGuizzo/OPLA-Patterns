package br.ufpr.inf.opla.patterns.designpatterns;

import arquitetura.representation.Interface;
import arquitetura.representation.Method;
import br.ufpr.inf.opla.patterns.models.Scope;
import br.ufpr.inf.opla.patterns.models.ps.PS;
import br.ufpr.inf.opla.patterns.models.ps.PSPLA;
import br.ufpr.inf.opla.patterns.models.ps.impl.PSPLAStrategy;
import br.ufpr.inf.opla.patterns.models.ps.impl.PSStrategy;
import br.ufpr.inf.opla.patterns.strategies.ScopeSelectionStrategy;
import br.ufpr.inf.opla.patterns.strategies.impl.WholeArchitectureScopeSelection;
import br.ufpr.inf.opla.patterns.repositories.ArchitectureRepositoryFlyweight;
import br.ufpr.inf.opla.patterns.util.AlgorithmFamilyUtil;
import br.ufpr.inf.opla.patterns.util.MethodUtil;
import java.util.List;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

public class StrategyTest {

    private final Strategy strategy;
    private final ScopeSelectionStrategy scopeSelectionStrategy;
    private final ArchitectureRepositoryFlyweight architectureRepository;

    public StrategyTest() {
        this.strategy = Strategy.getInstance();
        this.architectureRepository = ArchitectureRepositoryFlyweight.getInstance();
        this.scopeSelectionStrategy = new WholeArchitectureScopeSelection();
    }

    /**
     * Verifies if there are some PSs in the scope. There should be 3 PSs, with 4 members each (1 context and 3 algorithms).
     */
    @Test
    public void verifyPSTest() {
        Scope scope = scopeSelectionStrategy.selectScope(architectureRepository.getArchitecture(ArchitectureRepositoryFlyweight.STRATEGY_MODELS[0]));

        assertEquals(8, scope.getElements().size());

        boolean verifyPS = strategy.verifyPS(scope);
        assertTrue(verifyPS);

        List<PS> psList = scope.getPS();
        assertEquals(3, psList.size());

        for (PS ps : psList) {
            PSStrategy psStrategy = (PSStrategy) ps;
            assertEquals(4, psStrategy.getParticipants().size());
            assertEquals(3, psStrategy.getAlgorithmFamily().getParticipants().size());
            assertEquals(1, psStrategy.getContexts().size());
        }
    }

    /**
     * Verifies if the Algorithm Family "sort" is the best one.
     */
    @Test
    public void verifyPSTest2() {
        Scope scope = scopeSelectionStrategy.selectScope(architectureRepository.getArchitecture(ArchitectureRepositoryFlyweight.STRATEGY_MODELS[1]));

        strategy.verifyPS(scope);

        List<PS> psList = scope.getPS();

        PSStrategy pSStrategy = (PSStrategy) psList.get(0);
        assertEquals("sort", pSStrategy.getAlgorithmFamily().getName());
        assertEquals(4, pSStrategy.getAlgorithmFamily().getParticipants().size());
        assertEquals(2, pSStrategy.getContexts().size());
    }

    /**
     * Verifies if there is some PSs-PLA in the scope. There should be 3 PSs-PLA, with 4 members each (1 context and 3 algorithms).
     */
    @Test
    public void verifyPSPLATest() {
        Scope scope = scopeSelectionStrategy.selectScope(architectureRepository.getArchitecture(ArchitectureRepositoryFlyweight.STRATEGY_MODELS[0]));

        assertEquals(8, scope.getElements().size());

        boolean verifyPSPLA = strategy.verifyPSPLA(scope);
        assertTrue(verifyPSPLA);

        List<PSPLA> psPLAList = scope.getPSPLA();
        assertEquals(3, psPLAList.size());

        for (PSPLA psPLA : psPLAList) {
            PSPLAStrategy psPLAStrategy = (PSPLAStrategy) psPLA;
            assertEquals(4, psPLAStrategy.getParticipants().size());
            assertEquals(3, psPLAStrategy.getAlgorithmFamily().getParticipants().size());
            assertEquals(1, psPLAStrategy.getContexts().size());
        }
    }

    /**
     * Verifies if there is no PSs-PLA in the scope.
     */
    @Test
    public void verifyPSPLATest2() {
        Scope scope = scopeSelectionStrategy.selectScope(architectureRepository.getArchitecture(ArchitectureRepositoryFlyweight.STRATEGY_MODELS[1]));

        assertEquals(8, scope.getElements().size());

        boolean verifyPSPLA = strategy.verifyPSPLA(scope);
        assertFalse(verifyPSPLA);
    }

    /**
     * Tests the Strategy application.
     */
//    @Ignore
//    @Test
//    public void applyTest() {
//
//    }
}
