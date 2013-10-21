package br.ufpr.inf.opla.patterns.util;

import arquitetura.representation.Element;
import arquitetura.representation.Interface;
import arquitetura.representation.Method;
import java.util.List;

public class ElementUtil {

    private static final ElementUtil INSTANCE = new ElementUtil();

    private ElementUtil() {
    }

    public static ElementUtil getInstance() {
        return INSTANCE;
    }

    public List<Method> getMethodsFromElement(Element iElement) {
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
}
