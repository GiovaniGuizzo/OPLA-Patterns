/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ufpr.inf.opla.patterns.util;

import arquitetura.representation.Architecture;
import arquitetura.representation.Class;
import arquitetura.representation.Element;
import br.ufpr.inf.opla.patterns.repositories.ArchitectureRepository;
import org.junit.Test;
import static org.junit.Assert.*;

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
}
