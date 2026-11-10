package ec.udla.alertas.cliente;

import ec.udla.inventario.api.InventarioService;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceEvent;
import org.osgi.framework.ServiceListener;
import org.osgi.framework.ServiceReference;

public class AlertasActivator implements BundleActivator, ServiceListener {

    private BundleContext context;

    private static final String BICICLETA = "MTB-001";
    private static final int STOCK_MINIMO = 3;

    @Override
    public void start(BundleContext context) throws Exception {
        this.context = context;

        String filtro = "(objectClass=" +
                InventarioService.class.getName() + ")";

        context.addServiceListener(this, filtro);

        System.out.println("[alertas] Bundle de alertas iniciado.");

        ServiceReference<InventarioService> referencia =
                context.getServiceReference(InventarioService.class);

        if (referencia != null) {
            comprobarInventario(referencia);
        } else {
            System.out.println(
                "[alertas] Esperando el servicio de inventario..."
            );
        }
    }

    @Override
    public void serviceChanged(ServiceEvent evento) {

        if (evento.getType() == ServiceEvent.REGISTERED) {
            System.out.println(
                "[alertas] Servicio de inventario disponible nuevamente."
            );

            ServiceReference<InventarioService> referencia =
                    context.getServiceReference(InventarioService.class);

            if (referencia != null) {
                comprobarInventario(referencia);
            }
        }

        if (evento.getType() == ServiceEvent.UNREGISTERING) {
            System.out.println(
                "[alertas] Inventario no disponible temporalmente."
            );
            System.out.println(
                "[alertas] El bundle de alertas sigue activo."
            );
        }
    }

    private void comprobarInventario(
            ServiceReference<InventarioService> referencia) {

        InventarioService servicio = context.getService(referencia);

        if (servicio == null) {
            return;
        }

        try {
            int stockInicial = servicio.consultarStock(BICICLETA);

            System.out.println(
                "[alertas] Stock inicial MTB-001: " + stockInicial
            );

            if (stockInicial >= 5) {
                boolean venta = servicio.registrarVenta(BICICLETA, 3);

                if (venta) {
                    System.out.println(
                        "[alertas] Venta simulada: 3 bicicletas MTB."
                    );
                }
            }

            int stockActual = servicio.consultarStock(BICICLETA);

            System.out.println(
                "[alertas] Stock actual MTB-001: " + stockActual
            );

            if (stockActual >= 0 && stockActual <= STOCK_MINIMO) {
                System.out.println(
                    "[alertas] ALERTA: Stock bajo en MTB-001. " +
                    "Quedan " + stockActual + " unidades."
                );
            } else if (stockActual > STOCK_MINIMO) {
                System.out.println(
                    "[alertas] Stock suficiente de MTB-001."
                );
            } else {
                System.out.println(
                    "[alertas] Bicicleta no encontrada."
                );
            }

        } finally {
            context.ungetService(referencia);
        }
    }

    @Override
    public void stop(BundleContext context) {
        context.removeServiceListener(this);
        this.context = null;

        System.out.println("[alertas] Bundle de alertas detenido.");
    }
}
