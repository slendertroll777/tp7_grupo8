package ar.edu.unju.escmi.dao;

import java.util.List;

import ar.edu.unju.escmi.entities.DetalleFactura;

// Los detalles se guardan y eliminan por cascada junto con su Factura (composicion),
// por eso este DAO solo necesita consultarlos.
public interface IDetalleFacturaDao {

    List<DetalleFactura> obtenerDetallesPorFactura(Long idFactura);
}
