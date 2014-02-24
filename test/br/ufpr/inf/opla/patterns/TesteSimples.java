/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package br.ufpr.inf.opla.patterns;

import arquitetura.representation.Architecture;
import br.ufpr.inf.opla.patterns.repositories.ArchitectureRepository;
import main.GenerateArchitecture;

/**
 *
 * @author giovaniguizzo
 */
public class TesteSimples {
    
    public static void main(String[] args){
        Architecture architecture = ArchitectureRepository.getArchitecture(ArchitectureRepository.SERVICE_AND_SUPPORT_SYSTEM);
        
        GenerateArchitecture generateArchitecture = new GenerateArchitecture();
        generateArchitecture.generate(architecture, ArchitectureRepository.OUTPUT[7]);
    }
    
}
