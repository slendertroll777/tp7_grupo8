package ar.edu.unju.escmi.dao;

import java.util.List;

import ar.edu.unju.escmi.entities.Producto;

public interface IProductoDao {

    void guardarProducto(Producto producto);

    void modificarProducto(Producto producto);

    void eliminarProducto(Producto producto);

    Producto buscarProducto(Long id);

    List<Producto> obtenerProductos();
}
