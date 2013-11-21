package br.ufpr.inf.opla.patterns.designpatterns;

import arquitetura.exceptions.ConcernNotFoundException;
import arquitetura.representation.Class;
import arquitetura.representation.Concern;
import arquitetura.representation.Element;
import arquitetura.representation.Interface;
import arquitetura.representation.Method;
import arquitetura.representation.Variability;
import arquitetura.representation.Variant;
import arquitetura.representation.VariationPoint;
import arquitetura.representation.relationship.Relationship;
import br.ufpr.inf.opla.patterns.list.MethodArrayList;
import br.ufpr.inf.opla.patterns.models.AlgorithmFamily;
import br.ufpr.inf.opla.patterns.models.DesignPattern;
import br.ufpr.inf.opla.patterns.models.Scope;
import br.ufpr.inf.opla.patterns.models.ps.PS;
import br.ufpr.inf.opla.patterns.models.ps.impl.PSPLAStrategy;
import br.ufpr.inf.opla.patterns.models.ps.impl.PSStrategy;
import br.ufpr.inf.opla.patterns.util.AlgorithmFamilyUtil;
import br.ufpr.inf.opla.patterns.util.ElementUtil;
import br.ufpr.inf.opla.patterns.util.MethodUtil;
import br.ufpr.inf.opla.patterns.util.RelationshipUtil;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Strategy extends DesignPattern {

    private static final Strategy INSTANCE = new Strategy();
    private static final Adapter ADAPTER = Adapter.getInstance();

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
        PSStrategy psStrategy = null;
        for (PS ps : scope.getPS()) {
            if (ps.isPsOf(this)) {
                psStrategy = (PSStrategy) ps;
                break;
            }
        }
        if (psStrategy != null) {
            AlgorithmFamily algorithmFamily = psStrategy.getAlgorithmFamily();
            List<Element> participants = psStrategy.getAlgorithmFamily().getParticipants();

            //Get or create Interface
            Interface strategyInterface = AlgorithmFamilyUtil.getStrategyInterfaceFromAlgorithmFamily(algorithmFamily);
            if (strategyInterface == null) {
                strategyInterface = AlgorithmFamilyUtil.createStrategyInterfaceForAlgorithmFamily(algorithmFamily);
            } else if (participants.contains(strategyInterface)) {
                participants.remove(strategyInterface);
            }

            //Implement
            List<Element> adapterList = new ArrayList<>();
            List<Element> adapteeList = new ArrayList<>();
            for (Element participant : participants) {
                if (!ElementUtil.isTypeOf(participant, strategyInterface)) {
                    //TODO - Édipo - Adicionar estereótipos Strategy.
                    if (participant instanceof arquitetura.representation.Class) {
                        Class participantClass = (Class) participant;
                        RelationshipUtil.createNewRealizationRelationship("implements", participantClass, strategyInterface);
                        MethodArrayList participantMethods = new MethodArrayList(new ArrayList<>(participantClass.getAllMethods()));
                        for (Method interfaceMethod : strategyInterface.getOperations()) {
                            int index = participantMethods.indexOf(interfaceMethod);
                            if (index != -1) {
                                MethodUtil.mergeMethodsToMethodA(participantMethods.get(index), interfaceMethod);
                            } else {
                                participantClass.addExternalMethod(MethodUtil.cloneMethod(interfaceMethod));
                            }
                        }
                    } else if (participant instanceof Interface) {
                        Class adapterClass = ADAPTER.applyAdapter(strategyInterface, participant);
                        adapterList.add(adapterClass);
                        adapteeList.add(participant);
                    }
                }
                //Concern
                for (Concern concern : participant.getOwnConcerns()) {
                    if (!strategyInterface.containsConcern(concern)) {
                        try {
                            strategyInterface.addConcern(concern.getName());
                        } catch (ConcernNotFoundException ex) {
                            Logger.getLogger(Strategy.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                }
            }

            participants.removeAll(adapteeList);
            participants.addAll(adapterList);

            //Move context relationships
            for (Element context : psStrategy.getContexts()) {
                HashMap<String, HashMap<String, List<Relationship>>> usingRelationshipsFromAlgorithms = new HashMap<>();
                for (Relationship relationShip : context.getRelationships()) {
                    Element usedElementFromRelationship = RelationshipUtil.getUsedElementFromRelationship(relationShip);
                    if (!usedElementFromRelationship.equals(context)
                            && (participants.contains(usedElementFromRelationship)
                            || adapteeList.contains(usedElementFromRelationship)
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
                    HashMap<String, List<Relationship>> typeMap = byTypeEntry.getValue();
                    for (Map.Entry<String, List<Relationship>> nameEntry : typeMap.entrySet()) {
                        List<Relationship> nameList = nameEntry.getValue();
                        Relationship relationship = nameList.get(0);

                        for (Relationship tempRelationship : nameList) {
                            Element usedElementFromRelationship = RelationshipUtil.getUsedElementFromRelationship(relationship);
                            usedElementFromRelationship.removeRelationship(tempRelationship);
                            context.removeRelationship(tempRelationship);
                            context.getArchitecture().removeRelationship(tempRelationship);
                        }

                        context.getArchitecture().addRelationship(relationship);
                        RelationshipUtil.moveRelationship(relationship, context, strategyInterface);
                    }
                }
                //Variabilities, variants and variation points.
                VariationPoint variationPoint = context.getVariationPoint();
                if (variationPoint != null) {
                    List<Variant> participantsVariants = new ArrayList<>();
                    for (Element participant : participants) {
                        if (participant.getVariant() != null) {
                            participantsVariants.add(participant.getVariant());
                        }
                    }
                    if (variationPoint.getVariants().containsAll(participantsVariants)) {
                        context.setVariationPoint(null);
                        strategyInterface.setVariationPoint(variationPoint);
                        variationPoint.replaceVariationPointElement(strategyInterface);
                        for (Variant variant : participantsVariants) {
                            variant.setRootVP(variationPoint.getVariationPointElement().getName());
                        }
                    }
                }
            }
            applied = true;
        }
        return applied;
    }
}
