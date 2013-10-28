package br.ufpr.inf.opla.patterns.util;

import arquitetura.representation.Architecture;
import arquitetura.representation.Concern;
import arquitetura.representation.Element;
import arquitetura.representation.Interface;
import java.util.List;
import java.util.UUID;

public class InterfaceUtil {

    private InterfaceUtil() {
    }

    public static Interface createInterfaceForSetOfElements(String interfaceName, List<Element> participants) {
        Interface aInterface = null;
        if (participants != null && !participants.isEmpty()) {
            Architecture architecture = Element.getArchitecture();
            aInterface = new Interface(architecture, interfaceName, UUID.randomUUID().toString());

            aInterface.getOperations().addAll(MethodUtil.createMethodsFromSetOfElements(participants));

            for (Element element : participants) {
                for (Concern concern : element.getOwnConcerns()) {
                    if (!aInterface.containsConcern(concern)) {
                        aInterface.getOwnConcerns().add(concern);
                    }
                }
            }
        }
        return aInterface;
    }

}
