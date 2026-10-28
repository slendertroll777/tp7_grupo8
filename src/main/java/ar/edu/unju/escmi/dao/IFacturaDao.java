package ar.edu.unju.escmi.dao;

import java.util.List;

import ar.edu.unju.escmi.entities.Factura;

public interface IFacturaDao {

    void guardarFactura(Factura factura);

    Factura buscarFactura(Long id);

    void eliminarFactura(Factura factura);

    List<Factura> obtenerFacturas();

    List<Factura> obtenerFacturasMayoresA(double monto);
}
