/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ufpr.inf.opla.patterns.factory;

import br.ufpr.inf.opla.patterns.operator.DesignPatternsAndPLAMutationOperator;
import br.ufpr.inf.opla.patterns.operator.DesignPatternsMutationOperator;
import java.util.HashMap;
import jmetal.operators.mutation.Mutation;
import jmetal.operators.mutation.PLAFeatureMutation;

/**
 *
 * @author giovaniguizzo
 */
public class MutationOperatorFactory {

    public static Mutation create(String operator, HashMap<String, Object> parameters) {
        switch (operator) {
            case "DesignPatternsMutationOperator":
                return new DesignPatternsMutationOperator(parameters, null, null);
            case "DesignPatternsAndPLAMutationOperator":
                return new DesignPatternsAndPLAMutationOperator(parameters, null, null);
            case "PLAMutation":
                return new PLAFeatureMutation(parameters);
            default:
                return null;
        }
    }

}
