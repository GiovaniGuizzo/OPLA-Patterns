package br.ufpr.inf.opla.patterns.designpatterns;

import arquitetura.representation.Class;
import arquitetura.representation.Element;
import arquitetura.representation.Interface;
import arquitetura.representation.Method;
import br.ufpr.inf.opla.patterns.models.DesignPattern;
import br.ufpr.inf.opla.patterns.models.Scope;
import br.ufpr.inf.opla.patterns.models.AlgorithmFamily;
import java.util.ArrayList;
import java.util.List;

public class Strategy extends DesignPattern {

    private static final Strategy INSTANCE = new Strategy();

    private Strategy() {
        super("Strategy", "Behavioral");
    }

    public static Strategy getInstance() {
        return INSTANCE;
    }

    @Override
    protected boolean verifyPS(Scope scope) {
        List<AlgorithmFamily> familiesInScope = new ArrayList<>();

        addFamiliesWithSuffixAndPreffix(scope, familiesInScope);
        addFamiliesWithSameMethod(scope, familiesInScope);

        return false;
    }

    private void addFamiliesWithSuffixAndPreffix(Scope scope, List<AlgorithmFamily> familiesInScope) {
        for (int i = 0; i < scope.getElements().size(); i++) {
            List<String> suffixes = new ArrayList<>();
            List<String> prefixes = new ArrayList<>();

            {
                final String elementName = scope.getElements().get(i).getName();
                for (int k = 3; k < elementName.length(); k++) {
                    suffixes.add(elementName.substring(k - 3, elementName.length()));
                    prefixes.add(elementName.substring(0, elementName.length() - k + 3));
                }
            }

            for (int j = i + 1; j < scope.getElements().size(); j++) {
                final String elementName = scope.getElements().get(j).getName();

                for (String prefix : prefixes) {
                    if (elementName.length() >= prefix.length()) {
                        if (elementName.substring(0, prefix.length()).equals(prefix)) {
                            AlgorithmFamily algorithmFamily = new AlgorithmFamily();
                            algorithmFamily.setFamilyName(prefix);
                            if (!familiesInScope.contains(algorithmFamily)) {
                                familiesInScope.add(algorithmFamily);
                                algorithmFamily.getParticipants().add(scope.getElements().get(i));
                                algorithmFamily.getParticipants().add(scope.getElements().get(j));
                            } else {
                                algorithmFamily = familiesInScope.get(familiesInScope.indexOf(algorithmFamily));
                                if (!algorithmFamily.getParticipants().contains(scope.getElements().get(j))) {
                                    algorithmFamily.getParticipants().add(scope.getElements().get(j));
                                }
                            }
                        }
                    }
                }

                for (String suffix : suffixes) {
                    if (elementName.length() >= suffix.length()) {
                        if (elementName.substring(elementName.length() - suffix.length()).equals(suffix)) {
                            AlgorithmFamily algorithmFamily = new AlgorithmFamily();
                            algorithmFamily.setFamilyName(suffix);
                            if (!familiesInScope.contains(algorithmFamily)) {
                                familiesInScope.add(algorithmFamily);
                                algorithmFamily.getParticipants().add(scope.getElements().get(i));
                                algorithmFamily.getParticipants().add(scope.getElements().get(j));
                            } else {
                                algorithmFamily = familiesInScope.get(familiesInScope.indexOf(algorithmFamily));
                                if (!algorithmFamily.getParticipants().contains(scope.getElements().get(j))) {
                                    algorithmFamily.getParticipants().add(scope.getElements().get(j));
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private void addFamiliesWithSameMethod(Scope scope, List<AlgorithmFamily> familiesInScope) {
        for (int i = 0; i < scope.getElements().size(); i++) {
            Element iElement = scope.getElements().get(i);

            List<Method> iMethods = getMethodsFromElement(iElement);
            if (iMethods == null || iMethods.isEmpty()) {
                continue;
            }

            for (int j = i + 1; j < scope.getElements().size(); j++) {
                Element jElement = scope.getElements().get(i);

                List<Method> jMethods = getMethodsFromElement(jElement);
                if (jMethods == null || jMethods.isEmpty()) {
                    continue;
                }

                System.out.println("");
            }
        }
    }

    private List<Method> getMethodsFromElement(Element iElement) {
        List<Method> iMethods;
        if (iElement instanceof arquitetura.representation.Class) {
            Class iClass = (Class) iElement;
            iMethods = iClass.getAllMethods();
        } else if (iElement instanceof Interface) {
            Interface iInterface = (Interface) iElement;
            iMethods = iInterface.getOperations();
        } else {
            return null;
        }
        return iMethods;
    }

    @Override
    protected boolean verifyPSPLA(Scope scope) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean apply(Scope scope) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
