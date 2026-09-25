package modelo.actividades;

import modelo.Estudiante;
import modelo.certificacion.Certificable;

public class Curso extends Actividad implements Certificable {

    private int nivel;

    public Curso (int id, String titulo, int cupo, int nivel) {
        super(id, titulo, cupo);
        this.nivel = nivel;
    }

    @Override
    public String generarCertificado(Estudiante estudiante) {
        String mensaje = "Certificado Emitido por: " + ENTIDAD_EMISORA +", se deja constancia de que "+ estudiante.getNombre() + " participó en el taller "+ getTitulo();
        return mensaje;
    }

    @Override
    public double calcularCostoMateriales() {
        switch (nivel) {
            case 1:
                return 1000;
            case 2:
                return 2000;
            case 3:
                return 3000;
            case 4:
                return 4000;
            default:
                return 0.0;
        }
    }

    @Override
    public String getTipo() {
        return this.getClass().getSimpleName();
    }
}
