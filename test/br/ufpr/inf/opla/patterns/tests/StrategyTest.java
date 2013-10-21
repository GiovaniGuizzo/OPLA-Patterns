package br.ufpr.inf.opla.patterns.tests;

import arquitetura.exceptions.VariationPointElementTypeErrorException;
import arquitetura.representation.Architecture;
import arquitetura.representation.Variability;
import arquitetura.representation.Variant;
import arquitetura.representation.VariationPoint;
import arquitetura.representation.relationship.UsageRelationship;
import br.ufpr.inf.opla.patterns.designpatterns.Strategy;
import br.ufpr.inf.opla.patterns.models.Scope;
import br.ufpr.inf.opla.patterns.models.ps.impl.PSStrategy;
import java.util.ArrayList;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.Test;
import org.junit.Assert;

public class StrategyTest {

    @Test
    public void verify() {
        try {
            Architecture architecture = new Architecture("Teste");
            
            Scope scope = new Scope();
            
            arquitetura.representation.Class bubbleSort = new arquitetura.representation.Class(architecture, "BubbleSort", UUID.randomUUID().toString());
            scope.getElements().add(bubbleSort);
            
            arquitetura.representation.Class quickSort = new arquitetura.representation.Class(architecture, "QuickSort", UUID.randomUUID().toString());
            scope.getElements().add(quickSort);
            
            arquitetura.representation.Class context = new arquitetura.representation.Class(architecture, "Context", UUID.randomUUID().toString());
            scope.getElements().add(context);
            
            UsageRelationship usageRelationship = new UsageRelationship("sortStrategy", bubbleSort, context, UUID.randomUUID().toString());
            bubbleSort.getRelationships().add(usageRelationship);
            context.getRelationships().add(usageRelationship);
            
            usageRelationship = new UsageRelationship("sortStrategy", quickSort, context, UUID.randomUUID().toString());
            quickSort.getRelationships().add(usageRelationship);
            context.getRelationships().add(usageRelationship);
            
            boolean verifyPS = Strategy.getInstance().verifyPS(scope);
            
            Assert.assertTrue(verifyPS);
            
            Assert.assertEquals("Sort", ((PSStrategy)scope.getPs().get(0)).getAlgorithmFamily().getFamilyName());
            
            Variability variability = new Variability("variability1", "1", "1", "DESIGN", true, context.getId(), context.getId());
            
            VariationPoint variationPoint = new VariationPoint(context, new ArrayList<Variant>(), "DESIGN");
            variationPoint.getVariabilities().add(variability);
            variability.addVariationPoint(variationPoint);
            variationPoint.replaceVariationPointElement(context);
            context.setVariationPoint(variationPoint);
            
            Variant variant1 = new Variant();
            variant1.getVariabilities().add(variability);
            variant1.getVariationPoints().add(variationPoint);
            variability.getVariants().add(variant1);
            variationPoint.getVariants().add(variant1);
            variant1.setVariantElement(bubbleSort);
            bubbleSort.setVariant(variant1);
            
            Variant variant2 = new Variant();
            variant2.getVariabilities().add(variability);
            variant2.getVariationPoints().add(variationPoint);
            variability.getVariants().add(variant2);
            variationPoint.getVariants().add(variant2);
            variant1.setVariantElement(quickSort);
            quickSort.setVariant(variant2);
            
            boolean verifyPSPLA = Strategy.getInstance().verifyPSPLA(scope);
            Assert.assertTrue(verifyPSPLA);
            
        } catch (VariationPointElementTypeErrorException ex) {
            Logger.getLogger(StrategyTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
