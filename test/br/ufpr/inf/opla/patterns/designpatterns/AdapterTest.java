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
import br.ufpr.inf.opla.patterns.repositories.ArchitectureRepository;
import br.ufpr.inf.opla.patterns.util.ElementUtil;
import java.util.ArrayList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
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

        RealizationRelationship realization;
        RealizationRelationship realization2;

        realization = (RealizationRelationship) ElementUtil.getRelationships(adapterClass).get(0);
        realization2 = (RealizationRelationship) ElementUtil.getRelationships(adapterClass).get(1);

        ArrayList<Element> suppliers = new ArrayList<>();
        suppliers.add(realization.getSupplier());
        suppliers.add(realization2.getSupplier());

        assertTrue(suppliers.contains(adaptee));
        assertTrue(suppliers.contains(target));
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

        RealizationRelationship realization;
        GeneralizationRelationship generalization2;

        if (ElementUtil.getRelationships(adapterClass).get(0) instanceof RealizationRelationship) {
            realization = (RealizationRelationship) ElementUtil.getRelationships(adapterClass).get(0);
            generalization2 = (GeneralizationRelationship) ElementUtil.getRelationships(adapterClass).get(1);
        } else {
            realization = (RealizationRelationship) ElementUtil.getRelationships(adapterClass).get(1);
            generalization2 = (GeneralizationRelationship) ElementUtil.getRelationships(adapterClass).get(0);
        }

        assertEquals(target, generalization2.getParent());
        assertEquals(adaptee, realization.getSupplier());
    }

}
