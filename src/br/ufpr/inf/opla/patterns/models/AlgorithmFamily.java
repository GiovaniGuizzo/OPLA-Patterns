package br.ufpr.inf.opla.patterns.models;

import arquitetura.representation.Element;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class AlgorithmFamily implements Comparable<AlgorithmFamily> {

    private final List<Element> participants;
    private String name;

    public AlgorithmFamily() {
        this.participants = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Element> getParticipants() {
        return participants;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 37 * hash + Objects.hashCode(this.name);
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
        final AlgorithmFamily other = (AlgorithmFamily) obj;
        return Objects.equals(this.name, other.name);
    }

    @Override
    public int compareTo(AlgorithmFamily o) {
        int compare = Integer.compare(this.getParticipants().size(), o.getParticipants().size());
        if (compare == 0) {
            compare = Integer.compare(this.getName().length(), o.getName().length());
            if (compare == 0) {
                compare = this.getName().compareToIgnoreCase(o.getName());
            }
        }
        return compare;
    }

}
