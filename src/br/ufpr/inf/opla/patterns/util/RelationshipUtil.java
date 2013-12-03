package br.ufpr.inf.opla.patterns.util;

import arquitetura.representation.Element;
import arquitetura.representation.Interface;
import arquitetura.representation.relationship.DependencyRelationship;
import arquitetura.representation.relationship.GeneralizationRelationship;
import arquitetura.representation.relationship.RealizationRelationship;
import arquitetura.representation.relationship.Relationship;
import arquitetura.representation.relationship.UsageRelationship;
import java.util.UUID;

public class RelationshipUtil {

    private RelationshipUtil() {
    }

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
        if (relationship instanceof UsageRelationship) {
            UsageRelationship usage = (UsageRelationship) relationship;

            usage.getSupplier().removeRelationship(usage);
            usage.getClient().removeRelationship(usage);

            usage.setSupplier(supplier);
            usage.setClient(client);
            supplier.addRelationship(usage);
            client.addRelationship(usage);
        } else if (relationship instanceof DependencyRelationship) {
            DependencyRelationship dependency = (DependencyRelationship) relationship;

            dependency.getSupplier().removeRelationship(dependency);
            dependency.getClient().removeRelationship(dependency);

            dependency.setSupplier(supplier);
            dependency.setClient(client);
            supplier.addRelationship(dependency);
            client.addRelationship(dependency);
        }
    }

    public static RealizationRelationship createNewRealizationRelationship(String relationshipName, Element client, Element supplier) {
        RealizationRelationship realizationRelationship = new RealizationRelationship(client, supplier, relationshipName, UUID.randomUUID().toString());
        client.addRelationship(realizationRelationship);
        supplier.addRelationship(realizationRelationship);
        client.getArchitecture().addRelationship(realizationRelationship);
        return realizationRelationship;
    }

    public static GeneralizationRelationship createNewGeneralizationRelationship(String aimplements, Element child, Element parent) {
        GeneralizationRelationship generalizationRelationship = new GeneralizationRelationship(parent, child, parent.getArchitecture(), UUID.randomUUID().toString());
        child.addRelationship(generalizationRelationship);
        parent.addRelationship(generalizationRelationship);
        parent.getArchitecture().addRelationship(generalizationRelationship);
        return generalizationRelationship;
    }

    public static UsageRelationship createNewUsageRelationship(String name, Element client, Element supplier) {
        UsageRelationship usage = new UsageRelationship(name, supplier, client, UUID.randomUUID().toString());
        client.addRelationship(usage);
        supplier.addRelationship(usage);
        client.getArchitecture().addRelationship(usage);
        return usage;
    }

}
