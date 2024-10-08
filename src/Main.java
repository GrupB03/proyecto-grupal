package src;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

/**
 * Clase principal que procesa archivos de ventas, vendedores y productos,
 * generando reportes de ventas y productos vendidos.
 */
public class Main {

    private static final String VENDEDOR_FILE = "vendedores.txt"; // Archivo de información de vendedores
    private static final String PRODUCTOS_FILE = "productos.txt"; // Archivo de información de productos
    private static final String REPORT_FILE = "reporte_vendedores.csv"; // Archivo de reporte de ventas
    private static final String PRODUCT_REPORT_FILE = "reporte_productos.csv"; // Archivo de reporte de productos vendidos

    /**
     * Clase que representa un vendedor.
     */
    static class Vendedor {
        long id;
        String nombre;

        Vendedor(long id, String nombre) {
            this.id = id;
            this.nombre = nombre;
        }
    }

    /**
     * Clase que representa un producto.
     */
    static class Producto {
        String id;
        String nombre;
        double precio;

        Producto(String id, String nombre, double precio) {
            this.id = id;
            this.nombre = nombre;
            this.precio = precio;
        }
    }

    /**
     * Método que carga la información de los vendedores desde el archivo.
     * 
     * @return Lista de vendedores.
     * @throws IOException Si ocurre un error al leer el archivo.
     */
    private static List<Vendedor> loadVendedores() throws IOException {
        List<Vendedor> vendedores = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(VENDEDOR_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] partes = line.split(";");
                if (partes.length >= 4) {
                    long id = Long.parseLong(partes[1]);
                    String nombreCompleto = partes[2] + " " + partes[3];
                    vendedores.add(new Vendedor(id, nombreCompleto));
                }
            }
        }
        return vendedores;
    }

    /**
     * Método que carga la información de productos desde el archivo.
     * 
     * @return Lista de productos.
     * @throws IOException Si ocurre un error al leer el archivo.
     */
    private static List<Producto> loadProductos() throws IOException {
        List<Producto> productos = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(PRODUCTOS_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] partes = line.split(";");
                if (partes.length == 3) {
                    String id = partes[0];
                    String nombre = partes[1];
                    double precio = Double.parseDouble(partes[2]);
                    productos.add(new Producto(id, nombre, precio));
                }
            }
        }
        return productos;
    }

    /**
     * Método que procesa los archivos de ventas y genera reportes de vendedores y productos vendidos.
     * 
     * @param vendedores Lista de vendedores cargados.
     * @param productos Lista de productos cargados.
     * @throws IOException Si ocurre un error al escribir los archivos de reporte.
     */
    private static void processSales(List<Vendedor> vendedores, List<Producto> productos) throws IOException {
        double[] totalVentasPorVendedor = new double[vendedores.size()];
        int[] cantidadVendidaPorProducto = new int[productos.size()];
        Stream<Path> paths = Files.list(Paths.get("."));
        
        paths.filter(path -> path.getFileName().toString().startsWith("sales_")) // Filtrar archivos de ventas
        .forEach(path -> {
        	
        	try (BufferedReader reader = new BufferedReader(new FileReader(path.toString()))) {
                String line = reader.readLine(); 
                if (line != null) {
                	 String[] header = line.split(";");
                     if (header.length != 2) { // Verificar que la línea tenga el formato correcto
                         System.err.println("Error: Línea en formato incorrecto en el archivo de ventas: " + line);
                         return; // Salir del procesamiento de este archivo
                     }
                     String[] infoVendedor =line.split(";");
                     if (infoVendedor.length >= 2) {
                         long idVendedor = Long.parseLong(infoVendedor[1]);
                         int vendedorIndex = -1;
                         for (int i = 0; i < vendedores.size(); i++) {
                             if (vendedores.get(i).id == idVendedor) {
                                 vendedorIndex = i;
                                 break;
                             }
                         }
                         			
                         while ((line = reader.readLine()) != null) {
                             String[] partes = line.split(";");
                             if (partes.length != 2) { // Verificar que la línea tenga el formato correcto
                                 System.err.println("Error: Línea en formato incorrecto en el archivo de ventas: " + line);
                                 break; // Saltar a la siguiente línea
                             }
                             if (partes.length == 2) {
                                 String idProducto = partes[0];
                                 int cantidadVendida = Integer.parseInt(partes[1]);
                                 if (!isValidUUID(idProducto)) {
                                     System.err.println("Error: ID de producto no válido en la venta: " + idProducto);
                                     continue; // Saltar a la siguiente línea
                                 }
                                 
                                 if (cantidadVendida > 0) {
                                     // Buscar el índice del producto
                                     for (int j = 0; j < productos.size(); j++) {
                                         if (productos.get(j).id.equals(idProducto)) {
                                             totalVentasPorVendedor[vendedorIndex] += productos.get(j).precio * cantidadVendida;
                                             cantidadVendidaPorProducto[j] += cantidadVendida;
                                             break;
                                         }
                                     }
                                 } else {
                                     System.err.println("Error: Cantidad negativa vendida para el producto ID " + idProducto);
                                     continue;
                                 }
                             }
                         }
                     }
                     
                }
              
            } catch (IOException e) {
                System.err.println("Error al procesar archivo: " + path + " - " + e.getMessage());
            }
        } );
    
        // Generar reporte de vendedores
        try (FileWriter writer = new FileWriter(REPORT_FILE)) {
            for (int i = 0; i < vendedores.size(); i++) {
                writer.write(vendedores.get(i).nombre + ";" + totalVentasPorVendedor[i] + "\n");
            }
        }

        // Generar reporte de productos
        try (FileWriter writer = new FileWriter(PRODUCT_REPORT_FILE)) {
            for (int i = 0; i < productos.size(); i++) {
                writer.write(productos.get(i).nombre + ";" + productos.get(i).precio + ";" + cantidadVendidaPorProducto[i] + "\n");
            }
        }
    }
    
    /**
     * Método para validar si una cadena es un UUID válido.
     * 
     * @param uuid Cadena que se va a verificar.
     * @return true si es un UUID válido, false en caso contrario.
     */
    private static boolean isValidUUID(String uuid) {
        try {
            UUID.fromString(uuid); // Intenta convertir la cadena a un UUID
            return true; // Es un UUID válido
        } catch (IllegalArgumentException e) {
            return false; // No es un UUID válido
        }
    }
    
    /**
     * Método principal para ejecutar el procesamiento de ventas.
     * 
     * @param args Argumentos de línea de comandos (no se utilizan).
     */
    public static void main(String[] args) {
        try {
            List<Vendedor> vendedores = loadVendedores(); // Cargar información de vendedores
            List<Producto> productos = loadProductos(); // Cargar información de productos
            processSales(vendedores, productos); // Procesar ventas y generar reportes
            System.out.println("Reportes generados exitosamente.");
        } catch (IOException e) {
            System.err.println("Error al procesar archivos: " + e.getMessage());
        }
    }
}
