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
        EntityManager em = EmfSingleton.getInstance().getEmf().createEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();
            em.persist(factura);
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

    @Override
    public Factura buscarFactura(Long id) {
        EntityManager em = EmfSingleton.getInstance().getEmf().createEntityManager();

        try {
            return em.find(Factura.class, id);
        } finally {
            em.close();
        }
    }

    @Override
    public void eliminarFactura(Factura factura) {
        EntityManager em = EmfSingleton.getInstance().getEmf().createEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();

            Factura facturaEncontrada = em.find(Factura.class, factura.getId());

            if (facturaEncontrada != null) {
                facturaEncontrada.setEstado(false);
                em.merge(facturaEncontrada);
            }

            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

    @Override
    public List<Factura> obtenerFacturas() {
        EntityManager em = EmfSingleton.getInstance().getEmf().createEntityManager();

        try {
            return em.createQuery(
                    "SELECT f FROM Factura f WHERE f.estado = true",
                    Factura.class).getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public List<Factura> obtenerFacturasMayoresA(double monto) {
        EntityManager em = EmfSingleton.getInstance().getEmf().createEntityManager();

        try {
            return em.createQuery(
                    "SELECT f FROM Factura f WHERE f.total > :monto AND f.estado = true",
                    Factura.class)
                    .setParameter("monto", monto)
                    .getResultList();
        } finally {
            em.close();
        }
    }
}
