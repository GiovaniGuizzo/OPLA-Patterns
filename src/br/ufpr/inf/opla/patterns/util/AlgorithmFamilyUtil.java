package br.ufpr.inf.opla.patterns.util;

import arquitetura.representation.Element;
import arquitetura.representation.Interface;
import arquitetura.representation.Method;
import arquitetura.representation.Variability;
import br.ufpr.inf.opla.patterns.list.MethodArrayList;
import br.ufpr.inf.opla.patterns.models.AlgorithmFamily;
import br.ufpr.inf.opla.patterns.models.Scope;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.collections4.CollectionUtils;

public class AlgorithmFamilyUtil {

    private AlgorithmFamilyUtil() {
    }

    public static List<AlgorithmFamily> getFamiliesFromScope(Scope scope) {
        List<AlgorithmFamily> familiesInScope = new ArrayList<>();
        addFamiliesWithSuffixAndPreffix(scope, familiesInScope);
        addFamiliesWithSameMethod(scope, familiesInScope);
        return familiesInScope;
    }

    private static void addFamiliesWithSuffixAndPreffix(Scope scope, List<AlgorithmFamily> familiesInScope) {
        for (int i = 0; i < scope.getElements().size(); i++) {
            Element iElement = scope.getElements().get(i);
            if (ElementUtil.isClassOrInterface(iElement)) {
                List<String> suffixes = new ArrayList<>();
                List<String> prefixes = new ArrayList<>();

                final String iElementName = iElement.getName();
                for (int k = 3; k <= iElementName.length(); k++) {
                    suffixes.add(iElementName.substring(k - 3, iElementName.length()));
                    prefixes.add(iElementName.substring(0, iElementName.length() - k + 3));
                }

                for (int j = i + 1; j < scope.getElements().size(); j++) {
                    Element jElement = scope.getElements().get(j);
                    if (ElementUtil.isClassOrInterface(jElement)) {
                        final String jElementName = jElement.getName();

                        for (String suffix : suffixes) {
                            if (jElementName.length() >= suffix.length()) {
                                if (jElementName.substring(jElementName.length() - suffix.length()).equals(suffix)) {
                                    AlgorithmFamily algorithmFamily = new AlgorithmFamily();
                                    algorithmFamily.setName(suffix);
                                    addElementsToAlgorithmFamily(algorithmFamily, familiesInScope, iElement, jElement);
                                }
                            }
                        }

                        for (String prefix : prefixes) {
                            if (jElementName.length() >= prefix.length()) {
                                if (jElementName.substring(0, prefix.length()).equals(prefix)) {
                                    AlgorithmFamily algorithmFamily = new AlgorithmFamily();
                                    algorithmFamily.setName(prefix);
                                    addElementsToAlgorithmFamily(algorithmFamily, familiesInScope, iElement, jElement);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private static void addFamiliesWithSameMethod(Scope scope, List<AlgorithmFamily> familiesInScope) {
        for (int i = 0; i < scope.getElements().size(); i++) {
            Element iElement = scope.getElements().get(i);
            if (ElementUtil.isClassOrInterface(iElement)) {

                MethodArrayList iMethods = new MethodArrayList(MethodUtil.getAllMethodsFromElement(iElement));
                if (iMethods.isEmpty()) {
                    continue;
                }

                for (int j = i + 1; j < scope.getElements().size(); j++) {
                    Element jElement = scope.getElements().get(j);
                    if (ElementUtil.isClassOrInterface(jElement)) {
                        MethodArrayList jMethods = new MethodArrayList(MethodUtil.getAllMethodsFromElement(jElement));
                        if (jMethods.isEmpty()) {
                            continue;
                        }
                        for (Method iMethod : iMethods) {
                            if (jMethods.contains(iMethod)) {
                                AlgorithmFamily algorithmFamily = new AlgorithmFamily();
                                algorithmFamily.setName(iMethod.getName());
                                addElementsToAlgorithmFamily(algorithmFamily, familiesInScope, iElement, jElement);
                            }
                        }
                    }
                }
            }
        }
    }

    private static void addElementsToAlgorithmFamily(AlgorithmFamily algorithmFamily, List<AlgorithmFamily> familiesInScope, Element iElement, Element jElement) {
        if (!familiesInScope.contains(algorithmFamily)) {
            algorithmFamily.getParticipants().add(iElement);
            algorithmFamily.getParticipants().add(jElement);
            familiesInScope.add(algorithmFamily);
        } else {
            algorithmFamily = familiesInScope.get(familiesInScope.indexOf(algorithmFamily));
            if (!algorithmFamily.getParticipants().contains(iElement)) {
                algorithmFamily.getParticipants().add(iElement);
            }
            if (!algorithmFamily.getParticipants().contains(jElement)) {
                algorithmFamily.getParticipants().add(jElement);
            }
        }
    }

    /**
     * Gets the Strategy interface from the algorithm family, if there is one.
     *
     * A Strategy interface is an interface implemented by all elements from an algorithm family and with all the methods from these elements (methods are equal if their names and return types are equal).
     *
     * @param algorithmFamily The algorithm family you want to get the Strategy interface from.
     * @return The Strategy interface, or null if there is not one.
     */
    public static Interface getStrategyInterfaceFromAlgorithmFamily(AlgorithmFamily algorithmFamily) {
        Interface strategyInterface = null;
        List<Element> participants = algorithmFamily.getParticipants();
        List<Interface> interfaces = new ArrayList<>();
        for (Element participant : participants) {
            List<Interface> elementInterfaces = ElementUtil.getAllImplementedInterfaces(participant);
            if (participant instanceof Interface) {
                List<Element> allExtendedElements = ElementUtil.getAllExtendedElements(participant);
                for (Element element : allExtendedElements) {
                    if (element instanceof Interface) {
                        elementInterfaces.add((Interface) element);
                    }
                }
            }
            if (interfaces.isEmpty()) {
                interfaces.addAll(elementInterfaces);
                if (participant instanceof Interface) {
                    elementInterfaces.add((Interface) participant);
                }
            } else {
                if (participant instanceof Interface) {
                    elementInterfaces.add((Interface) participant);
                }
                interfaces = new ArrayList<>(CollectionUtils.intersection(interfaces, elementInterfaces));
            }
        }

        MethodArrayList allMethodsFromAlgorithmFamily = new MethodArrayList(MethodUtil.getMethodsFromSetOfElements(algorithmFamily.getParticipants()));
        for (Interface aInterface : interfaces) {
            MethodArrayList interfaceMethods = new MethodArrayList(MethodUtil.getAllMethodsFromElement(aInterface));
            if (interfaceMethods.containsAll(allMethodsFromAlgorithmFamily)) {
                strategyInterface = aInterface;
                break;
            }
        }

        return strategyInterface;
    }

    public static Interface createStrategyInterfaceForAlgorithmFamily(AlgorithmFamily algorithmFamily) {
        return InterfaceUtil.createInterfaceForSetOfElements(Character.toUpperCase(algorithmFamily.getName().charAt(0)) + algorithmFamily.getName().substring(1) + "Strategy", algorithmFamily.getParticipants());
    }

    public static boolean areTheAlgorithmFamilyAndContextsPartOfAVariability(AlgorithmFamily algorithmFamily, List<Element> contexts) {
        List<Variability> variabilities = null;
        for (Element algorithm : algorithmFamily.getParticipants()) {
            if (algorithm.getVariant() == null) {
                return false;
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
                return false;
            }
        }
        if (variabilities == null || variabilities.isEmpty()) {
            return false;
        }
        for (Element context : contexts) {
            if (context.getVariationPoint() != null) {
                List<Variability> contextVariabilities = context.getVariationPoint().getVariabilities();
                for (Variability variability : contextVariabilities) {
                    if (variabilities.contains(variability)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
