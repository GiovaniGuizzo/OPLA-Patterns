package br.ufpr.inf.opla.patterns.util;

import arquitetura.representation.Concern;
import arquitetura.representation.Element;
import arquitetura.representation.Interface;
import arquitetura.representation.Method;
import arquitetura.representation.ParameterMethod;
import arquitetura.representation.relationship.Relationship;
import br.ufpr.inf.opla.patterns.list.MethodArrayList;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import org.apache.commons.collections4.CollectionUtils;

public class MethodUtil {

    private MethodUtil() {
    }

    public static List<Method> getMethodsFromElement(Element iElement) {
        List<Method> iMethods;
        if (iElement instanceof arquitetura.representation.Class) {
            arquitetura.representation.Class iClass = (arquitetura.representation.Class) iElement;
            iMethods = iClass.getAllMethods();
        } else if (iElement instanceof Interface) {
            Interface iInterface = (Interface) iElement;
            iMethods = iInterface.getOperations();
        } else {
            return null;
        }
        return iMethods;
    }

    public static List<Method> getMethodsFromSetOfElements(List<Element> elements) {
        MethodArrayList methods = new MethodArrayList();
        for (Element element : elements) {
            MethodArrayList elementMethods = new MethodArrayList(getMethodsFromElement(element));
            for (Method elementMethod : elementMethods) {
                if (!methods.contains(elementMethod)) {
                    methods.add(elementMethod);
                }
            }
        }
        return methods;
    }

    public static List<Method> createMethodsFromSetOfElements(List<Element> elements) {
        MethodArrayList methods = new MethodArrayList();
        for (Element element : elements) {
            MethodArrayList methodsFromElement = new MethodArrayList(getMethodsFromElement(element));
            for (Method elementMethod : methodsFromElement) {
                if (!methods.contains(elementMethod)) {
                    methods.add(cloneMethod(elementMethod));
                } else {
                    Method method = methods.get(methods.indexOf(elementMethod));
                    mergeMethodsToMethodA(method, elementMethod);
                }
            }
        }
        return methods;
    }

    public static Method cloneMethod(Method method) {
        Method newMethod = new Method(Method.getArchitecture(), method.getName(), method.getReturnType(), method.getNamespace(), method.isAbstract(), UUID.randomUUID().toString());
        newMethod.getParameters().addAll(method.getParameters());
        newMethod.getOwnConcerns().addAll(method.getOwnConcerns());
        return newMethod;
    }

    public static List<Method> cloneMethods(List<Method> methodsToBeCloned) {
        List<Method> methods = new ArrayList<>();
        for (Method method : methodsToBeCloned) {
            methods.add(cloneMethod(method));
        }
        return methods;
    }

    public static Method mergeMethodsToNewOne(Method methodA, Method methodB) {
        Method newMethod = cloneMethod(methodA);

        mergeMethodsToMethodA(newMethod, methodB);

        return newMethod;
    }

    public static void mergeMethodsToMethodA(Method methodA, Method methodB) {
        //TODO - Édipo - Adicionar loop para alterar o nome do parâmetro.
        ArrayList<ParameterMethod> parameters = new ArrayList<>(methodA.getParameters());
        methodA.getParameters().clear();
        methodA.getParameters().addAll(CollectionUtils.union(parameters, methodB.getParameters()));

        ArrayList<Concern> concerns = new ArrayList<>(methodA.getOwnConcerns());
        methodA.getOwnConcerns().clear();
        methodA.getOwnConcerns().addAll(CollectionUtils.union(concerns, methodB.getOwnConcerns()));
    }

    static List<Method> getAllMethodsFromHierarchy(Element element) {
        MethodArrayList methods = new MethodArrayList();
        if (element instanceof arquitetura.representation.Class) {
            arquitetura.representation.Class klass = (arquitetura.representation.Class) element;
            methods.addAll(klass.getAllMethods());
        } else if (element instanceof Interface) {
            Interface aInterface = (Interface) element;
            methods.addAll(aInterface.getOperations());
        }
        for (Relationship relationship : element.getRelationships()) {
            Element extendedElement = RelationshipUtil.getExtendedElement(relationship);
            if (extendedElement != null && !element.equals(extendedElement)) {
                methods.addAll(getAllMethodsFromHierarchy(extendedElement));
            }
        }
        return methods;
    }
}
