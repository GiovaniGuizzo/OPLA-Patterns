package br.ufpr.inf.opla.patterns.util;

import arquitetura.representation.Architecture;
import arquitetura.representation.Element;
import arquitetura.representation.Interface;
import arquitetura.representation.relationship.AssociationRelationship;
import arquitetura.representation.relationship.DependencyRelationship;
import arquitetura.representation.relationship.GeneralizationRelationship;
import arquitetura.representation.relationship.RealizationRelationship;
import arquitetura.representation.relationship.Relationship;
import arquitetura.representation.relationship.UsageRelationship;
import java.util.UUID;

public class RelationshipUtil {

    public static Element getUsedElementFromRelationship(Relationship relationship) {
        Element supplier = null;
        if (relationship instanceof UsageRelationship) {
            UsageRelationship usage = (UsageRelationship) relationship;
            supplier = usage.getSupplier();
        } else if (relationship instanceof DependencyRelationship) {
            DependencyRelationship dependency = (DependencyRelationship) relationship;
            supplier = dependency.getSupplier();
        }
        return supplier;
    }

    public static Interface getImplementedInterface(Relationship relationship) {
        if (relationship instanceof RealizationRelationship) {
            RealizationRelationship realization = (RealizationRelationship) relationship;
            return realization.getSupplier() instanceof Interface ? (Interface) realization.getSupplier() : null;
        }
        return null;
    }

    public static Element getExtendedElement(Relationship relationship) {
        if (relationship instanceof GeneralizationRelationship) {
            GeneralizationRelationship generalization = (GeneralizationRelationship) relationship;
            return generalization.getParent();
        }
        return null;
    }

    public static Element getSubElement(Relationship relationship) {
        if (relationship instanceof GeneralizationRelationship) {
            GeneralizationRelationship generalization = (GeneralizationRelationship) relationship;
            return generalization.getChild();
        } else if (relationship instanceof RealizationRelationship) {
            RealizationRelationship realization = (RealizationRelationship) relationship;
            return realization.getClient();
        }
        return null;
    }

    public static void moveRelationship(Relationship relationship, Element client, Element supplier) {
        Architecture architecture = client.getArchitecture();
        if (relationship instanceof UsageRelationship) {
            UsageRelationship usage = (UsageRelationship) relationship;

            ElementUtil.removeRelationship(architecture, usage);
            ElementUtil.removeRelationship(architecture, usage);
            ElementUtil.verifyAndRemoveRequiredInterface(usage.getClient(), usage.getSupplier());

            usage.setSupplier(supplier);
            usage.setClient(client);
            ElementUtil.addRelationship(architecture, usage);
            ElementUtil.addRelationship(architecture, usage);
            ElementUtil.addRequiredInterface(client, supplier);
        } else if (relationship instanceof DependencyRelationship) {
            DependencyRelationship dependency = (DependencyRelationship) relationship;

            ElementUtil.removeRelationship(architecture, dependency);
            ElementUtil.removeRelationship(architecture, dependency);
            ElementUtil.verifyAndRemoveRequiredInterface(dependency.getClient(), dependency.getSupplier());

            dependency.setSupplier(supplier);
            dependency.setClient(client);
            ElementUtil.addRelationship(architecture, dependency);
            ElementUtil.addRelationship(architecture, dependency);
            ElementUtil.addRequiredInterface(client, supplier);
        }
    }

    public static RealizationRelationship createNewRealizationRelationship(String relationshipName, Element client, Element supplier) {
        Architecture architecture = client.getArchitecture();
        
        RealizationRelationship realizationRelationship = new RealizationRelationship(client, supplier, relationshipName, UUID.randomUUID().toString());
        ElementUtil.addRelationship(architecture, realizationRelationship);
        ElementUtil.addRelationship(architecture, realizationRelationship);
        client.getArchitecture().addRelationship(realizationRelationship);
        ElementUtil.addImplementedInterface(client, supplier);
        return realizationRelationship;
    }

    public static GeneralizationRelationship createNewGeneralizationRelationship(Element child, Element parent) {
        Architecture architecture = child.getArchitecture();
        
        GeneralizationRelationship generalizationRelationship = new GeneralizationRelationship(parent, child, parent.getArchitecture().getRelationshipHolder(), UUID.randomUUID().toString());
        ElementUtil.addRelationship(architecture, generalizationRelationship);
        ElementUtil.addRelationship(architecture, generalizationRelationship);
        parent.getArchitecture().addRelationship(generalizationRelationship);
        ElementUtil.addImplementedInterface(child, parent);
        return generalizationRelationship;
    }

    public static UsageRelationship createNewUsageRelationship(String relationshipName, Element client, Element supplier) {
        Architecture architecture = client.getArchitecture();
        
        UsageRelationship usage = new UsageRelationship(relationshipName, supplier, client, UUID.randomUUID().toString());
        ElementUtil.addRelationship(architecture, usage);
        ElementUtil.addRelationship(architecture, usage);
        client.getArchitecture().addRelationship(usage);
        ElementUtil.addRequiredInterface(client, supplier);
        return usage;
    }

    public static AssociationRelationship createNewAggregationRelationship(String name, Element aggregator, Element aggregated) {
        Architecture architecture = aggregated.getArchitecture();
        
        AssociationRelationship associationRelationship = new AssociationRelationship(aggregator, aggregated);
        associationRelationship.setName(name);
        associationRelationship.getParticipants().get(1).setAggregation("shared");
        ElementUtil.addRelationship(architecture, associationRelationship);
        ElementUtil.addRelationship(architecture, associationRelationship);
        aggregator.getArchitecture().addRelationship(associationRelationship);
        ElementUtil.addRequiredInterface(aggregator, aggregated);
        return associationRelationship;
    }

    private RelationshipUtil() {
    }

}
