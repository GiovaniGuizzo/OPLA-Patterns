package br.ufpr.inf.opla.patterns.util;

import arquitetura.representation.Element;
import arquitetura.representation.Interface;
import arquitetura.representation.Method;
import arquitetura.representation.relationship.Relationship;
import br.ufpr.inf.opla.patterns.models.AlgorithmFamily;
import br.ufpr.inf.opla.patterns.models.Scope;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.collections4.CollectionUtils;

public class AlgorithmFamilyUtil {

    private static final AlgorithmFamilyUtil INSTANCE = new AlgorithmFamilyUtil();

    private final ElementUtil elementUtil;
    private final RelationshipUtil relationshipUtil;

    private AlgorithmFamilyUtil() {
        this.elementUtil = ElementUtil.getInstance();
        this.relationshipUtil = RelationshipUtil.getInstance();
    }

    public static AlgorithmFamilyUtil getInstance() {
        return INSTANCE;
    }

    public List<AlgorithmFamily> getFamiliesFromScope(Scope scope) {
        List<AlgorithmFamily> familiesInScope = new ArrayList<>();
        addFamiliesWithSuffixAndPreffix(scope, familiesInScope);
        addFamiliesWithSameMethod(scope, familiesInScope);
        return familiesInScope;
    }

    private void addFamiliesWithSuffixAndPreffix(Scope scope, List<AlgorithmFamily> familiesInScope) {
        for (int i = 0; i < scope.getElements().size(); i++) {
            Element iElement = scope.getElements().get(i);
            List<String> suffixes = new ArrayList<>();
            List<String> prefixes = new ArrayList<>();

            final String iElementName = iElement.getName();
            for (int k = 3; k <= iElementName.length(); k++) {
                suffixes.add(iElementName.substring(k - 3, iElementName.length()));
                prefixes.add(iElementName.substring(0, iElementName.length() - k + 3));
            }

            for (int j = i + 1; j < scope.getElements().size(); j++) {
                Element jElement = scope.getElements().get(j);
                final String jElementName = jElement.getName();

                for (String suffix : suffixes) {
                    if (jElementName.length() >= suffix.length()) {
                        if (jElementName.substring(jElementName.length() - suffix.length()).equals(suffix)) {
                            AlgorithmFamily algorithmFamily = new AlgorithmFamily();
                            algorithmFamily.setFamilyName(suffix);
                            addElementsToAlgorithmFamily(algorithmFamily, familiesInScope, iElement, jElement);
                        }
                    }
                }

                for (String prefix : prefixes) {
                    if (jElementName.length() >= prefix.length()) {
                        if (jElementName.substring(0, prefix.length()).equals(prefix)) {
                            AlgorithmFamily algorithmFamily = new AlgorithmFamily();
                            algorithmFamily.setFamilyName(prefix);
                            addElementsToAlgorithmFamily(algorithmFamily, familiesInScope, iElement, jElement);
                        }
                    }
                }
            }
        }
    }

    private void addFamiliesWithSameMethod(Scope scope, List<AlgorithmFamily> familiesInScope) {
        for (int i = 0; i < scope.getElements().size(); i++) {
            Element iElement = scope.getElements().get(i);

            List<Method> iMethods = elementUtil.getMethodsFromElement(iElement);
            if (iMethods == null || iMethods.isEmpty()) {
                continue;
            }

            for (int j = i + 1; j < scope.getElements().size(); j++) {
                Element jElement = scope.getElements().get(j);

                List<Method> jMethods = elementUtil.getMethodsFromElement(jElement);
                if (jMethods == null || jMethods.isEmpty()) {
                    continue;
                }

                for (Method iMethod : iMethods) {
                    for (Method jMethod : jMethods) {
                        if (iMethod.getName().equals(jMethod.getName())
                                && iMethod.getReturnType().equals(jMethod.getReturnType())) {
                            AlgorithmFamily algorithmFamily = new AlgorithmFamily();
                            algorithmFamily.setFamilyName(iMethod.getName());
                            addElementsToAlgorithmFamily(algorithmFamily, familiesInScope, iElement, jElement);
                        }
                    }
                }
            }
        }
    }

    private void addElementsToAlgorithmFamily(AlgorithmFamily algorithmFamily, List<AlgorithmFamily> familiesInScope, Element iElement, Element jElement) {
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
    public Interface getStrategyInterfaceFromAlgorithmFamily(AlgorithmFamily algorithmFamily) {
        Interface strategyInterface = null;
        List<Element> participants = algorithmFamily.getParticipants();
        List<Interface> interfaces = new ArrayList<>();
        for (Element participant : participants) {
            List<Interface> elementInterfaces = new ArrayList<>();
            for (Relationship relationship : participant.getRelationships()) {
                Interface implementedInterface = relationshipUtil.getImplementedInterface(relationship);
                if (implementedInterface != null) {
                    elementInterfaces.add(implementedInterface);
                }
            }
            if (interfaces.isEmpty()) {
                interfaces.addAll(elementInterfaces);
            } else {
                interfaces = new ArrayList<>(CollectionUtils.intersection(interfaces, elementInterfaces));
            }
        }

        List<Method> allMethodsFromAlgorithmFamily = getAllMethodsFromAlgorithmFamily(algorithmFamily);
        for (Interface aInterface : interfaces) {
            boolean isStrategyInterface = true;
            for (Method method : allMethodsFromAlgorithmFamily) {
                boolean hasMethod = false;
                for (Method interfaceMethod : aInterface.getOperations()) {
                    if (elementUtil.areMethodsEqual(interfaceMethod, method)) {
                        hasMethod = true;
                        break;
                    }
                }
                if (!hasMethod) {
                    isStrategyInterface = false;
                    break;
                }
            }
            if (isStrategyInterface) {
                strategyInterface = aInterface;
                break;
            }
        }

        return strategyInterface;
    }

    /**
     * Gets all methods from the specified algorithm family. Methods are equal if their names and return types are equal.
     *
     * @param algorithmFamily The algorithm family from which the methods must be extracted.
     * @return A List with all the methods from the elements of the algorithm family.
     */
    public List<Method> getAllMethodsFromAlgorithmFamily(AlgorithmFamily algorithmFamily) {
        List<Method> methods = new ArrayList<>();
        for (Element element : algorithmFamily.getParticipants()) {
            List<Method> elementMethods = elementUtil.getMethodsFromElement(element);
            if (methods.isEmpty()) {
                methods.addAll(elementMethods);
            } else {
                for (Method elementMethod : elementMethods) {
                    boolean hasMethod = false;
                    for (int i = 0; i < methods.size(); i++) {
                        Method method = methods.get(i);
                        if (elementUtil.areMethodsEqual(method, elementMethod)) {
                            hasMethod = true;
                            break;
                        }
                    }
                    if (!hasMethod) {
                        methods.add(elementMethod);
                    }
                }
            }
        }
        return methods;
    }
}
