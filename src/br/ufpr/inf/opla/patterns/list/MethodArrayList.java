package br.ufpr.inf.opla.patterns.list;

import arquitetura.representation.Method;
import java.util.ArrayList;
import java.util.List;

public class MethodArrayList extends ArrayList<Method> {

    public MethodArrayList(List<Method> methods) {
        super((methods != null ? methods : new ArrayList<Method>()));
    }

    public MethodArrayList() {
    }

    @Override
    public boolean contains(Object method) {
        if (method instanceof Method) {
            Method aMethod = (Method) method;
            for (int i = 0; i < this.size(); i++) {
                Method otherMethod = this.get(i);
                if (areMethodsEqual(aMethod, otherMethod)) {
                    return true;
                }
            }
            return false;
        } else {
            return super.contains(method); //To change body of generated methods, choose Tools | Templates.
        }
    }

    @Override
    public int indexOf(Object method) {
        if (method instanceof Method) {
            for (int i = 0; i < this.size(); i++) {
                Method iMethod = this.get(i);
                if(areMethodsEqual(iMethod, (Method) method)){
                    return i;
                }
            }
            return -1;
        } else {
            return super.indexOf(method); //To change body of generated methods, choose Tools | Templates.
        }
    }

    private boolean areMethodsEqual(Method method, Method otherMethod) {
        if (method == null && otherMethod == null) {
            return true;
        } else if (method == null || otherMethod == null) {
            return false;
        }
        return (method.getReturnType() == null ? otherMethod.getReturnType() == null : method.getReturnType().equals(otherMethod.getReturnType()))
                && (method.getName() == null ? otherMethod.getName() == null : method.getName().equals(otherMethod.getName()));
    }
}
