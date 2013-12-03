package br.ufpr.inf.opla.patterns.util;

import arquitetura.representation.Concern;
import arquitetura.representation.Element;
import arquitetura.representation.Interface;
import arquitetura.representation.Method;
import arquitetura.representation.relationship.AssociationEnd;
import arquitetura.representation.relationship.AssociationRelationship;
import arquitetura.representation.relationship.Relationship;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.apache.commons.collections4.CollectionUtils;

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

    public static List<Interface> getAllSuperInterfaces(Element child) {
        List<Interface> implementedInterfaces = new ArrayList<>();
        for (Relationship relationship : child.getRelationships()) {
            Interface tempInterface = RelationshipUtil.getImplementedInterface(relationship);
            if (tempInterface != null && !tempInterface.equals(child)) {
                implementedInterfaces.add(tempInterface);
                List<Interface> parentInterfaces = getAllSuperInterfaces(tempInterface);
                for (Interface parentInterface : parentInterfaces) {
                    if (!implementedInterfaces.contains(parentInterface)) {
                        implementedInterfaces.add(parentInterface);
                    }
                }
                List<Element> parentSuperTypes = getAllExtendedElements(tempInterface);
                for (Element parentSuperType : parentSuperTypes) {
                    if (parentSuperType instanceof Interface && !implementedInterfaces.contains(parentSuperType)) {
                        implementedInterfaces.add((Interface) parentSuperType);
                    }
                }
            }
        }
        List<Element> allExtendedElements = getAllExtendedElements(child);
        for (Element extendedElement : allExtendedElements) {
            List<Interface> parentInterfaces = getAllSuperInterfaces(extendedElement);
            for (Interface parentInterface : parentInterfaces) {
                if (!implementedInterfaces.contains(parentInterface)) {
                    implementedInterfaces.add(parentInterface);
                }
            }
            if (child instanceof Interface && extendedElement instanceof Interface) {
                if (!implementedInterfaces.contains(extendedElement)) {
                    implementedInterfaces.add((Interface) extendedElement);
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

    public static List<Element> getAllSubElements(Element parent) {
        List<Element> subElements = new ArrayList<>();
        for (Relationship relationship : parent.getRelationships()) {
            Element tempChild = RelationshipUtil.getSubElement(relationship);
            if (tempChild != null && !tempChild.equals(parent)) {
                subElements.add(tempChild);
                List<Element> subElementSubTypes = getAllSubElements(tempChild);
                for (Element subElementSubType : subElementSubTypes) {
                    if (!subElements.contains(subElementSubType)) {
                        subElements.add(subElementSubType);
                    }
                }
            }
        }
        return subElements;
    }

    public static boolean isClassOrInterface(Element element) {
        return (element instanceof arquitetura.representation.Class || element instanceof Interface);
    }

    public static Set<Concern> getCommonConcerns(List<Element> elements) {
        Set<Concern> commonConcerns = new HashSet<>();
        commonConcerns.addAll(getAllConcerns(elements.get(0)));
        for (Element participant : elements) {
            commonConcerns = new HashSet<>(CollectionUtils.intersection(commonConcerns, getAllConcerns(participant)));
        }
        return commonConcerns;
    }

    public static Set<Concern> getCommonConcernsOfAtLeastTwoElements(List<Element> elements) {
        Set<Concern> commonConcerns = new HashSet<>();
        for (Element iElement : elements) {
            concernLoop:
            for (Concern concern : getAllConcerns(iElement)) {
                if (!commonConcerns.contains(concern)) {
                    for (Element jElement : elements) {
                        if (!jElement.equals(iElement)) {
                            if (getAllConcerns(jElement).contains(concern)) {
                                commonConcerns.add(concern);
                                continue concernLoop;
                            }
                        }
                    }
                }
            }
        }
        return commonConcerns;
    }

    public static Set<Concern> getAllConcerns(List<Element> elements) {
        Set<Concern> commonConcerns = new HashSet<>();
        for (Element participant : elements) {
            commonConcerns.addAll(getAllConcerns(participant));
        }
        return commonConcerns;
    }

    public static Set<Concern> getAllConcerns(Element element) {
        Set<Concern> commonConcerns = new HashSet<>();
        commonConcerns.addAll(element.getOwnConcerns());
        for (Method method : MethodUtil.getAllMethodsFromElement(element)) {
            commonConcerns.addAll(method.getOwnConcerns());
        }
        return commonConcerns;
    }

    public static Set<Element> getAllAggregatedElements(Element element) {
        Set<Element> aggregatedElements = new HashSet<>();
        for (Relationship relationship : element.getRelationships()) {
            if (relationship instanceof AssociationRelationship) {
                AssociationRelationship association = (AssociationRelationship) relationship;
                for (AssociationEnd end : association.getParticipants()) {
                    if (end.isAggregation() && !end.getCLSClass().equals(element)) {
                        aggregatedElements.add(end.getCLSClass());
                    }
                }
            }
        }
        return aggregatedElements;
    }

}
