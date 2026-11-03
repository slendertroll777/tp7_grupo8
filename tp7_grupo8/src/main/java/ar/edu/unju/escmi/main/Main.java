
package ar.edu.unju.escmi.main;

import java.util.List;
import java.util.Scanner;

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

public class Main {

    private static Scanner scanner = new Scanner(System.in);

    private static IClienteDao clienteDao = new ClienteDaoImp();
    private static IProductoDao productoDao = new ProductoDaoImp();
    private static IFacturaDao facturaDao = new FacturaDaoImp();

    public static void main(String[] args) {

        int opcion;

        do {
            mostrarMenu();
            opcion = leerEntero("Ingrese una opcion: ");

            try {
                switch (opcion) {

                    case 1:
                        altaCliente();
                        break;

                    case 2:
                        altaProducto();
                        break;

                    case 3:
                        System.out.println("Opcion reservada para Guada: realizar venta.");
                        break;

                    case 4:
                        buscarFactura();
                        break;

                    case 5:
                        System.out.println("Opcion reservada para Guada: eliminar factura.");
                        break;

                    case 6:
                        System.out.println("Opcion reservada para Guada: eliminar producto.");
                        break;

                    case 7:
                        System.out.println("Opcion reservada para Guada: modificar cliente.");
                        break;

                    case 8:
                        System.out.println("Opcion reservada para Guada: modificar precio.");
                        break;

                    case 9:
                        System.out.println("Opcion reservada para Guada: eliminar cliente.");
                        break;

                    case 10:
                        mostrarFacturas();
                        break;

                    case 11:
                        mostrarClientes();
                        break;

                    case 12:
                        mostrarFacturasMayores();
                        break;

                    case 13:
                        System.out.println("Saliendo del programa...");
                        break;

                    default:
                        System.out.println("Opcion incorrecta. Ingrese un numero del 1 al 13.");
                }

            } catch (Exception e) {
                System.out.println("Error al realizar la operacion: " + e.getMessage());
            }

            if (opcion != 13) {
                System.out.println("\nPresione ENTER para continuar...");
                scanner.nextLine();
            }

        } while (opcion != 13);

        scanner.close();
        System.out.println("Programa finalizado.");
    }

    // MENU PRINCIPAL

    public static void mostrarMenu() {

        System.out.println("\n========== MENU PRINCIPAL ==========");
        System.out.println("1 - Alta de cliente");
        System.out.println("2 - Alta de producto");
        System.out.println("3 - Realizar venta");
        System.out.println("4 - Buscar factura");
        System.out.println("5 - Eliminar factura");
        System.out.println("6 - Eliminar producto");
        System.out.println("7 - Modificar datos de cliente");
        System.out.println("8 - Modificar precio de producto");
        System.out.println("9 - Eliminar cliente");
        System.out.println("10 - Mostrar todas las facturas");
        System.out.println("11 - Mostrar todos los clientes");
        System.out.println("12 - Mostrar facturas mayores a $900.000");
        System.out.println("13 - Salir");
        System.out.println("====================================");
    }

    // VALIDACION DE NUMEROS ENTEROS

    public static int leerEntero(String mensaje) {

        while (true) {
            System.out.print(mensaje);

            try {
                return Integer.parseInt(scanner.nextLine().trim());

            } catch (NumberFormatException e) {
                System.out.println("Error: debe ingresar un numero entero.");
            }
        }
    }

    // VALIDACION DE NUMEROS DECIMALES

    public static double leerDouble(String mensaje) {

        while (true) {
            System.out.print(mensaje);

            try {
                double numero = Double.parseDouble(
                        scanner.nextLine().trim().replace(",", "."));

                if (!Double.isFinite(numero)) {
                    System.out.println("Ingrese un numero valido.");
                    continue;
                }

                return numero;

            } catch (NumberFormatException e) {
                System.out.println("Error: debe ingresar un numero valido.");
            }
        }
    }

    // LECTURA DE TEXTO

    public static String leerTexto(String mensaje) {

        while (true) {
            System.out.print(mensaje);
            String texto = scanner.nextLine().trim();

            if (!texto.isEmpty()) {
                return texto;
            }

            System.out.println("El campo no puede estar vacio.");
        }
    }

    // OPCION 1: ALTA DE CLIENTE

