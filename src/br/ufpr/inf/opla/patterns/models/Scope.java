package br.ufpr.inf.opla.patterns.models;

import arquitetura.representation.Element;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Scope {

    private final List<Element> elements;
    private final List<Element> elementsOfInterest;
    private DesignPattern PSOf;
    private DesignPattern PSPLAOf;

    public Scope() {
        this.elementsOfInterest = new ArrayList<>();
        this.elements = new ArrayList<>();
    }

    public List<Element> getElements() {
        return elements;
    }

    public boolean isPS() {
        return PSOf != null;
    }

    public boolean isPSPLA() {
        return PSPLAOf != null;
    }

    public DesignPattern getPSOf() {
        return PSOf;
    }

    public void setPSOf(DesignPattern PSOf) {
        this.PSOf = PSOf;
    }

    public DesignPattern getPSPLAOf() {
        return PSPLAOf;
    }

    public void setPSPLAOf(DesignPattern PSPLAOf) {
        this.PSPLAOf = PSPLAOf;
    }

    public List<Element> getElementsOfInterest() {
        return elementsOfInterest;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Scope other = (Scope) obj;
        if (!Objects.equals(this.elements, other.elements)) {
            return false;
        }
        return true;
    }

}
