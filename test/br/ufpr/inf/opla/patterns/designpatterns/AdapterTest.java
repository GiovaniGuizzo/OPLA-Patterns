/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ufpr.inf.opla.patterns.designpatterns;

import arquitetura.exceptions.ClassNotFound;
import arquitetura.exceptions.InterfaceNotFound;
import arquitetura.representation.Architecture;
import arquitetura.representation.Class;
import arquitetura.representation.Interface;
import arquitetura.representation.relationship.GeneralizationRelationship;
import arquitetura.representation.relationship.RealizationRelationship;
import arquitetura.representation.relationship.UsageRelationship;
import br.ufpr.inf.opla.patterns.repositories.ArchitectureRepositoryFlyweight;
import java.util.logging.Level;
import java.util.logging.Logger;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import org.junit.Test;

/**
 *
 * @author giovaniguizzo
 */
public class AdapterTest {

    private final ArchitectureRepositoryFlyweight architectureRepository;
    private final Adapter adapter;

    public AdapterTest() {
        this.adapter = Adapter.getInstance();
        this.architectureRepository = ArchitectureRepositoryFlyweight.getInstance();
    }

    /**
     * Test of applyAdapter method, of class Adapter.
     */
    @Test
    public void testApplyAdapter() {
        String model = ArchitectureRepositoryFlyweight.STRATEGY_MODELS[4];
        architectureRepository.clearArchitecture(model);
        Architecture architecture = architectureRepository.getArchitecture(model);

        Interface target = null;
        try {
            target = architecture.findInterfaceByName("SomeInterface");
        } catch (InterfaceNotFound ex) {
            Logger.getLogger(AdapterTest.class.getName()).log(Level.SEVERE, null, ex);
        }
        assertEquals("SomeInterface", target.getName());

        Interface adaptee = null;
        try {
            adaptee = architecture.findInterfaceByName("Sort2");
        } catch (InterfaceNotFound ex) {
            Logger.getLogger(AdapterTest.class.getName()).log(Level.SEVERE, null, ex);
        }
        assertEquals("Sort2", adaptee.getName());

        Class adapterClass = adapter.applyAdapter(target, adaptee);
        assertNotNull(adapterClass);
        assertEquals("Sort2Adapter", adapterClass.getName());
        assertEquals(3, adapterClass.getAllMethods().size());
        assertEquals(2, adapterClass.getOwnConcerns().size());

        RealizationRelationship realization = (RealizationRelationship) adapterClass.getRelationships().get(0);
        assertEquals(target, realization.getSupplier());

        UsageRelationship usage = (UsageRelationship) adapterClass.getRelationships().get(1);
        assertEquals(adaptee, usage.getSupplier());

//        GenerateArchitecture generateArchitecture = new GenerateArchitecture();
//        generateArchitecture.generate(architecture, ArchitectureRepositoryFlyweight.OUTPUT[2]);
//        architectureRepository.clearArchitecture(model);
    }

    @Test
    public void testApplyAdapterHavingClassAsTarget() {
        String model = ArchitectureRepositoryFlyweight.STRATEGY_MODELS[4];
        Architecture architecture = architectureRepository.getArchitecture(model);

        Class target = null;
        try {
            target = architecture.findClassByName("Sort1").get(0);
        } catch (ClassNotFound ex) {
            Logger.getLogger(AdapterTest.class.getName()).log(Level.SEVERE, null, ex);
        }
        assertEquals("Sort1", target.getName());

        Interface adaptee = null;
        try {
            adaptee = architecture.findInterfaceByName("Sort2");
        } catch (InterfaceNotFound ex) {
            Logger.getLogger(AdapterTest.class.getName()).log(Level.SEVERE, null, ex);
        }
        assertEquals("Sort2", adaptee.getName());

        Class adapterClass = adapter.applyAdapter(target, adaptee);
        assertNotNull(adapterClass);
        assertEquals("Sort2Adapter", adapterClass.getName());
        assertEquals(1, adapterClass.getAllMethods().size());
        assertEquals(2, adapterClass.getOwnConcerns().size());

        GeneralizationRelationship generalization = (GeneralizationRelationship) adapterClass.getRelationships().get(0);
        assertEquals(target, generalization.getParent());

        UsageRelationship usage = (UsageRelationship) adapterClass.getRelationships().get(1);
        assertEquals(adaptee, usage.getSupplier());

//        GenerateArchitecture generateArchitecture = new GenerateArchitecture();
//        generateArchitecture.generate(architecture, ArchitectureRepositoryFlyweight.OUTPUT[2]);
//        architectureRepository.clearArchitecture(model);
    }

}