    public static void altaCliente() {

        System.out.println("\n--- ALTA DE CLIENTE ---");

        Cliente cliente = new Cliente();

        cliente.setNombre(leerTexto("Nombre: "));
        cliente.setApellido(leerTexto("Apellido: "));
        cliente.setDomicilio(leerTexto("Domicilio: "));

        int dni;

        do {
            dni = leerEntero("DNI: ");

            if (dni <= 0) {
                System.out.println("El DNI debe ser positivo.");
            }

        } while (dni <= 0);

        cliente.setDni(dni);
        cliente.setEstado(true);

        clienteDao.guardarCliente(cliente);

        System.out.println("Cliente registrado correctamente.");
    }

    // OPCION 2: ALTA DE PRODUCTO

    public static void altaProducto() {

        System.out.println("\n--- ALTA DE PRODUCTO ---");

        Producto producto = new Producto();

        producto.setDescripcion(leerTexto("Descripcion del producto: "));

        double precio;

        do {
            precio = leerDouble("Precio unitario: $");

            if (precio <= 0) {
                System.out.println("El precio debe ser mayor a cero.");
            }

        } while (precio <= 0);

        producto.setPrecioUnitario(precio);
        producto.setEstado(true);

        productoDao.guardarProducto(producto);

        System.out.println("Producto registrado correctamente.");
    }

    // OPCION 4: BUSCAR FACTURA

    public static void buscarFactura() {

        System.out.println("\n--- BUSCAR FACTURA ---");

        int numero = leerEntero("Ingrese el numero de factura: ");

        if (numero <= 0) {
            System.out.println("El numero de factura debe ser positivo.");
            return;
        }

        Factura factura = facturaDao.buscarFactura((long) numero);

        if (factura == null || !factura.isEstado()) {
            System.out.println("No se encontro una factura activa con ese numero.");
            return;
        }

        imprimirFactura(factura);
    }

    // OPCION 10: MOSTRAR TODAS LAS FACTURAS

    public static void mostrarFacturas() {

        System.out.println("\n--- LISTADO DE FACTURAS ---");

        List<Factura> facturas = facturaDao.obtenerFacturas();

        if (facturas.isEmpty()) {
            System.out.println("No hay facturas registradas.");
            return;
        }

        for (Factura factura : facturas) {
            imprimirFactura(factura);
        }
    }

    // OPCION 11: MOSTRAR TODOS LOS CLIENTES

    public static void mostrarClientes() {

        System.out.println("\n--- LISTADO DE CLIENTES ---");

        List<Cliente> clientes = clienteDao.obtenerClientes();

        if (clientes.isEmpty()) {
            System.out.println("No hay clientes registrados.");
            return;
        }

        for (Cliente cliente : clientes) {

            System.out.println("\nNumero: " + cliente.getId());
            System.out.println("Nombre: " + cliente.getNombre());
            System.out.println("Apellido: " + cliente.getApellido());
            System.out.println("Domicilio: " + cliente.getDomicilio());
            System.out.println("DNI: " + cliente.getDni());
            System.out.println("----------------------------");
        }
    }

    // OPCION 12: FACTURAS MAYORES A $900.000

    public static void mostrarFacturasMayores() {

        System.out.println("\n--- FACTURAS MAYORES A $900.000 ---");

        List<Factura> facturas =
                facturaDao.obtenerFacturasMayoresA(900000);

        if (facturas.isEmpty()) {
            System.out.println("No hay facturas que superen los $900.000.");
            return;
        }

        for (Factura factura : facturas) {
            imprimirFactura(factura);
        }
    }

    // MOSTRAR TODOS LOS DATOS DE UNA FACTURA

    public static void imprimirFactura(Factura factura) {

        System.out.println("\n========== FACTURA ==========");

        System.out.println("Numero: " + factura.getId());
        System.out.println("Fecha: " + factura.getFecha());

        Cliente cliente = factura.getCliente();

        if (cliente != null) {
            System.out.println("Cliente: " + cliente.getNombre()
                    + " " + cliente.getApellido());
            System.out.println("DNI: " + cliente.getDni());
        }

        System.out.println("Domicilio: " + factura.getDomicilio());

        System.out.println("\n--- DETALLES ---");

        for (DetalleFactura detalle : factura.getDetalles()) {

            Producto producto = detalle.getProducto();

            if (producto != null) {
                System.out.println("Producto: " + producto.getDescripcion());
                System.out.println("Cantidad: " + detalle.getCantidad());
                System.out.println("Precio unitario: $"
                        + producto.getPrecioUnitario());
                System.out.println("Subtotal: $" + detalle.getSubtotal());
                System.out.println("----------------------------");
            }
        }

        System.out.println("TOTAL: $" + factura.getTotal());
        System.out.println("============================");
    }
}
