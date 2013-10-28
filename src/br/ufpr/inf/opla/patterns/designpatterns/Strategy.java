package br.ufpr.inf.opla.patterns.designpatterns;

import arquitetura.representation.Element;
import arquitetura.representation.Interface;
import arquitetura.representation.Variability;
import arquitetura.representation.relationship.RealizationRelationship;
import arquitetura.representation.relationship.Relationship;
import br.ufpr.inf.opla.patterns.models.AlgorithmFamily;
import br.ufpr.inf.opla.patterns.models.DesignPattern;
import br.ufpr.inf.opla.patterns.models.Scope;
import br.ufpr.inf.opla.patterns.models.ps.PS;
import br.ufpr.inf.opla.patterns.models.ps.impl.PSPLAStrategy;
import br.ufpr.inf.opla.patterns.models.ps.impl.PSStrategy;
import br.ufpr.inf.opla.patterns.util.AlgorithmFamilyUtil;
import br.ufpr.inf.opla.patterns.util.RelationshipUtil;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Strategy extends DesignPattern {

    private static final Strategy INSTANCE = new Strategy();

    private Strategy() {
        super("Strategy", "Behavioral");
    }

    public static Strategy getInstance() {
        return INSTANCE;
    }

    @Override
    public boolean verifyPS(Scope scope) {
        List<AlgorithmFamily> familiesInScope = AlgorithmFamilyUtil.getFamiliesFromScope(scope);

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
                    Element usedElement = RelationshipUtil.getUsedElementFromRelationship(relationship);
                    if (usedElement != null && !usedElement.equals(element)) {
                        usedElements.add(usedElement);
                    }
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
                if (!scope.getPS().contains(psStrategy)) {
                    scope.addPS(psStrategy);
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
            for (PS ps : scope.getPS()) {
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
                                    if (!scope.getPSPLA().contains(psPlaStrategy)) {
                                        scope.addPSPLA(psPlaStrategy);
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
        boolean applied = false;
        if (scope.isPS()) {
            PSStrategy ps = (PSStrategy) scope.getPS().get(0);
            AlgorithmFamily algorithmFamily = ps.getAlgorithmFamily();

            Interface strategyInterface = AlgorithmFamilyUtil.getStrategyInterfaceFromAlgorithmFamily(ps.getAlgorithmFamily());
            if (strategyInterface == null) {
                strategyInterface = AlgorithmFamilyUtil.createStrategyInterfaceForAlgorithmFamily(algorithmFamily);
            }
            List<Element> participants = ps.getAlgorithmFamily().getParticipants();
            List<Relationship> interfaceRealizationRelationships = new ArrayList<>(strategyInterface.getRelationships());
            for (int i = 0; i < interfaceRealizationRelationships.size(); i++) {
                Relationship relationship = interfaceRealizationRelationships.get(i);
                if (!(relationship instanceof RealizationRelationship) || !RelationshipUtil.getImplementedInterface(relationship).equals(strategyInterface)) {
                    interfaceRealizationRelationships.remove(i);
                    i--;
                }
            }
            for (Element participant : participants) {
                boolean hasRelationship = false;
                for (Relationship elementRelationship : participant.getRelationships()) {
                    if (interfaceRealizationRelationships.contains(elementRelationship)) {
                        hasRelationship = true;
                        break;
                    }
                }
                if (!hasRelationship) {
                    RealizationRelationship realizationRelationship = new RealizationRelationship(participant, strategyInterface, "implements", UUID.randomUUID().toString());
                    participant.getRelationships().add(realizationRelationship);
                    strategyInterface.getRelationships().add(realizationRelationship);
                }
            }

            //Move relationships
            for (Element context : ps.getContexts()) {
                HashMap<String, HashMap<String, List<Relationship>>> usingRelationshipsFromAlgorithms = new HashMap<>();
                for (Relationship relationShip : context.getRelationships()) {
                    Element usedElementFromRelationship = RelationshipUtil.getUsedElementFromRelationship(relationShip);
                    if (!usedElementFromRelationship.equals(context)
                            && (participants.contains(usedElementFromRelationship)
                            || strategyInterface.equals(usedElementFromRelationship))) {
                        HashMap<String, List<Relationship>> relationshipByType = usingRelationshipsFromAlgorithms.get(relationShip.getType());
                        if (relationshipByType == null) {
                            relationshipByType = new HashMap<>();
                            usingRelationshipsFromAlgorithms.put(relationShip.getType(), relationshipByType);
                        }
                        List<Relationship> relationshipByName = relationshipByType.get(relationShip.getName());
                        if (relationshipByName == null) {
                            relationshipByName = new ArrayList<>();
                            relationshipByType.put(relationShip.getName(), relationshipByName);
                        }
                        relationshipByName.add(relationShip);
                    }
                }
                for (Map.Entry<String, HashMap<String, List<Relationship>>> byTypeEntry : usingRelationshipsFromAlgorithms.entrySet()) {
                    String typeKey = byTypeEntry.getKey();
                    HashMap<String, List<Relationship>> typeMap = byTypeEntry.getValue();
                    for (Map.Entry<String, List<Relationship>> nameEntry : typeMap.entrySet()) {
                        String nameKey = nameEntry.getKey();
                        List<Relationship> nameList = nameEntry.getValue();
                        Relationship relationship = nameList.get(0);

                        for (Relationship tempRelationship : nameList) {
                            Element usedElementFromRelationship = RelationshipUtil.getUsedElementFromRelationship(relationship);
                            usedElementFromRelationship.getRelationships().remove(tempRelationship);
                            context.getRelationships().remove(relationship);
                        }

                        context.getRelationships().add(relationship);
                        strategyInterface.getRelationships().add(relationship);

                        RelationshipUtil.setRelationshipClientAndSupplier(relationship, context, strategyInterface);
                    }
                }
            }
        }
        return applied;
    }
}
