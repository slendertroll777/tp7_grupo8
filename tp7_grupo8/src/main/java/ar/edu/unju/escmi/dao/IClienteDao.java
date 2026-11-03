package ar.edu.unju.escmi.dao;

import java.util.List;

import ar.edu.unju.escmi.entities.Cliente;

public interface IClienteDao {

    void guardarCliente(Cliente cliente);

    void modificarCliente(Cliente cliente);

    void eliminarCliente(Cliente cliente);

    Cliente buscarCliente(Long id);

    List<Cliente> obtenerClientes();
}
