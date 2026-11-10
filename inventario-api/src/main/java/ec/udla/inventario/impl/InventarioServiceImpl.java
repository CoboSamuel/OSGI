package ec.udla.inventario.impl;

import ec.udla.inventario.api.InventarioService;
import java.util.HashMap;
import java.util.Map;

public class InventarioServiceImpl implements InventarioService {

    private final Map<String, Integer> inventario = new HashMap<>();

    public InventarioServiceImpl() {
        inventario.put("MTB-001", 5);
        inventario.put("RUTA-002", 12);
    }

    @Override
    public synchronized int consultarStock(String codigo) {
        return inventario.getOrDefault(codigo, -1);
    }

    @Override
    public synchronized boolean registrarVenta(String codigo, int cantidad) {
        if (cantidad <= 0 || !inventario.containsKey(codigo)) {
            return false;
        }

        int disponible = inventario.get(codigo);

        if (disponible < cantidad) {
            return false;
        }

        inventario.put(codigo, disponible - cantidad);

        System.out.println(
            "[inventario] Venta realizada: " +
            cantidad + " unidades de " + codigo
        );

        return true;
    }
}
