package br.ufpr.inf.opla.patterns.models.ps;

import arquitetura.representation.Element;
import br.ufpr.inf.opla.patterns.models.DesignPattern;
import java.util.List;

public interface PS {

    public DesignPattern getPsOf();

    public List<Element> getParticipants();

}
