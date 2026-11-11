package ar.edu.unju.escmi.dao.impl;

import java.util.List;

import ar.edu.unju.escmi.config.EmfSingleton;
import ar.edu.unju.escmi.dao.IDetalleFacturaDao;
import ar.edu.unju.escmi.entities.DetalleFactura;
import jakarta.persistence.EntityManager;

public class DetalleFacturaDaoImp implements IDetalleFacturaDao {

    @Override
    public List<DetalleFactura> obtenerDetallesPorFactura(Long idFactura) {

        EntityManager em = EmfSingleton.getInstance()
                .getEmf().createEntityManager();

        try {
            // La relacion es unidireccional Factura -> DetalleFactura,
            // por eso se recorre desde la factura.
            return em.createQuery(
                    "SELECT d FROM Factura f JOIN f.detalles d WHERE f.id = :idFactura",
                    DetalleFactura.class)
                    .setParameter("idFactura", idFactura)
                    .getResultList();

        } catch (Exception e) {
            throw new RuntimeException("Error al obtener los detalles de la factura.", e);

        } finally {
            em.close();
        }
    }
}
