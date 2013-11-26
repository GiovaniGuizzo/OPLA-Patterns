/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ufpr.inf.opla.patterns.util;

import arquitetura.exceptions.ClassNotFound;
import arquitetura.exceptions.InterfaceNotFound;
import arquitetura.representation.Architecture;
import arquitetura.representation.Element;
import arquitetura.representation.Interface;
import arquitetura.representation.relationship.GeneralizationRelationship;
import arquitetura.representation.relationship.RealizationRelationship;
import arquitetura.representation.relationship.Relationship;
import arquitetura.representation.relationship.UsageRelationship;
import br.ufpr.inf.opla.patterns.repositories.ArchitectureRepository;
import java.util.logging.Level;
import java.util.logging.Logger;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

/**
 *
 * @author giovaniguizzo
 */
public class RelationshipUtilTest {

    private final ArchitectureRepository architectureRepository;

    public RelationshipUtilTest() {
        this.architectureRepository = ArchitectureRepository.getInstance();
    }

    /**
     * Test of getUsedElementFromRelationship method, of class RelationshipUtil.
     */
    @Test
    public void testGetUsedElementFromRelationship() {
        Architecture architecture = architectureRepository.getArchitecture(ArchitectureRepository.STRATEGY_MODELS[0]);
        Element element = null;
        try {
            element = architecture.findClassByName("NotAContext").get(0);
        } catch (ClassNotFound ex) {
            Logger.getLogger(MethodUtilTest.class.getName()).log(Level.SEVERE, null, ex);
        }
        assertEquals("NotAContext", element.getName());
        Relationship relationship = element.getRelationships().get(0);
        assertEquals("Usage1", relationship.getName());
        Element result = RelationshipUtil.getUsedElementFromRelationship(relationship);
        assertEquals("NoAlgorithm", result.getName());
    }

    /**
     * Test of getImplementedInterface method, of class RelationshipUtil.
     */
    @Test
    public void testGetImplementedInterface() {
        Architecture architecture = architectureRepository.getArchitecture(ArchitectureRepository.STRATEGY_MODELS[2]);
        Element element = null;
        try {
            element = architecture.findClassByName("Class1").get(0);
        } catch (ClassNotFound ex) {
            Logger.getLogger(MethodUtilTest.class.getName()).log(Level.SEVERE, null, ex);
        }
        assertEquals("Class1", element.getName());
        Relationship relationship = element.getRelationships().get(0);
        assertEquals("Realization2", relationship.getName());
        Interface result = RelationshipUtil.getImplementedInterface(relationship);
        assertEquals("StrategyInterface", result.getName());
    }

    /**
     * Test of getExtendedElement method, of class RelationshipUtil.
     */
    @Test
    public void testGetExtendedElement() {
        Architecture architecture = architectureRepository.getArchitecture(ArchitectureRepository.OTHER_MODELS[0]);
        Element element = null;
        try {
            element = architecture.findClassByName("Class1").get(0);
        } catch (ClassNotFound ex) {
            Logger.getLogger(MethodUtilTest.class.getName()).log(Level.SEVERE, null, ex);
        }
        assertEquals("Class1", element.getName());
        Relationship relationship = element.getRelationships().get(0);
        assertEquals("generalization", relationship.getType());
        Element result = RelationshipUtil.getExtendedElement(relationship);
        assertEquals("Class2", result.getName());
    }

    /**
     * Test of moveRelationship method, of class RelationshipUtil.
     */
    @Test
    public void testMoveRelationship() {
        Architecture architecture = architectureRepository.getArchitecture(ArchitectureRepository.OTHER_MODELS[0]);
        Element element = null;
        try {
            element = architecture.findClassByName("Class1").get(0);
        } catch (ClassNotFound ex) {
            Logger.getLogger(MethodUtilTest.class.getName()).log(Level.SEVERE, null, ex);
        }
        Element element1 = null;
        try {
            element1 = architecture.findClassByName("Class2").get(0);
        } catch (ClassNotFound ex) {
            Logger.getLogger(MethodUtilTest.class.getName()).log(Level.SEVERE, null, ex);
        }
        Element element2 = null;
        try {
            element2 = architecture.findInterfaceByName("Class3");
        } catch (InterfaceNotFound ex) {
            Logger.getLogger(RelationshipUtilTest.class.getName()).log(Level.SEVERE, null, ex);
        }
        assertEquals("Class1", element.getName());
        assertEquals("Class2", element1.getName());
        assertEquals("Class3", element2.getName());
        Relationship relationship = element.getRelationships().get(1);
        assertEquals("Usage1", relationship.getName());
        RelationshipUtil.moveRelationship(relationship, element, element1);
        assertEquals("Class2", RelationshipUtil.getUsedElementFromRelationship(relationship).getName());
        assertTrue(element1.getRelationships().contains(relationship));
        assertTrue(element.getRelationships().contains(relationship));
        assertFalse(element2.getRelationships().contains(relationship));
    }

