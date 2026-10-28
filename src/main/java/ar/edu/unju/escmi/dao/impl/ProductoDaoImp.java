package ar.edu.unju.escmi.dao.impl;

import java.util.List;

import ar.edu.unju.escmi.config.EmfSingleton;
import ar.edu.unju.escmi.dao.IProductoDao;
import ar.edu.unju.escmi.entities.Producto;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;

public class ProductoDaoImp implements IProductoDao {

    @Override
    public void guardarProducto(Producto producto) {
        EntityManager em = EmfSingleton.getInstance().getEmf().createEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();
            em.persist(producto);
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
    public void modificarProducto(Producto producto) {
        EntityManager em = EmfSingleton.getInstance().getEmf().createEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();
            em.merge(producto);
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
    public void eliminarProducto(Producto producto) {
        EntityManager em = EmfSingleton.getInstance().getEmf().createEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();

            Producto productoEncontrado = em.find(Producto.class, producto.getId());

            if (productoEncontrado != null) {
                productoEncontrado.setEstado(false);
                em.merge(productoEncontrado);
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
    public Producto buscarProducto(Long id) {
        EntityManager em = EmfSingleton.getInstance().getEmf().createEntityManager();

        try {
            return em.find(Producto.class, id);
        } finally {
            em.close();
        }
    }

    @Override
    public List<Producto> obtenerProductos() {
        EntityManager em = EmfSingleton.getInstance().getEmf().createEntityManager();

        try {
            return em.createQuery(
                    "SELECT p FROM Producto p WHERE p.estado = true",
                    Producto.class).getResultList();
        } finally {
            em.close();
        }
    }
}
