/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ufpr.inf.opla.patterns.util;

import arquitetura.helpers.UtilResources;
import arquitetura.representation.Architecture;
import arquitetura.representation.Class;
import arquitetura.representation.Element;
import arquitetura.representation.relationship.Relationship;
import br.ufpr.inf.opla.patterns.repositories.ArchitectureRepository;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author giovaniguizzo
 */
public class AdapterUtil {

    private AdapterUtil() {
    }

    public static Class getAdapterClass(Element target, Element adaptee) {
        Class adapterClass = null;
        List<Element> allTargetSubElements = ElementUtil.getAllSubElements(target);
        if (adaptee instanceof Class) {
            rootFor:
            for (Element element : allTargetSubElements) {
                if (element instanceof Class) {
                    List<Relationship> elementRelationships = ElementUtil.getRelationships(element);
                    for (Relationship relationship : elementRelationships) {
                        Element usedElementFromRelationship = RelationshipUtil.getUsedElementFromRelationship(relationship);
                        if (usedElementFromRelationship != null && usedElementFromRelationship.equals(adaptee)) {
                            adapterClass = (Class) element;
                            break rootFor;
                        }
                    }
                }
            }
        } else {
            for (Element element : allTargetSubElements) {
                if (element instanceof Class) {
                    List<Element> allAdapteeSubElements = ElementUtil.getAllSubElements(adaptee);
                    if (allAdapteeSubElements.contains(element)) {
                        adapterClass = (Class) element;
                        break;
                    }
                }
            }
        }
        return adapterClass;
    }

    public static arquitetura.representation.Class createAdapterClass(Element target, Element adaptee) {
        Architecture architecture = ArchitectureRepository.getCurrentArchitecture();
        arquitetura.representation.Class adapterClass;
        arquitetura.representation.Package aPackage = null;

        List<Element> tempElements;

        String namespace = adaptee.getNamespace();
        String packageName = UtilResources.extractPackageName(namespace);

        boolean naArquitetura = packageName.equalsIgnoreCase("model");
        if (naArquitetura) {
            adapterClass = architecture.createClass(adaptee.getName() + "Adapter", false);

            architecture.removeClass(adapterClass);

            tempElements = Collections.unmodifiableList(new ArrayList<>(architecture.getElements()));
        } else {
            aPackage = architecture.findPackageByName(packageName);
            adapterClass = aPackage.createClass(adaptee.getName() + "Adapter", false);

            aPackage.removeClass(adapterClass);

            tempElements = Collections.unmodifiableList(new ArrayList<>(aPackage.getElements()));
        }

        adapterClass.setNamespace(adaptee.getNamespace());

        int count = 1;
        String name = adapterClass.getName();
        while (tempElements.contains(adapterClass)) {
            count++;
            adapterClass.setName(name + Integer.toString(count));
        }

        if (naArquitetura) {
            architecture.addExternalClass(adapterClass);
        } else if (aPackage != null) {
            aPackage.addExternalClass(adapterClass);
        }

        return adapterClass;
    }

}
