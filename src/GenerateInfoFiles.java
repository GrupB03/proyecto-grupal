package src;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

/**
 * Clase para generar archivos de información de vendedores, productos y ventas.
 */
public class GenerateInfoFiles {

    private static final String[] nombres = {"Juan", "María", "Pedro", "Ana", "Luis", "Carmen"};
    private static final String[] apellidos = {"Pérez", "González", "Rodríguez", "López", "Sánchez", "Martínez"};
    
    private static List<String> productIds = new ArrayList<>(); // Lista para almacenar los IDs de los productos

    /**
     * Método para crear el archivo de ventas de un vendedor.
     * 
     * @param randomSalesCount Cantidad de ventas a generar.
     * @param id Identificación del vendedor.
     * @param nombre Nombre del vendedor.
     * @throws IOException Si ocurre un error al escribir en el archivo.
     */
    public static void createSalesMenFile(int randomSalesCount, String nombre, long id) throws IOException {
        String fileName = "sales_" + nombre + "_" + id + ".txt";
        try (FileWriter writer = new FileWriter(fileName)) {
            writer.write("CC;" + id + "\n");
            Random random = new Random();
            for (int i = 0; i < randomSalesCount; i++) {
                // Usar un ID de producto aleatorio de la lista generada
                String productId = productIds.get(random.nextInt(productIds.size())); 
                int quantitySold = random.nextInt(20) + 1; // Cantidad vendida entre 1 y 20
                writer.write(productId + ";" + quantitySold + ";\n");
            }
        }
        System.out.println("Archivo generado: " + fileName);
    }

    /**
     * Método para crear un archivo de información de productos.
     * 
     * @param productsCount Cantidad de productos a generar.
     * @throws IOException Si ocurre un error al escribir en el archivo.
     */
    public static void createProductsFile(int productsCount) throws IOException {
        String fileName = "productos.txt";
        try (FileWriter writer = new FileWriter(fileName)) {
            Random random = new Random();
            for (int i = 0; i < productsCount; i++) {
                String productId = UUID.randomUUID().toString(); // ID de producto aleatorio
                String productName = "Producto_" + (i + 1);
                double price = 10 + (100 - 10) * random.nextDouble(); // Precio aleatorio entre 10 y 100
                writer.write(productId + ";" + productName + ";" + String.format("%.2f", price) + "\n");
                productIds.add(productId); // Agregar el ID del producto a la lista
            }
        }
        System.out.println("Archivo generado: " + fileName);
    }

    /**
     * Método para crear un archivo de información de vendedores.
     * 
     * @param salesmanCount Cantidad de vendedores a generar.
     * @throws IOException Si ocurre un error al escribir en el archivo.
     */
    public static void createSalesManInfoFile(int salesmanCount) throws IOException {
        String fileName = "vendedores.txt";
        try (FileWriter writer = new FileWriter(fileName)) {
            Random random = new Random();
            for (int i = 0; i < salesmanCount; i++) {
                String nombre = nombres[i];
                String apellido = apellidos[i];
                long id = 1000000000L + random.nextInt(900000000); // ID aleatorio de 10 dígitos
                writer.write("CC;" + id + ";" + nombre + ";" + apellido + "\n");
                createSalesMenFile(random.nextInt(10) + 1, nombre, id); // Crear archivo de ventas para cada vendedor
            }
        }
        System.out.println("Archivo generado: " + fileName);
    }

    /**
     * Método principal para ejecutar la generación de archivos.
     * 
     * @param args Argumentos de línea de comandos (no se utilizan).
     */
    public static void main(String[] args) {
        try {
            createProductsFile(10);     // Crear archivo de información de 10 productos
            createSalesManInfoFile(5); // Crear archivo de información de 5 vendedores
        } catch (IOException e) {
            System.err.println("Error al generar archivos: " + e.getMessage());
        }
    }
}
