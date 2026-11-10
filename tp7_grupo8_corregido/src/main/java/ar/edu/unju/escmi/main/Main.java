package ar.edu.unju.escmi.main;

import java.util.List;
import java.util.Locale;
import java.util.Scanner;

import ar.edu.unju.escmi.config.EmfSingleton;
import ar.edu.unju.escmi.dao.IClienteDao;
import ar.edu.unju.escmi.dao.IFacturaDao;
import ar.edu.unju.escmi.dao.IProductoDao;
import ar.edu.unju.escmi.dao.impl.ClienteDaoImp;
import ar.edu.unju.escmi.dao.impl.FacturaDaoImp;
import ar.edu.unju.escmi.dao.impl.ProductoDaoImp;
import ar.edu.unju.escmi.entities.Cliente;
import ar.edu.unju.escmi.entities.DetalleFactura;
import ar.edu.unju.escmi.entities.Factura;
import ar.edu.unju.escmi.entities.Producto;

public final class Main {
    private static final Scanner SC = new Scanner(System.in);
    private static final IClienteDao CLIENTES = new ClienteDaoImp();
    private static final IProductoDao PRODUCTOS = new ProductoDaoImp();
    private static final IFacturaDao FACTURAS = new FacturaDaoImp();

    private Main() { }

    public static void main(String[] args) {
        try {
            // Comprobacion inicial: si falla, mostrar un mensaje claro en vez de ocultarlo.
            EmfSingleton.getInstance().getEmf();
            ejecutarMenu();
        } catch (Exception e) {
            System.err.println("No se pudo iniciar JPA/MySQL: " + mensajeRaiz(e));
            System.err.println("Revise MySQL, la base persistencebd y las credenciales en persistence.xml.");
        } finally {
            EmfSingleton.getInstance().cerrar();
        }
    }

    private static void ejecutarMenu() {
        boolean salir = false;
        while (!salir) {
            mostrarMenu();
            int opcion;
            try {
                opcion = leerEntero("Ingrese una opcion: ");
            } catch (IllegalStateException e) {
                break; // EOF al usar entrada redirigida
            }
            try {
                switch (opcion) {
                    case 1 -> altaCliente();
                    case 2 -> altaProducto();
                    case 3 -> realizarVenta();
                    case 4 -> buscarFactura();
                    case 5 -> eliminarFactura();
                    case 6 -> eliminarProducto();
                    case 7 -> modificarCliente();
                    case 8 -> modificarPrecio();
                    case 9 -> eliminarCliente();
                    case 10 -> mostrarFacturas();
                    case 11 -> mostrarClientes();
                    case 12 -> mostrarFacturasMayores();
                    case 13 -> { salir = true; System.out.println("Saliendo del programa..."); }
                    default -> System.out.println("Opcion invalida (1 a 13).");
                }
            } catch (Exception e) {
                System.out.println("No se pudo completar la operacion: " + mensajeRaiz(e));
            }
            if (!salir) {
                System.out.print("\nPresione ENTER para continuar...");
                try { SC.nextLine(); } catch (Exception e) { break; }
            }
        }
        System.out.println("Programa finalizado.");
    }

    private static String mensajeRaiz(Throwable t) {
        while (t.getCause() != null) t = t.getCause();
        return t.getMessage() == null ? t.getClass().getSimpleName() : t.getMessage();
    }

    private static void mostrarMenu() {
        System.out.println("\n========== MENU PRINCIPAL ==========");
        System.out.println("1 - Alta de cliente");
        System.out.println("2 - Alta de producto");
        System.out.println("3 - Realizar venta (nueva factura)");
        System.out.println("4 - Buscar factura");
        System.out.println("5 - Eliminar factura (logico)");
        System.out.println("6 - Eliminar producto (logico)");
        System.out.println("7 - Modificar datos de cliente");
        System.out.println("8 - Modificar precio de producto");
        System.out.println("9 - Eliminar cliente (logico)");
        System.out.println("10 - Mostrar todas las facturas");
        System.out.println("11 - Mostrar todos los clientes");
        System.out.println("12 - Mostrar facturas mayores a $900.000");
        System.out.println("13 - Salir");
        System.out.println("====================================");
    }

    private static String linea(String mensaje) {
        System.out.print(mensaje);
        if (!SC.hasNextLine()) throw new IllegalStateException("Fin de entrada");
        return SC.nextLine().trim();
    }

    private static int leerEntero(String mensaje) {
        while (true) {
            try { return Integer.parseInt(linea(mensaje)); }
            catch (NumberFormatException e) { System.out.println("Ingrese un numero entero valido."); }
        }
    }

    private static long leerId(String mensaje) {
        while (true) {
            try {
                long id = Long.parseLong(linea(mensaje));
                if (id > 0) return id;
            } catch (NumberFormatException e) { /* volver a preguntar */ }
            System.out.println("El identificador debe ser positivo.");
        }
    }

