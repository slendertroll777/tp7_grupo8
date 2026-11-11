package ar.edu.unju.escmi.dao.impl;

import java.util.List;

import ar.edu.unju.escmi.config.EmfSingleton;
import ar.edu.unju.escmi.dao.IFacturaDao;
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
            if (factura == null || factura.getCliente() == null || factura.getDetalles().isEmpty()) {
                throw new IllegalArgumentException("La factura debe tener cliente y al menos un detalle.");
            }

            tx.begin();
            // Los detalles se guardan por cascada (composicion).
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
            return em.find(Factura.class, id);

        } catch (Exception e) {
            throw new RuntimeException("Error al buscar la factura.", e);

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

            Factura facturaEncontrada = em.find(Factura.class, factura.getId());

            if (facturaEncontrada != null) {
                facturaEncontrada.setEstado(false);
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
                    "SELECT f FROM Factura f WHERE f.estado = true",
                    Factura.class).getResultList();

        } catch (Exception e) {
            throw new RuntimeException("Error al obtener las facturas.", e);

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
                    "SELECT f FROM Factura f WHERE f.total > :monto AND f.estado = true",
                    Factura.class)
                    .setParameter("monto", monto)
                    .getResultList();

        } catch (Exception e) {
            throw new RuntimeException("Error al obtener las facturas.", e);

        } finally {
            em.close();
        }
    }
}
