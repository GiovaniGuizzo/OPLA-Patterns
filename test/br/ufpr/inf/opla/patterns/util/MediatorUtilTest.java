package br.ufpr.inf.opla.patterns.util;

import arquitetura.exceptions.AttributeNotFoundException;
import arquitetura.exceptions.MethodNotFoundException;
import arquitetura.representation.Architecture;
import arquitetura.representation.Attribute;
import arquitetura.representation.Class;
import arquitetura.representation.Concern;
import arquitetura.representation.Element;
import arquitetura.representation.Interface;
import arquitetura.representation.Method;
import arquitetura.representation.relationship.Relationship;
import arquitetura.touml.VisibilityKind;
import br.ufpr.inf.opla.patterns.repositories.ArchitectureRepository;
import java.util.ArrayList;
import java.util.List;
import org.junit.Assert;
import org.junit.Test;

public class MediatorUtilTest {

    public MediatorUtilTest() {
    }

    @Test
    public void testGetOrCreateEventOfInterestClass() throws Exception {
        ArchitectureRepository.getArchitecture(ArchitectureRepository.OTHER_MODELS[7]);

        Class eventOfInterest = MediatorUtil.getOrCreateEventOfInterestClass();
        testEventOfInterest(eventOfInterest);
        eventOfInterest.removeMethod(eventOfInterest.findMethodByName("setInvoker"));
        eventOfInterest = MediatorUtil.getOrCreateEventOfInterestClass();
        testEventOfInterest(eventOfInterest);
    }

    private void testEventOfInterest(Class eventOfInterest) throws MethodNotFoundException, AttributeNotFoundException {
        Assert.assertNotNull(eventOfInterest);

        //Methods
        Assert.assertEquals(6, eventOfInterest.getAllMethods().size());

        Method setInvoker = eventOfInterest.findMethodByName("setInvoker");
        Assert.assertNotNull(setInvoker);
        Assert.assertEquals("void", setInvoker.getReturnType());
        Assert.assertEquals(1, setInvoker.getParameters().size());

        Method setAction = eventOfInterest.findMethodByName("setAction");
        Assert.assertNotNull(setAction);
        Assert.assertEquals("void", setAction.getReturnType());
        Assert.assertEquals(1, setAction.getParameters().size());

        Method setParameters = eventOfInterest.findMethodByName("setParameters");
        Assert.assertNotNull(setParameters);
        Assert.assertEquals("void", setParameters.getReturnType());
        Assert.assertEquals(1, setParameters.getParameters().size());

        Method getInvoker = eventOfInterest.findMethodByName("getInvoker");
        Assert.assertNotNull(getInvoker);
        Assert.assertEquals("Object", getInvoker.getReturnType());
        Assert.assertEquals(0, getInvoker.getParameters().size());

        Method getAction = eventOfInterest.findMethodByName("getAction");
        Assert.assertNotNull(getAction);
        Assert.assertEquals("String", getAction.getReturnType());
        Assert.assertEquals(0, getAction.getParameters().size());

        Method getParameters = eventOfInterest.findMethodByName("getParameters");
        Assert.assertNotNull(getParameters);
        Assert.assertEquals("Object", getParameters.getReturnType());
        Assert.assertEquals(0, getParameters.getParameters().size());

        //Attributes
        Assert.assertEquals(3, eventOfInterest.getAllAttributes().size());

        Attribute invoker = eventOfInterest.findAttributeByName("invoker");
        Assert.assertNotNull(invoker);
        Assert.assertEquals("Object", invoker.getType());
        Assert.assertEquals(VisibilityKind.PRIVATE_LITERAL.toString(), invoker.getVisibility());

        Attribute action = eventOfInterest.findAttributeByName("action");
        Assert.assertNotNull(action);
        Assert.assertEquals("String", action.getType());
        Assert.assertEquals(VisibilityKind.PRIVATE_LITERAL.toString(), action.getVisibility());

        Attribute parameters = eventOfInterest.findAttributeByName("parameters");
        Assert.assertNotNull(parameters);
        Assert.assertEquals("Object", parameters.getType());
        Assert.assertEquals(VisibilityKind.PRIVATE_LITERAL.toString(), parameters.getVisibility());
    }