    /**
     * Test of createNewRealizationRelationship method, of class RelationshipUtil.
     */
    @Test
    public void testCreateNewRealizationRelationship() {
        Architecture architecture = architectureRepository.getArchitecture(ArchitectureRepository.OTHER_MODELS[0]);
        Element element = null;
        try {
            element = architecture.findClassByName("Class1").get(0);
        } catch (ClassNotFound ex) {
            Logger.getLogger(MethodUtilTest.class.getName()).log(Level.SEVERE, null, ex);
        }
        Element element2 = null;
        try {
            element2 = architecture.findInterfaceByName("Class3");
        } catch (InterfaceNotFound ex) {
            Logger.getLogger(RelationshipUtilTest.class.getName()).log(Level.SEVERE, null, ex);
        }
        assertEquals("Class1", element.getName());
        assertEquals("Class3", element2.getName());
        RealizationRelationship result = RelationshipUtil.createNewRealizationRelationship("RealizationVai", element, element2);
        assertEquals("Class1", result.getClient().getName());
        assertEquals("Class3", result.getSupplier().getName());
        assertTrue(element.getRelationships().contains(result));
        assertTrue(element2.getRelationships().contains(result));
    }

    /**
     * Test of createNewGeneralizationRelationship method, of class RelationshipUtil.
     */
    @Test
    public void testCreateNewGeneralizationRelationship() {
        Architecture architecture = architectureRepository.getArchitecture(ArchitectureRepository.OTHER_MODELS[0]);
        Element element = null;
        try {
            element = architecture.findClassByName("Class1").get(0);
        } catch (ClassNotFound ex) {
            Logger.getLogger(MethodUtilTest.class.getName()).log(Level.SEVERE, null, ex);
        }
        Element element2 = null;
        try {
            element2 = architecture.findClassByName("Class2").get(0);
        } catch (ClassNotFound ex) {
            Logger.getLogger(MethodUtilTest.class.getName()).log(Level.SEVERE, null, ex);
        }
        assertEquals("Class1", element.getName());
        assertEquals("Class2", element2.getName());
        GeneralizationRelationship result = RelationshipUtil.createNewGeneralizationRelationship("GeneralizationVai", element2, element);
        assertEquals("Class2", result.getChild().getName());
        assertEquals("Class1", result.getParent().getName());
        assertTrue(element.getRelationships().contains(result));
        assertTrue(element2.getRelationships().contains(result));
    }

    /**
     * Test of createNewUsageRelationship method, of class RelationshipUtil.
     */
    @Test
    public void testCreateNewUsageRelationship() {
        Architecture architecture = architectureRepository.getArchitecture(ArchitectureRepository.OTHER_MODELS[0]);
        Element element = null;
        try {
            element = architecture.findClassByName("Class1").get(0);
        } catch (ClassNotFound ex) {
            Logger.getLogger(MethodUtilTest.class.getName()).log(Level.SEVERE, null, ex);
        }
        Element element2 = null;
        try {
            element2 = architecture.findClassByName("Class2").get(0);
        } catch (ClassNotFound ex) {
            Logger.getLogger(MethodUtilTest.class.getName()).log(Level.SEVERE, null, ex);
        }
        assertEquals("Class1", element.getName());
        assertEquals("Class2", element2.getName());
        UsageRelationship result = RelationshipUtil.createNewUsageRelationship("GeneralizationVai", element2, element);
        assertEquals("Class2", result.getClient().getName());
        assertEquals("Class1", result.getSupplier().getName());
        assertTrue(element.getRelationships().contains(result));
        assertTrue(element2.getRelationships().contains(result));
    }

}
