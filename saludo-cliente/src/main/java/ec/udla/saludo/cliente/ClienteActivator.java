package ec.udla.saludo.cliente;

import ec.udla.saludo.api.SaludoService;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceEvent;
import org.osgi.framework.ServiceListener;
import org.osgi.framework.ServiceReference;

public class ClienteActivator implements BundleActivator, ServiceListener {

    private BundleContext context;

    @Override
    public void start(BundleContext context) throws Exception {
        this.context = context;

        String filtro = "(objectClass=" + SaludoService.class.getName() + ")";
        context.addServiceListener(this, filtro);

        System.out.println("[saludo-cliente] Bundle B iniciado.");

        ServiceReference<SaludoService> referencia =
                context.getServiceReference(SaludoService.class);

        if (referencia != null) {
            mostrarSaludo(referencia);
        } else {
            System.out.println("[saludo-cliente] Esperando servicio de saludos...");
        }
    }

    @Override
    public void serviceChanged(ServiceEvent evento) {

        if (evento.getType() == ServiceEvent.REGISTERED) {
            System.out.println("[saludo-cliente] Servicio disponible nuevamente.");

            ServiceReference<SaludoService> referencia =
                    context.getServiceReference(SaludoService.class);

            if (referencia != null) {
                mostrarSaludo(referencia);
            }
        }

        if (evento.getType() == ServiceEvent.UNREGISTERING) {
            System.out.println("[saludo-cliente] Servicio no disponible.");
            System.out.println("[saludo-cliente] El Bundle B sigue activo.");
        }
    }

    private void mostrarSaludo(ServiceReference<SaludoService> referencia) {
        SaludoService servicio = context.getService(referencia);

        if (servicio != null) {
            try {
                System.out.println(
                    "[saludo-cliente] " + servicio.saludar("Samuel")
                );
            } finally {
                context.ungetService(referencia);
            }
        }
    }

    @Override
    public void stop(BundleContext context) {
        context.removeServiceListener(this);
        this.context = null;

        System.out.println("[saludo-cliente] Bundle B detenido.");
    }
}
