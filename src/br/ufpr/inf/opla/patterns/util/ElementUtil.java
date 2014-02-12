package br.ufpr.inf.opla.patterns.util;

import arquitetura.representation.Concern;
import arquitetura.representation.Element;
import arquitetura.representation.Interface;
import arquitetura.representation.Method;
import arquitetura.representation.relationship.AssociationEnd;
import arquitetura.representation.relationship.AssociationRelationship;
import arquitetura.representation.relationship.Relationship;
import br.ufpr.inf.opla.patterns.designpatterns.Adapter;
import br.ufpr.inf.opla.patterns.list.MethodArrayList;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
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
                    if (parentSuperType instanceof Interface && !implementedInterfaces.contains((Interface) parentSuperType)) {
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
                if (!implementedInterfaces.contains((Interface) extendedElement)) {
                    implementedInterfaces.add((Interface) extendedElement);
                }
            }
        }
        return implementedInterfaces;
    }

    public static List<Interface> getAllCommonInterfaces(List<Element> participants) {
        List<Interface> interfaces = new ArrayList<>();
        for (Element participant : participants) {
            List<Interface> elementInterfaces = ElementUtil.getAllSuperInterfaces(participant);
            if (interfaces.isEmpty()) {
                interfaces.addAll(elementInterfaces);
                if (participant instanceof Interface) {
                    interfaces.add((Interface) participant);
                }
            } else {
                if (participant instanceof Interface) {
                    elementInterfaces.add((Interface) participant);
                }
                interfaces = new ArrayList<>(CollectionUtils.intersection(interfaces, elementInterfaces));
            }
        }
        return interfaces;
    }

    public static List<Interface> getAllCommonSuperInterfaces(List<Element> participants) {
        List<Interface> interfaces = new ArrayList<>();
        for (Element participant : participants) {
            List<Interface> elementInterfaces = ElementUtil.getAllSuperInterfaces(participant);
            if (interfaces.isEmpty()) {
                interfaces.addAll(elementInterfaces);
            } else {
                interfaces = new ArrayList<>(CollectionUtils.intersection(interfaces, elementInterfaces));
            }
        }
        return interfaces;
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

    public static Set<Concern> getOwnAndMethodsCommonConcerns(List<Element> elements) {
        Set<Concern> commonConcerns = new HashSet<>();
        commonConcerns.addAll(getOwnAndMethodsConcerns(elements.get(0)));
        for (Element participant : elements) {
            commonConcerns = new HashSet<>(CollectionUtils.intersection(commonConcerns, getOwnAndMethodsConcerns(participant)));
        }
        return commonConcerns;
    }

    public static Set<Concern> getOwnAndMethodsCommonConcernsOfAtLeastTwoElements(List<Element> elements) {
        Set<Concern> commonConcerns = new HashSet<>();
        for (Element iElement : elements) {
            concernLoop:
            for (Concern concern : getOwnAndMethodsConcerns(iElement)) {
                if (!commonConcerns.contains(concern)) {
                    for (Element jElement : elements) {
                        if (!jElement.equals(iElement)) {
                            if (getOwnAndMethodsConcerns(jElement).contains(concern)) {
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

    public static Set<Concern> getOwnAndMethodsConcerns(List<Element> elements) {
        Set<Concern> commonConcerns = new HashSet<>();
        for (Element participant : elements) {
            commonConcerns.addAll(getOwnAndMethodsConcerns(participant));
        }
        return commonConcerns;
    }

    public static Set<Concern> getOwnAndMethodsConcerns(Element element) {
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

    public static HashMap<Concern, List<Element>> groupElementsByConcern(List<Element> elements) {
        HashMap<Concern, List<Element>> groupedElements = new HashMap<>();
        Set<Concern> ownAndMethodsCommonConcerns = getOwnAndMethodsConcerns(elements);
        for (Concern concern : ownAndMethodsCommonConcerns) {
            List<Element> concernElements = new ArrayList<>();
            for (Element element : elements) {
                Set<Concern> elementConcerns = getOwnAndMethodsConcerns(element);
                if (elementConcerns.contains(concern)) {
                    concernElements.add(element);
                }
            }
            groupedElements.put(concern, concernElements);
        }
        ArrayList<Element> nullList = ElementUtil.getElementsWithNoOwnConcernsAndWithAtLeastOneMethodWithNoConcerns(elements);
        if (!nullList.isEmpty()) {
            groupedElements.put(null, nullList);
        }
        return groupedElements;
    }

    public static ArrayList<Element> getElementsWithNoOwnConcernsAndWithAtLeastOneMethodWithNoConcerns(Iterable<Element> elements) {
        ArrayList<Element> nullArrayList = new ArrayList<>();
        elementLoop:
        for (Element element : elements) {
            if (element.getOwnConcerns().isEmpty()) {
                for (Method method : MethodUtil.getAllMethodsFromElement(element)) {
                    if (method.getOwnConcerns().isEmpty()) {
                        nullArrayList.add(element);
                        continue elementLoop;
                    }
                }
            }
        }
        return nullArrayList;
    }

    public static String getNameSpace(List<Element> elements) {
        HashMap<String, Integer> namespaceList = new HashMap<>();
        for (Element element : elements) {
            Integer namespaceCount = namespaceList.get(element.getNamespace());
            namespaceList.put(element.getNamespace(), namespaceCount == null ? 1 : namespaceCount + 1);
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
        return namespace;
    }

    public static void implementInterface(List<Element> elements, Interface anInterface, List<Element> adapterList, List<Element> adapteeList) {
        for (Element participant : elements) {
            if (!ElementUtil.isTypeOf(participant, anInterface)) {
                if (participant instanceof arquitetura.representation.Class) {
                    arquitetura.representation.Class participantClass = (arquitetura.representation.Class) participant;
                    RelationshipUtil.createNewRealizationRelationship("implements", participantClass, anInterface);
                } else if (participant instanceof Interface) {
                    arquitetura.representation.Class adapterClass = Adapter.getInstance().applyAdapter(anInterface, participant);
                    adapterList.add(adapterClass);
                    adapteeList.add(participant);
                }
            }
            if (participant instanceof arquitetura.representation.Class) {
                arquitetura.representation.Class participantClass = (arquitetura.representation.Class) participant;
                if (!participantClass.isAbstract()) {
                    MethodArrayList participantMethods = new MethodArrayList(new ArrayList<>(participantClass.getAllMethods()));
                    for (Method interfaceMethod : anInterface.getOperations()) {
                        int index = participantMethods.indexOf(interfaceMethod);
                        if (index != -1) {
                            MethodUtil.mergeMethodsToMethodA(participantMethods.get(index), interfaceMethod);
                        } else {
                            participantClass.addExternalMethod(MethodUtil.cloneMethod(interfaceMethod));
                        }
                    }
                }
            }
        }
    }

}
