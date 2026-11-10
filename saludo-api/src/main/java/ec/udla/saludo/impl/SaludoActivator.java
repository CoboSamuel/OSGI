package ec.udla.saludo.impl;

import ec.udla.saludo.api.SaludoService;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;

public class SaludoActivator implements BundleActivator {

    private ServiceRegistration<SaludoService> registro;

    @Override
    public void start(BundleContext context) {
        SaludoService servicio = new SaludoServiceImpl();

        registro = context.registerService(
            SaludoService.class,
            servicio,
            null
        );

        System.out.println("[saludo-api] Servicio de saludos registrado.");
    }

    @Override
    public void stop(BundleContext context) {
        if (registro != null) {
            registro.unregister();
            registro = null;
        }

        System.out.println("[saludo-api] Servicio de saludos detenido.");
    }
}
