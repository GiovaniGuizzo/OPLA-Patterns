package br.ufpr.inf.opla.patterns.models;

import arquitetura.representation.Element;
import br.ufpr.inf.opla.patterns.models.ps.PS;
import br.ufpr.inf.opla.patterns.models.ps.PSPLA;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Scope {

    private final List<Element> elements;
    private final List<PS> ps;
    private final List<PSPLA> psPla;

    public Scope() {
        this.elements = new ArrayList<>();
        this.ps = new ArrayList<>();
        this.psPla = new ArrayList<>();
    }

    public List<Element> getElements() {
        return elements;
    }

    public List<PS> getPs() {
        return ps;
    }

    public List<PSPLA> getPsPla() {
        return psPla;
    }

    public void addPs(PS ps) {
        this.ps.add(ps);
    }

    public void addPsPla(PSPLA psPla) {
        this.psPla.add(psPla);
    }

    public boolean isPs() {
        return !ps.isEmpty();
    }

    public boolean isPsPla() {
        return !psPla.isEmpty();
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
