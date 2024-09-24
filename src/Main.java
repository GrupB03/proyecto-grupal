package src;
import java.io.IOException;

public class Main {

    public static void main(String[] args) {
        try {
            ProcesadorDeArchivos procesador = new ProcesadorDeArchivos();
            procesador.procesarArchivosVendedores("vendedores.txt");
            procesador.procesarArchivosVentas("ruta_a_carpeta_con_ventas");
            procesador.procesarArchivosProductos("productos.txt");

            // Generar los reportes en CSV
            procesador.generarReporteVendedores("reporte_vendedores.csv");
            procesador.generarReporteProductos("reporte_productos.csv");

            System.out.println("Reportes generados correctamente.");
        } catch (IOException e) {
            System.err.println("Error procesando archivos: " + e.getMessage());
        }
    }
}