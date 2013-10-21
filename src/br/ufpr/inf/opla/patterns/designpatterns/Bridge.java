package br.ufpr.inf.opla.patterns.designpatterns;

import br.ufpr.inf.opla.patterns.models.DesignPattern;
import br.ufpr.inf.opla.patterns.models.Scope;

public class Bridge extends DesignPattern {

    private static final Bridge INSTANCE = new Bridge();

    private Bridge() {
        super("Bridge", "Structural");
    }

    public static Bridge getInstance() {
        return INSTANCE;
    }

    @Override
    protected boolean verifyPS(Scope scope) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    protected boolean verifyPSPLA(Scope scope) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean apply(Scope scope) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

 }