    private static int leerPositivo(String mensaje) {
        int n;
        do {
            n = leerEntero(mensaje);
            if (n <= 0) System.out.println("Debe ingresar un entero mayor a cero.");
        } while (n <= 0);
        return n;
    }

    private static double leerPrecio(String mensaje) {
        while (true) {
            String texto = linea(mensaje).replace(',', '.');
            try {
                double valor = Double.parseDouble(texto);
                if (Double.isFinite(valor) && valor > 0) return valor;
            } catch (NumberFormatException e) { /* volver a preguntar */ }
            System.out.println("El precio debe ser un numero mayor a cero.");
        }
    }

    private static String leerTexto(String mensaje) {
        while (true) {
            String texto = linea(mensaje);
            if (!texto.isEmpty()) return texto;
            System.out.println("El campo no puede estar vacio.");
        }
    }

    private static boolean confirmar(String mensaje) {
        return linea(mensaje + " (s/N): ").equalsIgnoreCase("s");
    }

    private static void altaCliente() {
        System.out.println("\n--- ALTA DE CLIENTE ---");
        Cliente c = new Cliente();
        c.setNombre(leerTexto("Nombre: "));
        c.setApellido(leerTexto("Apellido: "));
        c.setDomicilio(leerTexto("Domicilio: "));
        c.setDni(leerPositivo("DNI: "));
        CLIENTES.guardarCliente(c);
        System.out.println("Cliente registrado. ID: " + c.getId());
    }

    private static void altaProducto() {
        System.out.println("\n--- ALTA DE PRODUCTO ---");
        Producto p = new Producto();
        p.setDescripcion(leerTexto("Descripcion: "));
        p.setPrecioUnitario(leerPrecio("Precio unitario: $"));
        PRODUCTOS.guardarProducto(p);
        System.out.println("Producto registrado. ID: " + p.getId());
    }

    private static void listarProductos() {
        List<Producto> productos = PRODUCTOS.obtenerProductos();
        System.out.println("\nProductos activos:");
        for (Producto p : productos) {
            System.out.printf(Locale.US, "  ID %d - %s - $%.2f%n", p.getId(), p.getDescripcion(), p.getPrecioUnitario());
        }
    }

    private static void listarClientes() {
        System.out.println("\nClientes activos:");
        for (Cliente c : CLIENTES.obtenerClientes()) {
            System.out.printf("  ID %d - %s %s - DNI %d%n", c.getId(), c.getNombre(), c.getApellido(), c.getDni());
        }
    }

    private static void realizarVenta() {
        System.out.println("\n--- REALIZAR VENTA ---");
        if (CLIENTES.obtenerClientes().isEmpty() || PRODUCTOS.obtenerProductos().isEmpty()) {
            System.out.println("Primero debe registrar al menos un cliente y un producto activos.");
            return;
        }
        listarClientes();
        Cliente cliente = CLIENTES.buscarCliente(leerId("ID del cliente: "));
        if (cliente == null || !cliente.isEstado()) {
            System.out.println("Cliente inexistente o dado de baja.");
            return;
        }
        Factura factura = new Factura();
        factura.setCliente(cliente);
        factura.setDomicilio(cliente.getDomicilio()); // conservar domicilio al facturar
        do {
            listarProductos();
            Producto p = PRODUCTOS.buscarProducto(leerId("ID del producto: "));
            if (p == null || !p.isEstado()) {
                System.out.println("Producto inexistente o dado de baja.");
                continue;
            }
            int cantidad = leerPositivo("Cantidad: ");
            double subtotal = cantidad * p.getPrecioUnitario();
            if (!Double.isFinite(subtotal)) {
                System.out.println("Importe demasiado grande; el producto no se agrego.");
                continue;
            }
            DetalleFactura detalle = new DetalleFactura();
            detalle.setProducto(p);
            detalle.setCantidad(cantidad);
            detalle.setPrecioVenta(p.getPrecioUnitario());
            detalle.setSubtotal(subtotal);
            factura.agregarDetalle(detalle);
            System.out.printf(Locale.US, "Subtotal agregado: $%.2f%n", subtotal);
        } while (confirmar("Agregar otro producto"));
        if (factura.getDetalles().isEmpty()) {
            System.out.println("Venta cancelada: no hay productos.");
            return;
        }
        double total = 0;

for (DetalleFactura detalle : factura.getDetalles()) {
    total += detalle.getSubtotal();
}
        if (!Double.isFinite(total)) {
            System.out.println("Total fuera de rango. Venta cancelada.");
            return;
        }
        factura.setTotal(total);
        System.out.printf(Locale.US, "Total de la venta: $%.2f%n", total);
        if (!confirmar("Confirmar y guardar factura")) {
            System.out.println("Venta cancelada.");
            return;
        }
        FACTURAS.guardarFactura(factura);
        System.out.println("Factura guardada con numero " + factura.getId());
        imprimirFactura(factura);
    }

