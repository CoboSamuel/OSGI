package ec.udla.inventario.api;

public interface InventarioService {

    int consultarStock(String codigo);

    boolean registrarVenta(String codigo, int cantidad);

}
