package br.ufpr.inf.opla.patterns.util;

import arquitetura.representation.Element;
import arquitetura.representation.Interface;
import arquitetura.representation.relationship.DependencyRelationship;
import arquitetura.representation.relationship.RealizationRelationship;
import arquitetura.representation.relationship.Relationship;
import arquitetura.representation.relationship.UsageRelationship;
import java.util.ArrayList;
import java.util.List;

public class RelationshipUtil {

    private static final RelationshipUtil INSTANCE = new RelationshipUtil();

    private RelationshipUtil() {
    }

    public static RelationshipUtil getInstance() {
        return INSTANCE;
    }

    public List<Element> getUsedElementsFromRelationship(Relationship relationship) {
        List<Element> usedElements = new ArrayList<>();
        if (relationship instanceof UsageRelationship) {
            UsageRelationship usage = (UsageRelationship) relationship;
            usedElements.add(usage.getSupplier());
        } else if (relationship instanceof DependencyRelationship) {
            DependencyRelationship dependency = (DependencyRelationship) relationship;
            usedElements.add(dependency.getSupplier());
        }
        return usedElements;
    }

    public Interface getImplementedInterface(Relationship relationship) {
        if (relationship instanceof RealizationRelationship) {
            RealizationRelationship realization = (RealizationRelationship) relationship;
            return realization.getSupplier() instanceof Interface ? (Interface) realization.getSupplier() : null;
        }
        return null;
    }

}
