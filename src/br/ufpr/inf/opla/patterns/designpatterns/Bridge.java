package br.ufpr.inf.opla.patterns.designpatterns;

import arquitetura.representation.Concern;
import arquitetura.representation.Element;
import br.ufpr.inf.opla.patterns.models.AlgorithmFamily;
import br.ufpr.inf.opla.patterns.models.DesignPattern;
import br.ufpr.inf.opla.patterns.models.Scope;
import br.ufpr.inf.opla.patterns.models.ps.PS;
import br.ufpr.inf.opla.patterns.models.ps.impl.PSBridge;
import br.ufpr.inf.opla.patterns.models.ps.impl.PSPLABridge;
import br.ufpr.inf.opla.patterns.models.ps.impl.PSStrategy;
import br.ufpr.inf.opla.patterns.util.BridgeUtil;
import br.ufpr.inf.opla.patterns.util.ElementUtil;
import br.ufpr.inf.opla.patterns.util.StrategyUtil;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class Bridge extends DesignPattern {

    private static final Bridge INSTANCE = new Bridge();

    private Bridge() {
        super("Bridge", "Structural");
    }

    public static Bridge getInstance() {
        return INSTANCE;
    }

    @Override
    public boolean verifyPS(Scope scope) {
        boolean isPS = false;
        if (Strategy.getInstance().verifyPS(scope)) {
            List<PS> psStrategyList = scope.getPSs(Strategy.getInstance());
            for (Iterator<PS> it = psStrategyList.iterator(); it.hasNext();) {
                PSStrategy psStrategy = (PSStrategy) it.next();
                Set<Concern> commonConcerns = ElementUtil.getCommonConcernsOfAtLeastTwoElements(psStrategy.getAlgorithmFamily().getParticipants());
                if (!commonConcerns.isEmpty()) {
                    PSBridge psBridge = new PSBridge(psStrategy.getContexts(), psStrategy.getAlgorithmFamily(), new ArrayList<>(commonConcerns));
                    if (!scope.getPSs(this).contains(psBridge)) {
                        scope.addPS(psBridge);
                    }
                    isPS = true;
                }
            }
        }
        return isPS;
    }

    @Override
    public boolean verifyPSPLA(Scope scope) {
        boolean psPLA = false;
        if (verifyPS(scope)) {
            for (PS ps : scope.getPSs(this)) {
                PSBridge psBridge = (PSBridge) ps;
                List<Element> contexts = psBridge.getContexts();
                AlgorithmFamily algorithmFamily = psBridge.getAlgorithmFamily();
                if (StrategyUtil.areTheAlgorithmFamilyAndContextsPartOfAVariability(algorithmFamily, contexts)) {
                    PSPLABridge psPlaBridge = new PSPLABridge(contexts, algorithmFamily, psBridge.getCommonConcerns());
                    if (!scope.getPSsPLA(this).contains(psPlaBridge)) {
                        scope.addPSPLA(psPlaBridge);
                        psPLA = true;
                    }
                }
            }
        }
        return psPLA;
    }

    @Override
    public boolean apply(Scope scope) {
        boolean applied = false;
        List<PS> pSs = scope.getPSs(this);
        if(!pSs.isEmpty()){
            PSBridge psBridge = (PSBridge) pSs.get(0);
            AlgorithmFamily algorithmFamily = psBridge.getAlgorithmFamily();
            List<Element> abstractionClasses = BridgeUtil.getAbstractionClasses(scope, algorithmFamily);
        }
        return applied;
    }

}
