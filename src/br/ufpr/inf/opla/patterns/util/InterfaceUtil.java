package br.ufpr.inf.opla.patterns.util;

import arquitetura.exceptions.PackageNotFound;
import arquitetura.helpers.UtilResources;
import arquitetura.representation.Architecture;
import arquitetura.representation.Concern;
import arquitetura.representation.Element;
import arquitetura.representation.Interface;
import arquitetura.representation.Package;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

public class InterfaceUtil {

    private InterfaceUtil() {
    }

    public static Interface createInterfaceForSetOfElements(String interfaceName, List<Element> participants) {
        Interface anInterface = null;
        if (participants != null && !participants.isEmpty()) {
            Architecture architecture = participants.get(0).getArchitecture();

            anInterface = new Interface(architecture, interfaceName, null, "", UUID.randomUUID().toString());

            anInterface.getOperations().addAll(MethodUtil.createMethodsFromSetOfElements(participants));

            HashMap<String, Integer> namespaceList = new HashMap<>();
            for (Element element : participants) {
                Integer namespaceCount = namespaceList.get(element.getNamespace());
                namespaceList.put(element.getNamespace(), namespaceCount == null ? 1 : namespaceCount + 1);
                for (Concern concern : element.getOwnConcerns()) {
                    if (!anInterface.containsConcern(concern)) {
                        anInterface.getOwnConcerns().add(concern);
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
            anInterface.setNamespace(namespace);
            try {
                Package aPackage = anInterface.getArchitecture().findPackageByName(UtilResources.extractPackageName(namespace));
                if (aPackage != null) {
                    aPackage.getElements().add(anInterface);
                }
            } catch (PackageNotFound ex) {
                Logger.getLogger(InterfaceUtil.class.getName()).log(Level.SEVERE, null, ex);
            }

            int count = 1;
            while (architecture.getElements().contains(anInterface)) {
                count++;
                anInterface.setName(anInterface.getName() + Integer.toString(count));
            }
            architecture.getElements().add(anInterface);
        }
        return anInterface;
    }

}
