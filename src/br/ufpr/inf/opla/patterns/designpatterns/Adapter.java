package br.ufpr.inf.opla.patterns.designpatterns;

import arquitetura.exceptions.ConcernNotFoundException;
import arquitetura.helpers.UtilResources;
import arquitetura.representation.Architecture;
import arquitetura.representation.Concern;
import arquitetura.representation.Element;
import arquitetura.representation.Interface;
import arquitetura.representation.Method;
import arquitetura.representation.Variant;
import arquitetura.representation.relationship.Relationship;
import br.ufpr.inf.opla.patterns.models.DesignPattern;
import br.ufpr.inf.opla.patterns.models.Scope;
import br.ufpr.inf.opla.patterns.util.ElementUtil;
import br.ufpr.inf.opla.patterns.util.MethodUtil;
import br.ufpr.inf.opla.patterns.util.RelationshipUtil;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.collections4.CollectionUtils;

public class Adapter extends DesignPattern {

    private static volatile Adapter INSTANCE;

    public static synchronized Adapter getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new Adapter();
        }
        return INSTANCE;
    }

    private Adapter() {
        super("Adapter", "Structural");
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
        arquitetura.representation.Class adapterClass = null;
        if (target != null
                && adaptee != null
                && (target instanceof arquitetura.representation.Class || target instanceof Interface)
                && (adaptee instanceof arquitetura.representation.Class || adaptee instanceof Interface)) {
            try {
                arquitetura.representation.Package aPackage = null;
                Architecture architecture = adaptee.getArchitecture();

                List<Element> tempElements;

                String namespace = adaptee.getNamespace();
                String packageName = UtilResources.extractPackageName(namespace);

                boolean naArquitetura = packageName.equalsIgnoreCase("model");
                if (naArquitetura) {
                    adapterClass = target.getArchitecture().createClass(adaptee.getName() + "Adapter", false);

                    architecture.removeClass(adapterClass);

                    tempElements = Collections.unmodifiableList(new ArrayList<>(architecture.getElements()));
                } else {
                    aPackage = architecture.findPackageByName(packageName);
                    adapterClass = aPackage.createClass(adaptee.getName() + "Adapter", false);

                    aPackage.removeClass(adapterClass);

                    tempElements = Collections.unmodifiableList(new ArrayList<>(aPackage.getElements()));
                }

                adapterClass.setNamespace(adaptee.getNamespace());
                adapterClass.setVariant(adaptee.getVariant());

                //Implements/Extends and add all methods.
                if (target instanceof arquitetura.representation.Class) {
                    arquitetura.representation.Class targetClass = (arquitetura.representation.Class) target;
                    RelationshipUtil.createNewGeneralizationRelationship(adapterClass, target);
                    Set<Method> clonedMethods = MethodUtil.cloneMethods(new HashSet<>(targetClass.getAllAbstractMethods()));
                    for (Method method : clonedMethods) {
                        adapterClass.addExternalMethod(method);
                    }
                } else {
                    Interface targetInterface = (Interface) target;
                    RelationshipUtil.createNewRealizationRelationship("implements", adapterClass, target);
                    Set<Method> clonedMethods = MethodUtil.cloneMethods(targetInterface.getOperations());
                    for (Method method : clonedMethods) {
                        adapterClass.addExternalMethod(method);
                    }
                }

                RelationshipUtil.createNewUsageRelationship("adaptee", adapterClass, adaptee);

                Relationship relationshipToBeExcluded = null;
                if (adaptee.getClass().equals(target.getClass())) {
                    for (Relationship relationship : ElementUtil.getRelationships(adaptee)) {
                        if (target.equals(RelationshipUtil.getExtendedElement(relationship))) {
                            relationshipToBeExcluded = relationship;
                            break;
                        }
                    }
                } else {
                    for (Relationship relationship : ElementUtil.getRelationships(adaptee)) {
                        if (target.equals(RelationshipUtil.getImplementedInterface(relationship))) {
                            relationshipToBeExcluded = relationship;
                            break;
                        }
                    }
                }

                if (relationshipToBeExcluded != null) {
                    ElementUtil.removeRelationship(architecture, relationshipToBeExcluded);
                    ElementUtil.removeRelationship(architecture, relationshipToBeExcluded);
                    architecture.removeRelationship(relationshipToBeExcluded);
                }

                //Copy concerns
                for (Concern concern : CollectionUtils.union(target.getOwnConcerns(), adaptee.getOwnConcerns())) {
                    try {
                        adapterClass.addConcern(concern.getName());
                    } catch (ConcernNotFoundException ex) {
                        Logger.getLogger(Adapter.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }

                //Move variants
                Variant variant = adapterClass.getVariant();
                if (variant != null) {
                    variant.setVariantElement(adapterClass);
                    adaptee.setVariant(null);
                }

                int count = 1;
                String name = adapterClass.getName();
                while (tempElements.contains(adapterClass)) {
                    count++;
                    adapterClass.setName(name + Integer.toString(count));
                }

                if (naArquitetura) {
                    architecture.addExternalClass(adapterClass);
                } else if (aPackage != null) {
                    aPackage.addExternalClass(adapterClass);
                }
            } catch (Exception ex) {
                Logger.getLogger(Adapter.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return adapterClass;
    }

}