    @Test
    public void testGetOrCreateMediatorInterface() throws Exception {
        Architecture architecture = ArchitectureRepository.getArchitecture(ArchitectureRepository.OTHER_MODELS[7]);
        List<Element> elements = architecture.getElements();

        Interface mediatorInterface = MediatorUtil.getOrCreateMediatorInterface(elements, new Concern("action"), MediatorUtil.getOrCreateEventOfInterestClass());
        Assert.assertNotNull(mediatorInterface);

        Method actionEvent = mediatorInterface.getOperations().iterator().next();
        Assert.assertNotNull(actionEvent);
        Assert.assertEquals("actionEvent", actionEvent.getName());
        Assert.assertEquals("void", actionEvent.getReturnType());
        Assert.assertEquals(1, actionEvent.getParameters().size());
    }

    @Test
    public void testGetOrCreateMediatorClass() throws Exception {
        Architecture architecture = ArchitectureRepository.getArchitecture(ArchitectureRepository.OTHER_MODELS[7]);
        List<Element> elements = architecture.getElements();

        Interface mediatorInterface = MediatorUtil.getOrCreateMediatorInterface(elements, new Concern("action"), MediatorUtil.getOrCreateEventOfInterestClass());
        Assert.assertNotNull(mediatorInterface);

        Class mediatorClass = MediatorUtil.getOrCreateMediatorClass(elements, new Concern("action"), mediatorInterface);
        Assert.assertNotNull(mediatorClass);

        Assert.assertTrue(ElementUtil.isTypeOf(mediatorClass, mediatorInterface));
        Assert.assertTrue(ElementUtil.getAllSubElements(mediatorInterface).contains(mediatorClass));

        Method actionEvent = mediatorClass.findMethodByName("actionEvent");
        Assert.assertNotNull(actionEvent);
        Assert.assertEquals("void", actionEvent.getReturnType());
        Assert.assertEquals(1, actionEvent.getParameters().size());
    }

    @Test
    public void testGetOrCreateColleagueInterface() throws Exception {
        Architecture architecture = ArchitectureRepository.getArchitecture(ArchitectureRepository.OTHER_MODELS[7]);
        List<Element> elements = architecture.getElements();

        Class eventOfInterest = MediatorUtil.getOrCreateEventOfInterestClass();
        Assert.assertNotNull(eventOfInterest);

        Interface mediatorInterface = MediatorUtil.getOrCreateMediatorInterface(elements, new Concern("action"), eventOfInterest);
        Assert.assertNotNull(mediatorInterface);

        Class mediatorClass = MediatorUtil.getOrCreateMediatorClass(elements, new Concern("action"), mediatorInterface);
        Assert.assertNotNull(mediatorClass);

        Interface colleagueInterface = MediatorUtil.getOrCreateColleagueInterface(elements, new Concern("action"), mediatorInterface, eventOfInterest);
        Assert.assertNotNull(colleagueInterface);

        Assert.assertEquals(2, colleagueInterface.getOperations().size());

        List<Element> usedElements = new ArrayList<>();
        usedElements.add(eventOfInterest);
        usedElements.add(mediatorInterface);

        Assert.assertTrue(usedElements.contains(RelationshipUtil.getUsedElementFromRelationship(ElementUtil.getRelationships(colleagueInterface).get(0))));
        Assert.assertTrue(usedElements.contains(RelationshipUtil.getUsedElementFromRelationship(ElementUtil.getRelationships(colleagueInterface).get(1))));
    }

    @Test
    public void testRemoveRelationships() {
        Architecture architecture = ArchitectureRepository.getArchitecture(ArchitectureRepository.OTHER_MODELS[7]);
        List<Element> elements = architecture.getElements();

        MediatorUtil.removeRelationships(elements, new Concern("action"));

        for (Element element : elements) {
            for (Relationship relationship : ElementUtil.getRelationships(element)) {
                Assert.assertNotSame("UsageRelationship", relationship.getType());
                Assert.assertNotSame("DependencyRelationship", relationship.getType());
            }
        }
    }

}
