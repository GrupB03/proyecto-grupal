package src;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;
import java.util.UUID;

public class GenerateInfoFiles {

    private static final String[] nombres = {"Juan", "María", "Pedro", "Ana", "Luis", "Carmen"};
    private static final String[] apellidos = {"Pérez", "González", "Rodríguez", "López", "Sánchez", "Martínez"};
    
    
    public static void createSalesMenFile(int randomSalesCount, String name, long id) throws IOException {
        String fileName = "sales_" + name + "_" + id + ".txt";
        try (FileWriter writer = new FileWriter(fileName)) {
            writer.write("CC;" + id + "\n");
            Random random = new Random();
            for (int i = 0; i < randomSalesCount; i++) {
                int productId = random.nextInt(100) + 1; 
                int quantitySold = random.nextInt(20) + 1; 
                writer.write(productId + ";" + quantitySold + ";\n");
            }
        }
        System.out.println("Archivo generado: " + fileName);
    }


    public static void createProductsFile(int productsCount) throws IOException {
        String fileName = "productos.txt";
        try (FileWriter writer = new FileWriter(fileName)) {
            Random random = new Random();
            for (int i = 0; i < productsCount; i++) {
                String productId = UUID.randomUUID().toString();
                String productName = "Producto_" + (i + 1);
                double price = 10 + (100 - 10) * random.nextDouble(); 
                writer.write(productId + ";" + productName + ";" + 
                String.format("%.2f", price) + "\n");
            }
        }
        System.out.println("Archivo generado: " + fileName);
    }

 
    public static void createSalesManInfoFile(int salesmanCount) throws IOException {
        String fileName = "vendedores.txt";
        try (FileWriter writer = new FileWriter(fileName)) {
            Random random = new Random();
            for (int i = 0; i < salesmanCount; i++) {
                String nombre = nombres[random.nextInt(nombres.length)];
                String apellido = apellidos[random.nextInt(apellidos.length)];
                long id = 1000000000L + random.nextInt(900000000); // ID aleatorio de 10 dígitos
                writer.write("CC;" + id + ";" + nombre + ";" + apellido + "\n");
            }
        }
        System.out.println("Archivo generado: " + fileName);
    }

    public static void main(String[] args) {
        try {
            createSalesManInfoFile(5);  
            createProductsFile(10);    
            createSalesMenFile(5, "Luis", 123456789);  
        } catch (IOException e) {
            System.err.println("Error al generar archivos: " + e.getMessage());
        }
    }
}
