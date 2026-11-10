
package ar.edu.unju.escmi.dao.impl;

import java.util.List;

import ar.edu.unju.escmi.config.EmfSingleton;
import ar.edu.unju.escmi.dao.IFacturaDao;
import ar.edu.unju.escmi.entities.DetalleFactura;
import ar.edu.unju.escmi.entities.Factura;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;

public class FacturaDaoImp implements IFacturaDao {

    @Override
    public void guardarFactura(Factura factura) {

        EntityManager em = EmfSingleton.getInstance()
                .getEmf().createEntityManager();

        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();
            if (factura == null || factura.getCliente() == null || factura.getDetalles().isEmpty()) {
                throw new IllegalArgumentException("La factura debe tener cliente y detalles.");
            }
           for (DetalleFactura detalle : factura.getDetalles()) {
    detalle.setFactura(factura);
}
            em.persist(factura);
            tx.commit();

        } catch (Exception e) {

            if (tx.isActive()) {
                tx.rollback();
            }

            throw new RuntimeException("Error al guardar la factura.", e);

        } finally {
            em.close();
        }
    }

    @Override
    public Factura buscarFactura(Long id) {

        EntityManager em = EmfSingleton.getInstance()
                .getEmf().createEntityManager();

        try {

            return em.createQuery(
                    "SELECT DISTINCT f FROM Factura f " +
                    "LEFT JOIN FETCH f.cliente " +
                    "LEFT JOIN FETCH f.detalles d " +
                    "LEFT JOIN FETCH d.producto " +
                    "WHERE f.id = :id",
                    Factura.class)
                    .setParameter("id", id)
                    .getSingleResult();

        } catch (jakarta.persistence.NoResultException e) {
            return null;

        } finally {
            em.close();
        }
    }

    @Override
    public void eliminarFactura(Factura factura) {

        EntityManager em = EmfSingleton.getInstance()
                .getEmf().createEntityManager();

        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();

            Factura facturaEncontrada =
                    em.find(Factura.class, factura.getId());

            if (facturaEncontrada != null) {
                facturaEncontrada.setEstado(false);
                em.merge(facturaEncontrada);
            }

            tx.commit();

        } catch (Exception e) {

            if (tx.isActive()) {
                tx.rollback();
            }

            throw new RuntimeException("Error al eliminar la factura.", e);

        } finally {
            em.close();
        }
    }

    @Override
    public List<Factura> obtenerFacturas() {

        EntityManager em = EmfSingleton.getInstance()
                .getEmf().createEntityManager();

        try {

            return em.createQuery(
                    "SELECT DISTINCT f FROM Factura f " +
                    "LEFT JOIN FETCH f.cliente " +
                    "LEFT JOIN FETCH f.detalles d " +
                    "LEFT JOIN FETCH d.producto " +
                    "WHERE f.estado = true",
                    Factura.class)
                    .getResultList();

        } finally {
            em.close();
        }
    }

    @Override
    public List<Factura> obtenerFacturasMayoresA(double monto) {

        EntityManager em = EmfSingleton.getInstance()
                .getEmf().createEntityManager();

        try {

            return em.createQuery(
                    "SELECT DISTINCT f FROM Factura f " +
                    "LEFT JOIN FETCH f.cliente " +
                    "LEFT JOIN FETCH f.detalles d " +
                    "LEFT JOIN FETCH d.producto " +
                    "WHERE f.total > :monto AND f.estado = true",
                    Factura.class)
                    .setParameter("monto", monto)
                    .getResultList();

        } finally {
            em.close();
        }
    }
}
