/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ufpr.inf.opla.patterns;

import arquitetura.representation.Architecture;
import br.ufpr.inf.opla.patterns.main.NSGAII_OPLA;
import br.ufpr.inf.opla.patterns.repositories.ArchitectureRepository;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import jmetal.util.JMException;

/**
 *
 * @author giovaniguizzo
 */
public class TesteSimples {

    public static void main(String[] args) {
        Architecture architecture = ArchitectureRepository.getArchitecture(ArchitectureRepository.MOBILE_MEDIA);

        try {
            NSGAII_OPLA.main(new String[]{"1", "1", "1", ArchitectureRepository.MOBILE_MEDIA, "Teste", "PLAMutation", "false"});
        } catch (IOException ex) {
            Logger.getLogger(TesteSimples.class.getName()).log(Level.SEVERE, null, ex);
        } catch (JMException ex) {
            Logger.getLogger(TesteSimples.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(TesteSimples.class.getName()).log(Level.SEVERE, null, ex);
        }

//        GenerateArchitecture generateArchitecture = new GenerateArchitecture();
//        generateArchitecture.generate(architecture, ArchitectureRepository.OUTPUT[7]);
    }

}
