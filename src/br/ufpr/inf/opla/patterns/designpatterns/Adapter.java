package br.ufpr.inf.opla.patterns.designpatterns;

import arquitetura.representation.Element;
import arquitetura.representation.Interface;
import arquitetura.representation.Variant;
import arquitetura.representation.relationship.Relationship;
import br.ufpr.inf.opla.patterns.models.DesignPattern;
import br.ufpr.inf.opla.patterns.models.Scope;
import br.ufpr.inf.opla.patterns.util.MethodUtil;
import br.ufpr.inf.opla.patterns.util.RelationshipUtil;
import java.util.UUID;
import org.apache.commons.collections4.CollectionUtils;

public class Adapter extends DesignPattern {

    private static final Adapter INSTANCE = new Adapter();

    private Adapter() {
        super("Adapter", "Structural");
    }

    public static Adapter getInstance() {
        return INSTANCE;
    }

    @Override
    public boolean verifyPS(Scope scope) {
        return false;
    }

    @Override
    public boolean verifyPSPLA(Scope scope) {
        return false;
    }

    @Override
    public boolean apply(Scope scope) {
        return false;
    }

    //TODO - Édipo - Adicionar estereótipos Adapter.
    public arquitetura.representation.Class applyAdapter(Element target, Element adaptee) {
        if (target != null
                && adaptee != null
                && (target instanceof arquitetura.representation.Class || target instanceof Interface)
                && (adaptee instanceof arquitetura.representation.Class || adaptee instanceof Interface)) {
            arquitetura.representation.Class adapterClass = new arquitetura.representation.Class(target.getArchitecture(), adaptee.getName() + "Adapter", adaptee.getVariant(), false, target.getNamespace(), UUID.randomUUID().toString());
            adapterClass.getArchitecture().getElements().add(adapterClass);

            //Implements/Extends and add all methods.
            if (target instanceof arquitetura.representation.Class) {
                arquitetura.representation.Class targetClass = (arquitetura.representation.Class) target;
                RelationshipUtil.createNewGeneralizationRelationship("implements", adapterClass, target);
                adapterClass.getAllMethods().addAll(MethodUtil.cloneMethods(targetClass.getAllAbstractMethods()));
            } else {
                Interface targetInterface = (Interface) target;
                RelationshipUtil.createNewRealizationRelationship("implements", adapterClass, target);
                adapterClass.getAllMethods().addAll(MethodUtil.cloneMethods(targetInterface.getOperations()));
            }

            RelationshipUtil.createNewUsageRelationship("adaptee", adapterClass, adaptee);

            Relationship relationshipToBeExcluded = null;
            if (adaptee.getClass().equals(target.getClass())) {
                for (Relationship relationship : adaptee.getRelationships()) {
                    if (target.equals(RelationshipUtil.getExtendedElement(relationship))) {
                        relationshipToBeExcluded = relationship;
                        break;
                    }
                }
            } else {
                for (Relationship relationship : adaptee.getRelationships()) {
                    if (target.equals(RelationshipUtil.getImplementedInterface(relationship))) {
                        relationshipToBeExcluded = relationship;
                        break;
                    }
                }
            }

            if (relationshipToBeExcluded != null) {
                target.getRelationships().remove(relationshipToBeExcluded);
                adaptee.getRelationships().remove(relationshipToBeExcluded);
                adapterClass.getArchitecture().getAllRelationships().remove(relationshipToBeExcluded);
            }

            //Copy concerns
            adapterClass.getOwnConcerns().addAll(CollectionUtils.union(target.getOwnConcerns(), adaptee.getOwnConcerns()));

            //Move variants
            Variant variant = adapterClass.getVariant();
            if (variant != null) {
                variant.setVariantElement(adapterClass);
                adaptee.setVariant(null);
            }
            return adapterClass;
        }
        return null;
    }

}
