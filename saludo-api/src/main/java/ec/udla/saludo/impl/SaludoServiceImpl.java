package ec.udla.saludo.impl;

import ec.udla.saludo.api.SaludoService;

public class SaludoServiceImpl implements SaludoService {

    @Override
    public String saludar(String nombre) {
        return "Hola, " + nombre + "! Bienvenido al sistema OSGi.";
    }

}
