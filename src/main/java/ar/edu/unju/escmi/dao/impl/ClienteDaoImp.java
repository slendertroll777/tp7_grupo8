package ar.edu.unju.escmi.dao.impl;

import java.util.List;

import ar.edu.unju.escmi.config.EmfSingleton;
import ar.edu.unju.escmi.dao.IClienteDao;
import ar.edu.unju.escmi.entities.Cliente;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;

public class ClienteDaoImp implements IClienteDao {

    @Override
    public void guardarCliente(Cliente cliente) {
        EntityManager em = EmfSingleton.getInstance().getEmf().createEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();
            em.persist(cliente);
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
    public void modificarCliente(Cliente cliente) {
        EntityManager em = EmfSingleton.getInstance().getEmf().createEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();
            em.merge(cliente);
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
    public void eliminarCliente(Cliente cliente) {
        EntityManager em = EmfSingleton.getInstance().getEmf().createEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();

            Cliente clienteEncontrado = em.find(Cliente.class, cliente.getId());

            if (clienteEncontrado != null) {
                clienteEncontrado.setEstado(false);
                em.merge(clienteEncontrado);
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
    public Cliente buscarCliente(Long id) {
        EntityManager em = EmfSingleton.getInstance().getEmf().createEntityManager();

        try {
            return em.find(Cliente.class, id);
        } finally {
            em.close();
        }
    }

    @Override
    public List<Cliente> obtenerClientes() {
        EntityManager em = EmfSingleton.getInstance().getEmf().createEntityManager();

        try {
            return em.createQuery(
                    "SELECT c FROM Cliente c WHERE c.estado = true",
                    Cliente.class).getResultList();
        } finally {
            em.close();
        }
    }
}
