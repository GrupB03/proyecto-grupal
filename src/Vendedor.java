package src;

public class Vendedor {
    private String tipoDocumento;
    private long numeroDocumento;
    private String nombres;
    private String apellidos;
    private double totalVentas;

    public Vendedor(String tipoDocumento, long numeroDocumento, String nombres, String apellidos) {
        this.tipoDocumento = tipoDocumento;
        this.numeroDocumento = numeroDocumento;
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.totalVentas = 0;
    }

    public void sumarVentas(double monto) {
        this.totalVentas += monto;
    }

    public double getTotalVentas() {
        return totalVentas;
    }

    public String getNombres() {
        return nombres;
    }

    public String getApellidos() {
        return apellidos;
    }
}