/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ufpr.inf.opla.patterns.util;

import arquitetura.representation.Architecture;
import arquitetura.representation.Class;
import arquitetura.representation.Element;
import arquitetura.representation.Interface;
import br.ufpr.inf.opla.patterns.repositories.ArchitectureRepository;
import java.util.ArrayList;
import java.util.List;
import org.junit.Assert;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 *
 * @author giovaniguizzo
 */
public class AdapterUtilTest {

    public AdapterUtilTest() {
    }

    @Test
    public void testGetAdapterClass() {
        String model = ArchitectureRepository.ADAPTER_MODELS[0];
        Architecture architecture = ArchitectureRepository.getArchitecture(model);

        //Interface to Interface
        Element target = architecture.findElementByName("TargetII");
        Element adaptee = architecture.findElementByName("AdapteeII");
        Class adapter = architecture.findClassByName("AdapterII").get(0);
        Class result = AdapterUtil.getAdapterClass(target, adaptee);
        assertEquals(adapter, result);

        //Interface to Class
        target = architecture.findElementByName("TargetIC");
        adaptee = architecture.findElementByName("AdapteeIC");
        adapter = architecture.findClassByName("AdapterIC").get(0);
        result = AdapterUtil.getAdapterClass(target, adaptee);
        assertEquals(adapter, result);

        //Class to Class
        target = architecture.findElementByName("TargetCC");
        adaptee = architecture.findElementByName("AdapteeCC");
        adapter = architecture.findClassByName("AdapterCC").get(0);
        result = AdapterUtil.getAdapterClass(target, adaptee);
        assertEquals(adapter, result);

        //Class to Interface
        target = architecture.findElementByName("TargetCI");
        adaptee = architecture.findElementByName("AdapteeCI");
        adapter = architecture.findClassByName("AdapterCI").get(0);
        result = AdapterUtil.getAdapterClass(target, adaptee);
        assertEquals(adapter, result);
    }

    @Test
    public void testGetAllTargetInterfaces() {
        String model = ArchitectureRepository.OTHER_MODELS[6];
        Architecture architecture = ArchitectureRepository.getArchitecture(model);

        Interface adaptee1 = architecture.findInterfaceByName("Adaptee1");
        Interface adaptee2 = architecture.findInterfaceByName("Adaptee2");
        Interface adaptee3 = architecture.findInterfaceByName("Adaptee3");

        Interface target1 = architecture.findInterfaceByName("Target1");
        Interface target2 = architecture.findInterfaceByName("Target2");
        
        List<Interface> allTargetInterfaces = AdapterUtil.getAllTargetInterfaces(adaptee3);
        
        Assert.assertEquals(2, allTargetInterfaces.size());
        Assert.assertTrue(allTargetInterfaces.contains(target1));
        Assert.assertTrue(allTargetInterfaces.contains(target2));

        allTargetInterfaces = AdapterUtil.getAllTargetInterfaces(adaptee2);
        Assert.assertEquals(2, allTargetInterfaces.size());
        Assert.assertTrue(allTargetInterfaces.contains(target1));
        Assert.assertTrue(allTargetInterfaces.contains(target2));

        allTargetInterfaces = AdapterUtil.getAllTargetInterfaces(adaptee1);
        Assert.assertEquals(2, allTargetInterfaces.size());
        Assert.assertTrue(allTargetInterfaces.contains(target1));
        Assert.assertTrue(allTargetInterfaces.contains(target2));

        ArrayList<Element> elements = new ArrayList<>();
        elements.add(adaptee2);
        elements.add(adaptee3);
        elements.add(adaptee1);
        List<Interface> allCommonInterfaces = ElementUtil.getAllCommonInterfaces(elements);
        assertEquals(2, allCommonInterfaces.size());
        Assert.assertTrue(allCommonInterfaces.contains(target1));
        Assert.assertTrue(allCommonInterfaces.contains(target2));
    }
}
