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
    private final List<PSPLA> psPLA;

    public Scope() {
        this.elements = new ArrayList<>();
        this.ps = new ArrayList<>();
        this.psPLA = new ArrayList<>();
    }

    public List<Element> getElements() {
        return elements;
    }

    public List<PS> getPS() {
        return ps;
    }

    public List<PSPLA> getPSPLA() {
        return psPLA;
    }

    public void addPS(PS ps) {
        this.ps.add(ps);
    }

    public void addPSPLA(PSPLA psPla) {
        this.psPLA.add(psPla);
    }

    public boolean isPS() {
        return !ps.isEmpty();
    }

    public boolean isPSPLA() {
        return !psPLA.isEmpty();
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
        return Objects.equals(this.elements, other.elements);
    }

}
