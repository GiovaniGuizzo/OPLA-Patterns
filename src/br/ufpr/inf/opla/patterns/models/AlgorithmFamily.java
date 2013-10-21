package br.ufpr.inf.opla.patterns.models;

import arquitetura.representation.Element;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class AlgorithmFamily implements Comparable<AlgorithmFamily> {

    private final List<Element> participants;
    private String familyName;

    public AlgorithmFamily() {
        this.participants = new ArrayList<>();
    }

    public String getFamilyName() {
        return familyName;
    }

    public void setFamilyName(String familyName) {
        this.familyName = familyName;
    }

    public List<Element> getParticipants() {
        return participants;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 37 * hash + Objects.hashCode(this.familyName);
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
        return Objects.equals(this.familyName, other.familyName);
    }

    @Override
    public int compareTo(AlgorithmFamily o) {
        int compare = Integer.compare(this.getParticipants().size(), o.getParticipants().size());
        if (compare == 0) {
            compare = Integer.compare(this.getFamilyName().length(), o.getFamilyName().length());
            if (compare == 0) {
                compare = this.getFamilyName().compareToIgnoreCase(o.getFamilyName());
            }
        }
        return compare;
    }

}
