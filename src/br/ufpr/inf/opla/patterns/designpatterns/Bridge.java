package br.ufpr.inf.opla.patterns.designpatterns;

import arquitetura.representation.Concern;
import arquitetura.representation.Element;
import arquitetura.representation.Method;
import br.ufpr.inf.opla.patterns.models.AlgorithmFamily;
import br.ufpr.inf.opla.patterns.models.DesignPattern;
import br.ufpr.inf.opla.patterns.models.Scope;
import br.ufpr.inf.opla.patterns.models.ps.PS;
import br.ufpr.inf.opla.patterns.models.ps.impl.PSBridge;
import br.ufpr.inf.opla.patterns.models.ps.impl.PSPLABridge;
import br.ufpr.inf.opla.patterns.models.ps.impl.PSStrategy;
import br.ufpr.inf.opla.patterns.util.AlgorithmFamilyUtil;
import br.ufpr.inf.opla.patterns.util.MethodUtil;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import org.apache.commons.collections4.CollectionUtils;

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

                Set<Concern> commonConcerns = new HashSet<>();
                Element element = psStrategy.getParticipants().get(0);
                commonConcerns.addAll(element.getOwnConcerns());
                for (Method method : MethodUtil.getAllMethodsFromElement(element)) {
                    commonConcerns.addAll(method.getOwnConcerns());
                }

                for (Element participant : psStrategy.getParticipants()) {
                    Set<Concern> participantConcerns = new HashSet<>();
                    participantConcerns.addAll(participant.getOwnConcerns());
                    for (Method method : MethodUtil.getAllMethodsFromElement(participant)) {
                        participantConcerns.addAll(method.getOwnConcerns());
                    }
                    commonConcerns = new HashSet<>(CollectionUtils.intersection(commonConcerns, participantConcerns));
                }
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
                if (AlgorithmFamilyUtil.areTheAlgorithmFamilyAndContextsPartOfAVariability(algorithmFamily, contexts)) {
                    PSPLABridge psPlaBridge = new PSPLABridge(contexts, algorithmFamily, psBridge.getCommonConcerns());
                    if (!scope.getPSsPLA(this).contains(psPlaBridge)) {
                        scope.addPSPLA(psPlaBridge);
                    }
                }
            }
        }
        return psPLA;
    }

    @Override
    public boolean apply(Scope scope) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