    private static void buscarFactura() {
        Factura f = FACTURAS.buscarFactura(leerId("Numero de factura: "));
        if (f == null || !f.isEstado()) System.out.println("Factura activa no encontrada.");
        else imprimirFactura(f);
    }

    private static void eliminarFactura() {
        Factura f = FACTURAS.buscarFactura(leerId("Numero de factura a eliminar: "));
        if (f == null || !f.isEstado()) { System.out.println("Factura activa no encontrada."); return; }
        if (confirmar("Dar de baja la factura " + f.getId())) {
            FACTURAS.eliminarFactura(f);
            System.out.println("Factura dada de baja (no se borro de la base).");
        }
    }

    private static void eliminarProducto() {
        listarProductos();
        Producto p = PRODUCTOS.buscarProducto(leerId("ID del producto a eliminar: "));
        if (p == null || !p.isEstado()) { System.out.println("Producto activo no encontrado."); return; }
        if (confirmar("Dar de baja " + p.getDescripcion())) {
            PRODUCTOS.eliminarProducto(p);
            System.out.println("Producto dado de baja.");
        }
    }

    private static void modificarCliente() {
        listarClientes();
        Cliente c = CLIENTES.buscarCliente(leerId("ID del cliente a modificar: "));
        if (c == null || !c.isEstado()) { System.out.println("Cliente activo no encontrado."); return; }
        System.out.println("Ingrese los nuevos datos:");
        c.setNombre(leerTexto("Nombre: "));
        c.setApellido(leerTexto("Apellido: "));
        c.setDomicilio(leerTexto("Domicilio: "));
        c.setDni(leerPositivo("DNI: "));
        CLIENTES.modificarCliente(c);
        System.out.println("Cliente actualizado.");
    }

    private static void modificarPrecio() {
        listarProductos();
        Producto p = PRODUCTOS.buscarProducto(leerId("ID del producto: "));
        if (p == null || !p.isEstado()) { System.out.println("Producto activo no encontrado."); return; }
        p.setPrecioUnitario(leerPrecio("Nuevo precio: $"));
        PRODUCTOS.modificarProducto(p);
        System.out.println("Precio actualizado.");
    }

    private static void eliminarCliente() {
        listarClientes();
        Cliente c = CLIENTES.buscarCliente(leerId("ID del cliente a eliminar: "));
        if (c == null || !c.isEstado()) { System.out.println("Cliente activo no encontrado."); return; }
        if (confirmar("Dar de baja a " + c.getNombre() + " " + c.getApellido())) {
            CLIENTES.eliminarCliente(c);
            System.out.println("Cliente dado de baja.");
        }
    }

    private static void mostrarFacturas() {
        List<Factura> facturas = FACTURAS.obtenerFacturas();
        if (facturas.isEmpty()) { System.out.println("No hay facturas activas."); return; }
        for (Factura f : facturas) imprimirFactura(f);
    }

    private static void mostrarClientes() {
        if (CLIENTES.obtenerClientes().isEmpty()) { System.out.println("No hay clientes activos."); return; }
        listarClientes();
    }

    private static void mostrarFacturasMayores() {
        List<Factura> facturas = FACTURAS.obtenerFacturasMayoresA(900000.0);
        if (facturas.isEmpty()) { System.out.println("No hay facturas mayores a $900.000."); return; }
        for (Factura f : facturas) imprimirFactura(f);
    }

    private static void imprimirFactura(Factura f) {
        System.out.println("\n============== FACTURA ==============");
        System.out.println("Numero: " + f.getId() + " | Fecha: " + f.getFecha());
        Cliente c = f.getCliente();
        if (c != null) System.out.println("Cliente: " + c.getNombre() + " " + c.getApellido() + " | DNI: " + c.getDni());
        System.out.println("Domicilio de facturacion: " + f.getDomicilio());
        System.out.println("-------------------------------------");
        for (DetalleFactura d : f.getDetalles()) {
            Producto p = d.getProducto();
            String descripcion = p == null ? "Producto no disponible" : p.getDescripcion();
            double precioHistorico = d.getPrecioVenta() != null ? d.getPrecioVenta()
                    : d.getSubtotal() / d.getCantidad(); // facturas previas a la migracion
            System.out.printf(Locale.US, "%s | Cantidad: %d | Unitario: $%.2f | Subtotal: $%.2f%n",
                    descripcion, d.getCantidad(), precioHistorico, d.getSubtotal());
        }
        System.out.printf(Locale.US, "TOTAL: $%.2f%n", f.getTotal());
        System.out.println("=====================================");
    }
}
