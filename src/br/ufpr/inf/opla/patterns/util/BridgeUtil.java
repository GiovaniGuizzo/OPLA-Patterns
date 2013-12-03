package br.ufpr.inf.opla.patterns.util;

import arquitetura.representation.Concern;
import arquitetura.representation.Element;
import arquitetura.representation.Interface;
import br.ufpr.inf.opla.patterns.models.AlgorithmFamily;
import br.ufpr.inf.opla.patterns.models.Scope;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class BridgeUtil {

    private BridgeUtil() {
    }

    public static List<Element> getImplementationInterfaces(arquitetura.representation.Class abstractionClass) {
        List<Element> implementationInterfaces = new ArrayList<>();

        return implementationInterfaces;
    }

    public static List<Element> getAbstractionClasses(Scope scope, AlgorithmFamily algorithmFamily) {
        List<Element> abstractionClasses = new ArrayList<>();
        root:
        for (Element element : scope.getElements()) {
            if (element instanceof arquitetura.representation.Class) {
                arquitetura.representation.Class classElement = (arquitetura.representation.Class) element;
                if (classElement.isAbstract()) {
                    Set<Concern> allConcernsFromSetOfElements = ElementUtil.getAllConcerns(algorithmFamily.getParticipants());
                    if (ElementUtil.getAllConcerns(element).containsAll(allConcernsFromSetOfElements)) {
                        Set<Element> aggregatedElements = ElementUtil.getAllAggregatedElements(element);
                        boolean contains = false;
                        for (Element participant : algorithmFamily.getParticipants()) {
                            if (!participant.equals(element) && !ElementUtil.isTypeOf(participant, element)) {
                                List<Interface> allSuperInterfaces = ElementUtil.getAllSuperInterfaces(participant);
                                for (Interface participantInterface : allSuperInterfaces) {
                                    if (ElementUtil.isTypeOf(element, participantInterface)) {
                                        continue root;
                                    } else if (!contains && aggregatedElements.contains(participantInterface)) {
                                        contains = true;
                                    }
                                }
                            }
                        }
                        if (contains) {
                            abstractionClasses.add(element);
                            abstractionClasses.addAll(ElementUtil.getAllSubElements(element));
                            break;
                        }
                    }
                }
            }
        }
        return abstractionClasses;
    }

}
