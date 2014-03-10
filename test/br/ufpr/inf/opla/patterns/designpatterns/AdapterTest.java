/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ufpr.inf.opla.patterns.designpatterns;

import arquitetura.representation.Architecture;
import arquitetura.representation.Class;
import arquitetura.representation.Element;
import arquitetura.representation.Interface;
import arquitetura.representation.relationship.GeneralizationRelationship;
import arquitetura.representation.relationship.RealizationRelationship;
import arquitetura.representation.relationship.Relationship;
import arquitetura.representation.relationship.UsageRelationship;
import br.ufpr.inf.opla.patterns.repositories.ArchitectureRepository;
import br.ufpr.inf.opla.patterns.util.ElementUtil;
import java.util.List;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import org.junit.Test;

/**
 *
 * @author giovaniguizzo
 */
public class AdapterTest {

    private final Adapter adapter;

    public AdapterTest() {
        this.adapter = Adapter.getInstance();
    }

    /**
     * Test of applyAdapter method, of class Adapter.
     */
    @Test
    public void testApplyAdapter() {
        String model = ArchitectureRepository.STRATEGY_MODELS[4];
        Architecture architecture = ArchitectureRepository.getArchitecture(model);

        Interface target = null;
        target = architecture.findInterfaceByName("SomeInterface");
        assertEquals("SomeInterface", target.getName());

        Interface adaptee = null;
        adaptee = architecture.findInterfaceByName("Sort2");
        assertEquals("Sort2", adaptee.getName());

        Class adapterClass = adapter.applyAdapter(target, adaptee);
        assertNotNull(adapterClass);
        assertEquals("Sort2Adapter", adapterClass.getName());
        assertEquals(3, adapterClass.getAllMethods().size());
        assertEquals(2, adapterClass.getOwnConcerns().size());

        UsageRelationship usage;
        RealizationRelationship realization;

        final List<Relationship> relationships = ElementUtil.getRelationships(adapterClass);

        if (relationships.get(0) instanceof UsageRelationship) {
            usage = (UsageRelationship) relationships.get(0);
            realization = (RealizationRelationship) relationships.get(1);
        } else {
            usage = (UsageRelationship) relationships.get(1);
            realization = (RealizationRelationship) relationships.get(0);
        }

        assertEquals(adaptee, usage.getSupplier());
        assertEquals(target, realization.getSupplier());
    }

    @Test
    public void testApplyAdapterHavingClassAsTarget() {
        String model = ArchitectureRepository.STRATEGY_MODELS[4];
        Architecture architecture = ArchitectureRepository.getArchitecture(model);

        Class target = architecture.findClassByName("Sort1").get(0);
        assertEquals("Sort1", target.getName());

        Interface adaptee = architecture.findInterfaceByName("Sort2");
        assertEquals("Sort2", adaptee.getName());

        Class adapterClass = adapter.applyAdapter(target, adaptee);
        assertNotNull(adapterClass);
        assertEquals("Sort2Adapter", adapterClass.getName());
        assertEquals(2, adapterClass.getAllMethods().size());
        assertEquals(2, adapterClass.getOwnConcerns().size());

        UsageRelationship usage;
        GeneralizationRelationship generalization;

        final List<Relationship> relationships = ElementUtil.getRelationships(adapterClass);

        if (relationships.get(0) instanceof UsageRelationship) {
            usage = (UsageRelationship) relationships.get(0);
            generalization = (GeneralizationRelationship) relationships.get(1);
        } else {
            usage = (UsageRelationship) relationships.get(1);
            generalization = (GeneralizationRelationship) relationships.get(0);
        }

        assertEquals(adaptee, usage.getSupplier());
        assertEquals(target, generalization.getParent());
    }

    @Test
    public void testGetAdapterClass() {
        String model = ArchitectureRepository.ADAPTER_MODELS[0];
        Architecture architecture = ArchitectureRepository.getArchitecture(model);

        //Interface to Interface
        Element target = architecture.findElementByName("TargetII");
        Element adaptee = architecture.findElementByName("AdapteeII");
        Class localAdapter = architecture.findClassByName("AdapterII").get(0);
        Class result = adapter.applyAdapter(target, adaptee);
        assertEquals(localAdapter, result);
        assertEquals(2, localAdapter.getAllMethods().size());
    }

    @Test
    public void testApplyAdapter2() {
        String model = ArchitectureRepository.OTHER_MODELS[6];
        Architecture architecture = ArchitectureRepository.getArchitecture(model);

        Interface target = architecture.findInterfaceByName("Target1");
        assertEquals("Target1", target.getName());

        Interface adaptee = architecture.findInterfaceByName("Adaptee1");
        assertEquals("Adaptee1", adaptee.getName());

        Class adapterClass = adapter.applyAdapter(target, adaptee);
        assertNotNull(adapterClass);
        assertEquals("Adapter1", adapterClass.getName());
        assertEquals(2, adapterClass.getAllMethods().size());

        UsageRelationship usage;
        RealizationRelationship generalization;

        final List<Relationship> relationships = ElementUtil.getRelationships(adapterClass);

        if (relationships.get(0) instanceof UsageRelationship) {
            usage = (UsageRelationship) relationships.get(0);
            generalization = (RealizationRelationship) relationships.get(1);
        } else {
            usage = (UsageRelationship) relationships.get(1);
            generalization = (RealizationRelationship) relationships.get(0);
        }

        assertEquals(usage.getSupplier(), architecture.findInterfaceByName("Adaptee1"));
        assertEquals(generalization.getSupplier(), architecture.findInterfaceByName("Target2"));
    }

}
