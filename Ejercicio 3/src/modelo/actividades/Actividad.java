package modelo.actividades;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import excepciones.CupoExcedidoException;
import modelo.Estudiante;
import modelo.Inscripcion;

public abstract class Actividad implements Serializable { //IMPORTANTE AGREGAR SERIALIZABLE EN ACTIVIDAD TAMBIÉN
    private int id;
    private String titulo;
    private int cupoMaximo;
    private List<Inscripcion> inscripciones;
    public static final int CUPO_MINIMO;
    static {
        CUPO_MINIMO = 2;
        System.out.println("Inicializador estático: se cargó la clase Actividad");
    }

    public Actividad (int id, String titulo, int cupo) {
        this.id = id;
        this.titulo = titulo;
        if (cupo < CUPO_MINIMO) {
            this.cupoMaximo = CUPO_MINIMO;
        } else {
            this.cupoMaximo = cupo;
        }
        this.inscripciones = new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        if(titulo == null || titulo.isBlank()) {
            return;
        }
        this.titulo = titulo;
    }

    public int getCupoMaximo() {
        return cupoMaximo;
    }

    public void setCupoMaximo(int cupo) {
        if (cupo < CUPO_MINIMO) {
            this.cupoMaximo = CUPO_MINIMO;
        } else {
            this.cupoMaximo = cupo;
        }
    }

    public Inscripcion inscribir(Estudiante estudiante) throws CupoExcedidoException {
        // Excepción añadida en TP2 Ejercicio 1
        if (inscripciones.size() >= cupoMaximo) {
            throw new CupoExcedidoException("No se puede inscribir al estudiante "+estudiante.getNombre()+" - Cupo máximo alcanzado");
        }
        Inscripcion inscripcion = new Inscripcion(this, estudiante, LocalDate.now(), "REGISTRADO");
        inscripciones.add(inscripcion);
        return inscripcion;
    } //Funciona como un setter para inscripcion

    public List<Inscripcion> getInscripciones() {
        return inscripciones;
    } //respectivo getter

    public void mostrarInscripciones() {
        if(inscripciones.isEmpty()) {
            System.out.println("Sin inscripciones registradas");
        } else {
            System.out.println("Inscripciones registradas: ");
            for (Inscripcion inscripcion : inscripciones) {
                System.out.println(" " + inscripcion.getFecha() + " - " + inscripcion.getEstado() + " - " + inscripcion.getEstudiante().getNombre() + " (Legajo: " + inscripcion.getEstudiante().getLegajo() + ")" );
            }

            /*
            Metodo alternativo usando un for en lugar de un for each
              for (int i = 0; i < inscripciones.size(); i++) {
                Inscripcion inscripcion = inscripciones.get(i);
                System.out.println("   " + inscripcion.getFecha() + " - " + inscripcion.getEstado() + " - " + inscripcion.getEstudiante().getNombre() + " (Legajo: " + inscripcion.getEstudiante().getLegajo() + ")");
              }
              */
        }
    }

    //Métodos añadidos en ejercicio 3

    public abstract double calcularCostoMateriales();

    public abstract String getTipo();

    public final void mostrarIdentificacion() {
        System.out.println("[" + id + "] " + getTipo() + ": "+ titulo  + " - Cupo: " + cupoMaximo);
    }

}
