package modelo;

import java.io.Serializable;
import java.time.LocalDate;
import modelo.actividades.Actividad;

public class Inscripcion implements Serializable { //IMPORTANTE AGREGAR IMPLEMENTS SERIALIZABLE A INSCRIPCIÓN
    private Actividad actividad;
    private Estudiante estudiante;
    private LocalDate fecha;
    private String estado;

    public Inscripcion (Actividad actividad, Estudiante estudiante, LocalDate fecha, String estado) {
        this.actividad = actividad;
        this.estudiante = estudiante;
        this.fecha = fecha;
        this.estado = estado;
    }

    public Actividad getActividad() {
        return actividad;
    }

    public Estudiante getEstudiante() {
        return estudiante;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public String getEstado() {
        return estado;
    }

    public void confirmar() {
        this.estado = "CONFIRMADO";
    }
}
