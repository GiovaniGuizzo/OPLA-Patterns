package br.ufpr.inf.opla.patterns.util;

import arquitetura.representation.Architecture;
import arquitetura.representation.Concern;
import arquitetura.representation.Element;
import arquitetura.representation.Interface;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.apache.commons.collections4.CollectionUtils;

public class InterfaceUtil {

    private InterfaceUtil() {
    }

    public static Interface createInterfaceForSetOfElements(String interfaceName, List<Element> participants) {
        Interface aInterface = null;
        if (participants != null && !participants.isEmpty()) {
            Architecture architecture = participants.get(0).getArchitecture();

            aInterface = new Interface(architecture, interfaceName, null, "", UUID.randomUUID().toString());

            aInterface.getOperations().addAll(MethodUtil.createMethodsFromSetOfElements(participants));

            HashMap<String, Integer> namespaceList = new HashMap<>();
            for (Element element : participants) {
                Integer namespaceCount = namespaceList.get(element.getNamespace());
                namespaceList.put(element.getNamespace(), namespaceCount == null ? 1 : namespaceCount + 1);
                for (Concern concern : element.getOwnConcerns()) {
                    if (!aInterface.containsConcern(concern)) {
                        aInterface.getOwnConcerns().add(concern);
                    }
                }
            }

            Integer max = -1;
            String namespace = "";
            for (Map.Entry<String, Integer> entry : namespaceList.entrySet()) {
                String key = entry.getKey();
                Integer value = entry.getValue();
                if (value > max) {
                    max = value;
                    namespace = key;
                }
            }
            aInterface.setNamespace(namespace);
            
            //TODO - Édipo - Descomentar quando o Édipo comitar as alterações.
            int count = 1;
            while (architecture.getElements().contains(aInterface)) {
                count++;
//                aInterface.setName(aInterface.getName() + Integer.valueOf(count));
            }
            architecture.getElements().add(aInterface);
        }
        return aInterface;
    }

}
