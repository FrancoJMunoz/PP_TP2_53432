package modelo.actividades;

import modelo.Estudiante;
import modelo.certificacion.Certificable;

public class Taller extends Actividad implements Certificable {

    private boolean requiereNotebook;

    public Taller (int id, String titulo, int cupo, boolean requiereNotebook ) {
        super(id, titulo, cupo);
        this.requiereNotebook = requiereNotebook;
    }

    public boolean getRequiereNotebook() {
        return requiereNotebook;
    }

    public void setRequiereNotebook (boolean requiereNotebook) {
        this.requiereNotebook = requiereNotebook;
    }

    //POLIMORFISMO

    @Override
    public double calcularCostoMateriales() {
        if (requiereNotebook) {
            return 5000.0;
        }
        return 2000.0;
    }

    @Override
    public String getTipo() {
        return this.getClass().getSimpleName();
    }

    @Override
    public String generarCertificado(Estudiante estudiante) {
        String mensaje = "Certificado Emitido por: " + ENTIDAD_EMISORA +", se deja constancia de que "+ estudiante.getNombre() + " participó en el taller "+ getTitulo();
        return mensaje;
    }
}
