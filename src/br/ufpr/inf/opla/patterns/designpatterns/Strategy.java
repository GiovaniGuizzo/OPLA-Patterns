package br.ufpr.inf.opla.patterns.designpatterns;

import arquitetura.representation.Element;
import arquitetura.representation.Variability;
import arquitetura.representation.relationship.Relationship;
import br.ufpr.inf.opla.patterns.models.DesignPattern;
import br.ufpr.inf.opla.patterns.models.Scope;
import br.ufpr.inf.opla.patterns.models.AlgorithmFamily;
import br.ufpr.inf.opla.patterns.models.ps.PS;
import br.ufpr.inf.opla.patterns.models.ps.impl.PSPLAStrategy;
import br.ufpr.inf.opla.patterns.models.ps.impl.PSStrategy;
import br.ufpr.inf.opla.patterns.util.AlgorithmFamilyUtil;
import br.ufpr.inf.opla.patterns.util.RelationshipUtil;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Strategy extends DesignPattern {

    private static final Strategy INSTANCE = new Strategy();
    //private static final String[] CONSIDERED_RELATIONSHIPS = new String[]{"usage","dependency"};

    private final AlgorithmFamilyUtil algorithmFamilyUtil;
    private final RelationshipUtil relationshipUtil;

    private Strategy() {
        super("Strategy", "Behavioral");
        relationshipUtil = RelationshipUtil.getInstance();
        this.algorithmFamilyUtil = AlgorithmFamilyUtil.getInstance();
    }

    public static Strategy getInstance() {
        return INSTANCE;
    }

    @Override
    public boolean verifyPS(Scope scope) {
        List<AlgorithmFamily> familiesInScope = new ArrayList<>();

        algorithmFamilyUtil.addFamiliesWithSuffixAndPreffix(scope, familiesInScope);
        algorithmFamilyUtil.addFamiliesWithSameMethod(scope, familiesInScope);

        Collections.sort(familiesInScope);

        boolean isPs = false;
        for (int i = familiesInScope.size() - 1; i >= 0; i--) {
            AlgorithmFamily iFamily = familiesInScope.get(i);
            List<Element> participants = iFamily.getParticipants();
            List<Element> elementsInScope = new ArrayList<>(scope.getElements());
            elementsInScope.removeAll(participants);
            List<Element> contexts = new ArrayList<>();
            for (Element element : elementsInScope) {
                List<Element> usedElements = new ArrayList<>();
                for (Relationship relationship : element.getRelationships()) {
                    List<Element> tempUsedElements = relationshipUtil.getUsedElementsFromRelationship(relationship);
                    tempUsedElements.remove(element);
                    usedElements.addAll(tempUsedElements);
                }
                for (int j = 0; j < usedElements.size(); j++) {
                    if (!participants.contains(usedElements.get(j))) {
                        usedElements.remove(j);
                        j--;
                    }
                }
                if (!usedElements.isEmpty()) {
                    contexts.add(element);
                }
            }
            if (!contexts.isEmpty()) {
                PSStrategy psStrategy = new PSStrategy(contexts, iFamily);
                if (!scope.getPs().contains(psStrategy)) {
                    scope.addPs(psStrategy);
                }
                isPs = true;
            }
        }
        return isPs;
    }

    @Override
    public boolean verifyPSPLA(Scope scope) {
        boolean isPsPla = false;
        if (verifyPS(scope)) {
            pSIteratorFor:
            for (PS ps : scope.getPs()) {
                if (ps.getPsOf().equals(this)) {
                    PSStrategy psStrategy = (PSStrategy) ps;
                    List<Variability> variabilities = null;
                    for (Element algorithm : psStrategy.getAlgorithmFamily().getParticipants()) {
                        if (algorithm.getVariant() == null) {
                            continue pSIteratorFor;
                        }
                        if (variabilities == null) {
                            variabilities = new ArrayList<>();
                            variabilities.addAll(algorithm.getVariant().getVariabilities());
                        } else if (!variabilities.isEmpty()) {
                            for (int i = 0; i < variabilities.size(); i++) {
                                if (!algorithm.getVariant().getVariabilities().contains(variabilities.get(i))) {
                                    variabilities.remove(i);
                                    i--;
                                }
                            }
                        } else {
                            continue pSIteratorFor;
                        }
                    }
                    if (variabilities == null || variabilities.isEmpty()) {
                        continue;
                    }
                    for (Element context : psStrategy.getContexts()) {
                        if (context.getVariationPoint() != null) {
                            List<Variability> contextVariabilities = context.getVariationPoint().getVariabilities();
                            for (Variability variability : contextVariabilities) {
                                if (variabilities.contains(variability)) {
                                    PSPLAStrategy psPlaStrategy = new PSPLAStrategy(psStrategy.getContexts(), psStrategy.getAlgorithmFamily());
                                    if (!scope.getPsPla().contains(psPlaStrategy)) {
                                        scope.addPsPla(psPlaStrategy);
                                    }
                                    isPsPla = true;
                                    continue pSIteratorFor;
                                }
                            }
                        }
                    }
                }
            }
        }
        return isPsPla;
    }

    @Override
    public boolean apply(Scope scope) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
