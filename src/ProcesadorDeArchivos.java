package src;

import java.io.*;
import java.util.*;

public class ProcesadorDeArchivos {
    private Map<Long, Vendedor> vendedores = new HashMap<>();
    private Map<String, Producto> productos = new HashMap<>();
    private Map<String, Integer> ventasProductos = new HashMap<>();

    // Procesar archivo de vendedores
    public void procesarArchivosVendedores(String rutaArchivo) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(rutaArchivo));
        String linea;
        while ((linea = reader.readLine()) != null) {
            String[] partes = linea.split(";");
            long id = Long.parseLong(partes[1]);
            Vendedor vendedor = new Vendedor(partes[0], id, partes[2], partes[3]);
            vendedores.put(id, vendedor);
        }
        reader.close();
    }

    // Procesar archivos de ventas
    public void procesarArchivosVentas(String rutaCarpeta) throws IOException {
        File carpeta = new File(rutaCarpeta);
        File[] archivos = carpeta.listFiles((dir, name) -> name.endsWith(".txt"));

        if (archivos != null) {
            for (File archivo : archivos) {
                procesarArchivoVentas(archivo);
            }
        }
    }

    private void procesarArchivoVentas(File archivo) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(archivo));
        String linea = reader.readLine();
        String[] partes = linea.split(";");
        long idVendedor = Long.parseLong(partes[1]);

        while ((linea = reader.readLine()) != null) {
            partes = linea.split(";");
            String idProducto = partes[0];
            int cantidad = Integer.parseInt(partes[1]);

            ventasProductos.put(idProducto, ventasProductos.getOrDefault(idProducto, 0) + cantidad);
            
            Vendedor vendedor = vendedores.get(idVendedor);
            if (vendedor != null) {
                Producto producto = productos.get(idProducto);
                if (producto != null) {
                    vendedor.sumarVentas(cantidad * producto.getPrecio());
                }
            }
        }
        reader.close();
    }

    // Procesar archivo de productos
    public void procesarArchivosProductos(String rutaArchivo) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(rutaArchivo));
        String linea;
        while ((linea = reader.readLine()) != null) {
            String[] partes = linea.split(";");
            String idProducto = partes[0];
            String nombreProducto = partes[1];
            double precioProducto = Double.parseDouble(partes[2]);
            productos.put(idProducto, new Producto(idProducto, nombreProducto, precioProducto));
        }
        reader.close();
    }

    // Generar reporte de vendedores
    public void generarReporteVendedores(String rutaArchivo) throws IOException {
        List<Vendedor> listaVendedores = new ArrayList<>(vendedores.values());
        listaVendedores.sort(Comparator.comparingDouble(Vendedor::getTotalVentas).reversed());

        BufferedWriter writer = new BufferedWriter(new FileWriter(rutaArchivo));
        for (Vendedor vendedor : listaVendedores) {
            writer.write(vendedor.getNombres() + " " + vendedor.getApellidos() + ";" + vendedor.getTotalVentas());
            writer.newLine();
        }
        writer.close();
    }

    // Generar reporte de productos
    public void generarReporteProductos(String rutaArchivo) throws IOException {
        List<Map.Entry<String, Integer>> listaProductos = new ArrayList<>(ventasProductos.entrySet());
        listaProductos.sort(Map.Entry.comparingByValue(Comparator.reverseOrder()));

        BufferedWriter writer = new BufferedWriter(new FileWriter(rutaArchivo));
        for (Map.Entry<String, Integer> entry : listaProductos) {
            Producto producto = productos.get(entry.getKey());
            writer.write(producto.getNombre() + ";" + producto.getPrecio());
            writer.newLine();
        }
        writer.close();
    }
}