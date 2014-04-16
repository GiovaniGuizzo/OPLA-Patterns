package br.ufpr.inf.opla.patterns.util;

import arquitetura.exceptions.ConcernNotFoundException;
import arquitetura.helpers.UtilResources;
import arquitetura.representation.Architecture;
import arquitetura.representation.Class;
import arquitetura.representation.Concern;
import arquitetura.representation.Element;
import arquitetura.representation.Interface;
import arquitetura.representation.Method;
import arquitetura.representation.Package;
import arquitetura.representation.ParameterMethod;
import arquitetura.representation.Patterns;
import arquitetura.representation.relationship.Relationship;
import arquitetura.touml.Types;
import arquitetura.touml.VisibilityKind;
import br.ufpr.inf.opla.patterns.repositories.ArchitectureRepository;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class MediatorUtil {

    private MediatorUtil() {
    }

    public static Class getOrCreateEventOfInterestClass() {
        Architecture architecture = ArchitectureRepository.getCurrentArchitecture();

        List<arquitetura.representation.Class> eventOfInterestList = architecture.findClassByName("EventOfInterest");
        Class eventOfInterest;
        if (eventOfInterestList.isEmpty()) {
            eventOfInterest = architecture.createClass("EventOfInterest", false);

            //Cria atributos
            eventOfInterest.createAttribute("invoker", Types.custom("Object"), VisibilityKind.PRIVATE_LITERAL);
            eventOfInterest.createAttribute("action", Types.STRING, VisibilityKind.PRIVATE_LITERAL);
            eventOfInterest.createAttribute("parameters", Types.custom("Object"), VisibilityKind.PRIVATE_LITERAL);

            //Cria métodos
            eventOfInterest.createMethod("getInvoker", "Object", false, null);
            eventOfInterest.createMethod("getAction", "String", false, null);
            eventOfInterest.createMethod("getParameters", "Object", false, null);

            eventOfInterest.createMethod("setInvoker", "void", false, Arrays.asList(new ParameterMethod[]{new ParameterMethod("invoker", "Object", "in")}));
            eventOfInterest.createMethod("setAction", "void", false, Arrays.asList(new ParameterMethod[]{new ParameterMethod("action", "String", "in")}));
            eventOfInterest.createMethod("setParameters", "void", false, Arrays.asList(new ParameterMethod[]{new ParameterMethod("parameters", "Object", "in")}));

            //Adiciona padrões
            eventOfInterest.getPatternsOperations().applyPattern(Patterns.MEDIATOR);
            eventOfInterest.getPatternsOperations().applyPattern(Patterns.FACADE);
        } else {
            eventOfInterest = eventOfInterestList.get(0);
        }

        return eventOfInterest;
    }

    public static Interface getOrCreateMediatorInterface(List<Element> participants, Concern concern) throws ConcernNotFoundException, Exception {
        Interface mediator = null;

        Class eventOfInterest = getOrCreateEventOfInterestClass();
        for (Relationship relationship : ElementUtil.getRelationships(eventOfInterest)) {
            Element client = RelationshipUtil.getClientElementFromRelationship(relationship);
            if (client != null
                    && !client.equals(eventOfInterest)
                    && client.getOwnConcerns().contains(concern)
                    && client instanceof Interface
                    && client.getName().endsWith("Mediator")) {
                mediator = (Interface) client;
                break;
            }
        }

        if (mediator == null) {
            Architecture architecture = ArchitectureRepository.getCurrentArchitecture();

            String namespace = ElementUtil.getNameSpace(participants);
            String packageName = UtilResources.extractPackageName(namespace);
            boolean naArquitetura = packageName.equalsIgnoreCase("model");
            if (naArquitetura) {
                mediator = architecture.createInterface(Character.toUpperCase(concern.getName().charAt(0)) + "Mediator");
            } else {
                Package aPackage = architecture.findPackageByName(UtilResources.extractPackageName(namespace));
                mediator = aPackage.createInterface(Character.toUpperCase(concern.getName().charAt(0)) + "Mediator");
            }

            mediator.addConcern(concern.getName());
            Method operation = new Method(concern.getName() + "Event", mediator.getName(), "void", false, UUID.randomUUID().toString());
            mediator.addExternalOperation(operation);
        }

        return mediator;
    }
}
