package ec.udla.inventario.impl;

import ec.udla.inventario.api.InventarioService;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;

public class InventarioActivator implements BundleActivator {

    private ServiceRegistration<InventarioService> registro;

    @Override
    public void start(BundleContext context) {

        InventarioService servicio = new InventarioServiceImpl();

        registro = context.registerService(
            InventarioService.class,
            servicio,
            null
        );

        System.out.println("[inventario] Bundle de inventario iniciado.");
        System.out.println("[inventario] Stock inicial:");
        System.out.println("[inventario] MTB-001: 5 unidades");
        System.out.println("[inventario] RUTA-002: 12 unidades");
    }

    @Override
    public void stop(BundleContext context) {

        if (registro != null) {
            registro.unregister();
            registro = null;
        }

        System.out.println("[inventario] Servicio de inventario detenido.");
    }
}
