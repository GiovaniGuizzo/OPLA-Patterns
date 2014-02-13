package br.ufpr.inf.opla.patterns.util;

import arquitetura.exceptions.ConcernNotFoundException;
import arquitetura.exceptions.PackageNotFound;
import arquitetura.helpers.UtilResources;
import arquitetura.representation.Concern;
import arquitetura.representation.Element;
import arquitetura.representation.Interface;
import arquitetura.representation.Method;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class InterfaceUtil {

    private InterfaceUtil() {
    }

    public static Interface createInterfaceForSetOfElements(String interfaceName, List<Element> participants) {
        Interface anInterface = null;
        if (participants != null && !participants.isEmpty()) {
            try {
                String namespace = ElementUtil.getNameSpace(participants);
                arquitetura.representation.Package aPackage = participants.get(0).getArchitecture().findPackageByName(UtilResources.extractPackageName(namespace));

                anInterface = aPackage.createInterface(interfaceName);
                aPackage.removeInterface(anInterface);

                List<Method> methodsFromSetOfElements = MethodUtil.createMethodsFromSetOfElements(participants);
                for (Method method : methodsFromSetOfElements) {
                    anInterface.addExternalOperation(method);
                }

                for (Concern concern : ElementUtil.getOwnAndMethodsConcerns(participants)) {
                    if (!anInterface.containsConcern(concern)) {
                        try {
                            anInterface.addConcern(concern.getName());
                        } catch (ConcernNotFoundException ex) {
                            Logger.getLogger(InterfaceUtil.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                }

                anInterface.setNamespace(namespace);

                int count = 1;
                String name = anInterface.getName();
                while (aPackage.getElements().contains(anInterface)) {
                    count++;
                    anInterface.setName(name + Integer.toString(count));
                }

                aPackage.addExternalInterface(anInterface);
            } catch (PackageNotFound ex) {
                Logger.getLogger(InterfaceUtil.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return anInterface;
    }
}
