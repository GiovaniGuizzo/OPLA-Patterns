package br.ufpr.inf.opla.patterns.util;

import arquitetura.representation.Element;
import arquitetura.representation.Interface;
import arquitetura.representation.relationship.Relationship;
import java.util.ArrayList;
import java.util.List;

public class ElementUtil {

    private ElementUtil() {
    }

    public static boolean isTypeOf(Element child, Element parent) {
        boolean isType = false;
        for (Relationship relationship : child.getRelationships()) {
            Element tempParent = RelationshipUtil.getImplementedInterface(relationship);
            if (tempParent == null) {
                tempParent = RelationshipUtil.getExtendedElement(relationship);
                if (tempParent == null) {
                    continue;
                }
            }
            if (!tempParent.equals(child)) {
                if (tempParent.equals(parent)) {
                    isType = true;
                } else {
                    isType = isTypeOf(tempParent, parent);
                }
            }
        }
        return isType;
    }

    public static List<Interface> getAllImplementedInterfaces(Element child) {
        List<Interface> implementedInterfaces = new ArrayList<>();
        for (Relationship relationship : child.getRelationships()) {
            Interface tempInterface = RelationshipUtil.getImplementedInterface(relationship);
            if (tempInterface != null && !tempInterface.equals(child)) {
                implementedInterfaces.add(tempInterface);
                List<Interface> parentInterfaces = getAllImplementedInterfaces(tempInterface);
                for (Interface parentInterface : parentInterfaces) {
                    if (!implementedInterfaces.contains(parentInterface)) {
                        implementedInterfaces.add(parentInterface);
                    }
                }
                List<Element> parentSuperTypes = getAllExtendedElements(tempInterface);
                for (Element parentSuperType : parentSuperTypes) {
                    if (parentSuperType instanceof Interface && !implementedInterfaces.contains((Interface) parentSuperType)) {
                        implementedInterfaces.add((Interface) parentSuperType);
                    }
                }
            }
        }
        List<Element> allExtendedElements = getAllExtendedElements(child);
        for (Element extendedElement : allExtendedElements) {
            List<Interface> parentInterfaces = getAllImplementedInterfaces(extendedElement);
            for (Interface parentInterface : parentInterfaces) {
                if (!implementedInterfaces.contains(parentInterface)) {
                    implementedInterfaces.add(parentInterface);
                }
            }
        }
        return implementedInterfaces;
    }

    public static List<Element> getAllExtendedElements(Element child) {
        List<Element> extendedElements = new ArrayList<>();
        for (Relationship relationship : child.getRelationships()) {
            Element tempParent = RelationshipUtil.getExtendedElement(relationship);
            if (tempParent != null && !tempParent.equals(child)) {
                extendedElements.add(tempParent);
                List<Element> parentSuperTypes = getAllExtendedElements(tempParent);
                for (Element parentSuperType : parentSuperTypes) {
                    if (!extendedElements.contains(parentSuperType)) {
                        extendedElements.add(parentSuperType);
                    }
                }
            }
        }
        return extendedElements;
    }

}
