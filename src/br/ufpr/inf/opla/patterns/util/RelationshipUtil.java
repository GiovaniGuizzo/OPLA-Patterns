package br.ufpr.inf.opla.patterns.util;

import arquitetura.representation.Element;
import arquitetura.representation.relationship.DependencyRelationship;
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
        switch (relationship.getType()) {
            case "usage": {
                UsageRelationship usage = (UsageRelationship) relationship;
                usedElements.add(usage.getSupplier());
                break;
            }
            case "dependency": {
                DependencyRelationship dependency = (DependencyRelationship) relationship;
                usedElements.add(dependency.getSupplier());
                break;
            }
        }
        return usedElements;
    }

}
